package org.df.smartmvc.view.resovler;

import org.df.smartmvc.view.InternalResourceView;
import org.df.smartmvc.view.View;

/**
 * @program: SmartMVC
 * @description: 内部页面处理器
 * @author: duanf
 * @create: 2021-03-22 15:47
 **/
public class InternalResourceViewResolver extends  UrlBasedViewResolver {
    @Override
    protected View buildView(String viewName) {
        String url = getPrefix() + viewName + getSuffix();
        return new InternalResourceView(url);
    }
}
