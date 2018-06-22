package com.tvcat;


import android.content.res.ColorStateList;
import android.os.Build;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sunian.baselib.baselib.RxActivity;
import com.tvcat.discover.DiscoverFrg;
import com.tvcat.homepage.HomPageFrg;
import com.tvcat.my.views.MyFrg;
import com.tvcat.util.ActivityManager;

public class MainActivity extends RxActivity {
    private BottomNavigationView bottomNavigationView;
    private final String homePage = "hompage";
    private final String discover = "discover";
    private final String my = "my";
    private View statusBarView;
    private long mTime;




    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {
        ActivityManager.fullScreen(this);
        starSinkBar(getResources().getColor(R.color.main_color));


        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_checked}
        };

        int[] colors = new int[]{getResources().getColor(R.color.color_text),
                getResources().getColor(R.color.main_color)
        };

        ColorStateList csl = new ColorStateList(states, colors);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bv);

        bottomNavigationView.setItemIconTintList(csl);
        bottomNavigationView.setItemTextColor(csl);


        bottomNavigationView.setSelectedItemId(R.id.home_page);
        changFrg(homePage);

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
    protected void initListener() {


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home_page:
                    changFrg(homePage);
                    break;
                case R.id.discovery:
                    changFrg(discover);
                    //new DialogTip(this).show();
                    break;
                case R.id.my:
                    changFrg(my);
                    break;


            }


            return true;
        });
    }



    public void changFrg(String cur) {
        if (cur == null)
            return;

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();

        Fragment homPageFrg = supportFragmentManager.findFragmentByTag(homePage);
        Fragment myFrg = supportFragmentManager.findFragmentByTag(my);
        Fragment disFrg = supportFragmentManager.findFragmentByTag(discover);

        switch (cur) {
            case homePage:

                if (homPageFrg == null) {
                    transaction.add(R.id.fl_content, new HomPageFrg(), homePage);
                } else {
                    transaction.show(homPageFrg);
                }

                if (myFrg != null)
                    transaction.hide(myFrg);
                if (disFrg != null)
                    transaction.hide(disFrg);


                break;
            case my:

                if (myFrg == null) {
                    transaction.add(R.id.fl_content, new MyFrg(), my);
                } else {
                    transaction.show(myFrg);
                }

                if (homPageFrg != null)
                    transaction.hide(homPageFrg);
                if (disFrg != null)
                    transaction.hide(disFrg);

                break;
            case discover:
                if (disFrg == null) {
                    transaction.add(R.id.fl_content, new DiscoverFrg(), discover);
                } else {
                    transaction.show(disFrg);
                }

                if (myFrg != null)
                    transaction.hide(myFrg);
                if (homPageFrg != null)
                    transaction.hide(homPageFrg);

                break;
        }


        transaction.commitAllowingStateLoss();


    }


    @Override
    public void onBackPressed() {
        if (bottomNavigationView.getSelectedItemId() == R.id.discovery) {

            Fragment fragment = getSupportFragmentManager().findFragmentByTag(discover);
            if (fragment == null || !(fragment instanceof DiscoverFrg)) {
                back();
            } else {
                DiscoverFrg discoverFrg = (DiscoverFrg) fragment;

                if (!discoverFrg.backPress()) {
                    back();
                }


            }


            return;
        } else {
            back();
        }


    }

    void back() {

        if (System.currentTimeMillis() - mTime < 2000) {
            super.onBackPressed();
        } else {
            mTime = System.currentTimeMillis();
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
        }


    }
}
