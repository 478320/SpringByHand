package org.springframework.beans.factory;

/**
 * BeanNameAware回调接口
 */
public interface BeanNameAware {

    void setBeanName(String name);
}
