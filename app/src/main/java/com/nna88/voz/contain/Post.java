package com.nna88.voz.contain;

import android.graphics.Bitmap;
import com.nna88.voz.main.BuildConfig;
import com.nna88.voz.main.Global;
import com.nna88.voz.util.spanable;
import java.util.ArrayList;
import org.jsoup.nodes.Element;

public class Post {
    public static final String urlAvatar = "http://pik.vn/2014277c7c6c-57e7-4a12-bb2a-c83315886870.png";
    public static final String urlOffline = "http://pik.vn/2014672b230d-420c-4855-adc8-bb370d696e37.png";
    public static final String urlOnline = "http://pik.vn/2014e76c1d82-9947-4362-991c-c29457164f29.png";
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
        this.mJd = BuildConfig.FLAVOR;
        this.mPosts = "0";
        this.m_UserId = BuildConfig.FLAVOR;
        this.isOnline = false;
        this.mTime = BuildConfig.FLAVOR;
        this.mIndex = BuildConfig.FLAVOR;
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

    public String UrlAvatar() {
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
        /*
        r4 = this;
        r0 = "img[src^=customavatars/avatar]";
        r0 = r5.select(r0);	 Catch:{ Exception -> 0x010b }
        r1 = "src";
        r0 = r0.attr(r1);	 Catch:{ Exception -> 0x010b }
        r4.mUrlAvatar = r0;	 Catch:{ Exception -> 0x010b }
        r0 = r4.mUrlAvatar;	 Catch:{ Exception -> 0x010b }
        r1 = "";
        r0 = r0.equals(r1);	 Catch:{ Exception -> 0x010b }
        if (r0 == 0) goto L_0x001c;
    L_0x0018:
        r0 = "http://pik.vn/2014277c7c6c-57e7-4a12-bb2a-c83315886870.png";
        r4.mUrlAvatar = r0;	 Catch:{ Exception -> 0x010b }
    L_0x001c:
        r0 = "div:containsOwn(Join Date)";
        r0 = r5.select(r0);	 Catch:{ Exception -> 0x0129 }
        r0 = r0.first();	 Catch:{ Exception -> 0x0129 }
        r0 = r0.text();	 Catch:{ Exception -> 0x0129 }
        r1 = "Date:";
        r1 = r0.contains(r1);	 Catch:{ Exception -> 0x0129 }
        if (r1 == 0) goto L_0x003b;
    L_0x0032:
        r1 = "Date:";
        r0 = r0.split(r1);	 Catch:{ Exception -> 0x0129 }
        r1 = 1;
        r0 = r0[r1];	 Catch:{ Exception -> 0x0129 }
    L_0x003b:
        r4.mJd = r0;	 Catch:{ Exception -> 0x0129 }
    L_0x003d:
        r0 = "div:containsOwn(Posts: )";
        r0 = r5.select(r0);	 Catch:{ Exception -> 0x012c }
        r0 = r0.first();	 Catch:{ Exception -> 0x012c }
        r0 = r0.text();	 Catch:{ Exception -> 0x012c }
        r4.mPosts = r0;	 Catch:{ Exception -> 0x012c }
    L_0x004d:
        r0 = "img[src=images/statusicon/user_online.gif";
        r0 = r5.select(r0);	 Catch:{ Exception -> 0x0112 }
        r0 = r0.first();	 Catch:{ Exception -> 0x0112 }
        if (r0 == 0) goto L_0x0117;
    L_0x0059:
        r0 = 1;
        r4.isOnline = r0;	 Catch:{ Exception -> 0x0112 }
    L_0x005c:
        r0 = "a[class=bigusername]";
        r0 = r5.select(r0);	 Catch:{ Exception -> 0x012f }
        r0 = r0.text();	 Catch:{ Exception -> 0x012f }
        r4.mUser = r0;	 Catch:{ Exception -> 0x012f }
        r0 = "a[class=bigusername]";
        r0 = r5.select(r0);	 Catch:{ Exception -> 0x012f }
        r0 = r0.first();	 Catch:{ Exception -> 0x012f }
        r1 = "href";
        r0 = r0.attr(r1);	 Catch:{ Exception -> 0x012f }
        r1 = "u=";
        r0 = r0.split(r1);	 Catch:{ Exception -> 0x012f }
        r1 = 1;
        r0 = r0[r1];	 Catch:{ Exception -> 0x012f }
        r4.m_UserId = r0;	 Catch:{ Exception -> 0x012f }
    L_0x0083:
        r0 = "div[class=smallfont]";
        r0 = r5.select(r0);	 Catch:{ Exception -> 0x0132 }
        r0 = r0.first();	 Catch:{ Exception -> 0x0132 }
        r0 = r0.text();	 Catch:{ Exception -> 0x0132 }
        r4.mUserTitle = r0;	 Catch:{ Exception -> 0x0132 }
    L_0x0093:
        r0 = r4.htmlMenu;	 Catch:{ Exception -> 0x0112 }
        r1 = "menu_time";
        r2 = r4.mTime;	 Catch:{ Exception -> 0x0112 }
        r0 = r0.replace(r1, r2);	 Catch:{ Exception -> 0x0112 }
        r4.htmlMenu = r0;	 Catch:{ Exception -> 0x0112 }
        r0 = r4.htmlMenu;	 Catch:{ Exception -> 0x0112 }
        r1 = "menu_thang";
        r2 = r4.mIndex;	 Catch:{ Exception -> 0x0112 }
        r0 = r0.replace(r1, r2);	 Catch:{ Exception -> 0x0112 }
        r4.htmlMenu = r0;	 Catch:{ Exception -> 0x0112 }
        r0 = r4.htmlMenu;	 Catch:{ Exception -> 0x0112 }
        r1 = "menu_username";
        r2 = r4.mUser;	 Catch:{ Exception -> 0x0112 }
        r0 = r0.replace(r1, r2);	 Catch:{ Exception -> 0x0112 }
        r4.htmlMenu = r0;	 Catch:{ Exception -> 0x0112 }
        r0 = r4.htmlMenu;	 Catch:{ Exception -> 0x0112 }
        r1 = "menu_title";
        r2 = r4.mUserTitle;	 Catch:{ Exception -> 0x0112 }
        r0 = r0.replace(r1, r2);	 Catch:{ Exception -> 0x0112 }
        r4.htmlMenu = r0;	 Catch:{ Exception -> 0x0112 }
        r0 = r4.htmlMenu;	 Catch:{ Exception -> 0x0112 }
        r1 = "menu_avatar";
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0112 }
        r2.<init>();	 Catch:{ Exception -> 0x0112 }
        r3 = r4.mUrlAvatar;	 Catch:{ Exception -> 0x0112 }
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x0112 }
        r3 = "\" width=\"48\"";
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x0112 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x0112 }
        r0 = r0.replace(r1, r2);	 Catch:{ Exception -> 0x0112 }
        r4.htmlMenu = r0;	 Catch:{ Exception -> 0x0112 }
        r0 = r4.isOnline;	 Catch:{ Exception -> 0x0112 }
        if (r0 == 0) goto L_0x011c;
    L_0x00e6:
        r0 = r4.htmlMenu;	 Catch:{ Exception -> 0x0112 }
        r1 = "menu_online";
        r2 = "http://pik.vn/2014e76c1d82-9947-4362-991c-c29457164f29.png\" width=\"6\"";
        r0 = r0.replace(r1, r2);	 Catch:{ Exception -> 0x0112 }
        r4.htmlMenu = r0;	 Catch:{ Exception -> 0x0112 }
    L_0x00f2:
        r0 = r4.htmlMenu;	 Catch:{ Exception -> 0x0112 }
        r1 = "menu_date";
        r2 = r4.mJd;	 Catch:{ Exception -> 0x0112 }
        r0 = r0.replace(r1, r2);	 Catch:{ Exception -> 0x0112 }
        r4.htmlMenu = r0;	 Catch:{ Exception -> 0x0112 }
        r0 = r4.htmlMenu;	 Catch:{ Exception -> 0x0112 }
        r1 = "menu_posts";
        r2 = r4.mPosts;	 Catch:{ Exception -> 0x0112 }
        r0 = r0.replace(r1, r2);	 Catch:{ Exception -> 0x0112 }
        r4.htmlMenu = r0;	 Catch:{ Exception -> 0x0112 }
    L_0x010a:
        return;
    L_0x010b:
        r0 = move-exception;
        r0 = "http://pik.vn/2014277c7c6c-57e7-4a12-bb2a-c83315886870.png";
        r4.mUrlAvatar = r0;	 Catch:{ Exception -> 0x0112 }
        goto L_0x001c;
    L_0x0112:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x010a;
    L_0x0117:
        r0 = 0;
        r4.isOnline = r0;	 Catch:{ Exception -> 0x0112 }
        goto L_0x005c;
    L_0x011c:
        r0 = r4.htmlMenu;	 Catch:{ Exception -> 0x0112 }
        r1 = "menu_online";
        r2 = "http://pik.vn/2014672b230d-420c-4855-adc8-bb370d696e37.png\" width=\"6\"";
        r0 = r0.replace(r1, r2);	 Catch:{ Exception -> 0x0112 }
        r4.htmlMenu = r0;	 Catch:{ Exception -> 0x0112 }
        goto L_0x00f2;
    L_0x0129:
        r0 = move-exception;
        goto L_0x003d;
    L_0x012c:
        r0 = move-exception;
        goto L_0x004d;
    L_0x012f:
        r0 = move-exception;
        goto L_0x0083;
    L_0x0132:
        r0 = move-exception;
        goto L_0x0093;
        */
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
}
