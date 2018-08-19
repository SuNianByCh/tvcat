package com.sunian.baselib.model.http;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.sunian.baselib.app.DataManger;
import com.sunian.baselib.baselib.RxPresenter;
import com.sunian.baselib.beans.RegisterBeanHelper;
import com.sunian.baselib.util.DeviceUtil;
import com.sunian.baselib.util.NetworkIsAvilableUtil;

import org.json.JSONObject;

import java.util.HashMap;

public class SeesinPrenster extends RxPresenter {

    public void startSensein() {
        if (hasNet(false, null)) {
            HashMap<String, String> map = new HashMap<>();
            if (RegisterBeanHelper.getToken() != null)
                map.put("token", RegisterBeanHelper.getToken());
            String location = getLocation();
            if (location != null) {
                map.put("loc", location);
            }
            if (NetworkIsAvilableUtil.isWifiEnabled(mDataManger.getContext())) {
                map.put("network", "wifi");
            } else {
                map.put("network", "3g");
            }

            String version = DeviceUtil.getVersionName();
            map.put("version", version);
            map.put("uuid", DeviceUtil.getUUID(DataManger.instance().getContext()));
            map.put("os", "android");
            map.put("osv", DeviceUtil.getSystemVersion());
            map.put("model", DeviceUtil.getSystemModel());
            map.put("screen", DeviceUtil.de());
            map.put("lang_code", DeviceUtil.getSystemLanguage());
            mDataManger.getHttp().httpPostString(HttpConstance.HTTP_START_SEESIN, map, false)
                    .subscribe(s -> {
                        String session_id = new JSONObject(s).getString("session_id");
                        RegisterBeanHelper.setSeesion_id(session_id);
                    }, throwable -> {
                        throwable.toString();
                    });
        }
    }


    public void endSensein() {
        if (hasNet(false, null)) {
            if (RegisterBeanHelper.getSeesion_id() == null || RegisterBeanHelper.getToken() == null)
                return;
            HashMap<String, String> map = new HashMap<>();
            map.put("token", RegisterBeanHelper.getToken());
            map.put("session_id", RegisterBeanHelper.getSeesion_id());
            mDataManger.getHttp().httpPostString(HttpConstance.HTTP_END_SEESNI, map, false,false)
                    .subscribe(s -> {
                        String session_id = new JSONObject(s).getString("session_id");
                        RegisterBeanHelper.setSeesion_id(session_id);
                    }, throwable -> {
                        throwable.toString();
                    });
        }
    }


    @SuppressLint("MissingPermission")
    private String getLocation() {
        String result = null;

        try {
            /**
             * 取得位置信息
             */
            if (LocationManager.NETWORK_PROVIDER == null) {
                return null;
            }

            LocationManager systemService = (LocationManager) mDataManger.getContext().getSystemService(Context.LOCATION_SERVICE);
            Location lastKnownLocation = systemService.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (lastKnownLocation == null) {
                return null;
            }
            double latitude = lastKnownLocation.getLatitude();
            double longitude = lastKnownLocation.getLongitude();

            result = longitude + "," + latitude;
        } catch (Throwable throwable) {

        }


        return result;
    }


}
