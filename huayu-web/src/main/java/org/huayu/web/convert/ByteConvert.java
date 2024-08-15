package org.huayu.web.convert;

/**
 * byte类型转换器
 */
public class ByteConvert extends Convert<Byte> {


    public ByteConvert(Class<Byte> type) {
        super(type);
    }

    @Override
    public Object convert(Object arg) throws Exception {
        return defaultConvert(arg.toString());
    }
}
