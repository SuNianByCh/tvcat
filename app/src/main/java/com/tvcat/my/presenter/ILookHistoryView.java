package com.tvcat.my.presenter;

import com.sunian.baselib.baselib.IBaseView;
import com.tvcat.beans.LookHistParseBean;
import com.tvcat.beans.LookHistoryBean;

import java.util.List;

/**
 * Created by sunian on 2018/6/22.
 */

public interface ILookHistoryView<T> extends IBaseView<T> {
    void resultLookHistory(List<LookHistoryBean> historyBeanList);
    void parseLook(LookHistParseBean lookHistParseBean);
}
