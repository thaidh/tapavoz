package com.whoami.voz.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nna88.voz.contain.Post;
import com.nna88.voz.listview.Page3ListViewAdapter;
import com.nna88.voz.main.BuildConfig;
import com.nna88.voz.main.Global;
import com.nna88.voz.main.R;
import com.nna88.voz.ui.SidebarAdapter;
import com.nna88.voz.util.UserInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.whoami.voz.ui.fragment.Page3Fragment;
import com.whoami.voz.ui.utils.HtmlLoader;

import org.apache.commons.io.IOUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by thaidh on 10/3/15.
 */
public class Page3PagerAdapter extends PagerAdapter {
    private static final String TAG = Page3PagerAdapter.class.getSimpleName();

    private String mUrl;
    private int mTotalPage;
    private int mCurPage = 1;
    private ArrayList<Post> mListPosts;
    private Activity mContext;
    private Bitmap mImageStart;
    private float mTextSize;
    private int loadPageIndex;
    private Map<Integer, ArrayList<Post>>  mMapPostPerPage = new LinkedHashMap() {
        public boolean removeEldestEntry(Map.Entry eldest) {
            return size() > Page3Fragment.MAX_ENTRIES;
        }
    };
    Page3PagerListener mPage3Listener;

    public Page3PagerAdapter(Activity mContext, int mTotalPage, Map<Integer, ArrayList<Post>> mMapPostPerPage, String mUrl, Bitmap mImageStart, float mTextSize) {
        this.mTotalPage = mTotalPage;
        this.mTextSize = mTextSize;
        this.mContext = mContext;
        this.mUrl = mUrl;
        this.mImageStart = mImageStart;
//        this.mMapPostPerPage = new LinkedHashMap<>(mMapPostPerPage);
    }

