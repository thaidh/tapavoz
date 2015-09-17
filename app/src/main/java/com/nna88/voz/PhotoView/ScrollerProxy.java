package com.nna88.voz.PhotoView;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.widget.OverScroller;
import android.widget.Scroller;

public abstract class ScrollerProxy {

    @TargetApi(9)
    private static class GingerScroller extends ScrollerProxy {
        private OverScroller mScroller;

        public GingerScroller(Context context) {
            this.mScroller = new OverScroller(context);
        }

        public boolean computeScrollOffset() {
            return this.mScroller.computeScrollOffset();
        }

        public void fling(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
            this.mScroller.fling(i, i2, i3, i4, i5, i6, i7, i8, i9, i10);
        }

        public void forceFinished(boolean z) {
            this.mScroller.forceFinished(z);
        }

        public int getCurrX() {
            return this.mScroller.getCurrX();
        }

        public int getCurrY() {
            return this.mScroller.getCurrY();
        }
    }

    private static class PreGingerScroller extends ScrollerProxy {
        private Scroller mScroller;

        public PreGingerScroller(Context context) {
            this.mScroller = new Scroller(context);
        }

        public boolean computeScrollOffset() {
            return this.mScroller.computeScrollOffset();
        }

        public void fling(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
            this.mScroller.fling(i, i2, i3, i4, i5, i6, i7, i8);
        }

        public void forceFinished(boolean z) {
            this.mScroller.forceFinished(z);
        }

        public int getCurrX() {
            return this.mScroller.getCurrX();
        }

        public int getCurrY() {
            return this.mScroller.getCurrY();
        }
    }

    public static ScrollerProxy getScroller(Context context) {
        return VERSION.SDK_INT < 9 ? new PreGingerScroller(context) : new GingerScroller(context);
    }

    public abstract boolean computeScrollOffset();

    public abstract void fling(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10);

    public abstract void forceFinished(boolean z);

    public abstract int getCurrX();

    public abstract int getCurrY();
}
