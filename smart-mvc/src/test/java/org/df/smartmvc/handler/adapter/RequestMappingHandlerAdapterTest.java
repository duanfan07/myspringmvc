package org.df.smartmvc.handler.adapter;

import com.alibaba.fastjson.JSON;
import org.df.smartmvc.ModelAndView;
import org.df.smartmvc.controller.TestInvocableHandlerMethodController;
import org.df.smartmvc.handler.HandlerMethod;
import org.junit.Test;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;

import java.lang.reflect.Method;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-22 14:43
 **/
//SpringMVC中`HandlerAdapter`有多个实现类，都有不同的使用方式，而`RequestMappingHandlerAdapter`是使用最多的一个
public class RequestMappingHandlerAdapterTest {
    @Test
    public void handle() throws Exception {
        TestInvocableHandlerMethodController controller = new TestInvocableHandlerMethodController();

        Method method = controller.getClass().getMethod("testViewName", Model.class);
        HandlerMethod handlerMethod = new HandlerMethod(controller, method);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        DateFormatter dateFormatter = new DateFormatter();
        dateFormatter.setPattern("yyyy-MM-dd HH:mm:ss");
        conversionService.addFormatter(dateFormatter);

        RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
        handlerAdapter.setConversionService(conversionService);
        handlerAdapter.afterPropertiesSet();

        ModelAndView modelAndView = handlerAdapter.handle(request, response, handlerMethod);

        System.out.println("modelAndView:");
        System.out.println(JSON.toJSONString(modelAndView));
    }
}
