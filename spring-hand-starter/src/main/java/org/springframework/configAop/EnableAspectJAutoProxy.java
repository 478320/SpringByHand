package org.springframework.configAop;

import org.springframework.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 允许使用Aop的注解
 */
@Import(PointBeanPostProcess.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableAspectJAutoProxy {
}
