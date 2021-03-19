package org.df.smartmvc.controller;

import org.df.smartmvc.annotation.RequestBody;
import org.df.smartmvc.annotation.RequestMapping;
import org.df.smartmvc.annotation.RequestParam;
import org.df.smartmvc.http.RequestMethod;
import org.df.smartmvc.vo.UserVo;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-19 11:36
 **/
//创建TestConroller类，添加一个方法`test4`，在类上面标注注解`@Service`，解析完成后`test4`不能在注册中心里面找到
@Service
public class TestController {
    @RequestMapping(path = "/test4", method = RequestMethod.POST)
    public void test4(@RequestParam(name = "name") String name,
                      @RequestParam(name = "age") Integer age,
                      @RequestParam(name = "birthday") Date birthday,
                      HttpServletRequest request) {
    }

    @RequestMapping(path = "/user", method = RequestMethod.POST)
    public void user(@RequestBody UserVo userVo) {
    }

}
