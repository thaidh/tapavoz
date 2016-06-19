package com.whoami.voz.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.whoami.voz.ui.delegate.PagerListener;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.lang.ref.WeakReference;

/**
 * Created by thaidh on 5/18/16.
 */
public abstract class BasePagerAdapter  extends PagerAdapter{
    public static final int GO_FIRST = 4;
    public static final int GO_LAST = 5;
    public static final int GO_PREVIOUS = 3;
    public static final int GO_NEXT = 2;

    protected static final String TAG_NAVIGATION_HEADER = "navigation_header";
    protected static final String TAG_NAVIGATION_FOOTER = "navigation_footer";
    private static final String TAG = BasePagerAdapter.class.getSimpleName();
    protected WeakReference<ViewPager> mViewPager;
    protected PagerListener mPagerListener;
    protected int loadPageIndex;
    protected int mTotalPage;
    protected int pageCount;
    protected String mUrl;

    public void setTotalPage(int mTotalPage) {
        Log.e(TAG, "update total page : " + mTotalPage);
        this.pageCount = mTotalPage;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return pageCount;
    }

    public void setPagerListener(PagerListener mPagerListener) {
        this.mPagerListener = mPagerListener;
    }

    String parsePage(int action, int i2, Document doc) throws Exception {
//        if (doc == null) {
//            return null;
//        }
        String attr;
        String text = "zzz";
        if (doc != null) {
            Element first = doc.select("div[class=pagenav").first();
            if (first == null) {
                return null;
            }

            text = first.select("td[class=vbmenu_control]").text();
            loadPageIndex = Integer.parseInt(text.split(" ")[1]);
            mTotalPage = Integer.parseInt(text.split(" ")[3]);
            if (mUrl.contains("showthread.php?p=")) {
                int size = first.getElementsByAttribute("href").size();
                int i3 = 0;
                while (i3 < size) {
                    Element element = (Element) first.getElementsByAttribute("href").get(i3);
                    if (element != null) {
                        attr = element.attr("href");
                        if (!attr.contains("&page=")) {
                            attr = attr.concat("&page=1");
                        }
                        attr = attr.substring(attr.indexOf("?t=") + 3, attr.indexOf("&page"));
                        attr = "https://vozforums.com/showthread.php?t=" + attr + "&page=" + String.valueOf(loadPageIndex);
                    } else {
                        i3 += 1;
                    }
                }
                attr = null;
                attr = "https://vozforums.com/showthread.php?t=" + attr + "&page=" + String.valueOf(loadPageIndex);
            } else {
                attr = mUrl;
                if (!attr.contains("&page=")) {
                    attr = attr.concat("&page=1");
                }
            }
        } else {
            attr = mUrl;
            if (!attr.contains("&page=")) {
                attr = attr.concat("&page=1");
            }
        }

        switch (action) {
            case 0:
                String[] split = text.split(" ");
                return split[1] + "/" + split[3];
//            case 1:
//                return attr.split("&page")[0].concat("&page=" + String.valueOf(i2));
//            case Page3Fragment.GO_NEXT:
//                mCurPage++;
//                return attr.substring(0, attr.lastIndexOf("=") + 1).concat(String.valueOf(mCurPage));
//            case Page3Fragment.GO_PREVIOUS:
//                mCurPage--;
//                String concat = attr.substring(0, attr.lastIndexOf("=") + 1).concat(String.valueOf(mCurPage));
//                return concat.contains("&page=0") ? concat.split("&page")[0] : concat;
//            case Page3Fragment.GO_FIRST:
//                mCurPage = 1;
//                return attr.split("&page")[0];
//            case Page3Fragment.GO_LAST:
//                mCurPage = mTotalPage;
//                return attr.split("&page")[0].concat("&page=" + String.valueOf(mTotalPage));
            default:
                return null;

        }
    }
}
