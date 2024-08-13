package com.springboot.annotation;

import com.springboot.AutoConfigurationImportSelector;
import com.springboot.webServer.HuayuOnClassCondition;
import com.springboot.webServer.WebServerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ComponentScan()
@Import(WebServerAutoConfiguration.class)
public @interface HuayuSpringBootApplication {
}
