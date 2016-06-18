package com.whoami.voz.ui.delegate;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

/**
 * Created by thaidh on 5/15/16.
 */
public interface PagerListener {
    View findPageView(int curPage);

    void onSwipeTReresh(SwipeRefreshLayout swipeRefreshLayout);

    void onGoPage(int type);

    void showDialogGoPage();
}
