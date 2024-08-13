package org.huayu.web.resolver;

import org.huayu.web.annotation.RequestHeader;
import org.huayu.web.convert.ConvertComposite;
import org.huayu.web.excpetion.NotFoundExcpetion;
import org.huayu.web.handler.HandlerMethod;
import org.huayu.web.support.WebServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取请求头中的指定内容
 */
public class RequestHeaderMethodArgumentResolver implements HandlerMethodArgumentResolver{
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestHeader.class) && parameter.getParameterType() != Map.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod, WebServletRequest webServletRequest, ConvertComposite convertComposite) throws Exception {

        String name = "";
        final RequestHeader parameterAnnotation = parameter.getParameterAnnotation(RequestHeader.class);
        name = parameterAnnotation.value().equals("") ? parameter.getParameterName() : parameterAnnotation.value();
        final HttpServletRequest request = webServletRequest.getRequest();
        if (parameterAnnotation.require() && ObjectUtils.isEmpty(request.getHeader(name))){
            throw new NotFoundExcpetion(handlerMethod.getPath() + "请求头没有携带" + name);
        }
        return convertComposite.convert(handlerMethod,parameter.getParameterType(),request.getHeader(name));
    }
}
