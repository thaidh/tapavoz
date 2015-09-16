package com.nna88.voz.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.nna88.voz.contain.containEmo;
import com.nna88.voz.util.Util;
import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {
    private ArrayList<containEmo> listEmo;
    private Context mContext;

    public GridAdapter(Context context, ArrayList<containEmo> arrayList) {
        this.mContext = context;
        this.listEmo = arrayList;
    }

    public int getCount() {
        return this.listEmo.size();
    }

    public Object getItem(int i) {
        return this.listEmo.get(i);
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(this.mContext);
        imageView.setImageBitmap(((containEmo) this.listEmo.get(i)).bitmap());
        imageView.setScaleType(ScaleType.CENTER_CROP);
        int convertDpToPx = Util.convertDpToPx(this.mContext.getApplicationContext(), 32);
        imageView.setLayoutParams(new LayoutParams(convertDpToPx, convertDpToPx));
        return imageView;
    }
}
