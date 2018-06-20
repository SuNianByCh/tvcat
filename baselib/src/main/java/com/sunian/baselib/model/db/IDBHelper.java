package com.sunian.baselib.model.db;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by fujun on 2018/4/8.
 */

public interface IDBHelper {

    //查
    @Nullable
    String queryResponse(@NonNull String urlKey, @Nullable String params, long time);

    //增
    void insertResponse(@NonNull String urlKey, @Nullable String params, @NonNull String value);

    //删
    void deleteResponse(@NonNull String urlKey, @Nullable String params);

    void insertResponseJsonBean(@NonNull String urlKey,@Nullable String params,@NonNull Object object);

    void deleteAllResponse();
}
