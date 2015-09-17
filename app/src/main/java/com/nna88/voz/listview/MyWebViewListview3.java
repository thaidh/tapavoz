package com.nna88.voz.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class MyWebViewListview3 extends WebView {
    public MyWebViewListview3(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void applyAfterMoveFix() {
        onScrollChanged(getScrollX(), getScrollY(), getScrollX(), getScrollY());
    }
}
