package com.sunian.baselib.app;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by fujun on 2018/4/9.
 */

public class BaseApp extends TinkerApplication {
    protected BaseApp(int tinkerFlags) {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.sunian.SampleApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }

    public BaseApp(){
        super(ShareConstants.TINKER_ENABLE_ALL, "com.sunian.SampleApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }

  /*  @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        ARouter.init(this);
    }
*/
   /* @Override
    public void onCreate() {
        super.onCreate();

        DataManger.init(this);
        // 置入一个不设防的VmPolicy（不设置的话 7.0以上一调用拍照功能就崩溃了）
        // 还有一种方式：manifest中加入provider然后修改intent代码
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        // Normal app init code...
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
        }
        Stetho.initializeWithDefaults(this);
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }

    }*/
}
