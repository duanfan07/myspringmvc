package org.df.smartmvc.view;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-22 14:47
 **/
public abstract class AbstractView implements View {
    @Override
    public void render(Map<String, Object> model, HttpServletRequest request,
                       HttpServletResponse response) throws Exception {
        this.prepareResponse(request, response);
        this.renderMergedOutputModel(model, request, response);
    }

    //执行渲染的逻辑都将放入到这个方法中
    protected abstract void renderMergedOutputModel(
            Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception;
    //在实施渲染之前需要做的一些工作放入到这个方法中，比如：设置响应的头信息
    public void prepareResponse(HttpServletRequest request, HttpServletResponse response) {

    }

}
