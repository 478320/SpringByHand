package org.springframework.annotation;

import java.lang.annotation.*;

/**
 * 用于判断一个Bean是单实例还是多实例bean
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {

    String value() ;

}
