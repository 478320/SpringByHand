package org.huayu.web.resolver;

import org.huayu.web.annotation.Cookie;
import org.huayu.web.annotation.PathVariable;
import org.huayu.web.convert.ConvertComposite;
import org.huayu.web.excpetion.NotFoundExcpetion;
import org.huayu.web.handler.HandlerMethod;
import org.huayu.web.support.WebServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 解析Cookie当中的参数
 */
public class RequestCookieMethodArgumentResolver implements HandlerMethodArgumentResolver{
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Cookie.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod, WebServletRequest webServletRequest, ConvertComposite convertComposite) throws Exception {

        final Cookie parameterAnnotation = parameter.getParameterAnnotation(Cookie.class);
        String name = "";
        name = parameterAnnotation.value().equals("") ? parameter.getParameterName() : parameterAnnotation.value();
        final HttpServletRequest request = webServletRequest.getRequest();
        // 获取所有Cookie
        final javax.servlet.http.Cookie[] cookies = request.getCookies();
        // 遍历拿值
        for (javax.servlet.http.Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return convertComposite.convert(handlerMethod,parameter.getParameterType(),cookie.getValue());
            }
        }
        if (parameterAnnotation.require()){
            throw new NotFoundExcpetion(handlerMethod.getPath() + "cookie没有携带" + name);
        }
        return null;
    }
}
