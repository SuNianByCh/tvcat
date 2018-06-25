package com.tvcat.my.views;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tvcat.R;
import com.tvcat.beans.VipChargeListBean;
import com.tvcat.my.adapters.VipHistoryAdapter;
import com.tvcat.util.ActivityManager;
import com.tvcat.util.HttpConstance;
import com.tvcat.util.HttpModel;
import com.tvcat.util.RegisterBeanHelper;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
@Deprecated
public class VIPHistoryActivity extends AppCompatActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.sml)
    SmartRefreshLayout sml;
    private View statusBarView;
    private Unbinder bind;
    private VipHistoryAdapter adapter;
    private Disposable subscribe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.fullScreen(this);
        starSinkBar(ContextCompat.getColor(this, R.color.main_color));
        setContentView(R.layout.activity_vip_history);
        bind = ButterKnife.bind(this);

        init();


    }

    private void getDate() {
        subscribe = HttpModel.getApiServer().getBackString(HttpConstance.HTTP_VIP_CHARGE_LIS
                + "?token=" + RegisterBeanHelper.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                            sml.finishRefresh();
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("code") == 0) {

                                Type type = new TypeToken<List<VipChargeListBean>>() {
                                }.getType();
                                if (adapter != null)

                                    adapter.addData(new Gson().fromJson(jsonObject.getString("data"), type));
                            } else {
                                Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        }
                        , throwable -> {
                            sml.finishRefresh();
                        });
    }

    private void init() {


        sml.setEnableLoadMore(false);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VipHistoryAdapter();
        rv.setAdapter(adapter);


        tvTitle.setText("VIP充值记录");
        tvRight.setText("增加");
        ivBack.setOnClickListener(v -> finish());
        tvRight.setOnClickListener(v -> startActivity(new Intent(this, VIPChargeActivity.class)));
        sml.setOnRefreshListener(refreshLayout -> {
            getDate();
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        sml.autoRefresh();
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
        if (subscribe != null)
            subscribe.dispose();
        if (bind != null)
            bind.unbind();
        super.onDestroy();
    }
}
