package com.sunian.baselib.util.Bus;

/**
 * Created by fujun on 2017/6/20.
 * rx 发送的事件
 */

public class RxBusEvent<T> {

    private int code;//事件类型
    private T object;//事件的内容

    public RxBusEvent(int code) {
        this.code = code;
    }

    /**
     * code 代表的意思
     */
    public RxBusEvent(int code, T object) {
        this.code = code;
        this.object = object;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
