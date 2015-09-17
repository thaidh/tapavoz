package com.nna88.voz.PhotoView;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.nna88.voz.PhotoView.VersionedGestureDetector.OnGestureListener;
import java.lang.ref.WeakReference;
import org.apache.http.conn.routing.HttpRouteDirector;

public class PhotoViewAttacher implements IPhotoView, OnTouchListener, OnGestureListener, OnDoubleTapListener, OnGlobalLayoutListener {
    static final boolean DEBUG;
    public static final float DEFAULT_MAX_SCALE = 3.0f;
    public static final float DEFAULT_MID_SCALE = 1.75f;
    public static final float DEFAULT_MIN_SCALE = 1.0f;
    static final int EDGE_BOTH = 2;
    static final int EDGE_LEFT = 0;
    static final int EDGE_NONE = -1;
    static final int EDGE_RIGHT = 1;
    static final String LOG_TAG = "PhotoViewAttacher";
    private boolean mAllowParentInterceptOnEdge;
    private final Matrix mBaseMatrix;
    private FlingRunnable mCurrentFlingRunnable;
    private final RectF mDisplayRect;
    private final Matrix mDrawMatrix;
    private GestureDetector mGestureDetector;
    private WeakReference<ImageView> mImageView;
    private int mIvBottom;
    private int mIvLeft;
    private int mIvRight;
    private int mIvTop;
    private OnLongClickListener mLongClickListener;
    private OnMatrixChangedListener mMatrixChangeListener;
    private final float[] mMatrixValues;
    private float mMaxScale;
    private float mMidScale;
    private float mMinScale;
    private OnPhotoTapListener mPhotoTapListener;
    private VersionedGestureDetector mScaleDragDetector;
    private ScaleType mScaleType;
    private int mScrollEdge;
    private final Matrix mSuppMatrix;
    private OnViewTapListener mViewTapListener;
    private ViewTreeObserver mViewTreeObserver;
    private boolean mZoomEnabled;

    /* renamed from: com.nna88.voz.PhotoView.PhotoViewAttacher.2 */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$android$widget$ImageView$ScaleType;

