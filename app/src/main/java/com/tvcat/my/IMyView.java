package com.tvcat.my;

import com.tvcat.ILauncherView;
import com.tvcat.IUpdateView;
import com.tvcat.beans.ConfigBean;
import com.tvcat.beans.MyInfos;

public interface IMyView<T> extends IUpdateView<T> ,ILauncherView<T> {
    void resultInfos(MyInfos myInfos);


}
