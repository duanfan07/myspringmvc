package org.df.smartmvc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-22 10:52
 **/
public interface View {
    //控制视图支持的ContentType是什么，默认是返回空
    default String getContentType() {
        return null;
    }
    //通过response把model中的数据渲染成视图返回给用户
    void render(Map<String, Object> model, HttpServletRequest request,
                HttpServletResponse response) throws Exception;
}
