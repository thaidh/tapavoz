package com.nna88.voz.PhotoView;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.support.v4.view.MotionEventCompat;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import com.google.android.gms.location.LocationStatusCodes;
import com.nna88.voz.main.R;
import org.apache.http.conn.routing.HttpRouteDirector;

public abstract class VersionedGestureDetector {
    static final String LOG_TAG = "VersionedGestureDetector";
    OnGestureListener mListener;

    public interface OnGestureListener {
        void onDrag(float f, float f2);

        void onFling(float f, float f2, float f3, float f4);

        void onScale(float f, float f2, float f3);
    }

    private static class CupcakeDetector extends VersionedGestureDetector {
        private boolean mIsDragging;
        float mLastTouchX;
        float mLastTouchY;
        final float mMinimumVelocity;
        final float mTouchSlop;
        private VelocityTracker mVelocityTracker;

        public CupcakeDetector(Context context) {
            ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
            this.mMinimumVelocity = (float) viewConfiguration.getScaledMinimumFlingVelocity();
            this.mTouchSlop = (float) viewConfiguration.getScaledTouchSlop();
        }

        float getActiveX(MotionEvent motionEvent) {
            return motionEvent.getX();
        }

        float getActiveY(MotionEvent motionEvent) {
            return motionEvent.getY();
        }

        public boolean isScaling() {
            return false;
        }

        public boolean onTouchEvent(MotionEvent motionEvent) {
            boolean z = false;
            float yVelocity;
            switch (motionEvent.getAction()) {
                case HttpRouteDirector.COMPLETE /*0*/:
                    this.mVelocityTracker = VelocityTracker.obtain();
                    this.mVelocityTracker.addMovement(motionEvent);
                    this.mLastTouchX = getActiveX(motionEvent);
                    this.mLastTouchY = getActiveY(motionEvent);
                    this.mIsDragging = false;
                    break;
                case HttpRouteDirector.CONNECT_TARGET /*1*/:
                    if (this.mIsDragging && this.mVelocityTracker != null) {
                        this.mLastTouchX = getActiveX(motionEvent);
                        this.mLastTouchY = getActiveY(motionEvent);
                        this.mVelocityTracker.addMovement(motionEvent);
                        this.mVelocityTracker.computeCurrentVelocity(LocationStatusCodes.GEOFENCE_NOT_AVAILABLE);
                        float xVelocity = this.mVelocityTracker.getXVelocity();
                        yVelocity = this.mVelocityTracker.getYVelocity();
                        if (Math.max(Math.abs(xVelocity), Math.abs(yVelocity)) >= this.mMinimumVelocity) {
                            this.mListener.onFling(this.mLastTouchX, this.mLastTouchY, -xVelocity, -yVelocity);
                        }
                    }
                    if (this.mVelocityTracker != null) {
                        this.mVelocityTracker.recycle();
                        this.mVelocityTracker = null;
                        break;
                    }
                    break;
                case HttpRouteDirector.CONNECT_PROXY /*2*/:
                    yVelocity = getActiveX(motionEvent);
                    float activeY = getActiveY(motionEvent);
                    float f = yVelocity - this.mLastTouchX;
                    float f2 = activeY - this.mLastTouchY;
                    if (!this.mIsDragging) {
                        if (Math.sqrt((f * f) + (f2 * f2)) >= this.mTouchSlop) {
                            z = true;
                        }
                        this.mIsDragging = z;
                    }
                    if (this.mIsDragging) {
                        this.mListener.onDrag(f, f2);
                        this.mLastTouchX = yVelocity;
                        this.mLastTouchY = activeY;
                        if (this.mVelocityTracker != null) {
                            this.mVelocityTracker.addMovement(motionEvent);
                            break;
                        }
                    }
                    break;
                case HttpRouteDirector.TUNNEL_TARGET /*3*/:
                    if (this.mVelocityTracker != null) {
                        this.mVelocityTracker.recycle();
                        this.mVelocityTracker = null;
                        break;
                    }
                    break;
            }
            return true;
        }
    }

    @TargetApi(5)
    private static class EclairDetector extends CupcakeDetector {
        private static final int INVALID_POINTER_ID = -1;
        private int mActivePointerId;
        private int mActivePointerIndex;

        public EclairDetector(Context context) {
            super(context);
            this.mActivePointerId = INVALID_POINTER_ID;
            this.mActivePointerIndex = 0;
        }

        float getActiveX(MotionEvent motionEvent) {
            try {
                return motionEvent.getX(this.mActivePointerIndex);
            } catch (Exception e) {
                return motionEvent.getX();
            }
        }

        float getActiveY(MotionEvent motionEvent) {
            try {
                return motionEvent.getY(this.mActivePointerIndex);
            } catch (Exception e) {
                return motionEvent.getY();
            }
        }

        public boolean onTouchEvent(MotionEvent motionEvent) {
            int i = 0;
            switch (motionEvent.getAction() & MotionEventCompat.ACTION_MASK) {
                case HttpRouteDirector.COMPLETE /*0*/:
                    this.mActivePointerId = motionEvent.getPointerId(0);
                    break;
                case HttpRouteDirector.CONNECT_TARGET /*1*/:
                case HttpRouteDirector.TUNNEL_TARGET /*3*/:
                    this.mActivePointerId = INVALID_POINTER_ID;
                    break;
                case R.styleable.Toolbar_contentInsetLeft /*6*/:
                    int action = (motionEvent.getAction() & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                    if (motionEvent.getPointerId(action) == this.mActivePointerId) {
                        action = action == 0 ? 1 : 0;
                        this.mActivePointerId = motionEvent.getPointerId(action);
                        this.mLastTouchX = motionEvent.getX(action);
                        this.mLastTouchY = motionEvent.getY(action);
                        break;
                    }
                    break;
            }
            if (this.mActivePointerId != INVALID_POINTER_ID) {
                i = this.mActivePointerId;
            }
            this.mActivePointerIndex = motionEvent.findPointerIndex(i);
            return super.onTouchEvent(motionEvent);
        }
    }

    @TargetApi(8)
    private static class FroyoDetector extends EclairDetector {
        private final ScaleGestureDetector mDetector;
        private final OnScaleGestureListener mScaleListener;

        public FroyoDetector(Context context) {
            super(context);
            this.mScaleListener = new OnScaleGestureListener() {
                public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
                    FroyoDetector.this.mListener.onScale(scaleGestureDetector.getScaleFactor(), scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
                    return true;
                }

                public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
                    return true;
                }

                public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
                }
            };
            this.mDetector = new ScaleGestureDetector(context, this.mScaleListener);
        }

        public boolean isScaling() {
            return this.mDetector.isInProgress();
        }

        public boolean onTouchEvent(MotionEvent motionEvent) {
            this.mDetector.onTouchEvent(motionEvent);
            return super.onTouchEvent(motionEvent);
        }
    }

    public static VersionedGestureDetector newInstance(Context context, OnGestureListener onGestureListener) {
        int i = VERSION.SDK_INT;
        VersionedGestureDetector cupcakeDetector = i < 5 ? new CupcakeDetector(context) : i < 8 ? new EclairDetector(context) : new FroyoDetector(context);
        cupcakeDetector.mListener = onGestureListener;
        return cupcakeDetector;
    }

    public abstract boolean isScaling();

    public abstract boolean onTouchEvent(MotionEvent motionEvent);
}
