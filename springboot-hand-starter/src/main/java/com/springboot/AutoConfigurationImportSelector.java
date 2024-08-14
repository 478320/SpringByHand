package com.springboot;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 依赖选择器（未实现）
 */
public class AutoConfigurationImportSelector implements DeferredImportSelector {

    // 每个判断性能太低，所以使用SPI来注入
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[0];
    }
}
