package com.whoami.voz.ui.contain;

import android.text.TextUtils;
import android.widget.LinearLayout;

import com.whoami.voz.ui.contain.item.ContentItem;
import com.whoami.voz.ui.main.MainApplication;
import com.whoami.voz.ui.utils.EmoLoader;
import com.whoami.voz.ui.utils.Util;

import java.util.ArrayList;

public class VozPost {
    public static final String urlAvatar = "http://pik.vn/2014277c7c6c-57e7-4a12-bb2a-c83315886870.png";
    public static final String urlOffline = "http://pik.vn/2014672b230d-420c-4855-adc8-bb370d696e37.png";
    public static final String urlOnline = "http://pik.vn/2014e76c1d82-9947-4362-991c-c29457164f29.png";
    public static final String EMO_PREFIX = "images/smilies";
    public static final int DP32 = Util.convertDpToPx(MainApplication.getAppContext(), 32);
    public static final int TEXT_SIZE = 1;


    public ArrayList<String> imageUrls;
    private boolean isMultiQuote;
    private boolean isOnline;
    private String mJd;
    private String mPostCount;
    private String mTime;
    private String mUrlAvatar;
    private String mUser;
    private String mUserTitle;
    private String mUid;
    private String mPostId;
    private String mPlainText;
    public ArrayList<ContentItem> mContentItemList;
    public LinearLayout mContentLayout;

    public VozPost() {
        this.mJd = "";
        this.mPostCount = "0";
        this.mUid = "";
        this.isOnline = false;
        this.mTime = "";
        this.mPlainText = new String();
        this.isMultiQuote = false;
        this.mContentItemList = new ArrayList<>();
    }

    public String getPostId() {
        return this.mPostId;
    }

    public void setInfo(String str, String str2, String str3, String str4) {
        this.mUser = str;
        this.mUserTitle = str2;
        this.mTime = str3;
        this.mUrlAvatar = str4;
    }

    public String getJD() {
        return this.mJd;
    }

    public String getPostCount() {
        return this.mPostCount;
    }

    public String getTime() {
        return this.mTime;
    }

    public String getUrlAvatar() {
        return this.mUrlAvatar;
    }

    public String getUserName() {
        return this.mUser;
    }

    public String getUserTitle() {
        return this.mUserTitle;
    }

    public ContentItem getLastTextItem() {
        ContentItem textItem;
        int size = mContentItemList.size();
        if (size == 0 || mContentItemList.get(size -1).type != ContentItem.TYPE_PLAIN_TEXT) {
            textItem = new ContentItem(ContentItem.TYPE_PLAIN_TEXT, "");
            mContentItemList.add(textItem);
        } else {
            textItem = mContentItemList.get(size - 1);
        }
        return textItem;
    }

    public ContentItem getLastQuoteItem() {
        ContentItem textItem;
        int size = mContentItemList.size();
        if (size == 0 || mContentItemList.get(size -1).type != ContentItem.TYPE_QUOTE) {
            textItem = new ContentItem(ContentItem.TYPE_QUOTE, "");
            mContentItemList.add(textItem);
        } else {
            textItem = mContentItemList.get(size - 1);
        }
        return textItem;
    }

    public void addText(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mPlainText += str;
            ContentItem textItem;
            textItem = getLastTextItem();
            textItem.mData += str;
        }
    }

    public void addQuote(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mPlainText += str;
            ContentItem textItem = getLastQuoteItem();
            textItem.mData += str;
        }
    }

    public void addUrl(String url, boolean isQuote) {
        ContentItem textItem;
        if (isQuote) {
            textItem = getLastQuoteItem();
        } else {
            textItem = getLastTextItem();
        }
        textItem.addUrl(url, textItem.mData.length(), textItem.mData.length() + url.length());
        textItem.mData += url;
    }

    public void addEmo(String url, boolean isQuote) {
        ContentItem textItem;
        if (isQuote) {
            textItem = getLastQuoteItem();
        } else {
            textItem = getLastTextItem();
        }
        textItem.addEmo(url, textItem.mData.length(), textItem.mData.length() + 2);
        textItem.mData += "  ";
    }

    public void addPhoto(String url) {
        ContentItem photoItem = new ContentItem(ContentItem.TYPE_PHOTO, url);
        mContentItemList.add(photoItem);
    }

    public String getText() {
        return this.mPlainText;
    }

    public boolean isMultiQuote() {
        return this.isMultiQuote;
    }

    public void setId(String str) {
        this.mPostId = str;
    }

    public void setJD(String str) {
        this.mJd = str;
    }

    public void setMultiQuote(boolean z) {
        this.isMultiQuote = z;
    }

    public void setPosts(String str) {
        this.mPostCount = str;
    }

    public void preloadEmo() {
        if (imageUrls != null) {
            for (int j = 0; j < imageUrls.size(); j++) {
                String url = imageUrls.get(j);
                if (url.contains(EMO_PREFIX)) {
                    EmoLoader.getInstance().initEmoBitmapCache(url);
                }
            }
        }
    }

    public void initContent() {
        try {
            preloadEmo();
            for (ContentItem item : mContentItemList) {
                item.initContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public void addImageUrl(String url) {
        if (imageUrls == null) {
            imageUrls = new ArrayList<>();
        }
        imageUrls.add(url);
    }

    public void setUid(String mUid) {
        this.mUid = mUid;
    }


}
