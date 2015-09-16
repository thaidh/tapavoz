package com.nna88.voz.root;

import android.os.AsyncTask;
import com.nna88.voz.root.Shell.SU;

public class ExitApp extends AsyncTask<String, Void, Void> {
    protected Void doInBackground(String... strArr) {
        if (SU.available()) {
            SU.run(new String[]{"id", "am force-stop com.nna88.voz.main"});
        }
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
        }
        return null;
    }
}
