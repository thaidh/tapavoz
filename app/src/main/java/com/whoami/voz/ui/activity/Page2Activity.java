package com.whoami.voz.ui.activity;

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

import com.whoami.voz.BuildConfig;
import com.whoami.voz.R;
import com.whoami.voz.ui.adapter.BasePagerAdapter;
import com.whoami.voz.ui.adapter.Page2PagerAdapter;
import com.whoami.voz.ui.adapter.list.Page2ListViewAdapter;
import com.whoami.voz.ui.contain.VozThread;
import com.whoami.voz.ui.delegate.PagerListener;
import com.whoami.voz.ui.fragment.Page3Fragment;
import com.whoami.voz.ui.main.Global;
import com.whoami.voz.ui.utils.HtmlLoader;
import com.whoami.voz.ui.utils.Util;
import com.whoami.voz.ui.widget.NavigationBar;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by thaidh on 9/3/16.
 */
public class Page2Activity extends BaseActivity {
    public static final String TAG = Page2Activity.class.getSimpleName();
    private String mUrl;
    private String mTitle;
    private LinearLayout mLayoutProgress;
    private ViewPager mViewPager;
    private Page2PagerAdapter mPage2PagerAdapter;
    private TextView mTitleTv;


    private Map<Integer, ArrayList<VozThread>> mMapPostPerPage = new LinkedHashMap() {
        public boolean removeEldestEntry(Map.Entry eldest) {
            return size() > Page3Fragment.MAX_ENTRIES;
        }
    };
    private int mTotalPage = 1;


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

        loadPage(1, false);
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
                loadPage(position + 1, false);
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

