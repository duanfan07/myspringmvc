package org.df.smartmvc.exception;

import javax.servlet.ServletException;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-19 17:19
 **/
public class MissingServletRequestParameterException extends ServletException {
    private String parameterName;

    private String parameterType;

    public MissingServletRequestParameterException(String parameterName, String parameterType) {
        super("Required " + parameterType + " parameter '" + parameterName + "' is not present");
        this.parameterName = parameterName;
        this.parameterType = parameterType;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getParameterType() {
        return parameterType;
    }
}
