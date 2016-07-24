package com.whoami.voz.ui.contain.item;

import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.QuoteSpan;
import android.text.style.StyleSpan;

import com.whoami.voz.R;
import com.whoami.voz.ui.main.MainApplication;
import com.whoami.voz.ui.utils.CustomSpanable;
import com.whoami.voz.ui.utils.EmoLoader;

/**
 * Created by thaidh on 7/24/16.
 */
public class ContentItem {
    public static final int TYPE_PLAIN_TEXT = 0;
    public static final int TYPE_PHOTO = 1;

    public int type;
    public SpannableString mContent;
    public String mData;

    public CustomSpanable mEmoList;
    public CustomSpanable mQuoteList;

    public ContentItem(int type, String mData) {
        this.type = type;
        this.mData = mData;
        this.mEmoList = new CustomSpanable();
        this.mQuoteList = new CustomSpanable();
    }

    public void addEmo(String url, int start, int end) {
        mEmoList.add(url, start, end);
    }

    public void addQuote(int start, int end) {
        mQuoteList.add("" ,start, end);
    }

    public void initContent() {
        if (type == ContentItem.TYPE_PLAIN_TEXT) {
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

            for (int i = 0; i < mQuoteList.getSize(); i++)  {
                int start = mQuoteList.getStart(i).intValue();
                int end = mQuoteList.getEnd(i).intValue();
                mContent.setSpan(new QuoteSpan(ContextCompat.getColor(MainApplication.getAppContext(), R.color.ics_blue_dark)), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                mContent.setSpan(new StyleSpan(2), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                mContent.setSpan(new ForegroundColorSpan(ContextCompat.getColor(MainApplication.getAppContext(), R.color.primary_material_light)), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
        }
    }

}
