package com.whoami.voz.ui.main;

import android.content.Context;
import android.widget.ArrayAdapter;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;

class URLHistory extends ArrayAdapter<HistoryItem> {
    private ArrayList<HistoryItem> spareCopy;

    URLHistory(Context context, int i) {
        super(context, i, new ArrayList());
        this.spareCopy = new ArrayList();
    }

    void clear(int i) {
        remove(this.spareCopy.get(i));
    }

    void load(String str) throws JSONException {
        JSONObject jSONObject = new JSONObject(str);
        Iterator keys = jSONObject.keys();
        while (keys.hasNext()) {
            String obj = keys.next().toString();
            HistoryItem historyItem = new HistoryItem(obj, jSONObject.getInt(obj));
            this.spareCopy.add(historyItem);
            add(historyItem);
        }
    }

    void save(Writer writer) throws JSONException, Exception {
        JSONStringer object = new JSONStringer().object();
        Iterator it = this.spareCopy.iterator();
        while (it.hasNext()) {
            ((HistoryItem) it.next()).emit(object);
        }
        writer.write(object.endObject().toString());
    }

    void update(String str) {
        HistoryItem historyItem;
        Iterator it = this.spareCopy.iterator();
        while (it.hasNext()) {
            historyItem = (HistoryItem) it.next();
            if (str.equals(historyItem.url)) {
                historyItem.count++;
                return;
            }
        }
        historyItem = new HistoryItem(str);
        this.spareCopy.add(historyItem);
        add(historyItem);
    }
}
