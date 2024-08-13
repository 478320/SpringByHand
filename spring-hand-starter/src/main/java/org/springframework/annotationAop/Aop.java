package org.springframework.annotationAop;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component("aopTest")
public @interface Aop {

    //基于路径aop
    String jointPath() default "";

    //基于注解aop
    Class<? extends Annotation> joinAnnotationClass() default Void.class;
}
