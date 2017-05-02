package com.whoami.voz.ui.pagethreads;

import android.graphics.Bitmap;

import com.whoami.voz.BuildConfig;
import com.whoami.voz.ui.activity.BaseActivity;
import com.whoami.voz.ui.contain.VozThread;
import com.whoami.voz.ui.main.Global;
import com.whoami.voz.ui.utils.HtmlLoader;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by thaidh on 5/1/17.
 */

public class PageThreadsPresenter implements PageThreadsContract.Presenter {
    private Map<Integer, ArrayList<VozThread>> mMapPostPerPage = new LinkedHashMap() {
        public boolean removeEldestEntry(Map.Entry eldest) {
            return size() > BaseActivity.MAX_ENTRIES;
        }
    };

    public PageThreadsContract.View mPageThreadsView;

    private String mUrl;
    private int mTotalPage = 1;

    @Override
    public void start() {

    }

    public PageThreadsPresenter(PageThreadsContract.View pageThreadsView, String url) {
        this.mPageThreadsView = pageThreadsView;
        this.mUrl = url;
    }

    @Override
    public void loadPage(int curPage, boolean refresh) {
        try {
            if (refresh || !mMapPostPerPage.containsKey(Integer.valueOf(curPage))) {
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
                mPageThreadsView.refreshCurrentPage(mTotalPage,curPage, false ,mMapPostPerPage.get(curPage));
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

    void parseDataPage2(Document doc, final int curPage) throws Exception {
        if (doc != null) {
            ArrayList<VozThread> vozThreads = new ArrayList<>();
            vozThreads.clear();
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
                vozThreads.add(new VozThread("Sub-Forum", null, null, null, null, null, null));
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
                loadBitmapAssert = (!Global.bTopicHeader || first3 == null) ? bitmap : mPageThreadsView.loadBitmapAssert(first3.select("img").attr("src"));
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
                vozThreads.add(new VozThread(str2, text, null, null, loadBitmapAssert, attr, str));
                bitmap = loadBitmapAssert;
                str3 = attr;
                str4 = text;
            }
            vozThreads.add(new VozThread("Thread", null, null, null, null, null, null));
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
                    loadBitmapAssert = mPageThreadsView.loadBitmapAssert(element.select("img[id*=thread_statusicon]").first().attr("src"));
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
                vozThreads.add(vozThread);
                str5 = text;
                text = str3;
            }
            mMapPostPerPage.put(Integer.valueOf(curPage), vozThreads);
            mPageThreadsView.refreshCurrentPage(mTotalPage, curPage, true, vozThreads);
        }
    }
}
