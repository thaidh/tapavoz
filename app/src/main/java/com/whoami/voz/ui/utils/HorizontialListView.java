package com.whoami.voz.ui.utils;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Scroller;

import java.util.LinkedList;
import java.util.Queue;

public class HorizontialListView extends AdapterView<ListAdapter> {
    protected ListAdapter mAdapter;
    public boolean mAlwaysOverrideTouch;
    protected int mCurrentX;
    private boolean mDataChanged;
    private DataSetObserver mDataObserver;
    private int mDisplayOffset;
    private GestureDetector mGesture;
    private int mLeftViewIndex;
    private int mMaxX;
    protected int mNextX;
    private OnGestureListener mOnGesture;
    private OnItemClickListener mOnItemClicked;
    private OnItemSelectedListener mOnItemSelected;
    private Queue<View> mRemovedViewQueue;
    private int mRightViewIndex;
    protected Scroller mScroller;

    public HorizontialListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mAlwaysOverrideTouch = true;
        this.mLeftViewIndex = -1;
        this.mRightViewIndex = 0;
//        this.mMaxX = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        this.mMaxX = 5;
        this.mDisplayOffset = 0;
        this.mRemovedViewQueue = new LinkedList();
        this.mDataChanged = false;
        this.mDataObserver = new DataSetObserver() {
            public void onChanged() {
                synchronized (HorizontialListView.this) {
                    HorizontialListView.this.mDataChanged = true;
                }
                HorizontialListView.this.invalidate();
                HorizontialListView.this.requestLayout();
            }

            public void onInvalidated() {
                HorizontialListView.this.reset();
                HorizontialListView.this.invalidate();
                HorizontialListView.this.requestLayout();
            }
        };
        this.mOnGesture = new SimpleOnGestureListener() {
            public boolean onDown(MotionEvent motionEvent) {
                return HorizontialListView.this.onDown(motionEvent);
            }

            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                return HorizontialListView.this.onFling(motionEvent, motionEvent2, f, f2);
            }

            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                synchronized (HorizontialListView.this) {
                    HorizontialListView horizontialListView = HorizontialListView.this;
                    horizontialListView.mNextX += (int) f;
                }
                HorizontialListView.this.requestLayout();
                return true;
            }

            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                Rect rect = new Rect();
                for (int i = 0; i < HorizontialListView.this.getChildCount(); i++) {
                    View childAt = HorizontialListView.this.getChildAt(i);
                    rect.set(childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom());
                    if (rect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                        if (HorizontialListView.this.mOnItemClicked != null) {
                            HorizontialListView.this.mOnItemClicked.onItemClick(HorizontialListView.this, childAt, (HorizontialListView.this.mLeftViewIndex + 1) + i, HorizontialListView.this.mAdapter.getItemId((HorizontialListView.this.mLeftViewIndex + 1) + i));
                        }
                        if (HorizontialListView.this.mOnItemSelected != null) {
                            HorizontialListView.this.mOnItemSelected.onItemSelected(HorizontialListView.this, childAt, (HorizontialListView.this.mLeftViewIndex + 1) + i, HorizontialListView.this.mAdapter.getItemId((HorizontialListView.this.mLeftViewIndex + 1) + i));
                        }
                        return true;
                    }
                }
                return true;
            }
        };
        initView();
    }

    private void addAndMeasureChild(View view, int i) {
        LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LayoutParams(-1, -1);
        }
        addViewInLayout(view, i, layoutParams, true);
        view.measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.AT_MOST));
    }

    private void fillList(int i) {
        int i2 = 0;
        View childAt = getChildAt(getChildCount() - 1);
        fillListRight(childAt != null ? childAt.getRight() : 0, i);
        childAt = getChildAt(0);
        if (childAt != null) {
            i2 = childAt.getLeft();
        }
        fillListLeft(i2, i);
    }

    private void fillListLeft(int i, int i2) {
        while (i + i2 > 0 && this.mLeftViewIndex >= 0) {
            View view = this.mAdapter.getView(this.mLeftViewIndex, (View) this.mRemovedViewQueue.poll(), this);
            addAndMeasureChild(view, 0);
            i -= view.getMeasuredWidth();
            this.mLeftViewIndex--;
            this.mDisplayOffset -= view.getMeasuredWidth();
        }
    }

    private void fillListRight(int i, int i2) {
        while (i + i2 < getWidth() && this.mRightViewIndex < this.mAdapter.getCount()) {
            View view = this.mAdapter.getView(this.mRightViewIndex, (View) this.mRemovedViewQueue.poll(), this);
            addAndMeasureChild(view, -1);
            i += view.getMeasuredWidth();
            if (this.mRightViewIndex == this.mAdapter.getCount() - 1) {
                this.mMaxX = (this.mCurrentX + i) - getWidth();
            }
            this.mRightViewIndex++;
        }
    }

    private void initView() {
        synchronized (this) {
            this.mLeftViewIndex = -1;
            this.mRightViewIndex = 0;
            this.mDisplayOffset = 0;
            this.mCurrentX = 0;
            this.mNextX = 0;
//            this.mMaxX =  ActivityChooserView.MAX_ACTIVITY_COUNT_UNLIMITED;
            this.mMaxX = 5;
            this.mScroller = new Scroller(getContext());
            this.mGesture = new GestureDetector(getContext(), this.mOnGesture);
        }
    }

    private void positionItems(int i) {
        if (getChildCount() > 0) {
            this.mDisplayOffset += i;
            int i2 = this.mDisplayOffset;
            for (int i3 = 0; i3 < getChildCount(); i3++) {
                View childAt = getChildAt(i3);
                int measuredWidth = childAt.getMeasuredWidth();
                childAt.layout(i2, 0, i2 + measuredWidth, childAt.getMeasuredHeight());
                i2 += measuredWidth;
            }
        }
    }

    private void removeNonVisibleItems(int i) {
        View childAt = getChildAt(0);
        while (childAt != null && childAt.getRight() + i <= 0) {
            this.mDisplayOffset += childAt.getMeasuredWidth();
            this.mRemovedViewQueue.offer(childAt);
            removeViewInLayout(childAt);
            this.mLeftViewIndex++;
            childAt = getChildAt(0);
        }
        childAt = getChildAt(getChildCount() - 1);
        while (childAt != null && childAt.getLeft() + i >= getWidth()) {
            this.mRemovedViewQueue.offer(childAt);
            removeViewInLayout(childAt);
            this.mRightViewIndex--;
            childAt = getChildAt(getChildCount() - 1);
        }
    }

    private void reset() {
        synchronized (this) {
            initView();
            removeAllViewsInLayout();
            requestLayout();
        }
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return this.mGesture.onTouchEvent(motionEvent);
    }

    public ListAdapter getAdapter() {
        return this.mAdapter;
    }

    public View getSelectedView() {
        return null;
    }

    protected boolean onDown(MotionEvent motionEvent) {
        this.mScroller.forceFinished(true);
        return true;
    }

    protected boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        synchronized (this) {
            this.mScroller.fling(this.mNextX, 0, (int) (-f), 0, 0, this.mMaxX, 0, 0);
        }
        requestLayout();
        return true;
    }

    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        synchronized (this) {
            super.onLayout(z, i, i2, i3, i4);
            if (this.mAdapter != null) {
                int i5;
                if (this.mDataChanged) {
                    i5 = this.mCurrentX;
                    initView();
                    removeAllViewsInLayout();
                    this.mNextX = i5;
                    this.mDataChanged = false;
                }
                if (this.mScroller.computeScrollOffset()) {
                    this.mNextX = this.mScroller.getCurrX();
                }
                if (this.mNextX < 0) {
                    this.mNextX = 0;
                    this.mScroller.forceFinished(true);
                }
                if (this.mNextX > this.mMaxX) {
                    this.mNextX = this.mMaxX;
                    this.mScroller.forceFinished(true);
                }
                i5 = this.mCurrentX - this.mNextX;
                removeNonVisibleItems(i5);
                fillList(i5);
                positionItems(i5);
                this.mCurrentX = this.mNextX;
                if (!this.mScroller.isFinished()) {
                    post(new Runnable() {
                        public void run() {
                            HorizontialListView.this.requestLayout();
                        }
                    });
                }
            }
        }
    }

    public void scrollTo(int i) {
        synchronized (this) {
            this.mScroller.startScroll(this.mNextX, 0, i - this.mNextX, 0);
            requestLayout();
        }
    }

    public void setAdapter(ListAdapter listAdapter) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mDataObserver);
        }
        this.mAdapter = listAdapter;
        this.mAdapter.registerDataSetObserver(this.mDataObserver);
        reset();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClicked = onItemClickListener;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.mOnItemSelected = onItemSelectedListener;
    }

    public void setSelection(int i) {
    }
}
