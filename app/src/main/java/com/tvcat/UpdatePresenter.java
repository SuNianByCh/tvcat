package com.tvcat;

import com.sunian.baselib.baselib.RxPresenter;
import com.sunian.baselib.util.APKVersionCodeUtil;
import com.sunian.baselib.beans.UpdateBean;
import com.sunian.baselib.model.http.HttpConstance;
import com.sunian.baselib.beans.RegisterBeanHelper;

import io.reactivex.disposables.Disposable;

public class UpdatePresenter<T extends IUpdateView> extends RxPresenter<T> {


    public void checkUpdate(boolean isNoInterBack) {


        if (hasNet(isNoInterBack, null)) {
            String url = HttpConstance.HTTP_UPDATE + "?bv=" + APKVersionCodeUtil.getVerName(App.instance)
                    + "&token=" + RegisterBeanHelper.getToken() + "&os=android";
            Disposable subscribe = mDataManger.getHttp().httpGetObject(url, null, UpdateBean.class, false)
                    .filter(p -> mView != null)
                    .subscribe(updateBean -> {
                        mView.stateMain(0);
                        mView.update(updateBean);
                    },throwable -> {});
            addSubscribe(subscribe);
        }


    }

}
