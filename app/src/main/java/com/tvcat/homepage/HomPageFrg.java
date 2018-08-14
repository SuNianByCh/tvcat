package com.tvcat.homepage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sunian.baselib.baselib.RxFragment;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tvcat.App;
import com.tvcat.DialogUpdate;
import com.tvcat.R;
import com.tvcat.beans.BannerBean;
import com.tvcat.beans.HomeBean;
import com.tvcat.beans.UpdateBean;
import com.tvcat.my.views.MyWebViewActiviy;
import com.tvcat.util.DeviceUtil;
import com.tvcat.util.DownLoadService;
import com.tvcat.util.MD5Util;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import okhttp3.FormBody;

public class HomPageFrg extends RxFragment<HomePresenter,Object> implements IHomPageView<Object> {
    @BindView(R.id.rv)
    protected RecyclerView mRv;
    private String UUID;
    @BindView(R.id.banner)
    protected Banner mBanner;
    private HomeAdapter adapter;

    @Override
    protected void initPresenter() {
        mPresenter = new HomePresenter();
    }

    @Override
    protected int getLayout() {
        return R.layout.frg_homepage;
    }

    @Override
    protected void initRefresh() {
        super.initRefresh();
        mSrl =  mView.findViewById(R.id.sml);
    }
    @Override
    protected void initListener() {
        super.initListener();
        mSrl.setOnRefreshListener(refreshLayout -> {
            register();
        });
        initUIID();
    }

    @Override
    protected void adjustView(Bundle savedInstanceState) {
        super.adjustView(savedInstanceState);
        mSrl.setEnableLoadMore(false);
        mSrl.setEnableOverScrollDrag(true);
        mRv.setLayoutManager(new GridLayoutManager(getContext(), 4));
        adapter = new HomeAdapter(getContext(), null);
        mRv.setAdapter(adapter);
        mBanner.setDelayTime(3000);
        adapter.setClickBack((postion, homeBean) -> startWebAcitvity(homeBean.getName(), homeBean.getUrl(), homeBean.getId() + ""));
    }

    @Override
    public void resultHomeBeanList(List<HomeBean> homeBeanList) {
        if (homeBeanList == null || homeBeanList.isEmpty())
            return;
        adapter.clearAndRefresh(homeBeanList);
    }

    @Override
    public void resultBannerList(List<BannerBean> bannerBeanList) {
        if (bannerBeanList == null || bannerBeanList.isEmpty())
            return;
        mBanner.stopAutoPlay();
        mBanner.setImages(bannerBeanList);
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                BannerBean bannerBean = (BannerBean) path;
                Glide.with(getContext()).load(bannerBean.getImage()).into(imageView);

                if (bannerBean.getLink() == null || "".equals(bannerBean.getLink().trim()))
                    return;
                imageView.setOnClickListener(v -> {

                    if (bannerBean.getLink() == null || bannerBean.getLink().trim().equals("")) {
                        return;
                    }
                    Intent intent = new Intent(getContext(), MyWebViewActiviy.class);
                    intent.putExtra("type",MyWebViewActiviy.type_ad);
                    intent.putExtra("title", "广告");
                    intent.putExtra("url", bannerBean.getLink());
                   startActivity(intent);
                });
            }
        });
        mBanner.start();
    }

    @Override
    protected void onUserInvisible(boolean isFirstInvisible) {
        super.onUserInvisible(isFirstInvisible);
        mBanner.stopAutoPlay();
    }

    @Override
    protected void onUserVisible(boolean isFirstVisible) {
        super.onUserVisible(isFirstVisible);
        if(!isFirstVisible)
            mBanner.startAutoPlay();
    }

    @Override
    public void onResume() {
        super.onResume();
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
    }
    void register() {
        if (UUID == null)
            UUID = DeviceUtil.getUUID(getContext());
        String apki = MD5Util.getMD5("c6c8fd23676b4f039330e9107285ab59");
        long l = System.currentTimeMillis();
        FormBody build = new FormBody.Builder().add("uuid", UUID)
                .add("model", Build.MODEL)
                .add("os", "Android")
                .add("i", l + "")
                .add("ak", apki + l)
                .add("osv", Build.VERSION.SDK_INT + "").build();

        HashMap<String, String> map = new HashMap<>();
        map.put("uuid", UUID);
        map.put("model", Build.MODEL);
        map.put("os", "Android");
        map.put("i", l + "");
        map.put("ak", apki + l);
        map.put("osv", Build.VERSION.SDK_INT + "");
        mPresenter.httpRegister(map);
    }

    private void initUIID() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !DeviceUtil.hasUUID(App.instance)) {
            Disposable subscribe = new RxPermissions(getActivity())
                    .request(Manifest.permission.READ_PHONE_STATE)
                    .subscribe(aBoolean -> {
                        if (aBoolean) {
                            UUID = DeviceUtil.getDeviceId(getContext());
                        } else {
                            UUID = DeviceUtil.getUUID(getContext());
                        }
                        mSrl.autoRefresh();
                    });

        } else {
            UUID = DeviceUtil.getDeviceId(getContext());
            mSrl.autoRefresh();
        }
    }
    private void startWebAcitvity(String title, String url, String mpID) {
        if (title == null || "".equals(title.trim()))
            return;
        if (url == null || "".equals(url.trim()))
            return;
        Intent intent = new Intent(getContext(), HomePageWebViewActivity.class);
        intent.putExtra(HomePageWebViewActivity.TITLE, title);
        intent.putExtra(HomePageWebViewActivity.URL, url);
        intent.putExtra(HomePageWebViewActivity.MPID, mpID);
        startActivity(intent);
    }

    @Override
    public void update(UpdateBean updateBean) {
        if (updateBean == null)
            return;
        if (updateBean.getChangelog() == null)
            return;
        String version = updateBean.getVersion();
        if (version == null)
            return;
        version = version.replace(".", "").trim();
        if (!version.matches("[0-9]+"))
            return;
        int versionInt = Integer.parseInt(version);
        if (versionInt > DeviceUtil.getVersionCode()) {
            new DialogUpdate(getContext(), updateBean)
                    .setiClickBack((postion, s) -> {
                        if (s == null || "".equals(s.trim())) {
                            Toast.makeText(getContext(), "下载地址有错", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent service = new Intent(getContext(), DownLoadService.class);
                        service.putExtra(DownLoadService.TYPE_DOWN, s);
                        getActivity().startService(service);
                    }).show();
        }

    }
}
