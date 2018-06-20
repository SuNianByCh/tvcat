package com.sunian.baselib.app;

import android.content.Context;

import com.sunian.baselib.model.db.DBHelperImp;
import com.sunian.baselib.model.db.IDBHelper;
import com.sunian.baselib.model.http.HttpImp;
import com.sunian.baselib.model.http.IHttp;
import com.sunian.baselib.model.preferences.IPreferencesHelper;
import com.sunian.baselib.model.preferences.PreferencesHelperImp;

/**
 * Created by fujun on 2018/4/8.
 * 数据的管理类
 */

public class DataManger {
    private static DataManger instance;
    private IHttp mHttp;
    private IDBHelper mDbHelper;
    private IPreferencesHelper mPreferencesHelper;
    private Context context;

    private DataManger(IHttp mHttp, IDBHelper mDbHelper, IPreferencesHelper mPreferencesHelper,Context context) {
        this.mHttp = mHttp;
        this.mDbHelper = mDbHelper;
        this.mPreferencesHelper = mPreferencesHelper;
        this.context = context;
    }

    public static void init(Context contexts) {
        Context context = contexts.getApplicationContext();
        synchronized (DataManger.class) {
            if (instance == null)
                synchronized (DataManger.class) {
                    if (instance == null) {
                        instance = new DataManger(new HttpImp(context), new DBHelperImp(context), new PreferencesHelperImp(context),context);
                    }
                }

        }

    }

    public static DataManger instance() {
        return instance;
    }

    public IHttp getHttp() {
        return mHttp;
    }

    public void setHttp(IHttp mHttp) {
        this.mHttp = mHttp;
    }

    public IDBHelper getDbHelper() {
        return mDbHelper;
    }

    public IPreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public Context getContext() {
        return context;
    }
}
