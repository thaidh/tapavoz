package com.nna88.voz.listview;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nna88.voz.contain.Thread;
import com.nna88.voz.main.R;
import java.util.ArrayList;

public class listViewCustom2Html extends BaseAdapter {
    private ArrayList<Thread> contains;
    private ViewHolder holder;
    private LayoutInflater inflater;

    static class ViewHolder {
        TapAwareRelativeLayout layout;
        MyWebView webview;

        ViewHolder() {
        }
    }

    public listViewCustom2Html(Activity activity, ArrayList<Thread> arrayList) {
        this.contains = arrayList;
        this.inflater = (LayoutInflater) activity.getSystemService("layout_inflater");
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
        Thread thread = (Thread) this.contains.get(i);
        if (view == null) {
            this.holder = new ViewHolder();
            view = this.inflater.inflate(R.layout.list_item32, null);
            this.holder.webview = (MyWebView) view.findViewById(R.id.webview);
            this.holder.layout = (TapAwareRelativeLayout) view.findViewById(R.id.layout);
            this.holder.webview.setBackgroundColor(0);
            this.holder.webview.getSettings().setJavaScriptEnabled(false);
            this.holder.webview.getSettings().setJavaScriptEnabled(false);
            this.holder.webview.getSettings().setDomStorageEnabled(true);
            this.holder.webview.getSettings().setCacheMode(2);
            this.holder.webview.setLayerType(1, null);
            view.setTag(this.holder);
        } else {
            this.holder = (ViewHolder) view.getTag();
        }
        this.holder.webview.loadData(thread.Thread(), "text/html", AsyncHttpResponseHandler.DEFAULT_CHARSET);
        return view;
    }
}
