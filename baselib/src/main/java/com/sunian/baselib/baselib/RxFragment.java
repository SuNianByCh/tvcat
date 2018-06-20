package com.sunian.baselib.baselib;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.sunian.baselib.util.StringUtil;

/**
 * Created by fujun on 2018/4/10.
 */

public abstract class RxFragment<T extends RxPresenter, JB> extends BaseFragment implements IBaseView<JB> {

    protected T mPresenter;

    protected SmartRefreshLayout mSrl;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initPresenter();
        initRefresh();
        if (mPresenter != null)
            mPresenter.attachView(this);
        super.onViewCreated(view, savedInstanceState);

    }


    /**
     * 初始化刷新控件
     */
    protected void initRefresh() {

    }


    @Override
    protected void onUserVisible(boolean isFirstVisible) {
        super.onUserVisible(isFirstVisible);
    }

    @Override
    protected void onUserInvisible(boolean isFirstInvisible) {
        super.onUserInvisible(isFirstInvisible);
    }

    /**
     * 初始化控制器
     */
    protected abstract void initPresenter();

    @Override
    public void showErrorMsg(String msg, int type) {
        if (!StringUtil.isNull(msg)) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(mContext, "访问服务器失败", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void stateEmpty(String msg, int type) {

    }

    @Override
    public void stateLoading(String msg,int type) {

    }

    @Override
    public void stateMain(int type) {
        if (mSrl != null) {
            mSrl.finishRefresh();
            mSrl.finishLoadMore();
        }

    }

    @Override
    public void stateNoInternet(String msg,int type) {

    }

    @Override
    public void resultDate(JB data,int type) {

    }

    /**
     * 设置标题及背景色
     *
     * @param tvTitle-----
     * @param title-------String，标题
     * @param colorResource--int，标题背景色的id
     */
    protected void setTitle(TextView tvTitle, String title, int colorResource) {
        if (tvTitle == null)
            return;
        tvTitle.setText(title);
        if (colorResource == 0)
            return;
        tvTitle.setBackgroundResource(colorResource);
    }

    @Override
    public void onDestroy() {
        if (mSrl != null) {
            mSrl.finishLoadMore(0);
            mSrl.finishRefresh(0);
        }
        super.onDestroy();
    }
}
