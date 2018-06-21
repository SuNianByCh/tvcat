package com.tvcat.util;

public class HttpConstance {
  //  public final static String HOST_ADDRESS = "http://tvcat.small-best.com/api/";
    public final static String HOST_ADDRESS = "http://m.tvcat.co/api/";
    public final static String HTTP_REGISTER = HOST_ADDRESS + "v1/account/create";
    public final static String HTTP_BANNER = HOST_ADDRESS + "v1/banners";
    public final static String HTTP_MEDIA = HOST_ADDRESS + "v1/media/providers?";
    public static final String HTTP_CONFIG = HOST_ADDRESS + "v1/app/config";
    public static final String HTTP_ABOUT_ME = HOST_ADDRESS + "v1/user/me";
    public static final String HTTP_PLAYER = HOST_ADDRESS + "v1/media/player";
    public static final String HTTP_VIP_ACTIVE = HOST_ADDRESS + "v1/user/vip/active";//VIP激活
    public static final String HTTP_VIP_CHARGE_LIS = HOST_ADDRESS + "v1/user/vip_charge_list";//充值记录

    public static final String HTTP_MEDIA_HISTORIES = HOST_ADDRESS + "v1/media/histories";//获取所有浏览历史
    public static final String HTTP_UPDATE = HOST_ADDRESS + "v1/app/check_version";//版本更新

    public static final String HTTP_SAVE_PLAY_TIME =  HOST_ADDRESS + "v1/media/play/progress";//保存播放进度

}