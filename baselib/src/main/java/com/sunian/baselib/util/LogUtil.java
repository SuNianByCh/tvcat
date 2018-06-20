package com.sunian.baselib.util;

import android.util.Log;

import com.sunian.baselib.BuildConfig;

/**
 * Created by sunian on 2018/5/10.
 */

public class LogUtil {
    public static void i(String tag, Object obj) {
        if (!BuildConfig.isdebug) {
            return;
        }
        if (tag == null || obj == null)
            return;
        Log.i(tag,obj.toString());
    }

    public static void e(String tag, Object obj) {
        if (!BuildConfig.isdebug) {
            return;
        }
        if (tag == null || obj == null)
            return;
        Log.e(tag,obj.toString());
    }

    public static void w(String tag, Object obj) {
        if (!BuildConfig.isdebug) {
            return;
        }
        if (tag == null || obj == null)
            return;
        Log.w(tag,obj.toString());
    }

    public static void v(String tag, Object obj) {
        if (!BuildConfig.isdebug) {
            return;
        }
        if (tag == null || obj == null)
            return;
        Log.v(tag,obj.toString());
    }



}
