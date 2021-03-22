package org.df.smartmvc.view.resovler;

import org.df.smartmvc.view.View;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-22 15:26
 **/
public interface ViewResolver {
    //将viewName解析成View对象，所以参数是viewName，
    //处理完成后返回的对象是View
    View resolveViewName(String viewName) throws Exception;
}
