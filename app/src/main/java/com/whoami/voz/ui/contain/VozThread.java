package com.whoami.voz.ui.contain;

import android.graphics.Bitmap;

public class VozThread {
    private Bitmap bitmap;
    public boolean isBookmark;
    private boolean isStar;
    private boolean isSticky;
    public String mIdThread;
    private String mLastPost;
    public String mPrefixLink;
    private String mReply;
    private String mTitle;
    private String mUrlLasrPost;
    private String mUrlLastLast;
    private String mUrlThread;
    private String mView;

    public VozThread(String title, String lastPost, String reply, String view, Bitmap bitmap, String url, String lastPostUrl) {
        this.mUrlLastLast = null;
        this.isBookmark = false;
        this.mIdThread = null;
        this.mPrefixLink = null;
        this.mReply = reply;
        this.mLastPost = lastPost;
        this.mView = view;
        this.mTitle = title;
        this.bitmap = bitmap;
        this.mUrlThread = url;
        this.mUrlLasrPost = lastPostUrl;
        this.isSticky = false;
        this.isStar = false;
    }

    public VozThread(String mTitle, String mUrlThread, String mIdThread) {
        this.mIdThread = mIdThread;
        this.mTitle = mTitle;
        this.mUrlThread = mUrlThread;
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
        return this.mTitle;
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
