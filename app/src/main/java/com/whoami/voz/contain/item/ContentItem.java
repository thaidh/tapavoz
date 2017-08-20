package com.whoami.voz.contain.item;

import android.graphics.drawable.BitmapDrawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;

import com.whoami.voz.utils.CustomSpanable;
import com.whoami.voz.utils.EmoLoader;

/**
 * Created by thaidh on 7/24/16.
 */
public class ContentItem {
    public static final int TYPE_PLAIN_TEXT = 0;
    public static final int TYPE_PHOTO = 1;
    public static final int TYPE_QUOTE = 2;

    public int type;
    public SpannableString mContent;
    public String mData;

    public CustomSpanable mEmoList;
    public CustomSpanable mQuoteList;
    public CustomSpanable mUrlList;

    public ContentItem(int type, String mData) {
        this.type = type;
        this.mData = mData;
        this.mEmoList = new CustomSpanable();
        this.mQuoteList = new CustomSpanable();
        this.mUrlList = new CustomSpanable();
    }

    public void addEmo(String url, int start, int end) {
        mEmoList.add(url, start, end);
    }

    public void addUrl(String url, int start, int end) {
        mUrlList.add(url, start, end);
    }

    public void addQuote(int start, int end) {
        mQuoteList.add("" ,start, end);
    }

    public void initContent() {
        mContent = new SpannableString(mData);
        for (int i = 0; i < mEmoList.getSize(); i++) {
            BitmapDrawable bmp = EmoLoader.getInstance().getBitmapFromMemCache(mEmoList.getStr(i));
            if (bmp != null) {
                android.text.style.ImageSpan span = new android.text.style.ImageSpan(bmp, 0);
                int start = mEmoList.getStart(i).intValue();
                int end = mEmoList.getEnd(i).intValue();
                mContent.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
        }
        for (int i = 0; i < mUrlList.getSize(); i++) {
            URLSpan span = new URLSpan(mUrlList.getStr(i));
//            ForegroundColorSpan span = new ForegroundColorSpan(ContextCompat.getColor(MainApplication.getAppContext(), R.color.blue));
            int start = mUrlList.getStart(i).intValue();
            int end = mUrlList.getEnd(i).intValue();
            mContent.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        //
//            for (int i = 0; i < mQuoteList.getSize(); i++)  {
//                int start = mQuoteList.getStart(i).intValue();
//                int end = mQuoteList.getEnd(i).intValue();
//                mContent.setSpan(new QuoteSpan(ContextCompat.getColor(MainApplication.getAppContext(), R.color.ics_blue_dark)), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//                mContent.setSpan(new StyleSpan(2), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//                mContent.setSpan(new ForegroundColorSpan(ContextCompat.getColor(MainApplication.getAppContext(), R.color.primary_material_light)), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//            }
    }

}
