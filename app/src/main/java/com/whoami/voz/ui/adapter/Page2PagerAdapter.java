package com.whoami.voz.ui.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nna88.voz.contain.Post;
import com.nna88.voz.listview.Page3ListViewAdapter;
import com.nna88.voz.listview.listViewCustom2;
import com.nna88.voz.main.BuildConfig;
import com.nna88.voz.main.Global;
import com.nna88.voz.main.R;
import com.nna88.voz.util.Util;
import com.whoami.voz.ui.delegate.PagerListener;
import com.whoami.voz.ui.fragment.Page2Fragment;
import com.whoami.voz.ui.fragment.Page3Fragment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nna88.voz.contain.Thread;
import com.whoami.voz.ui.utils.HtmlLoader;
import com.whoami.voz.ui.widget.NavigationBar;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by thaidh on 5/14/16.
 */
public class Page2PagerAdapter extends BasePagerAdapter {
    private static final String TAG = Page2PagerAdapter.class.getSimpleName();
    private Activity mContext;

    private Map<Integer, ArrayList<Thread>> mMapPostPerPage = new LinkedHashMap() {
        public boolean removeEldestEntry(Map.Entry eldest) {
            return size() > Page3Fragment.MAX_ENTRIES;
        }
    };
    private boolean isLoading;

