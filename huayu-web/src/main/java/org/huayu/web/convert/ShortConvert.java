package org.huayu.web.convert;

/**
 * Short类型转换器
 */
public class ShortConvert extends Convert<Short>{


    public ShortConvert(Class<Short> type) {
        super(type);
    }

    @Override
    public Object convert(Object arg) throws Exception {
        return defaultConvert(arg.toString());
    }
}
