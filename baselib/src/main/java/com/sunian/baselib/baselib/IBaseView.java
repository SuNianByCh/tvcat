package com.sunian.baselib.baselib;

/**
 * Created by fujun on 2017/6/14.
 */

public interface IBaseView<T> {
    void showErrorMsg(String msg, int type);

    void stateEmpty(String msg, int type);

    void stateLoading(String msg, int type);

    void stateMain(int type);

    void stateNoInternet(String msg, int type);

    void resultDate(T data, int type);
}
