package org.df.smartmvc.exception;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * @program: SmartMVC
 * @description: 在通过HandlerMapping.getHandler获取对应request处理器的时候，可能会遇到写错了请求的路径导致找不到匹配的Handler情况，
 * 这个时候需要抛出指定的异常，方便我们后续处理，比如说跳转到错误页面
 * @author: duanf
 * @create: 2021-03-19 14:50
 **/
public class NoHandlerFoundException extends ServletException {

    private String httpMethod;
    private String requestURL;

    public NoHandlerFoundException(HttpServletRequest request) {
        this.httpMethod = request.getMethod();
        this.requestURL = request.getRequestURL().toString();
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getRequestURL() {
        return requestURL;
    }
}
