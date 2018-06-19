package com.tvcat.my;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.tvcat.R;
import com.tvcat.beans.LookHistoryBean;
import com.tvcat.util.ActivityManager;
import com.tvcat.util.DialogTip;
import com.tvcat.util.HttpConstance;
import com.tvcat.util.HttpModel;
import com.tvcat.util.RegisterBeanHelper;
import com.tvcat.videoplay.PlayVideoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LookHistoryAcitvity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    private View statusBarView;
    private Unbinder bind;
    private HistoryAdapter adapter;
    private int page = 0;
    private Disposable subscribe;
    private DialogTip dialogTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.fullScreen(this);
        starSinkBar(getResources().getColor(R.color.main_color));
        setContentView(R.layout.activity_look_history);
        bind = ButterKnife.bind(this);

        tvTitle.setText("观看历史");


        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter(this);
        rv.setAdapter(adapter);
        initClick();
        srl.autoRefresh();

    }


    private void initClick() {

        ivBack.setOnClickListener(v -> finish());
        srl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

                loadDate(false);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

                loadDate(true);
            }
        });

        adapter.setiClickBack((position, lookHistoryBean) -> {
            if (lookHistoryBean == null || lookHistoryBean.getProvider() == null)
                return;
       /*     Intent intent = new Intent(this, HomePageWebViewActivity.class);
            intent.putExtra(HomePageWebViewActivity.IS_HISTROY,true);
            intent.putExtra(HomePageWebViewActivity.TITLE,lookHistoryBean.getTitle());
            intent.putExtra(HomePageWebViewActivity.MPID,lookHistoryBean.getId());
            intent.putExtra(HomePageWebViewActivity.URL,lookHistoryBean.getSource_url());


            startActivity(intent);*/
            parsePlayUrl(lookHistoryBean);

        });

    }


    private void parsePlayUrl(LookHistoryBean lookHistoryBean) {
        if (lookHistoryBean == null || lookHistoryBean.getProvider() == null)
            return;
        showDialogTip();
        String url1 = HttpConstance.HTTP_PLAYER + "?token=" + RegisterBeanHelper
                .getToken() + "&url=" + lookHistoryBean.getSource_url() + "&mp_id=" + lookHistoryBean.getProvider().getId();

        subscribe = HttpModel.getApiServer()
                .getBackString(url1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    dismissDialog();
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getInt("code") == 0) {
                        JSONObject data = jsonObject
                                .getJSONObject("data");
                        Log.e("-------", data + "****");
                        if ("h5mp4".equals(data.getString("type") + "")) {
                            Intent intent = new Intent(LookHistoryAcitvity.this, PlayVideoActivity.class);
                            intent.putExtra("videopath", data.getString("url"));
                            intent.putExtra("recodURL", lookHistoryBean.getSource_url());
                            intent.putExtra("title", lookHistoryBean.getTitle());
                            intent.putExtra("progress", ((int) data.getDouble("progress")));
                            startActivity(intent);
                        }

                    } else if (jsonObject.getInt("code") == 6008) {
                        startActivity(new Intent(LookHistoryAcitvity.this, VIPChargeActivity.class));
                    } else {
                        runOnUiThread(() -> {
                            try {
                                Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        });
                    }
                }, throwable -> {
                    dismissDialog();
                    throwable.printStackTrace();
                });
    }


    private void loadDate(boolean isRefresh) {
        if (isRefresh) {
            page = 1;
        }
        if (subscribe != null)
            subscribe.dispose();


        String url = HttpConstance.HTTP_MEDIA_HISTORIES + "?token=" + RegisterBeanHelper.getToken() + "&page=" + page;
        subscribe = HttpModel.getApiServer().getBackString(url)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {

                            if (srl == null)
                                return;
                            srl.finishRefresh();
                            srl.finishLoadMore();
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("code") == 0) {
                                page++;

                                Type type = new TypeToken<List<LookHistoryBean>>() {
                                }.getType();
                                if (isRefresh) {
                                    adapter.clearRefresh(new Gson().fromJson(jsonObject.getString("data"), type));
                                } else {
                                    adapter.addMore(new Gson().fromJson(jsonObject.getString("data"), type));
                                }


                            } else {
                                Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }


                        }, throwable -> {
                            if (srl == null)
                                return;
                            srl.finishRefresh();
                            srl.finishLoadMore();
                            throwable.printStackTrace();
                        }
                );


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

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        //super.onSaveInstanceState(outState, outPersistentState);
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
        if (bind != null)
            bind.unbind();
        super.onDestroy();
    }
}
