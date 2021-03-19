package org.df.smartmvc.annotation;

import jdk.nashorn.internal.ir.RuntimeNode;
import org.df.smartmvc.http.RequestMethod;
import org.w3c.dom.Element;

import java.lang.annotation.*;

/**
 * @program: SmartMVC
 * @description: RequestMapping注解
 * @author: duanf
 * @create: 2021-03-18 18:25
 **/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

    String path();
    RequestMethod method() default RequestMethod.GET;
}
