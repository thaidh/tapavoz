package com.whoami.voz.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.whoami.voz.ui.contain.VozThread;
import com.whoami.voz.ui.delegate.PagerListener;
import com.whoami.voz.ui.fragment.Page3Fragment;
import com.whoami.voz.ui.main.Global;
import com.whoami.voz.ui.mysqlite.DatabaseHelper;
import com.whoami.voz.ui.parserhtml.HtmlParser;
import com.whoami.voz.ui.utils.HtmlLoader;
import com.whoami.voz.ui.utils.Util;
import com.whoami.voz.ui.widget.NavigationBar;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by thaidh on 9/3/16.
 */
public class Page3Activity extends BaseActivity {
    public static final String TAG = Page3Activity.class.getSimpleName();
    private Bitmap bmImageStart;

    // base activity
    //    private ListView mList;
    private LinearLayout mLayoutProgress;
    private ViewPager mViewPager;
    private Page3PagerAdapter mPage3PagerAdapter;
    private PagerListener mPagerListener;
    private int mTotalPage = 1;

    private Map<Integer, ArrayList<Post>> mMapPostPerPage = new LinkedHashMap() {
        public boolean removeEldestEntry(Map.Entry eldest) {
            return size() > Page3Fragment.MAX_ENTRIES;
        }
    };
    private String mUrl;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_page3);
        setupToolbar();
        initLayout();
        initData();
        initQuickReturn();
    }

    private void initLayout() {
//        readSettings();

        this.mLayoutProgress = (LinearLayout) findViewById(R.id.layoutprogress);
        Global.iDensity = getResources().getDisplayMetrics().density;

        this.bmImageStart = BitmapFactory.decodeResource(getResources(), R.drawable.stub_image);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(1);
        mPage3PagerAdapter = new Page3PagerAdapter(this, mTotalPage, mUrl, bmImageStart, 1.0f, mViewPager);
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

    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (!bundle.getString(EXTRA_URL).contains(Global.URL)) {
                mUrl = Global.URL + bundle.getString(EXTRA_URL);
            } else {
                mUrl = bundle.getString(EXTRA_URL);
            }
            mTitle = bundle.getString(EXTRA_TITLE);
        }
        mToolbar.setTitle(mTitle);
        //init page 1
        loadPage(1, false);
    }

    protected void alertGoPage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = getLayoutInflater().inflate(R.layout.login, null);
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
        if (!mUrl.contains("&page=")) {
            mUrl = mUrl.concat("&page=1");
        }
        String concat = mUrl.substring(0, mUrl.lastIndexOf("=") + 1).concat(String.valueOf(mPage));
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
                        if (parent.select("div[class=smallfont]:has(strong)").first() != null) {
                            mTitle = parent.select("div[class=smallfont]:has(strong)").first().text();
                        }
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
                    post.addText("\n", false);
                    parseMessagePage3(fieldSetElement, post, false);
                }

                if (Global.bSign && postElement.select("div:contains(_______)").first() != null) {
                    post.addText("\n", false);
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToolbar.setTitle(mTitle);
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
                    final Page3ListViewAdapter adapter = new Page3ListViewAdapter(this, mMapPostPerPage.get(Integer.valueOf(curPage)), null, null, 1.0f);
                    listView.setAdapter(adapter);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseMessagePage3(Element element, Post post, boolean isGetWholeText) {
        parseMessagePage3(element, post, isGetWholeText, false);
    }

    private void parseMessagePage3(Element element, Post post, boolean isGetWholeText, boolean isQuote) {
        try {
            if (element != null) {
                for (Node node : element.childNodes()) {
                    if (node instanceof Element) {
                        if (((Element) node).tagName().equals("div")) {
                            Element first = ((Element) node).select("div").first();
                            if (first.attr("style").contains("padding")) {
                                post.addText("\n", isQuote);
                            }
                            if (first.ownText().contains("Originally Posted by")) {
                                post.addText("Originally Posted by ", isQuote);
                                int length = post.getText().length();
                                post.addText(first.select("strong").text(), isQuote);
                                int length2 = post.getText().length();
                                post.web.add(Global.URL + first.select("a").attr("href"), length, length2);
                                post.type.add("", length, length2, 1);
                                post.addText("\n", isQuote);
                            } else {
                                parseMessagePage3(first, post, isGetWholeText, isQuote);
                                post.addText("\n", isQuote);
                            }
                        } else if (((Element) node).tagName().equals("blockquote")) {
                            Element first = ((Element) node).select("blockquote").first();
                            post.addText("\n", isQuote);
                            parseMessagePage3(first, post, isGetWholeText, isQuote);
                            post.addText("\n", isQuote);
                        } else if (((Element) node).tagName().equals("fieldset")) {
                            Element first = ((Element) node).select("fieldset").first();
                            post.addText("\n", isQuote);
                            parseMessagePage3(first, post, isGetWholeText, isQuote);
                            post.addText("\n", isQuote);
                        } else if (((Element) node).tagName().equals("b")) {
                            Element first = ((Element) node).select("b").first();
                            int length = post.getText().length();
                            parseMessagePage3(first, post, isGetWholeText, isQuote);
                            post.type.add("", length, post.getText().length(), 1);
                        } else if (((Element) node).tagName().equals("i")) {
                            Element first = ((Element) node).select("i").first();
                            int length = post.getText().length();
                            parseMessagePage3(first, post, isGetWholeText, isQuote);
                            post.type.add("", length, post.getText().length(), 2);
                        }
                        /*else if (((Element) node).tagName().equals("pre")) {
                            Element first = ((Element) node).select("pre").first();
                            int length = post.getText().length();
                            int startQuote = post.getLastTextItem().mData.length();
                            parseMessagePage3(first, post, isGetWholeText, isQuote);
                            post.quote.add("", length, post.getText().length());
                            int endQuote = post.getLastTextItem().mData.length();
                            post.getLastTextItem().addQuote(startQuote, endQuote);

                        }*/ else if (((Element) node).tagName().equals("table")) {
                            Element first = ((Element) node).select("table").first();
//                            int length = post.getText().length();
//                            int startQuote = post.getLastTextItem().mData.length();
                            parseMessagePage3(first, post, isGetWholeText, true);
//                            post.quote.add("", length, post.getText().length());
//                            int endQuote = post.getLastTextItem().mData.length();
//                            post.getLastTextItem().addQuote(startQuote, endQuote);
                        } else if (((Element) node).tagName().equals("ol")) {
                        } else if (((Element) node).tagName().equals("tbody")) {
                            Element first = ((Element) node).select("tbody").first();
                            parseMessagePage3(first, post, isGetWholeText, isQuote);
                        } else if (((Element) node).tagName().equals("li")) {
                            Element first = ((Element) node).select("li").first();
                            parseMessagePage3(first, post, isGetWholeText, isQuote);
                        } else if (((Element) node).tagName().equals("tr")) {
                            Element first = ((Element) node).select("tr").first();
                            post.addText("\n", isQuote);
                            parseMessagePage3(first, post, isGetWholeText, isQuote);
                        } else if (((Element) node).tagName().equals("td")) {
                            Element first = ((Element) node).select("td").first();
                            parseMessagePage3(first, post, isGetWholeText, isQuote);
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
                                post.addEmo(imageUrl, isQuote);
                                post.image.add(imageUrl, messageLength, messageLength + 2, null);
                                imageUrl = "  ";
//                                if (/*!imageUrl.contains("images/buttons/viewpost.gif") &&*/) {
//                                }
                                post.addText(imageUrl, isQuote);
                                if (node.hasAttr("onload")) {
                                    post.addText("\n", isQuote);
                                }
                            }
                        } else if (((Element) node).tagName().equals("br")) {
                            post.addText("\n", isQuote);
                        } else if (((Element) node).tagName().equals("u")) {
                            Element first = ((Element) node).select("u").first();
                            int length = post.getText().length();
                            parseMessagePage3(first, post, isGetWholeText, isQuote);
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
                            parseMessagePage3(first, post, isGetWholeText, isQuote);
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
                                    post.addText(r0 , isQuote);
                                } else if (r0.contains("http")) {
                                    String r1 = r0.substring(r0.indexOf("http"), r0.length());
                                    r0 = ((Element) node).select("a[href]").text();
                                    int length2 = post.getText().length();
                                    post.web.add(r1, length2, r0.length() + length2);
                                    post.addText(r0, isQuote);
                                }
                            } else {
                                parseMessagePage3(first, post, true, isQuote);
                            }
                        } else {
                            post.addText(((Element) node).text(), isQuote);
                        }
                    } else if (node instanceof TextNode) {
                        // IMPORTANT: add plain message !!!
                        if (isGetWholeText) {
                            post.addText(((TextNode) node).getWholeText(), isQuote);
                        } else {
                            post.addText(((TextNode) node).text(), isQuote);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bookmark, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_bookmark) {
            VozThread thread = new VozThread(mTitle, mUrl, "");
            DatabaseHelper.getInstance(this).insertBookmark(thread);
            Util.showMess("Bookmark : " + mUrl);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
