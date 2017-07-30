package com.whoami.voz.ui.pageposts;

import com.whoami.voz.retrofit.VozService;
import com.whoami.voz.retrofit.data.PostData;
import com.whoami.voz.ui.activity.BaseActivity;
import com.whoami.voz.ui.contain.VozPost;
import com.whoami.voz.ui.utils.ClipboardUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by thaidh on 5/2/17.
 */

public class PagePostPresenter implements PagePostContract.Presenter {

    private PagePostContract.View mPagePostView;

    private Map<Integer, PostData> mMapPostPerPage = new LinkedHashMap() {
        public boolean removeEldestEntry(Map.Entry eldest) {
            return size() > BaseActivity.MAX_ENTRIES;
        }
    };
    private String mUrl;
    private int mTotalPage;
    private String mTitle;
    private int mCurPage;

    public PagePostPresenter(PagePostContract.View mPagePostView, String mUrl) {
        this.mPagePostView = mPagePostView;
        this.mUrl = mUrl;
    }

    @Override
    public void start() {

    }

    public void loadPage(final int curPage, final boolean refres) {
        try {
            mCurPage = curPage;
            if (refres || !mMapPostPerPage.containsKey(Integer.valueOf(curPage))) {
                final String url = getUrlWithPage(curPage);
                if (url != null) {
                    VozService.getInstance().getPosts(url).enqueue(new Callback<PostData>() {
                        @Override
                        public void onResponse(Call<PostData> call, Response<PostData> response) {
                            if (response.isSuccessful()) {
                                PostData data = response.body();
                                mTotalPage = data.totalPage;
                                mTitle = data.title;
                                mMapPostPerPage.put(Integer.valueOf(curPage), data);
                                mPagePostView.refreshCurrentPage(mTotalPage, curPage, refres, data);
                            }
                        }

                        @Override
                        public void onFailure(Call<PostData> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            } else {
                mPagePostView.refreshCurrentPage(mTotalPage, curPage, false, mMapPostPerPage.get(curPage));
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

    @Override
    public void copyCurrentUrlToClipboard() {
        ClipboardUtils.copyText(getUrlWithPage(mCurPage));
    }
}
