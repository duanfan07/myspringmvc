package org.df.smartmvc.handler.interceptor;

import org.df.smartmvc.annotation.RequestMapping;
import org.df.smartmvc.http.RequestMethod;
import org.springframework.stereotype.Controller;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-19 15:51
 **/
@Controller
public class TestHandlerController {
    @RequestMapping(path = "/ex_test", method = RequestMethod.POST)
    public void exTest() {
    }

    @RequestMapping(path = "/in_test", method = RequestMethod.POST)
    public void inTest() {
    }


    @RequestMapping(path = "/in_test2", method = RequestMethod.POST)
    public void inTest2() {
    }

    @RequestMapping(path = "/in_test3", method = RequestMethod.POST)
    public void inTest3() {
    }
}
