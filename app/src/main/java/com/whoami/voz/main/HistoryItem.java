package com.whoami.voz.main;

import org.json.JSONException;
import org.json.JSONStringer;

/**
 * Created by Thai on 9/12/2015.
 */
class HistoryItem {
    int count;
    String url;

    HistoryItem(String str) {
        this.url = null;
        this.count = 1;
        this.url = str;
    }

    HistoryItem(String str, int i) {
        this.url = null;
        this.count = 1;
        this.url = str;
        this.count = i;
    }

    void emit(JSONStringer jSONStringer) throws JSONException {
        jSONStringer.key(this.url).value((long) this.count);
    }

    public String toString() {
        return this.url;
    }
}
