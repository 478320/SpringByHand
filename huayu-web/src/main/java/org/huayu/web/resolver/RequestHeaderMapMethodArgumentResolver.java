package org.huayu.web.resolver;

import org.huayu.web.annotation.RequestHeader;
import org.huayu.web.convert.ConvertComposite;
import org.huayu.web.handler.HandlerMethod;
import org.huayu.web.support.WebServletRequest;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取所有请求头中的内容
 */
public class RequestHeaderMapMethodArgumentResolver implements HandlerMethodArgumentResolver{
    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        return parameter.hasParameterAnnotation(RequestHeader.class) && parameter.getParameterType() == Map.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod, WebServletRequest webServletRequest, ConvertComposite convertComposite) throws Exception {

        final HttpServletRequest request = webServletRequest.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String,String> resultMap = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            final String key = headerNames.nextElement();
            final String value = request.getHeader(key);
            resultMap.put(key,value);
        }


        return resultMap;
    }
}
