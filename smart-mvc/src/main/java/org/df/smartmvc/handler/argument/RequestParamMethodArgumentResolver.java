package org.df.smartmvc.handler.argument;

import org.df.smartmvc.annotation.RequestParam;
import org.df.smartmvc.exception.MissingServletRequestParameterException;
import org.df.smartmvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @program: SmartMVC
 * @description: RequestParam注解的实现
 * @author: duanf
 * @create: 2021-03-19 17:09
 **/
public class RequestParamMethodArgumentResolver implements HandlerMethodArgumentResolver {

    //判断Handler的参数是否有添加注解`@RequestParam`
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //是否采用RequestParam注解，采用了则支持
        return parameter.hasParameterAnnotation(RequestParam.class);
    }

    //从request中找指定name的参数，如果找不到用默认值赋值，如果默认值也没有，当required=true时抛出异常，
    //否知返回null; 如果从request中找到了参数值，那么调用`conversionService.convert`方法转换成正确的类型
    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request,
                                  HttpServletResponse response, ModelAndViewContainer container,
                                  ConversionService conversionService) throws Exception {
        RequestParam param = parameter.getParameterAnnotation(RequestParam.class);

        if (Objects.isNull(param)) {
            return null;
        }
        String value = request.getParameter(param.name());
        //没拿到值赋默认值
        if (StringUtils.isEmpty(value)) {
            value = param.defaultValue();
        }
        //值不为空转化
        if (!StringUtils.isEmpty(value)) {
            return conversionService.convert(value, parameter.getParameterType());
        }
        //前面如果没有返回，参数又必须，所以只能抛异常
        if (param.required()){
            throw  new MissingServletRequestParameterException(parameter.getParameterName(),
                    parameter.getParameterType().getName());
        }

        return null;
    }
}
