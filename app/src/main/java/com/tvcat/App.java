package com.tvcat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;
import com.sunian.baselib.app.DataManger;
import com.sunian.baselib.beans.ConfigBean;
import com.sunian.baselib.model.http.SeesinPrenster;

import com.tvcat.util.HttpModel;

import static android.content.ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN;

public class App extends MultiDexApplication {
    public static Application instance;
    private static ConfigBean configBean;
    private boolean isBackground;

   /* public App(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }*/

    public static ConfigBean getConfigBean() {
        return configBean;
    }

    public static void setConfigBean(ConfigBean configBean) {
        App.configBean = configBean;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance =this;
        HttpModel.init(this);

        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        // 调试时，将第三个参数改为true
        // Bugly.init(this, "ad55aae560", false);
        DataManger.init(this);
        // 置入一个不设防的VmPolicy（不设置的话 7.0以上一调用拍照功能就崩溃了）
        // 还有一种方式：manifest中加入provider然后修改intent代码
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        // Normal app init code...
        LeakCanary.install(this);
        if (BuildConfig.DEBUG) {

        }
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        };
        listenForForeground();
        listenForScreenTurningOff();
    }

/*    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onBaseContextAttached(Context base) {
      //  super.onBaseContextAttached(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
      //  ARouter.init(this);
        // 安装tinker
        // TinkerManager.installTinker(this); 替换成下面Bugly提供的方法
      //  Beta.installTinker(this);
    }*/

    private void listenForForeground() {
        this.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.e("TVCat", activity.getClass().getSimpleName() + " created");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.e("TVCat", activity.getClass().getSimpleName() + " started");
            }

            //...
            @Override
            public void onActivityResumed(Activity activity) {
                if (isBackground) {
                    isBackground = false;
                    notifyForeground();
                }

                Log.e("TVCat", activity.getClass().getSimpleName() + " resumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.e("TVCat", activity.getClass().getSimpleName() + " paused");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.e("TVCat", activity.getClass().getSimpleName() + " stopped");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.e("TVCat", activity.getClass().getSimpleName() + " save instance state");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.e("TVCat", activity.getClass().getSimpleName() + " destroyed");
            }
            //...
        });
    }

    private void listenForScreenTurningOff() {
        IntentFilter screenStateFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                isBackground = true;
                notifyBackground();
            }
        }, screenStateFilter);
        this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String reason = intent.getStringExtra("reason");
                Log.e("TVCat", reason);
                if (reason != null) {
                    if (reason.equals("homekey")) {
                        // home pressed
                        isBackground = true;
                        notifyBackground();
                    } else if (reason.equals("recentapps")) {
                        // recentapps pressed
//                        Log.e("TVCat", "recentapps pressed");
                        isBackground = true;
                        notifyBackground();
                    }
                }
            }
        }, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            isBackground = true;
            notifyBackground();
        }

    }

    private void notifyForeground() {
        // This is where you can notify listeners, handle session tracking, etc
        new SeesinPrenster().startSensein();
    }

    private void notifyBackground() {
        // This is where you can notify listeners, handle session tracking, etc

        new SeesinPrenster().endSensein();
    }

    public boolean isBackground() {
        return isBackground;
    }

}


