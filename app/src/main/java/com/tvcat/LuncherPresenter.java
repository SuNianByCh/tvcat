package com.tvcat;


import com.sunian.baselib.baselib.RxPresenter;
import com.tvcat.beans.ConfigBean;
import com.tvcat.util.HttpConstance;

import io.reactivex.disposables.Disposable;

public class LuncherPresenter extends RxPresenter<ILauncherView> {


    public LuncherPresenter() {
        super();

    }

    public void getConfig() {
        if (hasNet(true, null)) {
            Disposable subscribe = mDataManger.getHttp().httpGetObject(HttpConstance.HTTP_CONFIG, null, ConfigBean.class, true)
                    .filter(p -> mView != null)
                    .subscribe(configBean -> {
                        mView.stateMain(0);
                        mView.resultConfig(configBean);
                    }, throwable -> handerException(throwable));
            addSubscribe(subscribe);
        }
    }




}
