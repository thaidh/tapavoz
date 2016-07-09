package com.whoami.voz.ui.adapter;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.whoami.voz.R;
import com.whoami.voz.ui.widget.NavigationBar;

import java.lang.ref.WeakReference;

/**
 * Created by thaidh on 5/14/16.
 */
public class Page2PagerAdapter extends BasePagerAdapter {
    private static final String TAG = Page2PagerAdapter.class.getSimpleName();
    private Activity mContext;

    public Page2PagerAdapter(Activity mContext, String mUrl, int mTotalPage, ViewPager viewPager) {
        this.mContext = mContext;
        this.mUrl = mUrl;
        this.mTotalPage = mTotalPage;
        this.pageCount = mTotalPage;
        this.mViewPager = new WeakReference<ViewPager>(viewPager);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        loadPageIndex = position + 1;
        Log.i(TAG, "LoadPage : " + loadPageIndex);
        View view = mContext.getLayoutInflater().inflate(R.layout.layout_page3_item, null);
        view.setTag(loadPageIndex);
//        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
//        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                loadPage(position + 1, true);
//                refreshLayout.setRefreshing(false);
//            }
//        });
        final ListView listView = (ListView) view.findViewById(R.id.content_frame);
        NavigationBar navigationHeader = (NavigationBar) mContext.getLayoutInflater().inflate(R.layout.navigation_bar, null);
        navigationHeader.setTag(TAG_NAVIGATION_HEADER);
//        navigationHeader.setPagerListener(mPagerListener);

        NavigationBar navigationFooter = (NavigationBar) mContext.getLayoutInflater().inflate(R.layout.navigation_bar, null);
        navigationFooter.setTag(TAG_NAVIGATION_FOOTER);
//        navigationFooter.setPagerListener(mPagerListener);

        listView.addHeaderView(navigationHeader);
        listView.addFooterView(navigationFooter);
//        final ArrayList<Thread> curListPost = mMapPostPerPage.get(Integer.valueOf(loadPageIndex));
//        if (curListPost != null) {
//            view.findViewById(R.id.layout_progress).setVisibility(View.GONE);
//            listViewCustom2 adapter = new listViewCustom2(mContext, curListPost);
//            NavigationBar navigationHeaderBar = (NavigationBar) view.findViewWithTag(TAG_NAVIGATION_HEADER);
//            navigationHeaderBar.refresh(loadPageIndex, mTotalPage);
//            NavigationBar navigationFooterBar = (NavigationBar) view.findViewWithTag(TAG_NAVIGATION_FOOTER);
//            navigationFooterBar.refresh(loadPageIndex, mTotalPage);
//            listView.setAdapter(adapter);
//
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
//                    handleThreadClicked(i, listView, curListPost);
//                }
//            });
//        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
