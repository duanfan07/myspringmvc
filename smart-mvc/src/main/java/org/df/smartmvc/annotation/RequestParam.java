package org.df.smartmvc.annotation;


import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {

    String name(); //从request取参数的名字，该参数必填

    boolean required() default true; //说明该参数是否必填，默认是true

    String defaultValue() default ""; //如果request中找不到对应的参数，那么就用默认值
}
