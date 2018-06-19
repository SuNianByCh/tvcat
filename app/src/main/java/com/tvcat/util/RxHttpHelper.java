package com.tvcat.util;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import org.reactivestreams.Publisher;

import javax.security.auth.Subject;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxHttpHelper {

    public static <T> Flowable<T> rxObjectT(@NonNull Flowable<String> flowable, Class<T> tClass) {

        return flowable.subscribeOn(Schedulers.io())
                .flatMap((Function<String, Flowable<T>>) s -> {

                    return Flowable.just(tClass.newInstance());
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

}
