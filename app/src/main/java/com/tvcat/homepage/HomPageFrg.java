package com.tvcat.homepage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tvcat.App;
import com.tvcat.DialogUpdate;
import com.tvcat.R;
import com.tvcat.beans.BannerBean;
import com.tvcat.beans.HomeBean;
import com.tvcat.beans.UpdateBean;
import com.tvcat.my.MyWebViewActiviy;
import com.tvcat.util.DeviceUtil;
import com.tvcat.util.DownLoadService;
import com.tvcat.util.MD5Util;
import com.tvcat.util.TipUtil;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.HashMap;
import java.util.List;

import io.reactivex.disposables.Disposable;
import okhttp3.FormBody;

public class HomPageFrg extends Fragment implements HomPageView {

    private HomePresenter presenter;
    private SmartRefreshLayout mSfl;
    private RecyclerView mRv;
    private String UUID;
    private HomePresenter homePresenter;
    private Banner mBanner;
    private HomeAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.frg_homepage, null, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        homePresenter = new HomePresenter(this);
        initUIID();


    }

    private void initView(View view) {
        mSfl = view.findViewById(R.id.sml);
        mSfl.setEnableLoadMore(false);
        mRv = view.findViewById(R.id.rv);
        mSfl.setEnableOverScrollDrag(true);
        mRv.setLayoutManager(new GridLayoutManager(getContext(), 4));
        adapter = new HomeAdapter(getContext(), null);
        mRv.setAdapter(adapter);


        mBanner = view.findViewById(R.id.banner);
        mSfl.setOnRefreshListener(refreshLayout -> {
            register();
        });


        int widthPixels = getResources().getDisplayMetrics().widthPixels;

        float rate = widthPixels*1.0f/504;
        float height = 1080/rate;

        ViewGroup.LayoutParams layoutParams = mBanner.getLayoutParams();
        layoutParams.height= (int) height;
        mBanner.setLayoutParams(layoutParams);
        mBanner.setDelayTime(3000);


        adapter.setClickBack((postion, homeBean) -> startWebAcitvity(homeBean.getName(), homeBean.getUrl(), homeBean.getId() + ""));

    }


    @Override
    public void failReg(String result) {
        Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
        mSfl.finishRefresh(false);

    }

    @Override
    public void failGetBanner(String reason) {
        Toast.makeText(getContext(), reason, Toast.LENGTH_SHORT).show();
        mSfl.finishRefresh(false);


    }

    @Override
    public void noInternet() {
        Toast.makeText(getContext(), TipUtil.NO_NET, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void resultHomeBeanList(List<HomeBean> homeBeanList) {

        mSfl.finishRefresh(true);

        if (homeBeanList == null || homeBeanList.isEmpty())
            return;

        adapter.clearAndRefresh(homeBeanList);

    }

    @Override
    public void resultBannerList(List<BannerBean> bannerBeanList) {

        bannerBeanList.toString();
        mSfl.finishRefresh(true);
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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            mBanner.stopAutoPlay();
        }else {
            mBanner.startAutoPlay();
        }
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


        homePresenter.httpRegister(map);


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


                        mSfl.autoRefresh();
                    });

        } else {
            UUID = DeviceUtil.getDeviceId(getContext());
            mSfl.autoRefresh();
        }


    }

    @Override
    public void onDestroy() {
        if (homePresenter != null)
            homePresenter.unSubscribe();
        super.onDestroy();
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
