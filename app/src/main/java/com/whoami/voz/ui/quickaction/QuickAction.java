package com.whoami.voz.ui.quickaction;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.whoami.voz.R;
import com.whoami.voz.ui.main.Global;

import java.util.ArrayList;
import java.util.List;

public class QuickAction extends PopupWindows implements android.widget.PopupWindow.OnDismissListener {
    public static final int ANIM_AUTO = 4;
    public static final int ANIM_GROW_FROM_CENTER = 3;
    public static final int ANIM_GROW_FROM_LEFT = 1;
    public static final int ANIM_GROW_FROM_RIGHT = 2;
    public static final int WOOD_TEXT_TITLE = -7829368;
    private LayoutInflater inflater;
    private List<ActionItem> mActionItemList;
    private int mAnimStyle;
    private boolean mAnimateTrack;
    private ImageView mArrowDown;
    private ImageView mArrowUp;
    private int mChildPos;
    private boolean mDidAction;
    private OnDismissListener mDismissListener;
    private OnActionItemClickListener mItemClickListener;
    private ViewGroup mTrack;
    private Animation mTrackAnim;
    private View mview;

    public interface OnActionItemClickListener {
        void onItemClick(QuickAction quickAction, int i, int i2);
    }

    class AnonymousClass2 implements OnClickListener {
        final /* synthetic */ int val$actionId;
        final /* synthetic */ int val$pos;

