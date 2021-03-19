package org.df.smartmvc.handler.mapping;

import org.df.smartmvc.handler.HandlerMethod;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: SmartMVC
 * @description: 是RequestMappingInfo和HandlerMethod的注册中心。
 * 当解析完一个控制器的method后就会向MappingRegistry中注册一个；最后当接收到用户请求后，根据请求的url在MappingRegistry找到对应的HandlerMethod
 * @author: duanf
 * @create: 2021-03-19 10:00
 **/
public class MappingRegistry {

    //保存RequestMappingInfo的map（线程安全）
    private Map<String, RequestMappingInfo> pathMappingInfo = new ConcurrentHashMap<>();
    //保存HandlerMethod的map（线程安全）
    private Map<String, HandlerMethod> pathHandlerMethod = new ConcurrentHashMap<>();

    /**
     * 注册url和Mapping/HandlerMethod的对应关系
     * @param mapping
     * @param handler
     * @param method
     */
    public void register(RequestMappingInfo mapping, Object handler, Method method) {
        pathMappingInfo.put(mapping.getPath(), mapping);

        HandlerMethod handlerMethod = new HandlerMethod(handler, method);
        pathHandlerMethod.put(mapping.getPath(), handlerMethod);
    }
    /***
     * @Description: 通过path查找RequestMappingInfo
     * @Author: duanfan
     * @Date: 2021/3/19 10:13
     * @Param path:
     * @return: org.df.smartmvc.handler.mapping.RequestMappingInfo
    **/
    public  RequestMappingInfo getMappingByPath(String path) {
        return this.pathMappingInfo.get(path);
    }

    /***
     * @Description: 通过path查找HandlerMethod
     * @Author: duanfan
     * @Date: 2021/3/19 10:13
     * @Param path:
     * @return: org.df.smartmvc.handler.HandlerMethod
    **/
    public  HandlerMethod getHandlerMethodByPath(String path) {
        return this.pathHandlerMethod.get(path);
    }

    public Map<String, HandlerMethod> getPathHandlerMethod() {
        return pathHandlerMethod;
    }
}
