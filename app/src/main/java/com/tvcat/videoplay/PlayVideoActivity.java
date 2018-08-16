package com.tvcat.videoplay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.tvcat.R;
import com.tvcat.util.HttpConstance;
import com.tvcat.util.HttpModel;
import com.tvcat.util.RegisterBeanHelper;

import java.util.HashMap;

import io.reactivex.schedulers.Schedulers;


/**
 * Created by lishangwei on 16/12/1.
 */

public class PlayVideoActivity extends AppCompatActivity {


    GiraffePlayer player;

    String videopath;
    private Intent intent;
    private String recodURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen(this);
        setContentView(R.layout.activity_playvideo);
        player = new GiraffePlayer(this, true);
        intent = getIntent();
        recodURL = intent.getStringExtra("recodURL");
        videopath = intent.getStringExtra("videopath");
        String title = getIntent().getStringExtra("title");
        player.setTitle("" + title);
        player.play(videopath);
        player.setProgressPlay(intent.getIntExtra("progress", 0) * 1000);

/*
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏应用程序的标题栏，即当前activity的label
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 隐藏android系统的状态栏*/
        // 视频播放时开启屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }


    public static void start(Context context, String title, String videopath, String recodURL) {
        if (context == null)
            return;
        Intent intent = new Intent(context, PlayVideoActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("videopath",videopath);
        intent.putExtra("recodURL",recodURL);
        context.startActivity(intent);

    }


    public void initplay() {
        //player.setScaleType(GiraffePlayer.SCALETYPE_FILLPARENT );
        /*player.setDefaultRetryTime(config.defaultRetryTime);
        player.setFullScreenOnly(config.fullScreenOnly);
        player.setShowNavIcon(config.showNavIcon);*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        savePlayTime();
        if (player != null) {
            player.onDestroy();
        }

        //关闭屏幕常量
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }


    public void fullScreen(Activity activity) {
       /* if (activity == null)
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

        activity = null;*/

        requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏应用程序的标题栏，即当前activity的label
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 隐藏android系统的状态栏
    }


    /**
     * 保存时间播放时间进度
     */
    private void savePlayTime() {
        if (player == null || recodURL == null)
            return;
        long currectProgress = player.getCurrectProgress();
        if (currectProgress < 0)
            return;


        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("url", recodURL);
        hashMap.put("token", RegisterBeanHelper.getToken());
        hashMap.put("progress", "" + currectProgress / 1000);
        HttpModel.getApiServer().postBackString(HttpConstance.HTTP_SAVE_PLAY_TIME, hashMap).observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io()).subscribe(s -> {

            s.toString();
        }, throwable -> {
            throwable.toString();

        });
    }
}
