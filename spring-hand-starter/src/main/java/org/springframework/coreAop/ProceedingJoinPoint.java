package org.springframework.coreAop;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 切入点增强实现类（重要）
 */
public class ProceedingJoinPoint extends JoinPoint {

    private final Object targetObject;


    private final MethodProxy methodProxy;

    public ProceedingJoinPoint(Method method, Object[] args, Object targetObject, MethodProxy methodProxy) {
        super(method, args);
        this.targetObject = targetObject;
        this.methodProxy = methodProxy;
    }

    /**
     * 执行目标对象原方法
     */
    @Override
    public Object invoke() throws Throwable {
        return method.invoke(targetObject, getArgs());
    }

    /**
     * 执行目标对象原方法
     */
    @Override
    public Object invoke(Object[] args) throws Throwable {
        return method.invoke(targetObject, args);
    }
}
