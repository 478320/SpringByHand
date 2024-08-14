package com.springboot.annotation;

import com.springboot.webServer.HuayuOnClassCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * 用来判断一个类是否存在的注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Conditional({HuayuOnClassCondition.class})
public @interface HuayuConditionOnClass {

    String value();
}
