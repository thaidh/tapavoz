package com.nna88.voz.contain;

import android.graphics.Bitmap;

public class containEmo {
    private Bitmap emo;
    private String text;

    public containEmo(Bitmap bitmap, String str) {
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
