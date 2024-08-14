package org.springframework.annotation;

import java.lang.annotation.*;

/**
 * 自动注入注解
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {

    boolean require() default false;
}
