package com.sunian.baselib.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;


import java.util.List;

/**
 * 判断系统网络状态的工具类
 * Created by MengJie on 2016/3/5.
 */
public class NetworkIsAvilableUtil {

    //判断网络是否可用
    public static boolean isNetworkAvailable(Context context) {
        if(context == null)
            return false;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            //如果仅仅是用来判断网络连接
            //则可以使用 cm.getActiveNetworkInfo().isAvailable();
            @SuppressLint("MissingPermission")
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // 判断GSP是否打开

    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = ((LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE));
        List<String> accessibleProviders = lm.getProviders(true);
        return accessibleProviders != null && accessibleProviders.size() > 0;
    }

    //判断wife是否打开
    @SuppressLint("MissingPermission")
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager mgrConn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (mgrConn != null) {
            return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
                    .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
                    .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
        }
        return false;
    }


    /**
     * 判断当前网络是否是wifi网络.
     *
     * @param context the context
     * @return boolean
     */
    @SuppressLint("MissingPermission")
    public static boolean isWifiConnectivity(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    //判断是wifi还是3g网络
    @SuppressLint("MissingPermission")
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }
}
