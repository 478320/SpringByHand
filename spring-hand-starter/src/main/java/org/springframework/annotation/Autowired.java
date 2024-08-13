package org.springframework.annotation;

import java.lang.annotation.*;

/**
 *
 */
@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {

    boolean require() default false;
}
