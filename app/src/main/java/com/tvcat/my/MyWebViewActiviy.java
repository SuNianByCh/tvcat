package com.tvcat.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tvcat.App;
import com.tvcat.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyWebViewActiviy extends Activity {
    public final static int type_about_us = 1;
    public final static int type_online_serviece = 2;
    public final static int type_common_question = 3;
    public final static int type_ad = 4;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.wv)
    WebView wv;
    private int type;
    private View statusBarView;
    ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_web);
        type = getIntent().getIntExtra("type", 0);
        if (type == 0) {

            finish();
            return;
        }
        ButterKnife.bind(this);
        init();
    }


    void init() {
        starSinkBar(ContextCompat.getColor(getApplication(), R.color.main_color));
        WebSettings settings = wv.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                if (pb == null)
                    return;
                if (i < 99) {
                    pb.setProgress(i);
                    pb.setVisibility(View.VISIBLE);
                } else {
                    pb.setVisibility(View.GONE);
                }

            }
        });
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }
        });
        ivBack.setOnClickListener(v -> finish());
        switch (type) {
            case type_about_us:
                tvTitle.setText("关于我们");
                wv.loadUrl(App.getConfigBean().getAboutus_url());
                break;
            case type_common_question:
                tvTitle.setText("常见问题");
                wv.loadUrl(App.getConfigBean().getFaq_url());
                break;
            case type_online_serviece:
                tvTitle.setText("在线客服");
                wv.loadUrl(App.getConfigBean().getKefu_url());
                break;
            case type_ad:
                Intent intent = getIntent();
                tvTitle.setText(intent.getStringExtra("title"));
                wv.loadUrl(intent.getStringExtra("url"));
                break;

        }


    }


    /**
     * 开始沉浸式
     */
    private void starSinkBar(int color) {
//设置 paddingTop
        ViewGroup rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        // if (isPadding)
        // rootView.setPadding(0, getStatusBarHeight(), 0, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0 以上直接设置状态栏颜色
            getWindow().setStatusBarColor(color);
        } else  /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)*/ {
            //根布局添加占位状态栏
            ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
            if (statusBarView != null) {
                decorView.removeView(statusBarView);
            }
            statusBarView = new View(this);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight());
            statusBarView.setBackgroundColor(color);
            decorView.addView(statusBarView, lp);
        }
    }

    /**
     * 利用反射获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        wv.destroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (wv.canGoBack())
            wv.goBack();
        else
            super.onBackPressed();
    }
}
