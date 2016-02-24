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
import com.whoami.voz.ui.adapter.Page3PagerAdapter;
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
    
    public static final int GO_FIRST = 4;
    public static final int GO_LAST = 5;
    public static final int GO_PREVIOUS = 3;
    public static final int GO_NEXT = 2;

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
    private Page3PagerAdapter.Page3PagerListener page3PagerListener = new Page3PagerAdapter.Page3PagerListener() {
        @Override
        public void onSwipeTReresh(SwipeRefreshLayout swipeRefreshLayout) {
//            refreshPage();
//            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onGoPage(int type) {
            try {
                switch (type) {
                    case GO_FIRST: {
                        if (mViewPager.getCurrentItem() != 0) {
                            mViewPager.setCurrentItem(0);
                        }
                        break;
                    }
                    case GO_LAST:{
                        int lasItemIndex = mPage3PagerAdapter.getTotalPage() - 1;
                        if (mViewPager.getCurrentItem() != lasItemIndex) {
                            mViewPager.setCurrentItem(lasItemIndex);
                        }
                        break;
                    }
                    case GO_PREVIOUS:{
//                        if (mCurPage - 1 > 0) {
//                            mCurPage = mCurPage - 1;
//                        }
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                        break;
                    }
                    case GO_NEXT: {
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
            alertGoPage();
        }
    };


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
//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                if (state == ViewPager.SCROLL_STATE_IDLE) {
//                    try {
//                        int position = mViewPager.getCurrentItem() + 1;
//                        mCurPage = position;
//                        loadPage(position);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
//            }
//        });

        mPage3PagerAdapter = new Page3PagerAdapter(getActivity(), mTotalPage, mMapPostPerPage, url, bmImageStart, 1.0f);
//        mPage3PagerAdapter.setPage3Listener(page3PagerListener);
        mViewPager.setAdapter(mPage3PagerAdapter);
        //init page 1
//        loadPage(mCurPage);
        return mRootView;
    }

    private void readSettings() {
        this.mTextSize = this.settings.getFloat("FontSize", 1);
        Global.iSize = this.mTextSize;
        Global.bSwipe = this.settings.getBoolean("SWIPE", true);
        Global.bTopicHeader = this.settings.getBoolean("TOPICHEADER", false);
        Global.bIconRefresh = this.settings.getBoolean("ICONREFRESH", false);
        Global.bClickAd = this.settings.getBoolean("CLICKAD", false);
        Global.bEffect = this.settings.getBoolean("EFFECT", false);
        Global.bSign = this.settings.getBoolean("SIGN", false);
        Global.bDevider = this.settings.getBoolean("DEVIDER", true);
        Global.iHome = this.settings.getInt("SETHOMEVOZ", Page.STATE_ONSCREEN);
        Global.mSavePath = this.settings.getString("PATHSAVE", "/mnt/sdcard/vozforums");
        Global.bNotifSubscribe = this.settings.getBoolean("NOTIFICATIONSUBSCRIBE", true);
        Global.bNotifQuote = this.settings.getBoolean("NOTIFICATIONQUOTE", true);
        Global.iNotifminutes = this.settings.getInt("NOTIFICATIONTIMER", 30);
        Global.iNumQuote = this.settings.getInt("NUMQUOTE", Page.STATE_ONSCREEN);
        Global.bVibrate = this.settings.getBoolean("VIBRATE", true);
        Global.bFullScreen = this.settings.getBoolean("FULLSCREEN", false);
        Global.iSizeImage = this.settings.getInt("SIZEIMAGE", 8);
        Global.sYourDevice = this.settings.getString("YOURDEVICE", Build.MODEL);
//        toggleFullScreen(Global.bFullScreen);
        Map hashMap = new HashMap();
        if (!this.settings.getString("vfuserid", BuildConfig.FLAVOR).equals(BuildConfig.FLAVOR) && !this.settings.getString("vfuserid", BuildConfig.FLAVOR).equals("deleted")) {
            hashMap.put("vflastvisit", this.settings.getString("vflastvisit", BuildConfig.FLAVOR));
            hashMap.put("vflastactivity", this.settings.getString("vflastactivity", BuildConfig.FLAVOR));
            hashMap.put("vfuserid", this.settings.getString("vfuserid", BuildConfig.FLAVOR));
            hashMap.put("vfpassword", this.settings.getString("vfpassword", BuildConfig.FLAVOR));
            hashMap.put("vfimloggedin", this.settings.getString("vfimloggedin", BuildConfig.FLAVOR));
            hashMap.put("vfsessionhash", this.settings.getString("vfsessionhash", BuildConfig.FLAVOR));
            this.mUser.cookies(hashMap);
            this.mUser.setCookieStore(hashMap);
            this.mUser.setUserId(this.settings.getString("vfuserid", BuildConfig.FLAVOR));
//            String string = this.settings.getString("usered", null);
//            if (string != null) {
//                String[] split = string.split(";");
//                int length = split.length;
//                for (int i = STATE_ONSCREEN; i < length; i += STATE_OFFSCREEN) {
//                    String str = split[i];
//                    this.mArrayUsered.add(this.settings.getString("username-" + str, BuildConfig.FLAVOR));
//                    this.mArrayPass.add(this.settings.getString("password-" + str, BuildConfig.FLAVOR));
//                    this.mArrayUseredId.add(this.settings.getString("userid-" + str, BuildConfig.FLAVOR));
//                }
//            }
//            this.mUser.SetUser(this.settings.getString("username-" + this.mUser.UserId(), getResources().getString(R.string.Login)));
//            ((SidebarAdapter.Item) this.mListSideMenu2.get(STATE_ONSCREEN)).mTitle = this.mUser.User();
//            this.mAdapterSideMenu2.notifyDataSetInvalidated();
        }
    }

    private void refreshPage() {
//        TaskCancle();
//        this.mTask = new TaskGetHtml();
//        mTask.execute(new Integer[]{REFRESH_INDEX_PAGE});
        HtmlLoader.getInstance().fetchData(getUrlWithPage(mCurPage), new HtmlLoader.HtmlLoaderListener() {
            @Override
            public void onCallback(Document doc) {
                try {
                    parseDataPage3(0, doc, null, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }



    private void readCustomTheme() {
        Global.mCusThemeBg = this.settings.getInt("THEMEBG", -1);
        Global.mCusThemeBgFocus = this.settings.getInt("THEMEBGFOCUS", -13388315);
        Global.mCusThemeTitleBg = this.settings.getInt("THEMETITLEBG", -4995021);
        Global.mCusThemeTxtTitle = this.settings.getInt("THEMETXTTITLE", -1);
        Global.mCusThemeTxt1 = this.settings.getInt("THEMETXT1", ViewCompat.MEASURED_STATE_MASK);
        Global.mCusThemeTxt2 = this.settings.getInt("THEMETXT2", -11184811);
        Global.mCusThemeQuicklink = this.settings.getInt("THEMEQUICKLINK", -4995021);
        Global.themeColor[8][Page.STATE_ONSCREEN] = Global.mCusThemeBg;
        Global.themeColor[8][Page.STATE_RETURNING] = Global.mCusThemeTitleBg;
        Global.themeColor[8][3] = Global.mCusThemeTxtTitle;
        Global.themeColor[8][4] = Global.mCusThemeTxt1;
        Global.themeColor[8][5] = Global.mCusThemeTxt2;
        Global.themeColor[8][6] = Global.mCusThemeQuicklink;
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
            mCurPage = Integer.parseInt(text.split(" ")[1]);
            mTotalPage = Integer.parseInt(text.split(" ")[3]);
            if (this.mParser.getUrl().contains("showthread.php?p=")) {
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
                        attr = "https://vozforums.com/showthread.php?t=" + attr + "&page=" + String.valueOf(mCurPage);
                    } else {
                        i3 += 1;
                    }
                }
                attr = null;
                attr = "https://vozforums.com/showthread.php?t=" + attr + "&page=" + String.valueOf(mCurPage);
            } else {
                attr = this.mParser.getUrl();
                if (!attr.contains("&page=")) {
                    attr = attr.concat("&page=1");
                }
            }
        } else {
            attr = this.mParser.getUrl();
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
            case GO_NEXT:
                mCurPage++;
                return attr.substring(0, attr.lastIndexOf("=") + 1).concat(String.valueOf(mCurPage));
            case GO_PREVIOUS:
                mCurPage--;
                String concat = attr.substring(0, attr.lastIndexOf("=") + 1).concat(String.valueOf(mCurPage));
                return concat.contains("&page=0") ? concat.split("&page")[0] : concat;
            case GO_FIRST:
                mCurPage = 1;
                return attr.split("&page")[0];
            case GO_LAST:
                mCurPage = mTotalPage;
                return attr.split("&page")[0].concat("&page=" + String.valueOf(mTotalPage));
            default:
                return null;
        }
    }

    private String getUrlWithPage(int mPage) {
        String attr = mParser.getUrl();
        if (!attr.contains("&page=")) {
            attr = attr.concat("&page=1");
        }
        String concat = attr.substring(0, attr.lastIndexOf("=") + 1).concat(String.valueOf(mPage));
        return concat.contains("&page=0") ? concat.split("&page")[0] : concat;
    }

    private void writeSetingUser(Map<String, String> cookies) {

    }

    private void alertLogin() {

    }

    protected void setProgress(boolean z) {
        if (z) {
            this.mLayoutProgress.setVisibility(View.VISIBLE);
        } else {
            this.mLayoutProgress.setVisibility(View.GONE);
        }
    }

    void parseDataPage3(int typeParse, Document doc, Document doc2, boolean refresh) throws Exception {
        if (doc == null && this.mParser.sNotif != null) {
            toast(this.mParser.sNotif);
        }

        if (doc != null) {
            String str;
            Intent intent;
            Bundle bundle;
            Element first;
            switch (typeParse) {
                case 3: {
//                    if (this.iPostType == 3) {
//                        this.mTask.cancel(true);
//                        this.mTask = new TaskGetHtml();
//                        this.mTask.execute(new Integer[]{Integer.valueOf(6)});
//                        return;
//                    }
//                    str = "";
//                    intent = new Intent(getContext(), PageNewThread.class);
//                    bundle = new Bundle();
//                    if (this.iPostType != 0) {
//                        first = doc.select("textarea[name=message]").first();
//                        if (first != null) {
//                            str = ((TextNode) first.textNodes().get(0)).getWholeText();
//                        }
//                    }
//                    bundle.putString(NotificationCompat.CATEGORY_MESSAGE, str);
//                    bundle.putString(MySQLiteHelper.COLUMN_TITLE, this.mTextTitle);
//                    bundle.putFloat("textsize", this.mTextSize);
//                    bundle.putInt("POSTTYPE", this.iPostType);
//                    intent.putExtras(bundle);
//                    startActivityForResult(intent, this.iPostType);
                    break;
                }
                case 4: {
//                    Element r0 = doc.select("table[class=tborder]:contains(The following errors occurred)").first();
//                    if (r0 == null || !r0.text().contains("To be able to post links or images your post count")) {
//                        parseDataPage3(0, doc, doc2, true);
//                        return;
//                    }
//                    try {
//                        toast(r0.text());
//                        String obj = "\n\n[I]Sent from my " + Global.sYourDevice + " using [URL=\"https://play.google.com/store/apps/details?id=com.nna88.voz.main\"]vozForums[/URL]" + "[/I]";
//                        if (this.sPost.contains(obj)) {
//                            this.sPost = this.sPost.substring(0, this.sPost.indexOf(obj));
//                            this.sPost += "\n\n[I]Sent from my " + Global.sYourDevice + " using vozForums" + "[/I]";
//                            toast("Try Again");
//                            TaskCancle();
//                            this.mTask = new TaskGetHtml();
//                            this.mTask.execute(new Integer[]{Integer.valueOf(4)});
//                            return;
//                        }
//                        this.iPostType = 1;
//                        str = BuildConfig.FLAVOR;
//                        intent = new Intent(getContext(), PageNewThread.class);
//                        bundle = new Bundle();
//                        if (this.iPostType != 0) {
//                            first = doc.select("textarea[name=message]").first();
//                            if (first != null) {
//                                str = ((TextNode) first.textNodes().get(0)).getWholeText();
//                            }
//                        }
//                        bundle.putString(NotificationCompat.CATEGORY_MESSAGE, str);
//                        bundle.putString(MySQLiteHelper.COLUMN_TITLE, this.mTextTitle);
//                        bundle.putFloat("textsize", this.mTextSize);
//                        bundle.putInt("POSTTYPE", this.iPostType);
//                        intent.putExtras(bundle);
//                        startActivityForResult(intent, this.iPostType);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                    break;
                }

                default: {
                    Post post;
                    mListPost.clear();
                    parsePage(0, 0, doc);
                    Element element = doc.select("div[align=center]").size() > 1 ? (Element) doc.select("div[align=center]").get(1) : doc;
                    Element r0 = element.select("a[href=private.php]").first();
                    this.mPostId = getPostIdFromUrl(this.mParser.getUrl());
                    if (r0 != null) {
                        this.mUser.setUserId(((Element) element.select("td[class=alt2]").get(0)).select("a[href*=mem]").attr("href").split("=")[1]);
                        r0 = element.select("input[name*=securitytoken]").first();
                        if (r0 != null) {
                            this.mUser.setToken(r0.attr("value"));
                        }
                        r0 = doc.select("div[style=margin-top:6px]:has(input[value=Post Quick Reply])").first();
                        if (r0 != null) {
                            this.sIdThread = r0.select("input[name=t]").attr("value");
                        }
                        r0 = doc.select("td[class=alt1]:has(a[href *= subscription.php]").first();
                        if (r0 != null) {
                            if (r0.select("a[href *= subscription.php]").attr("href").contains("removesubscription")) {
                                this.isSubscribe = true;
                                ((SidebarAdapter.Item) this.mListSideMenu2.get(3)).mTitle = getResources().getString(R.string.UnSubscribe);
                                this.mAdapterSideMenu2.notifyDataSetInvalidated();
                            } else {
                                ((SidebarAdapter.Item) this.mListSideMenu2.get(3)).mTitle = getResources().getString(R.string.Subscribe);
                                this.mAdapterSideMenu2.notifyDataSetInvalidated();
                                this.isSubscribe = false;
                            }
                        }
                    }
                    Elements select = element.select("td[id*=td_post]");
                    Iterator it = select.iterator();
                    this.iPositiion = 0;
                    String str2 = null;
                    while (it.hasNext()) {
                        String text;
                        String str3 = null;
                        post = new Post();
                        String str4 = BuildConfig.FLAVOR;
                        Element element2 = (Element) it.next();
                        Element parent = element2.parent();
                        if (select.select("div[class=smallfont]:has(strong)").first() != null) {
                            this.mTextTitle = select.select("div[class=smallfont]:has(strong)").first().text();
                            setTitle(this.mTextTitle);
                        }
                        Element previousElementSibling = parent.previousElementSibling();
                        Element previousElementSibling2 = previousElementSibling.previousElementSibling();
                        if (previousElementSibling.select("img[src*=avatars]").first() != null) {
                            str3 = previousElementSibling.select("img[src*=avatars]").attr("src");
                            if (!str3.contains(Global.URL)) {
                                str3 = Global.URL + str3;
                            }
                        }
                        if (previousElementSibling.select("div:containsOwn(Join Date)").first() != null) {
                            text = previousElementSibling.select("div:containsOwn(Join Date)").first().text();
                            if (text.contains("Date:")) {
                                text = text.split("Date:")[1];
                            }
                            post.setJD("Jd:" + text);
                        } else {
                            post.setJD(BuildConfig.FLAVOR);
                        }
                        if (previousElementSibling.select("div:containsOwn(Posts: )").first() != null) {
                            post.setPosts(previousElementSibling.select("div:containsOwn(Posts: )").first().text());
                        } else {
                            post.setPosts(BuildConfig.FLAVOR);
                        }
                        if (previousElementSibling.select("img[src*=line.gif").first() != null) {
                            if (previousElementSibling.select("img[src*=line.gif").attr("src").contains("online")) {
                                post.isOnline = true;
                            } else {
                                post.isOnline = false;
                            }
                        }
                        if (previousElementSibling.select("a[class=bigusername]").first() != null) {
                            text = previousElementSibling.select("a[class=bigusername]").text();
                            post.m_UserId = previousElementSibling.select("a[class=bigusername]").first().attr("href").split("u=")[1];
                        } else {
                            text = str2;
                        }
                        str2 = previousElementSibling.select("div[class=smallfont]").first() != null ? previousElementSibling.select("div[class=smallfont]").first().text() : str4;
                        str4 = previousElementSibling2.text();
                        previousElementSibling = element2.select("div[id*=post_message").first();
                        if (previousElementSibling != null) {
                            if (previousElementSibling.attr("id").split("_").length > 2) {
                                post.setId(previousElementSibling.attr("id").split("_")[2]);
                            }
                            parsePage3(previousElementSibling, post, false);
                        }
                        previousElementSibling = element2.select("fieldset[class=fieldset]").first();
                        if (previousElementSibling != null) {
                            post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                            parsePage3(previousElementSibling, post, false);
                        }
                        post.Info(text, str2, str4, null, str3);
                        if (Global.bSign && element2.select("div:contains(_______)").first() != null) {
                            post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                            parsePage3(element2.select("div:contains(_______)").first(), post, false);
                        }
                        post.initContent();
                        mListPost.add(post);
                        if (post.Id().equals(this.mPostId)) {
                            this.iPositiion = mListPost.indexOf(post) + 1;
                            str2 = text;
                        } else {
                            str2 = text;
                        }
                    }
                    mItemCount = 23;
                    if (mItemOffsetY == null) {
                        this.mItemOffsetY = new int[(this.mItemCount + 1)];
                        this.mItemtemp = new int[(this.mItemCount + 1)];
                    }
                    this.scrollIsComputed = true;
                    mMapPostPerPage.put(Integer.valueOf(mCurPage), new ArrayList<Post>(mListPost));
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        setProgress(false);
                        mPage3PagerAdapter.setTotalPage(mTotalPage);
                        refreshCurrentPage(mCurPage, false);
                    }
                });


            }
        }
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

    private void loadPage(int curPage) {
        //download page data
        try {
            if (!mMapPostPerPage.containsKey(Integer.valueOf(curPage))) {
                String url = getUrlWithPage(curPage);
                if (url != null) {
    //                new TaskGetHtmlSilent().execute(new String[]{url});
//                    setProgress(true);
                    HtmlLoader.getInstance().fetchData(url, new HtmlLoader.HtmlLoaderListener() {
                        @Override
                        public void onCallback(Document doc) {
                            try {
                                parseDataPage3(0, doc, null, true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } else {
                refreshCurrentPage(curPage, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshCurrentPage(final int curPage, final boolean forceRefresh) {
        try {
            Log.i(TAG, "Refresh page :  " + curPage);
            View page = mViewPager.findViewWithTag(curPage);
            if (page != null) {
                ListView listView = (ListView) page.findViewById(R.id.content_frame);
                if (forceRefresh) {
                    if (listView.getAdapter() != null && mMapPostPerPage.containsKey(Integer.valueOf(mCurPage))) {
                        final Page3ListViewAdapter adapter = new Page3ListViewAdapter(getActivity(), mMapPostPerPage.get(Integer.valueOf(mCurPage)), null, bmImageStart, mTextSize);
                        View header = listView.findViewWithTag("Header");
                        listView.removeHeaderView(header);
                        View footer = listView.findViewWithTag("Footer");
                        listView.removeFooterView(footer);

                        listView.addHeaderView(getNavigationView(mCurPage, "Header"));
                        listView.addFooterView(getNavigationView(mCurPage, "Footer"));
                        listView.setAdapter(adapter);

                        mPage3PagerAdapter.notifyDataSetChanged();
                    }
                } else {
                    (page.findViewById(R.id.layout_progress)).setVisibility(View.GONE); // gone progress
                    if (listView.getAdapter() == null && mMapPostPerPage.containsKey(Integer.valueOf(curPage))) {
                        final Page3ListViewAdapter adapter = new Page3ListViewAdapter(getActivity(), mMapPostPerPage.get(Integer.valueOf(curPage)), null, bmImageStart, mTextSize);
                        listView.addHeaderView(getNavigationView(curPage, "Header"));
                        listView.addFooterView(getNavigationView(curPage, "Footer"));
                        listView.setAdapter(adapter);
                        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(AbsListView view, int scrollState) {
    //                        if (scrollState == SCROLL_STATE_IDLE) {
    //                            adapter.setIsScrolling(false);
    //                            adapter.notifyDataSetChanged();
    //                        } else {
    //                            adapter.setIsScrolling(true);
    //                        }
                            }

                            @Override
                            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                            }
                        });
                        mPage3PagerAdapter.notifyDataSetChanged();
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View getNavigationView(int page , String tag) {
        LinearLayout navigationView = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.threadfoot, null);
        navigationView.setTag(tag);
        Global.setBackgroundItemThread(navigationView);
        ImageView mImg1Footer = (ImageView) navigationView.findViewById(R.id.fast_prev);
        ImageView mImg2Footer = (ImageView) navigationView.findViewById(R.id.prev);
        TextView butPageFooter = (TextView) navigationView.findViewById(R.id.page_list);
        butPageFooter.setText(page + "/" + mTotalPage);
        ImageView mImg3Footer = (ImageView) navigationView.findViewById(R.id.next);
        ImageView mImg4Footer = (ImageView) navigationView.findViewById(R.id.fast_next);
        butPageFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertGoPage();
            }
        });

        mImg1Footer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    mViewPager.setCurrentItem(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mImg4Footer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    mViewPager.setCurrentItem(mTotalPage - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mImg2Footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurPage - 1 > 0) {
                    mCurPage = mCurPage - 1;
                    mViewPager.setCurrentItem(mCurPage - 1);
                }
            }
        });
        mImg3Footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurPage + 1 <= mTotalPage) {
                    mCurPage = mCurPage + 1;
                    mViewPager.setCurrentItem(mCurPage - 1);
                }
            }
        });
        //update button navigation state
        if (page == 1 && page == mTotalPage) {
            mImg1Footer.setEnabled(false);
            mImg2Footer.setEnabled(false);
            mImg3Footer.setEnabled(false);
            mImg4Footer.setEnabled(false);
        } else if (page == 1) {
            mImg1Footer.setEnabled(false);
            mImg2Footer.setEnabled(false);
            mImg3Footer.setEnabled(true);
            mImg4Footer.setEnabled(true);

        } else if (page == mTotalPage) {
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
        return navigationView;
    }

    private void log(String s) {
        Log.i(Page3Fragment.class.getSimpleName(), s);
    }

    void toast(String str) {
        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
    }

//    protected void TaskCancle() {
//        if (this.mTask != null) {
//            this.mTask.cancel(true);
//        }
//    }

    private String getPostIdFromUrl(String str) {
        String str2 = null;
        try {
            if (str.contains("p=")) {
                str2 = str.split("p=")[1];
            }
            return str2.contains("#post") ? str2.split("#post")[1] : str2;
        } catch (Exception e) {
            return null;
        }
    }

    private void parsePage3(Element element, Post post, boolean z) {
        if (element != null) {
            for (Node node : element.childNodes()) {
                if (node instanceof Element) {
                    Element first;
                    int length;
                    int length2;
                    if (((Element) node).tagName().equals("div")) {
                        first = ((Element) node).select("div").first();
                        if (first.attr("style").contains("padding")) {
                            post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                        }
                        if (first.ownText().contains("Originally Posted by")) {
                            post.addText("Originally Posted by ");
                            length = post.getText().length();
                            post.addText(first.select("strong").text());
                            length2 = post.getText().length();
                            post.web.add(Global.URL + first.select("a").attr("href"), length, length2);
                            post.type.add("", length, length2, 1);
                            post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                        } else {
                            parsePage3(first, post, z);
                            post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                        }
                    } else if (((Element) node).tagName().equals("blockquote")) {
                        first = ((Element) node).select("blockquote").first();
                        post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                        parsePage3(first, post, z);
                        post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                    } else if (((Element) node).tagName().equals("fieldset")) {
                        first = ((Element) node).select("fieldset").first();
                        post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                        parsePage3(first, post, z);
                        post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                    } else if (((Element) node).tagName().equals("b")) {
                        first = ((Element) node).select("b").first();
                        length = post.getText().length();
                        parsePage3(first, post, z);
                        post.type.add("", length, post.getText().length(), 1);
                    } else if (((Element) node).tagName().equals("i")) {
                        first = ((Element) node).select("i").first();
                        length = post.getText().length();
                        parsePage3(first, post, z);
                        post.type.add("", length, post.getText().length(), 2);
                    } else if (((Element) node).tagName().equals("pre")) {
                        first = ((Element) node).select("pre").first();
                        length = post.getText().length();
                        parsePage3(first, post, z);
                        post.quote.add("", length, post.getText().length());
                    } else if (((Element) node).tagName().equals("table")) {
                        first = ((Element) node).select("table").first();
                        length = post.getText().length();
                        parsePage3(first, post, z);
                        post.quote.add("", length, post.getText().length());
                    } else if (((Element) node).tagName().equals("ol")) {
                        parsePage3(((Element) node).select("ol").first(), post, z);
                    } else if (((Element) node).tagName().equals("tbody")) {
                        parsePage3(((Element) node).select("tbody").first(), post, z);
                    } else if (((Element) node).tagName().equals("li")) {
                        parsePage3(((Element) node).select("li").first(), post, z);
                    } else if (((Element) node).tagName().equals("tr")) {
                        first = ((Element) node).select("tr").first();
                        post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                        parsePage3(first, post, z);
                    } else if (((Element) node).tagName().equals("td")) {
                        parsePage3(((Element) node).select("td").first(), post, z);
                    } else if (((Element) node).tagName().equals("img")) {
                        String r0;
                        r0 = ((Element) node).select("img[src]").attr("src");
                        if (r0.contains(Global.URL) && r0.subSequence(0, 21).equals(Global.URL) && !r0.contains("https://vozforums.com/attachment.php?attachmentid") && !r0.contains("https://vozforums.com/customavatars/")) {
                            r0 = r0.substring(21);
                        }
                        if (r0.substring(0, 1).equals("/")) {
                            r0 = r0.substring(1, r0.length());
                        }
                        length = post.getText().length();
                        length2 = r0.length();
                        if (r0.contains("http://") || r0.contains("https://") || r0.contains("attachment.php?attachmentid")) {
                            post.image.add(r0, length, length2 + length, this.bmImageStart);
                        } else {
                            post.image.add(r0, length, length + 2, null);
                            r0 = "  ";
                        }
                        if (!r0.contains("images/buttons/viewpost.gif")) {
                            post.addText(r0);
                            if (node.hasAttr("onload")) {
                                post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                            }
                        }
                    } else if (((Element) node).tagName().equals("br")) {
                        post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                    } else if (((Element) node).tagName().equals("u")) {
                        first = ((Element) node).select("u").first();
                        length = post.getText().length();
                        parsePage3(first, post, z);
                        post.typeU.add("", length, post.getText().length());
                    } else if (((Element) node).tagName().equals("font")) {
                        Element first2 = ((Element) node).select("font").first();
                        String str = "while";
                        String r1 = "3";
                        if (((Element) node).select("font[color]").first() != null) {
                            str = ((Element) node).select("font[color]").attr("color");
                        }
                        String attr = ((Element) node).select("font[size]").first() != null ? ((Element) node).select("font[size]").attr("size") : r1;
                        length2 = post.getText().length();
                        parsePage3(first2, post, z);
                        post.font.add("", length2, post.getText().length(), str, Integer.parseInt(attr));
                    } else if (((Element) node).tagName().equals("a")) {
                        first = ((Element) node).select("a[href]").first();
                        if (first.select("img").first() == null) {
                            String r0;
                            r0 = ((Element) node).select("a[href]").attr("href").replace("%3A", ":").replace("%2F", "/").replace("%3F", "?").replace("%3D", "=").replace("%26", "&");
                            if (r0.contains("mailto:")) {
                                String r1 = r0.substring(7, r0.length());
                                r0 = ((Element) node).select("a[href]").text();
                                length2 = post.getText().length();
                                post.web.add(r1, length2, r0.length() + length2);
                                post.addText(r0);
                            } else if (r0.contains("http")) {
                                String r1 = r0.substring(r0.indexOf("http"), r0.length());
                                r0 = ((Element) node).select("a[href]").text();
                                length2 = post.getText().length();
                                post.web.add(r1, length2, r0.length() + length2);
                                post.addText(r0);
                            }
                        } else {
                            parsePage3(first, post, true);
                        }
                    } else {
                        post.addText(((Element) node).text());
                    }
                }
                if (node instanceof TextNode) {
                    if (z) {
                        post.addText(((TextNode) node).getWholeText());
                    } else {
                        post.addText(((TextNode) node).text());
                    }
                }
            }
        }
//        post.addText(element.text());
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
