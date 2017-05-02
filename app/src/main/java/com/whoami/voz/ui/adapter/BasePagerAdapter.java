package com.whoami.voz.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.whoami.voz.ui.delegate.PagerListener;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.lang.ref.WeakReference;

/**
 * Created by thaidh on 5/18/16.
 */
public abstract class BasePagerAdapter  extends PagerAdapter{
    public static final int GO_FIRST = 4;
    public static final int GO_LAST = 5;
    public static final int GO_PREVIOUS = 3;
    public static final int GO_NEXT = 2;

    public static final String TAG_NAVIGATION_HEADER = "navigation_header";
    public static final String TAG_NAVIGATION_FOOTER = "navigation_footer";
    private static final String TAG = BasePagerAdapter.class.getSimpleName();
    protected WeakReference<ViewPager> mViewPager;
    protected PagerListener mPagerListener;
    protected int loadPageIndex;
    protected int mTotalPage;
    protected int pageCount;
    protected String mUrl;

    public void setTotalPage(int mTotalPage) {
        Log.e(TAG, "update total page : " + mTotalPage);
        this.pageCount = mTotalPage;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return pageCount;
    }

    public void setPagerListener(PagerListener mPagerListener) {
        this.mPagerListener = mPagerListener;
    }
}
