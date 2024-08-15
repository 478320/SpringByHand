package org.huayu.web.convert;

/**
 * Long类型转换器
 */
public class LongConvert extends Convert<Long>{


    public LongConvert(Class<Long> type) {
        super(type);
    }

    @Override
    public Object convert(Object arg) throws Exception {
        return defaultConvert(arg.toString());
    }
}
