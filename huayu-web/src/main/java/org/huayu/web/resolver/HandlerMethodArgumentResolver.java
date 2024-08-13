package org.huayu.web.resolver;

import org.huayu.web.convert.ConvertComposite;
import org.huayu.web.handler.HandlerMethod;
import org.huayu.web.support.WebServletRequest;
import org.springframework.core.MethodParameter;

/**
 * 参数解析器顶层接口
 */
public interface HandlerMethodArgumentResolver {

    // 当前请求携带数据格式是否支持当前参数
    boolean supportsParameter(MethodParameter parameter);
    // 解析参数
    Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod, WebServletRequest webServletRequest, ConvertComposite convertComposite) throws Exception;
}
