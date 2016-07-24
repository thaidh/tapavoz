package com.whoami.voz.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.whoami.voz.R;
import com.whoami.voz.ui.adapter.BasePagerAdapter;
import com.whoami.voz.ui.adapter.Page3PagerAdapter;
import com.whoami.voz.ui.adapter.list.Page3ListViewAdapter;
import com.whoami.voz.ui.contain.Post;
import com.whoami.voz.ui.delegate.PagerListener;
import com.whoami.voz.ui.main.Global;
import com.whoami.voz.ui.parserhtml.HtmlParser;
import com.whoami.voz.ui.utils.HtmlLoader;
import com.whoami.voz.ui.widget.NavigationBar;

import org.apache.commons.io.IOUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
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

    private Bitmap bmImageStart;
    private String url;

    // base activity
    private HtmlParser mParser;
//    private ListView mList;
    private View mRootView;
    private SharedPreferences settings;
    private LinearLayout mLayoutProgress;
    private ViewPager mViewPager;
    private Page3PagerAdapter mPage3PagerAdapter;
    private PagerListener mPagerListener;

    private TextView mTitleTv;

    private int mTotalPage = 1;

    private Map<Integer, ArrayList<Post>>  mMapPostPerPage = new LinkedHashMap() {
        public boolean removeEldestEntry(Map.Entry eldest) {
            return size() > Page3Fragment.MAX_ENTRIES;
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
            url = getArguments().getString(URL_PARAM);

            if (!url.contains(Global.URL) && !url.contains(Global.URL2)) {
                url = Global.URL + url;
            }
        }
    }

    @Override
    public View  onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_page3, container, false);
        mTitleTv = (TextView) mRootView.findViewById(R.id.tv_title);
        settings = getActivity().getSharedPreferences("Setting", 0);
        Global.iTheme = this.settings.getInt("THEME", 1);
//        readSettings();

        this.mLayoutProgress = (LinearLayout) mRootView.findViewById(R.id.layoutprogress);
        Global.iDensity = getResources().getDisplayMetrics().density;

        this.bmImageStart = BitmapFactory.decodeResource(getResources(), R.drawable.stub_image);

        this.mParser = new HtmlParser(Global.URL);
        this.mParser.setUrl(url);


