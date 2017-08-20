package com.whoami.voz.pagethreads;

import android.graphics.Bitmap;

import com.whoami.voz.BasePresenter;
import com.whoami.voz.BaseView;
import com.whoami.voz.contain.VozThread;

import java.util.ArrayList;

/**
 * Created by thaidh on 5/1/17.
 */

public interface PageThreadsContract  {
    interface View extends BaseView<Presenter> {
        void refreshCurrentPage(int totalPage, int curPage, boolean refresh, ArrayList<VozThread> vozThreads);
        Bitmap loadBitmapAssert(String url);
    }

    interface Presenter extends BasePresenter {
        void loadPage(final int curPage, boolean refresh);
    }
}
