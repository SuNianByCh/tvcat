package com.tvcat.my.views;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.sunian.baselib.app.AppConfig;
import com.sunian.baselib.baselib.RxActivity;
import com.sunian.baselib.util.StatusBarUtil;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tvcat.R;
import com.tvcat.util.HttpConstance;
import com.tvcat.util.RegisterBeanHelper;
import com.tvcat.util.WebViewUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * Created by sunian on 2018/6/25.
 */

public class VipChargeViewActivity extends RxActivity {
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

    @Override
    public int getLayout() {
        return R.layout.activity_my_web;
    }

    @Override
    protected void initListener() {
        RxView.clicks(ivBack).throttleFirst(AppConfig.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe(o -> finish());

        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                if (pb == null)
                    return;
                if (i >= 99)
                    pb.setVisibility(View.GONE);
                else
                    pb.setVisibility(View.VISIBLE);
            }
        });
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }
        });
        String format = String.format(HttpConstance.HTTP_CHAGE_VIP, (RegisterBeanHelper.getId()+""));
        wv.loadUrl(format);
    }

    @Override
    protected void initEventAndData() {
        new WebViewUtil().setWebView(wv);
        StatusBarUtil.setColor(this, ContextCompat.getColor(mContext,R.color.main_color));
        tvTitle.setText("VIP充值");


    }


}
