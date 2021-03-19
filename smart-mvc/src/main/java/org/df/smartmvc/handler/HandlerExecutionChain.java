package org.df.smartmvc.handler;

import org.df.smartmvc.ModelAndView;
import org.df.smartmvc.handler.interceptor.HandlerInterceptor;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-19 10:18
 **/

//1. applyPreHandle: 执行所有拦截器的preHandle方法，如果preHandle返回的是false，那么就执行triggerAfterCompletion
//2. applyPostHandle: 执行所有拦截器的postHandle方法
//3. triggerAfterCompletion: `HandlerExecutionChain`中还定义了一个变量`interceptorIndex`，
//  当每执行一个`HandlerInterceptor`的preHandle方法后`interceptorIndex`的值就会被修改成当前执行拦截器的下标，
//  `triggerAfterCompletion`中根据`interceptorIndex`记录的下标值反向执行拦截器的`afterCompletion`方法；
// 举例说明：假如有三个拦截器，第一个拦截器正常执行完成preHandle方法，在执行第二个拦截器的preHandle返回了false，
//那么当调用`triggerAfterCompletion`只会执行第一个拦截器的afterCompletion
public class HandlerExecutionChain {

    //根据request中的path找到匹配的`HandlerMethod`，也就是控制器中的某个方法
    private HandlerMethod handler;
    //根据request中的path找到所有对本次请求生效的拦截器`HandlerInterceptor`
    private List<HandlerInterceptor> interceptors = new ArrayList<>();
    //用来反向执行triggerAfterCompletion方法的位置记录
    private int interceptorIndex = -1;

    public HandlerExecutionChain(HandlerMethod handler, List<HandlerInterceptor> interceptors) {
        this.handler = handler;
        if (!CollectionUtils.isEmpty(interceptors)){
            this.interceptors = interceptors;
        }
    }

    /***
     * @Description: 执行所有拦截器的preHandle方法，如果preHandle返回的是false，那么就执行triggerAfterCompletion
     * @Author: duanfan
     * @Date: 2021/3/19 15:25
     * @Param request:
     * @Param response:
     * @return: boolean
    **/
    public boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (CollectionUtils.isEmpty(interceptors)){
            return true;
        }
        for (int i = 0; i < interceptors.size(); i++) {
            HandlerInterceptor interceptor = interceptors.get(i);
            if (!interceptor.preHandle(request, response, this.handler)) {
                triggerAfterCompletion(request, response, null);
                return false;
            }
            this.interceptorIndex = i;
        }
        return true;
    }


    /***
     * @Description: 执行所有拦截器的postHandle方法
     * @Author: duanfan
     * @Date: 2021/3/19 15:26
     * @Param request:
     * @Param response:
     * @Param mv:
     * @return: void
    **/
    public void applyPostHandle(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) throws Exception {
        if (CollectionUtils.isEmpty(interceptors)){
            return;
        }
        for (int i = interceptors.size()-1; i >=0 ; i--) {
            HandlerInterceptor interceptor = interceptors.get(i);
            interceptor.postHandle(request, response, this.handler, mv);
        }
    }



    /***
     * @Description:`HandlerExecutionChain`中还定义了一个变量`interceptorIndex`，
     *               当每执行一个`HandlerInterceptor`的preHandle方法后`interceptorIndex`的值就会被修改成当前执行拦截器的下标，
     *              `triggerAfterCompletion`中根据`interceptorIndex`记录的下标值反向执行拦截器的`afterCompletion`方法；
     * @Author: duanfan
     * @Date: 2021/3/19 15:26
     * @Param request:
     * @Param response:
     * @Param ex:
     * @return: void
    **/
    private void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, Exception ex) throws Exception{
        if (CollectionUtils.isEmpty(interceptors)){
            return;
        }
        for (int i = this.interceptorIndex; i >= 0 ; i--) {
            HandlerInterceptor interceptor = interceptors.get(i);
            interceptor.afterCompletion(request, response, this.handler, ex);
        }
    }

    public HandlerMethod getHandler() {
        return handler;
    }

    public List<HandlerInterceptor> getInterceptors() {
        return interceptors;
    }
}
