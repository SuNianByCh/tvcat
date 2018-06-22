package com.tvcat.my.views;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sunian.baselib.baselib.RxActivity;
import com.sunian.baselib.util.StatusBarUtil;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tvcat.App;
import com.tvcat.R;
import com.tvcat.util.WebViewUtil;

import butterknife.BindView;

public class MyWebViewActiviy extends RxActivity {
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




    @Override
    public int getLayout() {
        return R.layout.activity_my_web;
    }

    @Override
    protected void initListener() {
        backView(ivBack);


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
    }

    @Override
    protected void initEventAndData() {
        StatusBarUtil.setColor(this,ContextCompat.getColor(mContext,R.color.main_color));
        type = getIntent().getIntExtra("type", 0);
        if (type == 0) {
            finish();
            return;
        }

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

        new WebViewUtil().setWebView(wv);
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
