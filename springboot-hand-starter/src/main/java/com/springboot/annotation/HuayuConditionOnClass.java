package com.springboot.annotation;

import com.springboot.webServer.HuayuOnClassCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 *
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Conditional({HuayuOnClassCondition.class})
public @interface HuayuConditionOnClass {

    String value();
}
