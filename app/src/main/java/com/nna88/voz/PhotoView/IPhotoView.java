package com.nna88.voz.PhotoView;

import android.graphics.RectF;
import android.view.View.OnLongClickListener;
import android.widget.ImageView.ScaleType;
import com.nna88.voz.PhotoView.PhotoViewAttacher.OnMatrixChangedListener;
import com.nna88.voz.PhotoView.PhotoViewAttacher.OnPhotoTapListener;
import com.nna88.voz.PhotoView.PhotoViewAttacher.OnViewTapListener;

public interface IPhotoView {
    boolean canZoom();

    RectF getDisplayRect();

    float getMaxScale();

    float getMidScale();

    float getMinScale();

    float getScale();

    ScaleType getScaleType();

    void setAllowParentInterceptOnEdge(boolean z);

    void setMaxScale(float f);

    void setMidScale(float f);

    void setMinScale(float f);

    void setOnLongClickListener(OnLongClickListener onLongClickListener);

    void setOnMatrixChangeListener(OnMatrixChangedListener onMatrixChangedListener);

    void setOnPhotoTapListener(OnPhotoTapListener onPhotoTapListener);

    void setOnViewTapListener(OnViewTapListener onViewTapListener);

    void setScaleType(ScaleType scaleType);

    void setZoomable(boolean z);

    void zoomTo(float f, float f2, float f3);
}
