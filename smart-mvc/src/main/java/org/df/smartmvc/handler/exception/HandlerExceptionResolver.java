package org.df.smartmvc.handler.exception;

import org.df.smartmvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-22 17:00
 **/
public interface HandlerExceptionResolver {

    ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Exception ex);
}
