package org.df.smartmvc.handler;

import com.alibaba.fastjson.JSON;
import org.df.smartmvc.controller.TestInvocableHandlerMethodController;
import org.df.smartmvc.handler.argument.HandlerMethodArgumentResolverComposite;
import org.df.smartmvc.handler.argument.ModelMethodArgumentResolver;
import org.df.smartmvc.handler.argument.ServletRequestMethodArgumentResolver;
import org.df.smartmvc.handler.argument.ServletResponseMethodArgumentResolver;
import org.df.smartmvc.handler.returnvalue.HandlerMethodReturnValueHandlerComposite;
import org.df.smartmvc.handler.returnvalue.ViewNameMethodReturnValueHandler;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-22 14:01
 **/
//SpringMVC中的`ServletInvocableHandlerMethod`、`InvocableHandlerMethod`
public class InvocableHandlerMethodTest {
    @Test
    public void test1() throws Exception {
        TestInvocableHandlerMethodController controller = new TestInvocableHandlerMethodController();

        Method method = controller.getClass().getMethod("testRequestAndResponse",
                HttpServletRequest.class, HttpServletResponse.class);

        //初始化handlerMethod、HandlerMethodArgumentResolverComposite
        HandlerMethod handlerMethod = new HandlerMethod(controller, method);
        HandlerMethodArgumentResolverComposite argumentResolver = new HandlerMethodArgumentResolverComposite();
        argumentResolver.addResolver(new ServletRequestMethodArgumentResolver());
        argumentResolver.addResolver(new ServletResponseMethodArgumentResolver());

        //本测试用例中使用不到返回值处理器和转换器，所以传入null
        InvocableHandlerMethod inMethod = new InvocableHandlerMethod(handlerMethod, argumentResolver, null, null);

        ModelAndViewContainer mvContainer = new ModelAndViewContainer();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name", "Silently9527");
        MockHttpServletResponse response = new MockHttpServletResponse();

        inMethod.invokeAndHandle(request, response, mvContainer);

        System.out.println("输出到前端的内容:");
        System.out.println(response.getContentAsString());
    }
    @Test
    public void test2() throws Exception {
        TestInvocableHandlerMethodController controller = new TestInvocableHandlerMethodController();
        Method method = controller.getClass().getMethod("testViewName", Model.class);

        //初始化handlerMethod、HandlerMethodArgumentResolverComposite
        HandlerMethod handlerMethod = new HandlerMethod(controller, method);
        HandlerMethodArgumentResolverComposite argumentResolver = new HandlerMethodArgumentResolverComposite();
        argumentResolver.addResolver(new ModelMethodArgumentResolver());

        //由于testViewName的方法有返回值，所以需要设置返回值处理器
        HandlerMethodReturnValueHandlerComposite returnValueHandler = new HandlerMethodReturnValueHandlerComposite();
        returnValueHandler.addReturnValueHandler(new ViewNameMethodReturnValueHandler());

        InvocableHandlerMethod inMethod = new InvocableHandlerMethod(handlerMethod, argumentResolver, returnValueHandler, null);

        ModelAndViewContainer mvContainer = new ModelAndViewContainer();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        //执行调用
        inMethod.invokeAndHandle(request, response, mvContainer);

        System.out.println("ModelAndViewContainer:");
        System.out.println(JSON.toJSONString(mvContainer.getModel()));
        System.out.println("viewName: " + mvContainer.getViewName());
    }
}
