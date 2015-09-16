package com.nna88.voz.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import com.nna88.voz.contain.Thread;
import com.nna88.voz.listview.listViewCustom2;
import com.nna88.voz.mysqlite.Comment;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.jsoup.nodes.Element;

public class PageRecentPost extends Page {
    static final String urlListSubscribe = "https://vozforums.com/subscription.php?do=viewsubscription";
    static final String urlNewSubscribe = "https://vozforums.com/usercp.php";
    private ArrayList<Thread> ListContains;
    private listViewCustom2 adapter;
    private int iNumquote;
    List<Comment> lComment;

    public PageRecentPost() {
        this.iNumquote = 0;
    }

    void Parser(int i) throws Exception {
        super.Parser(i);
        if (this.doc != null) {
            String str;
            Bitmap bitmap;
            String str2;
            String str3;
            this.ListContains.clear();
            Iterator it = this.doc.select("table[id]").iterator();
            setPage(parsePage(0, 0));
            if (it.hasNext()) {
                it.next();
                str = null;
                bitmap = null;
                str2 = null;
                str3 = null;
            } else {
                str = null;
                bitmap = null;
                str2 = null;
                str3 = null;
            }
            while (it.hasNext()) {
                it.hasNext();
                Element element = (Element) it.next();
                Element first = element.select("td[class=thead]").first();
                if (first != null) {
                    str2 = ("Forums: " + first.select("a").first().text()) + IOUtils.LINE_SEPARATOR_UNIX + first.ownText();
                }
                if (element.select("a[href*=showthread.php]").first() != null) {
                    str = element.select("a[href*=showthread.php]").first().text();
                    str3 = element.select("a[href*=showthread.php]").first().attr("href");
                }
                if (element.select("em").first() != null) {
                    str2 = str2 + IOUtils.LINE_SEPARATOR_UNIX + element.select("em").first().ownText();
                    str3 = element.select("em").first().select("a").attr("href");
                }
                if (Global.bTopicHeader && element.select("div:has(img[src*=statusicon])").first() != null) {
                    bitmap = loadBitmapAssert(element.select("div:has(img[src*=statusicon])").first().select("img").first().attr("src"));
                }
                this.ListContains.add(new Thread(str, str2, null, null, bitmap, str3, null));
            }
            this.mItemCount = this.adapter.getCount();
            if (this.mItemOffsetY == null) {
                this.mItemOffsetY = new int[(this.mItemCount + 1)];
                this.mItemtemp = new int[(this.mItemCount + 1)];
            }
            this.scrollIsComputed = true;
            this.adapter.notifyDataSetChanged();
            this.mList.clearFocus();
            this.mList.post(new Runnable() {
                public void run() {
                    PageRecentPost.this.mList.setSelection(0);
                }
            });
        }
    }

    protected void onCreate(Bundle bundle) {
        this.iPage = 8;
        super.onCreate(bundle);
        this.ListContains = new ArrayList();
        this.adapter = new listViewCustom2(this.mContext, this.ListContains);
        this.adapter.setSize(this.mTextSize);
        this.mObjectAdapter = this.adapter;
        this.mList.setAdapter(this.adapter);
        this.mParser.setUserRecentPost(getIntent().getStringExtra("USERID"));
        this.mTask.execute(new Integer[]{Integer.valueOf(11)});
        this.mList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Exception e;
                try {
                    Intent intent;
                    PageRecentPost.this.vibrate();
                    if (((Thread) PageRecentPost.this.ListContains.get(i)).isUrl() == 0) {
                        intent = new Intent(PageRecentPost.this.mContext, Page2.class);
                        try {
                            if (((Thread) PageRecentPost.this.ListContains.get(i)).UrlThread() != null) {
                                intent.putExtra("URL", ((Thread) PageRecentPost.this.ListContains.get(i)).UrlThread());
                            } else {
                                return;
                            }
                        } catch (Exception e2) {
                            e = e2;
                            e.printStackTrace();
                        }
                    } else if (((Thread) PageRecentPost.this.ListContains.get(i)).isUrl() == 1) {
                        intent = new Intent(PageRecentPost.this.mContext, Page3.class);
                        if (((Thread) PageRecentPost.this.ListContains.get(i)).UrlLastPosst() == null || ((Thread) PageRecentPost.this.ListContains.get(i)).UrlLastPosst().equals(BuildConfig.FLAVOR)) {
                            if (((Thread) PageRecentPost.this.ListContains.get(i)).UrlThread() != null) {
                                intent.putExtra("URL", ((Thread) PageRecentPost.this.ListContains.get(i)).UrlThread());
                            } else {
                                return;
                            }
                        } else if (((Thread) PageRecentPost.this.ListContains.get(i)).UrlLastPosst() != null) {
                            intent.putExtra("URL", ((Thread) PageRecentPost.this.ListContains.get(i)).UrlLastPosst());
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                    intent.putExtra("TITLE", ((Thread) PageRecentPost.this.ListContains.get(i)).Thread());
                    PageRecentPost.this.startActivity(intent);
                    PageRecentPost.this.overridePendingTransition(R.anim.rail, R.anim.rail);
                } catch (Exception e3) {
                    e = e3;
                    e.printStackTrace();
                }
            }
        });
        this.mList.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
                PageRecentPost.this.vibrate();
                return false;
            }
        });
        this.lComment = this.mDataBookmark.getAllComments();
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
