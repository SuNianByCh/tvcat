package com.tvcat.homepage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sunian.baselib.baselib.RxActivity;
import com.sunian.baselib.util.StatusBarUtil;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tvcat.R;
import com.tvcat.beans.LookHistParseBean;
import com.tvcat.beans.LookHistoryBean;
import com.tvcat.my.presenter.ILookHistoryView;
import com.tvcat.my.presenter.LookHistoryPresenter;
import com.tvcat.util.WebViewUtil;

import java.util.List;

import butterknife.BindView;

public class HomePageWebViewActivity extends RxActivity<LookHistoryPresenter, Object> implements ILookHistoryView<Object> {

    public static final String URL = "url";
    public static final String TITLE = "title";
    public static final String MPID = "mpid";
    public static final String IS_HISTROY = "is_histroy";
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.wv)
    com.tencent.smtt.sdk.WebView wv;
    @BindView(R.id.pb)
    ProgressBar pb;


    public String mpid = null;

    private String title;
    private View customView;
    private FullscreenHolder fullscreenContainer;
    private IX5WebChromeClient.CustomViewCallback customViewCallback;
    private boolean isHistory;
    private String url;



    @Override
    public int getLayout() {
        return R.layout.activity_home_page_wv;
    }

    @Override
    protected void initPresenter() {
        super.initPresenter();
        mPresenter = new LookHistoryPresenter();
    }

    @Override
    protected void initListener() {
        ivBack.setOnClickListener(v -> {
            finish();
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
            public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
                //  super.onShowCustomView(view, customViewCallback);
                showCustomView(view, customViewCallback);
            }

            @Override
            public void onHideCustomView() {
                //super.onHideCustomView();
                hideCustomView();
            }

        });

        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

             runOnUiThread(()->{
                 mPresenter.parsePlayUrl(url,mpid);
             });
                return true;
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                url = url.toLowerCase();
                if (!url.contains("img.cdxzx-tech.com") && !hasAd(getApplicationContext(), url))
                    return super.shouldInterceptRequest(view, url);
                else
                    return new WebResourceResponse(null, null, null);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }


        });
        if (isHistory) {
            mPresenter.parsePlayUrl(url,mpid);
        } else {
            wv.loadUrl(url);
        }


    }

    @Override
    protected void initEventAndData() {
        new WebViewUtil().setWebView(wv);
        StatusBarUtil.setColor(this, ContextCompat.getColor(getApplicationContext(), R.color.main_color));

        Intent intent = getIntent();
        title = intent.getStringExtra(TITLE);
        tvTitle.setText(title);
        url = intent.getStringExtra(URL);
        if (url == null)
            return;

        mpid = intent.getStringExtra(MPID);


        isHistory = intent.getBooleanExtra(IS_HISTROY, false);


    }






    @Override
    protected void onDestroy() {
        if (wv != null)
            wv.clearFormData();
        if (wv != null)
            wv.clearHistory();
        if (wv != null)
            wv.clearCache(true);
        if (wv != null)
            wv.destroy();
        super.onDestroy();
    }


    public boolean hasAd(Context context, String url) {
        Resources res = context.getResources();
        String[] adUrls = res.getStringArray(R.array.adBlockUrl);
        for (String adUrl : adUrls) {
            if (url.contains(adUrl)) {
                return true;
            }
        }
        return false;
    }


    @Override
    protected void onPause() {
        super.onPause();
        wv.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        wv.onResume();
    }

    /**
     * 视频播放全屏
     **/
    private void showCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }

        HomePageWebViewActivity.this.getWindow().getDecorView();

        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        fullscreenContainer = new FullscreenHolder(this);
        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        customView = view;
        //setStatusBarVisibility(false);
        customViewCallback = callback;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * 隐藏视频全屏
     */
    private void hideCustomView() {
        if (customView == null) {
            return;
        }

        //setStatusBarVisibility(true);
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        decor.removeView(fullscreenContainer);
        fullscreenContainer = null;
        customView = null;
        customViewCallback.onCustomViewHidden();
        wv.setVisibility(View.VISIBLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void resultLookHistory(List<LookHistoryBean> historyBeanList) {

    }

    @Override
    public void parseLook(LookHistParseBean lookHistParseBean) {
        lookHistParseBean.toString();
    }

    /**
     * 全屏容器界面
     */
    static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

    }


}
