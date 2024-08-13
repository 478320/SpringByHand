package org.huayu.web.intercpetor;

import org.huayu.web.handler.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
public interface HandlerInterceptor {

    default boolean preHandle(HttpServletRequest request, HttpServletResponse response){
        return true;
    }

    default void  postHandle(HttpServletRequest request, HttpServletResponse response){}

    default void afterCompletion(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler,
                                 Exception ex){
    }
}
