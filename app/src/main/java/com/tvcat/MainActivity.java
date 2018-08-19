package com.tvcat;


import android.content.res.ColorStateList;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.sunian.baselib.baselib.RxActivity;
import com.sunian.baselib.util.StatusBarUtil;
import com.tvcat.discover.DiscoverFrg;
import com.tvcat.homepage.HomPageFrg;
import com.tvcat.my.views.MyFrg;

public class MainActivity extends RxActivity {
    private BottomNavigationView bottomNavigationView;
    private final String homePage = "hompage";
    private final String discover = "discover";
    private final String my = "my";
    private long mTime;

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.main_color), 0);
        /*StatusBarUtil.setLightMode(this);
        findViewById(R.id.fl_content).setPadding(0,StatusBarUtil.getStatusBarHeight(this),0,0);*/
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

    @Override
    protected void initListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home_page:
                    changFrg(homePage);
                    break;
                case R.id.discovery:
                    changFrg(discover);
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
