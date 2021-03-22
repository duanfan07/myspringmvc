package org.df.smartmvc.view.resovler;

import org.df.smartmvc.utils.RequestContextHolder;
import org.df.smartmvc.view.RedirectView;
import org.df.smartmvc.view.View;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @program: SmartMVC
 * @description: 获取最好的视图
 * @author: duanf
 * @create: 2021-03-22 15:48
 **/
public class ContentNegotiatingViewResolver implements ViewResolver, InitializingBean {

    private List<ViewResolver> viewResolvers;
    private List<View> defaultViews;

    @Override
    public View resolveViewName(String viewName) throws Exception {
        List<View> candidateViews = getCandidateViews(viewName);
        View bestView = getBestView(candidateViews);
        if(Objects.nonNull(bestView)){
            return bestView;
        }
        return null;
    }

    /**
     * @Description: 从request中拿出头信息`Accept`，根据视图的ContentType从候选视图中匹配出最优的视图返回
     * @Author: duanfan
     * @Date: 2021/3/22 15:52
     * @Param candidateViews:
     * @return: org.df.smartmvc.view.View
    **/
    private View getBestView(List<View> candidateViews) {
        Optional<View> viewOptional = candidateViews.stream()
                .filter(view -> view instanceof RedirectView)
                .findAny();
        if (viewOptional.isPresent()) {
            return viewOptional.get();
        }

        HttpServletRequest request = RequestContextHolder.getRequest();
        Enumeration<String> acceptHeaders = request.getHeaders("Accept");
        while (acceptHeaders.hasMoreElements()) {
            for (View view : candidateViews) {
                if (acceptHeaders.nextElement().equals(view.getContentType())) {
                    return view;
                }
            }
        }
        return null;
    }

    /**
     * @Description: 通过视图名字使用`ViewResolver`解析出所有不为null的视图，
     * 如果默认视图不为空，把所有视图返回作为候选视图
     * @Author: duanfan
     * @Date: 2021/3/22 15:51
     * @Param viewName:
     * @return: java.util.List<org.df.smartmvc.view.View>
    **/
    private List<View> getCandidateViews(String viewName) throws Exception {
        List<View> candidateViews = new ArrayList<>();
        for (ViewResolver viewResolver : viewResolvers) {
            View view = viewResolver.resolveViewName(viewName);
            if (Objects.nonNull(view)) {
                candidateViews.add(view);
            }
        }
        if (!CollectionUtils.isEmpty(defaultViews)) {
            candidateViews.addAll(defaultViews);
        }
        return candidateViews;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(viewResolvers, "viewResolvers can not null");
    }

    public List<ViewResolver> getViewResolvers() {
        return viewResolvers;
    }

    public void setViewResolvers(List<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
    }

    public List<View> getDefaultViews() {
        return defaultViews;
    }

    public void setDefaultViews(List<View> defaultViews) {
        this.defaultViews = defaultViews;
    }
}
