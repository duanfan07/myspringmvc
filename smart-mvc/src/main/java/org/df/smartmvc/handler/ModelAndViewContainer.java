package org.df.smartmvc.handler;

import org.df.smartmvc.http.HttpStatus;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.Objects;

/**
 * @program: SmartMVC
 * @description:每个请求进来都会新建一个对象，主要用于保存Handler处理过程中Model以及返回的View对象；
 *              该类将会用于参数解析器`HandlerMethodArgumentResolver`和Handler返回值解析器`HandlerMethodReturnValueHandler`
 * @author: duanf
 * @create: 2021-03-19 16:34
 **/
public class ModelAndViewContainer {

    //定义的类型是Object，是因为Handler既可以返回一个String表示视图的名字，也可以直接返回一个视图对象View
    private Object view;
    //Model、ExtendedModelMap都是Spring中定义的类，可以直接看做是Map
    private Model model;
    private HttpStatus status;
    //标记本次请求是否已经处理完成，后期在处理注解`@ResponseBody`将会使用到
    private boolean requestHandled = false;

    public void setView(Object view) {
        this.view = view;
    }

    public String getViewName() {
        return (this.view instanceof String ? (String) this.view : null);
    }

    public void setViewName(String viewName) {
        this.view = viewName;
    }

    public Object getView() {
        return this.view;
    }

    public boolean isViewReference() {
        return (this.view instanceof String);
    }

    public Model getModel() {
        if (Objects.isNull(this.model)) {
            this.model = new ExtendedModelMap();
        }
        return this.model;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public boolean isRequestHandled() {
        return requestHandled;
    }

    public void setRequestHandled(boolean requestHandled) {
        this.requestHandled = requestHandled;
    }

}
