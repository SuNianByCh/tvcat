package com.tvcat.homepage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tvcat.R;
import com.tvcat.my.views.VIPChargeActivity;
import com.tvcat.util.ActivityManager;
import com.tvcat.util.DialogTip;
import com.tvcat.util.HttpConstance;
import com.tvcat.util.HttpModel;
import com.tvcat.util.RegisterBeanHelper;
import com.tvcat.videoplay.PlayVideoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomePageWebViewActivity extends AppCompatActivity {

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

    private View statusBarView;
    private Unbinder bind;
    public String mpid = null;
    private Disposable subscribe;
    private String title;
    private View customView;
    private FullscreenHolder fullscreenContainer;
    private IX5WebChromeClient.CustomViewCallback customViewCallback;

    private DialogTip dialogTip;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_wv);
        bind = ButterKnife.bind(this);
        ActivityManager.fullScreen(this);
        starSinkBar(ContextCompat.getColor(getApplicationContext(), R.color.main_color));

        ivBack.setOnClickListener(v -> {
            finish();
        });


        findViewById(R.id.bt_fullScreen).setOnClickListener(v -> {


            wv.loadUrl("javascript:" + "window.alert('Js injection success')");
            wv.loadUrl("javascript:var video=document.getElementById('myVideo') ;" +
                    "if (video.requestFullscreen) {" +
                    "    video.requestFullscreen();" +
                    "  } else if (video.mozRequestFullScreen) {" +
                    "    video.mozRequestFullScreen(); " +
                    "  } else if (video.webkitRequestFullscreen) {" +
                    "    video.webkitRequestFullscreen(); " +
                    "  }");
        });


        Intent intent = getIntent();
        title = intent.getStringExtra(TITLE);
        tvTitle.setText(title);
        String url = intent.getStringExtra(URL);
        if (url == null)
            return;

        mpid = intent.getStringExtra(MPID);

        wv.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setBlockNetworkImage(false);


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
        wv.getSettings().setDomStorageEnabled(true);


        wv.getSettings().setPluginState(com.tencent.smtt.sdk.WebSettings.PluginState.ON);


        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wv.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

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


        wv.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

//                if (mpid == null || "0".equals(mpid.trim()))
//                    view.loadUrl(url);
//                else{
                resourceUrl(view, url);
//                }
//                else if (url.contains("http://m.iqiyi.com/v_") || url.contains("https://m.youku.com/video/id_")
//                        || url.contains("http://m.le.com/vplay_")
//                        || url.contains("https://m.mgtv.com/b/")
//                        || url.contains("http://m.pptv.com/show/")
//                        || url.contains("http://m.fun.tv/mplay/")
//                        || url.contains("https://m.film.sohu.com/album/")
//                        || url.contains("http://m.v.qq.com/x/cover/")) {
//                    resourceUrl(view, url);
//
//
//                } else
//                    view.loadUrl(url);

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
                // return super.shouldInterceptRequest(view,url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("WebView", "onPageFinished ");

                /*view.loadUrl("javascript:window.local_obj.showSource('<head>'+" +
                        "document.getElementsByTagName('html')[0].innerHTML+'</head>');");*/
                super.onPageFinished(view, url);
                /*view.loadUrl("javascript:window.local_obj.showSource('<head>'+"
                        + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
*/
                view.loadUrl("javascript:window.local_obj.showSource(document.documentElement.outerHTML);void(0)");
/*javascript:window.java_obj.getSource(document.documentElement.outerHTML);void(0)*/

                //     view.loadUrl("javascript: var $el = $('a[id^=__a_z_]'); $el.hide();");
            }


        });

        boolean isHistory = intent.getBooleanExtra(IS_HISTROY, false);

        if (isHistory) {
            resourceUrl(wv, url);
        } else {
            wv.loadUrl(url);
        }


    }


    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            Log.i("html=== ", html);
        }
    }


    /**
     * 开始沉浸式
     */
    private void starSinkBar(int color) {
//设置 paddingTop
        ViewGroup rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        // if (isPadding)
        rootView.setPadding(0, getStatusBarHeight(), 0, 0);
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


    private void resourceUrl(WebView view, String url) {
        if (view == null || url == null)
            return;

        String url1 = HttpConstance.HTTP_PLAYER + "?token=" + RegisterBeanHelper
                .getToken() + "&url=" + url + "&mp_id=" + mpid;

        HashMap<String, String> map = new HashMap<>();
        map.put("token", RegisterBeanHelper.getToken());
        map.put("url", url);
        map.put("mp_id", mpid);

        showDialogTip();
        subscribe = HttpModel.getApiServer()
                .getBackString(url1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    dismissDialog();
                    if (view != null && wv != null && tvTitle != null) {
                        //   s.toString();
                        //view.loadUrl(s);

                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getInt("code") == 0) {
                            JSONObject data = jsonObject
                                    .getJSONObject("data");
                            Log.e("-------", data + "****");
                            if ("h5mp4".equals(data.getString("type") + "") || "h5mp4".equals(data.getString("type") + "")) {
                                Intent intent = new Intent(HomePageWebViewActivity.this, PlayVideoActivity.class);
                                intent.putExtra("videopath", data.getString("url"));
                                intent.putExtra("recodURL", url);
                                intent.putExtra("title", data.getString("title"));
                                intent.putExtra("progress", ((int) data.getDouble("progress")));
                                startActivity(intent);
                            } else {
                                view.loadUrl(data.getString("url"));
                            }


//                            Intent intent = new Intent(HomePageWebViewActivity.this, HomePageWebViewActivity.class);
//                            intent.putExtra(TITLE,data.getString("title"));
//                            intent.putExtra(URL,data.getString("url"));
//                            startActivity(intent);
                        } else if (jsonObject.getInt("code") == 6008) {
                            startActivity(new Intent(HomePageWebViewActivity.this, VIPChargeActivity.class));
                        } else {
                            runOnUiThread(() -> {
                                try {
                                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();

                                }

                            });
                        }

                        // view.loadDataWithBaseURL(null, s, "text/html", "utf-8", null);
                    }


                }, throwable -> {
                    dismissDialog();
                    throwable.printStackTrace();
                });
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
        dismissDialog();
        if (subscribe != null)
            subscribe.dispose();
        if (bind != null)
            bind.unbind();
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


    /*  @Override
      public void onBackPressed() {
          if (wv.canGoBack()) {
              wv.goBack();
              tvTitle.setText(title);
              return;
          }
          super.onBackPressed();
      }

  */
    public boolean hasAd(Context context, String url) {
        //    Log.i("http","url == " + url);
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


    private void showDialogTip() {
        if (dialogTip == null)
            dialogTip = new DialogTip(this);
        if (!dialogTip.isShowing())
            dialogTip.show();

    }

    private void dismissDialog() {
        if (dialogTip == null)
            return;
        if (dialogTip.isShowing())
            dialogTip.dismiss();
    }
}
