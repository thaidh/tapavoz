package com.whoami.voz.ui.adapter.list;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whoami.voz.R;
import com.whoami.voz.ui.contain.Forum;

import java.util.ArrayList;

public class Page1ListViewAdapter extends BaseAdapter {
    private ArrayList<Forum> contains;
    private ViewHolder holder;
    private LayoutInflater inflater;
    private Context mContext;

    public static class ViewHolder {
        RelativeLayout layout;
        TextView txtView1;
        TextView txtView2;

        public ViewHolder(View view) {
            this.layout = (RelativeLayout) view.findViewById(R.id.listitem1);
            this.txtView1 = (TextView) view.findViewById(R.id.text1);
            this.txtView2 = (TextView) view.findViewById(R.id.text2);
            view.setTag(this);
        }
    }

    public Page1ListViewAdapter(Context context, ArrayList<Forum> arrayList) {
        this.mContext = context;
        this.contains = arrayList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<Forum> forumsList) {
        contains = forumsList;
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
        ViewHolder viewHolder;
        if (view == null) {
            view = ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_item, null);
            viewHolder = new ViewHolder(view);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.txtView1.setText(((Forum) this.contains.get(i)).Forum());
//        Global.setBackgroundItemThread(viewHolder.layout);
        if (((Forum) this.contains.get(i)).Viewing() == null) {
            viewHolder.txtView1.setText(((Forum) this.contains.get(i)).Forum());
//            Global.setTextColor2(viewHolder.txtView1);
            viewHolder.txtView1.setTextColor(ContextCompat.getColor(mContext, R.color.red));
            viewHolder.txtView2.setVisibility(View.GONE);
        } else {
            viewHolder.txtView2.setVisibility(View.VISIBLE);
            viewHolder.txtView1.setTextColor(ContextCompat.getColor(mContext, R.color.vozTcat));
            viewHolder.txtView2.setTextColor(ContextCompat.getColor(mContext, R.color.black));
//            Global.setTextColor1(viewHolder.txtView1);
//            Global.setTextColor2(viewHolder.txtView2);
            viewHolder.txtView1.setText(((Forum) this.contains.get(i)).Forum());
            if (((Forum) this.contains.get(i)).Viewing().contains("Viewing")) {
                viewHolder.txtView2.setText(((Forum) this.contains.get(i)).Viewing());
            } else {
                viewHolder.txtView2.setText("(0 Viewing)");
            }
        }
        viewHolder.layout.setClickable(false);
        viewHolder.layout.setFocusable(false);
        viewHolder.layout.setFocusableInTouchMode(false);
        return view;
    }
}
