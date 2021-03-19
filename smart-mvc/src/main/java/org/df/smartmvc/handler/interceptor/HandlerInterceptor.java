package org.df.smartmvc.handler.interceptor;

import org.df.smartmvc.ModelAndView;
import org.springframework.lang.Nullable;
import org.springframework.test.web.ModelAndViewAssert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.concurrent.Executors;

/**
 * @program: SmartMVC
 * @description: 三个方法（执行前，执行后，最终执行完成后）
 * @author: duanf
 * @create: 2021-03-19 13:46
 **/
public interface HandlerInterceptor {
    /**
     * @Description: 在执行Handler之前被调用，如果返回的是false，那么Handler就不会在执行
     * @Author: duanfan
     * @Date: 2021/3/19 13:50
     * @Param request:
     * @Param response:
     * @Param handler:
     * @return: boolean
    **/
    default boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception{
        return true;
    }
    /**
     * @Description: 在Handler执行完成之后被调用，可以获取Handler返回的结果`ModelAndView`
     * @Author: duanfan
     * @Date: 2021/3/19 13:51
     * @Param request:
     * @Param response:
     * @Param handler:
     * @Param modelAndView:
     * @return: void
    **/
    default void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                               @Nullable ModelAndView modelAndView)
            throws Exception{}
     /**
      * @Description: 该方法是无论什么情况下都会被调用，比如：`preHandle`返回false，Handler执行过程中抛出异常，Handler正常执行完成
      * @Author: duanfan
      * @Date: 2021/3/19 13:51
      * @Param request:
      * @Param response:
      * @Param handler:
      * @return: void
     **/
    default void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex)
            throws Exception{}

}
