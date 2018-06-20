package com.sunian.baselib.model.preferences;

/**
 * Created by fujun on 2018/4/8.
 */

public interface IPreferencesHelper {

    /**
     * 保存账号密码
     *
     * @param userName
     * @param password
     * @param isRemember 是否记住密码
     */
    void saveUserAndPsd(String userName, String password, boolean isRemember);

    /**
     * 取得密码
     *
     * @return
     */
    String getPsd();

    /**
     * 取得用户名
     *
     * @return
     */
    String getUserName();

    /**
     * 是否已登陆
     *
     * @return-------------true 已登陆
     */
    boolean hasLogIn();

    /**
     * 保存登陆状态
     *@param isLogin  ----------boolean true已登陆
     * @param
     */
    void setLogIn(boolean isLogin);


    /**
     * 是否已开启手势密
     *
     * @return-------------boolean，true开启
     */
    boolean hasOpenGesturePs();

    /**
     * 是否已开启手势密码
     *
     * @param isOpen ----------boolean,true 开启
     * @return
     */
    void setOpenGesturePs(boolean isOpen);

}
