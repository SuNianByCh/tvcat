package com.sunian.baselib.model.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.sunian.baselib.util.DesCryptoUtils;

/**
 * Created by fujun on 2018/4/8.
 */

public class PreferencesHelperImp implements IPreferencesHelper {
    private Context context;

    private static final String ZHI_SHANG_JIN_RONG = "zhishangjinrong";
    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor;

    /**
     * 加密的密钥
     */
    private static final String de_en_key = "qwerasd";
    /**
     * 是否已登陆 ，true 已登陆
     */
    private boolean isLogIn;
    /**
     * 是否开启手势密码，true开启
     */
    private boolean hasOpenGesturePs;

    public PreferencesHelperImp(Context context) {
        this.context = context;
    }

    /**
     * 得到SharedPreferences
     * @return
     */
    protected SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(ZHI_SHANG_JIN_RONG, Context.MODE_PRIVATE);
        }
        return sharedPreferences;

    }


    /**
     * 得到SharedPreferences.editor;
     * @return
     */
    protected SharedPreferences.Editor getEditor() {
        if (editor != null)
            return editor;
        if (sharedPreferences == null)
            getSharedPreferences();
        editor = sharedPreferences.edit();
        return editor;

    }


    /**
     * 保存账号密码
     *
     * @param userName
     * @param password
     * @param isRemember 是否记住密码
     */
    public void saveUserAndPsd(String userName, String password, boolean isRemember) {
        //对数据进行加密

        if (userName != null) {
            String numberByte = DesCryptoUtils.encode(de_en_key, userName);
            if (numberByte != null) {
                getEditor().putString("userName", numberByte);
                getEditor().apply();
            }

        }
        if (!isRemember)
            return;
        if (password != null)
            password = DesCryptoUtils.encode(de_en_key, password);
        if (password != null) {
            getEditor().putString("password", password);
            getEditor().apply();
        }

    }

    @Override
    public String getPsd() {
        String psd = getSharedPreferences().getString("password", null);
        if (psd != null)
            psd = DesCryptoUtils.decode(de_en_key, psd);
        return psd;
    }

    @Override
    public String getUserName() {
        String psd = getSharedPreferences().getString("userName", null);
        if (psd != null)
            psd = DesCryptoUtils.decode(de_en_key, psd);
        return psd;
    }

    @Override
    public boolean hasLogIn() {
        if (!isLogIn)
            isLogIn = getSharedPreferences().getBoolean("isLogIn", false);
        return isLogIn;
    }

    @Override
    public void setLogIn(boolean isLogin) {
        this.isLogIn = isLogin;
        getEditor().putBoolean("isLogIn", isLogin).apply();
    }

    @Override
    public boolean hasOpenGesturePs() {
        if (!hasOpenGesturePs)
            hasOpenGesturePs = getSharedPreferences().getBoolean("hasOpenGesturePs", false);
        return hasOpenGesturePs;
    }

    @Override
    public void setOpenGesturePs(boolean isOpen) {
        this.hasOpenGesturePs = isOpen;
        getEditor().putBoolean("hasOpenGesturePs", isOpen).apply();
    }


}
