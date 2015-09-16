package com.nna88.voz.contain;

import android.graphics.Bitmap;

public class Thread {
    private Bitmap bitmap;
    public boolean isBookmark;
    private boolean isStar;
    private boolean isSticky;
    public String mIdThread;
    private String mLastPost;
    public String mPrefixLink;
    private String mReply;
    private String mThread;
    private String mUrlLasrPost;
    private String mUrlLastLast;
    private String mUrlThread;
    private String mView;

    public Thread(String str, String str2, String str3, String str4, Bitmap bitmap, String str5, String str6) {
        this.mUrlLastLast = null;
        this.isBookmark = false;
        this.mIdThread = null;
        this.mPrefixLink = null;
        this.mReply = str3;
        this.mLastPost = str2;
        this.mView = str4;
        this.mThread = str;
        this.bitmap = bitmap;
        this.mUrlThread = str5;
        this.mUrlLasrPost = str6;
        this.isSticky = false;
        this.isStar = false;
    }

    public Thread(String str, String str2, String str3, String str4, Bitmap bitmap, String str5, String str6, Boolean bool) {
        this.mUrlLastLast = null;
        this.isBookmark = false;
        this.mIdThread = null;
        this.mPrefixLink = null;
        this.mReply = str3;
        this.mLastPost = str2;
        this.mView = str4;
        this.mThread = str;
        this.bitmap = bitmap;
        this.mUrlThread = str5;
        this.mUrlLasrPost = str6;
        this.isSticky = false;
        this.isStar = bool.booleanValue();
    }

    public Bitmap Image() {
        return this.bitmap;
    }

    public String LastPost() {
        return this.mLastPost;
    }

    public String Reply() {
        return this.mReply;
    }

    public String Thread() {
        return this.mThread;
    }

    public String UrlLastPosst() {
        return this.mUrlLasrPost;
    }

    public String UrlThread() {
        return this.mUrlThread;
    }

    public String View() {
        return this.mView;
    }

    public String getUrlLastLast() {
        return this.mUrlLastLast;
    }

    public boolean isStart() {
        return this.isStar;
    }

    public boolean isSticky() {
        return this.isSticky;
    }

    public int isUrl() {
        if (this.mUrlThread != null) {
            if (this.mUrlThread.contains("forumdisplay.php?")) {
                return 0;
            }
            if (this.mUrlThread.contains("showthread.php?")) {
                return 1;
            }
        }
        return 2;
    }

    public void setStart(boolean z) {
        this.isStar = z;
    }

    public void setSticky(boolean z) {
        this.isSticky = z;
    }

    public void setUrlLastPost(String str) {
        this.mUrlLastLast = str;
    }
}
