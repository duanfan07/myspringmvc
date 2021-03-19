package org.df.smartmvc.handler.mapping;

import org.df.smartmvc.annotation.RequestMapping;
import org.df.smartmvc.exception.NoHandlerFoundException;
import org.df.smartmvc.handler.HandlerExecutionChain;
import org.df.smartmvc.handler.HandlerMethod;
import org.df.smartmvc.handler.interceptor.HandlerInterceptor;
import org.df.smartmvc.handler.interceptor.MappedInterceptor;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
//本节我们实现的`RequestMappingHandlerMapping`，`HandlerMethod`相比SpringMVC都比较简单，大家可以对应着去看看SpringMVC中的实现；
// 因为在SpringMVC中不仅仅有`@RequestMapping`，还有基于xml配置的`SimpleUrlHandlerMapping`以及`BeanNameUrlHandlerMapping`等等，
// 所有`RequestMappingHandlerMapping`在SpringMVC中还有两层抽象类，有兴趣的小伙伴可以去看看每个实现。

/**因为在初始化的过程中，我们需要获取到容器中所有的Bean对象，所以`RequestMappingHandlerMapping`需要继承于`ApplicationObjectSupport`，
 `ApplicationObjectSupport`为我们提供了方便访问容器的方法；因为`RequestMappingHandlerMapping`需要在创建完对象后初始化`HandlerMethod`，
 所以实现了接口`InitializingBean`(提供了`afterPropertiesSet`方法，在对象创建完成后，spring容器会调用这个方法)，
 初始化代码的入口就在`afterPropertiesSet`中。
 **/
/**
 * @program: SmartMVC
 * @description: 这是初始化过程（获取handler的工具）
 * @author: duanf
 * @create: 2021-03-19 10:14
 **/
public class RequestMappingHandlerMapping extends ApplicationObjectSupport implements HandlerMapping, InitializingBean {

    private  MappingRegistry mappingRegistry = new MappingRegistry();
    //为什么我们还需要在`RequestMappingHandlerMapping`保存拦截器的集合呢？与`HandlerExecutionChain`中拦截器的集合有什么区别？
    //1. `RequestMappingHandlerMapping`中拦截器的集合包含了容器中所有的拦截器，而`HandlerExecutionChain`中拦截器集合只包含了匹配请求path的拦截器
    //2. `RequestMappingHandlerMapping`是获取Handler的工具，构建`HandlerExecutionChain`的过程中需要从所有拦截器中找到
    //与本次请求匹配的拦截器，所以把所有拦截器的集合放到`RequestMappingHandlerMapping`中是合理的
    private  List<MappedInterceptor> interceptors = new ArrayList<>();

    public void setInterceptors(List<MappedInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public MappingRegistry getMappingRegistry() {
        return mappingRegistry;
    }

    /***
     * @Description: 根据path得到此次请求的执行序列
     * @Author: duanfan
     * @Date: 2021/3/19 15:36
     * @Param request:
     * @return: org.df.smartmvc.handler.HandlerExecutionChain
    **/
    @Override
    public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        String lookupPath = request.getRequestURI();
        HandlerMethod handler = mappingRegistry.getHandlerMethodByPath(lookupPath);
        if (Objects.isNull(handler)){
            throw new NoHandlerFoundException(request);
        }
        return createHandlerExecutionChain(lookupPath, handler);
    }

    /**
     * @Description: 根据path创建此次请求的执行序列
     * @Author: duanfan
     * @Date: 2021/3/19 15:42
     * @Param lookupPath:
     * @Param handler:
     * @return: org.df.smartmvc.handler.HandlerExecutionChain
    **/
    private HandlerExecutionChain createHandlerExecutionChain(String lookupPath, HandlerMethod handler) {
        List<HandlerInterceptor> interceptors = this.interceptors.stream()
                .filter(mappedInterceptor -> mappedInterceptor.matches(lookupPath))
                .collect(Collectors.toList());
        return new HandlerExecutionChain(handler, interceptors);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initialHandlerMethods();
    }

    /***
     * @Description: 初始化处理器的方法
     * @Author: duanfan
     * @Date: 2021/3/19 11:01
     * @return: void
    **/
    private void initialHandlerMethods() {
        //首先我们需要从容器中拿出所有的Bean，这里我们用的是Spring提供的工具类，该方法将会返回beanName和bean实例对应的Map；
        Map<String, Object> beansOfMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(obtainApplicationContext(), Object.class);

        beansOfMap.entrySet().stream()
                //接着需要过滤出所有被标记`@Controller`的类
                .filter(entry -> this.isHandler(entry.getValue()))
                //解析出Controller中的所有方法，构建我们需要的`HandlerMethod`
                .forEach(entry -> this.detectHandlerMethods(entry.getKey(), entry.getValue()));
    }
    /**
     * @Description: 类上有@Controller注解的就是我找的handler
     * @Author: duanfan
     * @Date: 2021/3/19 11:03
     * @Param handler:
     * @return: boolean
    **/
    private boolean isHandler(Object handler) {
        Class<?> beanType = handler.getClass();
        //用到了Spring中的工具类`AnnotatedElementUtils.hasAnnotation`判断类是否有添加注解`@Controller`
        return (AnnotatedElementUtils.hasAnnotation(beanType, Controller.class));

    }

    /***
     * @Description: 查找handler中所有被@RequestMapping注解的方法并注册
     * @Author: duanfan
     * @Date: 2021/3/19 11:04
     * @Param beanName:
     * @Param handler:
     * @return: void
    **/
    private void detectHandlerMethods(String beanName, Object handler) {
        Class<?> beanType = handler.getClass();
        //用工具类`MethodIntrospector.selectMethods`找出Controller类中所有的方法
        //遍历每个方法，判断方法是否有添加注解`@RequestMapping`，如果没有就返回空，如果有就通过`@RequestMapping`构建`RequestMappingInfo`对象返回
        //如果所Controller类上有添加注解`@RequestMapping`，那么配的path将作为前缀
        Map<Method, RequestMappingInfo> methodsOfMap = MethodIntrospector.selectMethods(beanType,
                (MethodIntrospector.MetadataLookup<RequestMappingInfo>) method -> getMappingForMethod(method, beanType));
        //当所有的方法都解析完成之后，需要把所有配置有`@RequestMapping`注解的方法注册到`MappingRegistry`
        methodsOfMap.forEach((method, requestMappingInfo) -> this.mappingRegistry.register(requestMappingInfo, handler, method));
    }

    /**
     * @Description: 查找@RequestMapping注解的方法
     * @Author: duanfan
     * @Date: 2021/3/19 11:07
     * @Param method:
     * @Param beanType:
     * @return: org.df.smartmvc.handler.mapping.RequestMappingInfo
    **/
    private RequestMappingInfo getMappingForMethod(Method method, Class<?> beanType) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
        if (Objects.isNull(requestMapping)) {
            return null;
        }
        String prefix = getPathPrefix(beanType);
        return new RequestMappingInfo(prefix, requestMapping);

    }

    /**
     * @Description: 返回path
     * @Author: duanfan
     * @Date: 2021/3/19 11:08
     * @Param beanType:
     * @return: java.lang.String
    **/
    private String getPathPrefix(Class<?> beanType) {
        //如果所Controller类上有添加注解`@RequestMapping`，那么配的path将作为前缀
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(beanType, RequestMapping.class);
        if (Objects.isNull(requestMapping)){
            return "";
        }
        return requestMapping.path();
    }


}
