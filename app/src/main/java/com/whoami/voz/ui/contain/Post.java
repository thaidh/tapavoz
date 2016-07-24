package com.whoami.voz.ui.contain;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.SpannableString;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;


import com.whoami.voz.R;
import com.whoami.voz.ui.main.MainApplication;
import com.whoami.voz.ui.utils.CustomSpanable;
import com.whoami.voz.ui.utils.EmoLoader;
import com.whoami.voz.ui.utils.Util;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.ArrayList;

public class Post {
    public static final String urlAvatar = "http://pik.vn/2014277c7c6c-57e7-4a12-bb2a-c83315886870.png";
    public static final String urlOffline = "http://pik.vn/2014672b230d-420c-4855-adc8-bb370d696e37.png";
    public static final String urlOnline = "http://pik.vn/2014e76c1d82-9947-4362-991c-c29457164f29.png";
    public static final String EMO_PREFIX = "images/smilies";
    public static final int DP32 = Util.convertDpToPx(MainApplication.getAppContext(), 32);
    public static final int TEXT_SIZE = 1;


    private ArrayList<Bitmap> bitmaps;
    public FontSpan font;
    public ImageSpan image;
    private boolean isMultiQuote;
    public boolean isOnline;
    private String mJd;
    private String mPosts;
    public String mTime;
    private String mTitle;
    private String mUrlAvatar;
    private String mUser;
    private String mUserTitle;
    public String m_UserId;
    private String m_id;
    public CustomSpanable quote;
    public CustomSpanable size;
    private String mPlainText;
    public TypeSpan type;
    public CustomSpanable typeU;
    public CustomSpanable web;
    public SpannableString mContent;

    public class ImageSpan extends CustomSpanable {
        ArrayList<Bitmap> bitmaps;

        public ImageSpan() {
            this.bitmaps = new ArrayList();
        }

        public void SetBitmap(int i, Bitmap bitmap) {
            this.bitmaps.set(i, bitmap);
        }

        public void add(String str, int i, int i2, Bitmap bitmap) {
            super.add(str, i, i2);
            this.bitmaps.add(bitmap);
        }

        public void addBitmap(Bitmap bitmap) {
            this.bitmaps.add(bitmap);
        }

        public Bitmap getBitmap(int i) {
            return (Bitmap) this.bitmaps.get(i);
        }

        public ArrayList<Bitmap> getBitmap() {
            return this.bitmaps;
        }

        public int sizeBitmap() {
            return this.bitmaps.size();
        }
    }

    public class FontSpan extends CustomSpanable {
        private ArrayList<String> color;
        private ArrayList<Integer> size;

        public FontSpan() {
            this.color = new ArrayList();
            this.size = new ArrayList();
        }

        public void add(String str, int i, int i2) {
            super.add(str, i, i2);
        }

        public void add(String str, int i, int i2, String str2, int i3) {
            super.add(str, i, i2);
            this.color.add(str2);
            this.size.add(Integer.valueOf(i3));
        }

        public String color(int i) {
            return (String) this.color.get(i);
        }

        public int size(int i) {
            return ((Integer) this.size.get(i)).intValue();
        }
    }

    public class TypeSpan extends CustomSpanable {
        private int type;

        public void add(String str, int i, int i2, int i3) {
            super.add(str, i, i2);
            this.type = i3;
        }

        public int type() {
            return this.type;
        }
    }

    public Post() {
        this.mJd = "";
        this.mPosts = "0";
        this.m_UserId = "";
        this.isOnline = false;
        this.mTime = "";
        this.mPlainText = new String();
        this.web = new CustomSpanable();
        this.bitmaps = new ArrayList();
        this.image = new ImageSpan();
        this.font = new FontSpan();
        this.type = new TypeSpan();
        this.typeU = new CustomSpanable();
        this.quote = new CustomSpanable();
        this.isMultiQuote = false;
    }

    public String Id() {
        return this.m_id;
    }

    public void Info(String str, String str2, String str3, String str4) {
        this.mUser = str;
        this.mUserTitle = str2;
        this.mTime = str3;
        this.mUrlAvatar = str4;
    }

    public String JD() {
        return this.mJd;
    }

    public String Posts() {
        return this.mPosts;
    }

    public String Time() {
        return this.mTime;
    }

    public String getUrlAvatar() {
        return this.mUrlAvatar;
    }

    public String User() {
        return this.mUser;
    }

    public String UserTitle() {
        return this.mUserTitle;
    }