    public void loadPage(final int curPage, boolean refres) {
        try {
            if (refres || !mMapPostPerPage.containsKey(Integer.valueOf(curPage))) {
                String url = getUrlWithPage(curPage);
                if (url != null) {
                    HtmlLoader.getInstance().fetchData(url, curPage, new HtmlLoader.HtmlLoaderListener() {
                        @Override
                        public void onCallback(Document doc, int page) {
                            try {
                                parseDataPage2(doc, page);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } else {
                refreshCurrentPage(curPage, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void parseDataPage2(Document doc, final int curPage) throws Exception {
        if (doc != null) {
            ArrayList<VozThread> ListContains = new ArrayList<>();
            ListContains.clear();
            Element first = doc.select("a[href=private.php]").first();
            //todo set page
            //parse page
            Element firstElement = doc.select("div[class=pagenav").first();
            if (firstElement !=null) {
                String strPage = firstElement.select("td[class=vbmenu_control]").text();
                mTotalPage = Integer.parseInt(strPage.split(" ")[3]);
            }
//            if (first != null) {
//                this.mUser.setUserId(((Element) doc.select("td[class=alt2]").get(0)).select("a[href*=mem]").attr("href").split("=")[1]);
//                first = doc.select("input[name*=securitytoken]").first();
//                if (first != null) {
//                    this.mUser.setToken(first.attr("value"));
//                }
//            }
            first = doc.select("tbody:has(form[id])").first();
            if (first == null) {
//                toast("main Element null");
//                parserNotification();
                return;
            }
            Element first2 = first.select("td[class=tcat]:contains(Threads in Forum)").first();
            if (first2 != null) {
//                String mTitle = first2.select("span").text();
//                mTitle = mTitle.substring(2, mTitle.length());
//                toolbar.setTitle(mTitle);
            }
            first2 = first.select("tbody[id*=threadbits_forum]").first();
            if (first2 == null) {
//                toast("elebody null");
                return;
            }
            Bitmap loadBitmapAssert;
            String attr;
            String text;
            Elements select = first2.select("tr:has(td[id*=td_threads])");
            Elements select2 = first.select("tr > td[class=alt1Active]");
            Iterator it = select.iterator();
            Iterator it2 = select2.iterator();
            if (it2.hasNext()) {
                ListContains.add(new VozThread("Sub-Forum", null, null, null, null, null, null));
            }
            String str = null;
            String str2 = null;
            Bitmap bitmap = null;
            String str3 = null;
            String str4 = null;
            while (it2.hasNext()) {
                first = (Element) it2.next();
                Element first3 = first.select("td > img[id]").first();
                Element nextElementSibling = first.nextElementSibling();
                Element nextElementSibling2 = nextElementSibling.nextElementSibling();
                Element nextElementSibling3 = nextElementSibling2.nextElementSibling();
                loadBitmapAssert = (!Global.bTopicHeader || first3 == null) ? bitmap : loadBitmapAssert(first3.select("img").attr("src"));
                if (first != null) {
                    str2 = first.text();
                }
                attr = first.select("div > a").first() != null ? first.select("div > a").first().attr("href") : str3;
                if (nextElementSibling.select("span > a").first() != null) {
                    text = nextElementSibling.select("span > a").first().text();
                    str = nextElementSibling.select("span > a").first().attr("href");
                } else {
                    text = str4;
                }
                if (!(nextElementSibling2 == null || nextElementSibling3 == null)) {
                    text = text + "\nReplie:" + nextElementSibling2.text() + " - View:" + nextElementSibling3.text();
                }
                ListContains.add(new VozThread(str2, text, null, null, loadBitmapAssert, attr, str));
                bitmap = loadBitmapAssert;
                str3 = attr;
                str4 = text;
            }
            ListContains.add(new VozThread("Thread", null, null, null, null, null, null));
            text = null;
            loadBitmapAssert = bitmap;
            attr = str3;
            String str5 = str4;
            while (it.hasNext()) {
                String attr2;
                str = null;
                str2 = BuildConfig.FLAVOR;
                Element element = (Element) it.next();
                Element first4 = element.select("a[id*=thread_title").first();
                if (first4 != null) {
                    if (element.select("a[href*=prefixid]").first() != null) {
                        str2 = element.select("a[href*=prefixid]").first().text() + "-";
                        attr2 = element.select("a[href*=prefixid]").attr("href");
                    } else {
                        attr2 = text;
                    }
                    str2 = str2 + element.select("a[id*=thread_title").first().text();
                    attr = element.select("a[id*=thread_title").first().attr("href");
                    str3 = attr2;
                } else {
                    str3 = text;
                }
                attr2 = element.select("td[class=alt2]:has(span)").first() != null ? element.select("td[class=alt2]:has(span)").first().text() : str5;
                if (element.select("td[align=center]").first() != null) {
                    attr2 = attr2 + "\nReplies:" + ((Element) element.select("td[align=center]").get(0)).text() + " - Views:" + ((Element) element.select("td[align=center]").get(1)).text();
                }
                text = element.select("div:has(span[onclick*=member.php])").first() != null ? element.select("div:has(span[onclick*=member.php])").first().text() + " - " + attr2 : attr2;
                if (Global.bTopicHeader && element.select("img[id*=thread_statusicon]").first() != null) {
                    loadBitmapAssert = loadBitmapAssert(element.select("img[id*=thread_statusicon]").first().attr("src"));
                }
                if (element.select("a[id*=thread_gotonew]").first() != null) {
                    str = element.select("a[id*=thread_gotonew]").first().attr("href");
                }
                VozThread vozThread = new VozThread(str2, text, null, null, loadBitmapAssert, attr, str);
                vozThread.mPrefixLink = str3;
                vozThread.mIdThread = attr.split("t=")[1];
                if (element.select("a:has(img[src*=lastpost])").first() != null) {
                    vozThread.setUrlLastPost(element.select("a:has(img[src*=lastpost])").first().attr("href"));
                }
                if (first4.hasClass("vozsticky")) {
                    vozThread.setSticky(true);
                } else {
                    vozThread.setSticky(false);
                }
//                if (checkBookmark(thread.mIdThread)) {
//                    thread.isBookmark = true;
//                    this.mDataBookmark.updateBookmark(thread);
//                }
                ListContains.add(vozThread);
                str5 = text;
                text = str3;
            }
//            this.mItemCount = this.adapter.getCount();
//            if (this.mItemOffsetY == null) {
//                this.mItemOffsetY = new int[(this.mItemCount + 1)];
//                this.mItemtemp = new int[(this.mItemCount + 1)];
//            }
//            this.scrollIsComputed = true;
            mMapPostPerPage.put(Integer.valueOf(curPage), ListContains);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mPage2PagerAdapter.setTotalPage(mTotalPage);
                    refreshCurrentPage(curPage, true);
                }
            });

//            mList.post(new Runnable() {
//                public void run() {
//                    mList.setSelection(0);
//                }
//            });
        }
    }

    Bitmap loadBitmapAssert(String str) {
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

    private String getUrlWithPage(int mPage) {
        if (!mUrl.contains("&page=")) {
            mUrl = mUrl.concat("&page=1");
        }
        String concat = mUrl.substring(0, mUrl.lastIndexOf("=") + 1).concat(String.valueOf(mPage));
        return concat.contains("&page=0") ? concat.split("&page")[0] : concat;
    }

    private void refreshCurrentPage(final int curPage, boolean refresh) {
        try {
            Log.i(TAG, "Refresh page :  " + curPage);
            View page = mViewPager.findViewWithTag(curPage);
            if (page != null) {
                final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) page.findViewById(R.id.swipe_refresh_layout);
                refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadPage(curPage, true);
                        refreshLayout.setRefreshing(false);
                    }
                });


                final ListView listView = (ListView) page.findViewById(R.id.content_frame);
                NavigationBar navigationHeaderBar = (NavigationBar) page.findViewWithTag(BasePagerAdapter.TAG_NAVIGATION_HEADER);
                navigationHeaderBar.refresh(curPage, mTotalPage);
                NavigationBar navigationFooterBar = (NavigationBar) page.findViewWithTag(BasePagerAdapter.TAG_NAVIGATION_FOOTER);
                navigationFooterBar.refresh(curPage, mTotalPage);
                (page.findViewById(R.id.layout_progress)).setVisibility(View.GONE); // gone progress
                if ((refresh || listView.getAdapter() == null) && mMapPostPerPage.containsKey(curPage)) {
                    final ArrayList<VozThread> curListPost = mMapPostPerPage.get(curPage);
                    final Page2ListViewAdapter adapter = new Page2ListViewAdapter(this, curListPost);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                            handleThreadClicked(i, listView, curListPost);
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
                    Intent intent = new Intent(Page2Activity.this, Page2Activity.class);
                    intent.putExtra(EXTRA_URL, curListPost.get(i).UrlThread());
                    intent.putExtra(EXTRA_TITLE, curListPost.get(i).Thread());
                    startActivity(intent);
                }
            } else if (curListPost.get(i).isUrl() == 1) {
                Intent intent = new Intent(Page2Activity.this, Page3Activity.class);
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
}
