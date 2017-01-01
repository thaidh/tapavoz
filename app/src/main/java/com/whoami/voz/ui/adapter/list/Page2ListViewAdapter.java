package com.whoami.voz.ui.adapter.list;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whoami.voz.R;
import com.whoami.voz.ui.contain.VozThread;
import com.whoami.voz.ui.main.Global;

import java.util.ArrayList;

public class Page2ListViewAdapter extends BaseAdapter {
    private ArrayList<VozThread> mVozThreads;
    private ViewHolder holder;
    private LayoutInflater inflater;
    private Context mContext;

    public static class ViewHolder {
        ImageView imgView;
        RelativeLayout relativelayout;
        TextView txtView1;
        TextView txtView2;
    }

    public Page2ListViewAdapter(Context context, ArrayList<VozThread> arrayList) {
        this.mVozThreads = arrayList;
        this.mContext = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<VozThread> contains) {
        this.mVozThreads = new ArrayList<>(contains);
    }

    public int getCount() {
        return this.mVozThreads.size();
    }

    public Object getItem(int i) {
        return null;
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        VozThread vozThread = this.mVozThreads.get(i);
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
//        Global.setBackgroundItemThread(this.holder.relativelayout);
        if (vozThread.UrlThread() == null) {
//            Global.setTextColor2(this.holder.txtView1);
            holder.txtView1.setTextColor(ContextCompat.getColor(mContext, R.color.red));
            this.holder.txtView1.setText(vozThread.Thread());
            this.holder.txtView2.setText(null);
            this.holder.txtView2.setVisibility(View.GONE);
        } else {
//            Global.setTextColor2(this.holder.txtView2);
            holder.txtView1.setTextColor(ContextCompat.getColor(mContext, R.color.vozTcat));
            this.holder.txtView1.setText(vozThread.Thread());
            this.holder.txtView2.setVisibility(View.VISIBLE);
//            if (thread.isSticky()) {
//                Global.setTextSticky(this.holder.txtView1);
//            } else {
//                Global.setTextColor1(this.holder.txtView1);
//            }
            if (vozThread.Reply() == null) {
                this.holder.txtView2.setText(vozThread.LastPost());
            } else {
                this.holder.txtView2.setText(vozThread.LastPost() + "- Replie:" + ((VozThread) this.mVozThreads.get(i)).Reply() + " - View:" + ((VozThread) this.mVozThreads.get(i)).View());
            }
            if (vozThread.UrlLastPosst() != null) {
                this.holder.txtView1.setTypeface(null, 1);
            } else {
                this.holder.txtView1.setTypeface(null, 0);
            }
        }
        if (Global.bTopicHeader) {
            try {
                this.holder.imgView.setImageBitmap(vozThread.Image());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.holder.imgView.setVisibility(View.GONE);
        }
        this.holder.relativelayout.setClickable(false);
        this.holder.relativelayout.setFocusable(false);
        return view;
    }

}
