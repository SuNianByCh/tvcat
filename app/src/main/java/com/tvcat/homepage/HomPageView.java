package com.tvcat.homepage;

import com.tvcat.IUpdate;
import com.tvcat.beans.BannerBean;
import com.tvcat.beans.HomeBean;
import com.tvcat.beans.RegisterBean;

import java.util.HashMap;
import java.util.List;

public interface HomPageView extends IUpdate {


    void failReg(String result);

    void failGetBanner(String reason);
    void noInternet();

    void resultHomeBeanList(List<HomeBean> homeBeanList);
    void resultBannerList(List<BannerBean> bannerBeanList);


}
