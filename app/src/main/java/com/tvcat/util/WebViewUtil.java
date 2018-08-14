package com.tvcat.util;

import android.os.Build;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by sunian on 2018/6/22.
 */

public class WebViewUtil {

    public void setWebView(WebView wv) {
        wv.getSettings().setJavaScriptEnabled(true);

        wv.getSettings().setSavePassword(false);
        wv.setVerticalScrollBarEnabled(false);
        wv.setHorizontalScrollBarEnabled(false);
// 设置可以支持缩放
    //    wv.getSettings().setSupportZoom(true);
// 扩大比例的缩放
        wv.getSettings().setUseWideViewPort(true);
// 自适应屏幕
        wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wv.getSettings().setLoadWithOverviewMode(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wv.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        wv = null;
    }

    public void setWebView(com.tencent.smtt.sdk.WebView wv) {
        wv.getSettings().setJavaScriptEnabled(true);

        wv.getSettings().setSavePassword(false);
        wv.setVerticalScrollBarEnabled(false);
        wv.setHorizontalScrollBarEnabled(false);
// 设置可以支持缩放
        wv.getSettings().setSupportZoom(true);
// 扩大比例的缩放
        wv.getSettings().setUseWideViewPort(true);
    //    wv.getSettings().setBuiltInZoomControls(true);
// 自适应屏幕
        wv.getSettings().setLayoutAlgorithm(com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wv.getSettings().setLoadWithOverviewMode(true);

        wv.getSettings().setPluginState(com.tencent.smtt.sdk.WebSettings.PluginState.ON);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wv.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        wv = null;

    }

    public void destroy(com.tencent.smtt.sdk.WebView wv) {


        if (wv == null)
            return;
        ViewParent parent = wv.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(wv);
        }
        wv.stopLoading(); // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
        wv.getSettings().setJavaScriptEnabled(false);
        wv.clearHistory();
        wv.clearView();
        wv.removeAllViews();
        wv.destroy();


    }

    public void destroy(WebView wv) {

        if (wv == null)
            return;
        ViewParent parent = wv.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(wv);
        }
        wv.stopLoading(); // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
        wv.getSettings().setJavaScriptEnabled(false);
        wv.clearHistory();
        wv.clearView();
        wv.removeAllViews();
        wv.destroy();
    }
}
