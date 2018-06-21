package com.tvcat;

import com.sunian.baselib.baselib.RxPresenter;
import com.sunian.baselib.util.APKVersionCodeUtil;
import com.tvcat.beans.UpdateBean;
import com.tvcat.util.HttpConstance;
import com.tvcat.util.RegisterBeanHelper;

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
