package org.huayu.web.resolver;

import org.huayu.web.excpetion.ConvertCastExcpetion;
import org.huayu.web.excpetion.HttpRequestMethodNotSupport;
import org.huayu.web.excpetion.NotFoundExcpetion;
import org.huayu.web.handler.HandlerMethod;

import javax.security.sasl.SaslException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 默认异常解析器，尽可能枚举上层发生的异常进行处理
 */
public class DefaultHandlerExceptionResolver implements HandlerExceptionResolver{

    private int order;

    /**
     * 根据异常类型返回前端对应异常
     */
    @Override
    public Boolean resolveException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, Exception ex) throws IOException {

        final Class<? extends Exception> type = ex.getClass();
        if (type == ConvertCastExcpetion.class) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,ex.getMessage());
            return true;
        }else if (type == HttpRequestMethodNotSupport.class){
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, ex.getMessage());
            return true;
        }else if (type == NotFoundExcpetion.class){
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
            return true;
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 1;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
