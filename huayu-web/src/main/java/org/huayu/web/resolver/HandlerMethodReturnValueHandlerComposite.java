package org.huayu.web.resolver;

import org.huayu.web.excpetion.NotFoundExcpetion;
import org.huayu.web.support.WebServletRequest;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 返回值处理器组合器
 */
public class HandlerMethodReturnValueHandlerComposite {

    private List<HandlerMethodReturnValueHandler> methodReturnValueHandlers = new ArrayList<>();

    public void addMethodReturnValueHandlers(List<HandlerMethodReturnValueHandler> methodReturnValueHandlers) {
        this.methodReturnValueHandlers.addAll(methodReturnValueHandlers);
    }

    /**
     * 选择返回值处理器
     */
    public HandlerMethodReturnValueHandler selectHandler(Method method) throws Exception {
        for (HandlerMethodReturnValueHandler returnValueHandler : this.methodReturnValueHandlers) {
            if (returnValueHandler.supportsReturnType(method)) {
                return returnValueHandler;
            }
        }
        throw new NotFoundExcpetion(method.toString() + "找不到返回值处理器");
    }

    /**
     * 处理返回值
     */
    public void doInvoke(Object returnValue, Method method, WebServletRequest webServletRequest) throws Exception {
        //选择返回值处理器
        final HandlerMethodReturnValueHandler returnValueHandler = selectHandler(method);
        //执行
        returnValueHandler.handleReturnValue(returnValue,webServletRequest);
    }
}
