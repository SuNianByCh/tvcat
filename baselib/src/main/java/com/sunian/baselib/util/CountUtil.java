package com.sunian.baselib.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by fujun on 2018/4/10.
 * 计数器类
 */

public class CountUtil {

    private List<Disposable> disposableList = null;

    /**
     * 到计时，单位秒
     *
     * @param timeSends------int 时长，单位s
     * @param consumer--------   Consumer<Long>，计数器到时后的回调，不能为空
     */
    public void countBackMainThread(int timeSends, Consumer<Long> consumer) {
        if (consumer == null)
            return;
        Disposable subscribe = Observable.intervalRange(1, timeSends, 0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .filter(aLong -> {
                    if (timeSends - aLong <= 0) {
                        return true;
                    } else {

                        return false;
                    }
                })
                .subscribe(consumer);
        addDisposable(subscribe);
    }

    /**
     * 到计时，单位秒
     *
     * @param timeSends
     * @param next
     * @param complete
     */
    public void countBackMainThread(int timeSends, Consumer<Long> next, Consumer complete) {
        Disposable subscribe = Observable.intervalRange(1, timeSends, 0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (timeSends - aLong > 0) {
                        if (next != null)
                            next.accept(aLong);
                    } else if (complete != null) {
                        complete.accept(aLong);
                    }
                });
        addDisposable(subscribe);

    }


    private void addDisposable(Disposable disposable) {
        if (disposableList == null)
            disposableList = new ArrayList<>();
        disposableList.add(disposable);
    }


    public void destroyAllCount() {
        if (disposableList == null)
            return;
        for (Disposable dis : disposableList) {
            if (!dis.isDisposed())
                dis.dispose();
        }
    }
}
