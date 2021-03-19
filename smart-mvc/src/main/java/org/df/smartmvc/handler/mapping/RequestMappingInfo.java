package org.df.smartmvc.handler.mapping;

import org.df.smartmvc.annotation.RequestMapping;
import org.df.smartmvc.http.RequestMethod;

/**
 * @program: SmartMVC
 * @description: 与@RequestMapping对应的实体类
 * @author: duanf
 * @create: 2021-03-19 09:41
 **/
public class RequestMappingInfo {

    private String path;
    private RequestMethod httpMethod;

    public RequestMappingInfo(String prefix, RequestMapping requestMapping) {
        this.path = prefix + requestMapping.path();
        this.httpMethod = requestMapping.method();
    }

    public String getPath() {
        return path;
    }

    public RequestMethod getHttpMethod() {
        return httpMethod;
    }
}
