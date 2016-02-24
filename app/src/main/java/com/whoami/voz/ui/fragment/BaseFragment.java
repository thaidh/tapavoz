package com.whoami.voz.ui.fragment;

import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nna88.voz.main.Global;
import com.nna88.voz.main.Page;
import com.nna88.voz.main.R;
import com.nna88.voz.main.UILApplication;
import com.nna88.voz.util.Util;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by JonSnow on 2/18/2016.
 */
public class BaseFragment extends Fragment {
    protected LinearLayout mQuickReturnLayout;

    @Override public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = UILApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initQuickReturn(view);
    }

    private void initQuickReturn(View view) {
        int i = 0;
        this.mQuickReturnLayout = (LinearLayout) view.findViewById(R.id.compose_button_bar);
        this.mQuickReturnLayout.removeAllViews();
        int convertDpToPx = Util.convertDpToPx(getContext(), 40);

        SharedPreferences settings = getActivity().getSharedPreferences("Setting", Page.STATE_ONSCREEN);
        Global.iNumQuickLink = settings.getString("NUMQUICKLINK", "5");
        int parseInt = Integer.parseInt(Global.iNumQuickLink);
        ImageView imageView = new ImageView(this.mQuickReturnLayout.getContext());
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.menu_up));
        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(convertDpToPx, convertDpToPx);
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
//                    Page.this.mList.clearFocus();
//                    Page.this.mList.post(new Runnable() {
//                        public void run() {
//                            Page.this.mList.setSelection(Page.STATE_ONSCREEN);
//                        }
//                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
//        Global.setBackgroundItemThread(imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(layoutParams);
        this.mQuickReturnLayout.addView(imageView);
        ViewGroup.LayoutParams layoutParams2 =
                parseInt >= 5 ? new LinearLayout.LayoutParams((Global.width - (convertDpToPx * 2)) / 5, -1) : new LinearLayout.LayoutParams(0, -1, 1);
        while (i < Integer.parseInt(Global.iNumQuickLink)) {
            TextView textView = new TextView(this.mQuickReturnLayout.getContext());
//            Global.setBackgroundItemThread(textView);
            textView.setMinHeight(Util.convertDpToPx(getContext(), 40));
            if (i % 5 == 0) {
                textView.setTextColor(getResources().getColor(R.color.colorquiclink1));
                textView.setText(settings.getString("QUICKLINK" + i, "f=0"));
            } else if (i % 5 == 1) {
                textView.setTextColor(getResources().getColor(R.color.colorquiclink2));
                textView.setText(settings.getString("QUICKLINK" + i, "f=32"));
            } else if (i % 5 == 2) {
                textView.setTextColor(getResources().getColor(R.color.colorquiclink3));
                textView.setText(settings.getString("QUICKLINK" + i, "f=26"));
            } else if (i % 5 == 3) {
                textView.setTextColor(getResources().getColor(R.color.colorquiclink4));
                textView.setText(settings.getString("QUICKLINK" + i, "f=17"));
            } else if (i % 5 == 4) {
                textView.setTextColor(getResources().getColor(R.color.colorquiclink1));
                textView.setText(settings.getString("QUICKLINK" + i, "f=33"));
            }
            textView.setGravity(17);
            textView.setLayoutParams(layoutParams2);
//            textView.setOnLongClickListener(new AnonymousClass16(i));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v instanceof  TextView) {
                        String charSequence = ((TextView)v).getText().toString();
                        BaseFragment fragment = null;
                        if (charSequence.equals("f=0")) {
                            fragment = Page1Fragment.newInstance("", "");
                        } else {
                            fragment = Page2Fragment.newInstance("forumdisplay.php?" + charSequence, charSequence);
                        }
                        if (fragment != null) {
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.add(R.id.container, fragment, "AAAA");
                            ft.addToBackStack("Page3Fragment");
                            ft.commit();
                        }
                    }
                }
            });
            this.mQuickReturnLayout.addView(textView);
            i += 1;
        }
        ImageView imageView2 = new ImageView(this.mQuickReturnLayout.getContext());
        imageView2.setImageDrawable(getResources().getDrawable(R.drawable.menu_down));
        ViewGroup.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(convertDpToPx, convertDpToPx);
//        Global.setBackgroundItemThread(imageView2);
        imageView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
