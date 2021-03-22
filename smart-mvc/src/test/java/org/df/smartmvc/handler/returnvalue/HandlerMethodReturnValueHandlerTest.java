package org.df.smartmvc.handler.returnvalue;

import org.df.smartmvc.controller.TestReturnValueController;
import org.df.smartmvc.handler.ModelAndViewContainer;
import org.df.smartmvc.view.View;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;

import java.lang.reflect.Method;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-22 11:16
 **/
//大家可以对应的去看看SpringMVC中`HandlerMethodReturnValueHandler`的实现类，了解SpringMVC支持哪些返回值处理
public class HandlerMethodReturnValueHandlerTest {

    @Test
    public void test() throws Exception {
        HandlerMethodReturnValueHandlerComposite composite = new HandlerMethodReturnValueHandlerComposite();
        composite.addReturnValueHandler(new ModelMethodReturnValueHandler());
        composite.addReturnValueHandler(new MapMethodReturnValueHandler());
        composite.addReturnValueHandler(new ResponseBodyMethodReturnValueHandler());
        composite.addReturnValueHandler(new ViewMethodReturnValueHandler());
        composite.addReturnValueHandler(new ViewNameMethodReturnValueHandler());

        ModelAndViewContainer mvContainer = new ModelAndViewContainer();
        TestReturnValueController controller = new TestReturnValueController();

        //测试方法testViewName
        Method viewNameMethod = controller.getClass().getMethod("testViewName");
        //从构造方法的注释我可以了解到，当`parameterIndex`等于-1的时候，表示构造方法返回值的`MethodParameter`
        MethodParameter viewNameMethodParameter = new MethodParameter(viewNameMethod, -1);
        composite.handleReturnValue(controller.testViewName(), viewNameMethodParameter, mvContainer, null, null);
        Assert.assertEquals(mvContainer.getViewName(), "/jsp/index.jsp");

        //测试方法testView
        Method viewMethod = controller.getClass().getMethod("testView");
        MethodParameter viewMethodParameter = new MethodParameter(viewMethod, -1);
        composite.handleReturnValue(controller.testView(), viewMethodParameter, mvContainer, null, null);
        Assert.assertTrue(mvContainer.getView() instanceof View);

        //测试方法testResponseBody
        Method responseBodyMethod = controller.getClass().getMethod("testResponseBody");
        MethodParameter resBodyMethodParameter = new MethodParameter(responseBodyMethod, -1);
        MockHttpServletResponse response = new MockHttpServletResponse();
        composite.handleReturnValue(controller.testResponseBody(), resBodyMethodParameter, mvContainer, null, response);
        System.out.println(response.getContentAsString());

        //测试方法testModel
        Method modelMethod = controller.getClass().getMethod("testModel", Model.class);
        MethodParameter modelMethodParameter = new MethodParameter(modelMethod, -1);
        composite.handleReturnValue(controller.testModel(mvContainer.getModel()), modelMethodParameter, mvContainer, null, null);
        Assert.assertEquals(mvContainer.getModel().getAttribute("testModel"), "Silently9527");

        //测试方法testMap
        Method mapMethod = controller.getClass().getMethod("testMap");
        MethodParameter mapMethodParameter = new MethodParameter(mapMethod, -1);
        composite.handleReturnValue(controller.testMap(), mapMethodParameter, mvContainer, null, null);
        Assert.assertEquals(mvContainer.getModel().getAttribute("testMap"), "Silently9527");

    }

}
