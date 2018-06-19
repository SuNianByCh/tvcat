package com.tvcat.homepage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tvcat.App;
import com.tvcat.UpdatePresenter;
import com.tvcat.beans.BannerBean;
import com.tvcat.beans.HomeBean;
import com.tvcat.beans.RegisterBean;
import com.tvcat.beans.UpdateBean;
import com.tvcat.util.HttpConstance;
import com.tvcat.util.HttpModel;
import com.tvcat.util.NetworkIsAvilableUtil;
import com.tvcat.util.RegisterBeanHelper;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter  extends UpdatePresenter{

    private HomPageView homPageView;
    private Gson gson;



    public HomePresenter(HomPageView homPageView) {
        super(homPageView);
        this.homPageView = homPageView;
    }

    public void httpRegister(HashMap<String, String> hashMap) {

        if (!NetworkIsAvilableUtil.isNetworkAvailable(App.instance)) {
            homPageView.noInternet();
            return;
        }
        gson = new Gson();
        Disposable subscribe = HttpModel.getApiServer().postBackString(HttpConstance.HTTP_register, hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {


                    JSONObject jsonObject = new JSONObject(s);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        RegisterBean registerBean = gson.fromJson(jsonObject.getString("data"), RegisterBean.class);
                        RegisterBeanHelper.init(registerBean);
                        getBanner();
                        getHomeListBean();
                        checkUpdate();

                    } else {
                        homPageView.failGetBanner(jsonObject.getString("message"));
                    }


                }, throwable -> {

                    homPageView.failReg("服务器连接错误");

                    //throwable.printStackTrace();
                });
        addSubscribe(subscribe);


    }


    private void getHomeListBean(){
        if (!NetworkIsAvilableUtil.isNetworkAvailable(App.instance)) {

            homPageView.noInternet();
            return;
        }

        if (RegisterBeanHelper.getToken() == null) {

            homPageView.failReg("注册失败");
        }

        Disposable subscribe = HttpModel.getApiServer()
                .getBackString(HttpConstance.HTTP_MEDIA+"?token="+RegisterBeanHelper.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(s -> {
                            JSONObject jsonObject = new JSONObject(s);
                            int code = jsonObject.getInt("code");
                            if (code == 0) {
                                Type type = new TypeToken<List<HomeBean>>() {
                                }.getType();
                                List<HomeBean> bannerBeanList = gson.fromJson(jsonObject.getString("data"), type);
                                homPageView.resultHomeBeanList(bannerBeanList);
                            } else {
                                homPageView.failGetBanner(jsonObject.getString("message"));
                            }


                        }
                        , throwable -> {
                            homPageView.failReg("服务器连接错误");

                        });


        addSubscribe(subscribe);
    }













    private void getBanner() {

        if (!NetworkIsAvilableUtil.isNetworkAvailable(App.instance)) {

            homPageView.noInternet();
            return;
        }

        if (RegisterBeanHelper.getToken() == null) {

            homPageView.failReg("注册失败");
        }


        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", RegisterBeanHelper.getToken());
        Disposable subscribe = HttpModel.getApiServer().getBackString(HttpConstance.HTTP_BANNER+"?token="+RegisterBeanHelper.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(s -> {
                            JSONObject jsonObject = new JSONObject(s);
                            int code = jsonObject.getInt("code");
                            if (code == 0) {
                                Type type = new TypeToken<List<BannerBean>>() {
                                }.getType();
                                List<BannerBean> bannerBeanList = gson.fromJson(jsonObject.getString("data"), type);
                                homPageView.resultBannerList(bannerBeanList);
                            } else {
                                homPageView.failGetBanner(jsonObject.getString("message"));
                            }


                        }
                        , throwable -> {
                            homPageView.failReg("服务器连接错误");

                        });


        addSubscribe(subscribe);
    }


    public void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
            mCompositeDisposable.clear();
        }
    }



}
