package com.whoami.voz.ui.pageposts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.whoami.voz.R;
import com.whoami.voz.chrome.customtab.CustomTabActivityHelper;
import com.whoami.voz.chrome.customtab.WebviewFallback;
import com.whoami.voz.retrofit.data.PostData;
import com.whoami.voz.ui.activity.BaseActivity;
import com.whoami.voz.ui.adapter.BasePagerAdapter;
import com.whoami.voz.ui.adapter.Page3PagerAdapter;
import com.whoami.voz.ui.adapter.list.PostListViewAdapter;
import com.whoami.voz.ui.contain.VozThread;
import com.whoami.voz.ui.delegate.PagerListener;
import com.whoami.voz.ui.main.Global;
import com.whoami.voz.ui.mysqlite.DatabaseHelper;
import com.whoami.voz.ui.utils.ClipboardUtils;
import com.whoami.voz.ui.utils.Util;
import com.whoami.voz.ui.widget.NavigationBar;

/**
 * Created by thaidh on 9/3/16.
 */
public class PagePostActivity extends BaseActivity implements PagePostContract.View, CustomTabActivityHelper.ConnectionCallback {
    public static final String TAG = PagePostActivity.class.getSimpleName();
    private Bitmap bmImageStart;

    // base activity
    //    private ListView mList;
    private LinearLayout mLayoutProgress;
    private ViewPager mViewPager;
    private Page3PagerAdapter mPage3PagerAdapter;
    private PagerListener mPagerListener;

    private String mUrl;
    private String mTitle;
    private PagePostContract.Presenter mPresenter;
    private CustomTabActivityHelper customTabActivityHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_page3);
        setupToolbar();
        initLayout();
        initData();
        initQuickReturn();
    }

    private void initLayout() {
//        readSettings();

        this.mLayoutProgress = (LinearLayout) findViewById(R.id.layoutprogress);
        Global.iDensity = getResources().getDisplayMetrics().density;

        this.bmImageStart = BitmapFactory.decodeResource(getResources(), R.drawable.stub_image);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(1);
        mPage3PagerAdapter = new Page3PagerAdapter(this, 1, mUrl, bmImageStart, 1.0f, mViewPager);
        mPagerListener = new PagerListener() {
            @Override
            public View findPageView(int curPage) {
                return mViewPager.findViewWithTag(curPage);
            }

            @Override
            public void onSwipeTReresh(SwipeRefreshLayout swipeRefreshLayout) {

            }

            @Override
            public void onGoPage(int type) {
                try {
                    switch (type) {
                        case BasePagerAdapter.GO_FIRST: {
                            if (mViewPager.getCurrentItem() != 0) {
                                mViewPager.setCurrentItem(0);
                            }
                            break;
                        }
                        case BasePagerAdapter.GO_LAST:{
                            int lasItemIndex = mPage3PagerAdapter.getTotalPage() - 1;
                            if (mViewPager.getCurrentItem() != lasItemIndex) {
                                mViewPager.setCurrentItem(lasItemIndex);
                            }
                            break;
                        }
                        case BasePagerAdapter.GO_PREVIOUS:{
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                            break;
                        }
                        case BasePagerAdapter.GO_NEXT: {
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                            break;

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void showDialogGoPage() {
                alertGoPage();
            }
        };
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
        mViewPager.setAdapter(mPage3PagerAdapter);

    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (!bundle.getString(EXTRA_URL).contains(Global.URL)) {
                mUrl = Global.URL + bundle.getString(EXTRA_URL);
            } else {
                mUrl = bundle.getString(EXTRA_URL);
            }
            mTitle = bundle.getString(EXTRA_TITLE);
        }
        mToolbar.setTitle(mTitle);
        customTabActivityHelper = new CustomTabActivityHelper();
        customTabActivityHelper.setConnectionCallback(this);
        mPresenter = new PagePostPresenter(this, mUrl);
        //init page 1
        mPresenter.loadPage(1, false);
    }

    protected void alertGoPage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = getLayoutInflater().inflate(R.layout.login, null);
        final EditText editText = (EditText) inflate.findViewById(R.id.alert_edit1);
        TextView textView = (TextView) inflate.findViewById(R.id.alert_txt1);
        TextView textView2 = (TextView) inflate.findViewById(R.id.alert_txt2);
        Button button = (Button) inflate.findViewById(R.id.alert_ok);
        Button button2 = (Button) inflate.findViewById(R.id.alert_cancle);
        ((EditText) inflate.findViewById(R.id.alert_edit2)).setVisibility(View.GONE);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        textView.setVisibility(View.GONE);
        textView2.setVisibility(View.GONE);
        builder.setTitle("Page").setView(inflate);
        final Dialog dialog = builder.create();
        dialog.show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strPage = editText.getText().toString();
                int page = Integer.parseInt(strPage) - 1;
                if (page >= 0 && page < mPage3PagerAdapter.getTotalPage()) {
                    mViewPager.setCurrentItem(page);
                }
                dialog.dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void refreshCurrentPage(int totalPage, final int curPage, boolean forceRefresh, PostData data) {
        try {
            mToolbar.setTitle(data.title);
            mPage3PagerAdapter.setTotalPage(totalPage);
            Log.i(TAG, "Refresh page :  " + curPage);
            View page = mViewPager.findViewWithTag(curPage);
            if (page != null) {
                ListView listView = (ListView) page.findViewById(R.id.content_frame);
                final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) page.findViewById(R.id.swipe_refresh_layout);
                refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mPresenter.loadPage(curPage, true);
                        refreshLayout.setRefreshing(false);
                    }
                });
                NavigationBar navigationHeaderBar = (NavigationBar) page.findViewWithTag(BasePagerAdapter.TAG_NAVIGATION_HEADER);
                navigationHeaderBar.refresh(curPage, totalPage);
                navigationHeaderBar.setPagerListener(mPagerListener);
                NavigationBar navigationFooterBar = (NavigationBar) page.findViewWithTag(BasePagerAdapter.TAG_NAVIGATION_FOOTER);
                navigationFooterBar.refresh(curPage, totalPage);
                navigationFooterBar.setPagerListener(mPagerListener);
                (page.findViewById(R.id.layout_progress)).setVisibility(View.GONE); // gone progress
                if ((forceRefresh || listView.getAdapter() == null)) {
                    final PostListViewAdapter adapter = new PostListViewAdapter(this, data.vozPostList);
                    listView.setAdapter(adapter);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bookmark, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_bookmark) {
            VozThread thread = new VozThread(mTitle, mUrl, "");
            DatabaseHelper.getInstance(this).insertBookmark(thread);
            Util.showMess("Bookmark : " + mUrl);
            return true;
        } else if (id == R.id.action_copy_url) {
            ClipboardUtils.copyText(mPresenter.getCurrentUrl());
        } else if (id == R.id.action_open_webview) {
            Uri uri  = Uri.parse(mPresenter.getCurrentUrl());
            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder(customTabActivityHelper.getSession()).build();
            CustomTabActivityHelper.openCustomTab(this, customTabsIntent, uri, new WebviewFallback());
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(PagePostContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        customTabActivityHelper.setConnectionCallback(null);
    }

    @Override
    public void onCustomTabsConnected() {
//        mMayLaunchUrlButton.setEnabled(true);
    }

    @Override
    public void onCustomTabsDisconnected() {
//        mMayLaunchUrlButton.setEnabled(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        customTabActivityHelper.bindCustomTabsService(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        customTabActivityHelper.unbindCustomTabsService(this);
//        mMayLaunchUrlButton.setEnabled(false);
    }
}
