package org.huayu.web.resolver;

import org.huayu.web.annotation.PathVariable;
import org.huayu.web.convert.ConvertComposite;
import org.huayu.web.handler.HandlerMethod;
import org.huayu.web.support.WebServletRequest;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 解析路径参数
 */
public class PathVariableMethodArgumentResolver implements HandlerMethodArgumentResolver{
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(PathVariable.class) && parameter.getParameterType() != Map.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod, WebServletRequest webServletRequest, ConvertComposite convertComposite) throws Exception {

        String name = "";
        Object result = null;
        // 1.获取 PathVariable 中的变量
        PathVariable parameterAnnotation = parameter.getParameterAnnotation(PathVariable.class);
        name = parameterAnnotation.value().equals("") ? parameter.getParameterName() : parameterAnnotation.value();
        Map<String,Object> resultMap = new HashMap<>();
        int index = -1;
        // 1.以/ 分割源path，找到变量 保存下标以及对应的变量
        final String path = handlerMethod.getPath();
        String[] split = path.split("/");
        for (int i = 0; i < split.length; i++) {
            final String s = split[i];
            if (s.contains("{") && s.contains("}") && s.contains(name)) {
                index = i;
                break;
            }
        }
        final HttpServletRequest request = webServletRequest.getRequest();
        // 2.以/ 分割请求path，根据上一步找到的下标，找到对应的值，放入resultMap
        split = request.getRequestURI().split("/");
        if (index != -1){
            result = split[index];
        }
        Object o = convertComposite.convert(handlerMethod,parameter.getParameterType(),result);
        return o;
    }
}
