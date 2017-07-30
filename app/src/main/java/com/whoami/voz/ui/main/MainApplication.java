package com.whoami.voz.ui.main;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.whoami.voz.ui.utils.Utils;

public class MainApplication extends Application {
    private static Context mContext;
    public static void initImageLoader(Context context) {
        Fresco.initialize(context);
    }

    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        refWatcher = LeakCanary.install(this);
        initImageLoader(getApplicationContext());
        Utils.init(mContext);
    }

    public static Context getAppContext() {
        return mContext;
    }

    public static RefWatcher getRefWatcher(Context context) {
        MainApplication application = (MainApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;

}
