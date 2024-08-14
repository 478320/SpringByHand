package org.springframework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，虽然Spring有办法获得方法参数名称，但是我们自定义的Spring在java17的开发环境下，
 * 想要获取到方法上的参数名称比较复杂，所以我设计了这个注解用于获取参数名称，方便@Bean注解的注册Bean
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface BeanName {

    String value();
}
