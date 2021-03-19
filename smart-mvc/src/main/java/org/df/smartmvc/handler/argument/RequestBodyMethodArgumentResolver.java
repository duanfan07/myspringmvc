package org.df.smartmvc.handler.argument;

import com.alibaba.fastjson.JSON;
import org.df.smartmvc.annotation.RequestBody;
import org.df.smartmvc.exception.MissingServletRequestParameterException;
import org.df.smartmvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;

/**
 * @program: SmartMVC
 * @description: RequestBody注解的实现
 * @author: duanf
 * @create: 2021-03-19 17:25
 **/
public class RequestBodyMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //是否采用RequestBody注解，采用了则支持
        return parameter.hasParameterAnnotation(RequestBody.class);
    }

    /***
     * @Description: 把取出来的字符串通过fastjson转换成参数类型的对象
     * @Author: duanfan
     * @Date: 2021/3/19 17:38
     * @Param parameter:
     * @Param request:
     * @Param response:
     * @Param container:
     * @Param conversionService:
     * @return: java.lang.Object
    **/
    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request,
                                  HttpServletResponse response, ModelAndViewContainer container,
                                  ConversionService conversionService) throws Exception {

        String httpMessageBody = this.getHttpMessageBody(request);
        if (!StringUtils.isEmpty(httpMessageBody)) {
            return JSON.parseObject(httpMessageBody, parameter.getParameterType());
        }

        RequestBody requestBody = parameter.getParameterAnnotation(RequestBody.class);
        if (Objects.isNull(requestBody)) {
            return null;
        }
        if (requestBody.required()) {
            throw new MissingServletRequestParameterException(parameter.getParameterName(),
                    parameter.getParameterType().getName());
        }
        return null;
    }

    /**
     * @Description:  从request对象流中读取出数据转换成字符串
     * @Author: duanfan
     * @Date: 2021/3/19 17:37
     * @Param request:
     * @return: java.lang.String
    **/
    private String getHttpMessageBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        char[] buff = new char[1024];
        int len;
        while ((len = reader.read(buff)) != -1) {
            sb.append(buff, 0 , len);
        }
        return sb.toString();
    }
}
