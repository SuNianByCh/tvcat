package com.sunian.baselib.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by fujun on 2018/4/11.
 */

public class ToastUtil {

    private static Toast toast;

    public static void show(String msg, Context context) {
        show(msg, Gravity.CENTER, context);
    }
    public static void show(String msg, int gravity, Context context) {
        if (msg == null || context == null)
            return;
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.setGravity(gravity, 0, 0);
        toast.show();
        context = null;
    }


}
