package org.df.smartmvc.handler.interceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-19 14:16
 **/
//现在我们已经开发完了处理拦截业务逻辑的接口`HandlerInterceptor`，管理`HandlerInterceptor`与请求路径的映射关联类
//`MappedInterceptor`，我们还缺少一个拦截器的注册中心管理所有的拦截器，试想下如果没有这个，
//那么当需要获取项目中所有拦截器的时候就会很难受，所以我们还需要建了一个`InterceptorRegistry`
public class InterceptorRegistry {

    private List<MappedInterceptor> mappedInterceptors = new ArrayList<>();

    /***
     * @Description: 注册一个拦截器到InterceptorRegistry
     * @Author: duanfan
     * @Date: 2021/3/19 14:21
     * @Param interceptor:
     * @return: org.df.smartmvc.handler.interceptor.MappedInterceptor
    **/
    public MappedInterceptor addInterceptor(HandlerInterceptor interceptor) {
        MappedInterceptor mappedInterceptor = new MappedInterceptor(interceptor);
        mappedInterceptors.add(mappedInterceptor);
        return mappedInterceptor;
    }

    public List<MappedInterceptor> getMappedInterceptors() {
        return mappedInterceptors;
    }
}
