package org.df.smartmvc.handler.argument;

import com.sun.deploy.util.ArrayUtil;
import org.df.smartmvc.exception.MissingServletRequestParameterException;
import org.df.smartmvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-19 17:48
 **/
public class HandlerMethodArgumentResolverComposite implements HandlerMethodArgumentResolver{

    //内部定义List
    private  List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return true;
    }

    //在`resolveArgument`中循环所有的解析器，
    // 找到支持参数的解析器就开始解析，找不到就抛出异常
    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request,
                                  HttpServletResponse response, ModelAndViewContainer container,
                                  ConversionService conversionService) throws Exception {
        for (HandlerMethodArgumentResolver resolver : argumentResolvers) {
            if (resolver.supportsParameter(parameter)) {
                return resolver.resolveArgument(parameter, request, response, container, conversionService);
            }
        }
        throw new IllegalArgumentException("Unsupported parameter type [" +
                parameter.getParameterType().getName() + "]. supportsParameter should be called first.");
    }

    public void addResolver(HandlerMethodArgumentResolver resolver) {
        this.argumentResolvers.add(resolver);
    }
    public void addResolver(HandlerMethodArgumentResolver... resolvers) {
        Collections.addAll(this.argumentResolvers, resolvers);
    }

    public void addResolver(Collection<HandlerMethodArgumentResolver> resolvers) {
        this.argumentResolvers.addAll(resolvers);
    }

    public void clear() {
        this.argumentResolvers.clear();
    }
}
