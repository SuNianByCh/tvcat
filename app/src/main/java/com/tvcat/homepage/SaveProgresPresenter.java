package com.tvcat.homepage;

import com.sunian.baselib.baselib.RxPresenter;
import com.tvcat.util.HttpConstance;
import com.tvcat.util.RegisterBeanHelper;

import java.util.HashMap;


/**
 * Created by sunian on 2018/8/17.
 */

public class SaveProgresPresenter extends RxPresenter {

    /**
     * , url,progress,title
     */
    public void saveProgress(String progress, String url, String title) {
        if (hasNet(false, null)) {
            if (RegisterBeanHelper.getToken() == null || url == null || title == null)
                return;
            HashMap<String, String> map = new HashMap<>();
            map.put("token", RegisterBeanHelper.getToken());
            map.put("url", url);
            map.put("title", title);
            map.put("progress", progress);
            mDataManger.getHttp().httpPostString(HttpConstance.HTTP_SAVE_PLAY_TIME, map, false,false)
                    .subscribe(s -> {
                        s.toString();
                    }, throwable -> {
                        throwable.toString();
                    });


        }
    }
}
