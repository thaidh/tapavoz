package com.whoami.voz.ui.utils;

import android.util.Log;

import com.whoami.voz.retrofit.VozApi;
import com.whoami.voz.retrofit.VozService;
import com.whoami.voz.ui.main.Global;
import com.whoami.voz.ui.parserhtml.HtmlParser;
import com.whoami.voz.ui.worker.FetchHtmlTask;
import com.whoami.voz.ui.worker.FetchHtmlWorker;

import org.jsoup.nodes.Document;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by JonSnow on 2/13/2016.
 */
public class HtmlLoader {
    public static final int REFRESH_INDEX_PAGE = 999;
    private static HtmlLoader ourInstance = new HtmlLoader();

    private VozApi mVozService;

    public static HtmlLoader getInstance() {
        return ourInstance;
    }

    private HtmlLoader() {
//        mParser = new HtmlParser(Global.URL);
        mVozService = VozService.createGithubService("");
    }

    public void fetchData(final String url, final int curPage, final HtmlLoaderListener listener) {
        Log.e(HtmlLoader.class.getSimpleName(),"get url: " + url);
        FetchHtmlWorker.queueTask(new FetchHtmlTask() {
            @Override
            public void doTask() {
                mVozService.getHomePage(url).enqueue(new Callback<Document>() {
                    @Override
                    public void onResponse(Call<Document> call, Response<Document> response) {
//                        Log.i("AAAAAAAA", "data: " + response.body());
//                        Document doc =  Jsoup.parse(response.body());
                        if (listener != null) {
                            listener.onCallback(response.body(), curPage);
                        }
                    }

                    @Override
                    public void onFailure(Call<Document> call, Throwable t) {
                        Log.e("AAAAAAAA", "data: " + call.toString());
                    }
                });

            }
        });
    }


    public void login() {
        FetchHtmlWorker.queueTask(new FetchHtmlTask() {
            @Override
            public void doTask() {
            }
        });
    }

    public interface HtmlLoaderListener {
        void onCallback(Document doc, int curPage);
    }
}
