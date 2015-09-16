package com.nna88.voz.listview;

import android.content.Context;
import android.text.Html;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.method.Touch;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

public class TextViewFixTouchConsume extends TextView {
    boolean dontConsumeNonUrlClicks;
    boolean linkHit;

    public static class LocalLinkMovementMethod extends LinkMovementMethod {
        static LocalLinkMovementMethod sInstance;

        public static LocalLinkMovementMethod getInstance() {
            if (sInstance == null) {
                sInstance = new LocalLinkMovementMethod();
            }
            return sInstance;
        }

        public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            if (action != 1 && action != 0) {
                return Touch.onTouchEvent(textView, spannable, motionEvent);
            }
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            int totalPaddingLeft = textView.getTotalPaddingLeft();
            int totalPaddingTop = textView.getTotalPaddingTop();
            int scrollX = textView.getScrollX();
            int scrollY = textView.getScrollY();
            Layout layout = textView.getLayout();
            x = layout.getOffsetForHorizontal(layout.getLineForVertical((y - totalPaddingTop) + scrollY), (float) ((x - totalPaddingLeft) + scrollX));
            ClickableSpan[] clickableSpanArr = (ClickableSpan[]) spannable.getSpans(x, x, ClickableSpan.class);
            if (clickableSpanArr.length != 0) {
                if (action == 1) {
                    clickableSpanArr[0].onClick(textView);
                } else if (action == 0) {
                    Selection.setSelection(spannable, spannable.getSpanStart(clickableSpanArr[0]), spannable.getSpanEnd(clickableSpanArr[0]));
                }
                if (textView instanceof TextViewFixTouchConsume) {
                    ((TextViewFixTouchConsume) textView).linkHit = true;
                }
                return true;
            }
            Selection.removeSelection(spannable);
            Touch.onTouchEvent(textView, spannable, motionEvent);
            return false;
        }
    }

    public TextViewFixTouchConsume(Context context) {
        super(context);
        this.dontConsumeNonUrlClicks = true;
    }

    public TextViewFixTouchConsume(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.dontConsumeNonUrlClicks = true;
    }

    public TextViewFixTouchConsume(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.dontConsumeNonUrlClicks = true;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.linkHit = false;
        return this.dontConsumeNonUrlClicks ? this.linkHit : super.onTouchEvent(motionEvent);
    }

    public void setTextViewHTML(String str) {
        setText(new SpannableStringBuilder(Html.fromHtml(str)));
    }
}
