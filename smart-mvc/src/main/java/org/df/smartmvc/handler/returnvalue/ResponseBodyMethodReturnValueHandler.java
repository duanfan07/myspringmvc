package org.df.smartmvc.handler.returnvalue;

import com.alibaba.fastjson.JSON;
import org.df.smartmvc.annotation.ResponseBody;
import org.df.smartmvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-22 10:57
 **/
public class ResponseBodyMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportReturnType(MethodParameter returnType) {
        //类上或者方法上标注@ResponseBody
        return (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseBody.class) ||
                returnType.hasMethodAnnotation(ResponseBody.class));
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer modelAndViewContainer, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //标记出当前请求已经处理完成，后续的渲染无需在执行
        modelAndViewContainer.setRequestHandled(true);
        //将json数据写到response返回给前端
        outPutMessage(response, JSON.toJSONString(returnValue));
    }

    private void outPutMessage(HttpServletResponse response, String message) throws IOException {
        try (PrintWriter out = response.getWriter()){
            out.write(message);
        }
    }
}
