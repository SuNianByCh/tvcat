package com.tvcat.homepage;

import com.tvcat.IUpdateView;
import com.sunian.baselib.beans.BannerBean;
import com.sunian.baselib.beans.HomeBean;

import java.util.List;

public interface IHomPageView<T> extends IUpdateView<T> {

    void resultHomeBeanList(List<HomeBean> homeBeanList);
    void resultBannerList(List<BannerBean> bannerBeanList);
    void registerSuccess();


}
