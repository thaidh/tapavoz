package com.whoami.voz.ui.adapter.list;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.whoami.voz.R;
import com.whoami.voz.ui.utils.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SidebarAdapter extends BaseAdapter {
    public static final String TAG = "VLC/SidebarAdapter";
    static final List<SidebarEntry> entries;
    private Activity mActivity;
    private LayoutInflater mInflater;
    private OnActionItemClickListener mItemClickListener;
    private ArrayList<Object> mItems;

    /* renamed from: com.nna88.voz.listview.SidebarAdapter.1 */
    class AnonymousClass1 implements OnClickListener {
        final /* synthetic */ Object val$item;
        final /* synthetic */ int val$position;

        AnonymousClass1(int i, Object obj) {
            this.val$position = i;
            this.val$item = obj;
        }

        public void onClick(View view) {
            if (SidebarAdapter.this.mItemClickListener != null) {
                SidebarAdapter.this.mItemClickListener.onItemClick(this.val$position, this.val$item);
            }
        }
    }

    public static class Category {
        String mTitle;

        public Category(String str) {
            this.mTitle = str;
        }
    }

    public static class Item {
        public boolean isAvatart;
        public Drawable mIcon;
        public String mTitle;

        public Item(String str, Drawable drawable) {
            this.isAvatart = false;
            this.mTitle = str;
            this.mIcon = drawable;
        }

        public Item(String str, Drawable drawable, boolean z) {
            this.isAvatart = false;
            this.mTitle = str;
            this.mIcon = drawable;
            this.isAvatart = z;
        }
    }

    public interface OnActionItemClickListener {
        void onItemClick(int i, Object obj);
    }

    static class SidebarEntry {
        int drawableID;
        String id;
        String name;

        public SidebarEntry(String str, String str2, int i) {
            this.id = str;
            this.name = str2;
            this.drawableID = i;
        }
    }

    static {
        entries = Arrays.asList(new SidebarEntry[]{new SidebarEntry("User", "User", R.drawable.login), new SidebarEntry("Setting", "Setting", R.drawable.menu_more_setting), new SidebarEntry("GoAddress", "GoAddress", R.drawable.tab_network_icon)});
    }

    public SidebarAdapter(Activity activity, ArrayList<Object> arrayList) {
        this.mActivity = activity;
        this.mItems = arrayList;
        this.mInflater = (LayoutInflater) activity.getSystemService("layout_inflater");
    }

    public int getCount() {
        return this.mItems.size();
    }

    public Object getItem(int i) {
        return this.mItems.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public int getItemViewType(int i) {
        return getItem(i) instanceof Item ? 0 : 1;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View inflate;
        Object item = getItem(i);
        if (item instanceof Category) {
            inflate = view == null ? this.mInflater.inflate(R.layout.sidebar_catagory, viewGroup, false) : view;
            ((TextView) inflate).setText(((Category) item).mTitle);
        } else {
            inflate = view == null ? this.mInflater.inflate(R.layout.sidebar_item, viewGroup, false) : view;
            TextView textView = (TextView) inflate;
            textView.setText(((Item) item).mTitle);
            Drawable drawable = ((Item) item).mIcon;
            int convertDpToPx;
            if (((Item) item).isAvatart) {
                convertDpToPx = Util.convertDpToPx(this.mActivity.getApplicationContext(), 32);
                drawable.setBounds(0, 0, (drawable.getIntrinsicWidth() * convertDpToPx) / drawable.getIntrinsicHeight(), convertDpToPx);
            } else {
                convertDpToPx = Util.convertDpToPx(this.mActivity.getApplicationContext(), 32);
                drawable.setBounds(0, 0, convertDpToPx, convertDpToPx);
            }
            textView.setCompoundDrawables(drawable, null, null, null);
            textView.setOnClickListener(new AnonymousClass1(i, item));
        }
        return inflate;
    }

    public int getViewTypeCount() {
        return 2;
    }

    public void setOnActionItemClickListener(OnActionItemClickListener onActionItemClickListener) {
        this.mItemClickListener = onActionItemClickListener;
    }
}
