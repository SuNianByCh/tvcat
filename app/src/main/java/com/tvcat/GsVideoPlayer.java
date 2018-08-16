package com.tvcat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sunian.baselib.baselib.RxActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;


/**
 * Created by sunian on 2018/8/16.
 */

public class GsVideoPlayer extends RxActivity {
    @BindView(R.id.player)
    JZVideoPlayerStandard player;

    @Override
    public int getLayout() {
        return R.layout.video_gs;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initEventAndData() {
        Intent intent = getIntent();
        String recodURL = intent.getStringExtra("recodURL");
        String videopath = intent.getStringExtra("videopath");
        String title = getIntent().getStringExtra("title");

        player.setUp(videopath, JZVideoPlayer. SCREEN_WINDOW_NORMAL,title);

       // GSYVideoManager.instance().getPlayer().getMediaPlayer().setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "dns_cache_clear", 1);





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


}
