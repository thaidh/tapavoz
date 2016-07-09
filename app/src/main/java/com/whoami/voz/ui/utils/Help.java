package com.whoami.voz.ui.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.webkit.WebView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.whoami.voz.R;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Help {
    private static final String EOCL = "END_OF_CHANGE_LOG";
    private static final String NO_VERSION = "";
    private static final String TAG = "ChangeLog";
    private static final String VERSION_KEY = "PREFS_VERSION_KEY";
    private final Context context;
    private String lastVersion;
    private Listmode listMode;
    private StringBuffer sb;
    private String thisVersion;

    private enum Listmode {
        NONE,
        ORDERED,
        UNORDERED
    }

    public Help(Context context) {
        this(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    public Help(Context context, SharedPreferences sharedPreferences) {
        this.listMode = Listmode.NONE;
        this.sb = null;
        this.context = context;
        this.lastVersion = sharedPreferences.getString(VERSION_KEY, NO_VERSION);
        try {
            this.thisVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            this.thisVersion = NO_VERSION;
            Log.e(TAG, "could not get version name from manifest!");
            e.printStackTrace();
        }
    }

    private void closeList() {
        if (this.listMode == Listmode.ORDERED) {
            this.sb.append("</ol></div>\n");
        } else if (this.listMode == Listmode.UNORDERED) {
            this.sb.append("</ul></div>\n");
        }
        this.listMode = Listmode.NONE;
    }

    private AlertDialog getDialog(boolean z) {
        WebView webView = new WebView(this.context);
        webView.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        webView.loadDataWithBaseURL(null, getLog(z), "text/html", AsyncHttpResponseHandler.DEFAULT_CHARSET, null);
        Builder builder = new Builder(new ContextThemeWrapper(this.context, 16973835));
        builder.setTitle(this.context.getResources().getString(z ? R.string.help_title : R.string.changelog_title)).setView(webView).setCancelable(false).setPositiveButton(this.context.getResources().getString(R.string.changelog_ok_button), new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Help.this.updateVersionInPreferences();
            }
        });
        return builder.create();
    }

    private String getLog(boolean z) {
        this.sb = new StringBuffer();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.context.getResources().openRawResource(R.raw.help)));
            Object obj = null;
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    String trim = readLine.trim();
                    char charAt = trim.length() > 0 ? trim.charAt(0) : '\u0000';
                    if (charAt == '$') {
                        closeList();
                        readLine = trim.substring(1).trim();
                        if (!z) {
                            if (this.lastVersion.equals(readLine)) {
                                obj = 1;
                            } else if (readLine.equals(EOCL)) {
                                obj = null;
                            }
                        }
                    } else if (obj == null) {
                        switch (charAt) {
                            case R.styleable.Theme_actionModeFindDrawable /*33*/:
                                closeList();
                                this.sb.append("<div class='freetext'>" + trim.substring(1).trim() + "</div>\n");
                                break;
                            case R.styleable.Theme_actionModePopupWindowStyle /*35*/:
                                openList(Listmode.ORDERED);
                                this.sb.append("<li>" + trim.substring(1).trim() + "</li>\n");
                                break;
                            case R.styleable.Theme_textAppearanceSmallPopupMenu /*37*/:
                                closeList();
                                this.sb.append("<div class='title'>" + trim.substring(1).trim() + "</div>\n");
                                break;
                            case R.styleable.Theme_homeAsUpIndicator /*42*/:
                                openList(Listmode.UNORDERED);
                                this.sb.append("<li>" + trim.substring(1).trim() + "</li>\n");
                                break;
                            case '_':
                                closeList();
                                this.sb.append("<div class='subtitle'>" + trim.substring(1).trim() + "</div>\n");
                                break;
                            default:
                                closeList();
                                this.sb.append(trim + IOUtils.LINE_SEPARATOR_UNIX);
                                break;
                        }
                    } else {
                        continue;
                    }
                } else {
                    closeList();
                    bufferedReader.close();
                    return this.sb.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void openList(Listmode listmode) {
        if (this.listMode != listmode) {
            closeList();
            if (listmode == Listmode.ORDERED) {
                this.sb.append("<div class='list'><ol>\n");
            } else if (listmode == Listmode.UNORDERED) {
                this.sb.append("<div class='list'><ul>\n");
            }
            this.listMode = listmode;
        }
    }

    private void updateVersionInPreferences() {
        Editor edit = PreferenceManager.getDefaultSharedPreferences(this.context).edit();
        edit.putString(VERSION_KEY, this.thisVersion);
        edit.commit();
    }

    public void dontuseSetLastVersion(String str) {
        this.lastVersion = str;
    }

    public boolean firstRun() {
        return !this.lastVersion.equals(this.thisVersion);
    }

    public boolean firstRunEver() {
        return NO_VERSION.equals(this.lastVersion);
    }

    public String getFullLog() {
        return getLog(true);
    }

    public AlertDialog getFullLogDialog() {
        return getDialog(true);
    }

    public String getLastVersion() {
        return this.lastVersion;
    }

    public String getLog() {
        return getLog(false);
    }

    public AlertDialog getLogDialog() {
        return getDialog(true);
    }

    public String getThisVersion() {
        return this.thisVersion;
    }
}
