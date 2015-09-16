package com.nna88.voz.root;

import android.util.Log;
import com.nna88.voz.main.BuildConfig;

public class Debug {
    public static void log(String str) {
        if (BuildConfig.DEBUG) {
            StringBuilder append = new StringBuilder().append("[libsuperuser]");
            String str2 = (str.startsWith("[") || str.startsWith(" ")) ? BuildConfig.FLAVOR : " ";
            Log.d("libsuperuser", append.append(str2).append(str).toString());
        }
    }
}
