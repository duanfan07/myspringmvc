package org.df.smartmvc.view.resovler;

import org.df.smartmvc.view.View;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-22 15:28
 **/
public abstract class AbstractCachingViewResolver implements ViewResolver {

    //多线程的锁对象
    private final Object lock = new Object();
    //默认的空视图,当通过`viewName`解析不到视图返回null时，把默认的视图放入到缓存中
    private static final View UNRESOLVED_VIEW = (model, request, response) -> {
    };
    //缓存通过viewName得到的View
    private Map<String, View> cachedViews = new HashMap<>();

    @Override
    public View resolveViewName(String viewName) throws Exception {
        //先查缓存
        View view = cachedViews.get(viewName);
        if (Objects.nonNull(view)) {
            return (view != UNRESOLVED_VIEW ? view : null);
        }

        synchronized (lock) {
            view = cachedViews.get(viewName);
            if (Objects.nonNull(view)) {
                return (view != UNRESOLVED_VIEW ? view : null);
            }
            //生成view
            view = createView(viewName);
            if (Objects.isNull(view)) {
                view = UNRESOLVED_VIEW;
            }
            //加入缓存
            cachedViews.put(viewName, view);
        }
        //如果缓存中获取到的视图是`UNRESOLVED_VIEW`，那么就返回null
        return (view != UNRESOLVED_VIEW ? view : null);
    }

    protected abstract View createView(String viewName);
}
