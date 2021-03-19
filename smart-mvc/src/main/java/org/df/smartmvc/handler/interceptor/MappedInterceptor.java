package org.df.smartmvc.handler.interceptor;

import org.df.smartmvc.ModelAndView;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Handler;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-19 13:57
 **/
//1. 作为真正`HandlerInterceptor`的代理类，所以需要继承于`HandlerInterceptor`，实现`HandlerInterceptor`的三个接口，并且内部需要包含真正`HandlerInterceptor`的实例
//2. 管理`interceptor`对哪些URL生效，排除哪些URL
//3. 提供match功能，调用方传入path，判断当前`HandlerInterceptor`是否支持本次请求。该功能简单实现，只支持path的完整匹配，需要了解更复杂的匹配请查看SpringMVC中的`MappedInterceptor`
public class MappedInterceptor implements HandlerInterceptor {

    //用来管理支持的path和不支持的path
    private List<String> includePatterns = new ArrayList<>();
    private List<String> excludePatterns = new ArrayList<>();

    private HandlerInterceptor interceptor;

    public MappedInterceptor(HandlerInterceptor interceptor) {
        this.interceptor = interceptor;
    }
    /***
     * @Description: 添加支持的path
     * @Author: duanfan
     * @Date: 2021/3/19 14:14
     * @Param pattern:
     * @return: org.df.smartmvc.handler.interceptor.MappedInterceptor
    **/
    public MappedInterceptor addIncludePatterns(String... pattern) {
        this.includePatterns.addAll(Arrays.asList(pattern));
        return this;
    }

    /***
     * @Description: 添加排除的path
     * @Author: duanfan
     * @Date: 2021/3/19 14:14
     * @Param pattern:
     * @return: org.df.smartmvc.handler.interceptor.MappedInterceptor
    **/
    public MappedInterceptor addExcludePatterns(String... pattern) {
        this.excludePatterns.addAll(Arrays.asList(pattern));
        return this;
    }

    /***
     * @Description: 根据传入的path， 判断我们当前的interceptor是否支持
     * @Author: duanfan
     * @Date: 2021/3/19 14:15
     * @Param lookupPath:
     * @return: boolean
    **/
    public boolean matches(String lookupPath) {
        if (!CollectionUtils.isEmpty(this.excludePatterns)) {
            if (excludePatterns.contains(lookupPath)) {
                return false;
            }
        }
        if (ObjectUtils.isEmpty(this.includePatterns)) {
            return true;
        }
        if (includePatterns.contains(lookupPath)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return this.interceptor.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        this.interceptor.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        this.interceptor.afterCompletion(request, response, handler, ex);
    }

    public HandlerInterceptor getInterceptor() {
        return interceptor;
    }
}
