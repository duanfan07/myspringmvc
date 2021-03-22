package org.df.smartmvc.controller;

import org.df.smartmvc.annotation.*;
import org.df.smartmvc.exception.TestException;
import org.df.smartmvc.http.RequestMethod;
import org.df.smartmvc.vo.ApiResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-22 17:18
 **/
@ControllerAdvice
@Controller
@RequestMapping(path = "/test")
public class DispatcherController {

    @RequestMapping(path = "/dispatch", method = RequestMethod.GET)
    public String dispatch(@RequestParam(name = "name") String name, Model model) {
        System.out.println("DispatcherController.dispatch: name=>" + name);
        model.addAttribute("name", name);
        return "redirect:/silently9527.cn";
    }

    @RequestMapping(path = "/dispatch2", method = RequestMethod.GET)
    public String dispatch2(@RequestParam(name = "name") String name) {
        System.out.println("DispatcherController.dispatch2: name=>" + name);
        //处理请求的过程中抛异常
        throw new TestException("test exception", name);
    }

    //当出现TestException异常会执行此方法
    @ResponseBody
    @ExceptionHandler({TestException.class})
    public ApiResponse exceptionHandler(TestException ex) {
        System.out.println("exception message:" + ex.getMessage());
        return new ApiResponse(200, "exception handle complete", ex.getName());
    }
}
