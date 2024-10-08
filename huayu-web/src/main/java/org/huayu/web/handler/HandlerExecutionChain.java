package org.huayu.web.handler;

import org.huayu.web.intercpetor.HandlerInterceptor;
import org.huayu.web.intercpetor.MappedInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 映射器执行链类
 */
public class HandlerExecutionChain {

    private HandlerMethod handlerMethod;

    private List<HandlerInterceptor> handlerInterceptors = new ArrayList<>();

    public HandlerExecutionChain(HandlerMethod handlerMethod) {
        this.handlerMethod = handlerMethod;
    }

    public HandlerMethod getHandlerMethod() {
        return handlerMethod;
    }

    public void setHandlerInterceptors(List<HandlerInterceptor> handlerInterceptors) {
        // 路径映射匹配
        for (HandlerInterceptor interceptor : handlerInterceptors) {
            if (interceptor instanceof MappedInterceptor){
                if (((MappedInterceptor)interceptor).match(handlerMethod.getPath())){
                    this.handlerInterceptors.add(interceptor);
                }

            }else {
                this.handlerInterceptors.add(interceptor);
            }

        }

    }

    // 多个拦截器执行，一旦有一个拦截器返回false，整个链路就可以崩掉
    public boolean applyPreInterceptor(HttpServletRequest req, HttpServletResponse resp) {
        for (HandlerInterceptor handlerInterceptor : this.handlerInterceptors) {
            if (!handlerInterceptor.preHandle(req,resp)) {
                return false;
            }
        }
        return true;
    }

    public void applyPostInterceptor(HttpServletRequest req, HttpServletResponse resp) {
        for (HandlerInterceptor handlerInterceptor : this.handlerInterceptors) {
            handlerInterceptor.postHandle(req, resp);
        }
    }

    public void afterCompletion(HttpServletRequest req, HttpServletResponse resp, HandlerMethod handlerMethod, Exception ex) {
        for (HandlerInterceptor handlerInterceptor : this.handlerInterceptors) {
            handlerInterceptor.afterCompletion(req,resp,handlerMethod,ex);
        }
    }
}
