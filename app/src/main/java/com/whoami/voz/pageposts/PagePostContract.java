package com.whoami.voz.pageposts;

import com.whoami.voz.retrofit.data.PostData;
import com.whoami.voz.BasePresenter;
import com.whoami.voz.BaseView;

/**
 * Created by thaidh on 5/2/17.
 */

public interface PagePostContract {

    interface View extends BaseView<PagePostContract.Presenter> {
        void refreshCurrentPage(int totalPage, final int curPage, boolean forceRefresh, PostData data);
    }

    interface Presenter extends BasePresenter {
        void loadPage(final int curPage, boolean refresh);
        String getCurrentUrl();
    }

}
