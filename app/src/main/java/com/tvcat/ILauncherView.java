package com.tvcat;

import com.tvcat.beans.ConfigBean;

public interface ILauncherView  {
    void noInterNet();
    void getConfigFailed(String reason);
    void resultConfig(ConfigBean configBean);
}
