package com.nna88.voz.colorpicker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewCompat;
import android.support.v7.internal.widget.RtlSpacingHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import com.nna88.voz.PhotoView.PhotoViewAttacher;
import com.nna88.voz.main.R;
import org.apache.http.conn.routing.HttpRouteDirector;

public class SVBar extends View {
    private static final String STATE_COLOR = "color";
    private static final String STATE_PARENT = "parent";
    private static final String STATE_SATURATION = "saturation";
    private static final String STATE_VALUE = "value";
    private int mBarLength;
    private Paint mBarPaint;
    private Paint mBarPointerHaloPaint;
    private int mBarPointerHaloRadius;
    private Paint mBarPointerPaint;
    private int mBarPointerPosition;
    private int mBarPointerRadius;
    private RectF mBarRect;
    private int mBarThickness;
    private int mColor;
    private float[] mHSVColor;
    private boolean mIsMovingPointer;
    private ColorPicker mPicker;
    private float mPosToSVFactor;
    private int mPreferredBarLength;
    private float mSVToPosFactor;
    private Shader shader;

    public SVBar(Context context) {
        super(context);
        this.mBarRect = new RectF();
        this.mHSVColor = new float[3];
        this.mPicker = null;
        init(null, 0);
    }

    public SVBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mBarRect = new RectF();
        this.mHSVColor = new float[3];
        this.mPicker = null;
        init(attributeSet, 0);
    }

    public SVBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mBarRect = new RectF();
        this.mHSVColor = new float[3];
        this.mPicker = null;
        init(attributeSet, i);
    }

    private void calculateColor(int i) {
        if (i > this.mBarPointerHaloRadius + (this.mBarLength / 2) && i < this.mBarPointerHaloRadius + this.mBarLength) {
            this.mColor = Color.HSVToColor(new float[]{this.mHSVColor[0], PhotoViewAttacher.DEFAULT_MIN_SCALE, PhotoViewAttacher.DEFAULT_MIN_SCALE - (this.mPosToSVFactor * ((float) (i - (this.mBarPointerHaloRadius + (this.mBarLength / 2)))))});
        } else if (i > this.mBarPointerHaloRadius && i < this.mBarPointerHaloRadius + this.mBarLength) {
            this.mColor = Color.HSVToColor(new float[]{this.mHSVColor[0], this.mPosToSVFactor * ((float) (i - this.mBarPointerHaloRadius)), PhotoViewAttacher.DEFAULT_MIN_SCALE});
        } else if (i == this.mBarPointerHaloRadius) {
            this.mColor = -1;
        } else if (i == this.mBarPointerHaloRadius + this.mBarLength) {
            this.mColor = ViewCompat.MEASURED_STATE_MASK;
        }
    }

    private void init(AttributeSet attributeSet, int i) {
//        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.ColorBars, i, 0);
//        Resources resources = getContext().getResources();
//        this.mBarThickness = obtainStyledAttributes.getDimensionPixelSize(0, resources.getDimensionPixelSize(R.dimen.bar_thickness));
//        this.mBarLength = obtainStyledAttributes.getDimensionPixelSize(1, resources.getDimensionPixelSize(R.dimen.bar_length));
//        this.mPreferredBarLength = this.mBarLength;
//        this.mBarPointerRadius = obtainStyledAttributes.getDimensionPixelSize(2, resources.getDimensionPixelSize(R.dimen.bar_pointer_radius));
//        this.mBarPointerHaloRadius = obtainStyledAttributes.getDimensionPixelSize(3, resources.getDimensionPixelSize(R.dimen.bar_pointer_halo_radius));
//        obtainStyledAttributes.recycle();
        this.mBarPaint = new Paint(1);
        this.mBarPaint.setShader(this.shader);
        this.mBarPointerPosition = (this.mBarLength / 2) + this.mBarPointerHaloRadius;
        this.mBarPointerHaloPaint = new Paint(1);
        this.mBarPointerHaloPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mBarPointerHaloPaint.setAlpha(80);
        this.mBarPointerPaint = new Paint(1);
        this.mBarPointerPaint.setColor(-8257792);
        this.mPosToSVFactor = PhotoViewAttacher.DEFAULT_MIN_SCALE / (((float) this.mBarLength) / 2.0f);
        this.mSVToPosFactor = (((float) this.mBarLength) / 2.0f) / PhotoViewAttacher.DEFAULT_MIN_SCALE;
    }

    public int getColor() {
        return this.mColor;
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawRect(this.mBarRect, this.mBarPaint);
        canvas.drawCircle((float) this.mBarPointerPosition, (float) this.mBarPointerHaloRadius, (float) this.mBarPointerHaloRadius, this.mBarPointerHaloPaint);
        canvas.drawCircle((float) this.mBarPointerPosition, (float) this.mBarPointerHaloRadius, (float) this.mBarPointerRadius, this.mBarPointerPaint);
    }

    protected void onMeasure(int i, int i2) {
        int i3 = this.mPreferredBarLength + (this.mBarPointerHaloRadius * 2);
        int mode = MeasureSpec.getMode(i);
        int size = MeasureSpec.getSize(i);
        if (mode == 1073741824) {
            i3 = size;
        } else if (mode == RtlSpacingHelper.UNDEFINED) {
            i3 = Math.min(i3, size);
        }
        this.mBarLength = i3 - (this.mBarPointerHaloRadius * 2);
        setMeasuredDimension(this.mBarLength + (this.mBarPointerHaloRadius * 2), this.mBarPointerHaloRadius * 2);
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        Bundle bundle = (Bundle) parcelable;
        super.onRestoreInstanceState(bundle.getParcelable(STATE_PARENT));
        setColor(Color.HSVToColor(bundle.getFloatArray(STATE_COLOR)));
        if (bundle.containsKey(STATE_SATURATION)) {
            setSaturation(bundle.getFloat(STATE_SATURATION));
        } else {
            setValue(bundle.getFloat(STATE_VALUE));
        }
    }

    protected Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_PARENT, onSaveInstanceState);
        bundle.putFloatArray(STATE_COLOR, this.mHSVColor);
        float[] fArr = new float[3];
        Color.colorToHSV(this.mColor, fArr);
        if (fArr[1] < fArr[2]) {
            bundle.putFloat(STATE_SATURATION, fArr[1]);
        } else {
            bundle.putFloat(STATE_VALUE, fArr[2]);
        }
        return bundle;
    }

    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mBarLength = i - (this.mBarPointerHaloRadius * 2);
        this.mBarRect.set((float) this.mBarPointerHaloRadius, (float) (this.mBarPointerHaloRadius - (this.mBarThickness / 2)), (float) (this.mBarLength + this.mBarPointerHaloRadius), (float) (this.mBarPointerHaloRadius + (this.mBarThickness / 2)));
        if (isInEditMode()) {
            this.shader = new LinearGradient((float) this.mBarPointerHaloRadius, 0.0f, (float) (this.mBarLength + this.mBarPointerHaloRadius), (float) this.mBarThickness, new int[]{-1, -8257792, ViewCompat.MEASURED_STATE_MASK}, null, TileMode.CLAMP);
            Color.colorToHSV(-8257792, this.mHSVColor);
        } else {
            float f = (float) this.mBarPointerHaloRadius;
            float f2 = (float) (this.mBarLength + this.mBarPointerHaloRadius);
            float f3 = (float) this.mBarThickness;
            int HSVToColor = Color.HSVToColor(this.mHSVColor);
            int[] iArr = new int[]{-1, HSVToColor, ViewCompat.MEASURED_STATE_MASK};
            this.shader = new LinearGradient(f, 0.0f, f2, f3, iArr, null, TileMode.CLAMP);
        }
        this.mBarPaint.setShader(this.shader);
        this.mPosToSVFactor = PhotoViewAttacher.DEFAULT_MIN_SCALE / (((float) this.mBarLength) / 2.0f);
        this.mSVToPosFactor = (((float) this.mBarLength) / 2.0f) / PhotoViewAttacher.DEFAULT_MIN_SCALE;
        float[] fArr = new float[3];
        Color.colorToHSV(this.mColor, fArr);
        if (fArr[1] < fArr[2]) {
            this.mBarPointerPosition = Math.round((fArr[1] * this.mSVToPosFactor) + ((float) this.mBarPointerHaloRadius));
        } else {
            this.mBarPointerPosition = Math.round((((PhotoViewAttacher.DEFAULT_MIN_SCALE - fArr[2]) * this.mSVToPosFactor) + ((float) this.mBarPointerHaloRadius)) + ((float) (this.mBarLength / 2)));
        }
        if (isInEditMode()) {
            this.mBarPointerPosition = (this.mBarLength / 2) + this.mBarPointerHaloRadius;
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        getParent().requestDisallowInterceptTouchEvent(true);
        float x = motionEvent.getX();
        switch (motionEvent.getAction()) {
            case HttpRouteDirector.COMPLETE /*0*/:
                this.mIsMovingPointer = true;
                if (x >= ((float) this.mBarPointerHaloRadius) && x <= ((float) (this.mBarPointerHaloRadius + this.mBarLength))) {
                    this.mBarPointerPosition = Math.round(x);
                    calculateColor(Math.round(x));
                    this.mBarPointerPaint.setColor(this.mColor);
                    invalidate();
                    break;
                }
            case HttpRouteDirector.CONNECT_TARGET /*1*/:
                this.mIsMovingPointer = false;
                break;
            case HttpRouteDirector.CONNECT_PROXY /*2*/:
                if (this.mIsMovingPointer) {
                    if (x < ((float) this.mBarPointerHaloRadius) || x > ((float) (this.mBarPointerHaloRadius + this.mBarLength))) {
                        if (x >= ((float) this.mBarPointerHaloRadius)) {
                            if (x > ((float) (this.mBarPointerHaloRadius + this.mBarLength))) {
                                this.mBarPointerPosition = this.mBarPointerHaloRadius + this.mBarLength;
                                this.mColor = ViewCompat.MEASURED_STATE_MASK;
                                this.mBarPointerPaint.setColor(this.mColor);
                                if (this.mPicker != null) {
                                    this.mPicker.setNewCenterColor(this.mColor);
                                    this.mPicker.changeOpacityBarColor(this.mColor);
                                }
                                invalidate();
                                break;
                            }
                        }
                        this.mBarPointerPosition = this.mBarPointerHaloRadius;
                        this.mColor = -1;
                        this.mBarPointerPaint.setColor(this.mColor);
                        if (this.mPicker != null) {
                            this.mPicker.setNewCenterColor(this.mColor);
                            this.mPicker.changeOpacityBarColor(this.mColor);
                        }
                        invalidate();
                        break;
                    }
                    this.mBarPointerPosition = Math.round(x);
                    calculateColor(Math.round(x));
                    this.mBarPointerPaint.setColor(this.mColor);
                    if (this.mPicker != null) {
                        this.mPicker.setNewCenterColor(this.mColor);
                        this.mPicker.changeOpacityBarColor(this.mColor);
                    }
                    invalidate();
                    break;
                }
                break;
        }
        return true;
    }

    public void setColor(int i) {
        Color.colorToHSV(i, this.mHSVColor);
        int[] iArr = new int[]{-1, i, ViewCompat.MEASURED_STATE_MASK};
        this.shader = new LinearGradient((float) this.mBarPointerHaloRadius, 0.0f, (float) (this.mBarLength + this.mBarPointerHaloRadius), (float) this.mBarThickness, iArr, null, TileMode.CLAMP);
        this.mBarPaint.setShader(this.shader);
        calculateColor(this.mBarPointerPosition);
        this.mBarPointerPaint.setColor(this.mColor);
        if (this.mPicker != null) {
            this.mPicker.setNewCenterColor(this.mColor);
            this.mPicker.changeOpacityBarColor(this.mColor);
        }
        invalidate();
    }

    public void setColorPicker(ColorPicker colorPicker) {
        this.mPicker = colorPicker;
    }

    public void setSaturation(float f) {
        this.mBarPointerPosition = Math.round((this.mSVToPosFactor * f) + ((float) this.mBarPointerHaloRadius));
        calculateColor(this.mBarPointerPosition);
        this.mBarPointerPaint.setColor(this.mColor);
        if (this.mPicker != null) {
            this.mPicker.setNewCenterColor(this.mColor);
            this.mPicker.changeOpacityBarColor(this.mColor);
        }
        invalidate();
    }

    public void setValue(float f) {
        this.mBarPointerPosition = Math.round(((this.mSVToPosFactor * (PhotoViewAttacher.DEFAULT_MIN_SCALE - f)) + ((float) this.mBarPointerHaloRadius)) + ((float) (this.mBarLength / 2)));
        calculateColor(this.mBarPointerPosition);
        this.mBarPointerPaint.setColor(this.mColor);
        if (this.mPicker != null) {
            this.mPicker.setNewCenterColor(this.mColor);
            this.mPicker.changeOpacityBarColor(this.mColor);
        }
        invalidate();
    }
}
