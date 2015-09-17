package com.nna88.voz.main;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.nna88.voz.contain.Post;
import com.nna88.voz.listview.listViewCustom3Html;
import com.nna88.voz.listview.listViewCustom3Html.OnActionImageClickListener;
import com.nna88.voz.main.MyNetwork.OnResult;
import com.nna88.voz.mysqlite.MySQLiteHelper;
import com.nna88.voz.quickaction.ActionItem;
import com.nna88.voz.quickaction.QuickAction;
import com.nna88.voz.quickaction.QuickAction.OnActionItemClickListener;
import com.nna88.voz.ui.SidebarAdapter.Item;
import com.nna88.voz.util.Util;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.http.conn.routing.HttpRouteDirector;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class Page3New extends Page {
    private ArrayList<Post> ListContains;
    private listViewCustom3Html adapter;
    private FullScreenImage fullScreen;
    private String humanverify_hash;
    private int iCallFromNotification;
    private int iCallFromNotificationQuote;
    private int iPostType;
    private String linkapicaptcha;
    private String mPostId;
    private QuickAction mQuickAction;
    private String mTextTitle;
    private MyNetwork myNetwork;
    private String recaptcha_challenge_field;
    private String url;
    private String urlCaptcha;

    /* renamed from: com.nna88.voz.main.Page3New.4 */
    class AnonymousClass4 implements OnActionItemClickListener {
        final /* synthetic */ int val$pos;

        AnonymousClass4(int i) {
            this.val$pos = i;
        }

        public void onItemClick(QuickAction quickAction, int i, int i2) {
            if (i2 == 3) {
                Page3New.this.clickMultiQuote(this.val$pos);
            } else if (i2 == 7) {
                if (Page3New.this.mUser.isLogin()) {
                    Intent r1 = new Intent(Page3New.this.mContext, PageRecentPost.class);
                    r1.putExtra("USERID", ((Post) Page3New.this.ListContains.get(this.val$pos)).m_UserId);
                    Page3New.this.startActivity(r1);
                }
            } else if (i2 == 6) {
                Intent r1 = new Intent(Page3New.this.mContext, PageNewThread.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POSTTYPE", 7);
                bundle.putString("MID", ((Post) Page3New.this.ListContains.get(this.val$pos)).m_UserId);
                r1.putExtras(bundle);
                Page3New.this.startActivityForResult(r1, 7);
            } else {
                String str = Global.URL + Page3New.this.doc.select("a:has(img[src=images/buttons/reply.gif])").attr("href");
                if (i2 == 1) {
                    Page3New.this.iPostType = 0;
                } else if (i2 == 2) {
                    Page3New.this.iPostType = 1;
                    str = "https://vozforums.com/newreply.php?do=newreply&p=" + ((Post) Page3New.this.ListContains.get(this.val$pos)).Id();
                } else if (i2 == 5) {
                    str = "https://vozforums.com/editpost.php?do=editpost&p=" + ((Post) Page3New.this.ListContains.get(this.val$pos)).Id();
                    Page3New.this.iPostType = 3;
                } else if (i2 == 4) {
                    Page3New.this.iPostType = 2;
                    str = "https://vozforums.com/editpost.php?do=editpost&p=" + ((Post) Page3New.this.ListContains.get(this.val$pos)).Id();
                }
                Page3New.this.myNetwork.getDoc(str, new OnResult() {
                    public void onResultListenter(Document document, int i) {
                        Page3New.this.doc2 = document;
                        if (Page3New.this.iPostType == 3) {
                            Page3New.this.TaskCancle();
                            Page3New.this.mTask = new TaskGetHtml();
                            Page3New.this.mTask.execute(new Integer[]{Integer.valueOf(6)});
                            return;
                        }
                        try {
                            if (Page3New.this.doc2 != null) {
                                String str = BuildConfig.FLAVOR;
                                Intent intent = new Intent(Page3New.this.mContext, PageNewThread.class);
                                Bundle bundle = new Bundle();
                                if (Page3New.this.iPostType != 0) {
                                    Element first = Page3New.this.doc2.select("textarea[name=message]").first();
                                    if (first != null) {
                                        str = ((TextNode) first.textNodes().get(0)).getWholeText();
                                    }
                                }
                                bundle.putString(NotificationCompat.CATEGORY_MESSAGE, str);
                                bundle.putString(MySQLiteHelper.COLUMN_TITLE, Page3New.this.mTextTitle);
                                bundle.putFloat("textsize", Page3New.this.mTextSize);
                                bundle.putInt("POSTTYPE", Page3New.this.iPostType);
                                intent.putExtras(bundle);
                                Page3New.this.startActivityForResult(intent, Page3New.this.iPostType);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    /* renamed from: com.nna88.voz.main.Page3New.6 */
    class AnonymousClass6 implements OnResult {
        final /* synthetic */ ImageView val$img;

        AnonymousClass6(ImageView imageView) {
            this.val$img = imageView;
        }

        public void onResultListenter(Document document, int i) {
            Page3New.this.urlCaptcha = "http://api.recaptcha.net/" + document.select("img[src]").attr("src");
            Page3New.this.recaptcha_challenge_field = document.select("input[name*=recaptcha_challenge_field]").attr("value");
            Page3New.this.mImageLoad.imageLoader.loadImage(Page3New.this.urlCaptcha, new ImageLoadingListener() {
                public void onLoadingCancelled(String str, View view) {
                }

                public void onLoadingComplete(String str, View view, Bitmap bitmap) {
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    float convertDpToPx = ((float) Util.convertDpToPx(Page3New.this.mContext, 480)) / ((float) width);
                    float convertDpToPx2 = ((float) Util.convertDpToPx(Page3New.this.mContext, 360)) / ((float) width);
                    Matrix matrix = new Matrix();
                    matrix.postScale(convertDpToPx, convertDpToPx2);
                    AnonymousClass6.this.val$img.setImageBitmap(Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false));
                }

                public void onLoadingFailed(String str, View view, FailReason failReason) {
                }

                public void onLoadingStarted(String str, View view) {
                }
            });
        }
    }

    /* renamed from: com.nna88.voz.main.Page3New.7 */
    class AnonymousClass7 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ EditText val$edit1;

        AnonymousClass7(EditText editText, Dialog dialog) {
            this.val$edit1 = editText;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            String str = "\n\n[I]Sent from my " + Global.sYourDevice + " using [URL=\"https://play.google.com/store/apps/details?id=com.nna88.voz.main\"]vozForums[/URL][/I]";
            if (Page3New.this.sPost.indexOf(str) != -1) {
                Page3New.this.sPost = Page3New.this.sPost.substring(0, Page3New.this.sPost.indexOf(str));
            }
            StringBuilder stringBuilder = new StringBuilder();
            Page3New page3New = Page3New.this;
            page3New.sPost = stringBuilder.append(page3New.sPost).append("\n\n[I]Sent from my ").append(Global.sYourDevice).append(" using vozForums[/I]").toString();
            Page3New.this.sPost = Page3New.this.sPost.replace("[url]", "[ur l]");
            Page3New.this.sPost = Page3New.this.sPost.replace("[URL", "[UR L");
            Page3New.this.sPost = Page3New.this.sPost.replace("[IMG", "[IM G");
            Page3New.this.myNetwork.PostReplyQuoteCatcha(Page3New.this.doc2, new OnResult() {
                public void onResultListenter(Document document, int i) {
                    Page3New.this.doc = document;
                    if (Page3New.this.doc.select("table[class=tborder]:contains(The following errors occurred)").first() != null) {
                        try {
                            Page3New.this.Parser(0);
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    try {
                        Page3New.this.Parser(0);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }, Page3New.this.sPost, Page3New.this.humanverify_hash, Page3New.this.recaptcha_challenge_field, this.val$edit1.getText().toString());
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.Page3New.8 */
    class AnonymousClass8 implements OnClickListener {
        final /* synthetic */ ImageView val$img;

        AnonymousClass8(ImageView imageView) {
            this.val$img = imageView;
        }

        public void onClick(View view) {
            Page3New.this.getImageCaptcha(this.val$img);
        }
    }

    public Page3New() {
        this.mTextTitle = null;
        this.iCallFromNotification = 1;
        this.iCallFromNotificationQuote = 1;
    }

    private void alertCatcha() {
        Dialog dialog = new Dialog(this.mContext);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.alertcaptcha);
        dialog.getWindow().setLayout(-1, -2);
        EditText editText = (EditText) dialog.findViewById(R.id.alert_edit1);
        Button button = (Button) dialog.findViewById(R.id.alert_ok);
        Button button2 = (Button) dialog.findViewById(R.id.alert_cancle);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.imgCaptcha);
        editText.setFocusable(true);
        editText.requestFocus();
        dialog.getWindow().setSoftInputMode(5);
        getImageCaptcha(imageView);
        button.setOnClickListener(new AnonymousClass7(editText, dialog));
        button2.setOnClickListener(new AnonymousClass8(imageView));
        dialog.show();
    }

    private void clickMultiQuote(int i) {
        try {
            if (this.mUser.isLogin()) {
                String replace;
                if (((Post) this.ListContains.get(i)).isMultiQuote()) {
                    ((Post) this.ListContains.get(i)).setMultiQuote(false);
                    replace = this.mUser.getMultiquoteId().replace(((Post) this.ListContains.get(i)).Id(), BuildConfig.FLAVOR);
                    if (replace.contains("%2C") && replace.subSequence(0, 3).equals("%2C")) {
                        replace = replace.substring(3, replace.length());
                    }
                    if (replace.contains("%2C") && replace.subSequence(replace.length() - 3, replace.length()).equals("%2C")) {
                        replace = replace.substring(0, replace.length() - 3);
                    }
                    if (replace.contains("%2C%2C")) {
                        replace = replace.replace("%2C%2C", "%2C");
                    }
                } else {
                    ((Post) this.ListContains.get(i)).setMultiQuote(true);
                    replace = !this.mUser.isMultiquote() ? ((Post) this.ListContains.get(i)).Id() : this.mUser.getMultiquoteId() + "%2C" + ((Post) this.ListContains.get(i)).Id();
                }
                this.mUser.addMultiquote(replace);
                runOnUiThread(new Runnable() {
                    public void run() {
                        Page3New.this.adapter.notifyDataSetChanged();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getImageCaptcha(ImageView imageView) {
        this.myNetwork.getDoc(this.linkapicaptcha, new AnonymousClass6(imageView));
    }

    private String getPostIdFromUrl(String str) {
        String str2 = null;
        try {
            if (str.contains("p=")) {
                str2 = str.split("p=")[1];
            }
            return str2.contains("#post") ? str2.split("#post")[1] : str2;
        } catch (Exception e) {
            return null;
        }
    }

    void ActionPopup(View view, int i) throws Exception {
        ActionItem actionItem = new ActionItem(1, "Reply", getResources().getDrawable(R.drawable.tapatalk_bubble_reply));
        ActionItem actionItem2 = new ActionItem(2, "Quote", getResources().getDrawable(R.drawable.tapatalk_bubble_quote));
        ActionItem actionItem3 = new ActionItem(3, "Multi", getResources().getDrawable(R.drawable.tapatalk_bubble_multi));
        ActionItem actionItem4 = new ActionItem(4, "Edit ", getResources().getDrawable(R.drawable.tapatalk_bubble_edit));
        ActionItem actionItem5 = new ActionItem(5, "Delete", getResources().getDrawable(R.drawable.tapatalk_bubble_delete));
        ActionItem actionItem6 = new ActionItem(6, " PM  ", getResources().getDrawable(R.drawable.tapatalk_bubble_pm));
        ActionItem actionItem7 = new ActionItem(7, "Find Post", getResources().getDrawable(R.drawable.tapatalk_find_post));
        this.mQuickAction = new QuickAction(this.mContext);
        this.mQuickAction.addActionItem(actionItem);
        this.mQuickAction.addActionItem(actionItem2);
        this.mQuickAction.addActionItem(actionItem3);
        this.mQuickAction.addActionItem(actionItem6);
        if (((Post) this.ListContains.get(i)).m_UserId.equals(this.mUser.UserId())) {
            this.mQuickAction.addActionItem(actionItem4);
            this.mQuickAction.addActionItem(actionItem5);
        }
        this.mQuickAction.addActionItem(actionItem7);
        this.mQuickAction.show(view);
        this.mQuickAction.setOnActionItemClickListener(new AnonymousClass4(i));
    }

    protected void GoPage(int i, int i2) throws Exception {
        super.GoPage(i, i2);
    }

    void Parser(int i) throws Exception {
        super.Parser(i);
        if (this.doc != null) {
            String str;
            Intent intent;
            Bundle bundle;
            Element first;
            if (i == 3) {
                if (this.iPostType == 3) {
                    this.mTask.cancel(true);
                    this.mTask = new TaskGetHtml();
                    this.mTask.execute(new Integer[]{Integer.valueOf(6)});
                    return;
                }
                str = BuildConfig.FLAVOR;
                intent = new Intent(this.mContext, PageNewThread.class);
                bundle = new Bundle();
                if (this.iPostType != 0) {
                    first = this.doc.select("textarea[name=message]").first();
                    if (first != null) {
                        str = ((TextNode) first.textNodes().get(0)).getWholeText();
                    }
                }
                bundle.putString(NotificationCompat.CATEGORY_MESSAGE, str);
                bundle.putString(MySQLiteHelper.COLUMN_TITLE, this.mTextTitle);
                bundle.putFloat("textsize", this.mTextSize);
                bundle.putInt("POSTTYPE", this.iPostType);
                intent.putExtras(bundle);
                startActivityForResult(intent, this.iPostType);
            } else if (i == 4) {
                Element r0 = this.doc.select("table[class=tborder]:contains(The following errors occurred)").first();
                if (r0 == null || !r0.text().contains("To be able to post links or images your post count")) {
                    Parser(0);
                    return;
                }
                try {
                    toast(r0.text());
                    String obj = "\n\n[I]Sent from my " + Global.sYourDevice + " using [URL=\"https://play.google.com/store/apps/details?id=com.nna88.voz.main\"]vozForums[/URL]" + "[/I]";
                    if (this.sPost.contains(obj)) {
                        this.sPost = this.sPost.substring(0, this.sPost.indexOf(obj));
                        this.sPost += "\n\n[I]Sent from my " + Global.sYourDevice + " using vozForums" + "[/I]";
                        toast("Try Again");
                        this.doc2 = this.doc;
                        TaskCancle();
                        this.mTask = new TaskGetHtml();
                        this.mTask.execute(new Integer[]{Integer.valueOf(4)});
                        return;
                    }
                    this.iPostType = 1;
                    str = BuildConfig.FLAVOR;
                    intent = new Intent(this.mContext, PageNewThread.class);
                    bundle = new Bundle();
                    if (this.iPostType != 0) {
                        first = this.doc.select("textarea[name=message]").first();
                        if (first != null) {
                            str = ((TextNode) first.textNodes().get(0)).getWholeText();
                        }
                    }
                    bundle.putString(NotificationCompat.CATEGORY_MESSAGE, str);
                    bundle.putString(MySQLiteHelper.COLUMN_TITLE, this.mTextTitle);
                    bundle.putFloat("textsize", this.mTextSize);
                    bundle.putInt("POSTTYPE", this.iPostType);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, this.iPostType);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                this.ListContains.clear();
                Element r0;
                try {
                    r0 = this.doc.select("div[id=posts]").first();
                } catch (Exception e2) {
                    r0 = this.doc;
                }
                Elements select = r0.select("td[id^=td_post_]");
                this.iPositiion = 0;
                this.mPostId = getPostIdFromUrl(this.mParser.getUrl());
                this.mList.setAdapter(this.adapter);
                try {
                    this.mTextTitle = select.select("div[class=smallfont]:has(strong)").first().text();
                    setTitle(this.mTextTitle);
                } catch (Exception e3) {
                }
                Iterator it = select.iterator();
                while (it.hasNext()) {
                    r0 = (Element) it.next();
                    Post post = new Post();
                    first = r0.parent().previousElementSibling();
                    post.parserTime(first.previousElementSibling());
                    post.parserAvatar(first);
                    if (Global.bSign) {
                        try {
                            post.htmlSign = r0.select("div:contains(_______)").first().toString();
                            post.htmlSign = post.htmlSign.replace("<pre", "<div");
                            post.htmlSign = post.htmlSign.replace("</pre>", "</div>");
                            post.htmlSign = post.htmlSign.replace("width: 640px;", "width: 100%;");
                        } catch (Exception e4) {
                        }
                    }
                    post.parserContain(r0.select("div[id^=post_message").first());
                    post.mCount = this.ListContains.size();
                    this.ListContains.add(post);
                    if (post.Id().equals(this.mPostId)) {
                        this.iPositiion = this.ListContains.indexOf(post) + 1;
                    }
                }
                this.adapter.notifyDataSetChanged();
                this.mItemCount = 23;
                if (this.mItemOffsetY == null) {
                    this.mItemOffsetY = new int[(this.mItemCount + 1)];
                    this.mItemtemp = new int[(this.mItemCount + 1)];
                }
                this.scrollIsComputed = true;
                if (!(this.iPositiion == 0 || this.iPositiion == 1)) {
                    this.mList.clearFocus();
                    this.mList.postDelayed(new Runnable() {
                        public void run() {
                            Page3New.this.mList.setSelection(Page3New.this.iPositiion);
                        }
                    }, 1700);
                }
                r0 = this.doc.select("a[href=private.php]").first();
                setPage(parsePage(0, 0));
                if (r0 != null) {
                    this.mUser.setUserId(((Element) this.doc.select("td[class=alt2]").get(0)).select("a[href*=mem]").attr("href").split("=")[1]);
                    r0 = this.doc.select("input[name*=securitytoken]").first();
                    if (r0 != null) {
                        this.mUser.setToken(r0.attr("value"));
                    }
                    r0 = this.doc.select("div[style=margin-top:6px]:has(input[value=Post Quick Reply])").first();
                    if (r0 != null) {
                        this.sIdThread = r0.select("input[name=t]").attr("value");
                    }
                    r0 = this.doc.select("td[class=alt1]:has(a[href *= subscription.php]").first();
                    if (r0 == null) {
                        return;
                    }
                    if (r0.select("a[href *= subscription.php]").attr("href").contains("removesubscription")) {
                        this.isSubscribe = true;
                        ((Item) this.mListSideMenu2.get(3)).mTitle = getResources().getString(R.string.UnSubscribe);
                        this.mAdapterSideMenu2.notifyDataSetInvalidated();
                        return;
                    }
                    ((Item) this.mListSideMenu2.get(3)).mTitle = getResources().getString(R.string.Subscribe);
                    this.mAdapterSideMenu2.notifyDataSetInvalidated();
                    this.isSubscribe = false;
                }
            }
        }
    }

    protected void TaskCancle() {
        super.TaskCancle();
        this.ListContains.clear();
        this.adapter.notifyDataSetChanged();
        this.mList.setAdapter(null);
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1) {
            switch (i) {
                case HttpRouteDirector.COMPLETE /*0*/:
                case HttpRouteDirector.CONNECT_TARGET /*1*/:
                    this.specifiedpost = "1";
                    this.sPost = intent.getExtras().getString(NotificationCompat.CATEGORY_MESSAGE);
                    if (this.doc2 != null) {
                        Element first = this.doc2.select("fieldset[class=fieldset]:has(input[name*=humanverify]").first();
                        if (first != null) {
                            this.humanverify_hash = first.select("input[name*=humanverify]").attr("value");
                            this.linkapicaptcha = first.select("iframe[src*=api.recaptcha.net]").attr("src");
                            alertCatcha();
                            return;
                        }
                        TaskCancle();
                        this.mTask = new TaskGetHtml();
                        this.mTask.execute(new Integer[]{Integer.valueOf(4)});
                    }
                case HttpRouteDirector.CONNECT_PROXY /*2*/:
                    this.sPost = intent.getExtras().getString(NotificationCompat.CATEGORY_MESSAGE);
                    try {
                        String str = "\n\n[I]Sent from my " + Global.sYourDevice + " using [URL=\"https://play.google.com/store/apps/details?id=com.nna88.voz.main\"]vozForums[/URL]" + "[/I]";
                        if (this.sPost.indexOf(str) != -1) {
                            this.sPost = this.sPost.substring(0, this.sPost.indexOf(str));
                        }
                        this.sPost += "\n\n[I]Sent from my " + Global.sYourDevice + " using vozForums" + "[/I]";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    TaskCancle();
                    this.mTask = new TaskGetHtml();
                    this.mTask.execute(new Integer[]{Integer.valueOf(5)});
                case R.styleable.Toolbar_contentInsetRight /*7*/:
                    startActivity(new Intent(this.mContext, PagePM.class));
                default:
            }
        }
    }

    public void onBackPressed() {
        if (this.fullScreen == null || !this.fullScreen.isShowing()) {
            if (this.iCallFromNotification != 1) {
                startActivity(new Intent(this, PageCP.class));
            }
            if (this.iCallFromNotificationQuote != 1) {
                Intent intent = new Intent(this, PageQuote.class);
                intent.putExtra("NUMQUOTE", this.iCallFromNotificationQuote);
                startActivity(intent);
            }
            super.onBackPressed();
            return;
        }
        runOnUiThread(new Runnable() {
            public void run() {
                Page3New.this.fullScreen.hide();
            }
        });
    }

    protected void onCreate(Bundle bundle) {
        this.iPage = 3;
        super.onCreate(bundle);
        this.ListContains = new ArrayList();
        this.adapter = new listViewCustom3Html(this, this.ListContains);
        this.mObjectAdapter = this.adapter;
        this.myNetwork = new MyNetwork();
        if (getIntent().getIntExtra("NOTIFICATION", 1) != 1) {
            this.iCallFromNotification = getIntent().getIntExtra("NOTIFICATION", 1);
        }
        if (getIntent().getIntExtra("NOTIFICATIONQUOTE", 1) != 1) {
            this.iCallFromNotificationQuote = getIntent().getIntExtra("NOTIFICATIONQUOTE", 1);
        }
        this.url = getIntent().getStringExtra("URL");
        if (this.url.contains(Global.URL) || this.url.contains(Global.URL2)) {
            this.mParser.setUrl(getIntent().getStringExtra("URL"));
        } else {
            this.mParser.setUrl(Global.URL + getIntent().getStringExtra("URL"));
        }
        this.adapter.setOnActionItemClickListener(new listViewCustom3Html.OnActionItemClickListener() {
            public void onItemClick(int i, View view) {
                Page3New.this.vibrate();
                if (Page3New.this.mUser.isLogin()) {
                    try {
                        Page3New.this.ActionPopup(view, i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        this.adapter.setOnActionImageClickListener(new OnActionImageClickListener() {

            /* renamed from: com.nna88.voz.main.Page3New.2.1 */
            class AnonymousClass1 implements Runnable {
                final /* synthetic */ String val$link;

                AnonymousClass1(String str) {
                    this.val$link = str;
                }

                public void run() {
                    Page3New.this.fullScreen = new FullScreenImage(Page3New.this.mContext, Page3New.this.getWindow().getDecorView());
                    Page3New.this.fullScreen.show(this.val$link);
                    if (Page3New.this.mQuickAction != null) {
                        Page3New.this.mQuickAction.dismiss();
                    }
                }
            }

            public void onItemClick(String str) {
                Page3New.this.runOnUiThread(new AnonymousClass1(str));
            }
        });
        this.mTask.execute(new Integer[]{Integer.valueOf(0)});
    }

    protected void onDestroy() {
        super.onDestroy();
        this.adapter.destroy();
    }

    protected void onRestart() {
        super.onRestart();
        updateSetFont(this.mTextSize);
    }

    protected void onStop() {
        super.onStop();
    }

    protected void updateSetFont(float f) {
        super.updateSetFont(f);
        this.adapter.notifyDataSetChanged();
    }
}
