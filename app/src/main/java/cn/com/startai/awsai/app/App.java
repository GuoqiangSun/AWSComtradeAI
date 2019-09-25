package cn.com.startai.awsai.app;

import android.support.multidex.MultiDexApplication;

import cn.com.swain.baselib.log.Tlog;

/**
 * author Guoqiang_Sun
 * date 2019/8/30
 * desc
 */
public class App extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Tlog.v(" hello ");
    }
}
