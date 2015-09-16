package com.nna88.voz.util;

import android.graphics.Bitmap.Config;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ImageLoad {
    public ImageLoader imageLoader;
    public DisplayImageOptions options;

    public ImageLoad() {
        this.imageLoader = ImageLoader.getInstance();
        this.options = new Builder().cacheInMemory(true).cacheOnDisc(true).imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).bitmapConfig(Config.ARGB_8888).delayBeforeLoading(0).build();
    }
}
