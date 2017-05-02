package com.whoami.voz.ui.pagethreads;

import android.graphics.Bitmap;

import com.whoami.voz.ui.BasePresenter;
import com.whoami.voz.ui.BaseView;
import com.whoami.voz.ui.contain.VozThread;

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
