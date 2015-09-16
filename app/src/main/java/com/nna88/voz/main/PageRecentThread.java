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
import com.nna88.voz.mysqlite.MySQLiteHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PageRecentThread extends Page {
    private ArrayList<Thread> ListContains;
    private listViewCustom2 adapter;
    List<Comment> lComment;

    void Parser(int i) throws Exception {
        super.Parser(i);
        if (this.doc != null) {
            this.ListContains.clear();
            Iterator it = this.doc.select("tr:has(td[id*=td_threadstatusicon])").iterator();
            setPage(parsePage(0, 0));
            String str = null;
            Bitmap bitmap = null;
            while (it.hasNext()) {
                String str2;
                it.hasNext();
                Element element = (Element) it.next();
                String str3 = BuildConfig.FLAVOR;
                String str4 = BuildConfig.FLAVOR;
                if (element.select("a[id*=thread_title]").first() != null) {
                    str3 = element.select("a[id*=thread_title]").first().text();
                    if (element.select("div:has(span[onclick*=member.php])").first() != null) {
                        str3 = str3 + " " + element.select("div:has(span[onclick*=member.php])").first().text();
                    }
                    str = element.select("a[id*=thread_title]").first().attr("href");
                    str2 = str3;
                } else {
                    str2 = str3;
                }
                Elements select = element.select("a[href*=forumdisplay]");
                if (select.size() > 1) {
                    str4 = "Forums: " + ((Element) select.get(1)).text() + IOUtils.LINE_SEPARATOR_UNIX;
                    str3 = ((Element) select.get(0)).text() + " - " + str2;
                    str2 = str4;
                } else if (select.size() == 1) {
                    String str5 = str2;
                    str2 = "Forums: " + ((Element) select.get(0)).text() + IOUtils.LINE_SEPARATOR_UNIX;
                    str3 = str5;
                } else {
                    str3 = str2;
                    str2 = str4;
                }
                if (element.select("div[style*=text-align:right]").first() != null) {
                    str2 = str2 + element.select("div[style*=text-align:right]").first().text() + IOUtils.LINE_SEPARATOR_UNIX;
                }
                if (element.select("td[title*=Replies]").first() != null) {
                    str2 = str2 + element.select("td[title*=Replies]").first().attr(MySQLiteHelper.COLUMN_TITLE);
                }
                if (Global.bTopicHeader && element.select("img[src*=statusicon]").first() != null) {
                    bitmap = loadBitmapAssert(element.select("img[src*=statusicon]").first().attr("src"));
                }
                this.ListContains.add(new Thread(str3, str2, null, null, bitmap, str, null));
            }
            this.mItemCount = this.adapter.getCount();
            if (this.mItemOffsetY == null) {
                this.mItemOffsetY = new int[(this.mItemCount + 1)];
                this.mItemtemp = new int[(this.mItemCount + 1)];
            }
            this.scrollIsComputed = true;
            this.adapter.notifyDataSetChanged();
            this.mList.clearFocus();
            this.mList.requestFocusFromTouch();
            this.mList.post(new Runnable() {
                public void run() {
                    PageRecentThread.this.mList.setSelection(0);
                    PageRecentThread.this.mList.clearFocus();
                }
            });
        }
    }

    protected void onCreate(Bundle bundle) {
        this.iPage = 9;
        super.onCreate(bundle);
        this.ListContains = new ArrayList();
        this.adapter = new listViewCustom2(this.mContext, this.ListContains);
        this.adapter.setSize(this.mTextSize);
        this.mObjectAdapter = this.adapter;
        this.mList.setAdapter(this.adapter);
        this.mTask.execute(new Integer[]{Integer.valueOf(12)});
        this.mList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                IndexOutOfBoundsException e;
                try {
                    Intent intent;
                    PageRecentThread.this.vibrate();
                    if (((Thread) PageRecentThread.this.ListContains.get(i)).isUrl() == 0) {
                        intent = new Intent(PageRecentThread.this.mContext, Page2.class);
                        try {
                            if (((Thread) PageRecentThread.this.ListContains.get(i)).UrlThread() != null) {
                                intent.putExtra("URL", ((Thread) PageRecentThread.this.ListContains.get(i)).UrlThread());
                            } else {
                                return;
                            }
                        } catch (IndexOutOfBoundsException e2) {
                            e = e2;
                            e.printStackTrace();
                        }
                    } else if (((Thread) PageRecentThread.this.ListContains.get(i)).isUrl() == 1) {
                        intent = new Intent(PageRecentThread.this.mContext, Page3.class);
                        if (((Thread) PageRecentThread.this.ListContains.get(i)).UrlLastPosst() == null || ((Thread) PageRecentThread.this.ListContains.get(i)).UrlLastPosst().equals(BuildConfig.FLAVOR)) {
                            if (((Thread) PageRecentThread.this.ListContains.get(i)).UrlThread() != null) {
                                intent.putExtra("URL", ((Thread) PageRecentThread.this.ListContains.get(i)).UrlThread());
                            } else {
                                return;
                            }
                        } else if (((Thread) PageRecentThread.this.ListContains.get(i)).UrlLastPosst() != null) {
                            intent.putExtra("URL", ((Thread) PageRecentThread.this.ListContains.get(i)).UrlLastPosst());
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                    intent.putExtra("TITLE", ((Thread) PageRecentThread.this.ListContains.get(i)).Thread());
                    PageRecentThread.this.startActivity(intent);
                    PageRecentThread.this.overridePendingTransition(R.anim.rail, R.anim.rail);
                } catch (IndexOutOfBoundsException e3) {
                    e = e3;
                    e.printStackTrace();
                }
            }
        });
        this.mList.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
                PageRecentThread.this.vibrate();
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
