package com.whoami.voz.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.whoami.voz.R;
import com.whoami.voz.main.MainApplication;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 16/12/08
 *     desc  : Utils初始化相关
 * </pre>
 */
public final class Utils {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(@NonNull final Context context) {
        Utils.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }

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

    static long lastShowMess = 0;
    private static Handler mHandler = new Handler(Looper.getMainLooper());
    public static void showMess(final String mess) {
        try {
            if (System.currentTimeMillis() - lastShowMess > 2000) {
                if (mess != null && mess.equals("")) return;


                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            LayoutInflater inflater = LayoutInflater.from(MainApplication.getAppContext());
                            View layout = inflater.inflate(R.layout.toast_layout, null);

                            TextView text = (TextView) layout.findViewById(R.id.text);
                            text.setText(mess);

                            Toast toast = new Toast(MainApplication.getAppContext());
                            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

            lastShowMess = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
