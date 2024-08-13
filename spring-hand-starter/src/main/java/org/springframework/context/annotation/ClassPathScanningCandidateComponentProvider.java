package org.springframework.context.annotation;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ClassPathScanningCandidateComponentProvider implements ResourceLoaderAware {

    private final List<TypeFilter> includeFilters = new ArrayList<>();

    private final List<TypeFilter> excludeFilters = new ArrayList<>();

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {

    }

    protected void registerDefaultFilters() {
        // 添加过滤器，这就是我们将来扫描Component注解的操作
        // this.includeFilters.add(new AnnotationTypeFilter(Component.class));
        ClassLoader cl = ClassPathScanningCandidateComponentProvider.class.getClassLoader();
    }
}
