package com.sunian.baselib.app;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by fujun on 2018/4/9.
 */

public class BaseApp extends TinkerApplication {
    protected BaseApp(int tinkerFlags) {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.tvcat.App",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }

    public BaseApp(){
        super(ShareConstants.TINKER_ENABLE_ALL, "com.tvcat.App",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }


}
