package com.nna88.voz.contain;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

import com.nna88.voz.main.BuildConfig;
import com.nna88.voz.main.Global;
import com.nna88.voz.main.R;
import com.nna88.voz.main.UILApplication;
import com.nna88.voz.quickaction.QuickAction;
import com.nna88.voz.util.Util;
import com.nna88.voz.util.spanable;
import com.whoami.voz.ui.utils.EmoLoader;

import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class Post {
    public static final String urlAvatar = "http://pik.vn/2014277c7c6c-57e7-4a12-bb2a-c83315886870.png";
    public static final String urlOffline = "http://pik.vn/2014672b230d-420c-4855-adc8-bb370d696e37.png";
    public static final String urlOnline = "http://pik.vn/2014e76c1d82-9947-4362-991c-c29457164f29.png";
    public static final String EMO_PREFIX = "images/smilies";
    public static final int DP32 = Util.convertDpToPx(UILApplication.getAppContext(), 32);
    public static final int TEXT_SIZE = 1;


    private ArrayList<Bitmap> bitmaps;
    public spanable border;
    public String css;
    public fontSpan font;
    public String html;
    public String htmlDocType;
    public String htmlMenu;
    public String htmlSign;
    public bitmapSpan image;
    public spanable img;
    private boolean isMultiQuote;
    public boolean isOnline;
    private Bitmap mAvatar;
    public int mCount;
    public String mIndex;
    private String mJd;
    private String mPosts;
    public String mTime;
    private String mTitle;
    private String mUrlAvatar;
    private String mUser;
    private String mUserTitle;
    public String m_UserId;
    private String m_id;
    private String m_postId;
    public spanable quote;
    public spanable size;
    public int style;
    private String tContain;
    public String test;
    public typeSpan type;
    public spanable typeU;
    public spanable web;
    public SpannableString mContent;

    public class bitmapSpan extends spanable {
        ArrayList<Bitmap> bitmaps;

        public bitmapSpan() {
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

    public class fontSpan extends spanable {
        private ArrayList<String> color;
        private ArrayList<Integer> size;

        public fontSpan() {
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

    public class typeSpan extends spanable {
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
        this.mIndex = "";
        this.htmlDocType = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML Basic 1.1//EN\"\n    \"http://www.w3.org/TR/xhtml-basic/xhtml-basic11.dtd\">";
        this.test = "<input type=\"button\" value=\"Say hello\" onClick=\"showAndroidToast('Hello Android!')\" />\n";
        this.htmlMenu = "<div>\n<table style=\"width:100%\">\n<tr>\n\t<td >\n                <!-- #post -->\n\t\t<div style=\"float:right\">menu_thang</div>\n                <!-- time -->\n\t\t<div class=\"normal\">menu_time</div>\n\t</td>\n</tr>\n\n<tr>\n\t<td>\n\t\t<!-- user info -->\n\t\t<table cellpadding=\"0\" cellspacing=\"6\" border=\"0\" width=\"100%\">\n\t\t<tr>\n\t\t\t<td><img src=\"menu_avatar/></td>\n\t\t\t<td nowrap=\"nowrap\">\n\t\t\t\t<div >\n                                        <!-- online -->\n\t\t\t\t\t<img src=\"menu_online/>\n                                       <!--ten user -->\n\t\t\t\t\tmenu_username\n\t\t\t\t</div>\n\t\t\t\t<div class=\"smallfont\">menu_title</div>\n\t\t\t</td>\n\t\t\t<td width=\"100%\">&nbsp;</td>\n\t\t\t<td valign=\"top\" nowrap=\"nowrap\">\n\t\t\t\t<div class=\"smallfont\">\n\t\t\t\t\t<div>J/d: menu_date</div>\n\t\t\t\t\t<div>\n\t\t\t\t\t\tmenu_posts\n\t\t\t\t\t</div>\n\t\t\t\t</div>\n\n\t\t\t</td>\n\t\t</tr>\n\t\t</table>\n\t\t<!-- / user info -->\n\t</td>\n</tr>\n\n<table>\n</div>";
        this.tContain = new String();
        this.img = new spanable();
        this.web = new spanable();
        this.bitmaps = new ArrayList();
        this.image = new bitmapSpan();
        this.font = new fontSpan();
        this.type = new typeSpan();
        this.typeU = new spanable();
        this.quote = new spanable();
        this.isMultiQuote = false;
    }



    public Bitmap Avatar() {
        return this.mAvatar;
    }

    public String Id() {
        return this.m_id;
    }

    public void Info(String str, String str2, String str3, Bitmap bitmap, String str4) {
        this.mUser = str;
        this.mUserTitle = str2;
        this.mTime = str3;
        this.mAvatar = bitmap;
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
        this.tContain += str;
    }

    public String getPost() {
        String str = this.isMultiQuote ? "<div id=\"wrapper\" style=\"background-color:#" + Global.themeColor2[Global.iTheme][1] + "\">" : "<div id=\"wrapper\" style=\"background-color:#" + Global.themeColor2[Global.iTheme][0] + "\">";
        String str2 = "<font color=\"#" + Global.themeColor2[Global.iTheme][5] + "\">";
        String str3 = "<font color=\"#" + Global.themeColor2[Global.iTheme][4] + "\">";
        this.css = "<style type=\"text/css\">\n    \thtml, body {\n    \t\tmargin:0;\n    \t}\n    \t#wrapper {\n    \t}\n.alt2\n{\n\tbackground: #" + Global.themeColor2[Global.iTheme][0] + ";\n" + "\tcolor: #888888;\n" + "\tfont-style:italic;\n" + "}\n" + "    </style>" + "<script type=\"text/javascript\">\n" + "    function showAndroidToast(toast) {\n" + "        Android.showToast(toast);\n" + "    }\n" + "</script>";
        return this.htmlDocType + "<html><head>" + ("<meta name='viewport' content=' width=device-width, initial-scale=" + Global.iSize + ", minimum-scale=0.2, maximum-scale=2.5, user-scalable=no'/>") + this.css + "</head><body>" + str + str3 + this.htmlMenu + "</font>" + str2 + this.html + "</font></div></body></html>";
    }

    public String getText() {
        return this.tContain;
    }

    public boolean isEmpty() {
        for (int i = 0; i < this.tContain.length(); i++) {
            if (this.tContain.codePointAt(i) != 32) {
                return false;
            }
        }
        return this.bitmaps.size() == 0;
    }

    public boolean isMultiQuote() {
        return this.isMultiQuote;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void parserAvatar(org.jsoup.nodes.Element r5) {
        throw new UnsupportedOperationException("Method not decompiled: com.nna88.voz.contain.Post.parserAvatar(org.jsoup.nodes.Element):void");
    }

    public void parserContain(Element element) {
        try {
            this.html = element.toString();
            if (this.htmlSign != null) {
                this.html += this.htmlSign;
            }
            this.html = this.html.replace("<img src=\"http", "<img onClick=\"showAndroidToast(this.getAttribute('src'))\" style=\"width:95%;\" src=\"http");
            this.html = this.html.replace("<img onClick=\"showAndroidToast(this.getAttribute('src'))\" style=\"width:95%;\" src=\"http://www.google.com/+1/button/images/icon.png", "<img src=\"http://www.google.com/+1/button/images/icon.png\"");
            this.html = this.html.replace("style=\"margin:20px; margin-top:5px; \"", "style=\"margin-left:10px;margin-right:10px; \"");
            this.html = this.html.replace("<a href=\"/redirect/index.php?link=", "<a href=\"");
            this.html = this.html.replace("onload=\"NcodeImageResizer.createOn(this);\"", BuildConfig.FLAVOR);
            this.html = this.html.replace("%3A", ":");
            this.html = this.html.replace("%2F", "/");
            this.html = this.html.replace("%3F", "?");
            this.html = this.html.replace("%3D", "=");
            this.html = this.html.replace("%26", "&");
            if (element != null && element.attr("id").split("_").length > 2) {
                this.m_id = element.attr("id").split("_")[2];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parserTime(Element element) {
        try {
            String text = element.text();
            if (text != null && text.split(" ").length > 3) {
                String[] split = text.split(" ");
                this.mTime = split[2] + " " + split[3];
                this.mIndex = split[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void set(String str, ArrayList<Bitmap> arrayList) {
        this.tContain = str;
        this.bitmaps = arrayList;
    }

    public void setAvatar(Bitmap bitmap) {
        this.mAvatar = bitmap;
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
            if (url.contains("images/smilies")) {
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
//                if (font.color(curPos).equals("Purple")) {
//                    content.setSpan(new ForegroundColorSpan(-8388480), start, end, 18);
//                }
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
                mContent.setSpan(new QuoteSpan(UILApplication.getAppContext().getResources().getColor(R.color.ics_blue_dark)), start, end, 18);
                mContent.setSpan(new StyleSpan(2), start, end, 18);
                mContent.setSpan(new ForegroundColorSpan(QuickAction.WOOD_TEXT_TITLE), start, end, 18);
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
//                if (image.getStr(curPos).contains("http://") || image.getStr(curPos).contains("https://")) {
//                    if (curPos < image.getSize()
//                            && image.getStart(curPos).intValue() < mContent.length()
//                            && image.getEnd(curPos).intValue() < mContent.length()) {
//                        AlignmentSpan.Standard standard = new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER);
//                        ImageSpan span = new ImageSpan(UILApplication.getAppContext(), image.getBitmap(curPos), 0);
//                        start = image.getStart(curPos).intValue();
//                        end = image.getEnd(curPos).intValue();
////                        CustomClickableSpan customClickableSpan = new CustomClickableSpan(mContent.subSequence(start, end).toString());
//                        mContent.setSpan(span, start, end, 18);
//                        mContent.setSpan(standard, start + -2 >= 0 ? start - 2 : 0, end, 18);
////                        mContent.setSpan(customClickableSpan, start, end, 33);
//                    }
//                } else {
                    if (image.getStr(curPos).contains(EMO_PREFIX)) {
//                        BitmapDrawable d = new BitmapDrawable(UILApplication.getAppContext().getResources(), EmoLoader.getInstance().getBitmapFromMemCache(image.getStr(curPos)));
//                        d.setBounds(0, 0, (int) (((float) DP32) * TEXT_SIZE), (int) (((float) DP32) * TEXT_SIZE));
//                    if (image.getStr(curPos).contains("attachmentid")) {
//                        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
//                    } else {
//                        d.setBounds(0, 0, (int) (((float) DP32) * TEXT_SIZE), (int) (((float) DP32) * TEXT_SIZE));
//                    }
                        BitmapDrawable bmp = EmoLoader.getInstance().getBitmapFromMemCache(image.getStr(curPos));
                        if (bmp != null) {
                            ImageSpan span = new ImageSpan(bmp, 0);
                            start = image.getStart(curPos).intValue();
                            end = image.getEnd(curPos).intValue();
                            mContent.setSpan(span, start, end, 18);
                        }
                    }
//                }
                curPos++;
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }
}
