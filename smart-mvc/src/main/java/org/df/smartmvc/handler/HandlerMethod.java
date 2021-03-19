package org.df.smartmvc.handler;

import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

/**
 * @program: SmartMVC
 * @description: 对应控制器中的方法(Controller中的每个方法)
 * @author: duanf
 * @create: 2021-03-19 09:48
 **/
public class HandlerMethod {



    //表示该方法的实例对象，也就是Controller的实例对象
    private Object bean;
    //表示的是我们写的Controller的类型
    private Class<?> beanType;
    //表示Controller中的方法
    private Method method;

    //表示方法中的所有参数的定义
    private List<MethodParameter> parameters;

    public HandlerMethod(Object bean, Method method) {
        this.bean = bean;
        this.beanType = bean.getClass();
        this.method = method;

        this.parameters = new ArrayList<>();
        int parameterCount = method.getParameterCount();
        for (int index = 0; index < parameterCount; index++) {
            parameters.add(new MethodParameter(method, index));
        }
    }

    public Object getBean() {
        return bean;
    }

    public Class<?> getBeanType() {
        return beanType;
    }

    public Method getMethod() {
        return method;
    }

    public List<MethodParameter> getParameters() {
        return parameters;
    }
}
