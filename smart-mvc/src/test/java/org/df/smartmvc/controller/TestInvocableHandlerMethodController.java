package org.df.smartmvc.controller;

import org.springframework.ui.Model;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-22 14:00
 **/
public class TestInvocableHandlerMethodController {

    //两个参数`HttpServletRequest`、`HttpServletResponse`能正常注入；通过`response`能正常输出内容给前端
    public void testRequestAndResponse(HttpServletRequest request, HttpServletResponse response) {
        Assert.notNull(request, "request can not null");
        Assert.notNull(response, "response can not null");
        try (PrintWriter writer = response.getWriter()) {
            String name = request.getParameter("name");
            writer.println("Hello InvocableHandlerMethod, params:" + name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //能正常注入`Model`类型的参数；执行完成之后能够在`ModelAndViewContainer`中拿到视图名称和`Model`中的数据
    public String testViewName(Model model) {
        model.addAttribute("blogURL", "http://silently9527.cn");
        return "/silently9527.jsp";
    }
}
