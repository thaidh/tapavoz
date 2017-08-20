package com.whoami.voz.pagethreads;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.whoami.voz.R;
import com.whoami.voz.activity.BaseActivity;
import com.whoami.voz.adapter.BasePagerAdapter;
import com.whoami.voz.adapter.Page2PagerAdapter;
import com.whoami.voz.adapter.list.Page2ListViewAdapter;
import com.whoami.voz.contain.VozThread;
import com.whoami.voz.delegate.PagerListener;
import com.whoami.voz.main.Global;
import com.whoami.voz.pageposts.PagePostActivity;
import com.whoami.voz.utils.Util;
import com.whoami.voz.widget.NavigationBar;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by thaidh on 9/3/16.
 */
public class PageThreadsActivity extends BaseActivity implements PageThreadsContract.View {
    public static final String TAG = PageThreadsActivity.class.getSimpleName();

    private PageThreadsContract.Presenter mPresenter;

    private String mUrl;
    private String mTitle;
    private LinearLayout mLayoutProgress;
    private ViewPager mViewPager;
    private Page2PagerAdapter mPage2PagerAdapter;
    private TextView mTitleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_page3);
        setupToolbar();
        initLayout();
        initData();
        initQuickReturn();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mUrl = Global.URL + bundle.getString(EXTRA_URL);
            mTitle = bundle.getString(EXTRA_TITLE);
        }
        mToolbar.setTitle(mTitle);

        mPresenter = new PageThreadsPresenter(this, mUrl);
        mPresenter.loadPage(1, false);
    }

    private void initLayout() {

        mLayoutProgress = (LinearLayout) findViewById(R.id.layoutprogress);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(1);

        mPage2PagerAdapter = new Page2PagerAdapter(this, mUrl, 1, mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPresenter.loadPage(position + 1, false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setAdapter(mPage2PagerAdapter);
        mPage2PagerAdapter.setPagerListener(new PagerListener() {
            @Override
            public View findPageView(int curPage) {
                return mViewPager.findViewWithTag(curPage);
            }

            @Override
            public void onSwipeTReresh(SwipeRefreshLayout swipeRefreshLayout) {

            }

            @Override
            public void onGoPage(int type) {

            }

            @Override
            public void showDialogGoPage() {

            }
        });
    }

    public void refreshCurrentPage(int totalPage, final int curPage, boolean refresh, final ArrayList<VozThread> vozThreads) {
        try {
            mPage2PagerAdapter.setTotalPage(totalPage);
            Log.i(TAG, "Refresh page :  " + curPage);
            View page = mViewPager.findViewWithTag(curPage);
            if (page != null) {
                final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) page.findViewById(R.id.swipe_refresh_layout);
                refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mPresenter.loadPage(curPage, true);
                        refreshLayout.setRefreshing(false);
                    }
                });


                final ListView listView = (ListView) page.findViewById(R.id.content_frame);
                NavigationBar navigationHeaderBar = (NavigationBar) page.findViewWithTag(BasePagerAdapter.TAG_NAVIGATION_HEADER);
                navigationHeaderBar.refresh(curPage, totalPage);
                NavigationBar navigationFooterBar = (NavigationBar) page.findViewWithTag(BasePagerAdapter.TAG_NAVIGATION_FOOTER);
                navigationFooterBar.refresh(curPage ,totalPage);
                (page.findViewById(R.id.layout_progress)).setVisibility(View.GONE); // gone progress

                if ((refresh || listView.getAdapter() == null)) {
                    final Page2ListViewAdapter adapter = new Page2ListViewAdapter(this, vozThreads);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                            handleThreadClicked(i, listView, vozThreads);
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleThreadClicked(int i, ListView listView, ArrayList<VozThread> curListPost) {
        try {
            i = i - listView.getHeaderViewsCount();
            if (curListPost.get(i).isUrl() == 0) {
                if (curListPost.get(i).UrlThread() != null) {
                    Intent intent = new Intent(PageThreadsActivity.this, PageThreadsActivity.class);
                    intent.putExtra(EXTRA_URL, curListPost.get(i).UrlThread());
                    intent.putExtra(EXTRA_TITLE, curListPost.get(i).Thread());
                    startActivity(intent);
                }
            } else if (curListPost.get(i).isUrl() == 1) {
                Intent intent = new Intent(PageThreadsActivity.this, PagePostActivity.class);
                if (curListPost.get(i).UrlLastPosst() != null) {
                    if (curListPost.get(i).UrlLastPosst() != null) {
                        intent.putExtra(EXTRA_URL, curListPost.get(i).UrlLastPosst());
                        intent.putExtra(EXTRA_TITLE, "");
                    }
                } else if (curListPost.get(i).UrlThread() != null) {
                    intent.putExtra(EXTRA_URL, curListPost.get(i).UrlThread());
                    intent.putExtra(EXTRA_TITLE, curListPost.get(i).Thread());
                }
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap loadBitmapAssert(String str) {
        int i = 0;
        if (str.contains("smilies")) {
            String str2 = str.substring(0, str.length() - 3) + "gif";
            try {
                String[] list = getAssets().list("images/smilies/Off");
                int length = list.length;
                while (i < length) {
                    if ((str.substring(0, str.length() - 3) + "png").contains(list[i])) {
                        str = str.substring(0, str.length() - 3) + "png";
                        break;
                    }
                    i += 1;
                }
                str = str2;
            } catch (Exception e) {
                e.printStackTrace();
                str = str2;
            }
        }
        try {
            InputStream open = getAssets().open(str);
            Bitmap decodeStream = BitmapFactory.decodeStream(open);
            if (str.contains("statusicon")) {
                int convertDpToPx = Util.convertDpToPx(this, 24);
                open.close();
                return Bitmap.createScaledBitmap(decodeStream, convertDpToPx, convertDpToPx, false);
            }
            open.close();
            return decodeStream;
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public void setPresenter(PageThreadsContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }
}
