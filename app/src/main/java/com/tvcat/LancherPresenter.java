package com.tvcat;

import com.google.gson.Gson;
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

public class LancherPresenter {
    private CompositeDisposable mCompositeDisposable;

    private ILauncherView launcherView;

    public LancherPresenter(ILauncherView launcherView) {
        this.launcherView = launcherView;
    }


    public void getConfig() {
        if (!NetworkIsAvilableUtil.isNetworkAvailable(App.instance)) {
            if (launcherView != null)
                launcherView.noInterNet();
            return;
        }
        Disposable subscribe = HttpModel.getApiServer().getBackString(HttpConstance.HTTP_CONFIG)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {

                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getInt("code") == 0) {

                        ConfigBean configBean = new Gson().fromJson(jsonObject.getString("data"), ConfigBean.class);
                        App.setConfigBean(configBean);
                        if (launcherView == null)
                            launcherView.resultConfig(configBean);
                    } else {
                        if (launcherView != null) {
                            launcherView.getConfigFailed(jsonObject.getString("message"));
                        }
                    }


                }, throwable -> {
                    if (launcherView != null)
                        launcherView.getConfigFailed("访问服务器出错");
                });
        addSubscribe(subscribe);
    }


    public void unSubscribe() {


        launcherView = null;
    }

    protected void addSubscribe(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);

    }

}
