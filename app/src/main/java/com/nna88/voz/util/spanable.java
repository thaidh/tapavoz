package com.nna88.voz.util;

import com.nna88.voz.main.BuildConfig;
import java.util.ArrayList;

public class spanable {
    ArrayList<Integer> listEnd;
    ArrayList<Integer> listStart;
    ArrayList<String> listString;

    public spanable() {
        this.listString = new ArrayList();
        this.listStart = new ArrayList();
        this.listEnd = new ArrayList();
    }

    public void add(String str, int i, int i2) {
        this.listEnd.add(Integer.valueOf(i2));
        this.listStart.add(Integer.valueOf(i));
        this.listString.add(str);
    }

    public Integer getEnd(int i) {
        return i >= getSize() ? Integer.valueOf(0) : (Integer) this.listEnd.get(i);
    }

    public int getSize() {
        return this.listString.size();
    }

    public Integer getStart(int i) {
        return i >= getSize() ? Integer.valueOf(0) : (Integer) this.listStart.get(i);
    }

    public String getStr(int i) {
        return i >= getSize() ? BuildConfig.FLAVOR : (String) this.listString.get(i);
    }
}
