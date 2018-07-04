package com.tvcat.homepage;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sunian.baselib.baselib.RxActivity;
import com.sunian.baselib.util.StatusBarUtil;
import com.sunian.baselib.util.StringUtil;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tvcat.R;
import com.tvcat.beans.LookHistParseBean;
import com.tvcat.beans.LookHistoryBean;
import com.tvcat.my.presenter.ILookHistoryView;
import com.tvcat.my.presenter.LookHistoryPresenter;
import com.tvcat.my.views.VipChargeViewActivity;
import com.tvcat.util.WebViewUtil;

import java.util.List;

import butterknife.BindView;

public class HomePageWebViewActivity extends RxActivity<LookHistoryPresenter, Object> implements ILookHistoryView<Object> {

    public static final String URL = "url";
    public static final String TITLE = "title";
    public static final String MPID = "mpid";
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


        });

        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.contains("http://m.iqiyi.com/v_") || url.contains("https://m.youku.com/video/id_")
                        || url.contains("http://m.le.com/vplay_")
                        || url.contains("https://m.mgtv.com/b/")
                        || url.contains("http://m.pptv.com/show/")
                        || url.contains("http://m.fun.tv/mplay/")
                        || url.contains("https://m.film.sohu.com/album/")
                        || url.contains("http://m.v.qq.com/x/cover/")) {
                    mPresenter.parsePlayUrl(url, mpid);

                } else
                    view.loadUrl(url);

                return true;
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);


            }
        });

        wv.loadUrl(url);


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

    }


    @Override
    protected void onDestroy() {
        new WebViewUtil().destroy(wv);
        super.onDestroy();
    }


    @Override
    public void showErrorMsg(String msg, int type) {
        if (type == 6008) {//冲值界面
            Intent intent = new Intent(mContext, VipChargeViewActivity.class);
            startActivity(intent);
        } else {
            super.showErrorMsg(msg, type);
        }

    }

    @Override
    public void resultLookHistory(List<LookHistoryBean> historyBeanList) {

    }

    @Override
    public void parseLook(LookHistParseBean lookHistParseBean) {
        if (lookHistParseBean == null)
            return;
        if (StringUtil.isNull(lookHistParseBean.getType()))
            return;

        if (TextUtils.equals(lookHistParseBean.getType(), "url")) {
            VideoWebActivity.start(mContext, lookHistParseBean.getUrl(), lookHistParseBean.getTitle());
        }

    }


}
