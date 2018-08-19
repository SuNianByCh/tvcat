package com.tvcat;

import com.sunian.baselib.baselib.IBaseView;
import com.sunian.baselib.beans.ConfigBean;

public interface ILauncherView<T>  extends IBaseView<T> {

    void resultConfig(ConfigBean configBean);
}
