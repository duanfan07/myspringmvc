package org.df.smartmvc.handler.argument;

import org.df.smartmvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: SmartMVC
 * @description: HttpServletRequest参数的解析器
 * @author: duanf
 * @create: 2021-03-19 16:56
 **/
public class ServletRequestMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> parameterType = parameter.getParameterType();
        //判断该类型是不是`ServletRequest`的子类，如果是返回true
        return ServletRequest.class.isAssignableFrom(parameterType);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request,
                                  HttpServletResponse response, ModelAndViewContainer container,
                                  ConversionService conversionService) throws Exception {
        return request;
    }
}
