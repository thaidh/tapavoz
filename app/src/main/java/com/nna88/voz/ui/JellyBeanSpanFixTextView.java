package com.nna88.voz.ui;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class JellyBeanSpanFixTextView extends TextView {
    private static final String TAG;

    private static class FixingResult {
        public final boolean fixed;
        public final List<Object> spansWithSpacesAfter;
        public final List<Object> spansWithSpacesBefore;

        private FixingResult(boolean z, List<Object> list, List<Object> list2) {
            this.fixed = z;
            this.spansWithSpacesBefore = list;
            this.spansWithSpacesAfter = list2;
        }

        public static FixingResult fixed(List<Object> list, List<Object> list2) {
            return new FixingResult(true, list, list2);
        }

        public static FixingResult notFixed() {
            return new FixingResult(false, null, null);
        }
    }

    static {
        TAG = JellyBeanSpanFixTextView.class.getSimpleName();
    }

    public JellyBeanSpanFixTextView(Context context) {
        super(context);
    }

    public JellyBeanSpanFixTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public JellyBeanSpanFixTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    private FixingResult addSpacesAroundSpansUntilFixed(SpannableStringBuilder spannableStringBuilder, int i, int i2) {
        int i3 = 0;
        Object[] spans = spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), Object.class);
        List arrayList = new ArrayList(spans.length);
        List arrayList2 = new ArrayList(spans.length);
        int length = spans.length;
        while (i3 < length) {
            Object obj = spans[i3];
            int spanStart = spannableStringBuilder.getSpanStart(obj);
            if (isNotSpace(spannableStringBuilder, spanStart - 1)) {
                spannableStringBuilder.insert(spanStart, " ");
                arrayList.add(obj);
            }
            spanStart = spannableStringBuilder.getSpanEnd(obj);
            if (isNotSpace(spannableStringBuilder, spanStart)) {
                spannableStringBuilder.insert(spanStart, " ");
                arrayList2.add(obj);
            }
            try {
                setTextAndMeasure(spannableStringBuilder, i, i2);
                return FixingResult.fixed(arrayList, arrayList2);
            } catch (IndexOutOfBoundsException e) {
                i3++;
            }
        }
        return FixingResult.notFixed();
    }

    private void fallbackToString(int i, int i2) {
        setTextAndMeasure(getText().toString(), i, i2);
    }

    private void fixOnMeasure(int i, int i2) {
        CharSequence text = getText();
        if (text instanceof Spanned) {
            fixSpannedWithSpaces(new SpannableStringBuilder(text), i, i2);
        } else {
            fallbackToString(i, i2);
        }
    }

    private void fixSpannedWithSpaces(SpannableStringBuilder spannableStringBuilder, int i, int i2) {
        System.currentTimeMillis();
        FixingResult addSpacesAroundSpansUntilFixed = addSpacesAroundSpansUntilFixed(spannableStringBuilder, i, i2);
        if (addSpacesAroundSpansUntilFixed.fixed) {
            removeUnneededSpaces(i, i2, spannableStringBuilder, addSpacesAroundSpansUntilFixed);
        } else {
            fallbackToString(i, i2);
        }
    }

    private boolean isNotSpace(CharSequence charSequence, int i) {
        return charSequence.charAt(i) != ' ';
    }

    private void removeUnneededSpaces(int i, int i2, SpannableStringBuilder spannableStringBuilder, FixingResult fixingResult) {
        for (Object spanEnd : fixingResult.spansWithSpacesAfter) {
            int spanEnd2 = spannableStringBuilder.getSpanEnd(spanEnd);
            spannableStringBuilder.delete(spanEnd2, spanEnd2 + 1);
            try {
                setTextAndMeasure(spannableStringBuilder, i, i2);
            } catch (IndexOutOfBoundsException e) {
                spannableStringBuilder.insert(spanEnd2, " ");
            }
        }
        Object obj = 1;
        for (Object obj2 : fixingResult.spansWithSpacesBefore) {
            int spanStart = spannableStringBuilder.getSpanStart(obj2);
            spannableStringBuilder.delete(spanStart - 1, spanStart);
            try {
                setTextAndMeasure(spannableStringBuilder, i, i2);
                obj2 = null;
            } catch (IndexOutOfBoundsException e2) {
                spannableStringBuilder.insert(spanStart - 1, " ");
                obj2 = 1;
            }
        }
//        if (obj2 != null) {
//            setText(spannableStringBuilder);
//            super.onMeasure(i, i2);
//        }
    }

    private void setTextAndMeasure(CharSequence charSequence, int i, int i2) {
        setText(charSequence);
        super.onMeasure(i, i2);
    }

    protected void onMeasure(int i, int i2) {
        try {
            super.onMeasure(i, i2);
        } catch (IndexOutOfBoundsException e) {
            fixOnMeasure(i, i2);
        }
    }
}
