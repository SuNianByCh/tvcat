package com.tvcat.homepage;

import com.tvcat.UpdatePresenter;
import com.sunian.baselib.beans.BannerBean;
import com.sunian.baselib.beans.HomeBean;
import com.sunian.baselib.beans.RegisterBean;
import com.sunian.baselib.model.http.HttpConstance;
import com.sunian.baselib.beans.RegisterBeanHelper;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;

public class HomePresenter extends UpdatePresenter<IHomPageView> {


    public void httpRegister(HashMap<String, String> hashMap) {


        if (hasNet(true, null)) {

            Disposable subscribe = mDataManger.getHttp().httpPostObject(HttpConstance.HTTP_REGISTER, hashMap, RegisterBean.class, false)
                    .filter(p -> mView != null)
                    .subscribe(registerBean -> {
                        mView.stateMain(0);
                        RegisterBeanHelper.init(registerBean);
                        mView.registerSuccess();
                        getBanner();
                        getHomeListBean();
                        checkUpdate(false);
                    }, throwable -> handerException(throwable));
            addSubscribe(subscribe);
        }


    }


    private void getHomeListBean() {


        if (hasNet(true, null)) {
            String url = HttpConstance.HTTP_MEDIA + "?token=" + RegisterBeanHelper.getToken();
            Disposable subscribe = mDataManger.getHttp().httpGetList(url, null, HomeBean.class, true)
                    .filter(p -> mView != null)
                    .subscribe(homeBeans -> {
                        mView.resultHomeBeanList(homeBeans);
                        mView.stateMain(0);
                    }, throwable -> handerException(throwable));
            addSubscribe(subscribe);
        }



    }


    private void getBanner() {

        if (hasNet(true, null)) {
            String url =HttpConstance.HTTP_BANNER + "?token=" + RegisterBeanHelper.getToken();
            Disposable subscribe = mDataManger.getHttp().httpGetList(url, null, BannerBean.class, true)
                    .filter(p -> mView != null)
                    .subscribe(homeBeans -> {
                        mView.resultBannerList(homeBeans);
                        mView.stateMain(0);
                    }, throwable -> handerException(throwable));
            addSubscribe(subscribe);
        }


    }




}
