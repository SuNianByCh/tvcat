package com.sunian.baselib.model.http;

/**
 * Created by sunian on 2018/5/10.
 */

public class HttpException extends Exception {
    /**
     * 错误代码
     */
    public int code;
    /**
     * 错误说明
     */
    public String msg;
    /**
     * 是否为服务器的错误  true------是
     */
    public boolean isServiceError;
}

