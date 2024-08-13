package org.huayu.web.convert;

public abstract class Convert<T> {

    protected Class<T> type;

    public Class<T> getType() {
        return type;
    }

    public Convert(Class<T> type){
        this.type = type;
     
    }
	
    protected abstract Object convert(Object arg) throws Exception;

    // 默认的类型转换逻辑
    protected Object defaultConvert(String text) throws Exception {
        return type.getConstructor(String.class).newInstance(text);
    }

}