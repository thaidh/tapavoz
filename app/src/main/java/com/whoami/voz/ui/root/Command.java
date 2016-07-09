package com.whoami.voz.ui.root;

import android.os.AsyncTask;

import com.whoami.voz.ui.root.Shell.SU;

public class Command extends AsyncTask<String, Void, Void> {
    protected Void doInBackground(String... strArr) {
        if (SU.available()) {
            SU.run(new String[]{"id", strArr[0]});
        }
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
        }
        return null;
    }
}
