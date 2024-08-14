package org.springframework.annotation;

import java.lang.annotation.*;

/**
 * 导入包注解，但是这个注解没有真正的独立逻辑，只是被我用在EnableAop等注解上，扫描这些注解的流程
 * 会顺便扫描Import，暂时不能独立使用
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Import {
    Class<?>[] value();
}
