package com.tvcat.my;

import com.tvcat.IUpdateView;
import com.tvcat.beans.MyInfos;

public interface IMyView<T> extends IUpdateView<T> {
    void resultInfos(MyInfos myInfos);



}
