package com.tvcat.util;

import com.tvcat.beans.RegisterBean;

public class RegisterBeanHelper {

    private static RegisterBean registerBean = null;

    public static void init(RegisterBean registerBean) {
        if (registerBean == null)
            return;
        RegisterBeanHelper.registerBean = registerBean;
        registerBean = null;
    }


    public static int getId() {
        if (registerBean == null)
            return 0;
        else
            return registerBean.getId();
    }


    public static String getToken() {

        if (registerBean == null)
            return null;
        else
            return registerBean.getToken();
    }


}
