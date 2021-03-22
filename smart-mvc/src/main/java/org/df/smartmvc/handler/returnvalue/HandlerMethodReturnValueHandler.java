package org.df.smartmvc.handler.returnvalue;

import org.df.smartmvc.ModelAndView;
import org.df.smartmvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public interface HandlerMethodReturnValueHandler {

    //判断处理器是否支持该返回值的类型
    boolean supportReturnType(MethodParameter returnType);

    //returnValue(Handler执行之后的返回值)；该方法还需要传入`HttpServletResponse`对象，
    //是因为可能会在处理其中直接处理完整个请求，比如`@ResponseBody`
    void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType,
                           ModelAndViewContainer modelAndViewContainer,
                           HttpServletRequest request, HttpServletResponse response) throws Exception;
}
