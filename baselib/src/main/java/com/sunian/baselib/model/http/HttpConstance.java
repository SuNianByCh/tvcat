package com.sunian.baselib.model.http;

public class HttpConstance {
    //  public final static String HOST_ADDRESS = "http://tvcat.small-best.com/api/";
    public final static String HOST_ADDRESS = "http://m.tvcat.co/api/";
    public final static String HTTP_REGISTER = HOST_ADDRESS + "v1/account/create";
    public final static String HTTP_BANNER = HOST_ADDRESS + "v1/banners";
    public final static String HTTP_MEDIA = HOST_ADDRESS + "v1/media/providers";
    public static final String HTTP_CONFIG = HOST_ADDRESS + "v1/app/config";
    public static final String HTTP_ABOUT_ME = HOST_ADDRESS + "v1/user/me";
    public static final String HTTP_PLAYER = HOST_ADDRESS + "v1/media/player";
    public static final String HTTP_VIP_ACTIVE = HOST_ADDRESS + "v1/user/vip/active";//VIP激活
    public static final String HTTP_VIP_CHARGE_LIS = HOST_ADDRESS + "v1/user/vip_charge_list";//充值记录

    public static final String HTTP_MEDIA_HISTORIES = HOST_ADDRESS + "v1/media/histories";//获取所有浏览历史
    public static final String HTTP_UPDATE = HOST_ADDRESS + "v1/app/check_version";//版本更新

    public static final String HTTP_SAVE_PLAY_TIME = HOST_ADDRESS + "v1/media/play/progress";//保存播放进度
    public static final String HTTP_CHAGE_VIP = "http://m.tvcat.co/cards?uid=%s";//VIP充值

    /**
     * token
     (required)
     用户TOKEN
     formData	string
     type
     值为1或2或3；1表示APP Launch, 2表示 APP Resume
     formData	integer
     loc
     用户当前位置，值格式为：lng,lat
     formData	string
     network
     用户当前的网络类型，例如：wifi, 3g, 4g
     formData	string
     version
     当前客户端的版本号
     formData	string
     uuid
     设备UUID
     formData	string
     os
     设备系统
     formData	string
     osv
     设备系统版本号
     formData	string
     model
     设备型号，例如：iPhone 5s
     formData	string
     screen
     设备分辨率
     formData	string
     uname
     设备用户名字
     formData	string
     lang_code
     国家语言码，例如：zh_CN
     formData	string
     */
    public static final String HTTP_START_SEESIN = HOST_ADDRESS + "v1/user/session/begin";//开始会话

    public static final String HTTP_END_SEESNI = HOST_ADDRESS+"v1/user/session/end";//结束会话

}