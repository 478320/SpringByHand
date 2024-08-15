package org.huayu.web.support;

import org.huayu.web.intercpetor.InterceptorRegistry;

/**
 * MVC的配置类，拦截器等在这里配置，定义拓展点规范供用户自定义子类实现
 */
public interface WebMvcConfigurer {

    default void addIntercept(InterceptorRegistry registry){}
}
