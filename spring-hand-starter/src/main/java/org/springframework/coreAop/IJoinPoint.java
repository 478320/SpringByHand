package org.springframework.coreAop;

import java.lang.annotation.Annotation;

/**
 * 切入点接口
 */
public interface IJoinPoint {

    Object[] getArgs();

    <T extends Annotation> T getAnnotation(Class<T> annotationClass);

    default Object invoke() throws Throwable {
        return null;
    }

    default Object invoke(Object[] args) throws Throwable {
        return null;
    }
}
