package com.tvcat.homepage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunian.baselib.baselib.RxActivity;
import com.sunian.baselib.util.StatusBarUtil;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tvcat.R;
import com.tvcat.util.WebViewUtil;

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
    @BindView(R.id.bt_fullScreen)
    Button btFullScreen;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.wv)
    WebView wv;
    @BindView(R.id.rl_head)
    RelativeLayout rlHead;
    private WebViewUtil webViewUtil;
    private FullscreenHolder fullscreenContainer;
    private IX5WebChromeClient.CustomViewCallback customViewCallback;
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private View customView;

    @Override
    public int getLayout() {
        return R.layout.activity_home_page_wv;
    }

    @Override
    protected void initListener() {
        backView(ivBack);
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
                rlHead.setVisibility(View.GONE);
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

                view.loadUrl(url);
                return true;
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                url = url.toLowerCase();
                //return super.shouldInterceptRequest(view, url);
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
                //  view.loadUrl(getClearAdDivJs(mContext));
                //view.loadUrl("javascript:hideAd();");

            }


        });
        wv.addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");
        String url = getIntent().getStringExtra("url");
        if (url == null)
            return;
        wv.loadUrl(url);
    }

    public String getClearAdDivJs(Context context) {
        String js = "javascript:function hideAd() {"
                + "var obj = document.getElementsByTagName('img'); alert(obj.length);"
                + "for(var i= 0;i<obj.length;i++){ obj[i].style.display='none';}"
                + "for(var i= 0;i<obj.length;i++){ obj[i].remove();}"
                + "}";


        // String js = "var $el = $('a[id^=__a_z_]'); $el.hide();var vids = document.getElementsByTagName('video');vids.width=100%;";
        return js;
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

   /*     if(TbsVideo.canUseTbsPlayer(context)){

            TbsVideo.openVideo(context,url);

            return;
        }

*/
        Intent intent = new Intent(context, VideoWebActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);

        intent = null;
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
        getWindow().getDecorView();

        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        fullscreenContainer = new FullscreenHolder(this);
        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        customView = view;
        //setStatusBarVisibility(false);
        customViewCallback = callback;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }


    @Override
    public void onBackPressed() {
        if (rlHead.getVisibility() != View.VISIBLE) {
            hideCustomView();
        } else
            super.onBackPressed();
    }

    /**
     * 隐藏视频全屏
     */
    private void hideCustomView() {
        if (customView == null) {
            return;
        }
        rlHead.setVisibility(View.VISIBLE);
        //setStatusBarVisibility(true);
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        decor.removeView(fullscreenContainer);
        fullscreenContainer = null;
        customView = null;
        customViewCallback.onCustomViewHidden();
        wv.setVisibility(View.VISIBLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
    protected void onPause() {
        super.onPause();
        wv.onPause();
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

}
