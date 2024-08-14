package org.springframework.beans.factory;

/**
 * 初始化Bean接口
 */
public interface InitializingBean {

    void afterPropertiesSet() throws Exception;
}
