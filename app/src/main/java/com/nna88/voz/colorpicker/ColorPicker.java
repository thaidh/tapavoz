package com.nna88.voz.colorpicker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ViewCompat;
import android.support.v7.internal.widget.RtlSpacingHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.nna88.voz.PhotoView.PhotoViewAttacher;
import com.nna88.voz.main.R;
import org.apache.http.conn.routing.HttpRouteDirector;

public class ColorPicker extends View {
    private static final int[] COLORS;
    private static final String STATE_ANGLE = "angle";
    private static final String STATE_OLD_COLOR = "color";
    private static final String STATE_PARENT = "parent";
    private float mAngle;
    private Paint mCenterHaloPaint;
    private int mCenterNewColor;
    private Paint mCenterNewPaint;
    private int mCenterOldColor;
    private Paint mCenterOldPaint;
    private RectF mCenterRectangle;
    private int mColor;
    private int mColorCenterHaloRadius;
    private int mColorCenterRadius;
    private int mColorPointerHaloRadius;
    private int mColorPointerRadius;
    private Paint mColorWheelPaint;
    private int mColorWheelRadius;
    private RectF mColorWheelRectangle;
    private int mColorWheelThickness;
    private float[] mHSV;
    private OpacityBar mOpacityBar;
    private Paint mPointerColor;
    private Paint mPointerHaloPaint;
    private int mPreferredColorCenterHaloRadius;
    private int mPreferredColorCenterRadius;
    private int mPreferredColorWheelRadius;
    private SVBar mSVbar;
    private SaturationBar mSaturationBar;
    private float mTranslationOffset;
    private boolean mUserIsMovingPointer;
    private ValueBar mValueBar;
    private OnColorChangedListener onColorChangedListener;

    public interface OnColorChangedListener {
        void onColorChanged(int i);
    }

    static {
        COLORS = new int[]{SupportMenu.CATEGORY_MASK, -65281, -16776961, -16711681, -16711936, -256, SupportMenu.CATEGORY_MASK};
    }

    public ColorPicker(Context context) {
        super(context);
        this.mColorWheelRectangle = new RectF();
        this.mCenterRectangle = new RectF();
        this.mUserIsMovingPointer = false;
        this.mHSV = new float[3];
        this.mSVbar = null;
        this.mOpacityBar = null;
        this.mSaturationBar = null;
        this.mValueBar = null;
        init(null, 0);
    }