//        Toolbar toolbar = (Toolbar) mRootView.findViewById(R.id.my_awesome_toolbar);
//        toolbar.setTitle(mParam1);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        mTitleTv.setText(mParam1);
        mViewPager = (ViewPager) mRootView.findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(1);
        mPage3PagerAdapter = new Page3PagerAdapter(getActivity(), mTotalPage, url, bmImageStart, 1.0f, mViewPager);
        mPagerListener = new PagerListener() {
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
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                            break;
                        }
                        case BasePagerAdapter.GO_NEXT: {
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
//        mPage3PagerAdapter.setPagerListener(new PagerListener() {
//            @Override
//            public View findPageView(int curPage) {
//                return mViewPager.findViewWithTag(curPage);
//            }
//
//            @Override
//            public void onSwipeTReresh(SwipeRefreshLayout swipeRefreshLayout) {
//
//            }
//
//            @Override
//            public void onGoPage(int type) {
//                try {
//                    switch (type) {
//                        case BasePagerAdapter.GO_FIRST: {
//                            if (mViewPager.getCurrentItem() != 0) {
//                                mViewPager.setCurrentItem(0);
//                            }
//                            break;
//                        }
//                        case BasePagerAdapter.GO_LAST:{
//                            int lasItemIndex = mPage3PagerAdapter.getTotalPage() - 1;
//                            if (mViewPager.getCurrentItem() != lasItemIndex) {
//                                mViewPager.setCurrentItem(lasItemIndex);
//                            }
//                            break;
//                        }
//                        case BasePagerAdapter.GO_PREVIOUS:{
//                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
//                            break;
//                        }
//                        case BasePagerAdapter.GO_NEXT: {
//                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
//                            break;
//
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void showDialogGoPage() {
//
//            }
//        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                loadPage(position + 1, false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setAdapter(mPage3PagerAdapter);
        //init page 1
        loadPage(1, false);
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
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
                if (page >= 0 && page < mPage3PagerAdapter.getTotalPage()) {
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


    private HtmlLoader.HtmlLoaderListener htmlLoaderListener = new HtmlLoader.HtmlLoaderListener() {
        @Override
        public void onCallback(Document doc, int curPage) {
            try {
                parseDataPage3(doc, true, curPage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void loadPage(final int curPage, boolean refres) {
        try {
            if (refres || !mMapPostPerPage.containsKey(Integer.valueOf(curPage))) {
                Log.i(TAG, "Load page : " + curPage);
                final String url = getUrlWithPage(curPage);
                if (url != null) {
                    HtmlLoader.getInstance().fetchData(url, curPage, htmlLoaderListener);
                }
            } else {
                refreshCurrentPage(curPage, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUrlWithPage(int mPage) {
        if (!url.contains("&page=")) {
            url = url.concat("&page=1");
        }
        String concat = url.substring(0, url.lastIndexOf("=") + 1).concat(String.valueOf(mPage));
        return concat.contains("&page=0") ? concat.split("&page")[0] : concat;
    }

    void parseDataPage3(Document doc, final boolean refresh, final int curPage) throws Exception {
        if (doc != null) {
//            UserInfo mUser = new UserInfo();
//            String mPostId;
            Post post;
            ArrayList<Post> mListPost = new ArrayList<>();
            Element navigationElement = doc.select("div[class=pagenav").first();
            if (navigationElement != null) {
                String strPage = navigationElement.select("td[class=vbmenu_control]").text();
                mTotalPage = Integer.parseInt(strPage.split(" ")[3]);
            }

            Element contentElement = doc.select("div[align=center]").size() > 1 ? (Element) doc.select("div[align=center]").get(1) : doc;
//                    Element r0 = element.select("a[href=private.php]").first();
//            mPostId = getPostIdFromUrl(url);
//                    if (r0 != null) {
//                        mUser.setUserId(((Element) element.select("td[class=alt2]").get(0)).select("a[href*=mem]").attr("href").split("=")[1]);
//                        r0 = element.select("input[name*=securitytoken]").first();
//                        if (r0 != null) {
//                            mUser.setToken(r0.attr("value"));
//                        }
//                        r0 = doc.select("div[style=margin-top:6px]:has(input[value=Post Quick Reply])").first();
//                        if (r0 != null) {
//                            String sIdThread = r0.select("input[name=t]").attr("value");
//                        }
//                        r0 = doc.select("td[class=alt1]:has(a[href *= subscription.php]").first();
//                        if (r0 != null) {
//                            if (r0.select("a[href *= subscription.php]").attr("href").contains("removesubscription")) {
//                                this.isSubscribe = true;
//                                ((SidebarAdapter.Item) this.mListSideMenu2.get(3)).mTitle = getResources().getString(R.string.UnSubscribe);
//                                this.mAdapterSideMenu2.notifyDataSetInvalidated();
//                            } else {
//                                ((SidebarAdapter.Item) this.mListSideMenu2.get(3)).mTitle = getResources().getString(R.string.Subscribe);
//                                this.mAdapterSideMenu2.notifyDataSetInvalidated();
//                                this.isSubscribe = false;
//                            }
//                        }
//                    }
            Elements postElementsList = contentElement.select("td[id*=td_post]");
            Iterator it = postElementsList.iterator();
            String userTitle = null;
            while (it.hasNext()) {
                String username;
                String avatarUrl = null;
                post = new Post();
                String time = "";
                Element postElement = (Element) it.next();
                Element parent = postElement.parent();
//                        if (select.select("div[class=smallfont]:has(strong)").first() != null) {
//                            this.mTextTitle = select.select("div[class=smallfont]:has(strong)").first().text();
//                            setTitle(this.mTextTitle);
//                        }
                //===================Parse header================
                Element headerElement = parent.previousElementSibling();
                Element postTimeElement = headerElement.previousElementSibling();
                if (headerElement.select("img[src*=avatars]").first() != null) {
                    avatarUrl = headerElement.select("img[src*=avatars]").attr("src");
                    if (!avatarUrl.contains(Global.URL)) {
                        avatarUrl = Global.URL + avatarUrl;
                    }
                }
                if (headerElement.select("div:containsOwn(Join Date)").first() != null) {
                    String joinDate = headerElement.select("div:containsOwn(Join Date)").first().text();
                    if (joinDate.contains("Date:")) {
                        joinDate = joinDate.split("Date:")[1];
                    }
                    post.setJD("Jd:" + joinDate);
                } else {
                    post.setJD("");
                }
                if (headerElement.select("div:containsOwn(Posts: )").first() != null) {
                    post.setPosts(headerElement.select("div:containsOwn(Posts: )").first().text());
                } else {
                    post.setPosts("");
                }
                if (headerElement.select("img[src*=line.gif").first() != null) {
                    if (headerElement.select("img[src*=line.gif").attr("src").contains("online")) {
                        post.isOnline = true;
                    } else {
                        post.isOnline = false;
                    }
                }
                if (headerElement.select("a[class=bigusername]").first() != null) {
                    username = headerElement.select("a[class=bigusername]").text();
                    post.m_UserId = headerElement.select("a[class=bigusername]").first().attr("href").split("u=")[1];
                } else {
                    username = userTitle;
                }
                userTitle = headerElement.select("div[class=smallfont]").first() != null ? headerElement.select("div[class=smallfont]").first().text() : time;
                time = postTimeElement.text();
                post.Info(username, userTitle, time, avatarUrl);

                //===================Parse body================
                Element messageElement = postElement.select("div[id*=post_message").first();
                if (messageElement != null) {
                    if (messageElement.attr("id").split("_").length > 2) {
                        post.setId(messageElement.attr("id").split("_")[2]);
                    }
                    parseMessagePage3(messageElement, post, false);
                }
                Element fieldSetElement = postElement.select("fieldset[class=fieldset]").first();
                if (fieldSetElement != null) {
                    post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                    parseMessagePage3(fieldSetElement, post, false);
                }

                if (Global.bSign && postElement.select("div:contains(_______)").first() != null) {
                    post.addText("\n");
                    parseMessagePage3(postElement.select("div:contains(_______)").first(), post, false);
                }
                post.initContent();
                mListPost.add(post);
//                        if (post.Id().equals(mPostId)) {
//                            userTitle = username;
//                        } else {
//
//                            userTitle = username;
//                        }
            }
            mMapPostPerPage.put(Integer.valueOf(curPage), mListPost);
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPage3PagerAdapter.setTotalPage(mTotalPage);
                refreshCurrentPage(curPage, refresh);
            }
        });
    }

    private void refreshCurrentPage(final int curPage, boolean forceRefresh) {
        try {
            Log.i(TAG, "Refresh page :  " + curPage);
            View page = mViewPager.findViewWithTag(curPage);
            if (page != null) {
                ListView listView = (ListView) page.findViewById(R.id.content_frame);
                final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) page.findViewById(R.id.swipe_refresh_layout);
                refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadPage(curPage, true);
                        refreshLayout.setRefreshing(false);
                    }
                });
                NavigationBar navigationHeaderBar = (NavigationBar) page.findViewWithTag(BasePagerAdapter.TAG_NAVIGATION_HEADER);
                navigationHeaderBar.refresh(curPage, mTotalPage);
                navigationHeaderBar.setPagerListener(mPagerListener);
                NavigationBar navigationFooterBar = (NavigationBar) page.findViewWithTag(BasePagerAdapter.TAG_NAVIGATION_FOOTER);
                navigationFooterBar.refresh(curPage, mTotalPage);
                navigationFooterBar.setPagerListener(mPagerListener);
                (page.findViewById(R.id.layout_progress)).setVisibility(View.GONE); // gone progress
                if ((forceRefresh || listView.getAdapter() == null) && mMapPostPerPage.containsKey(Integer.valueOf(curPage))) {
                    final Page3ListViewAdapter adapter = new Page3ListViewAdapter(getActivity(), mMapPostPerPage.get(Integer.valueOf(curPage)), null, null, 1.0f);
                    listView.setAdapter(adapter);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseMessagePage3(Element element, Post post, boolean isGetWholeText) {
        try {
            if (element != null) {
                for (Node node : element.childNodes()) {
                    if (node instanceof Element) {
                        if (((Element) node).tagName().equals("div")) {
                            Element first = ((Element) node).select("div").first();
                            if (first.attr("style").contains("padding")) {
                                post.addText("\n");
                            }
                            if (first.ownText().contains("Originally Posted by")) {
                                post.addText("Originally Posted by ");
                                int length = post.getText().length();
                                post.addText(first.select("strong").text());
                                int length2 = post.getText().length();
                                post.web.add(Global.URL + first.select("a").attr("href"), length, length2);
                                post.type.add("", length, length2, 1);
                                post.addText("\n");
                            } else {
                                parseMessagePage3(first, post, isGetWholeText);
                                post.addText("\n");
                            }
                        } else if (((Element) node).tagName().equals("blockquote")) {
                            Element first = ((Element) node).select("blockquote").first();
                            post.addText("\n");
                            parseMessagePage3(first, post, isGetWholeText);
                            post.addText("\n");
                        } else if (((Element) node).tagName().equals("fieldset")) {
                            Element first = ((Element) node).select("fieldset").first();
                            post.addText("\n");
                            parseMessagePage3(first, post, isGetWholeText);
                            post.addText("\n");
                        } else if (((Element) node).tagName().equals("b")) {
                            Element first = ((Element) node).select("b").first();
                            int length = post.getText().length();
                            parseMessagePage3(first, post, isGetWholeText);
                            post.type.add("", length, post.getText().length(), 1);
                        } else if (((Element) node).tagName().equals("i")) {
                            Element first = ((Element) node).select("i").first();
                            int length = post.getText().length();
                            parseMessagePage3(first, post, isGetWholeText);
                            post.type.add("", length, post.getText().length(), 2);
                        } else if (((Element) node).tagName().equals("pre")) {
                            Element first = ((Element) node).select("pre").first();
                            int length = post.getText().length();
                            int startQuote = post.getLastTextItem().mData.length();
                            parseMessagePage3(first, post, isGetWholeText);
                            post.quote.add("", length, post.getText().length());
                            int endQuote = post.getLastTextItem().mData.length();
                            post.getLastTextItem().addQuote(startQuote, endQuote);

                        } else if (((Element) node).tagName().equals("table")) {
                            Element first = ((Element) node).select("table").first();
                            int length = post.getText().length();
                            int startQuote = post.getLastTextItem().mData.length();
                            parseMessagePage3(first, post, isGetWholeText);
                            post.quote.add("", length, post.getText().length());
                            int endQuote = post.getLastTextItem().mData.length();
                            post.getLastTextItem().addQuote(startQuote, endQuote);
                        } else if (((Element) node).tagName().equals("ol")) {
                        } else if (((Element) node).tagName().equals("tbody")) {
                            Element first = ((Element) node).select("tbody").first();
                            parseMessagePage3(first, post, isGetWholeText);
                        } else if (((Element) node).tagName().equals("li")) {
                            Element first = ((Element) node).select("li").first();
                            parseMessagePage3(first, post, isGetWholeText);
                        } else if (((Element) node).tagName().equals("tr")) {
                            Element first = ((Element) node).select("tr").first();
                            post.addText("\n");
                            parseMessagePage3(first, post, isGetWholeText);
                        } else if (((Element) node).tagName().equals("td")) {
                            Element first = ((Element) node).select("td").first();
                            parseMessagePage3(first, post, isGetWholeText);
                        } else if (((Element) node).tagName().equals("img")) { // parse image tag
                            String imageUrl = ((Element) node).select("img[src]").attr("src");
                            if (imageUrl.contains(Global.URL) && imageUrl.subSequence(0, 21).equals(Global.URL) && !imageUrl.contains("https://vozforums.com/attachment.php?attachmentid") && !imageUrl.contains("https://vozforums.com/customavatars/")) {
                                imageUrl = imageUrl.substring(21);
                            }
                            if (imageUrl.substring(0, 1).equals("/")) {
                                imageUrl = imageUrl.substring(1, imageUrl.length());
                            }
                            int messageLength = post.getText().length();
                            int imageUrlLength = imageUrl.length();
                            if (imageUrl.contains("http://") || imageUrl.contains("https://") || imageUrl.contains("attachment.php?attachmentid")) {
//                                post.image.add(imageUrl, messageLength, imageUrlLength + messageLength, null);
                                post.addPhoto(imageUrl);
                            } else if (imageUrl.contains(Post.EMO_PREFIX)) {
                                post.addEmo(imageUrl);
                                post.image.add(imageUrl, messageLength, messageLength + 2, null);
                                imageUrl = "  ";
//                                if (/*!imageUrl.contains("images/buttons/viewpost.gif") &&*/) {
//                                }
                                post.addText(imageUrl);
                                if (node.hasAttr("onload")) {
                                    post.addText("\n");
                                }
                            }
                        } else if (((Element) node).tagName().equals("br")) {
                            post.addText("\n");
                        } else if (((Element) node).tagName().equals("u")) {
                            Element first = ((Element) node).select("u").first();
                            int length = post.getText().length();
                            parseMessagePage3(first, post, isGetWholeText);
                            post.typeU.add("", length, post.getText().length());
                        } else if (((Element) node).tagName().equals("font")) {
                            Element first = ((Element) node).select("font").first();
                            String str = "while";
                            String r1 = "3";
                            if (((Element) node).select("font[color]").first() != null) {
                                str = ((Element) node).select("font[color]").attr("color");
                            }
                            String attr = ((Element) node).select("font[size]").first() != null ? ((Element) node).select("font[size]").attr("size") : r1;
                            int length2 = post.getText().length();
                            parseMessagePage3(first, post, isGetWholeText);
                            post.font.add("", length2, post.getText().length(), str, Integer.parseInt(attr));
                        } else if (((Element) node).tagName().equals("a")) {
                            Element first = ((Element) node).select("a[href]").first();
                            if (first.select("img").first() == null) {
                                String r0;
                                r0 = ((Element) node).select("a[href]").attr("href").replace("%3A", ":").replace("%2F", "/").replace("%3F", "?").replace("%3D", "=").replace("%26", "&");
                                if (r0.contains("mailto:")) {
                                    String r1 = r0.substring(7, r0.length());
                                    r0 = ((Element) node).select("a[href]").text();
                                    int length2 = post.getText().length();
                                    post.web.add(r1, length2, r0.length() + length2);
                                    post.addText(r0);
                                } else if (r0.contains("http")) {
                                    String r1 = r0.substring(r0.indexOf("http"), r0.length());
                                    r0 = ((Element) node).select("a[href]").text();
                                    int length2 = post.getText().length();
                                    post.web.add(r1, length2, r0.length() + length2);
                                    post.addText(r0);
                                }
                            } else {
                                parseMessagePage3(first, post, true);
                            }
                        } else {
                            post.addText(((Element) node).text());
                        }
                    } else if (node instanceof TextNode) {
                        // IMPORTANT: add plain message !!!
                        if (isGetWholeText) {
                            post.addText(((TextNode) node).getWholeText());
                        } else {
                            post.addText(((TextNode) node).text());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
}
