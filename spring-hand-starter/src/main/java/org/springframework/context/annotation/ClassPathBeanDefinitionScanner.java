package org.springframework.context.annotation;

import org.springframework.core.io.ResourceLoader;
import org.springframework.core.support.BeanDefinitionRegistry;

/**
 *
 */
public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider{

    private final BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this(registry, true);
    }

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        this(registry, useDefaultFilters,
                (registry instanceof ResourceLoader ? (ResourceLoader) registry : null));
    }


    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters
                                          , ResourceLoader resourceLoader) {

        this.registry = registry;

        if (useDefaultFilters) {
            registerDefaultFilters();
        }
        // 设置ResourceLoader暂时没有给值
        setResourceLoader(resourceLoader);
    }
}
