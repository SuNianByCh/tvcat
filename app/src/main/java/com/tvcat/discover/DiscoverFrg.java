package com.tvcat.discover;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tvcat.App;
import com.tvcat.ILauncherView;
import com.tvcat.LancherPresenter;
import com.tvcat.R;
import com.tvcat.beans.ConfigBean;
import com.tvcat.util.TipUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DiscoverFrg extends Fragment {
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.wv)
    WebView wv;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    Unbinder unbinder;
    private LancherPresenter lancherPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_dis, null, false);
        unbinder = ButterKnife.bind(this, view);
        return view;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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

        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });


        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress < 99) {
                    pb.setVisibility(View.VISIBLE);
                    pb.setProgress(newProgress);
                } else {
                    pb.setVisibility(View.GONE);
                }
            }
        });

        if (App.getConfigBean() == null) {
            lancherPresenter = new LancherPresenter(new ILauncherView() {
                @Override
                public void noInterNet() {
                    Toast.makeText(getContext(), TipUtil.NO_NET, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void getConfigFailed(String reason) {
                    if (reason == null)
                        return;
                    Toast.makeText(getContext(), reason, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void resultConfig(ConfigBean configBean) {
                    App.setConfigBean(configBean);
                    loadUrl();
                }
            });
            lancherPresenter.getConfig();
        } else {
            loadUrl();
        }

        ivBack.setOnClickListener(v -> {
            if (wv == null)
                return;
            if (wv.canGoBack())
                wv.goBack();
        });


    }


    void loadUrl() {

        wv.loadUrl(App.getConfigBean().getExplore_url());
    }


    public boolean backPress() {
        boolean back = false;

        if (wv.canGoBack()) {
            back = true;
            wv.goBack();
        }


        return back;

    }


    @Override
    public void onPause() {
        super.onPause();
        wv.onPause();
        wv.pauseTimers();
    }

    @Override
    public void onResume() {
        super.onResume();
        wv.resumeTimers();
        wv.onResume();
    }

    @Override
    public void onDestroyView() {

        if (wv != null)
            wv.destroy();


        super.onDestroyView();
        unbinder.unbind();
    }


}
