package com.tvcat;

import com.sunian.baselib.baselib.IBaseView;
import com.sunian.baselib.beans.UpdateBean;

public interface IUpdateView<T> extends IBaseView<T> {
    void update(UpdateBean updateBean);
}