    @Override
    public int getCount() {
        return mTotalPage;
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
        ListView listView = (ListView) view.findViewById(R.id.content_frame);
        ArrayList<Post> curListPost = mMapPostPerPage.get(Integer.valueOf(loadPageIndex));
        if (curListPost != null) {
            view.findViewById(R.id.layout_progress).setVisibility(View.GONE);
            listView.addHeaderView(getNavigationView(Gravity.CENTER, loadPageIndex));
            listView.addFooterView(getNavigationView(Gravity.CENTER, loadPageIndex));
            Page3ListViewAdapter adapter = new Page3ListViewAdapter(mContext, curListPost, null, mImageStart, mTextSize);
            listView.setAdapter(adapter);
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

    public void setTotalPage(int mTotalPage) {
        Log.e(TAG, "update total page : " + mTotalPage);
        this.mTotalPage = mTotalPage;
    }

    public void setPage3Listener(Page3PagerListener mPage3Listener) {
        this.mPage3Listener = mPage3Listener;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public interface Page3PagerListener {
        void onSwipeTReresh(SwipeRefreshLayout swipeRefreshLayout);

        void onGoPage(int type);

        void showDialogGoPage();
    }

    private View getNavigationView(int i, int curPos) {
        LinearLayout linearFooter = (LinearLayout) mContext.getLayoutInflater().inflate(R.layout.threadfoot, null);
        ImageView mImg1Footer = (ImageView) linearFooter.findViewById(R.id.fast_prev);
        ImageView mImg2Footer = (ImageView) linearFooter.findViewById(R.id.prev);
        TextView butPageFooter = (TextView) linearFooter.findViewById(R.id.page_list);
        butPageFooter.setText(curPos + "/" + mTotalPage);
        ImageView mImg3Footer = (ImageView) linearFooter.findViewById(R.id.next);
        ImageView mImg4Footer = (ImageView) linearFooter.findViewById(R.id.fast_next);

        butPageFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPage3Listener.showDialogGoPage();
            }
        });

        mImg1Footer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    mPage3Listener.onGoPage(Page3Fragment.GO_FIRST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mImg2Footer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    mPage3Listener.onGoPage(Page3Fragment.GO_PREVIOUS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        butPageFooter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                alertGoPage();
            }
        });
        mImg3Footer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    mPage3Listener.onGoPage(Page3Fragment.GO_NEXT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mImg4Footer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    mPage3Listener.onGoPage(Page3Fragment.GO_LAST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        linearFooter.setGravity(i);
        //update button navigation state
        if (curPos == 1 && curPos == mTotalPage) {
            mImg1Footer.setEnabled(false);
            mImg2Footer.setEnabled(false);
            mImg3Footer.setEnabled(false);
            mImg4Footer.setEnabled(false);
        } else if (curPos == 0) {
            mImg1Footer.setEnabled(false);
            mImg2Footer.setEnabled(false);
            mImg3Footer.setEnabled(true);
            mImg4Footer.setEnabled(true);

        } else if (curPos == mTotalPage) {
            mImg1Footer.setEnabled(true);
            mImg2Footer.setEnabled(true);
            mImg3Footer.setEnabled(false);
            mImg4Footer.setEnabled(false);
        } else {
            mImg1Footer.setEnabled(true);
            mImg2Footer.setEnabled(true);
            mImg3Footer.setEnabled(true);
            mImg4Footer.setEnabled(true);
        }
        return linearFooter;
    }

    private void loadPage(final int curPage, boolean refres) {
        //download page data
        try {
            if (refres || !mMapPostPerPage.containsKey(Integer.valueOf(curPage))) {
                String url = getUrlWithPage(curPage);
                if (url != null) {
                    //                new TaskGetHtmlSilent().execute(new String[]{url});
//                    setProgress(true);
                    HtmlLoader.getInstance().fetchData(url, new HtmlLoader.HtmlLoaderListener() {
                        @Override
                        public void onCallback(Document doc) {
                            try {
                                parseDataPage3(0, doc, null, true, curPage);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } else {
//                refreshCurrentPage(curPage, false);
                notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUrlWithPage(int mPage) {
        if (!mUrl.contains("&page=")) {
            mUrl = mUrl.concat("&page=1");
        }
        String concat = mUrl.substring(0, mUrl.lastIndexOf("=") + 1).concat(String.valueOf(mPage));
        return concat.contains("&page=0") ? concat.split("&page")[0] : concat;
    }

    void parseDataPage3(int typeParse, Document doc, Document doc2, boolean refresh, int curPage) throws Exception {
//        if (doc == null && this.mParser.sNotif != null) {
//            toast(this.mParser.sNotif);
//        }
        if (doc != null) {
            UserInfo mUser = new UserInfo();
            String mPostId;
            String str;
            Intent intent;
            Bundle bundle;
            Element first;
            switch (typeParse) {
                case 3: {
//                    if (this.iPostType == 3) {
//                        this.mTask.cancel(true);
//                        this.mTask = new TaskGetHtml();
//                        this.mTask.execute(new Integer[]{Integer.valueOf(6)});
//                        return;
//                    }
//                    str = "";
//                    intent = new Intent(getContext(), PageNewThread.class);
//                    bundle = new Bundle();
//                    if (this.iPostType != 0) {
//                        first = doc.select("textarea[name=message]").first();
//                        if (first != null) {
//                            str = ((TextNode) first.textNodes().get(0)).getWholeText();
//                        }
//                    }
//                    bundle.putString(NotificationCompat.CATEGORY_MESSAGE, str);
//                    bundle.putString(MySQLiteHelper.COLUMN_TITLE, this.mTextTitle);
//                    bundle.putFloat("textsize", this.mTextSize);
//                    bundle.putInt("POSTTYPE", this.iPostType);
//                    intent.putExtras(bundle);
//                    startActivityForResult(intent, this.iPostType);
                    break;
                }
                case 4: {
//                    Element r0 = doc.select("table[class=tborder]:contains(The following errors occurred)").first();
//                    if (r0 == null || !r0.text().contains("To be able to post links or images your post count")) {
//                        parseDataPage3(0, doc, doc2, true);
//                        return;
//                    }
//                    try {
//                        toast(r0.text());
//                        String obj = "\n\n[I]Sent from my " + Global.sYourDevice + " using [URL=\"https://play.google.com/store/apps/details?id=com.nna88.voz.main\"]vozForums[/URL]" + "[/I]";
//                        if (this.sPost.contains(obj)) {
//                            this.sPost = this.sPost.substring(0, this.sPost.indexOf(obj));
//                            this.sPost += "\n\n[I]Sent from my " + Global.sYourDevice + " using vozForums" + "[/I]";
//                            toast("Try Again");
//                            TaskCancle();
//                            this.mTask = new TaskGetHtml();
//                            this.mTask.execute(new Integer[]{Integer.valueOf(4)});
//                            return;
//                        }
//                        this.iPostType = 1;
//                        str = BuildConfig.FLAVOR;
//                        intent = new Intent(getContext(), PageNewThread.class);
//                        bundle = new Bundle();
//                        if (this.iPostType != 0) {
//                            first = doc.select("textarea[name=message]").first();
//                            if (first != null) {
//                                str = ((TextNode) first.textNodes().get(0)).getWholeText();
//                            }
//                        }
//                        bundle.putString(NotificationCompat.CATEGORY_MESSAGE, str);
//                        bundle.putString(MySQLiteHelper.COLUMN_TITLE, this.mTextTitle);
//                        bundle.putFloat("textsize", this.mTextSize);
//                        bundle.putInt("POSTTYPE", this.iPostType);
//                        intent.putExtras(bundle);
//                        startActivityForResult(intent, this.iPostType);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                    break;
                }

                default: {
                    Post post;
                    ArrayList<Post> mListPost = new ArrayList<>();
                    parsePage(0, 0, doc);
                    Element element = doc.select("div[align=center]").size() > 1 ? (Element) doc.select("div[align=center]").get(1) : doc;
                    Element r0 = element.select("a[href=private.php]").first();
                    mPostId = getPostIdFromUrl(mUrl);
                    if (r0 != null) {
                        mUser.setUserId(((Element) element.select("td[class=alt2]").get(0)).select("a[href*=mem]").attr("href").split("=")[1]);
                        r0 = element.select("input[name*=securitytoken]").first();
                        if (r0 != null) {
                            mUser.setToken(r0.attr("value"));
                        }
                        r0 = doc.select("div[style=margin-top:6px]:has(input[value=Post Quick Reply])").first();
                        if (r0 != null) {
                            String sIdThread = r0.select("input[name=t]").attr("value");
                        }
                        r0 = doc.select("td[class=alt1]:has(a[href *= subscription.php]").first();
//                        if (r0 != null) {
//                            if (r0.select("a[href *= subscription.php]").attr("href").contains("removesubscription")) {
//                                this.isSubscribe = true;
//                                ((SidebarAdapter.Item) this.mListSideMenu2.get(3)).mTitle = getResources().getString(R.string.UnSubscribe);
//                                this.mAdapterSideMenu2.notifyDataSetInvalidated();
//                            } else {
//                                ((SidebarAdapter.Item) this.mListSideMenu2.get(3)).mTitle = getResources().getString(R.string.Subscribe);
//                                this.mAdapterSideMenu2.notifyDataSetInvalidated();
//                                this.isSubscribe = false;
//                            }
//                        }
                    }
                    Elements select = element.select("td[id*=td_post]");
                    Iterator it = select.iterator();
                    int iPositiion = 0;
                    String str2 = null;
                    while (it.hasNext()) {
                        String text;
                        String str3 = null;
                        post = new Post();
                        String str4 = BuildConfig.FLAVOR;
                        Element element2 = (Element) it.next();
                        Element parent = element2.parent();
                        if (select.select("div[class=smallfont]:has(strong)").first() != null) {
//                            this.mTextTitle = select.select("div[class=smallfont]:has(strong)").first().text();
//                            setTitle(this.mTextTitle);
                        }
                        Element previousElementSibling = parent.previousElementSibling();
                        Element previousElementSibling2 = previousElementSibling.previousElementSibling();
                        if (previousElementSibling.select("img[src*=avatars]").first() != null) {
                            str3 = previousElementSibling.select("img[src*=avatars]").attr("src");
                            if (!str3.contains(Global.URL)) {
                                str3 = Global.URL + str3;
                            }
                        }
                        if (previousElementSibling.select("div:containsOwn(Join Date)").first() != null) {
                            text = previousElementSibling.select("div:containsOwn(Join Date)").first().text();
                            if (text.contains("Date:")) {
                                text = text.split("Date:")[1];
                            }
                            post.setJD("Jd:" + text);
                        } else {
                            post.setJD(BuildConfig.FLAVOR);
                        }
                        if (previousElementSibling.select("div:containsOwn(Posts: )").first() != null) {
                            post.setPosts(previousElementSibling.select("div:containsOwn(Posts: )").first().text());
                        } else {
                            post.setPosts(BuildConfig.FLAVOR);
                        }
                        if (previousElementSibling.select("img[src*=line.gif").first() != null) {
                            if (previousElementSibling.select("img[src*=line.gif").attr("src").contains("online")) {
                                post.isOnline = true;
                            } else {
                                post.isOnline = false;
                            }
                        }
                        if (previousElementSibling.select("a[class=bigusername]").first() != null) {
                            text = previousElementSibling.select("a[class=bigusername]").text();
                            post.m_UserId = previousElementSibling.select("a[class=bigusername]").first().attr("href").split("u=")[1];
                        } else {
                            text = str2;
                        }
                        str2 = previousElementSibling.select("div[class=smallfont]").first() != null ? previousElementSibling.select("div[class=smallfont]").first().text() : str4;
                        str4 = previousElementSibling2.text();
                        previousElementSibling = element2.select("div[id*=post_message").first();
                        if (previousElementSibling != null) {
                            if (previousElementSibling.attr("id").split("_").length > 2) {
                                post.setId(previousElementSibling.attr("id").split("_")[2]);
                            }
                            parsePage3(previousElementSibling, post, false);
                        }
                        previousElementSibling = element2.select("fieldset[class=fieldset]").first();
                        if (previousElementSibling != null) {
                            post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                            parsePage3(previousElementSibling, post, false);
                        }
                        post.Info(text, str2, str4, null, str3);
                        if (Global.bSign && element2.select("div:contains(_______)").first() != null) {
                            post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                            parsePage3(element2.select("div:contains(_______)").first(), post, false);
                        }
                        post.initContent();
                        mListPost.add(post);
                        if (post.Id().equals(mPostId)) {
                            iPositiion = mListPost.indexOf(post) + 1;
                            str2 = text;
                        } else {
                            str2 = text;
                        }
                    }
//                    mItemCount = 23;
//                    if (mItemOffsetY == null) {
//                        this.mItemOffsetY = new int[(this.mItemCount + 1)];
//                        this.mItemtemp = new int[(this.mItemCount + 1)];
//                    }
//                    this.scrollIsComputed = true;
                    mMapPostPerPage.put(Integer.valueOf(curPage), mListPost);
                }

                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        setProgress(false);
//                        mPage3PagerAdapter.setTotalPage(mTotalPage);
//                        refreshCurrentPage(mCurPage, false);
                        notifyDataSetChanged();
                    }
                });


            }
        }
    }

//    private void refreshCurrentPage(final int curPage, final boolean forceRefresh) {
//        try {
//            Log.i(TAG, "Refresh page :  " + curPage);
//            View page = mViewPager.findViewWithTag(curPage);
//            if (page != null) {
//                ListView listView = (ListView) page.findViewById(R.id.content_frame);
//                if (forceRefresh) {
//                    if (listView.getAdapter() != null && mMapPostPerPage.containsKey(Integer.valueOf(mCurPage))) {
//                        final Page3ListViewAdapter adapter = new Page3ListViewAdapter(getActivity(), mMapPostPerPage.get(Integer.valueOf(mCurPage)), null, bmImageStart, mTextSize);
//                        View header = listView.findViewWithTag("Header");
//                        listView.removeHeaderView(header);
//                        View footer = listView.findViewWithTag("Footer");
//                        listView.removeFooterView(footer);
//
//                        listView.addHeaderView(getNavigationView(mCurPage, "Header"));
//                        listView.addFooterView(getNavigationView(mCurPage, "Footer"));
//                        listView.setAdapter(adapter);
//
//                        mPage3PagerAdapter.notifyDataSetChanged();
//                    }
//                } else {
//                    (page.findViewById(R.id.layout_progress)).setVisibility(View.GONE); // gone progress
//                    if (listView.getAdapter() == null && mMapPostPerPage.containsKey(Integer.valueOf(curPage))) {
//                        final Page3ListViewAdapter adapter = new Page3ListViewAdapter(getActivity(), mMapPostPerPage.get(Integer.valueOf(curPage)), null, bmImageStart, mTextSize);
//                        listView.addHeaderView(getNavigationView(curPage, "Header"));
//                        listView.addFooterView(getNavigationView(curPage, "Footer"));
//                        listView.setAdapter(adapter);
//                        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//                            @Override
//                            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                                //                        if (scrollState == SCROLL_STATE_IDLE) {
//                                //                            adapter.setIsScrolling(false);
//                                //                            adapter.notifyDataSetChanged();
//                                //                        } else {
//                                //                            adapter.setIsScrolling(true);
//                                //                        }
//                            }
//
//                            @Override
//                            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//                            }
//                        });
//                        mPage3PagerAdapter.notifyDataSetChanged();
//                    }
//
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    String parsePage(int action, int i2, Document doc) throws Exception {
//        if (doc == null) {
//            return null;
//        }
        String attr;
        String text = "zzz";
        if (doc != null) {
            Element first = doc.select("div[class=pagenav").first();
            if (first == null) {
                return null;
            }

            text = first.select("td[class=vbmenu_control]").text();
            loadPageIndex = Integer.parseInt(text.split(" ")[1]);
            mTotalPage = Integer.parseInt(text.split(" ")[3]);
            if (mUrl.contains("showthread.php?p=")) {
                int size = first.getElementsByAttribute("href").size();
                int i3 = 0;
                while (i3 < size) {
                    Element element = (Element) first.getElementsByAttribute("href").get(i3);
                    if (element != null) {
                        attr = element.attr("href");
                        if (!attr.contains("&page=")) {
                            attr = attr.concat("&page=1");
                        }
                        attr = attr.substring(attr.indexOf("?t=") + 3, attr.indexOf("&page"));
                        attr = "https://vozforums.com/showthread.php?t=" + attr + "&page=" + String.valueOf(loadPageIndex);
                    } else {
                        i3 += 1;
                    }
                }
                attr = null;
                attr = "https://vozforums.com/showthread.php?t=" + attr + "&page=" + String.valueOf(loadPageIndex);
            } else {
                attr = mUrl;
                if (!attr.contains("&page=")) {
                    attr = attr.concat("&page=1");
                }
            }
        } else {
            attr = mUrl;
            if (!attr.contains("&page=")) {
                attr = attr.concat("&page=1");
            }
        }

        switch (action) {
            case 0:
                String[] split = text.split(" ");
                return split[1] + "/" + split[3];
//            case 1:
//                return attr.split("&page")[0].concat("&page=" + String.valueOf(i2));
//            case Page3Fragment.GO_NEXT:
//                mCurPage++;
//                return attr.substring(0, attr.lastIndexOf("=") + 1).concat(String.valueOf(mCurPage));
//            case Page3Fragment.GO_PREVIOUS:
//                mCurPage--;
//                String concat = attr.substring(0, attr.lastIndexOf("=") + 1).concat(String.valueOf(mCurPage));
//                return concat.contains("&page=0") ? concat.split("&page")[0] : concat;
//            case Page3Fragment.GO_FIRST:
//                mCurPage = 1;
//                return attr.split("&page")[0];
//            case Page3Fragment.GO_LAST:
//                mCurPage = mTotalPage;
//                return attr.split("&page")[0].concat("&page=" + String.valueOf(mTotalPage));
            default:
                return null;

        }
    }

    private void parsePage3(Element element, Post post, boolean z) {
        if (element != null) {
            for (Node node : element.childNodes()) {
                if (node instanceof Element) {
                    Element first;
                    int length;
                    int length2;
                    if (((Element) node).tagName().equals("div")) {
                        first = ((Element) node).select("div").first();
                        if (first.attr("style").contains("padding")) {
                            post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                        }
                        if (first.ownText().contains("Originally Posted by")) {
                            post.addText("Originally Posted by ");
                            length = post.getText().length();
                            post.addText(first.select("strong").text());
                            length2 = post.getText().length();
                            post.web.add(Global.URL + first.select("a").attr("href"), length, length2);
                            post.type.add("", length, length2, 1);
                            post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                        } else {
                            parsePage3(first, post, z);
                            post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                        }
                    } else if (((Element) node).tagName().equals("blockquote")) {
                        first = ((Element) node).select("blockquote").first();
                        post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                        parsePage3(first, post, z);
                        post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                    } else if (((Element) node).tagName().equals("fieldset")) {
                        first = ((Element) node).select("fieldset").first();
                        post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                        parsePage3(first, post, z);
                        post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                    } else if (((Element) node).tagName().equals("b")) {
                        first = ((Element) node).select("b").first();
                        length = post.getText().length();
                        parsePage3(first, post, z);
                        post.type.add("", length, post.getText().length(), 1);
                    } else if (((Element) node).tagName().equals("i")) {
                        first = ((Element) node).select("i").first();
                        length = post.getText().length();
                        parsePage3(first, post, z);
                        post.type.add("", length, post.getText().length(), 2);
                    } else if (((Element) node).tagName().equals("pre")) {
                        first = ((Element) node).select("pre").first();
                        length = post.getText().length();
                        parsePage3(first, post, z);
                        post.quote.add("", length, post.getText().length());
                    } else if (((Element) node).tagName().equals("table")) {
                        first = ((Element) node).select("table").first();
                        length = post.getText().length();
                        parsePage3(first, post, z);
                        post.quote.add("", length, post.getText().length());
                    } else if (((Element) node).tagName().equals("ol")) {
                        parsePage3(((Element) node).select("ol").first(), post, z);
                    } else if (((Element) node).tagName().equals("tbody")) {
                        parsePage3(((Element) node).select("tbody").first(), post, z);
                    } else if (((Element) node).tagName().equals("li")) {
                        parsePage3(((Element) node).select("li").first(), post, z);
                    } else if (((Element) node).tagName().equals("tr")) {
                        first = ((Element) node).select("tr").first();
                        post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                        parsePage3(first, post, z);
                    } else if (((Element) node).tagName().equals("td")) {
                        parsePage3(((Element) node).select("td").first(), post, z);
                    } else if (((Element) node).tagName().equals("img")) {
                        String r0;
                        r0 = ((Element) node).select("img[src]").attr("src");
                        if (r0.contains(Global.URL) && r0.subSequence(0, 21).equals(Global.URL) && !r0.contains("https://vozforums.com/attachment.php?attachmentid") && !r0.contains("https://vozforums.com/customavatars/")) {
                            r0 = r0.substring(21);
                        }
                        if (r0.substring(0, 1).equals("/")) {
                            r0 = r0.substring(1, r0.length());
                        }
                        length = post.getText().length();
                        length2 = r0.length();
                        if (r0.contains("http://") || r0.contains("https://") || r0.contains("attachment.php?attachmentid")) {
                            post.image.add(r0, length, length2 + length, null);
                        } else {
                            post.image.add(r0, length, length + 2, null);
                            r0 = "  ";
                        }
                        if (!r0.contains("images/buttons/viewpost.gif")) {
                            post.addText(r0);
                            if (node.hasAttr("onload")) {
                                post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                            }
                        }
                    } else if (((Element) node).tagName().equals("br")) {
                        post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                    } else if (((Element) node).tagName().equals("u")) {
                        first = ((Element) node).select("u").first();
                        length = post.getText().length();
                        parsePage3(first, post, z);
                        post.typeU.add("", length, post.getText().length());
                    } else if (((Element) node).tagName().equals("font")) {
                        Element first2 = ((Element) node).select("font").first();
                        String str = "while";
                        String r1 = "3";
                        if (((Element) node).select("font[color]").first() != null) {
                            str = ((Element) node).select("font[color]").attr("color");
                        }
                        String attr = ((Element) node).select("font[size]").first() != null ? ((Element) node).select("font[size]").attr("size") : r1;
                        length2 = post.getText().length();
                        parsePage3(first2, post, z);
                        post.font.add("", length2, post.getText().length(), str, Integer.parseInt(attr));
                    } else if (((Element) node).tagName().equals("a")) {
                        first = ((Element) node).select("a[href]").first();
                        if (first.select("img").first() == null) {
                            String r0;
                            r0 = ((Element) node).select("a[href]").attr("href").replace("%3A", ":").replace("%2F", "/").replace("%3F", "?").replace("%3D", "=").replace("%26", "&");
                            if (r0.contains("mailto:")) {
                                String r1 = r0.substring(7, r0.length());
                                r0 = ((Element) node).select("a[href]").text();
                                length2 = post.getText().length();
                                post.web.add(r1, length2, r0.length() + length2);
                                post.addText(r0);
                            } else if (r0.contains("http")) {
                                String r1 = r0.substring(r0.indexOf("http"), r0.length());
                                r0 = ((Element) node).select("a[href]").text();
                                length2 = post.getText().length();
                                post.web.add(r1, length2, r0.length() + length2);
                                post.addText(r0);
                            }
                        } else {
                            parsePage3(first, post, true);
                        }
                    } else {
                        post.addText(((Element) node).text());
                    }
                }
                if (node instanceof TextNode) {
                    if (z) {
                        post.addText(((TextNode) node).getWholeText());
                    } else {
                        post.addText(((TextNode) node).text());
                    }
                }
            }
        }
//        post.addText(element.text());
    }

    private String getPostIdFromUrl(String str) {
        String str2 = null;
        try {
            if (str.contains("p=")) {
                str2 = str.split("p=")[1];
            }
            return str2.contains("#post") ? str2.split("#post")[1] : str2;
        } catch (Exception e) {
            return null;
        }
    }

    public int getCurPage() {
        return mCurPage;
    }

    public int getTotalPage() {
        return mTotalPage;
    }
}