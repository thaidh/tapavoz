package com.whoami.voz.ui.utils;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.whoami.voz.ui.contain.VozPost;
import com.whoami.voz.ui.main.MainApplication;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by thaidh on 10/24/15.
 */
public class EmoLoader {
    private static final String TAG = EmoLoader.class.getSimpleName();
//    Map<String, Bitmap> emoCaChe = new HASH
    private static EmoLoader emoLoader;
    LruCache<String, BitmapDrawable> mEmoCache;

    public static EmoLoader getInstance() {
        if (emoLoader == null) {
            synchronized (EmoLoader.class) {
                if (emoLoader == null) {
                    emoLoader = new EmoLoader();
                }
            }
        }
        return emoLoader;
    }

    private EmoLoader() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mEmoCache = new LruCache<String, BitmapDrawable>(cacheSize) {
            @Override
            protected int sizeOf(String key, BitmapDrawable bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
//                return bitmap.get / 1024;
                return 1;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, BitmapDrawable bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mEmoCache.put(key, bitmap);
        }
    }

    public BitmapDrawable getBitmapFromMemCache(String key) {
        return mEmoCache.get(key);
    }

    public void initEmoBitmapCache(String strName) {
        BitmapDrawable bitmapDrawable = mEmoCache.get(strName) ;
        if (bitmapDrawable == null) {
            Log.i(TAG, "Init cache emo: " + strName);
            AssetManager assetManager = MainApplication.getAppContext().getAssets();
            InputStream istr = null;
            try {
                istr = assetManager.open(getlinkBitmapAssert(strName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(istr);
            bitmapDrawable = new BitmapDrawable(MainApplication.getAppContext().getResources(), bitmap);
            bitmapDrawable.setBounds(0, 0, (int) (((float) VozPost.DP32) * VozPost.TEXT_SIZE), (int) (((float) VozPost.DP32) * VozPost.TEXT_SIZE));
            mEmoCache.put(strName, bitmapDrawable);
        }
    }

    private String getlinkBitmapAssert(String str) {
        return str.substring(0, str.length() - 3) + "gif";
    }
}