    public ColorPicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mColorWheelRectangle = new RectF();
        this.mCenterRectangle = new RectF();
        this.mUserIsMovingPointer = false;
        this.mHSV = new float[3];
        this.mSVbar = null;
        this.mOpacityBar = null;
        this.mSaturationBar = null;
        this.mValueBar = null;
        init(attributeSet, 0);
    }

    public ColorPicker(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mColorWheelRectangle = new RectF();
        this.mCenterRectangle = new RectF();
        this.mUserIsMovingPointer = false;
        this.mHSV = new float[3];
        this.mSVbar = null;
        this.mOpacityBar = null;
        this.mSaturationBar = null;
        this.mValueBar = null;
        init(attributeSet, i);
    }

    private int ave(int i, int i2, float f) {
        return Math.round(((float) (i2 - i)) * f) + i;
    }

    private int calculateColor(float f) {
        float f2 = (float) (((double) f) / 6.283185307179586d);
        if (f2 < 0.0f) {
            f2 += PhotoViewAttacher.DEFAULT_MIN_SCALE;
        }
        if (f2 <= 0.0f) {
            this.mColor = COLORS[0];
            return COLORS[0];
        } else if (f2 >= PhotoViewAttacher.DEFAULT_MIN_SCALE) {
            this.mColor = COLORS[COLORS.length - 1];
            return COLORS[COLORS.length - 1];
        } else {
            f2 *= (float) (COLORS.length - 1);
            int i = (int) f2;
            f2 -= (float) i;
            int i2 = COLORS[i];
            i = COLORS[i + 1];
            int ave = ave(Color.alpha(i2), Color.alpha(i), f2);
            int ave2 = ave(Color.red(i2), Color.red(i), f2);
            int ave3 = ave(Color.green(i2), Color.green(i), f2);
            int ave4 = ave(Color.blue(i2), Color.blue(i), f2);
            this.mColor = Color.argb(ave, ave2, ave3, ave4);
            return Color.argb(ave, ave2, ave3, ave4);
        }
    }

    private float[] calculatePointerPosition(float f) {
        return new float[]{(float) (((double) this.mColorWheelRadius) * Math.cos((double) f)), (float) (((double) this.mColorWheelRadius) * Math.sin((double) f))};
    }

    private float colorToAngle(int i) {
        float[] fArr = new float[3];
        Color.colorToHSV(i, fArr);
        return (float) Math.toRadians((double) (-fArr[0]));
    }

    private void init(AttributeSet attributeSet, int i) {
//        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.ColorPicker, i, 0);
//        Resources resources = getContext().getResources();
//        this.mColorWheelThickness = obtainStyledAttributes.getDimensionPixelSize(1, resources.getDimensionPixelSize(R.dimen.color_wheel_thickness));
//        this.mColorWheelRadius = obtainStyledAttributes.getDimensionPixelSize(0, resources.getDimensionPixelSize(R.dimen.color_wheel_radius));
//        this.mPreferredColorWheelRadius = this.mColorWheelRadius;
//        this.mColorCenterRadius = obtainStyledAttributes.getDimensionPixelSize(2, resources.getDimensionPixelSize(R.dimen.color_center_radius));
//        this.mPreferredColorCenterRadius = this.mColorCenterRadius;
//        this.mColorCenterHaloRadius = obtainStyledAttributes.getDimensionPixelSize(3, resources.getDimensionPixelSize(R.dimen.color_center_halo_radius));
//        this.mPreferredColorCenterHaloRadius = this.mColorCenterHaloRadius;
//        this.mColorPointerRadius = obtainStyledAttributes.getDimensionPixelSize(4, resources.getDimensionPixelSize(R.dimen.color_pointer_radius));
//        this.mColorPointerHaloRadius = obtainStyledAttributes.getDimensionPixelSize(5, resources.getDimensionPixelSize(R.dimen.color_pointer_halo_radius));
//        obtainStyledAttributes.recycle();
        this.mAngle = -1.5707964f;
        Shader sweepGradient = new SweepGradient(0.0f, 0.0f, COLORS, null);
        this.mColorWheelPaint = new Paint(1);
        this.mColorWheelPaint.setShader(sweepGradient);
        this.mColorWheelPaint.setStyle(Style.STROKE);
        this.mColorWheelPaint.setStrokeWidth((float) this.mColorWheelThickness);
        this.mPointerHaloPaint = new Paint(1);
        this.mPointerHaloPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mPointerHaloPaint.setAlpha(80);
        this.mPointerColor = new Paint(1);
        this.mPointerColor.setColor(calculateColor(this.mAngle));
        this.mCenterNewPaint = new Paint(1);
        this.mCenterNewPaint.setColor(calculateColor(this.mAngle));
        this.mCenterNewPaint.setStyle(Style.FILL);
        this.mCenterOldPaint = new Paint(1);
        this.mCenterOldPaint.setColor(calculateColor(this.mAngle));
        this.mCenterOldPaint.setStyle(Style.FILL);
        this.mCenterHaloPaint = new Paint(1);
        this.mCenterHaloPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mCenterHaloPaint.setAlpha(0);
    }

    public void addOpacityBar(OpacityBar opacityBar) {
        this.mOpacityBar = opacityBar;
        this.mOpacityBar.setColorPicker(this);
        this.mOpacityBar.setColor(this.mColor);
    }

    public void addSVBar(SVBar sVBar) {
        this.mSVbar = sVBar;
        this.mSVbar.setColorPicker(this);
        this.mSVbar.setColor(this.mColor);
    }

    public void addSaturationBar(SaturationBar saturationBar) {
        this.mSaturationBar = saturationBar;
        this.mSaturationBar.setColorPicker(this);
        this.mSaturationBar.setColor(this.mColor);
    }

    public void addValueBar(ValueBar valueBar) {
        this.mValueBar = valueBar;
        this.mValueBar.setColorPicker(this);
        this.mValueBar.setColor(this.mColor);
    }

    public void changeOpacityBarColor(int i) {
        if (this.mOpacityBar != null) {
            this.mOpacityBar.setColor(i);
        }
    }

    public void changeSaturationBarColor(int i) {
        if (this.mSaturationBar != null) {
            this.mSaturationBar.setColor(i);
        }
    }

    public void changeValueBarColor(int i) {
        if (this.mValueBar != null) {
            this.mValueBar.setColor(i);
        }
    }

    public int getColor() {
        return this.mCenterNewColor;
    }

    public int getOldCenterColor() {
        return this.mCenterOldColor;
    }

    public OnColorChangedListener getOnColorChangedListener() {
        return this.onColorChangedListener;
    }

    protected void onDraw(Canvas canvas) {
        canvas.translate(this.mTranslationOffset, this.mTranslationOffset);
        canvas.drawOval(this.mColorWheelRectangle, this.mColorWheelPaint);
        float[] calculatePointerPosition = calculatePointerPosition(this.mAngle);
        canvas.drawCircle(calculatePointerPosition[0], calculatePointerPosition[1], (float) this.mColorPointerHaloRadius, this.mPointerHaloPaint);
        canvas.drawCircle(calculatePointerPosition[0], calculatePointerPosition[1], (float) this.mColorPointerRadius, this.mPointerColor);
        canvas.drawCircle(0.0f, 0.0f, (float) this.mColorCenterHaloRadius, this.mCenterHaloPaint);
        canvas.drawArc(this.mCenterRectangle, 90.0f, BitmapDescriptorFactory.HUE_CYAN, true, this.mCenterOldPaint);
        canvas.drawArc(this.mCenterRectangle, BitmapDescriptorFactory.HUE_VIOLET, BitmapDescriptorFactory.HUE_CYAN, true, this.mCenterNewPaint);
    }

    protected void onMeasure(int i, int i2) {
        int i3 = (this.mPreferredColorWheelRadius + this.mColorPointerHaloRadius) * 2;
        int mode = MeasureSpec.getMode(i);
        int size = MeasureSpec.getSize(i);
        int mode2 = MeasureSpec.getMode(i2);
        int size2 = MeasureSpec.getSize(i2);
        if (mode != 1073741824) {
            size = mode == RtlSpacingHelper.UNDEFINED ? Math.min(i3, size) : i3;
        }
        if (mode2 == 1073741824) {
            i3 = size2;
        } else if (mode2 == RtlSpacingHelper.UNDEFINED) {
            i3 = Math.min(i3, size2);
        }
        i3 = Math.min(size, i3);
        setMeasuredDimension(i3, i3);
        this.mTranslationOffset = ((float) i3) * 0.5f;
        this.mColorWheelRadius = ((i3 / 2) - this.mColorWheelThickness) - this.mColorPointerHaloRadius;
        this.mColorWheelRectangle.set((float) (-this.mColorWheelRadius), (float) (-this.mColorWheelRadius), (float) this.mColorWheelRadius, (float) this.mColorWheelRadius);
        this.mColorCenterRadius = (int) (((float) this.mPreferredColorCenterRadius) * (((float) this.mColorWheelRadius) / ((float) this.mPreferredColorWheelRadius)));
        this.mColorCenterHaloRadius = (int) (((float) this.mPreferredColorCenterHaloRadius) * (((float) this.mColorWheelRadius) / ((float) this.mPreferredColorWheelRadius)));
        this.mCenterRectangle.set((float) (-this.mColorCenterRadius), (float) (-this.mColorCenterRadius), (float) this.mColorCenterRadius, (float) this.mColorCenterRadius);
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        Bundle bundle = (Bundle) parcelable;
        super.onRestoreInstanceState(bundle.getParcelable(STATE_PARENT));
        this.mAngle = bundle.getFloat(STATE_ANGLE);
        setOldCenterColor(bundle.getInt(STATE_OLD_COLOR));
        this.mPointerColor.setColor(calculateColor(this.mAngle));
    }

    protected Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_PARENT, onSaveInstanceState);
        bundle.putFloat(STATE_ANGLE, this.mAngle);
        bundle.putInt(STATE_OLD_COLOR, this.mCenterOldColor);
        return bundle;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        getParent().requestDisallowInterceptTouchEvent(true);
        float x = motionEvent.getX() - this.mTranslationOffset;
        float y = motionEvent.getY() - this.mTranslationOffset;
        switch (motionEvent.getAction()) {
            case HttpRouteDirector.COMPLETE /*0*/:
                float[] calculatePointerPosition = calculatePointerPosition(this.mAngle);
                if (x < calculatePointerPosition[0] - ((float) this.mColorPointerHaloRadius) || x > calculatePointerPosition[0] + ((float) this.mColorPointerHaloRadius) || y < calculatePointerPosition[1] - ((float) this.mColorPointerHaloRadius) || y > calculatePointerPosition[1] + ((float) this.mColorPointerHaloRadius)) {
                    if (x >= ((float) (-this.mColorCenterRadius)) && x <= ((float) this.mColorCenterRadius) && y >= ((float) (-this.mColorCenterRadius)) && y <= ((float) this.mColorCenterRadius)) {
                        this.mCenterHaloPaint.setAlpha(80);
                        setColor(getOldCenterColor());
                        this.mCenterNewPaint.setColor(getOldCenterColor());
                        invalidate();
                        break;
                    }
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                }
                this.mUserIsMovingPointer = true;
                invalidate();
                break;
            case HttpRouteDirector.CONNECT_TARGET /*1*/:
                this.mUserIsMovingPointer = false;
                this.mCenterHaloPaint.setAlpha(0);
                invalidate();
                break;
            case HttpRouteDirector.CONNECT_PROXY /*2*/:
                if (this.mUserIsMovingPointer) {
                    this.mAngle = (float) Math.atan2((double) y, (double) x);
                    this.mPointerColor.setColor(calculateColor(this.mAngle));
                    int calculateColor = calculateColor(this.mAngle);
                    this.mCenterNewColor = calculateColor;
                    setNewCenterColor(calculateColor);
                    if (this.mOpacityBar != null) {
                        this.mOpacityBar.setColor(this.mColor);
                    }
                    if (this.mValueBar != null) {
                        this.mValueBar.setColor(this.mColor);
                    }
                    if (this.mSaturationBar != null) {
                        this.mSaturationBar.setColor(this.mColor);
                    }
                    if (this.mSVbar != null) {
                        this.mSVbar.setColor(this.mColor);
                    }
                    invalidate();
                    break;
                }
                getParent().requestDisallowInterceptTouchEvent(false);
                return false;
        }
        return true;
    }

    public void setColor(int i) {
        this.mAngle = colorToAngle(i);
        this.mPointerColor.setColor(calculateColor(this.mAngle));
        if (this.mOpacityBar != null) {
            this.mOpacityBar.setColor(this.mColor);
            this.mOpacityBar.setOpacity(Color.alpha(i));
        }
        if (this.mSVbar != null) {
            Color.colorToHSV(i, this.mHSV);
            this.mSVbar.setColor(this.mColor);
            if (this.mHSV[1] < this.mHSV[2]) {
                this.mSVbar.setSaturation(this.mHSV[1]);
            } else {
                this.mSVbar.setValue(this.mHSV[2]);
            }
        }
        if (this.mSaturationBar != null) {
            Color.colorToHSV(i, this.mHSV);
            this.mSaturationBar.setColor(this.mColor);
            this.mSaturationBar.setSaturation(this.mHSV[1]);
        }
        if (this.mValueBar != null && this.mSaturationBar == null) {
            Color.colorToHSV(i, this.mHSV);
            this.mValueBar.setColor(this.mColor);
            this.mValueBar.setValue(this.mHSV[2]);
        } else if (this.mValueBar != null) {
            Color.colorToHSV(i, this.mHSV);
            this.mValueBar.setValue(this.mHSV[2]);
        }
        invalidate();
    }

    public void setNewCenterColor(int i) {
        this.mCenterNewColor = i;
        this.mCenterNewPaint.setColor(i);
        if (this.mCenterOldColor == 0) {
            this.mCenterOldColor = i;
            this.mCenterOldPaint.setColor(i);
        }
        if (this.onColorChangedListener != null) {
            this.onColorChangedListener.onColorChanged(i);
        }
        invalidate();
    }

    public void setOldCenterColor(int i) {
        this.mCenterOldColor = i;
        this.mCenterOldPaint.setColor(i);
        invalidate();
    }

    public void setOnColorChangedListener(OnColorChangedListener onColorChangedListener) {
        this.onColorChangedListener = onColorChangedListener;
    }
}
