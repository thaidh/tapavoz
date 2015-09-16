package com.nna88.voz.listview;

import android.content.Context;
import android.os.Build.VERSION;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.BaseAdapter;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nna88.voz.contain.Forum;
import com.nna88.voz.main.Global;
import com.nna88.voz.main.R;
import java.util.ArrayList;

public class listViewCustom1Html extends BaseAdapter {
    private ArrayList<Forum> contains;
    private ViewHolder holder;
    private LayoutInflater inflater;

    static class ViewHolder {
        TapAwareRelativeLayout layout;
        MyWebView webview;

        ViewHolder() {
        }
    }

    public listViewCustom1Html(Context context, ArrayList<Forum> arrayList) {
        this.contains = arrayList;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    private void log(String str) {
        Log.d("nna", str);
    }

    public int getCount() {
        return this.contains.size();
    }

    public Object getItem(int i) {
        return null;
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        Forum forum = (Forum) this.contains.get(i);
        if (view == null) {
            this.holder = new ViewHolder();
            view = this.inflater.inflate(R.layout.list_item32, null);
            this.holder.webview = (MyWebView) view.findViewById(R.id.webview);
            this.holder.layout = (TapAwareRelativeLayout) view.findViewById(R.id.layout);
            this.holder.webview.setBackgroundColor(0);
            view.setTag(this.holder);
        } else {
            this.holder = (ViewHolder) view.getTag();
        }
        Global.setBackgroundItemThread(this.holder.webview);
        this.holder.layout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            }
        });
        if (VERSION.SDK_INT <= 18) {
            this.holder.webview.getSettings().setJavaScriptEnabled(true);
            this.holder.webview.addJavascriptInterface(this, "Android");
            this.holder.webview.getSettings().setCacheMode(2);
            this.holder.webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
            this.holder.webview.setScrollBarStyle(ViewCompat.MEASURED_STATE_TOO_SMALL);
            this.holder.webview.applyAfterMoveFix();
        } else {
            this.holder.webview.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 4.4; Nexus 4 Build/KRT16H) AppleWebKit/537.36\n(KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36");
            this.holder.webview.getSettings().setJavaScriptEnabled(true);
            this.holder.webview.addJavascriptInterface(this, "Android");
            this.holder.webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
            this.holder.webview.getSettings().setCacheMode(2);
            this.holder.webview.setScrollBarStyle(ViewCompat.MEASURED_STATE_TOO_SMALL);
            this.holder.webview.applyAfterMoveFix();
        }
        this.holder.webview.loadDataWithBaseURL(Global.URL, forum.getForum(), "text/html", AsyncHttpResponseHandler.DEFAULT_CHARSET, null);
        return view;
    }
}
