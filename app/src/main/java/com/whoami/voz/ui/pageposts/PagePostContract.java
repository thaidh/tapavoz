package com.whoami.voz.ui.pageposts;

import com.whoami.voz.ui.BasePresenter;
import com.whoami.voz.ui.BaseView;
import com.whoami.voz.ui.contain.VozPost;

import java.util.ArrayList;

/**
 * Created by thaidh on 5/2/17.
 */

public interface PagePostContract {

    interface View extends BaseView<PagePostContract.Presenter> {
        void refreshCurrentPage(int totalPage, final int curPage, boolean forceRefresh, ArrayList<VozPost> posts);
    }

    interface Presenter extends BasePresenter {
        void loadPage(final int curPage, boolean refresh);
    }

}
