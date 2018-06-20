package com.sunian.baselib.util;

import com.sunian.baselib.app.AppConfig;

/**
 * Created by fujun on 2017/6/19.
 */

public class FastClick {
    private static long clickTime = 0;

    public static boolean isFastClick() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - clickTime > AppConfig.THROTTLE) {
            clickTime = currentTimeMillis;
            return false;
        } else {
            clickTime = currentTimeMillis;
            return true;
        }

    }
}
