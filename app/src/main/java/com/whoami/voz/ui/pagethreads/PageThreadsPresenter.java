package com.whoami.voz.ui.pagethreads;

import com.whoami.voz.retrofit.VozService;
import com.whoami.voz.retrofit.data.ThreadData;
import com.whoami.voz.ui.activity.BaseActivity;
import com.whoami.voz.ui.contain.VozThread;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public void loadPage(final int curPage, boolean refresh) {
        try {
            if (refresh || !mMapPostPerPage.containsKey(Integer.valueOf(curPage))) {
                String url = getUrlWithPage(curPage);
                if (url != null) {
                    VozService.getInstance().getThreads(url).enqueue(new Callback<ThreadData>() {
                        @Override
                        public void onResponse(Call<ThreadData> call, Response<ThreadData> response) {
                            if (response.isSuccessful()) {
                                ThreadData data = response.body();
                                mTotalPage = data.totalPage;
                                mMapPostPerPage.put(Integer.valueOf(curPage), data.threadList);
                                mPageThreadsView.refreshCurrentPage(mTotalPage, curPage, true, data.threadList);
                            }
                        }

                        @Override
                        public void onFailure(Call<ThreadData> call, Throwable t) {
                            t.printStackTrace();
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
}
