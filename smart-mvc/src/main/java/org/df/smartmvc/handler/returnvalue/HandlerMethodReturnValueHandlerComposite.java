package org.df.smartmvc.handler.returnvalue;

import org.df.smartmvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @program: SmartMVC
 * @description: 返回值处理器的聚合类
 * @author: duanf
 * @create: 2021-03-22 11:06
 **/
public class HandlerMethodReturnValueHandlerComposite implements HandlerMethodReturnValueHandler {

    private List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<>();
    @Override
    public boolean supportReturnType(MethodParameter returnType) {
        return true;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType,
                                  ModelAndViewContainer modelAndViewContainer,
                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
        for (HandlerMethodReturnValueHandler handler : returnValueHandlers) {
            if (handler.supportReturnType(returnType)) {
                handler.handleReturnValue(returnValue, returnType, modelAndViewContainer, request, response);
                return;
            }
        }
        //托底处理
        throw new IllegalArgumentException("Unsupported parameter type [" +
                returnType.getParameterType().getName() + "]. supportsParameter should be called first.");

    }

    public void clear() {
        this.returnValueHandlers.clear();
    }

    public void addReturnValueHandler(HandlerMethodReturnValueHandler... handlers) {
        Collections.addAll(this.returnValueHandlers, handlers);
    }
    public void addReturnValueHandler(Collection<HandlerMethodReturnValueHandler> handlers) {
        this.returnValueHandlers.addAll(handlers);
    }
}