    public Page2PagerAdapter(Activity mContext, String mUrl, int mTotalPage) {
        this.mContext = mContext;
        this.mUrl = mUrl;
        this.mTotalPage = mTotalPage;
        this.pageCount = mTotalPage;
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
        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                    mPage3Listener.onSwipeTReresh(refreshLayout);
                loadPage(position + 1, true);
                refreshLayout.setRefreshing(false);
            }
        });
        final ListView listView = (ListView) view.findViewById(R.id.content_frame);
        View navigationHeader = mContext.getLayoutInflater().inflate(R.layout.navigation_bar, null);
        navigationHeader.setTag(TAG_NAVIGATION_HEADER);
        setPagerListener(mPagerListener);
        View navigationFooter = mContext.getLayoutInflater().inflate(R.layout.navigation_bar, null);
        navigationFooter.setTag(TAG_NAVIGATION_FOOTER);
        listView.addHeaderView(navigationHeader);
        listView.addFooterView(navigationFooter);
        final ArrayList<Thread> curListPost = mMapPostPerPage.get(Integer.valueOf(loadPageIndex));
        if (curListPost != null) {
            view.findViewById(R.id.layout_progress).setVisibility(View.GONE);
            listViewCustom2 adapter = new listViewCustom2(mContext, curListPost);
            NavigationBar navigationHeaderBar = (NavigationBar) view.findViewWithTag(TAG_NAVIGATION_HEADER);
            navigationHeaderBar.refresh(loadPageIndex, mTotalPage);
            NavigationBar navigationFooterBar = (NavigationBar) view.findViewWithTag(TAG_NAVIGATION_FOOTER);
            navigationFooterBar.refresh(loadPageIndex, mTotalPage);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                    handleThreadClicked(i, listView, curListPost);
                }
            });
        } else {
            loadPage(loadPageIndex, false);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private void loadPage(final int curPage, boolean refres) {
        try {
            if (refres || !mMapPostPerPage.containsKey(Integer.valueOf(curPage))) {
                String url = getUrlWithPage(curPage);
                if (url != null) {
                    isLoading = true;
                    HtmlLoader.getInstance().fetchData(url, new HtmlLoader.HtmlLoaderListener() {
                        @Override
                        public void onCallback(Document doc) {
                            try {
                                parseDataPage2(doc, curPage);
                                isLoading = false;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    refreshCurrentPage(curPage, false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void parseDataPage2(Document doc, final int curPage) throws Exception {
        if (doc != null) {
            ArrayList<Thread> ListContains = new ArrayList<>();
            ListContains.clear();
            Element first = doc.select("a[href=private.php]").first();
            //todo set page
            parsePage(0, 0, doc);
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
                ListContains.add(new Thread("Sub-Forum", null, null, null, null, null, null));
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
                ListContains.add(new Thread(str2, text, null, null, loadBitmapAssert, attr, str));
                bitmap = loadBitmapAssert;
                str3 = attr;
                str4 = text;
            }
            ListContains.add(new Thread("Thread", null, null, null, null, null, null));
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
                Thread thread = new Thread(str2, text, null, null, loadBitmapAssert, attr, str);
                thread.mPrefixLink = str3;
                thread.mIdThread = attr.split("t=")[1];
                if (element.select("a:has(img[src*=lastpost])").first() != null) {
                    thread.setUrlLastPost(element.select("a:has(img[src*=lastpost])").first().attr("href"));
                }
                if (first4.hasClass("vozsticky")) {
                    thread.setSticky(true);
                } else {
                    thread.setSticky(false);
                }
//                if (checkBookmark(thread.mIdThread)) {
//                    thread.isBookmark = true;
//                    this.mDataBookmark.updateBookmark(thread);
//                }
                ListContains.add(thread);
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
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setTotalPage(mTotalPage);
                    refreshCurrentPage(curPage, false);
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
                String[] list = mContext.getAssets().list("images/smilies/Off");
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
            InputStream open = mContext.getAssets().open(str);
            Bitmap decodeStream = BitmapFactory.decodeStream(open);
            if (str.contains("statusicon")) {
                int convertDpToPx = Util.convertDpToPx(mContext.getApplicationContext(), 24);
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

    private void refreshCurrentPage(final int curPage, final boolean forceRefresh) {
        try {
            Log.i(TAG, "Refresh page :  " + curPage);
            View page = mPagerListener.findPageView(curPage);
            if (page != null) {
                final ListView listView = (ListView) page.findViewById(R.id.content_frame);
                NavigationBar navigationHeaderBar = (NavigationBar) page.findViewWithTag(TAG_NAVIGATION_HEADER);
                navigationHeaderBar.refresh(curPage, mTotalPage);
                NavigationBar navigationFooterBar = (NavigationBar) page.findViewWithTag(TAG_NAVIGATION_FOOTER);
                navigationFooterBar.refresh(curPage, mTotalPage);
                if (forceRefresh) {
                    if (listView.getAdapter() != null && mMapPostPerPage.containsKey(curPage)) {
                        listViewCustom2 adapter = new listViewCustom2(mContext, mMapPostPerPage.get(curPage));
                        listView.setAdapter(adapter);
                    }
                } else {
                    (page.findViewById(R.id.layout_progress)).setVisibility(View.GONE); // gone progress
                    if (listView.getAdapter() == null && mMapPostPerPage.containsKey(curPage)) {
                        final ArrayList<Thread> curListPost = mMapPostPerPage.get(curPage);
                        final listViewCustom2 adapter = new listViewCustom2(mContext, curListPost);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                                handleThreadClicked(i, listView, curListPost);
                            }
                        });
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleThreadClicked(int i, ListView listView, ArrayList<Thread> curListPost) {
        try {
            i = i - listView.getHeaderViewsCount();
            if (curListPost.get(i).isUrl() == 0) {
                if (curListPost.get(i).UrlThread() != null) {
                    Page2Fragment fragment = Page2Fragment.newInstance(curListPost.get(i).UrlThread(), curListPost.get(i).Thread());
                    FragmentTransaction ft = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.container, fragment, "AAAA");
                    ft.addToBackStack("Page2Fragment");
                    ft.commit();
                }
            } else if (curListPost.get(i).isUrl() == 1) {
                Page3Fragment fragment = null;

                if (curListPost.get(i).UrlLastPosst() != null) {
                    if (curListPost.get(i).UrlLastPosst() != null) {
                        fragment = Page3Fragment.newInstance("", curListPost.get(i).UrlLastPosst());
                    }
                } else if (curListPost.get(i).UrlThread() != null) {
                    fragment = Page3Fragment.newInstance(curListPost.get(i).Thread(), curListPost.get(i).UrlThread());
                }
                if (fragment != null) {
                    FragmentTransaction ft = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.container, fragment, "AAAA");
                    ft.addToBackStack("Page3Fragment");
                    ft.commit();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
