package org.huayu.web.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.huayu.web.annotation.RequestBody;
import org.huayu.web.convert.ConvertComposite;
import org.huayu.web.handler.HandlerMethod;
import org.huayu.web.support.WebServletRequest;
import org.springframework.core.MethodParameter;
import sun.jvm.hotspot.opto.RegionNode;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * 处理json，无需转换
 */
public class RequestRequestBodyMethodArgumentResolver implements HandlerMethodArgumentResolver{

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod, WebServletRequest webServletRequest, ConvertComposite convertComposite) throws Exception {

        final String json = getJson(webServletRequest.getRequest());

        return objectMapper.readValue(json, parameter.getParameterType());
    }

    public String getJson(HttpServletRequest request){

        final StringBuilder builder = new StringBuilder();
        String line = null;
        try(final BufferedReader reader = request.getReader()) {
            while (line != (line = reader.readLine())){
                builder.append(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return builder.toString();
    }
}
