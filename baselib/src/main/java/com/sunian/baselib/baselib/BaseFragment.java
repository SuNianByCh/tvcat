package com.sunian.baselib.baselib;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    protected View mView;
    protected Activity mActivity;
    protected Context mContext;
    private Unbinder mUnBinder;

    private boolean isPrepared = false;
    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;


    @Override
    public void onAttach(Context context) {
        mActivity = (Activity) context;
        mContext = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getLayout() != 0)
            mView = inflater.inflate(getLayout(), container, false);
        else
            mView = super.onCreateView(inflater, container, savedInstanceState);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnBinder = ButterKnife.bind(this, mView);
        adjustView(savedInstanceState);
        initListener();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            if (isFirstVisible) {
                isFirstVisible = false;
                onUserVisible(true);
            } else {
                onUserVisible(false);
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false;
                onUserInvisible(true);
            } else {
                onUserInvisible(false);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstVisible) {
            isFirstVisible = false;
            onUserVisible(true);
        } else {
            onUserVisible(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isFirstInvisible) {
            isFirstInvisible = false;
            onUserInvisible(true);
        } else {
            onUserInvisible(false);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                onUserVisible(true);
            } else {
                onUserVisible(false);
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false;
                onUserInvisible(true);
            } else {
                onUserInvisible(false);
            }
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }

    private synchronized void initPrepare() {

            isPrepared = true;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mUnBinder)
            mUnBinder.unbind();
    }

    protected void initListener() {
    }

    /**
     * 得到布局ID
     *
     * @return
     */
    protected abstract int getLayout();

    /**
     * 调整视图
     */
    protected void adjustView(Bundle savedInstanceState) {
    }

    ;

    /**
     * 当视图可见时
     */
    protected void onUserVisible(boolean isFirstVisible) {
    }


    /**
     * 视图不可见
     */
    protected void onUserInvisible(boolean isFirstInvisible) {
    }



}