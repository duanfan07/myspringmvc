package org.df.smartmvc.handler.returnvalue;

import org.df.smartmvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: SmartMVC
 * @description: 返回的数据是Model类型处理类
 * @author: duanf
 * @create: 2021-03-22 10:40
 **/
public class ModelMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportReturnType(MethodParameter returnType) {
        return Model.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType,
                                  ModelAndViewContainer modelAndViewContainer,
                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (returnValue ==null) {
            return;
        } else if (returnValue instanceof Model){
            modelAndViewContainer.getModel().addAllAttributes(((Model) returnValue).asMap() );
        } else {
            throw new UnsupportedOperationException("Unexpected return type: " +
                    returnType.getParameterType().getName() + " in method: " + returnType.getMethod());
        }

    }
}
