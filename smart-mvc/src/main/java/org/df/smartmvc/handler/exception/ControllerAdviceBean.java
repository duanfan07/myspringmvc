package org.df.smartmvc.handler.exception;

import org.df.smartmvc.annotation.ControllerAdvice;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: SmartMVC
 * @description: 该类用于表示被`@ControllerAdvice`标注的类
 * @author: duanf
 * @create: 2021-03-22 17:22
 **/
public class ControllerAdviceBean {

    private String beanName;
    private Class<?> beanType;
    private Object bean;

    public ControllerAdviceBean(String beanName, Object bean) {
        Assert.notNull(bean, "Bean must not be null");
        this.beanName = beanName;
        this.beanType = bean.getClass();
        this.bean = bean;
    }

    /**
     * @Description: 从容器中找出被`ControllerAdvice`标注的所有类，构建一个`ControllerAdviceBean`集合返回
     * @Author: duanfan
     * @Date: 2021/3/22 17:26
     * @Param context:
     * @return: java.util.List<org.df.smartmvc.handler.exception.ControllerAdviceBean>
    **/
    public static List<ControllerAdviceBean> findAnnotatedBeans(ApplicationContext context) {
        Map<String, Object> beanMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, Object.class);
        return beanMap.entrySet().stream()
                .filter(entry -> hasControllerAdvice(entry.getValue()))
                .map(entry -> new ControllerAdviceBean(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
    /**
     * @Description: 判断类上是否有注解`ControllerAdvice`，在开发handlerMapping的初始化是也有类似的操作
     * @Author: duanfan
     * @Date: 2021/3/22 17:25
     * @Param bean:
     * @return: boolean
    **/
    private static boolean hasControllerAdvice(Object bean) {
        Class<?> beanType = bean.getClass();
        return (AnnotatedElementUtils.hasAnnotation(beanType, ControllerAdvice.class));
    }


    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Class<?> getBeanType() {
        return beanType;
    }

    public void setBeanType(Class<?> beanType) {
        this.beanType = beanType;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }
}
