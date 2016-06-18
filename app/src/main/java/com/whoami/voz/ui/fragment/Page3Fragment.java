package com.whoami.voz.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nna88.voz.contain.Post;
import com.nna88.voz.listview.Page3ListViewAdapter;
import com.nna88.voz.listview.WebkitCookieManagerProxy;
import com.nna88.voz.main.BuildConfig;
import com.nna88.voz.main.Global;
import com.nna88.voz.main.MyNetwork;
import com.nna88.voz.main.Page;
import com.nna88.voz.main.R;
import com.nna88.voz.parserhtml.HtmlParser;
import com.nna88.voz.quickaction.QuickAction;
import com.nna88.voz.ui.SidebarAdapter;
import com.nna88.voz.util.UserInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.whoami.voz.ui.adapter.BasePagerAdapter;
import com.whoami.voz.ui.adapter.Page3PagerAdapter;
import com.whoami.voz.ui.delegate.PagerListener;
import com.whoami.voz.ui.utils.HtmlLoader;

import org.apache.commons.io.IOUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.net.CookieHandler;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Page3Fragment extends BaseFragment {
    private static final String TAG = Page3Fragment.class.getSimpleName();
    
    public static final int REFRESH_INDEX_PAGE = 999;


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String URL_PARAM = "param2";

    private String mParam1;
    private String mParam2;

    public static final int MAX_ENTRIES = 5;
    private static Map<Integer, ArrayList<Post>> mMapPostPerPage = new LinkedHashMap() {
        public boolean removeEldestEntry(Map.Entry eldest) {
            return size() > MAX_ENTRIES;
        }
    };

    private ArrayList<Post> mListPost = new ArrayList<>();
    private Bitmap bmImageFailed;
    private Bitmap bmImageStart;
    private String humanverify_hash;
    private int iCallFromNotification;
    private int iCallFromNotificationQuote;
    private int iPostType;
    ImageLoader imageLoader;
    int indexI;
    int indexJ;
    private String linkapicaptcha;
    private String mPostId;
    private QuickAction mQuickAction;
    private String mTextTitle;
    private MyNetwork myNetwork;
    private String recaptcha_challenge_field;
    private String url;
    private String urlCaptcha;

    // base activity
    private HtmlParser mParser;
    private String sIdThread;
//    private TaskGetHtml mTask;
//    private Document doc2;
    private UserInfo mUser;
    protected String specifiedpost;
    private String sPost;
    private String sMessSearch;
    private String sSearch_ShowPost;
    private String sForum;
    private float mTextSize;
//    private ListView mList;
    private int iPositiion;
    private boolean isSubscribe;
    private IdentityHashMap mListSideMenu2;
    private BaseAdapter mAdapterSideMenu2;
    private int iPage;
    protected int mItemCount;
    protected int[] mItemOffsetY;
    protected int[] mItemtemp;
    private boolean scrollIsComputed;
    private View mRootView;
    private SharedPreferences settings;
    private LinearLayout mLayoutProgress;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ViewPager mViewPager;
    private Page3PagerAdapter mPage3PagerAdapter;
    private int mTotalPage = 1;
    private int mCurPage = 1;

    private Toolbar toolbar;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Page3Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Page3Fragment newInstance(String param1, String param2) {
        Page3Fragment fragment = new Page3Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(URL_PARAM, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Page3Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            mParam1 = getArguments().getString(ARG_PARAM1);
            //clear data
            mMapPostPerPage.clear();
            url = getArguments().getString(URL_PARAM);

            if (!url.contains(Global.URL) && !url.contains(Global.URL2)) {
                url = Global.URL + url;
            }

            if (getArguments().getInt("NOTIFICATION", 1) != 1) {
                this.iCallFromNotification = getArguments().getInt("NOTIFICATION", 1);
            }
            if (getArguments().getInt("NOTIFICATIONQUOTE", 1) != 1) {
                this.iCallFromNotificationQuote = getArguments().getInt("NOTIFICATIONQUOTE", 1);
            }
        }
    }

    @Override
    public View  onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_page3, container, false);

        settings = getActivity().getSharedPreferences("Setting", 0);
        this.mUser = new UserInfo();
        Global.iTheme = this.settings.getInt("THEME", 1);
