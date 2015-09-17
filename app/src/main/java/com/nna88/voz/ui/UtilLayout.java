package com.nna88.voz.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RelativeLayout;

public class UtilLayout {
    public static void addView(ViewGroup viewGroup, View view, int i, int i2) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(i, i2);
        layoutParams.addRule(12, -1);
        viewGroup.addView(view, layoutParams);
    }

    public static void setMargin(Context context, View view, int i, int i2, int i3, int i4) {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        marginLayoutParams.setMargins(Util.convertDpToPx(context, i), Util.convertDpToPx(context, i2), Util.convertDpToPx(context, i3), Util.convertDpToPx(context, i4));
        view.setLayoutParams(marginLayoutParams);
        view.requestLayout();
        view.invalidate();
    }
}
