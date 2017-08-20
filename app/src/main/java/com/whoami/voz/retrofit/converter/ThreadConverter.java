package com.whoami.voz.retrofit.converter;

import android.graphics.Bitmap;

import com.whoami.voz.BuildConfig;
import com.whoami.voz.retrofit.data.ThreadData;
import com.whoami.voz.contain.VozThread;
import com.whoami.voz.main.Global;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Iterator;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by thaidh on 6/26/17.
 */

public class ThreadConverter implements Converter<ResponseBody, ThreadData> {
    @Override
    public ThreadData convert(ResponseBody responseBody) throws IOException {
        Document doc = Jsoup.parse(responseBody.string());
        ThreadData data = new ThreadData();

        Element first = doc.select("a[href=private.php]").first();
        //todo set page
        //parse page
        Element firstElement = doc.select("div[class=pagenav]").first();
        if (firstElement !=null) {
            String strPage = firstElement.select("td[class=vbmenu_control]").text();
            data.totalPage = Integer.parseInt(strPage.split(" ")[3]);
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
            return data;
        }
        Element first2 = first.select("td[class=tcat]:contains(Threads in Forum)").first();
        if (first2 != null) {
//                String mTitle = first2.select("span").text();
//                mTitle = mTitle.substring(2, mTitle.length());
//                toolbar.setTitle(mTitle);
        }
        first2 = first.select("tbody[id*=threadbits_forum]").first();
        if (first2 == null) {
            return data;
        }
        Bitmap loadBitmapAssert = null;
        String attr;
        String lastPostInfo;
        Elements select = first2.select("tr:has(td[id*=td_threads])");
        Elements select2 = first.select("tr > td[class=alt1Active]");
        Iterator it = select.iterator();
        Iterator it2 = select2.iterator();
        if (it2.hasNext()) {
            data.threadList.add(new VozThread("Sub-Forum", null, null, null, null, null, null));
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
//            loadBitmapAssert = (!Global.bTopicHeader || first3 == null) ? bitmap : mPageThreadsView.loadBitmapAssert(first3.select("img").attr("src"));
            if (first != null) {
                str2 = first.text();
            }
            attr = first.select("div > a").first() != null ? first.select("div > a").first().attr("href") : str3;
            if (nextElementSibling.select("span > a").first() != null) {
                lastPostInfo = nextElementSibling.select("span > a").first().text();
                str = nextElementSibling.select("span > a").first().attr("href");
            } else {
                lastPostInfo = str4;
            }

            if (!(nextElementSibling2 == null || nextElementSibling3 == null)) {
                lastPostInfo = lastPostInfo + "\nReplie:" + nextElementSibling2.text() + " - View:" + nextElementSibling3.text();
            }
            data.threadList.add(new VozThread(str2, lastPostInfo, null, null, null/*loadBitmapAssert*/, attr, str));
            bitmap = loadBitmapAssert;
            str3 = attr;
            str4 = lastPostInfo;
        }
        data.threadList.add(new VozThread("Thread", null, null, null, null, null, null));
        lastPostInfo = null;
        loadBitmapAssert = bitmap;
        attr = str3;
        String str5 = str4;
        while (it.hasNext()) {
            String attr2;
            str = null;
            str2 = BuildConfig.FLAVOR;
            Element element = (Element) it.next();
            Element first4 = element.select("a[id*=thread_title]").first();
            if (first4 != null) {
                if (element.select("a[href*=prefixid]").first() != null) {
                    str2 = element.select("a[href*=prefixid]").first().text() + "-";
                    attr2 = element.select("a[href*=prefixid]").attr("href");
                } else {
                    attr2 = lastPostInfo;
                }
                str2 = str2 + element.select("a[id*=thread_title]").first().text();
                attr = element.select("a[id*=thread_title]").first().attr("href");
                str3 = attr2;
            } else {
                str3 = lastPostInfo;
            }
            attr2 = element.select("td[class=alt2]:has(span)").first() != null ? element.select("td[class=alt2]:has(span)").first().text() : str5;
            String replyCount = "";
            String viewCount = "";
            if (element.select("td[align=center]").first() != null) {
                replyCount = ((Element) element.select("td[align=center]").get(0)).text();
                viewCount = ((Element) element.select("td[align=center]").get(1)).text();
//                    attr2 = attr2 + "\nReplies:" + ((Element) element.select("td[align=center]").get(0)).text() + " - Views:" + ((Element) element.select("td[align=center]").get(1)).text();
            }
            lastPostInfo = element.select("div:has(span[onclick*=member.php])").first() != null ? element.select("div:has(span[onclick*=member.php])").first().text() + " - " + attr2 : attr2;
            if (Global.bTopicHeader && element.select("img[id*=thread_statusicon]").first() != null) {
//                loadBitmapAssert = mPageThreadsView.loadBitmapAssert(element.select("img[id*=thread_statusicon]").first().attr("src"));
            }
            if (element.select("a[id*=thread_gotonew]").first() != null) {
                str = element.select("a[id*=thread_gotonew]").first().attr("href");
            }
            VozThread vozThread = new VozThread(str2, lastPostInfo, replyCount, viewCount, loadBitmapAssert, attr, str);
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
            data.threadList.add(vozThread);
            str5 = lastPostInfo;
            lastPostInfo = str3;
        }
        return data;
    }
}
