package org.huayu.web.support;

import org.huayu.web.intercpetor.InterceptorRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class WebMvcComposite implements WebMvcConfigurer {

    private List<WebMvcConfigurer> webMvcConfigurers = new ArrayList<>();

    public void addWebMvcConfigurers(List<WebMvcConfigurer> webMvcConfigurers) {
        this.webMvcConfigurers.addAll(webMvcConfigurers);
    }

    /**
     * 遍历用户所有的自定义配置类，将所有的拦截器放到注册中心中
     */
    @Override
    public void addIntercept(InterceptorRegistry registry) {
        for (WebMvcConfigurer webMvcConfigurer : webMvcConfigurers) {
            webMvcConfigurer.addIntercept(registry);
        }
    }
}