        static {
            $SwitchMap$android$widget$ImageView$ScaleType = new int[ScaleType.values().length];
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.MATRIX.ordinal()] = PhotoViewAttacher.EDGE_RIGHT;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_START.ordinal()] = PhotoViewAttacher.EDGE_BOTH;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_END.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_CENTER.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_XY.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    private class AnimatedZoomRunnable implements Runnable {
        static final float ANIMATION_SCALE_PER_ITERATION_IN = 1.07f;
        static final float ANIMATION_SCALE_PER_ITERATION_OUT = 0.93f;
        private final float mDeltaScale;
        private final float mFocalX;
        private final float mFocalY;
        private final float mTargetZoom;

        public AnimatedZoomRunnable(float f, float f2, float f3, float f4) {
            this.mTargetZoom = f2;
            this.mFocalX = f3;
            this.mFocalY = f4;
            if (f < f2) {
                this.mDeltaScale = ANIMATION_SCALE_PER_ITERATION_IN;
            } else {
                this.mDeltaScale = ANIMATION_SCALE_PER_ITERATION_OUT;
            }
        }

        public void run() {
            View imageView = PhotoViewAttacher.this.getImageView();
            if (imageView != null) {
                PhotoViewAttacher.this.mSuppMatrix.postScale(this.mDeltaScale, this.mDeltaScale, this.mFocalX, this.mFocalY);
                PhotoViewAttacher.this.checkAndDisplayMatrix();
                float scale = PhotoViewAttacher.this.getScale();
                if ((this.mDeltaScale <= PhotoViewAttacher.DEFAULT_MIN_SCALE || scale >= this.mTargetZoom) && (this.mDeltaScale >= PhotoViewAttacher.DEFAULT_MIN_SCALE || this.mTargetZoom >= scale)) {
                    float f = this.mTargetZoom / scale;
                    PhotoViewAttacher.this.mSuppMatrix.postScale(f, f, this.mFocalX, this.mFocalY);
                    PhotoViewAttacher.this.checkAndDisplayMatrix();
                    return;
                }
                Compat.postOnAnimation(imageView, this);
            }
        }
    }

    private class FlingRunnable implements Runnable {
        private int mCurrentX;
        private int mCurrentY;
        private final ScrollerProxy mScroller;

        public FlingRunnable(Context context) {
            this.mScroller = ScrollerProxy.getScroller(context);
        }

        public void cancelFling() {
            if (PhotoViewAttacher.DEBUG) {
                Log.d(PhotoViewAttacher.LOG_TAG, "Cancel Fling");
            }
            this.mScroller.forceFinished(true);
        }

        public void fling(int i, int i2, int i3, int i4) {
            RectF displayRect = PhotoViewAttacher.this.getDisplayRect();
            if (displayRect != null) {
                int round;
                int i5;
                int round2;
                int i6;
                int round3 = Math.round(-displayRect.left);
                if (((float) i) < displayRect.width()) {
                    round = Math.round(displayRect.width() - ((float) i));
                    i5 = PhotoViewAttacher.EDGE_LEFT;
                } else {
                    round = round3;
                    i5 = round3;
                }
                int round4 = Math.round(-displayRect.top);
                if (((float) i2) < displayRect.height()) {
                    round2 = Math.round(displayRect.height() - ((float) i2));
                    i6 = PhotoViewAttacher.EDGE_LEFT;
                } else {
                    round2 = round4;
                    i6 = round4;
                }
                this.mCurrentX = round3;
                this.mCurrentY = round4;
                if (PhotoViewAttacher.DEBUG) {
                    Log.d(PhotoViewAttacher.LOG_TAG, "fling. StartX:" + round3 + " StartY:" + round4 + " MaxX:" + round + " MaxY:" + round2);
                }
                if (round3 != round || round4 != round2) {
                    this.mScroller.fling(round3, round4, i3, i4, i5, round, i6, round2, PhotoViewAttacher.EDGE_LEFT, PhotoViewAttacher.EDGE_LEFT);
                }
            }
        }

        public void run() {
            View imageView = PhotoViewAttacher.this.getImageView();
            if (imageView != null && this.mScroller.computeScrollOffset()) {
                int currX = this.mScroller.getCurrX();
                int currY = this.mScroller.getCurrY();
                if (PhotoViewAttacher.DEBUG) {
                    Log.d(PhotoViewAttacher.LOG_TAG, "fling run(). CurrentX:" + this.mCurrentX + " CurrentY:" + this.mCurrentY + " NewX:" + currX + " NewY:" + currY);
                }
                PhotoViewAttacher.this.mSuppMatrix.postTranslate((float) (this.mCurrentX - currX), (float) (this.mCurrentY - currY));
                PhotoViewAttacher.this.setImageViewMatrix(PhotoViewAttacher.this.getDisplayMatrix());
                this.mCurrentX = currX;
                this.mCurrentY = currY;
                Compat.postOnAnimation(imageView, this);
            }
        }
    }

    public interface OnMatrixChangedListener {
        void onMatrixChanged(RectF rectF);
    }

    public interface OnPhotoTapListener {
        void onPhotoTap(View view, float f, float f2);
    }

    public interface OnViewTapListener {
        void onViewTap(View view, float f, float f2);
    }

    static {
        DEBUG = Log.isLoggable(LOG_TAG, 3);
    }

    public PhotoViewAttacher(ImageView imageView) {
        this.mMinScale = DEFAULT_MIN_SCALE;
        this.mMidScale = DEFAULT_MID_SCALE;
        this.mMaxScale = DEFAULT_MAX_SCALE;
        this.mAllowParentInterceptOnEdge = true;
        this.mBaseMatrix = new Matrix();
        this.mDrawMatrix = new Matrix();
        this.mSuppMatrix = new Matrix();
        this.mDisplayRect = new RectF();
        this.mMatrixValues = new float[9];
        this.mScrollEdge = EDGE_BOTH;
        this.mScaleType = ScaleType.FIT_CENTER;
        this.mImageView = new WeakReference(imageView);
        imageView.setOnTouchListener(this);
        this.mViewTreeObserver = imageView.getViewTreeObserver();
        this.mViewTreeObserver.addOnGlobalLayoutListener(this);
        setImageViewScaleTypeMatrix(imageView);
        if (!imageView.isInEditMode()) {
            this.mScaleDragDetector = VersionedGestureDetector.newInstance(imageView.getContext(), this);
            this.mGestureDetector = new GestureDetector(imageView.getContext(), new SimpleOnGestureListener() {
                public void onLongPress(MotionEvent motionEvent) {
                    if (PhotoViewAttacher.this.mLongClickListener != null) {
                        PhotoViewAttacher.this.mLongClickListener.onLongClick((View) PhotoViewAttacher.this.mImageView.get());
                    }
                }
            });
            this.mGestureDetector.setOnDoubleTapListener(this);
            setZoomable(true);
        }
    }

    private void cancelFling() {
        if (this.mCurrentFlingRunnable != null) {
            this.mCurrentFlingRunnable.cancelFling();
            this.mCurrentFlingRunnable = null;
        }
    }

    private void checkAndDisplayMatrix() {
        checkMatrixBounds();
        setImageViewMatrix(getDisplayMatrix());
    }

    private void checkImageViewScaleType() {
        ImageView imageView = getImageView();
        if (imageView != null && !(imageView instanceof PhotoView) && imageView.getScaleType() != ScaleType.MATRIX) {
            throw new IllegalStateException("The ImageView's ScaleType has been changed since attaching a PhotoViewAttacher");
        }
    }

    private void checkMatrixBounds() {
        float f = 0.0f;
        ImageView imageView = getImageView();
        if (imageView != null) {
            RectF displayRect = getDisplayRect(getDisplayMatrix());
            if (displayRect != null) {
                float height = displayRect.height();
                float width = displayRect.width();
                int height2 = imageView.getHeight();
                if (height <= ((float) height2)) {
                    switch (AnonymousClass2.$SwitchMap$android$widget$ImageView$ScaleType[this.mScaleType.ordinal()]) {
                        case EDGE_BOTH /*2*/:
                            height = -displayRect.top;
                            break;
                        case HttpRouteDirector.TUNNEL_TARGET /*3*/:
                            height = (((float) height2) - height) - displayRect.top;
                            break;
                        default:
                            height = ((((float) height2) - height) / 2.0f) - displayRect.top;
                            break;
                    }
                }
                height = displayRect.top > 0.0f ? -displayRect.top : displayRect.bottom < ((float) height2) ? ((float) height2) - displayRect.bottom : 0.0f;
                int width2 = imageView.getWidth();
                if (width <= ((float) width2)) {
                    switch (AnonymousClass2.$SwitchMap$android$widget$ImageView$ScaleType[this.mScaleType.ordinal()]) {
                        case EDGE_BOTH /*2*/:
                            f = -displayRect.left;
                            break;
                        case HttpRouteDirector.TUNNEL_TARGET /*3*/:
                            f = (((float) width2) - width) - displayRect.left;
                            break;
                        default:
                            f = ((((float) width2) - width) / 2.0f) - displayRect.left;
                            break;
                    }
                    this.mScrollEdge = EDGE_BOTH;
                } else if (displayRect.left > 0.0f) {
                    this.mScrollEdge = EDGE_LEFT;
                    f = -displayRect.left;
                } else if (displayRect.right < ((float) width2)) {
                    f = ((float) width2) - displayRect.right;
                    this.mScrollEdge = EDGE_RIGHT;
                } else {
                    this.mScrollEdge = EDGE_NONE;
                }
                this.mSuppMatrix.postTranslate(f, height);
            }
        }
    }

    private static void checkZoomLevels(float f, float f2, float f3) {
        if (f >= f2) {
            throw new IllegalArgumentException("MinZoom should be less than MidZoom");
        } else if (f2 >= f3) {
            throw new IllegalArgumentException("MidZoom should be less than MaxZoom");
        }
    }

    private RectF getDisplayRect(Matrix matrix) {
        ImageView imageView = getImageView();
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable != null) {
                this.mDisplayRect.set(0.0f, 0.0f, (float) drawable.getIntrinsicWidth(), (float) drawable.getIntrinsicHeight());
                matrix.mapRect(this.mDisplayRect);
                return this.mDisplayRect;
            }
        }
        return null;
    }

    private float getValue(Matrix matrix, int i) {
        matrix.getValues(this.mMatrixValues);
        return this.mMatrixValues[i];
    }

    private static boolean hasDrawable(ImageView imageView) {
        return (imageView == null || imageView.getDrawable() == null) ? DEBUG : true;
    }

    private static boolean isSupportedScaleType(ScaleType scaleType) {
        if (scaleType == null) {
            return DEBUG;
        }
        switch (AnonymousClass2.$SwitchMap$android$widget$ImageView$ScaleType[scaleType.ordinal()]) {
            case EDGE_RIGHT /*1*/:
                throw new IllegalArgumentException(scaleType.name() + " is not supported in PhotoView");
            default:
                return true;
        }
    }

    private void resetMatrix() {
        this.mSuppMatrix.reset();
        setImageViewMatrix(getDisplayMatrix());
        checkMatrixBounds();
    }

    private void setImageViewMatrix(Matrix matrix) {
        ImageView imageView = getImageView();
        if (imageView != null) {
            checkImageViewScaleType();
            imageView.setImageMatrix(matrix);
            if (this.mMatrixChangeListener != null) {
                RectF displayRect = getDisplayRect(matrix);
                if (displayRect != null) {
                    this.mMatrixChangeListener.onMatrixChanged(displayRect);
                }
            }
        }
    }

    private static void setImageViewScaleTypeMatrix(ImageView imageView) {
        if (imageView != null && !(imageView instanceof PhotoView)) {
            imageView.setScaleType(ScaleType.MATRIX);
        }
    }

    private void updateBaseMatrix(Drawable drawable) {
        ImageView imageView = getImageView();
        if (imageView != null && drawable != null) {
            float width = (float) imageView.getWidth();
            float height = (float) imageView.getHeight();
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            this.mBaseMatrix.reset();
            float f = width / ((float) intrinsicWidth);
            float f2 = height / ((float) intrinsicHeight);
            if (this.mScaleType != ScaleType.CENTER) {
                if (this.mScaleType != ScaleType.CENTER_CROP) {
                    if (this.mScaleType != ScaleType.CENTER_INSIDE) {
                        RectF rectF = new RectF(0.0f, 0.0f, (float) intrinsicWidth, (float) intrinsicHeight);
                        RectF rectF2 = new RectF(0.0f, 0.0f, width, height);
                        switch (AnonymousClass2.$SwitchMap$android$widget$ImageView$ScaleType[this.mScaleType.ordinal()]) {
                            case EDGE_BOTH /*2*/:
                                this.mBaseMatrix.setRectToRect(rectF, rectF2, ScaleToFit.START);
                                break;
                            case HttpRouteDirector.TUNNEL_TARGET /*3*/:
                                this.mBaseMatrix.setRectToRect(rectF, rectF2, ScaleToFit.END);
                                break;
                            case HttpRouteDirector.TUNNEL_PROXY /*4*/:
                                this.mBaseMatrix.setRectToRect(rectF, rectF2, ScaleToFit.CENTER);
                                break;
                            case HttpRouteDirector.LAYER_PROTOCOL /*5*/:
                                this.mBaseMatrix.setRectToRect(rectF, rectF2, ScaleToFit.FILL);
                                break;
                            default:
                                break;
                        }
                    }
                    f = Math.min(DEFAULT_MIN_SCALE, Math.min(f, f2));
                    this.mBaseMatrix.postScale(f, f);
                    this.mBaseMatrix.postTranslate((width - (((float) intrinsicWidth) * f)) / 2.0f, (height - (((float) intrinsicHeight) * f)) / 2.0f);
                } else {
                    f = Math.max(f, f2);
                    this.mBaseMatrix.postScale(f, f);
                    this.mBaseMatrix.postTranslate((width - (((float) intrinsicWidth) * f)) / 2.0f, (height - (((float) intrinsicHeight) * f)) / 2.0f);
                }
            } else {
                this.mBaseMatrix.postTranslate((width - ((float) intrinsicWidth)) / 2.0f, (height - ((float) intrinsicHeight)) / 2.0f);
            }
            resetMatrix();
        }
    }

    public final boolean canZoom() {
        return this.mZoomEnabled;
    }

    public final void cleanup() {
        if (this.mImageView != null) {
            ((ImageView) this.mImageView.get()).getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
        this.mViewTreeObserver = null;
        this.mMatrixChangeListener = null;
        this.mPhotoTapListener = null;
        this.mViewTapListener = null;
        this.mImageView = null;
    }

    protected Matrix getDisplayMatrix() {
        this.mDrawMatrix.set(this.mBaseMatrix);
        this.mDrawMatrix.postConcat(this.mSuppMatrix);
        return this.mDrawMatrix;
    }

    public final RectF getDisplayRect() {
        checkMatrixBounds();
        return getDisplayRect(getDisplayMatrix());
    }

    public final ImageView getImageView() {
        ImageView imageView = null;
        if (this.mImageView != null) {
            imageView = (ImageView) this.mImageView.get();
        }
        if (imageView != null) {
            return imageView;
        }
        cleanup();
        throw new IllegalStateException("ImageView no longer exists. You should not use this PhotoViewAttacher any more.");
    }

    public float getMaxScale() {
        return this.mMaxScale;
    }

    public float getMidScale() {
        return this.mMidScale;
    }

    public float getMinScale() {
        return this.mMinScale;
    }

    public final float getScale() {
        return getValue(this.mSuppMatrix, EDGE_LEFT);
    }

    public final ScaleType getScaleType() {
        return this.mScaleType;
    }

    public final boolean onDoubleTap(MotionEvent motionEvent) {
        try {
            float scale = getScale();
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            if (scale < this.mMidScale) {
                zoomTo(this.mMidScale, x, y);
            } else if (scale < this.mMidScale || scale >= this.mMaxScale) {
                zoomTo(this.mMinScale, x, y);
            } else {
                zoomTo(this.mMaxScale, x, y);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return true;
    }

    public final boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return DEBUG;
    }

    public final void onDrag(float f, float f2) {
        if (DEBUG) {
            String str = LOG_TAG;
            Object[] objArr = new Object[EDGE_BOTH];
            objArr[EDGE_LEFT] = Float.valueOf(f);
            objArr[EDGE_RIGHT] = Float.valueOf(f2);
            Log.d(str, String.format("onDrag: dx: %.2f. dy: %.2f", objArr));
        }
        ImageView imageView = getImageView();
        if (imageView != null && hasDrawable(imageView)) {
            this.mSuppMatrix.postTranslate(f, f2);
            checkAndDisplayMatrix();
            if (this.mAllowParentInterceptOnEdge && !this.mScaleDragDetector.isScaling()) {
                if (this.mScrollEdge == EDGE_BOTH || ((this.mScrollEdge == 0 && f >= DEFAULT_MIN_SCALE) || (this.mScrollEdge == EDGE_RIGHT && f <= GroundOverlayOptions.NO_DIMENSION))) {
                    imageView.getParent().requestDisallowInterceptTouchEvent(DEBUG);
                }
            }
        }
    }

    public final void onFling(float f, float f2, float f3, float f4) {
        if (DEBUG) {
            Log.d(LOG_TAG, "onFling. sX: " + f + " sY: " + f2 + " Vx: " + f3 + " Vy: " + f4);
        }
        ImageView imageView = getImageView();
        if (hasDrawable(imageView)) {
            this.mCurrentFlingRunnable = new FlingRunnable(imageView.getContext());
            this.mCurrentFlingRunnable.fling(imageView.getWidth(), imageView.getHeight(), (int) f3, (int) f4);
            imageView.post(this.mCurrentFlingRunnable);
        }
    }

    public final void onGlobalLayout() {
        ImageView imageView = getImageView();
        if (imageView != null && this.mZoomEnabled) {
            int top = imageView.getTop();
            int right = imageView.getRight();
            int bottom = imageView.getBottom();
            int left = imageView.getLeft();
            if (top != this.mIvTop || bottom != this.mIvBottom || left != this.mIvLeft || right != this.mIvRight) {
                updateBaseMatrix(imageView.getDrawable());
                this.mIvTop = top;
                this.mIvRight = right;
                this.mIvBottom = bottom;
                this.mIvLeft = left;
            }
        }
    }

    public final void onScale(float f, float f2, float f3) {
        if (DEBUG) {
            Log.d(LOG_TAG, String.format("onScale: scale: %.2f. fX: %.2f. fY: %.2f", new Object[]{Float.valueOf(f), Float.valueOf(f2), Float.valueOf(f3)}));
        }
        if (!hasDrawable(getImageView())) {
            return;
        }
        if (getScale() < this.mMaxScale || f < DEFAULT_MIN_SCALE) {
            this.mSuppMatrix.postScale(f, f, f2, f3);
            checkAndDisplayMatrix();
        }
    }

    public final boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        View imageView = getImageView();
        if (imageView != null) {
            if (this.mPhotoTapListener != null) {
                RectF displayRect = getDisplayRect();
                if (displayRect != null) {
                    float x = motionEvent.getX();
                    float y = motionEvent.getY();
                    if (displayRect.contains(x, y)) {
                        this.mPhotoTapListener.onPhotoTap(imageView, (x - displayRect.left) / displayRect.width(), (y - displayRect.top) / displayRect.height());
                        return true;
                    }
                }
            }
            if (this.mViewTapListener != null) {
                this.mViewTapListener.onViewTap(imageView, motionEvent.getX(), motionEvent.getY());
            }
        }
        return DEBUG;
    }

    public final boolean onTouch(View view, MotionEvent motionEvent) {
        boolean z = DEBUG;
        if (!this.mZoomEnabled) {
            return DEBUG;
        }
        switch (motionEvent.getAction()) {
            case EDGE_LEFT /*0*/:
                view.getParent().requestDisallowInterceptTouchEvent(true);
                cancelFling();
                break;
            case EDGE_RIGHT /*1*/:
            case HttpRouteDirector.TUNNEL_TARGET /*3*/:
                if (getScale() < this.mMinScale) {
                    RectF displayRect = getDisplayRect();
                    if (displayRect != null) {
                        view.post(new AnimatedZoomRunnable(getScale(), this.mMinScale, displayRect.centerX(), displayRect.centerY()));
                        z = true;
                        break;
                    }
                }
                break;
        }
        if (this.mGestureDetector != null && this.mGestureDetector.onTouchEvent(motionEvent)) {
            z = true;
        }
        return (this.mScaleDragDetector == null || !this.mScaleDragDetector.onTouchEvent(motionEvent)) ? z : true;
    }

    public void setAllowParentInterceptOnEdge(boolean z) {
        this.mAllowParentInterceptOnEdge = z;
    }

    public void setMaxScale(float f) {
        checkZoomLevels(this.mMinScale, this.mMidScale, f);
        this.mMaxScale = f;
    }

    public void setMidScale(float f) {
        checkZoomLevels(this.mMinScale, f, this.mMaxScale);
        this.mMidScale = f;
    }

    public void setMinScale(float f) {
        checkZoomLevels(f, this.mMidScale, this.mMaxScale);
        this.mMinScale = f;
    }

    public final void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.mLongClickListener = onLongClickListener;
    }

    public final void setOnMatrixChangeListener(OnMatrixChangedListener onMatrixChangedListener) {
        this.mMatrixChangeListener = onMatrixChangedListener;
    }

    public final void setOnPhotoTapListener(OnPhotoTapListener onPhotoTapListener) {
        this.mPhotoTapListener = onPhotoTapListener;
    }

    public final void setOnViewTapListener(OnViewTapListener onViewTapListener) {
        this.mViewTapListener = onViewTapListener;
    }

    public final void setScaleType(ScaleType scaleType) {
        if (isSupportedScaleType(scaleType) && scaleType != this.mScaleType) {
            this.mScaleType = scaleType;
            update();
        }
    }

    public final void setZoomable(boolean z) {
        this.mZoomEnabled = z;
        update();
    }

    public final void update() {
        ImageView imageView = getImageView();
        if (imageView == null) {
            return;
        }
        if (this.mZoomEnabled) {
            setImageViewScaleTypeMatrix(imageView);
            updateBaseMatrix(imageView.getDrawable());
            return;
        }
        resetMatrix();
    }

    public final void zoomTo(float f, float f2, float f3) {
        ImageView imageView = getImageView();
        if (imageView != null) {
            imageView.post(new AnimatedZoomRunnable(getScale(), f, f2, f3));
        }
    }
}