    public void addText(String str) {
        this.mPlainText += str;
    }

//    public void addText(Element element, boolean isGetWholeText) {
//        for (Node node : element.childNodes()) {
//            if (node instanceof TextNode) {
//                if (isGetWholeText) {
//                    addText(((TextNode) node).getWholeText());
//                } else {
//                    addText(((TextNode) node).text());
//                }
//            }
//        }
//    }

//    public void addText(Node node, boolean isGetWholeText) {
//        if (node instanceof TextNode) {
//            if (isGetWholeText) {
//                addText(((TextNode) node).getWholeText());
//            } else {
//                addText(((TextNode) node).text());
//            }
//        }
//    }

    public String getText() {
        return this.mPlainText;
    }

    public boolean isEmpty() {
        for (int i = 0; i < this.mPlainText.length(); i++) {
            if (this.mPlainText.codePointAt(i) != 32) {
                return false;
            }
        }
        return this.bitmaps.size() == 0;
    }

    public boolean isMultiQuote() {
        return this.isMultiQuote;
    }



    public void set(String str, ArrayList<Bitmap> arrayList) {
        this.mPlainText = str;
        this.bitmaps = arrayList;
    }

    public void setId(String str) {
        this.m_id = str;
    }

    public void setJD(String str) {
        this.mJd = str;
    }

    public void setMultiQuote(boolean z) {
        this.isMultiQuote = z;
    }

    public void setPosts(String str) {
        this.mPosts = str;
    }

    public void setTitle(String str) {
        this.mTitle = str;
    }

    public String title() {
        return this.mTitle;
    }

    public SpannableString getContent() {
        return mContent;
    }

    public void preloadEmo() {
        for (int j = 0; j < image.getSize(); j++) {
            String url = image.getStr(j);
            if (url.contains(EMO_PREFIX)) {
                EmoLoader.getInstance().initEmoBitmapCache(url);
            }
        }
    }



    public void initContent() {
        try {
            preloadEmo();

            mContent = new SpannableString(getText());
            int curPos = 0;
            int start;
            int end;
            while (curPos < font.getSize()) {
                start = font.getStart(curPos).intValue();
                end = font.getEnd(curPos).intValue();
                mContent.setSpan(new RelativeSizeSpan((font.size(curPos) > 3 ? (float) (((double) (font.size(curPos) - 3)) / 10.0d)
                        : (float) (((double) (-(font.size(curPos) - 3))) / 10.0d)) + 1), start, end, 18);
                curPos++;
            }
            curPos = 0;
            while (curPos < web.getSize()) {
                start = web.getStart(curPos).intValue();
                end = web.getEnd(curPos).intValue();
                mContent.setSpan(new URLSpan(web.getStr(curPos)), start, end, 18);
                curPos++;
            }
            curPos = 0;
            while (curPos < quote.getSize()) {
                start = quote.getStart(curPos).intValue();
                end = quote.getEnd(curPos).intValue();
                mContent.setSpan(new QuoteSpan(MainApplication.getAppContext().getResources().getColor(R.color.ics_blue_dark)), start, end, 18);
                mContent.setSpan(new StyleSpan(2), start, end, 18);
//                mContent.setSpan(new ForegroundColorSpan(QuickAction.WOOD_TEXT_TITLE, start, end, 18);
                curPos++;
            }
            curPos = 0;
            while (curPos < type.getSize()) {
                start = type.getStart(curPos).intValue();
                end = type.getEnd(curPos).intValue();
                mContent.setSpan(new StyleSpan(type.type()), start, end, 18);
                curPos++;
            }
            curPos = 0;
            while (curPos < typeU.getSize()) {
                start = typeU.getStart(curPos).intValue();
                end = typeU.getEnd(curPos).intValue();
                mContent.setSpan(new UnderlineSpan(), start, end, 18);
                curPos++;
            }

            curPos = 0;
            while (curPos < image.getSize()) {
                    if (image.getStr(curPos).contains(EMO_PREFIX)) {
                        BitmapDrawable bmp = EmoLoader.getInstance().getBitmapFromMemCache(image.getStr(curPos));
                        if (bmp != null) {
                            android.text.style.ImageSpan span = new android.text.style.ImageSpan(bmp, 0);
                            start = image.getStart(curPos).intValue();
                            end = image.getEnd(curPos).intValue();
                            mContent.setSpan(span, start, end, 18);
                        }
                    }
                curPos++;
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }
}
