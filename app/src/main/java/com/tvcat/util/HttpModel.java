package com.tvcat.util;

import android.content.Context;


import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sunian on 2018/4/8.
 */

public class HttpModel {

    private static OkHttpClient okHttpClient;
    private static Context context;
    private static ApiService apiService;

    public static void init(Context context) {
        HttpModel.context = context.getApplicationContext();
        getOkHttpClient(context);

    }

    public static synchronized OkHttpClient getOkHttpClient(final Context context) {
        if (okHttpClient == null) {
            synchronized (HttpModel.class) {
                if (okHttpClient != null)
                    return okHttpClient;

                OkHttpClient.Builder builder = new OkHttpClient.Builder();

                //设置超时
                builder.connectTimeout(10, TimeUnit.SECONDS);
                builder.readTimeout(20, TimeUnit.SECONDS);
                builder.writeTimeout(20, TimeUnit.SECONDS);
                //错误重连
                builder.retryOnConnectionFailure(true);
                okHttpClient = builder.build();
            }

        }


        return okHttpClient;
    }

    private static Retrofit.Builder getRetrofitBuilder() {
        Retrofit.Builder builder = new Retrofit.Builder();
        return builder;
    }
/*
    public static Retrofit createRetrofitJson(Context context, String url) {
        return getRetrofitBuilder()
                .baseUrl(url)
                .client(getOkHttpClient(context))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }*/

    private static Retrofit createRetrofitString() {
        return getRetrofitBuilder().baseUrl(HttpConstance.HOST_ADDRESS)
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpClient(context)).build();
    }

    public static synchronized ApiService getApiServer() {
        if (apiService == null) {
            synchronized (ApiService.class) {
                if (apiService == null)
                    apiService = createRetrofitString().create(ApiService.class);
            }
        }

        return apiService;
    }
}
