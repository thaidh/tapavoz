package com.nna88.voz.ui;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.internal.widget.RtlSpacingHelper;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.GridView;

public class ExpandableHeightGridView extends GridView {
    boolean expanded;

    public ExpandableHeightGridView(Context context) {
        super(context);
        this.expanded = false;
    }

    public ExpandableHeightGridView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.expanded = false;
    }

    public ExpandableHeightGridView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.expanded = false;
    }

    public boolean isExpanded() {
        return this.expanded;
    }

    public void onMeasure(int i, int i2) {
        if (isExpanded()) {
            super.onMeasure(i, MeasureSpec.makeMeasureSpec(ViewCompat.MEASURED_SIZE_MASK, RtlSpacingHelper.UNDEFINED));
            getLayoutParams().height = getMeasuredHeight();
            return;
        }
        super.onMeasure(i, i2);
    }

    public void setExpanded(boolean z) {
        this.expanded = z;
    }
}
