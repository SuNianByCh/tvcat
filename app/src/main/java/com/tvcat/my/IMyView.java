package com.tvcat.my;

import com.tvcat.IUpdate;
import com.tvcat.beans.MyInfos;

public interface IMyView extends IUpdate{
    void resultInfos(MyInfos myInfos);
    void getMyInfosFailed(String url);
    void noInterNet();

}
