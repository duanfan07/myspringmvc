package org.df.smartmvc.handler.exception;

import org.df.smartmvc.ModelAndView;
import org.df.smartmvc.handler.InvocableHandlerMethod;
import org.df.smartmvc.handler.ModelAndViewContainer;
import org.df.smartmvc.handler.argument.*;
import org.df.smartmvc.handler.returnvalue.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-22 17:34
 **/
public class ExceptionHandlerExceptionResolver implements HandlerExceptionResolver, ApplicationContextAware, InitializingBean {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext applicationContext;
    private Map<ControllerAdviceBean, ExceptionHandlerMethodResolver> exceptionHandlerAdviceCache =
            new LinkedHashMap<>();
    private ConversionService conversionService;
    private List<HandlerMethodArgumentResolver> customArgumentResolvers;
    private HandlerMethodArgumentResolverComposite argumentResolvers;

    private List<HandlerMethodReturnValueHandler> customReturnValueHandlers;
    private HandlerMethodReturnValueHandlerComposite returnValueHandlers;

    /**
     * @Description:
     * @Author: duanfan
     * @Date: 2021/3/22 17:47
     * @Param request:
     * @Param response:
     * @Param ex:
     * @return: org.df.smartmvc.ModelAndView
    **/
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        //找到处理异常`ControllerAdviceBean`、`ExceptionHandlerMethodResolver`，构建出`InvocableHandlerMethod`
        InvocableHandlerMethod exceptionHandlerMethod = getExceptionHandlerMethod(ex);
        if (exceptionHandlerMethod == null) {
            return null;
        }

        ModelAndViewContainer mavContainer = new ModelAndViewContainer();

        //执行方法
        try {
            Throwable cause = ex.getCause();
            if (Objects.nonNull(cause)) {
                exceptionHandlerMethod.invokeAndHandle(request, response, mavContainer, cause);
            } else {
                exceptionHandlerMethod.invokeAndHandle(request, response, mavContainer, ex);
            }
        } catch (Exception e) {
            logger.error("exceptionHandlerMethod.invokeAndHandle fail", e);
            return null;
        }

        if (mavContainer.isRequestHandled()) {
            return null;
        }
        //构建ModelAndView对象
        ModelAndView mav = new ModelAndView();
        mav.setStatus(mavContainer.getStatus());
        mav.setModel(mavContainer.getModel());
        mav.setView(mavContainer.getView());
        return mav;
    }

    private InvocableHandlerMethod getExceptionHandlerMethod(Exception exception) {
        for (Map.Entry<ControllerAdviceBean, ExceptionHandlerMethodResolver> entry : this.exceptionHandlerAdviceCache.entrySet()) {
            ControllerAdviceBean advice = entry.getKey();
            ExceptionHandlerMethodResolver resolver = entry.getValue();
            Method method = resolver.resolveMethod(exception);
            if (method != null) {
                return new InvocableHandlerMethod(advice.getBean(),
                        method,
                        this.argumentResolvers,
                        this.returnValueHandlers,
                        this.conversionService);
            }
        }

        return null;
    }

    /**
     * @Description: 由于需要通过反射调用被`ExceptionHandler`标注的方法处理异常，与HandlerAdapter类型需要参数解析器和返回值处理，
     * 所以在`afterPropertiesSet`需要对参数解析器和返回值处理进行初始化；
     * @Author: duanfan
     * @Date: 2021/3/22 17:45
     * @return: void
    **/
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.conversionService, "conversionService can not null");
        //完成`exceptionHandlerAdviceCache`变量的初始化,建立起`ControllerAdviceBean`和`ExceptionHandlerMethodResolver`的关系
        initExceptionHandlerAdviceCache();
        if (this.argumentResolvers == null) {
            List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
            this.argumentResolvers = new HandlerMethodArgumentResolverComposite();
            this.argumentResolvers.addResolver(resolvers);
        }
        if (this.returnValueHandlers == null) {
            List<HandlerMethodReturnValueHandler> handlers = getDefaultReturnValueHandlers();
            this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite();
            this.returnValueHandlers.addReturnValueHandler(handlers);
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

    private void initExceptionHandlerAdviceCache() {
        List<ControllerAdviceBean> adviceBeans = ControllerAdviceBean.findAnnotatedBeans(applicationContext);
        for (ControllerAdviceBean adviceBean : adviceBeans) {
            Class<?> beanType = adviceBean.getBeanType();
            if (beanType == null) {
                throw new IllegalStateException("Unresolvable type for ControllerAdviceBean: " + adviceBean);
            }
            ExceptionHandlerMethodResolver resolver = new ExceptionHandlerMethodResolver(beanType);
            if (resolver.hasExceptionMappings()) {
                this.exceptionHandlerAdviceCache.put(adviceBean, resolver);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Map<ControllerAdviceBean, ExceptionHandlerMethodResolver> getExceptionHandlerAdviceCache() {
        return exceptionHandlerAdviceCache;
    }

    public List<HandlerMethodArgumentResolver> getCustomArgumentResolvers() {
        return customArgumentResolvers;
    }

    public List<HandlerMethodReturnValueHandler> getCustomReturnValueHandlers() {
        return customReturnValueHandlers;
    }

    public void setCustomArgumentResolvers(List<HandlerMethodArgumentResolver> customArgumentResolvers) {
        this.customArgumentResolvers = customArgumentResolvers;
    }

    public void setCustomReturnValueHandlers(List<HandlerMethodReturnValueHandler> customReturnValueHandlers) {
        this.customReturnValueHandlers = customReturnValueHandlers;
    }

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }
}

