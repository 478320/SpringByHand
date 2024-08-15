package org.huayu.web.convert;

/**
 * Boolean类型转换器
 */
public class BooleanConvert extends Convert<Boolean>{

    public BooleanConvert(Class<Boolean> type) {
        super(type);
    }

    @Override
    public Object convert(Object arg) throws Exception {

        return defaultConvert(arg.toString());
    }
}
