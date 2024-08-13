package org.huayu.web.adapter;

import org.huayu.web.handler.HandlerMethod;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 适配器接口
 */
public interface HandlerMethodAdapter extends Ordered {

    boolean support(HandlerMethod handlerMethod);

    void handler(HttpServletRequest req, HttpServletResponse res, HandlerMethod handler) throws Exception;

}
