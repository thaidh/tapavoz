package com.nna88.voz.quickaction;

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
import com.nna88.voz.main.Global;
import com.nna88.voz.main.R;
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

    /* renamed from: com.nna88.voz.quickaction.QuickAction.2 */
    class AnonymousClass2 implements OnClickListener {
        final /* synthetic */ int val$actionId;
        final /* synthetic */ int val$pos;

        AnonymousClass2(int i, int i2) {
            this.val$pos = i;
            this.val$actionId = i2;
        }

        public void onClick(View view) {
            if (QuickAction.this.mItemClickListener != null) {
                QuickAction.this.mItemClickListener.onItemClick(QuickAction.this, this.val$pos, this.val$actionId);
            }
            if (!QuickAction.this.getActionItem(this.val$pos).isSticky()) {
                QuickAction.this.mDidAction = true;
                view.post(new Runnable() {
                    public void run() {
                        QuickAction.this.dismiss();
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
        this.mActionItemList = new ArrayList();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mTrackAnim = AnimationUtils.loadAnimation(context, R.anim.rail);
        this.mTrackAnim.setInterpolator(new Interpolator() {
            public float getInterpolation(float f) {
                float f2 = (1.55f * f) - 1.1f;
                return 1.2f - (f2 * f2);
            }
        });
        setRootViewId(R.layout.quickaction);
        this.mAnimStyle = ANIM_AUTO;
        this.mAnimateTrack = true;
        this.mChildPos = 0;
    }

    private void setAnimationStyle(int i, int i2, boolean z) {
        int i3 = R.style.Animations_PopUpMenu_Center;
        int i4 = R.style.Animations_PopDownMenu_Left;
        int measuredWidth = i2 - (this.mArrowUp.getMeasuredWidth() / ANIM_GROW_FROM_RIGHT);
        PopupWindow popupWindow;
        switch (this.mAnimStyle) {
            case ANIM_GROW_FROM_LEFT /*1*/:
                popupWindow = this.mWindow;
                if (z) {
                    i4 = R.style.Animations_PopUpMenu_Left;
                }
                popupWindow.setAnimationStyle(i4);
            case ANIM_GROW_FROM_RIGHT /*2*/:
                this.mWindow.setAnimationStyle(z ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);
            case ANIM_GROW_FROM_CENTER /*3*/:
                this.mWindow.setAnimationStyle(z ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
            case ANIM_AUTO /*4*/:
                if (measuredWidth <= i / ANIM_AUTO) {
                    popupWindow = this.mWindow;
                    if (z) {
                        i4 = R.style.Animations_PopUpMenu_Left;
                    }
                    popupWindow.setAnimationStyle(i4);
                } else if (measuredWidth <= i / ANIM_AUTO || measuredWidth >= (i / ANIM_AUTO) * ANIM_GROW_FROM_CENTER) {
                    popupWindow = this.mWindow;
                    if (z) {
                        popupWindow.setAnimationStyle(R.style.Animations_PopDownMenu_Right);
                    } else {
                        popupWindow.setAnimationStyle(R.style.Animations_PopDownMenu_Right);
                    }
                } else {
                    popupWindow = this.mWindow;
                    if (!z) {
                        i3 = R.style.Animations_PopDownMenu_Center;
                    }
                    popupWindow.setAnimationStyle(i3);
                }
            default:
        }
    }

    private void showArrow(int i, int i2) {
        View view = i == R.id.arrow_up ? this.mArrowUp : this.mArrowDown;
        View view2 = i == R.id.arrow_up ? this.mArrowDown : this.mArrowUp;
        int measuredWidth = this.mArrowUp.getMeasuredWidth();
        view.setVisibility(View.VISIBLE);
        ((MarginLayoutParams) view.getLayoutParams()).leftMargin = i2 - (measuredWidth / ANIM_GROW_FROM_RIGHT);
        view2.setVisibility(View.INVISIBLE);
    }

    public void addActionItem(ActionItem actionItem) {
        this.mActionItemList.add(actionItem);
        CharSequence title = actionItem.getTitle();
        Drawable icon = actionItem.getIcon();
        View inflate = this.inflater.inflate(R.layout.action_item, null);
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
        inflate.setOnClickListener(new AnonymousClass2(this.mChildPos, actionItem.getActionId()));
        inflate.setFocusable(true);
        inflate.setClickable(true);
        this.mTrack.addView(inflate, this.mChildPos + ANIM_GROW_FROM_LEFT);
        this.mChildPos += ANIM_GROW_FROM_LEFT;
    }

    public ActionItem getActionItem(int i) {
        return (ActionItem) this.mActionItemList.get(i);
    }

    public void mAnimateTrack(boolean z) {
        this.mAnimateTrack = z;
    }

    public void onDismiss() {
        Log.d("nna", "dismiss");
        if (!this.mDidAction && this.mDismissListener != null) {
            this.mDismissListener.onDismiss();
        }
    }

    public void setAnimStyle(int i) {
        this.mAnimStyle = i;
    }

    public void setOnActionItemClickListener(OnActionItemClickListener onActionItemClickListener) {
        this.mItemClickListener = onActionItemClickListener;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        setOnDismissListener(this);
        this.mDismissListener = onDismissListener;
    }

    public void setRootViewId(int i) {
        this.mRootView = (ViewGroup) this.inflater.inflate(i, null);
        this.mTrack = (ViewGroup) this.mRootView.findViewById(R.id.tracks);
        this.mArrowDown = (ImageView) this.mRootView.findViewById(R.id.arrow_down);
        this.mArrowUp = (ImageView) this.mRootView.findViewById(R.id.arrow_up);
        FrameLayout frameLayout = (FrameLayout) this.mRootView.findViewById(R.id.footer);
        ((FrameLayout) this.mRootView.findViewById(R.id.header2)).setVisibility(View.INVISIBLE);
        frameLayout.setVisibility(View.INVISIBLE);
        this.mRootView.setLayoutParams(new LayoutParams(-2, -2));
        setContentView(this.mRootView);
    }

    public void show(View view) {
        boolean z;
        preShow();
        this.mview = view;
        int[] iArr = new int[ANIM_GROW_FROM_RIGHT];
        this.mDidAction = false;
        view.getLocationOnScreen(iArr);
        Rect rect = new Rect(iArr[0], iArr[ANIM_GROW_FROM_LEFT], iArr[0] + view.getWidth(), iArr[ANIM_GROW_FROM_LEFT] + view.getHeight());
        this.mRootView.measure(-2, -2);
        int measuredWidth = this.mRootView.getMeasuredWidth();
        int measuredHeight = this.mRootView.getMeasuredHeight();
        int width = this.mWindowManager.getDefaultDisplay().getWidth();
        int i = (width - measuredWidth) / ANIM_GROW_FROM_RIGHT;
        measuredWidth = rect.top - measuredHeight;
        if (measuredHeight > view.getTop()) {
            z = false;
            measuredHeight = rect.bottom;
        } else {
            measuredHeight = measuredWidth;
            z = true;
        }
        showArrow(z ? R.id.arrow_down : R.id.arrow_up, this.mTrack.getMeasuredWidth() / ANIM_GROW_FROM_RIGHT);
        setAnimationStyle(width, rect.centerX(), z);
        this.mWindow.showAtLocation(view, 0, i, measuredHeight);
        if (this.mAnimateTrack) {
            this.mTrack.startAnimation(this.mTrackAnim);
        }
    }
}
