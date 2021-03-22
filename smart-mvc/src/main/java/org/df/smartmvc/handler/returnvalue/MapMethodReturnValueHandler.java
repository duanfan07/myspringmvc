package org.df.smartmvc.handler.returnvalue;

import org.df.smartmvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @program: SmartMVC
 * @description: 返回的数据是Map类型处理类
 * @author: duanf
 * @create: 2021-03-22 10:43
 **/
public class MapMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportReturnType(MethodParameter returnType) {
        return Map.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer modelAndViewContainer, HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (returnValue instanceof Map) {
            modelAndViewContainer.getModel().addAllAttributes(((Map) returnValue));
        } else if (returnValue != null){
            throw new UnsupportedOperationException("Unexpected return type: " +
                    returnType.getParameterType().getName() + " in method: " + returnType.getMethod());
        }
    }
}
