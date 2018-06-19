package com.tvcat;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.google.gson.Gson;
import com.tvcat.beans.UpdateBean;
import com.tvcat.util.HttpConstance;
import com.tvcat.util.HttpModel;
import com.tvcat.util.RegisterBeanHelper;

import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UpdatePresenter {
    IUpdate iUpdate;
    protected CompositeDisposable mCompositeDisposable;

    public UpdatePresenter(IUpdate iUpdate) {
        this.iUpdate = iUpdate;
    }

    public void checkUpdate() {
        try {
            PackageInfo packageInfo = App.instance.getPackageManager().getPackageInfo(App.instance.getPackageName(), 0);

            String url = HttpConstance.HTTP_UPDATE + "?bv=" + packageInfo.versionName
                    + "&token=" + RegisterBeanHelper.getToken() + "&os=android";
            Disposable subscribe = HttpModel.getApiServer().getBackString(url)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        if (iUpdate == null)
                            return;
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getInt("code") == 0) {
                            if (iUpdate != null)
                                iUpdate.update(new Gson().fromJson(jsonObject.getString("data"), UpdateBean.class));
                        } else {
                            iUpdate.update(null);
                        }


                    }, throwable -> {
                        if (iUpdate != null)
                            iUpdate.update(null);
                    });
            addSubscribe(subscribe);

        } catch (PackageManager.NameNotFoundException e) {

        }


    }

    protected void addSubscribe(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);

    }
}
