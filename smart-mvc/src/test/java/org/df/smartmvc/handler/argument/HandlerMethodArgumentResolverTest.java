package org.df.smartmvc.handler.argument;

import com.alibaba.fastjson.JSON;
import org.df.smartmvc.controller.TestController;
import org.df.smartmvc.handler.HandlerMethod;
import org.df.smartmvc.vo.UserVo;
import org.junit.Test;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-19 18:05
 **/
//本节我们开发的解析器只实现了参数的自动封装；而SpringMVC的参数解析器还包含了参数的校验等，并且SpringMVC已经提供了很丰富的解析器，
//比如：`PathVariableMethodArgumentResolver`、`SessionAttributeMethodArgumentResolver`、`ServletCookieValueMethodArgumentResolver`等等，建议都了解一下
public class HandlerMethodArgumentResolverTest {

    //测试注解RequestParam
    @Test
    public void test1() throws NoSuchMethodException {
        TestController testController = new TestController();
        Method method = testController.getClass().getMethod("test4",
                String.class, Integer.class, Date.class, HttpServletRequest.class);

        //构建HandlerMethod对象
        HandlerMethod handlerMethod = new HandlerMethod(testController, method);

        //构建模拟请求的request
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name", "Silently9527");
        request.setParameter("age", "25");
        request.setParameter("birthday", "2020-11-12 13:00:00");

        //添加支持的解析器
        HandlerMethodArgumentResolverComposite resolverComposite = new HandlerMethodArgumentResolverComposite();
        resolverComposite.addResolver(new RequestParamMethodArgumentResolver());
        resolverComposite.addResolver(new ServletRequestMethodArgumentResolver());

        //定义转换器
        //该类是Spring中的一个数据转换器服务，默认已经添加了很多转换器，
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        DateFormatter dateFormatter = new DateFormatter();
        dateFormatter.setPattern("yyyy-MM-dd HH:mm:ss");
        conversionService.addFormatter(dateFormatter);

        MockHttpServletResponse response = new MockHttpServletResponse();

        //用于查找方法参数名
        //该类是用于查找参数名的类，因为一般来说，通过反射是很难获得参数名的，只能取到参数类型，
        //因为在编译时，参数名有可能是会改变的，所以需要这样一个类，Spring已经实现了多种解析，我们这里直接引用就行
        DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
        handlerMethod.getParameters().forEach(methodParameter -> {
            try {
                methodParameter.initParameterNameDiscovery(parameterNameDiscoverer);

                Object value = resolverComposite.resolveArgument(methodParameter, request,response, null, conversionService);
                System.out.println(methodParameter.getParameterName() + " : " + value + "   type: " + value.getClass());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    //测试注解RequestBody
    @Test
    public void test2() throws NoSuchMethodException {
        TestController testController = new TestController();
        Method method = testController.getClass().getMethod("user", UserVo.class);

        HandlerMethod handlerMethod = new HandlerMethod(testController, method);

        MockHttpServletRequest request = new MockHttpServletRequest();
        UserVo userVo = new UserVo();
        userVo.setName("Silently9527");
        userVo.setAge(25);
        userVo.setBirthday(new Date());
        request.setContent(JSON.toJSONString(userVo).getBytes()); //模拟JSON参数

        HandlerMethodArgumentResolverComposite resolverComposite = new HandlerMethodArgumentResolverComposite();
        resolverComposite.addResolver(new RequestBodyMethodArgumentResolver());

        MockHttpServletResponse response = new MockHttpServletResponse();

        DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
        handlerMethod.getParameters().forEach(methodParameter -> {
            try {
                methodParameter.initParameterNameDiscovery(parameterNameDiscoverer);
                Object value = resolverComposite.resolveArgument(methodParameter, request, response, null, null);
                System.out.println(methodParameter.getParameterName() + " : " + value + "   type: " + value.getClass());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
