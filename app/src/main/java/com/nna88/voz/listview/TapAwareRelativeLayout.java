//package com.nna88.voz.listview;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.widget.LinearLayout;
//import org.apache.http.conn.routing.HttpRouteDirector;
//
//public class TapAwareRelativeLayout extends LinearLayout {
//    private final float MOVE_THRESHOLD_DP;
//    private float mDownPosX;
//    private float mDownPosY;
//    private boolean mMoveOccured;
//
//    public TapAwareRelativeLayout(Context context) {
//        super(context);
//        this.MOVE_THRESHOLD_DP = 20.0f * getResources().getDisplayMetrics().density;
//    }
//
//    public TapAwareRelativeLayout(Context context, AttributeSet attributeSet) {
//        super(context, attributeSet);
//        this.MOVE_THRESHOLD_DP = 20.0f * getResources().getDisplayMetrics().density;
//    }
//
//    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
//        switch (motionEvent.getAction()) {
//            case HttpRouteDirector.COMPLETE /*0*/:
//                this.mMoveOccured = false;
//                this.mDownPosX = motionEvent.getX();
//                this.mDownPosY = motionEvent.getY();
//                break;
//            case HttpRouteDirector.CONNECT_TARGET /*1*/:
//                if (!this.mMoveOccured) {
//                    performClick();
//                    break;
//                }
//                Log.d("nna", "move");
//                break;
//            case HttpRouteDirector.CONNECT_PROXY /*2*/:
//                if (Math.abs(motionEvent.getX() - this.mDownPosX) > this.MOVE_THRESHOLD_DP || Math.abs(motionEvent.getY() - this.mDownPosY) > this.MOVE_THRESHOLD_DP) {
//                    this.mMoveOccured = true;
//                    break;
//                }
//        }
//        return super.onInterceptTouchEvent(motionEvent);
//    }
//}
