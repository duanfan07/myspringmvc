package org.df.smartmvc.handler.argument;

import org.df.smartmvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.ui.Model;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: SmartMVC
 * @description: 解析出Model对象，方便后期对Handler中的Model参数进行注入
 * @author: duanf
 * @create: 2021-03-19 17:02
 **/
public class ModelMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //判断该类型是不是`Model`的子类，如果是返回true
        return Model.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request,
                                  HttpServletResponse response, ModelAndViewContainer container,
                                  ConversionService conversionService) throws Exception {
        Assert.state(container != null, "ModelAndViewContainer is required for model exposure");
        return container.getModel();
    }
}
