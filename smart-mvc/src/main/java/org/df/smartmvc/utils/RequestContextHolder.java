package org.df.smartmvc.utils;

import org.springframework.core.NamedInheritableThreadLocal;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: SmartMVC
 * @description: 保留请求信息的一个类（通过ThreadLocal实现）
 * 在当前线程中存放了当前请求的`HttpServletRequest`
 * @author: duanf
 * @create: 2021-03-22 15:52
 **/
public abstract class RequestContextHolder {

    private static final ThreadLocal<HttpServletRequest> inheritableRequestHolder =
            new NamedInheritableThreadLocal<>("Request context");

    /**
     * Reset the HttpServletRequest for the current thread.
     */
    public static void resetRequest() {
        inheritableRequestHolder.remove();
    }

    public static void setRequest(HttpServletRequest request) {
        inheritableRequestHolder.set(request);
    }

    public static HttpServletRequest getRequest() {
        return inheritableRequestHolder.get();
    }
}
