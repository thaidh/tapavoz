package com.whoami.voz.adapter.list;

import android.content.Context;
import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

public class CustomLinkMovementMethod extends LinkMovementMethod {
    private static CustomLinkMovementMethod linkMovementMethod;
    private static Context movementContext;

    static {
        linkMovementMethod = new CustomLinkMovementMethod();
    }

    public static MovementMethod getInstance(Context context) {
        movementContext = context;
        return linkMovementMethod;
    }

    public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1) {
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            int totalPaddingLeft = textView.getTotalPaddingLeft();
            int totalPaddingTop = textView.getTotalPaddingTop();
            int scrollX = textView.getScrollX();
            int scrollY = textView.getScrollY();
            Layout layout = textView.getLayout();
            x = layout.getOffsetForHorizontal(layout.getLineForVertical((y - totalPaddingTop) + scrollY), (float) ((x - totalPaddingLeft) + scrollX));
            URLSpan[] uRLSpanArr = (URLSpan[]) spannable.getSpans(x, x, URLSpan.class);
            if (uRLSpanArr.length != 0) {
                String url = uRLSpanArr[0].getURL();
                if (url.contains("https")) {
                    Log.d("Link", url);
                    Toast.makeText(movementContext, "Link was clicked", 1).show();
                } else if (url.contains("tel")) {
                    Log.d("Link", url);
                    Toast.makeText(movementContext, "Tel was clicked", 1).show();
                    return true;
                } else if (url.contains("mailto")) {
                    Log.d("Link", url);
                    Toast.makeText(movementContext, "Mail link was clicked", 1).show();
                    return true;
                }
                return true;
            }
        }
        return super.onTouchEvent(textView, spannable, motionEvent);
    }
}
