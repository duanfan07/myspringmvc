package org.df.smartmvc.handler.returnvalue;

import org.df.smartmvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: SmartMVC
 * @description: 返回值是String，那么把这个返回值当做是视图的名字，放入到`ModelAndViewContainer`中
 * @author: duanf
 * @create: 2021-03-22 10:52
 **/
public class ViewNameMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportReturnType(MethodParameter returnType) {
        return CharSequence.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer modelAndViewContainer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (returnValue instanceof CharSequence) {
            String viewName = returnValue.toString();
            modelAndViewContainer.setViewName(viewName);
        } else if (returnValue != null) {
            // should not happen
            throw new UnsupportedOperationException("Unexpected return type: " +
                    returnType.getParameterType().getName() + " in method: " + returnType.getMethod());
        }
    }
}