//        readSettings();

//        this.textTitle = (TextView) mRootView.findViewById(Resources.getSystem().getIdentifier("action_bar_title", "id", "android"));
        CookieSyncManager.createInstance(getContext());
        CookieManager.getInstance().setAcceptCookie(true);
        CookieHandler.setDefault(new WebkitCookieManagerProxy(null, CookiePolicy.ACCEPT_ALL));
        this.mLayoutProgress = (LinearLayout) mRootView.findViewById(R.id.layoutprogress);
        Global.iDensity = getResources().getDisplayMetrics().density;

        this.bmImageStart = BitmapFactory.decodeResource(getResources(), R.drawable.stub_image);
        this.bmImageFailed = BitmapFactory.decodeResource(getResources(), R.drawable.image_for_empty_url);
        this.myNetwork = new MyNetwork();

        this.mParser = new HtmlParser(Global.URL);
        this.mParser.setUrl(url);


        toolbar = (Toolbar) mRootView.findViewById(R.id.my_awesome_toolbar);
        toolbar.setTitle(mParam1);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        mViewPager = (ViewPager) mRootView.findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(1);
        mPage3PagerAdapter = new Page3PagerAdapter(getActivity(), mTotalPage, mMapPostPerPage, url, bmImageStart, 1.0f);
        mPage3PagerAdapter.setPagerListener(new PagerListener() {
            @Override
            public View findPageView(int curPage) {
                return mViewPager.findViewWithTag(curPage);
            }

            @Override
            public void onSwipeTReresh(SwipeRefreshLayout swipeRefreshLayout) {

            }

            @Override
            public void onGoPage(int type) {
                try {
                    switch (type) {
                        case BasePagerAdapter.GO_FIRST: {
                            if (mViewPager.getCurrentItem() != 0) {
                                mViewPager.setCurrentItem(0);
                            }
                            break;
                        }
                        case BasePagerAdapter.GO_LAST:{
                            int lasItemIndex = mPage3PagerAdapter.getTotalPage() - 1;
                            if (mViewPager.getCurrentItem() != lasItemIndex) {
                                mViewPager.setCurrentItem(lasItemIndex);
                            }
                            break;
                        }
                        case BasePagerAdapter.GO_PREVIOUS:{
//                        if (mCurPage - 1 > 0) {
//                            mCurPage = mCurPage - 1;
//                        }
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                            break;
                        }
                        case BasePagerAdapter.GO_NEXT: {
//                        if (mCurPage + 1 <= mTotalPage) {
//                            mCurPage = mCurPage + 1;
//                            mViewPager.setCurrentItem(mCurPage - 1);
//                        }
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                            break;

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void showDialogGoPage() {

            }
        });
        mViewPager.setAdapter(mPage3PagerAdapter);
        //init page 1
//        loadPage(mCurPage);
        return mRootView;
    }


    private void setTitle(final String mTextTitle) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (toolbar != null && !TextUtils.isEmpty(mTextTitle)) {
                        toolbar.setTitle(mTextTitle);
                    }
                }
            });
        }
    }

    protected void alertGoPage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View inflate = getActivity().getLayoutInflater().inflate(R.layout.login, null);
        final EditText editText = (EditText) inflate.findViewById(R.id.alert_edit1);
        TextView textView = (TextView) inflate.findViewById(R.id.alert_txt1);
        TextView textView2 = (TextView) inflate.findViewById(R.id.alert_txt2);
        Button button = (Button) inflate.findViewById(R.id.alert_ok);
        Button button2 = (Button) inflate.findViewById(R.id.alert_cancle);
        ((EditText) inflate.findViewById(R.id.alert_edit2)).setVisibility(View.GONE);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        textView.setVisibility(View.GONE);
        textView2.setVisibility(View.GONE);
        builder.setTitle("Page").setView(inflate);
        final Dialog dialog = builder.create();
        dialog.show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "click 1111111");
                String strPage = editText.getText().toString();
                int page = Integer.parseInt(strPage) - 1;
                if (page >= 0 && page < mTotalPage) {
                    mViewPager.setCurrentItem(page);
                }
                dialog.dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "click 222222222");
                dialog.dismiss();
            }
        });
    }


}
