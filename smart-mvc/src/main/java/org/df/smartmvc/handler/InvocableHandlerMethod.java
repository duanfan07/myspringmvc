package org.df.smartmvc.handler;

import org.df.smartmvc.handler.argument.HandlerMethodArgumentResolverComposite;
import org.df.smartmvc.handler.returnvalue.HandlerMethodReturnValueHandlerComposite;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @program: SmartMVC
 * @description: 用于处理用户的请求调用控制器方法，包装处理所需的各种参数和执行处理逻辑
 * @author: duanf
 * @create: 2021-03-22 11:25
 **/
//`InvocableHandlerMethod`需要继承`HandlerMethod`，因为调用控制器的方法需要知道实例以及调用那个方法，它是`HandlerMethod`的扩展
public class InvocableHandlerMethod extends HandlerMethod {

    //用来查找方法名的默认的实现
    private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    //参数的解析器
    private HandlerMethodArgumentResolverComposite argumentResolver;
    //返回值处理器
    private HandlerMethodReturnValueHandlerComposite returnValueHandler;
    //参数解析器中会用到数据的转换
    private ConversionService conversionService;

    public InvocableHandlerMethod(HandlerMethod handlerMethod,
                                  HandlerMethodArgumentResolverComposite argumentResolver,
                                  HandlerMethodReturnValueHandlerComposite returnValueHandler,
                                  ConversionService conversionService) {
        super(handlerMethod);
        this.argumentResolver = argumentResolver;
        this.returnValueHandler = returnValueHandler;
        this.conversionService = conversionService;
    }

    public InvocableHandlerMethod(Object bean,
                                  Method method,
                                  HandlerMethodArgumentResolverComposite argumentResolvers,
                                  HandlerMethodReturnValueHandlerComposite returnValueHandlers,
                                  ConversionService conversionService) {
        super(bean, method);
        this.argumentResolver = argumentResolver;
        this.returnValueHandler = returnValueHandler;
        this.conversionService = conversionService;
    }

    /**
     * @Description: 调用handler
     * @Author: duanfan
     * @Date: 2021/3/22 13:47
     * @Param request:
     * @Param response:
     * @Param modelAndViewContainer:
     * @return: void
    **/
    public void invokeAndHandle(HttpServletRequest request,
                                HttpServletResponse response,
                                ModelAndViewContainer modelAndViewContainer,
                                Object... providedArgs) throws Exception {
        //得到所有参数
        List<Object> args = this.getMethodArgumentValues(request, response, modelAndViewContainer, providedArgs);
        //反射调用控制器中的方法
        Object resultValue = doInvoke(args);
        //是否为空
        if (Objects.isNull(resultValue)) {
            //response是否已经提交
            if (response.isCommitted()) {
                modelAndViewContainer.setRequestHandled(true);
                return;
            } else {
                throw new IllegalStateException("Controller handler return value is null");
            }
        }

        modelAndViewContainer.setRequestHandled(false);
        Assert.state(this.returnValueHandler != null, "No return value handler");
        MethodParameter returnType = new MethodParameter(this.getMethod(), -1);  //-1表示方法的返回值
        //返回值处理器
        this.returnValueHandler.handleReturnValue(resultValue, returnType, modelAndViewContainer, request, response);
    }

    private Object doInvoke(List<Object> args) throws InvocationTargetException, IllegalAccessException {
        return this.getMethod().invoke(this.getBean(), args.toArray());
    }

    /**
     * @Description: 获取到方法的参数
     * @Author: duanfan
     * @Date: 2021/3/22 13:55
     * @Param request:
     * @Param response:
     * @Param modelAndViewContainer:
     * @return: java.util.List<java.lang.Object>
    **/
    private List<Object> getMethodArgumentValues(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 ModelAndViewContainer modelAndViewContainer,
                                                 Object... providedArgs) throws Exception {
        Assert.notNull(argumentResolver, "HandlerMethodArgumentResolver can not null");

        List<MethodParameter> parameters = this.getParameters();
        ArrayList<Object> args = new ArrayList<>(parameters.size());
        //遍历方法所有的参数，处理每个参数之前需要先调用`initParameterNameDiscovery`，然后在通过参数解析器去找到想要的参数
        for (MethodParameter parameter : parameters) {
            parameter.initParameterNameDiscovery(this.parameterNameDiscoverer);
            Object arg = findProvidedArgument(parameter, providedArgs);
            if (Objects.nonNull(arg)) {
                args.add(arg);
                continue;
            }
            args.add(argumentResolver.resolveArgument(parameter, request, response, modelAndViewContainer, conversionService));
        }
        return args;
    }

    protected static Object findProvidedArgument(MethodParameter parameter, Object[] providedArgs) {
        if (!ObjectUtils.isEmpty(providedArgs)) {
            for (Object providedArg : providedArgs) {
                if (parameter.getParameterType().isInstance(providedArg)) {
                    return providedArg;
                }
            }
        }
        return null;
    }

    public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }
}
