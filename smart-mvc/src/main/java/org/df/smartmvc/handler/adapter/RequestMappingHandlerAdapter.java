package org.df.smartmvc.handler.adapter;


import org.df.smartmvc.ModelAndView;
import org.df.smartmvc.handler.HandlerMethod;
import org.df.smartmvc.handler.InvocableHandlerMethod;
import org.df.smartmvc.handler.ModelAndViewContainer;
import org.df.smartmvc.handler.argument.*;
import org.df.smartmvc.handler.returnvalue.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-22 14:15
 **/
//入到spring容器之后需要做一些初始化的工作，所以实现了接口`InitializingBean`
public class RequestMappingHandlerAdapter implements HandlerAdapter, InitializingBean {

    //如果SmartMVC提供的参数解析器和返回值处理器不满足用户的需求，允许添加自定义的参数解析器和返回值处理器(可扩展性)
    private List<HandlerMethodArgumentResolver> customArgumentResolvers;
    private HandlerMethodArgumentResolverComposite argumentResolverComposite;

    //如果SmartMVC提供的参数解析器和返回值处理器不满足用户的需求，允许添加自定义的参数解析器和返回值处理器（可扩展性）
    private List<HandlerMethodReturnValueHandler> customReturnValueHandlers;
    private HandlerMethodReturnValueHandlerComposite returnValueHandlerComposite;

    private ConversionService conversionService;

    /**
     * @Description: 当`DispatcherServlet`处理用户请求的时候会调用`HandlerAdapter`的handle方法，
     * 这时候先通过传入`HandlerMethod`创建之前我们已经开发完成的组件`InvocableHandlerMethod`，
     * 然后调用`invokeAndHandle`执行控制器的方法
     * @Author: duanfan
     * @Date: 2021/3/22 14:39
     * @Param request:
     * @Param response:
     * @Param handlerMethod:
     * @return: org.df.smartmvc.ModelAndView
    **/
    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response,
                               HandlerMethod handlerMethod) throws Exception {

        InvocableHandlerMethod invocableMethod = createInvocableHandlerMethod(handlerMethod);
        ModelAndViewContainer mavContainer = new ModelAndViewContainer();

        invocableMethod.invokeAndHandle(request, response, mavContainer);

        //将创建的`ModelAndView`对象返回
        return getModelAndView(mavContainer);
    }

    private ModelAndView getModelAndView(ModelAndViewContainer mavContainer) {
        if (mavContainer.isRequestHandled()) {
            //本次请求已经处理完成
            return null;
        }

        ModelAndView mav = new ModelAndView();
        mav.setStatus(mavContainer.getStatus());
        mav.setModel(mavContainer.getModel());
        mav.setView(mavContainer.getView());
        return mav;
    }

    /**
     * @Description: 创建组件`InvocableHandlerMethod`
     * @Author: duanfan
     * @Date: 2021/3/22 14:40
     * @Param null:
     * @return: null
    **/
    private InvocableHandlerMethod createInvocableHandlerMethod(HandlerMethod handlerMethod) {
        return new InvocableHandlerMethod(handlerMethod,
                this.argumentResolverComposite,
                this.returnValueHandlerComposite,
                this.conversionService);
    }

    /***
     * @Description: 需要把系统默认支持的参数解析器和返回值处理器以及用户自定义的一起添加到系统中
     * @Author: duanfan
     * @Date: 2021/3/22 14:38
     * @return: void
    **/
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(conversionService, "conversionService can not null");
        if (Objects.isNull(argumentResolverComposite)) {
            List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
            this.argumentResolverComposite = new HandlerMethodArgumentResolverComposite();
            this.argumentResolverComposite.addResolver( resolvers);
        }

        if (Objects.isNull(returnValueHandlerComposite)) {
            List<HandlerMethodReturnValueHandler> handlers = getDefaultReturnValueHandlers();
            this.returnValueHandlerComposite = new HandlerMethodReturnValueHandlerComposite();
            this.returnValueHandlerComposite.addReturnValueHandler(handlers);
        }
    }

    /**
     * 初始化默认返回值处理器
     *
     * @return
     */
    private List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers() {
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>();

        handlers.add(new MapMethodReturnValueHandler());
        handlers.add(new ModelMethodReturnValueHandler());
        handlers.add(new ResponseBodyMethodReturnValueHandler());
        handlers.add(new ViewNameMethodReturnValueHandler());
        handlers.add(new ViewMethodReturnValueHandler());

        if (!CollectionUtils.isEmpty(getCustomReturnValueHandlers())) {
            handlers.addAll(getDefaultReturnValueHandlers());
        }

        return handlers;
    }

    /**
     * 初始化默认参数解析器
     *
     * @return
     */
    private List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers() {
        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();

        resolvers.add(new ModelMethodArgumentResolver());
        resolvers.add(new RequestParamMethodArgumentResolver());
        resolvers.add(new RequestBodyMethodArgumentResolver());
        resolvers.add(new ServletResponseMethodArgumentResolver());
        resolvers.add(new ServletRequestMethodArgumentResolver());

        if (!CollectionUtils.isEmpty(getCustomArgumentResolvers())) {
            resolvers.addAll(getCustomArgumentResolvers());
        }

        return resolvers;
    }

    public List<HandlerMethodArgumentResolver> getCustomArgumentResolvers() {
        return customArgumentResolvers;
    }

    public void setCustomArgumentResolvers(List<HandlerMethodArgumentResolver> customArgumentResolvers) {
        this.customArgumentResolvers = customArgumentResolvers;
    }

    public List<HandlerMethodReturnValueHandler> getCustomReturnValueHandlers() {
        return customReturnValueHandlers;
    }

    public void setCustomReturnValueHandlers(List<HandlerMethodReturnValueHandler> customReturnValueHandlers) {
        this.customReturnValueHandlers = customReturnValueHandlers;
    }

    public ConversionService getConversionService() {
        return conversionService;
    }

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }
}