package com.tvcat;

import android.app.Application;

import com.tvcat.beans.ConfigBean;
import com.tvcat.util.HttpModel;

public class App extends Application {
    public static App instance;
    private static ConfigBean configBean;

    public static ConfigBean getConfigBean() {
        return configBean;
    }

    public static void setConfigBean(ConfigBean configBean) {
        App.configBean = configBean;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        HttpModel.init(instance);
    }
}
