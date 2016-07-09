package com.whoami.voz.ui.contain;

import android.graphics.Bitmap;

public class Emoticon {
    private Bitmap emo;
    private String text;

    public Emoticon(Bitmap bitmap, String str) {
        this.emo = bitmap;
        this.text = str;
    }

    public Bitmap bitmap() {
        return this.emo;
    }

    public String text() {
        return this.text;
    }
}
