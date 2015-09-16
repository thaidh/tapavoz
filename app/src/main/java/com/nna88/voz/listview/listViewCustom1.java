package com.nna88.voz.listview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nna88.voz.contain.Forum;
import com.nna88.voz.main.BuildConfig;
import com.nna88.voz.main.Global;
import com.nna88.voz.main.R;
import java.util.ArrayList;

public class listViewCustom1 extends BaseAdapter {
    private ArrayList<Forum> contains;
    private ViewHolder holder;
    private LayoutInflater inflater;
    private Context mContext;
    private float mTextSize1;
    private float mTextSize2;

    public static class ViewHolder {
        RelativeLayout layout;
        TextView txtView1;
        TextView txtView2;

        public ViewHolder(View view) {
            this.layout = (RelativeLayout) view.findViewById(R.id.listitem1);
            this.txtView1 = (TextView) view.findViewById(R.id.text1);
            this.txtView2 = (TextView) view.findViewById(R.id.text2);
            view.setTag(Integer.valueOf(R.id.id_holder));
        }
    }

    public listViewCustom1(Context context, ArrayList<Forum> arrayList) {
        this.mContext = context;
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
        ViewHolder viewHolder;
        if (view == null) {
            view = ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_item, null);
        }
        ViewHolder viewHolder2 = (ViewHolder) view.getTag(R.id.id_holder);
        if (viewHolder2 == null) {
            viewHolder2 = new ViewHolder(view);
            view.setTag(R.id.id_holder, viewHolder2);
            viewHolder = viewHolder2;
        } else {
            viewHolder = viewHolder2;
        }
        viewHolder.txtView1.setText(((Forum) this.contains.get(i)).Forum());
        viewHolder.txtView1.setTextSize(this.mTextSize1);
        viewHolder.txtView2.setTextSize(this.mTextSize2);
        Global.setBackgroundItemThread(viewHolder.layout);
        if (((Forum) this.contains.get(i)).Viewing() == null) {
            viewHolder.txtView1.setText(((Forum) this.contains.get(i)).Forum());
            Global.setTextColor2(viewHolder.txtView1);
            viewHolder.txtView2.setVisibility(View.GONE);
        } else {
            viewHolder.txtView2.setVisibility(View.VISIBLE);
            Global.setTextColor1(viewHolder.txtView1);
            Global.setTextColor2(viewHolder.txtView2);
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

    public void setSize(float f) {
        this.mTextSize1 = this.mContext.getResources().getDimension(R.dimen.textSize1) * f;
        this.mTextSize2 = this.mContext.getResources().getDimension(R.dimen.textSize2) * f;
        log(BuildConfig.FLAVOR + this.mTextSize1);
    }
}
