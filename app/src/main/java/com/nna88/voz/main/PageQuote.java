package com.nna88.voz.main;

import android.content.Intent;
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

public class PageQuote extends Page {
    private ArrayList<Thread> ListContains;
    private listViewCustom2 adapter;
    private int iNumquote;
    List<Comment> lComment;

    public PageQuote() {
        this.iNumquote = 0;
    }

    void Parser(int i) throws Exception {
        super.Parser(i);
        if (this.doc != null) {
            Element element;
            String str;
            String str2;
            String str3;
            Thread thread;
            this.ListContains.clear();
            Iterator it = this.doc.select("table[id]").iterator();
            setPage(parsePage(0, 0));
            if (it.hasNext()) {
                element = (Element) it.next();
                if (this.doc.select("span[title*=Showing results]").first() != null) {
                    String attr = this.doc.select("span[title*=Showing results]").first().attr(MySQLiteHelper.COLUMN_TITLE);
                    if (attr.contains("of ")) {
                        this.iNumquote = Integer.parseInt(attr.split("of ")[1]);
                        str = null;
                        str2 = null;
                        str3 = null;
                        while (it.hasNext()) {
                            it.hasNext();
                            element = (Element) it.next();
                            if (element.select("td[class=thead]").first() != null) {
                                str2 = element.select("td[class=thead]").first().ownText();
                            }
                            if (element.select("a[href*=showthread.php]").first() != null) {
                                str = element.select("a[href*=showthread.php]").first().text();
                                str3 = element.select("a[href*=showthread.php]").first().attr("href");
                            }
                            if (element.select("a[href*=member.php]").first() != null) {
                                str2 = str2 + "\nPosted By " + element.select("a[href*=member.php]").first().text();
                            }
                            if (element.select("em").first() != null) {
                                str2 = str2 + IOUtils.LINE_SEPARATOR_UNIX + element.select("em").first().ownText();
                                str3 = element.select("em").first().select("a").attr("href");
                            }
                            thread = new Thread(str, str2, null, null, null, str3, null);
                            if (Global.iNumQuote == -1) {
                                thread.setSticky(true);
                            } else if (Global.iNumQuote < this.iNumquote) {
                                thread.setSticky(true);
                                Global.iNumQuote++;
                            }
                            this.ListContains.add(thread);
                        }
                        if (this.iNumquote != 0) {
                            Global.iNumQuote = this.iNumquote;
                        }
                        writeSetNumQuote(Global.iNumQuote);
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
                                PageQuote.this.mList.setSelection(0);
                            }
                        });
                    }
                }
            }
            str = null;
            str2 = null;
            str3 = null;
            while (it.hasNext()) {
                it.hasNext();
                element = (Element) it.next();
                if (element.select("td[class=thead]").first() != null) {
                    str2 = element.select("td[class=thead]").first().ownText();
                }
                if (element.select("a[href*=showthread.php]").first() != null) {
                    str = element.select("a[href*=showthread.php]").first().text();
                    str3 = element.select("a[href*=showthread.php]").first().attr("href");
                }
                if (element.select("a[href*=member.php]").first() != null) {
                    str2 = str2 + "\nPosted By " + element.select("a[href*=member.php]").first().text();
                }
                if (element.select("em").first() != null) {
                    str2 = str2 + IOUtils.LINE_SEPARATOR_UNIX + element.select("em").first().ownText();
                    str3 = element.select("em").first().select("a").attr("href");
                }
                thread = new Thread(str, str2, null, null, null, str3, null);
                if (Global.iNumQuote == -1) {
                    thread.setSticky(true);
                } else if (Global.iNumQuote < this.iNumquote) {
                    thread.setSticky(true);
                    Global.iNumQuote++;
                }
                this.ListContains.add(thread);
            }
            if (this.iNumquote != 0) {
                Global.iNumQuote = this.iNumquote;
            }
            writeSetNumQuote(Global.iNumQuote);
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
                    PageQuote.this.mList.setSelection(0);
                }
            });
        }
    }

    protected void onCreate(Bundle bundle) {
        this.iPage = 7;
        super.onCreate(bundle);
        this.ListContains = new ArrayList();
        this.adapter = new listViewCustom2(this.mContext, this.ListContains);
        this.adapter.setSize(this.mTextSize);
        this.mObjectAdapter = this.adapter;
        this.mList.setAdapter(this.adapter);
        this.mTask.execute(new Integer[]{Integer.valueOf(10)});
        this.mList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                IndexOutOfBoundsException e;
                try {
                    Intent intent;
                    PageQuote.this.vibrate();
                    if (((Thread) PageQuote.this.ListContains.get(i)).isUrl() == 0) {
                        intent = new Intent(PageQuote.this.mContext, Page2.class);
                        try {
                            if (((Thread) PageQuote.this.ListContains.get(i)).UrlThread() != null) {
                                intent.putExtra("URL", ((Thread) PageQuote.this.ListContains.get(i)).UrlThread());
                            } else {
                                return;
                            }
                        } catch (IndexOutOfBoundsException e2) {
                            e = e2;
                            e.printStackTrace();
                        }
                    } else if (((Thread) PageQuote.this.ListContains.get(i)).isUrl() == 1) {
                        intent = new Intent(PageQuote.this.mContext, Page3.class);
                        if (((Thread) PageQuote.this.ListContains.get(i)).UrlLastPosst() == null || ((Thread) PageQuote.this.ListContains.get(i)).UrlLastPosst().equals(BuildConfig.FLAVOR)) {
                            if (((Thread) PageQuote.this.ListContains.get(i)).UrlThread() != null) {
                                intent.putExtra("URL", ((Thread) PageQuote.this.ListContains.get(i)).UrlThread());
                            } else {
                                return;
                            }
                        } else if (((Thread) PageQuote.this.ListContains.get(i)).UrlLastPosst() != null) {
                            intent.putExtra("URL", ((Thread) PageQuote.this.ListContains.get(i)).UrlLastPosst());
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                    intent.putExtra("TITLE", ((Thread) PageQuote.this.ListContains.get(i)).Thread());
                    PageQuote.this.startActivity(intent);
                    PageQuote.this.overridePendingTransition(R.anim.rail, R.anim.rail);
                } catch (IndexOutOfBoundsException e3) {
                    e = e3;
                    e.printStackTrace();
                }
            }
        });
        this.mList.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
                PageQuote.this.vibrate();
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
