package com.tvcat.my;

import com.tvcat.UpdatePresenter;
import com.tvcat.beans.MyInfos;
import com.tvcat.util.HttpConstance;
import com.tvcat.util.RegisterBeanHelper;

import io.reactivex.disposables.Disposable;

public class MyFrgPresenter extends UpdatePresenter<IMyView> {



    public void getMyInfos() {
        if (hasNet(true, null)) {
            String url = HttpConstance.HTTP_ABOUT_ME + "?token=" + RegisterBeanHelper.getToken();
            Disposable subscribe = mDataManger.getHttp().httpGetObject(url, null, MyInfos.class, false)
                    .subscribe(myInfos -> {
                        mView.stateMain(0);
                        mView.resultInfos(myInfos);
                    }, throwable -> handerException(throwable));
            addSubscribe(subscribe);
        }



    }


    public void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
            mCompositeDisposable.clear();
        }
    }


}
