package com.tvcat.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import com.tvcat.App;

import java.util.UUID;

import static android.text.TextUtils.isEmpty;

public class DeviceUtil {
    @SuppressLint("MissingPermission")
    public static String getDeviceId(Context context) {
        StringBuilder deviceId = new StringBuilder();
        // 渠道标志
        deviceId.append("a");
        try {
            //IMEI（imei）
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (!isEmpty(imei)) {
                deviceId.append("imei");
                deviceId.append(imei);
              //  LogUtil.e("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }
            //序列号（sn）
            String sn = tm.getSimSerialNumber();
            if (!isEmpty(sn)) {
                deviceId.append("sn");
                deviceId.append(sn);
             //   LogUtil.e("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }
            //如果上面都没有， 则生成一个id：随机码
            String uuid = getUUID(context);
            if (!isEmpty(uuid)) {
                deviceId.append("id");
                deviceId.append(uuid);
            //    LogUtil.e("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            deviceId.append("id").append(getUUID(context));
        }
      //  LogUtil.e("getDeviceId : ", deviceId.toString());
        return deviceId.toString();
    }

    /**
     * 得到全局唯一UUID
     */
    public static String getUUID(Context context) {
        SharedPreferences mShare = context.getSharedPreferences("sysCacheMap", Context.MODE_PRIVATE);
        String uuid = null;
        if (mShare != null) {
            uuid = mShare.getString("uuid", "");
        }
        if (isEmpty(uuid) || "".equals(uuid.trim())) {
            uuid = UUID.randomUUID().toString();
            SharedPreferences.Editor edit = mShare.edit();
            edit.putString("uuid", uuid).apply();

        }
        return uuid;
    }

    public static boolean hasUUID(Context context) {
        SharedPreferences mShare = context.getSharedPreferences("sysCacheMap", Context.MODE_PRIVATE);
        String uuid = null;
        if (mShare != null) {
            uuid = mShare.getString("uuid", "");
        }
        return !(isEmpty(uuid) || "".equals(uuid.trim()));
    }


    public static String getVersionName() {
        String versionName = null;

        PackageManager packageManager = App.instance.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(App.instance.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        return versionName;


    }

    public static int getVersionCode() {
        int code = -1;


        PackageManager packageManager = App.instance.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(App.instance.getPackageName(), 0);
            code = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        return code;


    }


}