package com.whoami.voz.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.whoami.voz.R;
import com.whoami.voz.ui.adapter.list.Page1ListViewAdapter;
import com.whoami.voz.ui.contain.Forum;
import com.whoami.voz.ui.utils.HtmlLoader;
import com.whoami.voz.ui.utils.UserInfo;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by thaidh on 9/3/16.
 */
public class Page1Activity extends BaseActivity {
    private boolean scrollIsComputed;
    private UserInfo mUser;
    private ListView mList;
    private ArrayList<Forum> forumsList;
    private Page1ListViewAdapter adapter;
    private StringBuilder sForum;
    private LinearLayout mLayoutProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_page1);
        setupToolbar();
        initLayout();
        initData();
        initQuickReturn();
    }

    private void initLayout() {

        mList = (ListView) findViewById(R.id.content_frame);
        mLayoutProgress = (LinearLayout) findViewById(R.id.layoutprogress);
        this.forumsList = new ArrayList();
        this.adapter = new Page1ListViewAdapter(this, this.forumsList);
        this.mList.setAdapter(this.adapter);
        showLoadingView(true);

        this.mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (forumsList.get(i).UrlForum() != null) {
                    Intent intent = new Intent(Page1Activity.this, Page2Activity.class);
                    intent.putExtra(EXTRA_URL, forumsList.get(i).UrlForum());
                    intent.putExtra(EXTRA_TITLE, forumsList.get(i).Forum());
                    startActivity(intent);
                }
            }
        });
    }

    private void initData() {
        HtmlLoader.getInstance().fetchData("https://vozforums.com", 0, new HtmlLoader.HtmlLoaderListener() {
            @Override
            public void onCallback(Document doc, int page) {
                try {
                    parseDataPage1(doc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void parseDataPage1(Document doc) throws Exception {
        try {
            if (doc != null) {
                Element first;
                forumsList.clear();
                //                if (this.doc.select("a[href=private.php]").first() != null) {
                //                    this.mUser.setUserId(((Element) this.doc.select("td[class=alt2]").get(0)).select("a[href*=mem]").attr("href").split("=")[1]);
                //                    first = this.doc.select("input[name*=securitytoken]").first();
                //                    if (first != null) {
                //                        this.mUser.setToken(first.attr("value"));
                //                    }
                //                }
                Iterator it = doc.select("td[class*=tcat]").iterator();
                String str = null;
                String str2 = null;
                String str3 = null;
                while (it.hasNext()) {
                    first = (Element) it.next();
                    if (!first.text().contains("Welcome to the vozForums")) {
                        forumsList.add(new Forum(first.text(), first.select("[href~=forum]").attr("href"), null));
                        first = first.parent().parent().nextElementSibling();
                        if (first != null) {
                            Iterator it2 = first.select("tr").iterator();
                            while (it2.hasNext()) {
                                first = ((Element) it2.next()).select("div:has(a[href*=forumdisplay]").first();
                                if (first != null) {
                                    str = first.select("a").text();
                                    str3 = first.select("a").attr("href");
                                    str2 = first.select("span").text();
                                }
                                forumsList.add(new Forum(str, str3, str2));
                                //                                this.sForum.append(str).append(",").append(str3).append(";");
                            }
                        }
                    }
                }
                if (forumsList.size() < 2) {
                    //                    parserNotification();
                    return;
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showLoadingView(false);
                            adapter.setData(forumsList);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showLoadingView(boolean isShow) {
        if (mLayoutProgress != null) {
            mLayoutProgress.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
    }
}
