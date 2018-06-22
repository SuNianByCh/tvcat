package com.tvcat.util;

import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by sunian on 2018/6/22.
 */

public class WebViewUtil {

    public void setWebView(WebView wv){
        wv.getSettings().setJavaScriptEnabled(true);

        wv.getSettings().setSavePassword(false);
        wv.setVerticalScrollBarEnabled(false);
        wv.setHorizontalScrollBarEnabled(false);
// 设置可以支持缩放
        wv.getSettings().setSupportZoom(true);
// 扩大比例的缩放
        wv.getSettings().setUseWideViewPort(true);
// 自适应屏幕
        wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wv.getSettings().setLoadWithOverviewMode(true);


    }
    public void setWebView(com.tencent.smtt.sdk.WebView wv){
        wv.getSettings().setJavaScriptEnabled(true);

        wv.getSettings().setSavePassword(false);
        wv.setVerticalScrollBarEnabled(false);
        wv.setHorizontalScrollBarEnabled(false);
// 设置可以支持缩放
        wv.getSettings().setSupportZoom(true);
// 扩大比例的缩放
        wv.getSettings().setUseWideViewPort(true);
// 自适应屏幕
        wv.getSettings().setLayoutAlgorithm(com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wv.getSettings().setLoadWithOverviewMode(true);

    }
}
