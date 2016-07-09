package com.whoami.voz.ui.utils;

import android.util.Log;

import com.whoami.voz.ui.main.Global;
import com.whoami.voz.ui.parserhtml.HtmlParser;
import com.whoami.voz.ui.worker.FetchHtmlTask;
import com.whoami.voz.ui.worker.FetchHtmlWorker;

import org.jsoup.nodes.Document;

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

    public void fetchData(String url, final int curPage, final HtmlLoaderListener listener) {
        Log.e(HtmlLoader.class.getSimpleName(),"get url: " + url);
        mParser.setUrl(url);
        FetchHtmlWorker.queueTask(new FetchHtmlTask() {
            @Override
            public void doTask() {
                Document doc = mParser.getDoc();
                if (listener != null) {
                    listener.onCallback(doc, curPage);
                }
            }
        });
    }

    public interface HtmlLoaderListener {
        void onCallback(Document doc, int curPage);
    }
}
