package org.df.smartmvc.controller;

import org.df.smartmvc.annotation.ResponseBody;
import org.df.smartmvc.view.View;
import org.df.smartmvc.vo.UserVo;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-22 11:13
 **/
public class TestReturnValueController {


    @ResponseBody
    public UserVo testResponseBody() {
        UserVo userVo = new UserVo();
        userVo.setBirthday(new Date());
        userVo.setAge(20);
        userVo.setName("Silently9527");
        return userVo;
    }

    public String testViewName() {
        return "/jsp/index.jsp";
    }

    public View testView() {
        return new View() {
            @Override
            public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
            }
        };
    }

    public Model testModel(Model model) {
        model.addAttribute("testModel", "Silently9527");
        return model;
    }

    public Map<String, Object> testMap() {
        Map<String, Object> params = new HashMap<>();
        params.put("testMap", "Silently9527");
        return params;
    }
}