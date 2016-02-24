package com.nna88.voz.main;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.nna88.voz.contain.Post;
import com.nna88.voz.listview.Page3ListViewAdapter;
import com.nna88.voz.listview.Page3ListViewAdapter.OnImageClickListestener;
import com.nna88.voz.main.MyNetwork.OnResult;
import com.nna88.voz.mysqlite.MySQLiteHelper;
import com.nna88.voz.quickaction.ActionItem;
import com.nna88.voz.quickaction.QuickAction;
import com.nna88.voz.quickaction.QuickAction.OnActionItemClickListener;
import com.nna88.voz.ui.SidebarAdapter.Item;
import com.nna88.voz.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.io.IOUtils;
import org.apache.http.conn.routing.HttpRouteDirector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class Page3 extends Page {
    private ArrayList<Post> ListContains;
    private Page3ListViewAdapter adapter;
    private Bitmap bmImageFailed;
    private Bitmap bmImageStart;
    private String humanverify_hash;
    private int iCallFromNotification;
    private int iCallFromNotificationQuote;
    private int iPostType;
    ImageLoader imageLoader;
    int indexI;
    int indexJ;
    private ArrayList<EmoClass2> lEmo;
    private ArrayList<String> lImage;
    private String linkapicaptcha;
    private ArrayList<TaskLoadAvatart> mListTaskDownAvatart;
    private ArrayList<DownImageAttach> mListTaskImageAttach;
    private String mPostId;
    private QuickAction mQuickAction;
    private String mTextTitle;
    private MyNetwork myNetwork;
    private String recaptcha_challenge_field;
    private String url;
    private String urlCaptcha;

    /* renamed from: com.nna88.voz.main.Page3.10 */
    class AnonymousClass10 implements ImageLoadingListener {
        final /* synthetic */ int val$i;

        AnonymousClass10(int i) {
            this.val$i = i;
        }

        public void onLoadingCancelled(String str, View view) {
            Page3.this.loadAvatarUniversal(this.val$i + 1);
        }

        public void onLoadingComplete(String str, View view, Bitmap bitmap) {
            if (bitmap != null) {
                try {
                    ((Post) Page3.this.ListContains.get(this.val$i)).setAvatar(Page3.this.getResizedBitmapAvatart(bitmap));
                } catch (Exception e) {
                    Page3.this.loadAvatarUniversal(this.val$i + 1);
                    e.printStackTrace();
                    return;
                }
            }
            Page3.this.adapter.notifyDataSetChanged();
            Page3.this.loadAvatarUniversal(this.val$i + 1);
        }

        public void onLoadingFailed(String str, View view, FailReason failReason) {
            Page3.this.loadAvatarUniversal(this.val$i + 1);
        }

        public void onLoadingStarted(String str, View view) {
        }
    }

    /* renamed from: com.nna88.voz.main.Page3.3 */
    class AnonymousClass3 implements OnActionItemClickListener {
        final /* synthetic */ int val$pos;

        AnonymousClass3(int i) {
            this.val$pos = i;
        }

        public void onItemClick(QuickAction quickAction, int i, int i2) {
            if (i2 == 3) {
                Page3.this.clickMultiQuote(this.val$pos);
            } else if (i2 == 7) {
                if (Page3.this.mUser.isLogin()) {
                    Intent r1 = new Intent(Page3.this.mContext, PageRecentPost.class);
                    r1.putExtra("USERID", ((Post) Page3.this.ListContains.get(this.val$pos)).m_UserId);
                    Page3.this.startActivity(r1);
                }
            } else if (i2 == 6) {
                Intent r1 = new Intent(Page3.this.mContext, PageNewThread.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POSTTYPE", 7);
                bundle.putString("MID", ((Post) Page3.this.ListContains.get(this.val$pos)).m_UserId);
                r1.putExtras(bundle);
                Page3.this.startActivityForResult(r1, 7);
            } else {
                String str = Global.URL + Page3.this.doc.select("a:has(img[src=images/buttons/reply.gif])").attr("href");
                if (i2 == 1) {
                    Page3.this.iPostType = 0;
                } else if (i2 == 2) {
                    Page3.this.iPostType = 1;
                    str = "https://vozforums.com/newreply.php?do=newreply&p=" + ((Post) Page3.this.ListContains.get(this.val$pos)).Id();
                } else if (i2 == 5) {
                    str = "https://vozforums.com/editpost.php?do=editpost&p=" + ((Post) Page3.this.ListContains.get(this.val$pos)).Id();
                    Page3.this.iPostType = 3;
                } else if (i2 == 4) {
                    Page3.this.iPostType = 2;
                    str = "https://vozforums.com/editpost.php?do=editpost&p=" + ((Post) Page3.this.ListContains.get(this.val$pos)).Id();
                }
                Page3.this.myNetwork.getDoc(str, new OnResult() {
                    public void onResultListenter(Document document, int i) {
                        Page3.this.doc2 = document;
                        if (Page3.this.iPostType == 3) {
                            Page3.this.TaskCancle();
                            Page3.this.mTask = new TaskGetHtml();
                            Page3.this.mTask.execute(new Integer[]{Integer.valueOf(6)});
                            return;
                        }
                        try {
                            if (Page3.this.doc2 != null) {
                                String str = BuildConfig.FLAVOR;
                                Intent intent = new Intent(Page3.this.mContext, PageNewThread.class);
                                Bundle bundle = new Bundle();
                                if (Page3.this.iPostType != 0) {
                                    Element first = Page3.this.doc2.select("textarea[name=message]").first();
                                    if (first != null) {
                                        str = ((TextNode) first.textNodes().get(0)).getWholeText();
                                    }
                                }
                                bundle.putString(NotificationCompat.CATEGORY_MESSAGE, str);
                                bundle.putString(MySQLiteHelper.COLUMN_TITLE, Page3.this.mTextTitle);
                                bundle.putFloat("textsize", Page3.this.mTextSize);
                                bundle.putInt("POSTTYPE", Page3.this.iPostType);
                                intent.putExtras(bundle);
                                Page3.this.startActivityForResult(intent, Page3.this.iPostType);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    /* renamed from: com.nna88.voz.main.Page3.4 */
    class AnonymousClass4 implements OnResult {
        final /* synthetic */ ImageView val$img;

        AnonymousClass4(ImageView imageView) {
            this.val$img = imageView;
        }

        public void onResultListenter(Document document, int i) {
            Page3.this.urlCaptcha = "http://api.recaptcha.net/" + document.select("img[src]").attr("src");
            Page3.this.recaptcha_challenge_field = document.select("input[name*=recaptcha_challenge_field]").attr("value");
            Page3.this.mImageLoad.imageLoader.loadImage(Page3.this.urlCaptcha, new ImageLoadingListener() {
                public void onLoadingCancelled(String str, View view) {
                }

                public void onLoadingComplete(String str, View view, Bitmap bitmap) {
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    float convertDpToPx = ((float) Util.convertDpToPx(Page3.this.mContext, 480)) / ((float) width);
                    float convertDpToPx2 = ((float) Util.convertDpToPx(Page3.this.mContext, 360)) / ((float) width);
                    Matrix matrix = new Matrix();
                    matrix.postScale(convertDpToPx, convertDpToPx2);
                    AnonymousClass4.this.val$img.setImageBitmap(Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false));
                }

                public void onLoadingFailed(String str, View view, FailReason failReason) {
                }

                public void onLoadingStarted(String str, View view) {
                }
            });
        }
    }

    /* renamed from: com.nna88.voz.main.Page3.5 */
    class AnonymousClass5 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ EditText val$edit1;

        AnonymousClass5(EditText editText, Dialog dialog) {
            this.val$edit1 = editText;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            String str = "\n\n[I]Sent from my " + Global.sYourDevice + " using [URL=\"https://play.google.com/store/apps/details?id=com.nna88.voz.main\"]vozForums[/URL][/I]";
            if (Page3.this.sPost.indexOf(str) != -1) {
                Page3.this.sPost = Page3.this.sPost.substring(0, Page3.this.sPost.indexOf(str));
            }
            StringBuilder stringBuilder = new StringBuilder();
            Page3 page3 = Page3.this;
            page3.sPost = stringBuilder.append(page3.sPost).append("\n\n[I]Sent from my ").append(Global.sYourDevice).append(" using vozForums[/I]").toString();
            Page3.this.sPost = Page3.this.sPost.replace("[url]", "[ur l]");
            Page3.this.sPost = Page3.this.sPost.replace("[URL", "[UR L");
            Page3.this.sPost = Page3.this.sPost.replace("[IMG", "[IM G");
            Page3.this.myNetwork.PostReplyQuoteCatcha(Page3.this.doc2, new OnResult() {
                public void onResultListenter(Document document, int i) {
                    Page3.this.doc = document;
                    if (Page3.this.doc.select("table[class=tborder]:contains(The following errors occurred)").first() != null) {
                        try {
                            Page3.this.Parser(0);
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    try {
                        Page3.this.Parser(0);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }, Page3.this.sPost, Page3.this.humanverify_hash, Page3.this.recaptcha_challenge_field, this.val$edit1.getText().toString());
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.Page3.6 */
    class AnonymousClass6 implements OnClickListener {
        final /* synthetic */ ImageView val$img;

        AnonymousClass6(ImageView imageView) {
            this.val$img = imageView;
        }

        public void onClick(View view) {
            Page3.this.getImageCaptcha(this.val$img);
        }
    }

    /* renamed from: com.nna88.voz.main.Page3.9 */
    class LoadEmojListener implements ImageLoadingListener {
        final /* synthetic */ int val$i;

        LoadEmojListener(int i) {
            this.val$i = i;
        }

        public void onLoadingCancelled(String str, View view) {
            try {
                ((Post) Page3.this.ListContains.get(((EmoClass2) Page3.this.lEmo.get(this.val$i)).i)).image.SetBitmap(((EmoClass2) Page3.this.lEmo.get(this.val$i)).j, Page3.this.bmImageFailed);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Page3.this.adapter.notifyDataSetChanged();
            Page3.this.loadEmo(this.val$i + 1);
        }

        public void onLoadingComplete(String str, View view, Bitmap bitmap) {
            if (bitmap != null) {
                try {
                    ((Post) Page3.this.ListContains.get(((EmoClass2) Page3.this.lEmo.get(this.val$i)).i)).image.SetBitmap(((EmoClass2) Page3.this.lEmo.get(this.val$i)).j, bitmap);
                } catch (Exception e) {
                    Exception exception = e;
                    try {
                        ((Post) Page3.this.ListContains.get(((EmoClass2) Page3.this.lEmo.get(this.val$i)).i)).image.SetBitmap(((EmoClass2) Page3.this.lEmo.get(this.val$i)).j, Page3.this.bmImageFailed);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    Page3.this.adapter.notifyDataSetChanged();
                    Page3.this.loadEmo(this.val$i + 1);
                    exception.printStackTrace();
                    return;
                }
            }
            Page3.this.adapter.notifyDataSetChanged();
            Page3.this.loadEmo(this.val$i + 1);
        }

        public void onLoadingFailed(String str, View view, FailReason failReason) {
            try {
                ((Post) Page3.this.ListContains.get(((EmoClass2) Page3.this.lEmo.get(this.val$i)).i)).image.SetBitmap(((EmoClass2) Page3.this.lEmo.get(this.val$i)).j, Page3.this.bmImageFailed);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Page3.this.adapter.notifyDataSetChanged();
            Page3.this.loadEmo(this.val$i + 1);
        }

        public void onLoadingStarted(String str, View view) {
        }
    }

    private class DownImageAttach extends AsyncTask<Integer, Void, String> {
        private DownImageAttach() {
        }

        protected String doInBackground(Integer... numArr) {
            if (!Page3.this.mUser.isLogin()) {
                return null;
            }
            if (Page3.this.ListContains.size() > numArr[0].intValue() && ((Post) Page3.this.ListContains.get(numArr[0].intValue())).image.sizeBitmap() > numArr[1].intValue()) {
                if (!((Post) Page3.this.ListContains.get(numArr[0].intValue())).image.getBitmap(numArr[1].intValue()).equals(Page3.this.bmImageStart)) {
                    return null;
                }
                try {
                    String str = ((Post) Page3.this.ListContains.get(numArr[0].intValue())).image.getStr(numArr[1].intValue());
                    if (!str.contains(Global.URL)) {
                        str = Global.URL + str;
                    }
                    Jsoup.connect(str).cookies(Page3.this.mUser.cookies()).ignoreContentType(true).execute().bodyAsBytes();
                    return "OK";
                } catch (FileNotFoundException e) {
                    FileNotFoundException fileNotFoundException = e;
                    if (Page3.this.ListContains.size() > numArr[0].intValue() && ((Post) Page3.this.ListContains.get(numArr[0].intValue())).image.sizeBitmap() > numArr[1].intValue()) {
                        ((Post) Page3.this.ListContains.get(numArr[0].intValue())).image.SetBitmap(numArr[1].intValue(), Page3.this.bmImageFailed);
                    }
                    fileNotFoundException.printStackTrace();
                } catch (Exception e2) {
                    Exception exception = e2;
                    if (Page3.this.ListContains.size() > numArr[0].intValue() && ((Post) Page3.this.ListContains.get(numArr[0].intValue())).image.sizeBitmap() > numArr[1].intValue()) {
                        ((Post) Page3.this.ListContains.get(numArr[0].intValue())).image.SetBitmap(numArr[1].intValue(), Page3.this.bmImageFailed);
                    }
                    exception.printStackTrace();
                }
            }
            return null;
        }

        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            if (str != null) {
                Page3.this.adapter.notifyDataSetChanged();
            }
        }
    }

    private class EmoClass2 {
        public int i;
        public int j;
        public String url;

        public EmoClass2(String str, int i, int i2) {
            this.i = i;
            this.j = i2;
            this.url = str;
        }
    }

    private class TaskLoadAvatart {
        ImageLoader imageLoader;
        int index;

        public TaskLoadAvatart(int i, ImageLoader imageLoader) {
            this.index = i;
            this.imageLoader = imageLoader;
        }

        public void cancle() {
            this.imageLoader.stop();
        }

        public void execute() {
            this.imageLoader.loadImage(((Post) Page3.this.ListContains.get(this.index)).getUrlAvatar(), Page3.this.mImageLoad.options, new ImageLoadingListener() {
                public void onLoadingCancelled(String str, View view) {
                }

                public void onLoadingComplete(String str, View view, Bitmap bitmap) {
                    if (Page3.this.ListContains.size() > TaskLoadAvatart.this.index) {
                        ((Post) Page3.this.ListContains.get(TaskLoadAvatart.this.index)).setAvatar(bitmap);
                    }
                    Page3.this.adapter.notifyDataSetChanged();
                }

                public void onLoadingFailed(String str, View view, FailReason failReason) {
                }

                public void onLoadingStarted(String str, View view) {
                }
            });
        }
    }

    public Page3() {
        this.iCallFromNotification = 1;
        this.iCallFromNotificationQuote = 1;
        this.imageLoader = ImageLoader.getInstance();
    }

    private void PaserPage3(Element element, Post post, boolean z) {
        if (element != null) {
            for (Node node : element.childNodes()) {
                if (node instanceof Element) {
                    Element first;
                    int length;
                    int length2;
                    if (((Element) node).tagName().equals("div")) {
                        first = ((Element) node).select("div").first();
                        if (first.attr("style").contains("padding")) {
                            post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                        }
                        if (first.ownText().contains("Originally Posted by")) {
                            post.addText("Originally Posted by ");
                            length = post.getText().length();
                            post.addText(first.select("strong").text());
                            length2 = post.getText().length();
                            post.web.add(Global.URL + first.select("a").attr("href"), length, length2);
                            post.type.add(BuildConfig.FLAVOR, length, length2, 1);
                            post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                        } else {
                            PaserPage3(first, post, z);
                            post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                        }
                    } else if (((Element) node).tagName().equals("blockquote")) {
                        first = ((Element) node).select("blockquote").first();
                        post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                        PaserPage3(first, post, z);
                        post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                    } else if (((Element) node).tagName().equals("fieldset")) {
                        first = ((Element) node).select("fieldset").first();
                        post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                        PaserPage3(first, post, z);
                        post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                    } else if (((Element) node).tagName().equals("b")) {
                        first = ((Element) node).select("b").first();
                        length = post.getText().length();
                        PaserPage3(first, post, z);
                        post.type.add(BuildConfig.FLAVOR, length, post.getText().length(), 1);
                    } else if (((Element) node).tagName().equals("i")) {
                        first = ((Element) node).select("i").first();
                        length = post.getText().length();
                        PaserPage3(first, post, z);
                        post.type.add(BuildConfig.FLAVOR, length, post.getText().length(), 2);
                    } else if (((Element) node).tagName().equals("pre")) {
                        first = ((Element) node).select("pre").first();
                        length = post.getText().length();
                        PaserPage3(first, post, z);
                        post.quote.add(BuildConfig.FLAVOR, length, post.getText().length());
                    } else if (((Element) node).tagName().equals("table")) {
                        first = ((Element) node).select("table").first();
                        length = post.getText().length();
                        PaserPage3(first, post, z);
                        post.quote.add(BuildConfig.FLAVOR, length, post.getText().length());
                    } else if (((Element) node).tagName().equals("ol")) {
                        PaserPage3(((Element) node).select("ol").first(), post, z);
                    } else if (((Element) node).tagName().equals("tbody")) {
                        PaserPage3(((Element) node).select("tbody").first(), post, z);
                    } else if (((Element) node).tagName().equals("li")) {
                        PaserPage3(((Element) node).select("li").first(), post, z);
                    } else if (((Element) node).tagName().equals("tr")) {
                        first = ((Element) node).select("tr").first();
                        post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                        PaserPage3(first, post, z);
                    } else if (((Element) node).tagName().equals("td")) {
                        PaserPage3(((Element) node).select("td").first(), post, z);
                    } else if (((Element) node).tagName().equals("img")) {
                        String r0;
                        r0 = ((Element) node).select("img[src]").attr("src");
                        if (r0.contains(Global.URL) && r0.subSequence(0, 21).equals(Global.URL) && !r0.contains("https://vozforums.com/attachment.php?attachmentid") && !r0.contains("https://vozforums.com/customavatars/")) {
                            r0 = r0.substring(21);
                        }
                        if (r0.substring(0, 1).equals("/")) {
                            r0 = r0.substring(1, r0.length());
                        }
                        length = post.getText().length();
                        length2 = r0.length();
                        if (r0.contains("http://") || r0.contains("https://") || r0.contains("attachment.php?attachmentid")) {
                            post.image.add(r0, length, length2 + length, this.bmImageStart);
                        } else {
                            post.image.add(r0, length, length + 2, null);
                            r0 = "  ";
                        }
                        if (!r0.contains("images/buttons/viewpost.gif")) {
                            post.addText(r0);
                            if (node.hasAttr("onload")) {
                                post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                            }
                        }
                    } else if (((Element) node).tagName().equals("br")) {
                        post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                    } else if (((Element) node).tagName().equals("u")) {
                        first = ((Element) node).select("u").first();
                        length = post.getText().length();
                        PaserPage3(first, post, z);
                        post.typeU.add(BuildConfig.FLAVOR, length, post.getText().length());
                    } else if (((Element) node).tagName().equals("font")) {
                        Element first2 = ((Element) node).select("font").first();
                        String str = "while";
                        String r1 = "3";
                        if (((Element) node).select("font[color]").first() != null) {
                            str = ((Element) node).select("font[color]").attr("color");
                        }
                        String attr = ((Element) node).select("font[size]").first() != null ? ((Element) node).select("font[size]").attr("size") : r1;
                        length2 = post.getText().length();
                        PaserPage3(first2, post, z);
                        post.font.add(BuildConfig.FLAVOR, length2, post.getText().length(), str, Integer.parseInt(attr));
                    } else if (((Element) node).tagName().equals("a")) {
                        first = ((Element) node).select("a[href]").first();
                        if (first.select("img").first() == null) {
                            String r0;
                            r0 = ((Element) node).select("a[href]").attr("href").replace("%3A", ":").replace("%2F", "/").replace("%3F", "?").replace("%3D", "=").replace("%26", "&");
                            if (r0.contains("mailto:")) {
                                String r1 = r0.substring(7, r0.length());
                                r0 = ((Element) node).select("a[href]").text();
                                length2 = post.getText().length();
                                post.web.add(r1, length2, r0.length() + length2);
                                post.addText(r0);
                            } else if (r0.contains("http")) {
                                String r1 = r0.substring(r0.indexOf("http"), r0.length());
                                r0 = ((Element) node).select("a[href]").text();
                                length2 = post.getText().length();
                                post.web.add(r1, length2, r0.length() + length2);
                                post.addText(r0);
                            }
                        } else {
                            PaserPage3(first, post, true);
                        }
                    } else {
                        post.addText(((Element) node).text());
                    }
                }
                if (node instanceof TextNode) {
                    if (z) {
                        post.addText(((TextNode) node).getWholeText());
                    } else {
                        post.addText(((TextNode) node).text());
                    }
                }
            }
            return;
        }
        post.addText(element.text());
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
        button.setOnClickListener(new AnonymousClass5(editText, dialog));
        button2.setOnClickListener(new AnonymousClass6(imageView));
        dialog.show();
    }

    private void clearTask() {
        Iterator it = this.mListTaskImageAttach.iterator();
        while (it.hasNext()) {
            DownImageAttach downImageAttach = (DownImageAttach) it.next();
            if (!(downImageAttach == null || downImageAttach.isCancelled())) {
                downImageAttach.cancel(true);
            }
        }
        it = this.mListTaskDownAvatart.iterator();
        while (it.hasNext()) {
            TaskLoadAvatart taskLoadAvatart = (TaskLoadAvatart) it.next();
            if (taskLoadAvatart != null) {
                taskLoadAvatart.cancle();
            }
        }
        this.mListTaskImageAttach = new ArrayList();
        this.mListTaskDownAvatart = new ArrayList();
    }

    private void clickMultiQuote(int i) {
        if (this.mUser.isLogin()) {
            String replace;
            if (((Post) this.ListContains.get(i)).isMultiQuote()) {
                ((Post) this.ListContains.get(i)).setMultiQuote(false);
                replace = ((String) this.mUser.cookies().get("vbulletin_multiquote")).replace(((Post) this.ListContains.get(i)).Id(), BuildConfig.FLAVOR);
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
                replace = !this.mUser.cookies().containsKey("vbulletin_multiquote") ? ((Post) this.ListContains.get(i)).Id() : ((String) this.mUser.cookies().get("vbulletin_multiquote")) + "%2C" + ((Post) this.ListContains.get(i)).Id();
            }
            this.mUser.addMultiquote(replace);
            this.adapter.notifyDataSetChanged();
        }
    }

    private void downloadAttach() {
        int size = this.ListContains.size();
        for (int i = 0; i < size; i++) {
            Post post = (Post) this.ListContains.get(i);
            int sizeBitmap = post.image.sizeBitmap();
            for (int i2 = 0; i2 < sizeBitmap; i2++) {
                if (post.image.getStr(i2).contains("attachment.php")) {
                    this.mListTaskImageAttach.add(new DownImageAttach());
                    ((DownImageAttach) this.mListTaskImageAttach.get(this.mListTaskImageAttach.size() - 1)).execute(new Integer[]{Integer.valueOf(i), Integer.valueOf(i2)});
                }
            }
        }
    }

    private void getImageCaptcha(ImageView imageView) {
        this.myNetwork.getDoc(this.linkapicaptcha, new AnonymousClass4(imageView));
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

    private void loadAvatarUniversal(int i) {
        try {
            if (i < this.ListContains.size()) {
                this.mImageLoad.imageLoader.loadImage(((Post) this.ListContains.get(i)).getUrlAvatar(), this.mImageLoad.options, new AnonymousClass10(i));
            }
        } catch (Exception e) {
            loadAvatarUniversal(i + 1);
            e.printStackTrace();
        }
    }

    private void loadEmo(int i) {
        try {
            if (i < this.lEmo.size()) {
                this.mImageLoad.imageLoader.loadImage(getlinkBitmapAssert(((EmoClass2) this.lEmo.get(i)).url), this.mImageLoad.options, new LoadEmojListener(i));
            }
        } catch (Exception e) {
            loadEmo(i + 1);
            e.printStackTrace();
        }
    }

    private void loadIamge() {
        try {
            if (this.indexI < this.ListContains.size()) {
                if (this.indexJ >= ((Post) this.ListContains.get(this.indexI)).image.getSize()) {
                    this.indexI++;
                    this.indexJ = 0;
                    loadIamge();
                }
                String str = ((Post) this.ListContains.get(this.indexI)).image.getStr(this.indexJ);
                if (!str.contains("http://") && !str.contains("https://")) {
                    this.indexJ++;
                    loadIamge();
                } else if (str.contains("attachmentid")) {
                    this.indexJ++;
                    loadIamge();
                } else if (Global.iSizeImage == 0) {
                    this.indexJ++;
                    loadIamge();
                } else if (((Post) this.ListContains.get(this.indexI)).image.getBitmap(this.indexJ).equals(this.bmImageStart)) {
                    this.imageLoader.loadImage(str, new ImageLoadingListener() {
                        public void onLoadingCancelled(String str, View view) {
                            ((Post) Page3.this.ListContains.get(Page3.this.indexI)).image.SetBitmap(Page3.this.indexJ, Page3.this.bmImageFailed);
                            Page3.this.adapter.notifyDataSetChanged();
                            Page3 page3 = Page3.this;
                            page3.indexJ++;
                            Page3.this.loadIamge();
                        }

                        public void onLoadingComplete(String str, View view, Bitmap bitmap) {
                            Page3 page3;
                            if (bitmap != null) {
                                try {
                                    ((Post) Page3.this.ListContains.get(Page3.this.indexI)).image.SetBitmap(Page3.this.indexJ, bitmap);
                                } catch (Exception e) {
                                    Exception exception = e;
                                    ((Post) Page3.this.ListContains.get(Page3.this.indexI)).image.SetBitmap(Page3.this.indexJ, Page3.this.bmImageFailed);
                                    Page3.this.adapter.notifyDataSetChanged();
                                    page3 = Page3.this;
                                    page3.indexJ++;
                                    Page3.this.loadIamge();
                                    exception.printStackTrace();
                                    return;
                                }
                            }
                            Page3.this.adapter.notifyDataSetChanged();
                            page3 = Page3.this;
                            page3.indexJ++;
                            Page3.this.loadIamge();
                        }

                        public void onLoadingFailed(String str, View view, FailReason failReason) {
                            ((Post) Page3.this.ListContains.get(Page3.this.indexI)).image.SetBitmap(Page3.this.indexJ, Page3.this.bmImageFailed);
                            Page3.this.adapter.notifyDataSetChanged();
                            Page3 page3 = Page3.this;
                            page3.indexJ++;
                            Page3.this.loadIamge();
                        }

                        public void onLoadingStarted(String str, View view) {
                        }
                    });
                } else {
                    this.indexJ++;
                    loadIamge();
                }
            }
        } catch (Exception e) {
            this.indexJ++;
            loadIamge();
            e.printStackTrace();
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
        this.mQuickAction.setOnActionItemClickListener(new AnonymousClass3(i));
    }

    protected void GoPage(int i, int i2) throws Exception {
        clearTask();
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
                Post post;
                int size;
                this.ListContains.clear();
                Element element = this.doc.select("div[align=center]").size() > 1 ? (Element) this.doc.select("div[align=center]").get(1) : this.doc;
                Element r0 = element.select("a[href=private.php]").first();
                this.mPostId = getPostIdFromUrl(this.mParser.getUrl());
                setPage(parsePage(0, 0));
                if (r0 != null) {
                    this.mUser.setUserId(((Element) element.select("td[class=alt2]").get(0)).select("a[href*=mem]").attr("href").split("=")[1]);
                    r0 = element.select("input[name*=securitytoken]").first();
                    if (r0 != null) {
                        this.mUser.setToken(r0.attr("value"));
                    }
                    r0 = this.doc.select("div[style=margin-top:6px]:has(input[value=Post Quick Reply])").first();
                    if (r0 != null) {
                        this.sIdThread = r0.select("input[name=t]").attr("value");
                    }
                    r0 = this.doc.select("td[class=alt1]:has(a[href *= subscription.php]").first();
                    if (r0 != null) {
                        if (r0.select("a[href *= subscription.php]").attr("href").contains("removesubscription")) {
                            this.isSubscribe = true;
                            ((Item) this.mListSideMenu2.get(3)).mTitle = getResources().getString(R.string.UnSubscribe);
                            this.mAdapterSideMenu2.notifyDataSetInvalidated();
                        } else {
                            ((Item) this.mListSideMenu2.get(3)).mTitle = getResources().getString(R.string.Subscribe);
                            this.mAdapterSideMenu2.notifyDataSetInvalidated();
                            this.isSubscribe = false;
                        }
                    }
                }
                Elements select = element.select("td[id*=td_post]");
                Iterator it = select.iterator();
                this.iPositiion = 0;
                String str2 = null;
                while (it.hasNext()) {
                    String text;
                    String str3 = null;
                    post = new Post();
                    String str4 = BuildConfig.FLAVOR;
                    Element element2 = (Element) it.next();
                    Element parent = element2.parent();
                    if (select.select("div[class=smallfont]:has(strong)").first() != null) {
                        this.mTextTitle = select.select("div[class=smallfont]:has(strong)").first().text();
                        setTitle(this.mTextTitle);
                    }
                    Element previousElementSibling = parent.previousElementSibling();
                    Element previousElementSibling2 = previousElementSibling.previousElementSibling();
                    if (previousElementSibling.select("img[src*=avatars]").first() != null) {
                        str3 = previousElementSibling.select("img[src*=avatars]").attr("src");
                        if (!str3.contains(Global.URL)) {
                            str3 = Global.URL + str3;
                        }
                    }
                    if (previousElementSibling.select("div:containsOwn(Join Date)").first() != null) {
                        text = previousElementSibling.select("div:containsOwn(Join Date)").first().text();
                        if (text.contains("Date:")) {
                            text = text.split("Date:")[1];
                        }
                        post.setJD("Jd:" + text);
                    } else {
                        post.setJD(BuildConfig.FLAVOR);
                    }
                    if (previousElementSibling.select("div:containsOwn(Posts: )").first() != null) {
                        post.setPosts(previousElementSibling.select("div:containsOwn(Posts: )").first().text());
                    } else {
                        post.setPosts(BuildConfig.FLAVOR);
                    }
                    if (previousElementSibling.select("img[src*=line.gif").first() != null) {
                        if (previousElementSibling.select("img[src*=line.gif").attr("src").contains("online")) {
                            post.isOnline = true;
                        } else {
                            post.isOnline = false;
                        }
                    }
                    if (previousElementSibling.select("a[class=bigusername]").first() != null) {
                        text = previousElementSibling.select("a[class=bigusername]").text();
                        post.m_UserId = previousElementSibling.select("a[class=bigusername]").first().attr("href").split("u=")[1];
                    } else {
                        text = str2;
                    }
                    str2 = previousElementSibling.select("div[class=smallfont]").first() != null ? previousElementSibling.select("div[class=smallfont]").first().text() : str4;
                    str4 = previousElementSibling2.text();
                    previousElementSibling = element2.select("div[id*=post_message").first();
                    if (previousElementSibling != null) {
                        if (previousElementSibling.attr("id").split("_").length > 2) {
                            post.setId(previousElementSibling.attr("id").split("_")[2]);
                        }
                        PaserPage3(previousElementSibling, post, false);
                    }
                    previousElementSibling = element2.select("fieldset[class=fieldset]").first();
                    if (previousElementSibling != null) {
                        post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                        PaserPage3(previousElementSibling, post, false);
                    }
                    post.Info(text, str2, str4, null, str3);
                    if (Global.bSign && element2.select("div:contains(_______)").first() != null) {
                        post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                        PaserPage3(element2.select("div:contains(_______)").first(), post, false);
                    }
                    this.ListContains.add(post);
                    if (post.Id().equals(this.mPostId)) {
                        this.iPositiion = this.ListContains.indexOf(post) + 1;
                        str2 = text;
                    } else {
                        str2 = text;
                    }
                }
                this.lImage = new ArrayList();
                Iterator it2 = this.ListContains.iterator();
                while (it2.hasNext()) {
                    post = (Post) it2.next();
                    size = post.image.getSize();
                    for (int i2 = 0; i2 < size; i2++) {
                        String str5 = post.image.getStr(i2);
                        if (str5.contains("http://") || str5.contains("https://")) {
                            this.lImage.add(str5);
                        }
                    }
                }
                loadAvatarUniversal(0);
                downloadAttach();
                size = this.ListContains.size();
                this.lEmo = new ArrayList();
                for (int i3 = 0; i3 < size; i3++) {
                    Post curPost = this.ListContains.get(i3);
                    int emoSize = curPost.image.getSize();
                    for (int j = 0; j < emoSize; j++) {
                        if (curPost.image.getStr(j).contains("images/smilies")) {
                            this.lEmo.add(new EmoClass2(curPost.image.getStr(j), i3, j));
                        }
                    }
                }
                loadEmo(0);
                this.mItemCount = 23;
                if (this.mItemOffsetY == null) {
                    this.mItemOffsetY = new int[(this.mItemCount + 1)];
                    this.mItemtemp = new int[(this.mItemCount + 1)];
                }
                this.scrollIsComputed = true;
                this.adapter.notifyDataSetChanged();
                if (this.iPositiion != 0) {
                    this.mList.clearFocus();
                    this.mList.requestFocusFromTouch();
                    this.mList.post(new Runnable() {
                        public void run() {
                            Page3.this.mList.setSelection(Page3.this.iPositiion);
                            Page3.this.mList.clearFocus();
                        }
                    });
                }
            }
        }
    }

    protected void TaskCancle() {
        super.TaskCancle();
        clearTask();
        this.ListContains.clear();
        this.adapter.notifyDataSetChanged();
    }

    public Bitmap getResizedBitmapAvatart(Bitmap bitmap) {
        bitmap.getWidth();
        bitmap.getHeight();
        return bitmap;
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
    }

    protected void onCreate(Bundle bundle) {
        this.iPage = 3;
        super.onCreate(bundle);
        this.ListContains = new ArrayList();
        this.mListTaskImageAttach = new ArrayList();
        this.mListTaskDownAvatart = new ArrayList();
        this.bmImageStart = BitmapFactory.decodeResource(getResources(), R.drawable.stub_image);
        this.bmImageFailed = BitmapFactory.decodeResource(getResources(), R.drawable.image_for_empty_url);
        this.adapter = new Page3ListViewAdapter(this.mContext, this.ListContains, this.mImageLoad.imageLoader, this.bmImageStart, this.mTextSize);
        this.mObjectAdapter = this.adapter;
        this.adapter.setSize(this.mTextSize);
        this.mList.setAdapter(this.adapter);
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
        this.mTask.execute(new Integer[]{Integer.valueOf(0)});
        this.mList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            }
        });
        hideAds();
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
        clearTask();
        super.onStop();
    }

    protected void updateSetFont(float f) {
        super.updateSetFont(f);
        this.adapter.setSize(f);
        this.adapter.notifyDataSetChanged();
    }
}
