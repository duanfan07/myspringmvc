package org.df.smartmvc.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-22 17:27
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ControllerAdvice  {
}
