package com.nna88.voz.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.nna88.voz.contain.Forum;
import com.nna88.voz.listview.listViewCustom1;
import com.nna88.voz.util.ChangeLog;
import java.util.ArrayList;
import java.util.Iterator;
import org.jsoup.nodes.Element;

public class Page1 extends Page {
    private ArrayList<Forum> ListContains;
    private listViewCustom1 adapter;
    private StringBuilder sForum;

    public Page1() {
        this.sForum = new StringBuilder();
    }

    void Parser(int i) throws Exception {
        super.Parser(i);
        if (this.doc != null) {
            Element first;
            this.scrollIsComputed = false;
            this.ListContains.clear();
            if (this.doc.select("a[href=private.php]").first() != null) {
                this.mUser.setUserId(((Element) this.doc.select("td[class=alt2]").get(0)).select("a[href*=mem]").attr("href").split("=")[1]);
                first = this.doc.select("input[name*=securitytoken]").first();
                if (first != null) {
                    this.mUser.setToken(first.attr("value"));
                }
            }
            Iterator it = this.doc.select("td[class*=tcat]").iterator();
            String str = null;
            String str2 = null;
            String str3 = null;
            while (it.hasNext()) {
                first = (Element) it.next();
                if (!first.text().contains("Welcome to the vozForums")) {
                    this.ListContains.add(new Forum(first.text(), first.select("[href~=forum]").attr("href"), null));
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
                            this.ListContains.add(new Forum(str, str3, str2));
                            this.sForum.append(str).append(",").append(str3).append(";");
                        }
                    }
                }
            }
            if (this.ListContains.size() < 2) {
                parserNotification();
                return;
            }
            writeStringForums(this.sForum.toString());
            this.mItemCount = this.adapter.getCount();
            if (this.mItemOffsetY == null) {
                this.mItemOffsetY = new int[(this.mItemCount + 1)];
                this.mItemtemp = new int[(this.mItemCount + 1)];
            }
            this.scrollIsComputed = true;
            this.adapter.notifyDataSetChanged();
        }
    }

    protected void TaskCancle() {
        super.TaskCancle();
        if (this.mTask != null) {
            this.mTask.cancel(true);
        }
        this.ListContains.clear();
        if (this.adapter != null) {
            this.adapter.notifyDataSetChanged();
        }
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 4) {
            finish();
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.iPage = 1;
        this.cl = new ChangeLog(this);
        if (this.cl.firstRun()) {
            this.cl.getLogDialog().show();
        }
        this.ListContains = new ArrayList();
        this.adapter = new listViewCustom1(this.mContext, this.ListContains);
        this.adapter.setSize(this.mTextSize);
        this.mObjectAdapter = this.adapter;
        if (getIntent().getBooleanExtra("Exit me", false)) {
            finish();
            return;
        }
        this.mList.setAdapter(this.adapter);
        Uri data = getIntent().getData();
        if (data != null) {
            String uri = data.toString();
            Intent intent;
            if (uri.contains("forumdisplay.php")) {
                intent = new Intent(this.mContext, Page2.class);
                intent.putExtra("URL", uri);
                intent.putExtra("TITLE", BuildConfig.FLAVOR);
                startActivityForResult(intent, 4);
            } else if (uri.contains("showthread.php") || uri.contains("showpost.php")) {
                intent = new Intent(this.mContext, Page3.class);
                intent.putExtra("URL", uri);
                intent.putExtra("TITLE", BuildConfig.FLAVOR);
                startActivityForResult(intent, 4);
            } else if (uri.contains("index.php")) {
                this.mTask.execute(new Integer[]{Integer.valueOf(0)});
            } else if (uri.equals("https://vozforums.com")) {
                this.mTask.execute(new Integer[]{Integer.valueOf(0)});
            } else if (uri.equals("http://www.vozforums.com")) {
                this.mTask.execute(new Integer[]{Integer.valueOf(0)});
            } else {
                toast("link error:" + uri);
                finish();
            }
        } else {
            this.mParser.setUrl("https://vozforums.com");
            this.mTask.execute(new Integer[]{Integer.valueOf(0)});
        }
        this.mList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (((Forum) Page1.this.ListContains.get(i)).UrlForum() != null) {
                    Intent intent = new Intent(Page1.this.mContext, Page2.class);
                    intent.putExtra("URL", ((Forum) Page1.this.ListContains.get(i)).UrlForum());
                    intent.putExtra("TITLE", ((Forum) Page1.this.ListContains.get(i)).Forum());
                    Page1.this.startActivity(intent);
                }
            }
        });
        loadAds();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onRestart() {
        super.onRestart();
        updateSetFont(this.mTextSize);
    }

    protected void updateSetFont(float f) {
        super.updateSetFont(f);
        this.adapter.setSize(f);
        this.adapter.notifyDataSetChanged();
    }
}
