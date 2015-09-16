package com.nna88.voz.PhotoView;

import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.nna88.voz.PhotoView.PhotoViewAttacher.OnMatrixChangedListener;
import com.nna88.voz.PhotoView.PhotoViewAttacher.OnPhotoTapListener;
import com.nna88.voz.PhotoView.PhotoViewAttacher.OnViewTapListener;

public class PhotoView extends ImageView implements IPhotoView {
    private final PhotoViewAttacher mAttacher;
    private ScaleType mPendingScaleType;

    public PhotoView(Context context) {
        this(context, null);
    }

    public PhotoView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PhotoView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        super.setScaleType(ScaleType.MATRIX);
        this.mAttacher = new PhotoViewAttacher(this);
        if (this.mPendingScaleType != null) {
            setScaleType(this.mPendingScaleType);
            this.mPendingScaleType = null;
        }
    }

    public boolean canZoom() {
        return this.mAttacher.canZoom();
    }

    public RectF getDisplayRect() {
        return this.mAttacher.getDisplayRect();
    }

    public float getMaxScale() {
        return this.mAttacher.getMaxScale();
    }

    public float getMidScale() {
        return this.mAttacher.getMidScale();
    }

    public float getMinScale() {
        return this.mAttacher.getMinScale();
    }

    public float getScale() {
        return this.mAttacher.getScale();
    }

    public ScaleType getScaleType() {
        return this.mAttacher.getScaleType();
    }

    protected void onDetachedFromWindow() {
        this.mAttacher.cleanup();
        super.onDetachedFromWindow();
    }

    public void setAllowParentInterceptOnEdge(boolean z) {
        this.mAttacher.setAllowParentInterceptOnEdge(z);
    }

    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        if (this.mAttacher != null) {
            this.mAttacher.update();
        }
    }

    public void setImageResource(int i) {
        super.setImageResource(i);
        if (this.mAttacher != null) {
            this.mAttacher.update();
        }
    }

    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        if (this.mAttacher != null) {
            this.mAttacher.update();
        }
    }

    public void setMaxScale(float f) {
        this.mAttacher.setMaxScale(f);
    }

    public void setMidScale(float f) {
        this.mAttacher.setMidScale(f);
    }

    public void setMinScale(float f) {
        this.mAttacher.setMinScale(f);
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.mAttacher.setOnLongClickListener(onLongClickListener);
    }

    public void setOnMatrixChangeListener(OnMatrixChangedListener onMatrixChangedListener) {
        this.mAttacher.setOnMatrixChangeListener(onMatrixChangedListener);
    }

    public void setOnPhotoTapListener(OnPhotoTapListener onPhotoTapListener) {
        this.mAttacher.setOnPhotoTapListener(onPhotoTapListener);
    }

    public void setOnViewTapListener(OnViewTapListener onViewTapListener) {
        this.mAttacher.setOnViewTapListener(onViewTapListener);
    }

    public void setScaleType(ScaleType scaleType) {
        if (this.mAttacher != null) {
            this.mAttacher.setScaleType(scaleType);
        } else {
            this.mPendingScaleType = scaleType;
        }
    }

    public void setZoomable(boolean z) {
        this.mAttacher.setZoomable(z);
    }

    public void zoomTo(float f, float f2, float f3) {
        this.mAttacher.zoomTo(f, f2, f3);
    }
}
