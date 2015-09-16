package com.nna88.voz.ui;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

public class Util {
    public static int convertDpToPx(Context context, int i) {
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        return Math.round(TypedValue.applyDimension(1, (float) i, displayMetrics));
    }

    public static int convertPxToDp(Context context, int i) {
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        return Math.round(((float) i) / displayMetrics.density);
    }

    public static float pixelsToSp(Context context, Float f) {
        return f.floatValue() / context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static float spTopixels(Context context, Float f) {
        return TypedValue.applyDimension(2, f.floatValue(), context.getResources().getDisplayMetrics());
    }
}
