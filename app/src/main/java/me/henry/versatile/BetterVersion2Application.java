package me.henry.versatile;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by dell on 2018/10/17.
 */

public class BetterVersion2Application extends MultiDexApplication{
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
    }
}
