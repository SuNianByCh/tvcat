package com.tvcat.homepage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.sunian.baselib.app.AppConfig;
import com.sunian.baselib.baselib.RxActivity;
import com.sunian.baselib.util.LogUtil;
import com.sunian.baselib.util.StatusBarUtil;

import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tvcat.App;
import com.tvcat.GsVideoPlayer;
import com.tvcat.R;
import com.tvcat.util.WebViewUtil;
import com.tvcat.videoplay.PlayVideoActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * Created by sunian on 2018/7/3.
 */

public class VideoWebActivity extends RxActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.fl_content)
    FrameLayout mVideoContainer;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.wv)
    WebView wv;
    @BindView(R.id.rl_head)
    RelativeLayout rlHead;
    private WebViewUtil webViewUtil;
    private WebChromeClient.CustomViewCallback mCallBack;


    @Override
    public void setContentView(View view) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.setContentView(view);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_home_page_wv;
    }

    @Override
    protected void initListener() {
        RxView.clicks(ivBack).throttleFirst(AppConfig.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    onBackPressed();
                });
        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (pb == null)
                    return;
                if (newProgress >= 99)
                    pb.setVisibility(View.GONE);
                else {
                    pb.setVisibility(View.VISIBLE);
                    pb.setProgress(newProgress);
                }

            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback customViewCallback) {
                rlHead.setVisibility(View.GONE);
                showCustomView(view, customViewCallback);
                super.onShowCustomView(view, customViewCallback);
            }

            @Override
            public void onHideCustomView() {
                hideCustomView();
                super.onHideCustomView();
            }

        });


        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                url = url.toLowerCase();
                //return super.shouldInterceptRequest(view, url);

                if(url.endsWith(".m3u8") || url.endsWith(".mp4")){
                    finish();
                    PlayVideoActivity.start(mContext,tvTitle.getText().toString(),url,null);
                    return new WebResourceResponse(null, null, null);
                }

                LogUtil.i("http----->", url);

                if (!url.contains("img.cdxzx-tech.com") && !hasAd(getApplicationContext(), url))
                    return super.shouldInterceptRequest(view, url);
                else
                    return new WebResourceResponse(null, null, null);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // 获取页面内容
                view.loadUrl("javascript:window.java_obj.showSource("
                        + "document.getElementsByTagName('html')[0].innerHTML);");
                super.onPageFinished(view, url);

            }


        });
        wv.addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");
        String url = getIntent().getStringExtra("url");
        if (url == null)
            return;
        wv.loadUrl(url);
    }


    @Override
    protected void initEventAndData() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(mContext, R.color.main_color));
        webViewUtil = new WebViewUtil();
        webViewUtil.setWebView(wv);
        String title = getIntent().getStringExtra("title");
        setTitle(tvTitle, title, R.color.main_color);
    }

    @Override
    protected void onDestroy() {
        if (webViewUtil != null)
            webViewUtil.destroy(wv);
        super.onDestroy();
    }


    public static void start(Context context, String url, String title) {
        if (context == null || url == null)
            return;

        Intent intent = new Intent(context, VideoWebActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);

        intent = null;
    }


    /**
     * 视频播放全屏
     **/
    private void showCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        fullScreen();
        wv.setVisibility(View.GONE);
        mVideoContainer.setVisibility(View.VISIBLE);
        mVideoContainer.addView(view);
        mCallBack = callback;
    }


    @Override
    public void onBackPressed() {
        if (wv.canGoBack()) {
            wv.goBack();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 隐藏视频全屏
     */
    private void hideCustomView() {
        fullScreen();
        if (mCallBack != null) {
            mCallBack.onCustomViewHidden();
        }
        wv.setVisibility(View.VISIBLE);
        mVideoContainer.removeAllViews();
        mVideoContainer.setVisibility(View.GONE);
    }


    @Override
    protected void onPause() {
        super.onPause();
        wv.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        wv.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        wv.restoreState(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        wv.onResume();
    }

    public boolean hasAd(Context context, String url) {
        Resources res = context.getResources();
        String[] adUrls = res.getStringArray(R.array.adBlockUrl);
        for (String adUrl : adUrls) {
            if (url.contains(adUrl)) {
                return false;
            }
        }
        return false;
    }

    public final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            System.out.println("====>html=" + html);
        }

        @JavascriptInterface
        public void showDescription(String str) {
            System.out.println("====>html=" + str);
        }
    }


    private void fullScreen() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            rlHead.setVisibility(View.GONE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            rlHead.setVisibility(View.VISIBLE);
        }
    }


}
