package com.nna88.voz.contain;

public class Forum {
    public String html;
    private String mForum;
    private String mUrlForum;
    private String mView;

    public Forum(String str, String str2, String str3) {
        this.mForum = str;
        this.mUrlForum = str2;
        this.mView = str3;
    }

    public String Forum() {
        return this.mForum;
    }

    public String UrlForum() {
        return this.mUrlForum;
    }

    public String Viewing() {
        return this.mView;
    }

    public String getForum() {
        return this.html;
    }
}
