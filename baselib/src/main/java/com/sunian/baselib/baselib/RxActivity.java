package com.sunian.baselib.baselib;

import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.sunian.baselib.util.ToastUtil;

/**
 * Created by fujun on 2018/4/8.
 */

public abstract class RxActivity<T extends RxPresenter, JB> extends BaseActivity implements IBaseView<JB> {


    protected T mPresenter;

    protected SmartRefreshLayout mSrl;

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        initRefresh();
        initPresenter();
        if (mPresenter != null)
            mPresenter.attachView(this);


    }


    /**
     * 初始化刷新控件
     */
    protected void initRefresh() {

    }
    /**
     * 初始化控制器
     */
    protected void initPresenter() {

    }


    @Override
    protected void onDestroy() {
        if (mSrl != null) {
            mSrl.finishRefresh(0);
            mSrl.finishLoadMore(0);
        }
        if (mPresenter != null)
            mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showErrorMsg(String msg, int type) {
        if (msg != null)
            ToastUtil.show(msg, mContext);

    }

    @Override
    public void stateEmpty(String msg, int type) {

    }

    @Override
    public void stateLoading(String msg, int type) {

    }

    @Override
    public void stateMain(int type) {
        if (mSrl != null) {
            mSrl.finishRefresh();
            mSrl.finishLoadMore();
        }
    }

    @Override
    public void stateNoInternet(String msg, int type) {

    }

    @Override
    public void resultDate(JB data, int type) {

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
}
