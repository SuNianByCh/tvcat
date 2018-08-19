package com.tvcat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.sunian.baselib.baselib.RxActivity;
import com.sunian.baselib.util.StatusBarUtil;
import com.tvcat.homepage.SaveProgresPresenter;

import butterknife.BindView;


/**
 * Created by sunian on 2018/8/16.
 */

public class GsVideoPlayer extends RxActivity {
    @BindView(R.id.player)
    StandardGSYVideoPlayer videoPlayer;
    private OrientationUtils orientationUtils;
    private String recodURL;
    private String title;

    @Override
    public int getLayout() {
        return R.layout.video_gs;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initEventAndData() {
        StatusBarUtil.setColor(this, Color.BLACK);
        Intent intent = getIntent();
        recodURL = intent.getStringExtra("recodURL");
        String videopath = intent.getStringExtra("videopath");
        title = getIntent().getStringExtra("title");
        if (title == null) {
            title = "";
        }
        /*
        if (videopath.contains("= http:")) {
            String[] split = videopath.split("=http:");
            if (split.length > 1)
                videopath = "http:" + split[1];
        } else if (videopath.contains("= http:")) {
            String[] split = videopath.split("= http:");
            if (split.length > 1)
                videopath = "http:" + split[1];
        } else if (videopath.contains("=https:")) {
            String[] split = videopath.split("=https:");
            if (split.length > 1)
                videopath = "https:" + split[1];
        } else if (videopath.contains("= https:")) {
            String[] split = videopath.split("= https:");
            if (split.length > 1)
                videopath = "https:" + split[1];
        }*/
        String[] comps = videopath.split("url=");
        if (comps.length > 1) {
            videopath = comps[1];
        }
        videoPlayer.setShrinkImageRes(R.mipmap.ic_small_screen);
        videoPlayer.setEnlargeImageRes(R.mipmap.ic_full_screen);
        videoPlayer.setUp(videopath, false, title);
        init();

    }


    public static void start(Context context, String title, String videopath, String recodURL) {
        if (context == null)
            return;
        Intent intent = new Intent(context, GsVideoPlayer.class);
        intent.putExtra("title", title);
        intent.putExtra("videopath", videopath);
        intent.putExtra("recodURL", recodURL);
        context.startActivity(intent);

    }


    private void init() {
        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        videoPlayer.setThumbImageView(imageView);
        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);



        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);
        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setOnClickListener(v ->{
            orientationUtils.resolveByClick();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); ;//显示状态栏
        } );



        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);
        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(v -> onBackPressed());
        videoPlayer.startPlayLogic();
    }


    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        if (videoPlayer != null) {
            new SaveProgresPresenter().saveProgress(videoPlayer.getCurrentPositionWhenPlaying() + "", recodURL, title);
        }


        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onBackPressed() {
        //先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer.getFullscreenButton().performClick();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); ;//显示状态栏
            return;
        }
        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }

}
