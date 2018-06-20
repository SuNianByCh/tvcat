package com.sunian.baselib.model.http;


import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by fujun on 2018/4/24.
 */

public interface HttpRetrofit {
    @GET
    Flowable<Response<String>> httpGetString(@Url String url);

    /* @POST
     Flowable<String> http(@Url String url,)*/
    @POST
    @FormUrlEncoded
    Flowable<Response<String>> httPostString(@Url String url, @FieldMap Map<String, String> map);
}
