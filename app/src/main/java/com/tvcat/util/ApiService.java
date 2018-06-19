package com.tvcat.util;

import java.util.HashMap;

import io.reactivex.Flowable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiService {
    @POST
    @FormUrlEncoded
    Flowable<String> postBackString(@Url String url, @FieldMap HashMap<String, String> hashMap);
    @GET
    Flowable<String> getBackString(@Url String url);
}
