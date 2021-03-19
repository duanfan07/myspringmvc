package org.df.smartmvc.config;

import org.df.smartmvc.handler.interceptor.InterceptorRegistry;
import org.df.smartmvc.handler.mapping.RequestMappingHandlerMapping;
import org.df.smartmvc.handler.interceptor.Test2HandlerInterceptor;
import org.df.smartmvc.handler.interceptor.TestHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @program: SmartMVC
 * @description: 配置主类
 * @author: duanf
 * @create: 2021-03-18 17:55
 **/
@Configuration
@ComponentScan(basePackages = "org.df.smartmvc")
public class AppConfig {

//    @Bean
//    public RequestMappingHandlerMapping handlerMapping() {
//        return new RequestMappingHandlerMapping();
//    }

    @Bean
    public RequestMappingHandlerMapping handlerMapping() {
        InterceptorRegistry interceptorRegistry = new InterceptorRegistry();

        //配置的拦截器
        TestHandlerInterceptor interceptor = new TestHandlerInterceptor();
        interceptorRegistry.addInterceptor(interceptor)
                .addExcludePatterns("/ex_test")
                .addIncludePatterns("/in_test");

        Test2HandlerInterceptor interceptor2 = new Test2HandlerInterceptor();
        interceptorRegistry.addInterceptor(interceptor2)
                .addIncludePatterns("/in_test2", "/in_test3");

        RequestMappingHandlerMapping mapping = new RequestMappingHandlerMapping();
        mapping.setInterceptors(interceptorRegistry.getMappedInterceptors());
        return mapping;
    }

}
