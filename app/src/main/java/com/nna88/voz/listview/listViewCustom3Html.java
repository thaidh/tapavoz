package com.nna88.voz.listview;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.widget.BaseAdapter;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nna88.voz.contain.Post;
import com.nna88.voz.main.Global;
import com.nna88.voz.main.R;
import com.nna88.voz.util.UserInfo;
import java.util.ArrayList;

public class listViewCustom3Html extends BaseAdapter {
    private Activity activity;
    private ViewHolder holder;
    private LayoutInflater inflater;
    private OnActionImageClickListener mImageClickListenr;
    private OnActionItemClickListener mItemClickListener;
    protected UserInfo mUser;
    private ArrayList<Post> mcontains;

    /* renamed from: com.nna88.voz.listview.listViewCustom3Html.1 */
    class AnonymousClass1 implements OnClickListener {
        final /* synthetic */ int val$pos;

        AnonymousClass1(int i) {
            this.val$pos = i;
        }

        public void onClick(View view) {
            listViewCustom3Html.this.mItemClickListener.onItemClick(this.val$pos, view);
        }
    }

    public interface OnActionImageClickListener {
        void onItemClick(String str);
    }

    public interface OnActionItemClickListener {
        void onItemClick(int i, View view);
    }

    static class ViewHolder {
        TapAwareRelativeLayout layout;
        MyWebView webview;

        ViewHolder() {
        }
    }

    public listViewCustom3Html(Activity activity, ArrayList<Post> arrayList) {
        this.activity = activity;
        this.inflater = (LayoutInflater) this.activity.getSystemService("layout_inflater");
        this.mcontains = arrayList;
        this.mUser = new UserInfo();
    }

    private void log(String str) {
        Log.d("nna", str);
    }

    public void destroy() {
    }

    public int getCount() {
        return this.mcontains.size();
    }

    public Object getItem(int i) {
        return null;
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        Post post = (Post) this.mcontains.get(i);
        if (view == null) {
            this.holder = new ViewHolder();
            view = this.inflater.inflate(R.layout.list_item32, null);
            this.holder.webview = (MyWebView) view.findViewById(R.id.webview);
            this.holder.layout = (TapAwareRelativeLayout) view.findViewById(R.id.layout);
            this.holder.webview.getSettings().setUseWideViewPort(true);
            this.holder.webview.getSettings().setJavaScriptEnabled(true);
            this.holder.webview.setWebChromeClient(new WebChromeClient());
            view.setTag(this.holder);
        } else {
            this.holder = (ViewHolder) view.getTag();
        }
        if (post.isMultiQuote()) {
            Global.setBackgroundItemThreadMultiQuote(this.holder.webview);
        } else {
            Global.setBackgroundItemThread(this.holder.webview);
        }
        this.holder.layout.setOnClickListener(new AnonymousClass1(i));
        this.holder.webview.loadDataWithBaseURL(Global.URL, ((Post) this.mcontains.get(i)).getPost(), "text/html", AsyncHttpResponseHandler.DEFAULT_CHARSET, null);
        return view;
    }

    public void setOnActionImageClickListener(OnActionImageClickListener onActionImageClickListener) {
        this.mImageClickListenr = onActionImageClickListener;
    }

    public void setOnActionItemClickListener(OnActionItemClickListener onActionItemClickListener) {
        this.mItemClickListener = onActionItemClickListener;
    }

    @JavascriptInterface
    public void showToast(String str) {
        log("image");
        this.mImageClickListenr.onItemClick(str);
    }
}
