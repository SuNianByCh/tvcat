package com.tvcat.homepage;

import com.tvcat.IUpdateView;
import com.tvcat.beans.BannerBean;
import com.tvcat.beans.HomeBean;

import java.util.List;

public interface IHomPageView<T> extends IUpdateView<T> {

    void resultHomeBeanList(List<HomeBean> homeBeanList);
    void resultBannerList(List<BannerBean> bannerBeanList);


}
