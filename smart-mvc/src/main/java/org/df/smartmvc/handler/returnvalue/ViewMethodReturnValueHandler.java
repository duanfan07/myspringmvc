package org.df.smartmvc.handler.returnvalue;

import org.df.smartmvc.handler.ModelAndViewContainer;
import org.df.smartmvc.view.View;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: SmartMVC
 * @description: 如果返回值是View对象，那么直接把视图放入到`ModelAndViewContainer`中
 * @author: duanf
 * @create: 2021-03-22 10:55
 **/
public class ViewMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportReturnType(MethodParameter returnType) {
        return View.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer modelAndViewContainer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (returnValue instanceof View) {
            modelAndViewContainer.setView(returnValue);
        } else if (returnValue != null) {
            // should not happen
            throw new UnsupportedOperationException("Unexpected return type: " +
                    returnType.getParameterType().getName() + " in method: " + returnType.getMethod());
        }
    }
}
