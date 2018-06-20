package com.sunian.baselib.app;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fujun on 2018/4/8.
 * Activity的管理类
 */

public class ActivityManager {
    private static List<Activity> allActivities;//保存所有的activity

    public static void addActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new ArrayList<>();
        }
        allActivities.add(act);
    }



    public static boolean hasActivity(Class clazz){
        if(allActivities == null)
            return false;
        else {
            boolean result = false;

            for (Activity activity : allActivities) {
                if (activity.getClass().isAssignableFrom(clazz)) {

                    result = true;
                    break;
                }
            }

            return result;

        }


    }

    public static boolean popToActivity(Class clazz) {
        if (allActivities == null)
            return false;
        else {
            boolean result = false;
            for (Activity activity : allActivities) {
                if (activity.getClass().isAssignableFrom(clazz)) {

                    result = true;
                    break;
                }
            }

            if (result) {
                ArrayList<Activity> activities = new ArrayList<>();
                for (int i = allActivities.size() - 1; i >= 0; i--) {
                    if (allActivities.get(i).getClass().isAssignableFrom(clazz)) {
                        break;
                    }
                    activities.add(allActivities.get(i));
                }
                for (Activity activity : activities) {
                    activity.finish();
                }
                activities.clear();
                activities = null;
            }


            return result;
        }
    }


    public static Activity getCurrentActivity() {
        if (allActivities == null || allActivities.isEmpty())
            return null;
        else
            return allActivities.get(allActivities.size() - 1);

    }

    /**
     * 移除activity
     *
     * @param act
     */
    public static void removeActivity(Activity act) {
        if (allActivities != null) {
            allActivities.remove(act);
        }
    }

    /**
     * 退出APP
     */
    public static void exitApp() {
        if (allActivities != null) {
            synchronized (allActivities) {
                for (Activity act : allActivities) {
                    act.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public static void setWindowAlpha(float alpha, Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = alpha; //0.0-1.0
        //    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setAttributes(lp);
        activity = null;
    }

    public static void fullScreen(Activity activity) {
        if (activity == null)
            return;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
        } else {
            Window window = activity.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
            window.setAttributes(attributes);
        }

        activity = null;
    }
}

