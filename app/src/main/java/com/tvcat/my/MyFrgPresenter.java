package com.tvcat.my;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.google.gson.Gson;
import com.tvcat.App;
import com.tvcat.UpdatePresenter;
import com.tvcat.beans.MyInfos;
import com.tvcat.beans.RegisterBean;
import com.tvcat.util.HttpConstance;
import com.tvcat.util.HttpModel;
import com.tvcat.util.NetworkIsAvilableUtil;
import com.tvcat.util.RegisterBeanHelper;

import org.json.JSONObject;

import java.util.HashMap;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MyFrgPresenter  extends UpdatePresenter{
    IMyView iMyView;


    public MyFrgPresenter(IMyView iMyView) {
        super(iMyView);
        this.iMyView = iMyView;
    }


    public void getMyInfos() {
        if (!NetworkIsAvilableUtil.isNetworkAvailable(App.instance)) {
            iMyView.noInterNet();
            return;
        }


        Disposable subscribe = HttpModel.getApiServer().getBackString(HttpConstance.HTTP_ABOUT_ME + "?token=" + RegisterBeanHelper.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {


                            JSONObject jsonObject = new JSONObject(s);

                            int code = jsonObject.getInt("code");
                            if (code == 0) {
                                iMyView.resultInfos(new Gson().fromJson(jsonObject.getString("data"), MyInfos.class));
                            }else {
                                iMyView.getMyInfosFailed(jsonObject.getString("message"));
                            }


                        }


                        , throwable ->  iMyView.getMyInfosFailed("服务器连接错误"));
        addSubscribe(subscribe);


    }










    public void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
            mCompositeDisposable.clear();
        }
    }


}
