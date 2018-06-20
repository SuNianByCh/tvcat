package com.sunian.baselib.model.http;

import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.sunian.baselib.BuildConfig;
import com.sunian.baselib.app.AppConfig;
import com.sunian.baselib.util.FileUtil;
import com.sunian.baselib.util.NetworkIsAvilableUtil;

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
 * Created by fujun on 2018/4/8.
 */

public class HttpModel {

    private static OkHttpClient okHttpClient;

    public static synchronized OkHttpClient getOkHttpClient(final Context context) {
        if (okHttpClient == null) {
            synchronized (HttpModel.class) {
                if (okHttpClient != null)
                    return okHttpClient;
                File cacheFile = new File(FileUtil.getCacheFilePath(context.getApplicationContext(), true));
                Cache cache = new Cache(cacheFile, AppConfig.HTTP_CACHE_SISZ);
                Interceptor cacheInterceptor = chain -> {
                    Request request = chain.request();
                    String url = request.url().url().getPath();
                    if (!NetworkIsAvilableUtil.isNetworkAvailable(context)) {
                        request = request.newBuilder()
                                .cacheControl(CacheControl.FORCE_CACHE)
                                .build();
                    }
                    Response response = chain.proceed(request);
                    if (NetworkIsAvilableUtil.isNetworkAvailable(context)) {
                        int maxAge = 0;
                        // 有网络时, 不缓存, 最大保存时长为0
                        response.newBuilder()
                                .header("Cache-Control", "public, max-age=" + maxAge)
                                .removeHeader("Pragma")
                                .build();
                    } else {
                        // 无网络时，设置超时为4周
                        int maxStale = 60 * 60 * 24 * 28;
                        response.newBuilder()
                                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                .removeHeader("Pragma")
                                .build();
                    }
                    return response;
                };
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                //设置缓存
                //    builder.addNetworkInterceptor(cacheInterceptor);
                //     builder.addInterceptor(cacheInterceptor);
                if (BuildConfig.isdebug)
                    builder.addInterceptor(new StethoInterceptor());
                builder.cache(cache);
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

    public static Retrofit.Builder getRetrofitBuilder() {
        Retrofit.Builder builder = new Retrofit.Builder();
        return builder;
    }

    public static Retrofit createRetrofitString(Context context, String url) {
        return getRetrofitBuilder().baseUrl(url)
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpClient(context)).build();
    }
}
