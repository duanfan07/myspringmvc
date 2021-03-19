package org.df.smartmvc.handler.mapping;


import org.df.smartmvc.handler.HandlerExecutionChain;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception;
}
