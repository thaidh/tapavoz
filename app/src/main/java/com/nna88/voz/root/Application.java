package com.nna88.voz.root;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class Application extends android.app.Application {
    private static Handler mApplicationHandler;

    /* renamed from: com.nna88.voz.root.Application.1 */
    static final class AnonymousClass1 implements Runnable {
        final /* synthetic */ Context val$c;
        final /* synthetic */ String val$m;

        AnonymousClass1(Context context, String str) {
            this.val$c = context;
            this.val$m = str;
        }

        public void run() {
            Toast.makeText(this.val$c, this.val$m, 1).show();
        }
    }

    static {
        mApplicationHandler = new Handler();
    }

    public static void toast(Context context, String str) {
        if (context != null) {
            Context applicationContext = !(context instanceof Application) ? context.getApplicationContext() : context;
            if (applicationContext instanceof Application) {
                ((Application) applicationContext).runInApplicationThread(new AnonymousClass1(applicationContext, str));
            }
        }
    }

    public void onCreate() {
        super.onCreate();
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
        }
    }

    public void runInApplicationThread(Runnable runnable) {
        mApplicationHandler.post(runnable);
    }
}
