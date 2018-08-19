package com.tvcat.util;

import android.content.Context;

import com.sunian.baselib.model.http.HttpConstance;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
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
