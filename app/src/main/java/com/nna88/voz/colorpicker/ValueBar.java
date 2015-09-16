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
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.internal.widget.RtlSpacingHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import com.nna88.voz.PhotoView.PhotoViewAttacher;
import com.nna88.voz.main.R;
import org.apache.http.conn.routing.HttpRouteDirector;

public class ValueBar extends View {
    private static final String STATE_COLOR = "color";
    private static final String STATE_PARENT = "parent";
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
    private float mPosToSatFactor;
    private int mPreferredBarLength;
    private float mSatToPosFactor;
    private Shader shader;

    public ValueBar(Context context) {
        super(context);
        this.mBarRect = new RectF();
        this.mHSVColor = new float[3];
        this.mPicker = null;
        init(null, 0);
    }

    public ValueBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mBarRect = new RectF();
        this.mHSVColor = new float[3];
        this.mPicker = null;
        init(attributeSet, 0);
    }

    public ValueBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mBarRect = new RectF();
        this.mHSVColor = new float[3];
        this.mPicker = null;
        init(attributeSet, i);
    }

    private void calculateColor(int i) {
        int i2 = i - this.mBarPointerHaloRadius;
        if (i2 < 0) {
            i2 = 0;
        } else if (i2 > this.mBarLength) {
            i2 = this.mBarLength;
        }
        float[] fArr = new float[3];
        fArr[0] = this.mHSVColor[0];
        fArr[1] = this.mHSVColor[1];
        fArr[2] = PhotoViewAttacher.DEFAULT_MIN_SCALE - (((float) i2) * this.mPosToSatFactor);
        this.mColor = Color.HSVToColor(fArr);
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
        this.mBarPointerPosition = this.mBarPointerHaloRadius;
        this.mBarPointerHaloPaint = new Paint(1);
        this.mBarPointerHaloPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mBarPointerHaloPaint.setAlpha(80);
        this.mBarPointerPaint = new Paint(1);
        this.mBarPointerPaint.setColor(-8257792);
        this.mPosToSatFactor = PhotoViewAttacher.DEFAULT_MIN_SCALE / ((float) this.mBarLength);
        this.mSatToPosFactor = ((float) this.mBarLength) / PhotoViewAttacher.DEFAULT_MIN_SCALE;
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
        setValue(bundle.getFloat(STATE_VALUE));
    }

    protected Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_PARENT, onSaveInstanceState);
        bundle.putFloatArray(STATE_COLOR, this.mHSVColor);
        float[] fArr = new float[3];
        Color.colorToHSV(this.mColor, fArr);
        bundle.putFloat(STATE_VALUE, fArr[2]);
        return bundle;
    }

    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mBarLength = i - (this.mBarPointerHaloRadius * 2);
        this.mBarRect.set((float) this.mBarPointerHaloRadius, (float) (this.mBarPointerHaloRadius - (this.mBarThickness / 2)), (float) (this.mBarLength + this.mBarPointerHaloRadius), (float) (this.mBarPointerHaloRadius + (this.mBarThickness / 2)));
        if (isInEditMode()) {
            this.shader = new LinearGradient((float) this.mBarPointerHaloRadius, 0.0f, (float) (this.mBarLength + this.mBarPointerHaloRadius), (float) this.mBarThickness, new int[]{-8257792, ViewCompat.MEASURED_STATE_MASK}, null, TileMode.CLAMP);
            Color.colorToHSV(-8257792, this.mHSVColor);
        } else {
            float f = (float) this.mBarPointerHaloRadius;
            float f2 = (float) (this.mBarLength + this.mBarPointerHaloRadius);
            float f3 = (float) this.mBarThickness;
            int HSVToColor = Color.HSVToColor(MotionEventCompat.ACTION_MASK, this.mHSVColor);
            int[] iArr = new int[]{HSVToColor, ViewCompat.MEASURED_STATE_MASK};
            this.shader = new LinearGradient(f, 0.0f, f2, f3, iArr, null, TileMode.CLAMP);
        }
        this.mBarPaint.setShader(this.shader);
        this.mPosToSatFactor = PhotoViewAttacher.DEFAULT_MIN_SCALE / ((float) this.mBarLength);
        this.mSatToPosFactor = ((float) this.mBarLength) / PhotoViewAttacher.DEFAULT_MIN_SCALE;
        float[] fArr = new float[3];
        Color.colorToHSV(this.mColor, fArr);
        if (isInEditMode()) {
            this.mBarPointerPosition = this.mBarPointerHaloRadius;
            return;
        }
        this.mBarPointerPosition = Math.round((((float) this.mBarLength) - (fArr[2] * this.mSatToPosFactor)) + ((float) this.mBarPointerHaloRadius));
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
                        this.mColor = Color.HSVToColor(this.mHSVColor);
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
        int[] iArr = new int[]{i, ViewCompat.MEASURED_STATE_MASK};
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

    public void setValue(float f) {
        this.mBarPointerPosition = Math.round((((float) this.mBarLength) - (this.mSatToPosFactor * f)) + ((float) this.mBarPointerHaloRadius));
        calculateColor(this.mBarPointerPosition);
        this.mBarPointerPaint.setColor(this.mColor);
        if (this.mPicker != null) {
            this.mPicker.setNewCenterColor(this.mColor);
            this.mPicker.changeOpacityBarColor(this.mColor);
        }
        invalidate();
    }
}
