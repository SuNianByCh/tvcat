package com.tvcat.util;

import android.util.Log;

import com.tvcat.BuildConfig;

public class LogUtil {

    public static void i(String tag, String info) {
        if (BuildConfig.DEBUG)
            Log.i(tag, info);
    }
    public static void e(String tag, String info) {
        if (BuildConfig.DEBUG)
            Log.e(tag, info);
    }
    public static void v(String tag, String info) {
        if (BuildConfig.DEBUG)
            Log.v(tag, info);
    }
    public static void w(String tag, String info) {
        if (BuildConfig.DEBUG)
            Log.i(tag, info);
    }
}