        AnonymousClass2(int i, int i2) {
            this.val$pos = i;
            this.val$actionId = i2;
        }

        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(QuickAction.this, this.val$pos, this.val$actionId);
            }
            if (!getActionItem(this.val$pos).isSticky()) {
                mDidAction = true;
                view.post(new Runnable() {
                    public void run() {
                        dismiss();
                    }
                });
            }
        }
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    public QuickAction(Context context) {
        super(context);
        mActionItemList = new ArrayList();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTrackAnim = AnimationUtils.loadAnimation(context, R.anim.rail);
        mTrackAnim.setInterpolator(new Interpolator() {
            public float getInterpolation(float f) {
                float f2 = (1.55f * f) - 1.1f;
                return 1.2f - (f2 * f2);
            }
        });
        setRootViewId(R.layout.quickaction);
        mAnimStyle = ANIM_AUTO;
        mAnimateTrack = true;
        mChildPos = 0;
    }

    private void setAnimationStyle(int i, int i2, boolean z) {
        int i3 = R.style.Animations_PopUpMenu_Center;
        int i4 = R.style.Animations_PopDownMenu_Left;
        int measuredWidth = i2 - (mArrowUp.getMeasuredWidth() / ANIM_GROW_FROM_RIGHT);
        PopupWindow popupWindow;
        switch (mAnimStyle) {
            case ANIM_GROW_FROM_LEFT /*1*/:
                popupWindow = mWindow;
                if (z) {
                    i4 = R.style.Animations_PopUpMenu_Left;
                }
                popupWindow.setAnimationStyle(i4);
            case ANIM_GROW_FROM_RIGHT /*2*/:
                mWindow.setAnimationStyle(z ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);
            case ANIM_GROW_FROM_CENTER /*3*/:
                mWindow.setAnimationStyle(z ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
            case ANIM_AUTO /*4*/:
                if (measuredWidth <= i / ANIM_AUTO) {
                    popupWindow = mWindow;
                    if (z) {
                        i4 = R.style.Animations_PopUpMenu_Left;
                    }
                    popupWindow.setAnimationStyle(i4);
                } else if (measuredWidth <= i / ANIM_AUTO || measuredWidth >= (i / ANIM_AUTO) * ANIM_GROW_FROM_CENTER) {
                    popupWindow = mWindow;
                    if (z) {
                        popupWindow.setAnimationStyle(R.style.Animations_PopDownMenu_Right);
                    } else {
                        popupWindow.setAnimationStyle(R.style.Animations_PopDownMenu_Right);
                    }
                } else {
                    popupWindow = mWindow;
                    if (!z) {
                        i3 = R.style.Animations_PopDownMenu_Center;
                    }
                    popupWindow.setAnimationStyle(i3);
                }
            default:
        }
    }

    private void showArrow(int i, int i2) {
        View view = i == R.id.arrow_up ? mArrowUp : mArrowDown;
        View view2 = i == R.id.arrow_up ? mArrowDown : mArrowUp;
        int measuredWidth = mArrowUp.getMeasuredWidth();
        view.setVisibility(View.VISIBLE);
        ((MarginLayoutParams) view.getLayoutParams()).leftMargin = i2 - (measuredWidth / ANIM_GROW_FROM_RIGHT);
        view2.setVisibility(View.INVISIBLE);
    }

    public void addActionItem(ActionItem actionItem) {
        mActionItemList.add(actionItem);
        CharSequence title = actionItem.getTitle();
        Drawable icon = actionItem.getIcon();
        View inflate = inflater.inflate(R.layout.action_item, null);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.icon);
        TextView textView = (TextView) inflate.findViewById(R.id.title);
        Global.setBackgroundQuickAction(inflate);
        Global.setTextContain(textView);
        if (icon != null) {
            icon.setBounds(0, 0, 10, 20);
            imageView.setImageDrawable(icon);
        } else {
            imageView.setVisibility(View.GONE);
        }
        if (title != null) {
            textView.setText(title);
        } else {
            textView.setVisibility(View.GONE);
        }
        inflate.setOnClickListener(new AnonymousClass2(mChildPos, actionItem.getActionId()));
        inflate.setFocusable(true);
        inflate.setClickable(true);
        mTrack.addView(inflate, mChildPos + ANIM_GROW_FROM_LEFT);
        mChildPos += ANIM_GROW_FROM_LEFT;
    }

    public ActionItem getActionItem(int i) {
        return (ActionItem) mActionItemList.get(i);
    }

    public void mAnimateTrack(boolean z) {
        mAnimateTrack = z;
    }

    public void onDismiss() {
        Log.d("nna", "dismiss");
        if (!mDidAction && mDismissListener != null) {
            mDismissListener.onDismiss();
        }
    }

    public void setAnimStyle(int i) {
        mAnimStyle = i;
    }

    public void setOnActionItemClickListener(OnActionItemClickListener onActionItemClickListener) {
        mItemClickListener = onActionItemClickListener;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        setOnDismissListener(this);
        mDismissListener = onDismissListener;
    }

    public void setRootViewId(int i) {
        mRootView = (ViewGroup) inflater.inflate(i, null);
        mTrack = (ViewGroup) mRootView.findViewById(R.id.tracks);
        mArrowDown = (ImageView) mRootView.findViewById(R.id.arrow_down);
        mArrowUp = (ImageView) mRootView.findViewById(R.id.arrow_up);
        FrameLayout frameLayout = (FrameLayout) mRootView.findViewById(R.id.footer);
        ((FrameLayout) mRootView.findViewById(R.id.header2)).setVisibility(View.INVISIBLE);
        frameLayout.setVisibility(View.INVISIBLE);
        mRootView.setLayoutParams(new LayoutParams(-2, -2));
        setContentView(mRootView);
    }

    public void show(View view) {
        boolean z;
        preShow();
        mview = view;
        int[] iArr = new int[ANIM_GROW_FROM_RIGHT];
        mDidAction = false;
        view.getLocationOnScreen(iArr);
        Rect rect = new Rect(iArr[0], iArr[ANIM_GROW_FROM_LEFT], iArr[0] + view.getWidth(), iArr[ANIM_GROW_FROM_LEFT] + view.getHeight());
        mRootView.measure(-2, -2);
        int measuredWidth = mRootView.getMeasuredWidth();
        int measuredHeight = mRootView.getMeasuredHeight();
        int width = mWindowManager.getDefaultDisplay().getWidth();
        int i = (width - measuredWidth) / ANIM_GROW_FROM_RIGHT;
        measuredWidth = rect.top - measuredHeight;
        if (measuredHeight > view.getTop()) {
            z = false;
            measuredHeight = rect.bottom;
        } else {
            measuredHeight = measuredWidth;
            z = true;
        }
        showArrow(z ? R.id.arrow_down : R.id.arrow_up, mTrack.getMeasuredWidth() / ANIM_GROW_FROM_RIGHT);
        setAnimationStyle(width, rect.centerX(), z);
        mWindow.showAtLocation(view, 0, i, measuredHeight);
        if (mAnimateTrack) {
            mTrack.startAnimation(mTrackAnim);
        }
    }
}
