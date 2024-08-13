package org.huayu.web.handler;

import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public interface HandlerMapping extends Ordered {

    HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception;
}
