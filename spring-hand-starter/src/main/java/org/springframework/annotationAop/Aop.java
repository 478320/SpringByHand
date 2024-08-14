package org.springframework.annotationAop;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 自定义的Aop注解，被标注了这个注解的类将成为一个Aop类，和Aspect类似
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
