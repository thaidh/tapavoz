package com.whoami.voz.adapter.list;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.whoami.voz.R;
import com.whoami.voz.main.Global;

import java.util.ArrayList;

public class SettingAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private OnActionItemClickListener mItemClickListener;
    private ArrayList<Item> mItems;
    private SharedPreferences sf;

    /* renamed from: com.nna88.voz.listview.SettingAdapter.1 */
    class AnonymousClass1 implements OnCheckedChangeListener {
        final /* synthetic */ Item val$item;

        AnonymousClass1(Item item) {
            this.val$item = item;
        }

        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            this.val$item.write(z);
            if (this.val$item.mName.equals("Fullscreen")) {
                Global.bFullScreen = z;
                SettingAdapter.this.mItemClickListener.onItemClick(0, z, this.val$item);
            } else if (this.val$item.mName.equals("Sign")) {
                Global.bSign = z;
                SettingAdapter.this.mItemClickListener.onItemClick(1, z, this.val$item);
            } else if (this.val$item.mName.equals("Icon header")) {
                Global.bTopicHeader = z;
                SettingAdapter.this.mItemClickListener.onItemClick(2, z, this.val$item);
            } else if (this.val$item.mName.equals("Swipe")) {
                Global.bSwipe = z;
                SettingAdapter.this.mItemClickListener.onItemClick(3, z, this.val$item);
            } else if (!this.val$item.mName.equals("Pull to next") && !this.val$item.mName.equals("Pull to refresh")) {
                if (this.val$item.mName.equals("Vibrate")) {
                    Global.bVibrate = z;
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(5, z, this.val$item);
                    }
                } else if (this.val$item.mName.equals("Divider")) {
                    Global.bDevider = z;
                    SettingAdapter.this.mItemClickListener.onItemClick(6, z, this.val$item);
                } else if (this.val$item.mName.equals("Effect")) {
                    Global.bEffect = z;
                    SettingAdapter.this.mItemClickListener.onItemClick(7, z, this.val$item);
                } else if (this.val$item.mName.equals("Click to hide")) {
                    Global.bClickAd = z;
                } else if (this.val$item.mName.equals("Notification")) {
                    Global.bNotifSubscribe = z;
                }
            }
        }
    }

    public static class Item {
        public static boolean mValue;
        public String mName;
        private String mSaveName;
        private boolean mValueDefaule;
        private SharedPreferences sf;

        public Item(SharedPreferences sharedPreferences, String str, String str2, boolean z, boolean z2) {
            this.mName = str;
            this.mSaveName = str2;
            mValue = z;
            this.mValueDefaule = z2;
            this.sf = sharedPreferences;
        }

        public boolean read() {
            return this.sf.getBoolean(this.mSaveName, this.mValueDefaule);
        }

        public void write(boolean z) {
            mValue = z;
            Editor edit = this.sf.edit();
            edit.putBoolean(this.mSaveName, z);
            edit.commit();
        }
    }

    public interface OnActionItemClickListener {
        void onItemClick(int i, boolean z, Item item);
    }

    public SettingAdapter(Context context, ArrayList<Item> arrayList) {
        this.mItems = arrayList;
        this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.sf = context.getSharedPreferences("Setting", 0);
    }

    public int getCount() {
        return this.mItems.size();
    }

    public Item getItem(int i) {
        return (Item) this.mItems.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        Item item = getItem(i);
        if (view == null) {
            view = this.mInflater.inflate(R.layout.setting_item, viewGroup, false);
        }
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.check);
        checkBox.setText(item.mName);
        checkBox.setChecked(item.read());
        checkBox.setOnCheckedChangeListener(new AnonymousClass1(item));
        return view;
    }

    public void setOnActionItemClickListener(OnActionItemClickListener onActionItemClickListener) {
        this.mItemClickListener = onActionItemClickListener;
    }
}
