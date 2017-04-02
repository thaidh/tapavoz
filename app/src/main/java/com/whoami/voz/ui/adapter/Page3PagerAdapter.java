package com.whoami.voz.ui.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.whoami.voz.R;
import com.whoami.voz.ui.activity.BaseActivity;
import com.whoami.voz.ui.contain.Post;
import com.whoami.voz.ui.widget.NavigationBar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by thaidh on 10/3/15.
 */
public class Page3PagerAdapter extends BasePagerAdapter {
    private static final String TAG = Page3PagerAdapter.class.getSimpleName();
    private Activity mContext;
    private Bitmap mImageStart;
    private float mTextSize;
    private Map<Integer, ArrayList<Post>>  mMapPostPerPage = new LinkedHashMap() {
        public boolean removeEldestEntry(Map.Entry eldest) {
            return size() > BaseActivity.MAX_ENTRIES;
        }
    };


    public Page3PagerAdapter(Activity mContext, int mTotalPage, String mUrl, Bitmap mImageStart, float mTextSize, ViewPager viewPager) {
        this.mTotalPage = mTotalPage;
        pageCount = mTotalPage;
        this.mTextSize = mTextSize;
        this.mContext = mContext;
        this.mUrl = mUrl;
        this.mImageStart = mImageStart;
        mViewPager = new WeakReference<ViewPager>(viewPager);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        loadPageIndex = position + 1;
        Log.i(TAG, "instantiateItem : " + loadPageIndex);
        View view = mContext.getLayoutInflater().inflate(R.layout.layout_page3_item, null);
        view.setTag(loadPageIndex);
        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                loadPage(position + 1, true);
//                refreshLayout.setRefreshing(false);
            }
        });
        ListView listView = (ListView) view.findViewById(R.id.content_frame);

        NavigationBar navigationHeader = (NavigationBar) mContext.getLayoutInflater().inflate(R.layout.navigation_bar, null);
        navigationHeader.setTag(TAG_NAVIGATION_HEADER);
//        navigationHeader.setPagerListener(mPagerListener);
        NavigationBar navigationFooter = (NavigationBar) mContext.getLayoutInflater().inflate(R.layout.navigation_bar, null);
        navigationFooter.setTag(TAG_NAVIGATION_FOOTER);
//        navigationFooter.setPagerListener(mPagerListener);

        listView.addHeaderView(navigationHeader);
        listView.addFooterView(navigationFooter);

//        ArrayList<Post> curListPost = mMapPostPerPage.get(Integer.valueOf(loadPageIndex));
//        if (curListPost != null) {
//            view.findViewById(R.id.layout_progress).setVisibility(View.GONE);
//            Page3ListViewAdapter adapter = new Page3ListViewAdapter(mContext, curListPost, null, mImageStart, mTextSize);
//            NavigationBar navigationHeaderBar = (NavigationBar) view.findViewWithTag(TAG_NAVIGATION_HEADER);
//            navigationHeaderBar.refresh(loadPageIndex, mTotalPage);
//            NavigationBar navigationFooterBar = (NavigationBar) view.findViewWithTag(TAG_NAVIGATION_FOOTER);
//            navigationFooterBar.refresh(loadPageIndex, mTotalPage);
//            listView.setAdapter(adapter);
//        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mPagerListener = null;
    }




    public int getTotalPage() {
        return pageCount;
    }
}