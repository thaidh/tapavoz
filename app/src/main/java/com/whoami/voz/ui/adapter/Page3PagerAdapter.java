package com.whoami.voz.ui.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nna88.voz.contain.Post;
import com.nna88.voz.listview.Page3ListViewAdapter;
import com.nna88.voz.main.Global;
import com.nna88.voz.main.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.whoami.voz.ui.fragment.Page3Fragment;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by thaidh on 10/3/15.
 */
public class Page3PagerAdapter extends PagerAdapter {
    private static final String TAG = Page3PagerAdapter.class.getSimpleName();

    private int mTotalPage;
    private ArrayList<Post> mListPosts;
    private Activity mContext;
    private ImageLoader mImageLoader;
    private Bitmap mImageStart;
    private float mTextSize;
    private Map<Integer, ArrayList<Post>> mMapPostPerPage;
    Page3PagerListener mPage3Listener;

    public Page3PagerAdapter(Activity mContext, int mTotalPage,  Map<Integer, ArrayList<Post>> mMapPostPerPage, ImageLoader mImageLoader, Bitmap mImageStart, float mTextSize) {
        this.mTotalPage = mTotalPage;
        this.mTextSize = mTextSize;
        this.mImageLoader = mImageLoader;
        this.mContext = mContext;
        this.mImageStart = mImageStart;
        this.mMapPostPerPage = mMapPostPerPage;
    }

    @Override
    public int getCount() {
        return mTotalPage;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int loadPageIndex = position + 1;
        Log.i(TAG, "LoadPage : " + loadPageIndex);
        View view = mContext.getLayoutInflater().inflate(R.layout.layout_page3_item, null);
        view.setTag(loadPageIndex);
        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        if (mPage3Listener != null) {
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mPage3Listener.onSwipeTReresh(refreshLayout);
                }
            });
        }
        ListView listView = (ListView) view.findViewById(R.id.content_frame);
        ArrayList<Post> curListPost = mMapPostPerPage.get(Integer.valueOf(loadPageIndex));
        if (curListPost != null) {
            view.findViewById(R.id.layout_progress).setVisibility(View.GONE);
            listView.addHeaderView(getNavigationView(Gravity.CENTER, loadPageIndex));
            listView.addFooterView(getNavigationView(Gravity.CENTER, loadPageIndex));
            Page3ListViewAdapter adapter = new Page3ListViewAdapter(mContext, curListPost, mImageLoader, mImageStart, mTextSize);
            listView.setAdapter(adapter);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setTotalPage(int mTotalPage) {
        Log.e(TAG, "update total page : "  + mTotalPage);
        this.mTotalPage = mTotalPage;
    }

    public void setPage3Listener(Page3PagerListener mPage3Listener) {
        this.mPage3Listener = mPage3Listener;
    }

    public interface Page3PagerListener {
        void onSwipeTReresh(SwipeRefreshLayout swipeRefreshLayout);
        void onGoPage(int type);
        void showDialogGoPage();
    }

    private View getNavigationView(int i, int curPos) {
        LinearLayout linearFooter = (LinearLayout) mContext.getLayoutInflater().inflate(R.layout.threadfoot, null);
        Global.setBackgroundItemThread(linearFooter);
        ImageView mImg1Footer = (ImageView) linearFooter.findViewById(R.id.fast_prev);
        ImageView mImg2Footer = (ImageView) linearFooter.findViewById(R.id.prev);
        TextView butPageFooter = (TextView) linearFooter.findViewById(R.id.page_list);
        butPageFooter.setText(curPos + "/" + mTotalPage);
        ImageView mImg3Footer = (ImageView) linearFooter.findViewById(R.id.next);
        ImageView mImg4Footer = (ImageView) linearFooter.findViewById(R.id.fast_next);

        butPageFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPage3Listener.showDialogGoPage();
            }
        });

        mImg1Footer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    mPage3Listener.onGoPage(Page3Fragment.GO_FIRST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mImg2Footer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    mPage3Listener.onGoPage(Page3Fragment.GO_PREVIOUS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        butPageFooter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                alertGoPage();
            }
        });
        mImg3Footer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    mPage3Listener.onGoPage(Page3Fragment.GO_NEXT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mImg4Footer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    mPage3Listener.onGoPage(Page3Fragment.GO_LAST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        linearFooter.setGravity(i);
        //update button navigation state
        if (curPos == 1 && curPos == mTotalPage) {
            mImg1Footer.setEnabled(false);
            mImg2Footer.setEnabled(false);
            mImg3Footer.setEnabled(false);
            mImg4Footer.setEnabled(false);
        } else if (curPos == 0) {
            mImg1Footer.setEnabled(false);
            mImg2Footer.setEnabled(false);
            mImg3Footer.setEnabled(true);
            mImg4Footer.setEnabled(true);

        } else if (curPos == mTotalPage) {
            mImg1Footer.setEnabled(true);
            mImg2Footer.setEnabled(true);
            mImg3Footer.setEnabled(false);
            mImg4Footer.setEnabled(false);
        } else {
            mImg1Footer.setEnabled(true);
            mImg2Footer.setEnabled(true);
            mImg3Footer.setEnabled(true);
            mImg4Footer.setEnabled(true);
        }
        return linearFooter;
    }
}
