package org.huayu.web.support;

import org.huayu.web.intercpetor.InterceptorRegistry;

/**
 * 定义拓展点规范供子类实现，都是default
 */
public interface WebMvcConfigurer {

    default void addIntercept(InterceptorRegistry registry){}
}
