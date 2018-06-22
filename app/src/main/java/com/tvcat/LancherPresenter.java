package com.tvcat;

import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;
import com.sunian.baselib.baselib.RxPresenter;
import com.tvcat.beans.ConfigBean;
import com.tvcat.util.ApiService;
import com.tvcat.util.HttpConstance;
import com.tvcat.util.HttpModel;
import com.tvcat.util.NetworkIsAvilableUtil;

import org.json.JSONObject;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LancherPresenter extends RxPresenter<ILauncherView> {


    public LancherPresenter() {
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
