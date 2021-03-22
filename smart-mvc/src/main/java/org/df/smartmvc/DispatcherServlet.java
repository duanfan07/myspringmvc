package org.df.smartmvc;

import com.sun.xml.internal.ws.handler.HandlerException;
import org.df.smartmvc.handler.HandlerExecutionChain;
import org.df.smartmvc.handler.adapter.HandlerAdapter;
import org.df.smartmvc.handler.exception.HandlerExceptionResolver;
import org.df.smartmvc.handler.mapping.HandlerMapping;
import org.df.smartmvc.utils.RequestContextHolder;
import org.df.smartmvc.view.View;
import org.df.smartmvc.view.resovler.ViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-22 16:53
 **/

//1. 初始化部分，当Servlet在第一次初始化的时候会调用 init方法，在该方法里对诸如 handlerMapping，ViewResolver 等进行初始化，
//2. 对HTTP请求进行响应，作为一个Servlet，当请求到达时Web容器会调用其service方法; 通过`RequestContextHolder`在线程变量中设置request，然后调用`doDispatch`完成请求
//
public class DispatcherServlet extends HttpServlet implements ApplicationContextAware {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext applicationContext;

    private HandlerMapping handlerMapping;
    private HandlerAdapter handlerAdapter;
    private ViewResolver viewResolver;
    ////DispatcherServlet中的异常变量
    private Collection<HandlerExceptionResolver> handlerExceptionResolvers;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.applicationContext = applicationContext;
    }

    /**
     * @Description: 初始化部分，当Servlet在第一次初始化的时候会调用 init方法，在
     * 该方法里对诸如 handlerMapping，ViewResolver 等进行初始化，
     * @Author: duanfan
     * @Date: 2021/3/22 17:06
     * @return: void
    **/
    @Override
    public void init() throws ServletException {
        this.handlerMapping = this.applicationContext.getBean(HandlerMapping.class);
        this.handlerAdapter = this.applicationContext.getBean(HandlerAdapter.class);
        this.viewResolver = this.applicationContext.getBean(ViewResolver.class);
        //初始化异常
        this.handlerExceptionResolvers =
                this.applicationContext.getBeansOfType(HandlerExceptionResolver.class).values();
    }

    /**
     * @Description: 对HTTP请求进行响应，作为一个Servlet，当请求到达时Web容器会调用其service方法;
     * 通过`RequestContextHolder`在线程变量中设置request，然后调用`doDispatch`完成请求
     * @Author: duanfan
     * @Date: 2021/3/22 17:07
     * @Param request:
     * @Param response:
     * @return: void
    **/
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("DispatcherServlet.service => uri:{}", request.getRequestURI());
        RequestContextHolder.setRequest(request);

        try {
            doDispatch(request, response);
        } catch (Exception e) {
            logger.error("Handler the request fail", e);
        } finally {
            RequestContextHolder.resetRequest();
        }

    }

    private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Exception dispatchException = null;
        HandlerExecutionChain executionChain = null;
        try {
            ModelAndView mv = null;
            try {
                //首先通过handlerMapping获取到处理本次请求的`HandlerExecutionChain`
                executionChain = this.handlerMapping.getHandler(request);
                //执行拦截器的前置方法
                if (!executionChain.applyPreHandle(request, response)) {
                    return;
                }
                //通过`handlerAdapter`执行handler返回ModelAndView
                mv = handlerAdapter.handle(request, response, executionChain.getHandler());
                //执行拦截器的后置方法
                executionChain.applyPostHandle(request, response, mv);
            } catch (Exception e) {
                dispatchException = e;
            }
            // 处理返回的结果
            processDispatchResult(request, response, mv, dispatchException);
        } catch (Exception ex) {
            dispatchException = ex;
            throw ex;
        } finally {
            if (Objects.nonNull(executionChain)) {
                //处理完成请求后调用`executionChain.triggerAfterCompletion(request, response, dispatchException);`，完成拦截器的`afterCompletion`方法调用
                executionChain.triggerAfterCompletion(request, response, dispatchException);
            }
        }
    }

    private void processDispatchResult(HttpServletRequest request, HttpServletResponse response,
                                       ModelAndView mv, Exception dispatchException) throws Exception {

        //在执行的过程中抛出了任何异常，那么就会执行`processHandlerException`，方便做全局异常处理
        if (Objects.nonNull(dispatchException)) {
            mv = processHandlerException(request, response, dispatchException);
        }
        //正常的返回ModelAndView，那么就执行render方法
        if (Objects.nonNull(mv)) {
            render(mv, request, response);
            return;
        }

        logger.info("No view rendering, null ModelAndView returned.");

    }

    private void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {

        View view;
        String viewName = mv.getViewName();
        //首先通过ViewResolver解析出视图
        if (!StringUtils.isEmpty(viewName)) {
            view = this.viewResolver.resolveViewName(viewName);
        } else {
            view = (View) mv.getView();
        }

        if (mv.getStatus() != null) {
            response.setStatus(mv.getStatus().getValue());
        }
        //然后在调用View的render方法实施渲染逻辑
        view.render(mv.getModel().asMap(), request, response);
    }

    //异常处理后返回的ModeAndView
    private ModelAndView processHandlerException(HttpServletRequest request, HttpServletResponse response,
                                                 Exception dispatchException) throws Exception {

        if (CollectionUtils.isEmpty(this.handlerExceptionResolvers)) {
            throw dispatchException;
        }
        for (HandlerExceptionResolver resolver : this.handlerExceptionResolvers) {
            ModelAndView exMv = resolver.resolveException(request, response, dispatchException);
            if (exMv != null) {
                return exMv;
            }
        }

        throw dispatchException;
    }
}
