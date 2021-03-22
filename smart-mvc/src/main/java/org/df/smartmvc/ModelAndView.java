package org.df.smartmvc;

import org.df.smartmvc.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-19 13:53
 **/
public class ModelAndView {

    private Object view;
    private Model model;
    private HttpStatus status;

    public String getViewName() {
        return (this.view instanceof String ? (String) this.view : null);
    }

    public Object getView() {
        return view;
    }

    public void setView(Object view) {
        this.view = view;
    }

    public void setViewName(Object view) {
        this.view = view;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
