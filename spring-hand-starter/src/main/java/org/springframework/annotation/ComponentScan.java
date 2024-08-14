package org.springframework.annotation;

import java.lang.annotation.*;

/**
 * 包扫描注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ComponentScan {

    String value() ;

}
