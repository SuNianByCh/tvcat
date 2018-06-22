package com.tvcat;

import com.jakewharton.rxbinding2.view.RxView;
import com.sunian.baselib.baselib.IBaseView;
import com.tvcat.beans.ConfigBean;

public interface ILauncherView<T>  extends IBaseView<T> {

    void resultConfig(ConfigBean configBean);
}
