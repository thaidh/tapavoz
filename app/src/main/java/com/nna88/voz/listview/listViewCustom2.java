package com.nna88.voz.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nna88.voz.contain.Thread;
import com.nna88.voz.main.Global;
import com.nna88.voz.main.R;
import java.util.ArrayList;

public class listViewCustom2 extends BaseAdapter {
    private ArrayList<Thread> contains;
    private ViewHolder holder;
    private LayoutInflater inflater;
    private Context mContext;
    private float mTextSize1;
    private float mTextSize2;

    public static class ViewHolder {
        ImageView imgView;
        RelativeLayout relativelayout;
        TextView txtView1;
        TextView txtView2;
    }

    public listViewCustom2(Context context, ArrayList<Thread> arrayList) {
        this.contains = arrayList;
        this.mContext = context;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
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
            view = this.inflater.inflate(R.layout.list_item2, null);
            this.holder.relativelayout = (RelativeLayout) view.findViewById(R.id.relativelayout);
            this.holder.imgView = (ImageView) view.findViewById(R.id.image);
            this.holder.txtView1 = (TextView) view.findViewById(R.id.text1);
            this.holder.txtView2 = (TextView) view.findViewById(R.id.text2);
            view.setTag(this.holder);
        } else {
            this.holder = (ViewHolder) view.getTag();
        }
        Global.setBackgroundItemThread(this.holder.relativelayout);
        this.holder.txtView1.setTextSize(this.mTextSize1);
        this.holder.txtView2.setTextSize(this.mTextSize2);
        if (thread.UrlThread() == null) {
            Global.setTextColor2(this.holder.txtView1);
            this.holder.txtView1.setText(thread.Thread());
            this.holder.txtView2.setText(null);
            this.holder.txtView2.setVisibility(8);
        } else {
            Global.setTextColor2(this.holder.txtView2);
            this.holder.txtView1.setText(thread.Thread());
            this.holder.txtView2.setVisibility(0);
            if (thread.isSticky()) {
                Global.setTextSticky(this.holder.txtView1);
            } else {
                Global.setTextColor1(this.holder.txtView1);
            }
            if (thread.Reply() == null) {
                this.holder.txtView2.setText(thread.LastPost());
            } else {
                this.holder.txtView2.setText(thread.LastPost() + "- Replie:" + ((Thread) this.contains.get(i)).Reply() + " - View:" + ((Thread) this.contains.get(i)).View());
            }
            if (thread.UrlLastPosst() != null) {
                this.holder.txtView1.setTypeface(null, 1);
            } else {
                this.holder.txtView1.setTypeface(null, 0);
            }
        }
        if (Global.bTopicHeader) {
            try {
                this.holder.imgView.setImageBitmap(thread.Image());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.holder.imgView.setVisibility(8);
        }
        this.holder.relativelayout.setClickable(false);
        this.holder.relativelayout.setFocusable(false);
        return view;
    }

    public void setSize(float f) {
        this.mTextSize1 = this.mContext.getResources().getDimension(R.dimen.textSize1) * f;
        this.mTextSize2 = this.mContext.getResources().getDimension(R.dimen.textSize2) * f;
    }
}
