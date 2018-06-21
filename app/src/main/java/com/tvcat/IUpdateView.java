package com.tvcat;

import com.sunian.baselib.baselib.IBaseView;
import com.tvcat.beans.UpdateBean;

public interface IUpdateView<T> extends IBaseView<T> {
    void update(UpdateBean updateBean);
}
