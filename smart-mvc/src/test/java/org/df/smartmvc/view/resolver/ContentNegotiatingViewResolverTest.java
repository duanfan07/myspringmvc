package org.df.smartmvc.view.resolver;

import org.df.smartmvc.utils.RequestContextHolder;
import org.df.smartmvc.view.InternalResourceView;
import org.df.smartmvc.view.RedirectView;
import org.df.smartmvc.view.View;
import org.df.smartmvc.view.resovler.ContentNegotiatingViewResolver;
import org.df.smartmvc.view.resovler.InternalResourceViewResolver;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Collections;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-22 15:56
 **/

//springmvc中的视图解析器，比如：`ContentNegotiatingViewResolver`、`BeanNameViewResolver`、
//`XmlViewResolver`等，特别是`ContentNegotiatingViewResolver`，
// 我们自己实现的是简版，springmvc的支持头信息，url后缀等方法。
public class ContentNegotiatingViewResolverTest {

    @Test
    public void resolveViewName() throws Exception {
        ContentNegotiatingViewResolver negotiatingViewResolver = new ContentNegotiatingViewResolver();
        negotiatingViewResolver.setViewResolvers(Collections.singletonList(new InternalResourceViewResolver()));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Accept", "text/html");
        RequestContextHolder.setRequest(request);

        View redirectView = negotiatingViewResolver.resolveViewName("redirect:/silently9527.cn");
        Assert.assertTrue(redirectView instanceof RedirectView); //判断是否返回重定向视图

        View forwardView = negotiatingViewResolver.resolveViewName("forward:/silently9527.cn");
        Assert.assertTrue(forwardView instanceof InternalResourceView); //

        View view = negotiatingViewResolver.resolveViewName("/silently9527.cn");
        Assert.assertTrue(view instanceof InternalResourceView); //通过头信息`Accept`，判断是否返回的`InternalResourceView`

    }
}