//                    Page.this.mList.clearFocus();
//                    Page.this.mList.post(new Runnable() {
//                        public void run() {
//                            int count = Page.this.iPage == Page.STATE_OFFSCREEN ? ((listViewCustom1) Page.this.mObjectAdapter).getCount() : Page.this.iPage == Page.STATE_RETURNING ? ((listViewCustom2) Page.this.mObjectAdapter).getCount() + Page.STATE_OFFSCREEN : Page.this.iPage == 3 ? ((listViewCustom3Html) Page.this.mObjectAdapter).getCount() + Page.STATE_OFFSCREEN : Page.this.iPage == 11 ? ((Page3ListViewAdapter) Page.this.mObjectAdapter).getCount() + Page.STATE_OFFSCREEN : ((listViewCustom2) Page.this.mObjectAdapter).getCount();
//                            Page.this.mList.setSelection(count);
//                        }
//                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        imageView2.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView2.setLayoutParams(layoutParams3);
        this.mQuickReturnLayout.addView(imageView2);
//        this.mList.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            public void onGlobalLayout() {
//                Page.this.mQuickReturnHeight = Page.this.mQuickReturnLayout.getHeight();
//            }
//        });
//        this.mList.setOnScrollListener(new AbsListView.OnScrollListener() {
//            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
//                Page.this.mScrollY = Page.STATE_ONSCREEN;
//                if (Page.this.scrollIsComputed) {
//                    Page.this.mScrollY = Page.this.getComputedScrollY();
//                }
//                int i4 = Page.this.mScrollY;
//                switch (Page.this.mState) {
//                    case Page.STATE_ONSCREEN /*0*/:
//                        if (i4 > Page.this.mQuickReturnHeight) {
//                            Page.this.mState = Page.STATE_OFFSCREEN;
//                            Page.this.mMinRawY = i4;
//                        }
//                        Page.this.mQuickReturnLayout.setVisibility(Page.STATE_ONSCREEN);
//                        break;
//                    case Page.STATE_OFFSCREEN /*1*/:
//                        if (i4 >= Page.this.mMinRawY) {
//                            Page.this.mMinRawY = i4;
//                        } else {
//                            Page.this.mState = Page.STATE_RETURNING;
//                        }
//                        Page.this.mQuickReturnLayout.setVisibility(View.GONE);
//                        break;
//                    case Page.STATE_RETURNING /*2*/:
//                        int i5 = (i4 - Page.this.mMinRawY) + Page.this.mQuickReturnHeight;
//                        if (i5 < 0) {
//                            Page.this.mMinRawY = Page.this.mQuickReturnHeight + i4;
//                            i5 = Page.STATE_ONSCREEN;
//                        }
//                        if (i4 == 0) {
//                            Page.this.mState = Page.STATE_ONSCREEN;
//                            i5 = Page.STATE_ONSCREEN;
//                        }
//                        if (i5 > Page.this.mQuickReturnHeight) {
//                            Page.this.mState = Page.STATE_OFFSCREEN;
//                            Page.this.mMinRawY = i4;
//                        }
//                        if (i5 != Page.this.mQuickReturnHeight) {
//                            Page.this.mQuickReturnLayout.setVisibility(Page.STATE_ONSCREEN);
//                            i4 = i5;
//                            break;
//                        }
//                        Page.this.mQuickReturnLayout.setVisibility(View.GONE);
//                        i4 = i5;
//                        break;
//                    default:
//                        i4 = Page.STATE_ONSCREEN;
//                        break;
//                }
//                if (Build.VERSION.SDK_INT <= 11) {
//                    Page.this.anim = new TranslateAnimation(0.0f, 0.0f, (float) i4, (float) i4);
//                    Page.this.anim.setFillAfter(true);
//                    Page.this.anim.setDuration(0);
//                    Page.this.mQuickReturnLayout.startAnimation(Page.this.anim);
//                    return;
//                }
//                Page.this.mQuickReturnLayout.setTranslationY((float) i4);
//            }
//
//            @SuppressLint({"NewApi"})
//            public void onScrollStateChanged(AbsListView absListView, int i) {
//                switch (i) {
//                    case Page.STATE_OFFSCREEN /*1*/:
//                        Global.bScrolling = true;
//                    default:
//                }
//            }
//        });
    }
}
