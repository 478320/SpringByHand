package org.huayu.web.convert;

/**
 * String类型转换器
 */
public class StringConvert extends Convert<String>{


    public StringConvert(Class<String> type) {
        super(type);
    }

    @Override
    public Object convert(Object arg) throws Exception {
        return defaultConvert(arg.toString());
    }
}
