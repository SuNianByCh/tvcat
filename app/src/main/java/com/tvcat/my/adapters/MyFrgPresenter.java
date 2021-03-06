package com.tvcat.my.adapters;

import com.tvcat.LuncherPresenter;
import com.tvcat.UpdatePresenter;
import com.sunian.baselib.beans.MyInfos;
import com.tvcat.my.presenter.IMyView;
import com.sunian.baselib.model.http.HttpConstance;
import com.sunian.baselib.beans.RegisterBeanHelper;

import io.reactivex.disposables.Disposable;

public class MyFrgPresenter extends UpdatePresenter<IMyView> {

    private final LuncherPresenter lancherPresenter;

    public MyFrgPresenter() {
        super();
        lancherPresenter = new LuncherPresenter();
        lancherPresenter.getConfig();
    }

    public void getMyInfos() {
        if (hasNet(true, null)) {
            String url = HttpConstance.HTTP_ABOUT_ME + "?token=" + RegisterBeanHelper.getToken();
            Disposable subscribe = mDataManger.getHttp().httpGetObject(url, null, MyInfos.class, false)
                    .filter(p->mView!= null)
                    .subscribe(myInfos -> {
                        mView.stateMain(0);
                        mView.resultInfos(myInfos);
                    }, throwable -> handerException(throwable));
            addSubscribe(subscribe);
        }



    }

    public void getConfig(){
        lancherPresenter.getConfig();
    }

    @Override
    public void detachView() {
        lancherPresenter.detachView();
        super.detachView();
    }

    @Override
    public void attachView(IMyView view) {
        lancherPresenter.attachView(view);
        super.attachView(view);
    }
}
