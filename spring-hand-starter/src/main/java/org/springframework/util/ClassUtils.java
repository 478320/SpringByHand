package org.springframework.util;

/**
 *
 */
public class ClassUtils {

    private static final char PACKAGE_SEPARATOR = '.';

    public static final String CGLIB_CLASS_SEPARATOR = "$$";

    private static final char NESTED_CLASS_SEPARATOR = '$';

    public static String getShortName(String className) {

        int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
        int nameEndIndex = className.indexOf(CGLIB_CLASS_SEPARATOR);
        if (nameEndIndex == -1) {
            nameEndIndex = className.length();
        }
        String shortName = className.substring(lastDotIndex + 1, nameEndIndex);
        shortName = shortName.replace(NESTED_CLASS_SEPARATOR, PACKAGE_SEPARATOR);
        return shortName;
    }
}
