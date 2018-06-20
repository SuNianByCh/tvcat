package com.sunian.baselib.baselib;

/**
 * Created by fujun on 2017/6/14.
 *
 * presenter 的基类
 */

public interface IBasePresenter<T extends IBaseView>  {
    void attachView(T view);
    void detachView();
}
