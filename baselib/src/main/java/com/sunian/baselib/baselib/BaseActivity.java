package com.sunian.baselib.baselib;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.sunian.baselib.app.ActivityManager;
import com.sunian.baselib.app.AppConfig;
import com.sunian.baselib.util.UtilInput;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by fujun on 2018/4/8.
 */

public abstract class BaseActivity extends AppCompatActivity {

    TextView tvTitle;
   // private View statusBarView;
    Unbinder mUnbinder;
    protected AppCompatActivity mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.addActivity(this);
        onViewBeforeCreate();
        mContext = this;
        setContentView(getLayout());
      /*  if (isSinkBar()) {
            ActivityManager.fullScreen(this);
            starSinkBar(getStatusBarColor(), false);
        }*/
        mUnbinder = ButterKnife.bind(this);
        onViewCreated();
        initEventAndData();
        initListener();

    }

    /**
     * 布局文件的id
     *
     * @return-------int
     */
    public abstract int getLayout();

    /**
     * 初始化点击事件
     */
    protected abstract void initListener();

    /**
     * 初始化数据
     */
    protected abstract void initEventAndData();

    /**
     * 加载布局后
     */
    protected void onViewCreated() {
    }


   /* *//**
     * 开始沉浸式
     *//*
    private void starSinkBar(int color, boolean isPadding) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return;

//设置 paddingTop
        ViewGroup rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        // if (isPadding)
        int paddingTop = rootView.getPaddingTop();
        if (paddingTop <= 0)
            rootView.setPadding(0, getStatusBarHeight(), 0, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0 以上直接设置状态栏颜色
            getWindow().setStatusBarColor(color);
        } else  *//*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)*//* {
            //根布局添加占位状态栏
            ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
            if (statusBarView != null && decorView.indexOfChild(statusBarView) != -1) {
                statusBarView.setBackgroundColor(color);
            } else {
                statusBarView = new View(this);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        getStatusBarHeight());
                statusBarView.setBackgroundColor(color);
                decorView.addView(statusBarView, lp);
            }

        }
    }
*/

 /*   public void changeStatusBarColor(int color, boolean isPadding) {
        if (!isSinkBar())
            return;
        starSinkBar(color, isPadding);

    }
*/

  /*  protected int getStatusBarColor() {
        return Color.TRANSPARENT;
    }*/

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

    /**
     * 绑定视图前
     */
    protected void onViewBeforeCreate() {

    }

    /**
     * 是否使用沉浸式
     *
     * @return
     */
    protected boolean isSinkBar() {
        return true;
    }

    @Override
    protected void onDestroy() {
        ActivityManager.removeActivity(this);
        if (mUnbinder != null)
            mUnbinder.unbind();
        UtilInput.fixInputMethodManagerLeak(this);
        super.onDestroy();
    }

/*
    public void hideSinkBar(boolean isPaddingToZero) {
        ViewGroup rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        //  rootView.setPadding(0, getStatusBarHeight(), 0, 0);
        if (isPaddingToZero)
            rootView.setPadding(0, 0, 0, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0 以上直接设置状态栏颜色
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else  *//*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)*//* {
            //根布局添加占位状态栏
            ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
            if (statusBarView != null) {
                statusBarView.setBackgroundColor(Color.TRANSPARENT);
            }
        }

    }*/

    protected void backView(View view) {
        if (view == null)
            return;
        RxView.clicks(view).throttleFirst(AppConfig.THROTTLE, TimeUnit.MILLISECONDS).subscribe(o -> finish());
    }
}
