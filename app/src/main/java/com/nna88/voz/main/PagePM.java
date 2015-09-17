package com.nna88.voz.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.nna88.voz.contain.Thread;
import com.nna88.voz.listview.listViewCustom2;
import com.nna88.voz.mysqlite.Comment;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.jsoup.nodes.Element;

public class PagePM extends Page {
    private ArrayList<Thread> ListContains;
    private listViewCustom2 adapter;
    List<Comment> lComment;

    void Parser(int i) throws Exception {
        super.Parser(i);
        if (this.doc != null) {
            this.ListContains.clear();
            Iterator it = this.doc.select("td[id*=m]").iterator();
            String str = null;
            while (it.hasNext()) {
                it.hasNext();
                Element element = (Element) it.next();
                String str2 = BuildConfig.FLAVOR;
                String str3 = BuildConfig.FLAVOR;
                if (element.select("a[href*=private.php]").first() != null) {
                    str2 = element.select("a[href*=private.php]").first().text();
                    str = element.select("a[href*=private.php]").first().attr("href");
                }
                if (element.select("span[onclick*=member]").first() != null) {
                    str3 = element.select("span[onclick*=member]").first().text();
                }
                if (element.select("span[class*=smallfont]").first() != null) {
                    str3 = str3 + IOUtils.LINE_SEPARATOR_UNIX + element.select("span[class*=smallfont]").first().text();
                }
                if (element.select("span[class*=time]").first() != null) {
                    str3 = str3 + "  " + element.select("span[class*=time]").first().text();
                }
                this.ListContains.add(new Thread(str2, str3, null, null, null, str, null));
            }
            this.mItemCount = this.adapter.getCount();
            if (this.mItemOffsetY == null) {
                this.mItemOffsetY = new int[(this.mItemCount + 1)];
                this.mItemtemp = new int[(this.mItemCount + 1)];
            }
            this.scrollIsComputed = true;
            this.adapter.notifyDataSetChanged();
        }
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1) {
            TaskCancle();
            this.mTask = new TaskGetHtml();
            this.mTask.execute(new Integer[]{Integer.valueOf(15)});
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
        this.mTask.execute(new Integer[]{Integer.valueOf(15)});
        this.mList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (((Thread) PagePM.this.ListContains.get(i)).UrlThread() != null) {
                    Intent intent = new Intent(PagePM.this.mContext, PagePMDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("URL", ((Thread) PagePM.this.ListContains.get(i)).UrlThread());
                    bundle.putString("TITLE", BuildConfig.FLAVOR);
                    intent.putExtras(bundle);
                    PagePM.this.startActivityForResult(intent, 1);
                    PagePM.this.overridePendingTransition(R.anim.rail, R.anim.rail);
                }
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
