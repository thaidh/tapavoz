package com.whoami.voz.adapter.list;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whoami.voz.R;
import com.whoami.voz.contain.VozThread;

import java.util.ArrayList;

public class Page2ListViewAdapter extends BaseAdapter {
    private ArrayList<VozThread> mVozThreads;
    private LayoutInflater inflater;
    private Context mContext;

    public static class ViewHolder {
        RelativeLayout relativelayout;
        TextView txtView1;
        TextView txtView2;
        TextView replyTv;
        TextView viewTv;
        View replyLayout;
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
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = this.inflater.inflate(R.layout.list_item2, null);
            holder.relativelayout = (RelativeLayout) view.findViewById(R.id.relativelayout);
            holder.txtView1 = (TextView) view.findViewById(R.id.text1);
            holder.txtView2 = (TextView) view.findViewById(R.id.text2);
            holder.replyLayout = view.findViewById(R.id.reply_view_layout);
            holder.replyTv = (TextView) view.findViewById(R.id.reply_tv);
            holder.viewTv = (TextView) view.findViewById(R.id.view_tv);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.replyLayout.setVisibility(View.GONE);
        if (vozThread.UrlThread() == null) {
            holder.txtView1.setTextColor(ContextCompat.getColor(mContext, R.color.red));
            holder.txtView1.setText(vozThread.Thread());
            holder.txtView2.setText(null);
            holder.txtView2.setVisibility(View.GONE);
        } else {
            holder.txtView1.setTextColor(ContextCompat.getColor(mContext, R.color.vozTcat));
            holder.txtView1.setText(vozThread.Thread());
            holder.txtView2.setVisibility(View.VISIBLE);
            if (vozThread.isSticky()) {
                holder.txtView1.setTextColor(ContextCompat.getColor(mContext, R.color.red));
            }
            holder.txtView2.setText(vozThread.LastPost());
            if (vozThread.UrlLastPosst() != null) {
                holder.txtView1.setTypeface(Typeface.DEFAULT_BOLD, 1);
            } else {
                holder.txtView1.setTypeface(Typeface.DEFAULT, 0);
            }
            if (!TextUtils.isEmpty(vozThread.Reply())) {
                holder.replyLayout.setVisibility(View.VISIBLE);
                holder.replyTv.setText(vozThread.Reply());
                holder.viewTv.setText(vozThread.View());
            }
        }
        return view;
    }

}
