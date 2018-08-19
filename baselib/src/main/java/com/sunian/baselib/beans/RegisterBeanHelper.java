package com.sunian.baselib.beans;

public class RegisterBeanHelper {

    private static RegisterBean registerBean = null;

    private static  String seesion_id;
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

    public static String getSeesion_id() {
        return seesion_id;
    }

    public static void setSeesion_id(String seesion_id) {
        RegisterBeanHelper.seesion_id = seesion_id;
    }
}
