package org.springframework.coreAop;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 *
 */
public class ProceedingJoinPoint extends JoinPoint{

    private final Object targetObject;


    private final MethodProxy methodProxy;

    public ProceedingJoinPoint(Method method, Object[] args, Object targetObject, MethodProxy methodProxy) {
        super(method, args);
        this.targetObject = targetObject;
        this.methodProxy = methodProxy;
    }

    @Override
    public Object invoke() throws Throwable {
        return method.invoke(targetObject,getArgs());
    }

    @Override
    public Object invoke(Object[] args) throws Throwable {
        return method.invoke(targetObject,args);
    }
}
