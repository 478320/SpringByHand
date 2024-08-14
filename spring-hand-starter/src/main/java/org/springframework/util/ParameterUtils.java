package org.springframework.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 判断参数是否为基本数据类型的工具类
 */
public class ParameterUtils {

    private static final Set<Class<?>> WRAPPER_TYPES = new HashSet<>(Arrays.asList(
            Byte.class, Short.class, Integer.class, Long.class,
            Float.class, Double.class, Character.class, Boolean.class,
            String.class
    ));

    public static boolean isPrimitiveOrWrapperOrString(Class<?> clazz) {
        return clazz.isPrimitive() || WRAPPER_TYPES.contains(clazz);
    }
}
