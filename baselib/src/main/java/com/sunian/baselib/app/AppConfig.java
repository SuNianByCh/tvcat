package com.sunian.baselib.app;

/**
 * Created by fujun on 2018/4/8.
 */

public class AppConfig {

    /**
     * 网络缓存的大小
     */
    public static long HTTP_CACHE_SISZ = 1024 * 1024 * 50;

    /**
     * 图片缓存大小
     */
    public static int IMAGE_CACHE_SIZE = 1024 * 1024 * 30;

    /**
     * 记录崩溃日志  true -------记录
     */
    public static boolean CRASH_LOG = true;

    /**
     * 点击 去抖 500ms
     */
    public static int THROTTLE = 500;

    /**
     * 每一页加载的条数
     */
    public static int PAGE_COUNT = 10;
    /**
     * 群成员过期时间，单位秒
     */
    public static int GROUP_OVER_TIME = 2 * 24 * 60 * 60;
    /**
     * 显示登录失败框的时间间隔
     */
    public static int LOGIN_OTHER = 10 * 1000;

    /**
     * 手机号码的正则表达式
     */
    public static String phone_reg = "[1][34578]\\d{9}";

    //判断是否为数字包括小数
    public static String DIGITAL_REG = "^[0-9]+(.[0-9]+)?$";

    //包含数字、字母并且要同时含有数字和字母，且长度要在6-20位之间
    public static final String PASSWORD_REG = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z_]{6,20}$";


    /**
     * 身份证的正则表达式
     */
    public static final String CARID_REG = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
            "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
}
