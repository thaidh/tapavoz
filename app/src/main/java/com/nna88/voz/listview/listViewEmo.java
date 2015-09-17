package com.nna88.voz.listview;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nna88.voz.contain.containEmo;
import com.nna88.voz.main.R;
import java.util.ArrayList;

public class listViewEmo extends BaseAdapter {
    Drawable d;
    private ViewHolder holder;
    public LayoutInflater inflater;
    public ArrayList<containEmo> mcontains;

    public static class ViewHolder {
        ImageView emo1;
        TextView text1;
    }

    public listViewEmo(Activity activity, ArrayList<containEmo> arrayList) {
        this.inflater = (LayoutInflater) activity.getSystemService("layout_inflater");
        this.mcontains = arrayList;
    }

    public int getCount() {
        return this.mcontains.size();
    }

    public Object getItem(int i) {
        return null;
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            this.holder = new ViewHolder();
            view = this.inflater.inflate(R.layout.list_item_emo, null);
            this.holder.emo1 = (ImageView) view.findViewById(R.id.list3_emoBitmap1);
            this.holder.text1 = (TextView) view.findViewById(R.id.list3_emoText1);
            view.setTag(this.holder);
        } else {
            this.holder = (ViewHolder) view.getTag();
        }
        this.holder.emo1.setImageBitmap(((containEmo) this.mcontains.get(i)).bitmap());
        this.holder.text1.setText(((containEmo) this.mcontains.get(i)).text());
        this.holder.text1.setVisibility(8);
        return view;
    }
}
