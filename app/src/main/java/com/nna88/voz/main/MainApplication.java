package com.nna88.voz.main;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap.Config;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class MainApplication extends Application {
    private static Context mContext;
    public static void initImageLoader(Context context) {
        ImageLoader.getInstance().init(
                new Builder(context).writeDebugLogs()
                .threadPriority(7)
                        .denyCacheImageMultipleSizesInMemory()
                .defaultDisplayImageOptions(
                        new DisplayImageOptions.Builder().cacheOnDisk(true)
                        .cacheInMemory(true).imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                        .bitmapConfig(Config.ARGB_8888)
                        .displayer(new FadeInBitmapDisplayer(300)).build()
                )
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                        .tasksProcessingOrder(QueueProcessingType.LIFO).build()
        );

//        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context)
//                .setbitmap
//                .build();
//        Fresco.initialize(context, config);
        Fresco.initialize(context);
    }

    @TargetApi(9)
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        refWatcher = LeakCanary.install(this);
        initImageLoader(getApplicationContext());
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
