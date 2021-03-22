package org.df.smartmvc.config;

import org.df.smartmvc.DispatcherServlet;
import org.df.smartmvc.handler.adapter.HandlerAdapter;
import org.df.smartmvc.handler.adapter.RequestMappingHandlerAdapter;
import org.df.smartmvc.handler.exception.ExceptionHandlerExceptionResolver;
import org.df.smartmvc.handler.exception.HandlerExceptionResolver;
import org.df.smartmvc.handler.interceptor.InterceptorRegistry;
import org.df.smartmvc.handler.mapping.RequestMappingHandlerMapping;
import org.df.smartmvc.handler.interceptor.Test2HandlerInterceptor;
import org.df.smartmvc.handler.interceptor.TestHandlerInterceptor;
import org.df.smartmvc.view.resovler.ContentNegotiatingViewResolver;
import org.df.smartmvc.view.resovler.InternalResourceViewResolver;
import org.df.smartmvc.view.resovler.ViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.support.DefaultFormattingConversionService;

import java.util.Collections;

/**
 * @program: SmartMVC
 * @description: 配置主类
 * @author: duanf
 * @create: 2021-03-18 17:55
 **/
@Configuration
@ComponentScan(basePackages = "org.df.smartmvc")
public class AppConfig {

    @Bean
    public RequestMappingHandlerMapping handlerMapping() {
        return new RequestMappingHandlerMapping();
    }

    @Bean
    public HandlerAdapter handlerAdapter(ConversionService conversionService) {
        RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
        handlerAdapter.setConversionService(conversionService);
        return handlerAdapter;
    }
    @Bean
    public ConversionService conversionService() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        DateFormatter dateFormatter = new DateFormatter();
        dateFormatter.setPattern("yyyy-MM-dd HH:mm:ss");
        conversionService.addFormatter(dateFormatter);
        return conversionService;
    }
    @Bean
    public ViewResolver viewResolver() {
        ContentNegotiatingViewResolver negotiatingViewResolver = new ContentNegotiatingViewResolver();
        negotiatingViewResolver.setViewResolvers(Collections.singletonList(new InternalResourceViewResolver()));
        return negotiatingViewResolver;
    }
    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    @Bean
    public HandlerExceptionResolver handlerExceptionResolver(ConversionService conversionService) {
        ExceptionHandlerExceptionResolver resolver = new ExceptionHandlerExceptionResolver();
        resolver.setConversionService(conversionService);
        return resolver;
    }

//    @Bean
//    public RequestMappingHandlerMapping handlerMapping() {
//        InterceptorRegistry interceptorRegistry = new InterceptorRegistry();
//
//        //配置的拦截器
//        TestHandlerInterceptor interceptor = new TestHandlerInterceptor();
//        interceptorRegistry.addInterceptor(interceptor)
//                .addExcludePatterns("/ex_test")
//                .addIncludePatterns("/in_test");
//
//        Test2HandlerInterceptor interceptor2 = new Test2HandlerInterceptor();
//        interceptorRegistry.addInterceptor(interceptor2)
//                .addIncludePatterns("/in_test2", "/in_test3");
//
//        RequestMappingHandlerMapping mapping = new RequestMappingHandlerMapping();
//        mapping.setInterceptors(interceptorRegistry.getMappedInterceptors());
//        return mapping;
//    }

}
