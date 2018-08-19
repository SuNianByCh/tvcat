package com.tvcat.my.presenter;

import com.sunian.baselib.baselib.RxPresenter;
import com.sunian.baselib.beans.LookHistParseBean;
import com.sunian.baselib.beans.LookHistoryBean;
import com.sunian.baselib.model.http.HttpConstance;
import com.sunian.baselib.beans.RegisterBeanHelper;

import io.reactivex.disposables.Disposable;

/**
 * Created by sunian on 2018/6/22.
 */

public class LookHistoryPresenter extends RxPresenter<ILookHistoryView> {

    private Disposable subscribe;

    public void getLookHistory(int page) {
        if (!hasNet(true, null))
            return;
        String url = HttpConstance.HTTP_MEDIA_HISTORIES + "?token=" + RegisterBeanHelper.getToken() + "&page=" + page;

        Disposable subscribe = mDataManger.getHttp().httpGetList(url, null, LookHistoryBean.class, true)
                .filter(p -> mView != null)
                .subscribe(lookHistoryBeans -> {
                    mView.stateMain(0);
                    mView.resultLookHistory(lookHistoryBeans);
                }, throwable -> handerException(throwable));
        addSubscribe(subscribe);
    }

    public void parsePlayUrl(String sourceUrl, String id) {
        if (!hasNet(true, null)) {
            return;
        } else {
            String url = HttpConstance.HTTP_PLAYER + "?token=" + RegisterBeanHelper
                    .getToken() + "&mp_id=" + id + "&url=" + sourceUrl ;

            if (subscribe != null && !subscribe.isDisposed())
                subscribe.dispose();
            subscribe = mDataManger.getHttp().httpGetObject(url, null, LookHistParseBean.class, false)
                    .filter(p -> mView != null)
                    .subscribe(lookHistParseBean -> {
                                mView.stateMain(0);
                                mView.parseLook(lookHistParseBean);
                            }
                            , throwable -> handerException(throwable));
            addSubscribe(subscribe);
        }


    }
}
