package org.springframework.beans.factory;

/**
 * Bean类加载器Aware回调接口
 */
public interface BeanClassLoaderAware extends Aware {

	void setBeanClassLoader(ClassLoader classLoader);

}