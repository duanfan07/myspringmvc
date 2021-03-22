package org.df.smartmvc.handler.adapter;

import org.df.smartmvc.ModelAndView;
import org.df.smartmvc.handler.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-22 14:10
 **/
public interface HandlerAdapter {
    //该接口我们只定义了一个handle方法，但是在SpringMVC中还有一个`supports`方法，刚才我们也说过在SpringMVC中`HandlerAdapter`有多个实现，
    //这也是一个策略模式，所以需要这一个`supports`方法；在我们开发的SmartMVC中只打算做一个实现，所以只要一个handle方法就足够了。
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response,
                        HandlerMethod handler) throws Exception;
}
