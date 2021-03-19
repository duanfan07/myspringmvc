package org.df.smartmvc.controller;

import org.df.smartmvc.annotation.RequestMapping;
import org.df.smartmvc.http.RequestMethod;
import org.springframework.stereotype.Controller;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-19 11:30
 **/
@Controller
@RequestMapping(path = "/index")
public class IndexController {
    //在IndexController中我们在类上面配置path的前缀`/index`；解析完成后的path要拼接上`/index`
    //在IndexController中添加三个方法，其中`test`、`test2`两个用`@RequestMapping`标注，`test3`不标注；解析完成后`test3`不再我们注册中心里面，`test`、`test2`两个在注册中里面，并且`@RequestMapping`中的属性正确解析成`RequestMappingInfo`对象

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public void test(String name){

    }

    @RequestMapping(path = "/test2", method = RequestMethod.POST)
    public void test2(String name2) {

    }

    public void test3(String name3) {

    }
}
