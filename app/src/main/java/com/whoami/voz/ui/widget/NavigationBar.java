package com.whoami.voz.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nna88.voz.main.R;
import com.whoami.voz.ui.adapter.BasePagerAdapter;
import com.whoami.voz.ui.delegate.PagerListener;

/**
 * Created by thaidh on 5/18/16.
 */
public class NavigationBar extends LinearLayout {
    private PagerListener mPagerListener;
    private ImageView mImg1Footer;
    private ImageView mImg2Footer;
    private TextView butPageFooter;
    private ImageView mImg3Footer;
    private ImageView mImg4Footer;
    private int curPos;
    private int mTotalPage;

    public NavigationBar(Context context) {
        super(context);
        initLayout(context);
    }

    public void setPagerListener(PagerListener mPagerListener) {
        this.mPagerListener = mPagerListener;
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
    }

    public void initLayout(Context context) {
        LinearLayout root = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.threadfoot, this, true);
        mImg1Footer = (ImageView) root.findViewById(R.id.fast_prev);
        mImg2Footer = (ImageView) root.findViewById(R.id.prev);
        mImg3Footer = (ImageView) root.findViewById(R.id.next);
        mImg4Footer = (ImageView) root.findViewById(R.id.fast_next);
        butPageFooter = (TextView) root.findViewById(R.id.page_list);

        setGravity(Gravity.CENTER);

        butPageFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPagerListener.showDialogGoPage();
            }
        });

        mImg1Footer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    mPagerListener.onGoPage(BasePagerAdapter.GO_FIRST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mImg2Footer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    mPagerListener.onGoPage(BasePagerAdapter.GO_PREVIOUS);
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
                    mPagerListener.onGoPage(BasePagerAdapter.GO_NEXT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mImg4Footer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    mPagerListener.onGoPage(BasePagerAdapter.GO_LAST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void refresh(int curPage, int totalPage) {
        curPos = curPage;
        mTotalPage = totalPage;
        butPageFooter.setText(curPos + "/" + mTotalPage);
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
    }

}
