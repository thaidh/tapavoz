package com.whoami.voz.ui.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nna88.voz.main.Global;
import com.nna88.voz.parserhtml.HtmlParser;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;

/**
 * Created by JonSnow on 2/13/2016.
 */
public class HtmlLoader {
    public static final int REFRESH_INDEX_PAGE = 999;
    private HtmlParser mParser;
    private static HtmlLoader ourInstance = new HtmlLoader();

    public static HtmlLoader getInstance() {
        return ourInstance;
    }

    private HtmlLoader() {
        mParser = new HtmlParser(Global.URL);
    }

    public void fetchData(String url, HtmlLoaderListener listener) {
        Log.e(HtmlLoader.class.getSimpleName(),"get url: " + url);
        mParser.setUrl(url);
        HtmlGetter htmlGetter = new HtmlGetter(listener);
        htmlGetter.execute(0);
    }


    public class HtmlGetter extends AsyncTask<Integer, Void, Integer> {
        private Document doc;
        private Document doc2;
        private HtmlLoaderListener htmlLoaderListener;

        HtmlGetter(HtmlLoaderListener listener) {
            htmlLoaderListener = listener;
        }

        protected Integer doInBackground(Integer... numArr) {
            Element element;
            Element first;
            Element first2;
            String attr;
            String attr2;
            switch (numArr[0].intValue()) {
                case 0:
                    try {
                        if (!Global.OFFILNE.booleanValue()) {
                            doc = mParser.getDoc();
                        } else {
                            doc = Jsoup.parse(FileUtils.readFileToString(new File("/mnt/sdcard/paga3.html"), AsyncHttpResponseHandler.DEFAULT_CHARSET));
                        }
                        if (htmlLoaderListener != null) {
                            htmlLoaderListener.onCallback(doc);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case REFRESH_INDEX_PAGE:
                    try {
//                        doc = mParser.getDocWithUrl(getUrlWithPage(mCurPage));
//                        parseData(0, doc, doc2, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return REFRESH_INDEX_PAGE;

//                case 1:
//                    doc = mParser.login(mUser);
//                    break;
//                case 2:
//                    doc = mParser.Logout();
//                    break;
//                case 3:
//                    doc = mParser.quoteGet(numArr[Page.STATE_OFFSCREEN].intValue(), numArr[Page.STATE_RETURNING].intValue());
//                    break;
//                case 4:
//                    try {
//                        doc = mParser.PostReplyQuote(doc2, mUser, sPost, specifiedpost);
//                        break;
//                    } catch (Exception e2) {
//                        break;
//                    }
//                case 5:
//                    try {
//                        element = (Element) doc2.select("div[align=center]").get(Page.STATE_OFFSCREEN);
//                        first = element.select("input[name*=securitytoken]").first();
//                        first2 = element.select("input[name*=posthash]").first();
//                        Element first3 = element.select("input[name*=poststarttime]").first();
//                        Element first4 = element.select("input[name=p]").first();
//                        element = element.select("input[name=title]").first();
//                        attr = first != null ? first.attr("value") : null;
//                        String attr3 = first2 != null ? first2.attr("value") : null;
//                        String attr4 = first3 != null ? first3.attr("value") : null;
//                        attr2 = first4 != null ? first4.attr("value") : null;
//                        doc = mParser.PostEditReply(Page.STATE_ONSCREEN, sPost, "editpost.php?do=updatepost&p=" + attr2, element != null ? element.attr("value") : null, attr, attr2, attr3, attr4);
//                        break;
//                    } catch (Exception e3) {
//                        break;
//                    }
//                case 6:
//                    try {
//                        element = (Element) doc2.select("div[align=center]").get(Page.STATE_OFFSCREEN);
//                        first = element.select("input[name*=securitytoken]").first();
//                        first2 = element.select("input[name=p]").first();
//                        element = element.select("input[name=title]").first();
//                        attr = first != null ? first.attr("value") : null;
//                        attr2 = first2 != null ? first2.attr("value") : null;
//                        doc = mParser.PostEditReply(Page.STATE_OFFSCREEN, sPost, "editpost.php?do=deletepost&p=" + attr2, element != null ? element.attr("value") : null, attr, attr2, null, null);
//                        break;
//                    } catch (Exception e4) {
//                        break;
//                    }
//                case 7:
//                    element = doc.select("input[name*=securitytoken]").first();
//                    doc = mParser.Subscribe(sIdThread, element != null ? element.attr("value") : null);
//                    break;
//                case 8:
//                    doc = mParser.UnSubscribe(sIdThread);
//                    break;
//                case 11:
//                    doc = mParser.myRecentPost();
//                    break;
//                case 12:
//                    doc = mParser.myRecentThread();
//                    break;
//                case 13:
//                    doc = mParser.AdvanceSearch(mUser.Token(), sMessSearch, sForum, sSearch_ShowPost);
//                    break;
//                case 14:
//                    doc = mParser.Search(mUser.Token(), sMessSearch, sSearch_ShowPost);
//                    break;
//                case 15:
//                    doc = mParser.getPM();
//                    break;
            }
            return numArr[0];
        }

        protected void onPostExecute(Integer num) {
            try {

                //todo handle page 3
//                setProgress(false);
//                if (num == REFRESH_INDEX_PAGE) {
//                    mPage3PagerAdapter.setTotalPage(mTotalPage);
//                    refreshCurrentPage(mCurPage, true);
//                } else {
//                    parseData(num.intValue(), doc, doc2, true);
//                }
                //todo =============


//                switch (num.intValue()) {
//                    case 1:
//                        if (doc != null) {
//                            writeSetingUser(mUser.cookies());
//                            mUser.setLogin(true);
//                            ((SidebarAdapter.Item) mListSideMenu2.get(Page.STATE_ONSCREEN)).mTitle = mUser.User();
//                            mUser.setAvatart(null);
//                            mAdapterSideMenu2.notifyDataSetInvalidated();
//                        } else {
//                            toast("T\u00ean \u0111\u0103ng nh\u1eadp ho\u1eb7c m\u1eadt kh\u1ea9u sai!");
//                            alertLogin();
//                        }
//                        break;
//                    case 2:
//                        UserInfo.mCookieStore = null;
//                        mUser.cookies(null);
//                        mUser.setLogin(false);
//                        mUser.setAvatart(null);
//                        mUser.SetUser(null);
//                        ((SidebarAdapter.Item) mListSideMenu2.get(Page.STATE_ONSCREEN)).mTitle = getResources().getString(R.string.Login);
//                        mAdapterSideMenu2.notifyDataSetInvalidated();
//                        writeSetingUser(mUser.cookies());
//                        break;
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            super.onPostExecute(num);
        }

        protected void onPreExecute() {
            super.onPreExecute();
//            setProgress(true);
        }
    }





    public interface HtmlLoaderListener {
        void onCallback(Document doc);
    }
}
