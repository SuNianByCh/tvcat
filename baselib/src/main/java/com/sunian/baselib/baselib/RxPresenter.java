package com.sunian.baselib.baselib;

import android.app.Activity;

import com.sunian.baselib.app.ActivityManager;
import com.sunian.baselib.app.DataManger;
import com.sunian.baselib.model.http.HttpException;
import com.sunian.baselib.util.NetworkIsAvilableUtil;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by fujun on 2017/6/14.
 * 基于Rx的Presenter封装,控制订阅的生命周期
 */

public class RxPresenter<T extends IBaseView> implements IBasePresenter<T> {
    protected T mView;
    protected CompositeDisposable mCompositeDisposable;
    protected DataManger mDataManger;

    public RxPresenter() {
        mDataManger = DataManger.instance();
    }

    protected void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
            mCompositeDisposable.clear();
        }
    }


    /**
     * 检查是否有网
     *
     * @param isCallBack---true
     * @param msg
     * @return
     */
    protected boolean hasNet(boolean isCallBack, String msg) {

        return hasNet(isCallBack, msg, 0);

    }


    /**
     * 检查是否有网
     *
     * @param isCallBack---true
     * @param msg
     * @return
     */
    protected boolean hasNet(boolean isCallBack, String msg, int type) {
        Activity currentActivity = ActivityManager.getCurrentActivity();
        if (currentActivity == null)
            return false;
        boolean networkAvailable = NetworkIsAvilableUtil.isNetworkAvailable(currentActivity.getApplication());
        if (networkAvailable) {
            return true;
        } else if (isCallBack) {
            if (mView == null)
                return false;
            mView.stateMain(type);
            mView.stateNoInternet(msg, type);
            return false;
        } else {
            return false;
        }
    }


    /**
     * 处理错误
     *
     * @param throwable
     */
    protected void handerException(Throwable throwable) {

        handerException(throwable, 0);
    }


    /**
     * 处理错误
     *
     * @param throwable
     */
    protected void handerException(Throwable throwable, int type) {
        if (mView == null)
            return;
        mView.stateMain(type);
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            mView.showErrorMsg(httpException.msg, type);
        } else {
            mView.showErrorMsg("访问服务器出错", type);
        }
    }

    protected void addSubscribe(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);

    }
/*
    protected <U> void addRxBusSubscribe(Class<U> evenType, Consumer<U> act) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        // mCompositeDisposable.add(RxBus.getInstance().)
    }*/

    @Override
    public void attachView(T view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        unSubscribe();
        this.mView = null;
    }
}