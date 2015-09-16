package com.nna88.voz.util;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class SwipeDetector implements OnTouchListener {
    public static final int DIRECTION_SWIPE_CLICK = 0;
    public static final int DIRECTION_SWIPE_DOWN = 5;
    public static final int DIRECTION_SWIPE_LEFT = 2;
    public static final int DIRECTION_SWIPE_RIGHT = 3;
    public static final int DIRECTION_SWIPE_UP = 4;
    private float MAX_CLICK_DISTANCE;
    private float MIN_SWIPE_DISTANCE;
    private long downTime;
    private OnSwipeListener mListener;
    private float startX;
    private float startY;

    public interface OnSwipeListener {
        void onSwipe(View view, int i);
    }

    public SwipeDetector() {
        this.MIN_SWIPE_DISTANCE = 100.0f;
        this.MAX_CLICK_DISTANCE = 2.0f;
    }

    private void log(String str) {
        Log.d("nna", str);
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (this.mListener == null) {
            return true;
        }
        if (motionEvent.getAction() == 0) {
            this.downTime = System.currentTimeMillis();
            this.startX = motionEvent.getRawX();
            this.startY = motionEvent.getRawY();
            return false;
        }
        float rawX, rawY;

        if (motionEvent.getAction() == 1) {
            rawX = motionEvent.getRawX();
            rawY = motionEvent.getRawY();
            if (this.startX - rawX <= this.MAX_CLICK_DISTANCE && this.startY - rawY < this.MAX_CLICK_DISTANCE) {
                this.mListener.onSwipe(view, DIRECTION_SWIPE_CLICK);
                return false;
            }
        }
        if (motionEvent.getAction() != 1 && motionEvent.getAction() != DIRECTION_SWIPE_RIGHT) {
            return false;
        }
        float rawX2 = motionEvent.getRawX();
        rawX = motionEvent.getRawY();
        rawY = (float) Math.sqrt(Math.pow((double) (this.startX - rawX2), 2.0d) + Math.pow((double) (this.startY - rawX), 2.0d));
        float currentTimeMillis = rawY / ((float) ((System.currentTimeMillis() - this.downTime) / 1000));
        if (rawY >= this.MIN_SWIPE_DISTANCE) {
            Math.abs(this.startX - rawX2);
            Math.abs(this.startY - rawX);
            if (this.startX > rawX2) {
                this.mListener.onSwipe(view, DIRECTION_SWIPE_LEFT);
            } else {
                this.mListener.onSwipe(view, DIRECTION_SWIPE_RIGHT);
            }
        }
        return false;
    }

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.mListener = onSwipeListener;
    }
}
