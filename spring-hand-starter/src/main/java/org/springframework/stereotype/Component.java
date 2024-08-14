package org.springframework.stereotype;

import java.lang.annotation.*;

/**
 * 定义普通Bean的注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {

    String value() default "";

}
