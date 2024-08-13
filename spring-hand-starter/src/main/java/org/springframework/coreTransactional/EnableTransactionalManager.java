package org.springframework.coreTransactional;

import org.springframework.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 该类是为了导入一个选择器，目前不实现选择器暂时无任何作用
 */
@Retention(RetentionPolicy.RUNTIME)
@Import(TransactionalAop.class)
public @interface EnableTransactionalManager {
}
