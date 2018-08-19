package com.tvcat.my.presenter;

import com.tvcat.ILauncherView;
import com.tvcat.IUpdateView;
import com.sunian.baselib.beans.MyInfos;

public interface IMyView<T> extends IUpdateView<T> ,ILauncherView<T> {
    void resultInfos(MyInfos myInfos);


}
