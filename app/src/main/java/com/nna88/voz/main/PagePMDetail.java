package com.nna88.voz.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.nna88.voz.contain.Post;
import com.nna88.voz.listview.listViewCustom3;
import com.nna88.voz.mysqlite.MySQLiteHelper;
import com.nna88.voz.quickaction.ActionItem;
import com.nna88.voz.quickaction.QuickAction;
import com.nna88.voz.quickaction.QuickAction.OnActionItemClickListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class PagePMDetail extends Page {
    private ArrayList<Post> ListContains;
    private listViewCustom3 adapter;
    private int iCallFromNotification;
    private int iCallFromNotificationQuote;
    private int iPostType;
    private String mPostId;
    private QuickAction mQuickAction;
    private String mTextTitle;
    private String url;

    /* renamed from: com.nna88.voz.main.PagePMDetail.3 */
    class AnonymousClass3 implements Runnable {
        final /* synthetic */ Post val$post;

        AnonymousClass3(Post post) {
            this.val$post = post;
        }

        public void run() {
            PagePMDetail.this.mList.setSelection(PagePMDetail.this.ListContains.indexOf(this.val$post));
            PagePMDetail.this.mList.smoothScrollToPosition(PagePMDetail.this.ListContains.indexOf(this.val$post));
        }
    }

    private class DownAvatar extends AsyncTask<Integer, Void, String> {
        private DownAvatar() {
        }

        protected String doInBackground(Integer... numArr) {
            if (((Post) PagePMDetail.this.ListContains.get(numArr[0].intValue())).Avatar() != null) {
                return null;
            }
            String UrlAvatar = ((Post) PagePMDetail.this.ListContains.get(numArr[0].intValue())).UrlAvatar();
            if (UrlAvatar == null) {
                return null;
            }
            try {
                byte[] bodyAsBytes = Jsoup.connect(UrlAvatar).ignoreContentType(true).execute().bodyAsBytes();
                ((Post) PagePMDetail.this.ListContains.get(numArr[0].intValue())).setAvatar(BitmapFactory.decodeByteArray(bodyAsBytes, 0, bodyAsBytes.length));
                return "OK";
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            if (str != null) {
                PagePMDetail.this.adapter.notifyDataSetChanged();
            }
        }
    }

    private class DownImageAttach extends AsyncTask<Integer, Void, String> {
        private DownImageAttach() {
        }

        protected String doInBackground(Integer... numArr) {
            if (!PagePMDetail.this.mUser.isLogin()) {
                return null;
            }
            if (((Post) PagePMDetail.this.ListContains.get(numArr[0].intValue())).image.getBitmap(numArr[1].intValue()) != null) {
                return null;
            }
            try {
                String str = ((Post) PagePMDetail.this.ListContains.get(numArr[0].intValue())).image.getStr(numArr[1].intValue());
                if (!str.contains(Global.URL)) {
                    str = Global.URL + str;
                }
                byte[] bodyAsBytes = Jsoup.connect(str).cookies(PagePMDetail.this.mUser.cookies()).ignoreContentType(true).execute().bodyAsBytes();
                ((Post) PagePMDetail.this.ListContains.get(numArr[0].intValue())).image.SetBitmap(numArr[1].intValue(), BitmapFactory.decodeByteArray(bodyAsBytes, 0, bodyAsBytes.length));
                return "OK";
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            if (str != null) {
                PagePMDetail.this.adapter.notifyDataSetChanged();
            }
        }
    }

    private class TaskLoadAvatart {
        int index;

        public TaskLoadAvatart(int i) {
            this.index = i;
        }

        public void execute() {
            PagePMDetail.this.mImageLoad.imageLoader.loadImage(((Post) PagePMDetail.this.ListContains.get(this.index)).UrlAvatar(), PagePMDetail.this.mImageLoad.options, new ImageLoadingListener() {
                public void onLoadingCancelled(String str, View view) {
                }

                public void onLoadingComplete(String str, View view, Bitmap bitmap) {
                    ((Post) PagePMDetail.this.ListContains.get(TaskLoadAvatart.this.index)).setAvatar(bitmap);
                    PagePMDetail.this.adapter.notifyDataSetChanged();
                }

                public void onLoadingFailed(String str, View view, FailReason failReason) {
                }

                public void onLoadingStarted(String str, View view) {
                }
            });
        }
    }

    public PagePMDetail() {
        this.iCallFromNotification = 1;
        this.iCallFromNotificationQuote = 1;
    }

    private void PaserPage3(Element element, Post post, boolean z, boolean z2) {
//        if (element != null) {
//            for (Node node : element.childNodes()) {
//                if (node instanceof Element) {
//
//                    if (((Element) node).tagName().equals("div")) {
//                        PaserPage3(((Element) node).select("div").first(), post, false, z2);
//                        post.addText(IOUtils.LINE_SEPARATOR_UNIX);
//                    } else if (((Element) node).tagName().equals("blockquote")) {
//                        r0 = ((Element) node).select("blockquote").first();
//                        post.addText(IOUtils.LINE_SEPARATOR_UNIX);
//                        PaserPage3(r0, post, false, z2);
//                        post.addText(IOUtils.LINE_SEPARATOR_UNIX);
//                    } else if (((Element) node).tagName().equals("fieldset")) {
//                        r0 = ((Element) node).select("fieldset").first();
//                        post.addText(IOUtils.LINE_SEPARATOR_UNIX);
//                        PaserPage3(r0, post, false, z2);
//                        post.addText(IOUtils.LINE_SEPARATOR_UNIX);
//                    } else if (((Element) node).tagName().equals("b")) {
//                        r0 = ((Element) node).select("b").first();
//                        r1 = post.getText().length();
//                        PaserPage3(r0, post, false, z2);
//                        post.type.add(BuildConfig.FLAVOR, r1, post.getText().length(), 1);
//                    } else if (((Element) node).tagName().equals("i")) {
//                        r0 = ((Element) node).select("i").first();
//                        r1 = post.getText().length();
//                        PaserPage3(r0, post, false, z2);
//                        post.type.add(BuildConfig.FLAVOR, r1, post.getText().length(), 2);
//                    } else if (((Element) node).tagName().equals("pre")) {
//                        r0 = ((Element) node).select("pre").first();
//                        r1 = post.getText().length();
//                        PaserPage3(r0, post, false, z2);
//                        post.quote.add(BuildConfig.FLAVOR, r1, post.getText().length());
//                    } else if (((Element) node).tagName().equals("table")) {
//                        r0 = ((Element) node).select("table").first();
//                        r1 = post.getText().length();
//                        PaserPage3(r0, post, false, z2);
//                        post.quote.add(BuildConfig.FLAVOR, r1, post.getText().length());
//                    } else if (((Element) node).tagName().equals("ol")) {
//                        PaserPage3(((Element) node).select("ol").first(), post, false, z2);
//                    } else if (((Element) node).tagName().equals("tbody")) {
//                        PaserPage3(((Element) node).select("tbody").first(), post, false, z2);
//                    } else if (((Element) node).tagName().equals("li")) {
//                        PaserPage3(((Element) node).select("li").first(), post, false, z2);
//                    } else if (((Element) node).tagName().equals("tr")) {
//                        r0 = ((Element) node).select("tr").first();
//                        post.addText(IOUtils.LINE_SEPARATOR_UNIX);
//                        PaserPage3(r0, post, false, z2);
//                    } else if (((Element) node).tagName().equals("td")) {
//                        PaserPage3(((Element) node).select("td").first(), post, false, z2);
//                    } else if (((Element) node).tagName().equals("img")) {
//                        r0 = ((Element) node).select("img[src]").attr("src");
//                        if (r0.contains(Global.URL) && r0.subSequence(0, 21).equals(Global.URL) && !r0.contains("https://vozforums.com/attachment.php?attachmentid") && !r0.contains("https://vozforums.com/customavatars/")) {
//                            r0 = r0.substring(21);
//                        }
//                        if (r0.substring(0, 1).equals("/")) {
//                            r0 = r0.substring(1, r0.length());
//                        }
//                        r1 = post.getText().length();
//                        r2 = r0.length() + r1;
//                        if (r0.contains("http://") || r0.contains("https://") || r0.contains("attachment.php?attachmentid")) {
//                            post.image.add(r0, r1, r2, null);
//                        } else {
//                            post.image.add(r0, r1, r2, loadBitmapAssert(r0));
//                        }
//                        post.addText(r0);
//                        if (((Element) node).hasAttr("onload")) {
//                            post.addText(IOUtils.LINE_SEPARATOR_UNIX);
//                        }
//                    } else if (((Element) node).tagName().equals("br")) {
//                        post.addText(IOUtils.LINE_SEPARATOR_UNIX);
//                    } else if (((Element) node).tagName().equals("u")) {
//                        r0 = ((Element) node).select("u").first();
//                        r1 = post.getText().length();
//                        PaserPage3(r0, post, false, z2);
//                        post.typeU.add(BuildConfig.FLAVOR, r1, post.getText().length());
//                    } else if (((Element) node).tagName().equals("font")) {
//                        Element first = ((Element) node).select("font").first();
//                        String str = "while";
//                        r1 = "3";
//                        if (((Element) node).select("font[color]").first() != null) {
//                            str = ((Element) node).select("font[color]").attr("color");
//                        }
//                        String attr = ((Element) node).select("font[size]").first() != null ? ((Element) node).select("font[size]").attr("size") : r1;
//                        r2 = post.getText().length();
//                        PaserPage3(first, post, false, z2);
//                        post.font.add(BuildConfig.FLAVOR, r2, post.getText().length(), str, Integer.parseInt(attr));
//                    } else if (((Element) node).tagName().equals("a")) {
//                        r0 = ((Element) node).select("a[href]").first();
//                        if (r0.select("img").first() == null) {
//                            r0 = ((Element) node).select("a[href]").attr("href").replace("%3A", ":").replace("%2F", "/").replace("%3F", "?").replace("%3D", "=").replace("%26", "&");
//                            if (r0.contains("mailto:")) {
//                                r1 = r0.substring(7, r0.length());
//                                r0 = ((Element) node).select("a[href]").text();
//                                r2 = post.getText().length();
//                                post.web.add(r1, r2, r0.length() + r2);
//                                post.addText(r0);
//                            } else if (r0.contains("http")) {
//                                r1 = r0.substring(r0.indexOf("http"), r0.length());
//                                r0 = ((Element) node).select("a[href]").text();
//                                r2 = post.getText().length();
//                                post.web.add(r1, r2, r0.length() + r2);
//                                post.addText(r0);
//                            }
//                        } else {
//                            PaserPage3(r0, post, false, true);
//                        }
//                    } else {
//                        post.addText(((Element) node).text());
//                    }
//                }
//                if (node instanceof TextNode) {
//                    if (z2) {
//                        post.addText(((TextNode) node).getWholeText());
//                    } else {
//                        post.addText(((TextNode) node).text());
//                    }
//                }
//            }
//            return;
//        }
        post.addText(element.text());
    }

    private void downloadAttach() {
        int size = this.ListContains.size();
        for (int i = 0; i < size; i++) {
            Post post = (Post) this.ListContains.get(i);
            int sizeBitmap = post.image.sizeBitmap();
            for (int i2 = 0; i2 < sizeBitmap; i2++) {
                if (post.image.getStr(i2).contains("attachment.php")) {
                    new DownImageAttach().execute(new Integer[]{Integer.valueOf(i), Integer.valueOf(i2)});
                }
            }
        }
    }

    private void downloadAvatart() {
        int size = this.ListContains.size();
        for (int i = 0; i < size; i++) {
            new TaskLoadAvatart(i).execute();
        }
    }

    private String getPostIdFromUrl(String str) {
        String str2 = null;
        try {
            log(str);
            if (str.contains("p=")) {
                str2 = str.split("p=")[1];
            }
            if (str2.contains("#post")) {
                str2 = str2.split("#post")[1];
            }
            log(str2);
        } catch (Exception e) {
        }
        return str2;
    }

    void ActionPopup(View view, int i) {
        ActionItem actionItem = new ActionItem(1, "Reply", getResources().getDrawable(R.drawable.tapatalk_bubble_reply));
        this.mQuickAction = new QuickAction(this.mContext);
        this.mQuickAction.addActionItem(actionItem);
        this.mQuickAction.show(view);
        this.mQuickAction.setOnActionItemClickListener(new OnActionItemClickListener() {
            public void onItemClick(QuickAction quickAction, int i, int i2) {
                String attr;
                String str;
                String UserId;
                String str2 = null;
                Intent intent = new Intent(PagePMDetail.this.mContext, PageNewThread.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POSTTYPE", 6);
                if (PagePMDetail.this.doc != null) {
                    if (PagePMDetail.this.doc.select("textarea[name=message]").first() != null) {
//                        bundle.putString(NotificationCompatApi21.CATEGORY_MESSAGE, PagePMDetail.this.doc.select("textarea[name=message]").first().text() + IOUtils.LINE_SEPARATOR_UNIX);
                    }
                    attr = PagePMDetail.this.doc.select("input[name=securitytoken]").attr("value");
                    str = PagePMDetail.this.url.split("id=")[1];

//                  todo fix  PagePMDetail.this.mTextTitle;
                    UserId = PagePMDetail.this.mUser.UserId();
                    String str3 = attr;
                    attr = ((Post) PagePMDetail.this.ListContains.get(0)).User() + " ;";
                    str2 = str3;
                } else {
                    attr = null;
                    UserId = null;
                    str = null;
                }
                bundle.putString(MySQLiteHelper.COLUMN_TITLE, PagePMDetail.this.mTextTitle);
                bundle.putString("TOKEN", str2);
                bundle.putString("MID", str);
                bundle.putString("USERID", UserId);
                bundle.putString("RECIPLIES", attr);
                intent.putExtras(bundle);
                PagePMDetail.this.startActivityForResult(intent, 6);
            }
        });
    }

    void Parser(int i) throws Exception {
        super.Parser(i);
        if (this.doc != null) {
            Element first;
            Post post;
            this.ListContains.clear();
            Element element = (Element) this.doc.select("div[align=center]").get(1);
            if (element.select("a[href=private.php]").first() != null) {
                this.mUser.setUserId(((Element) element.select("td[class=alt2]").get(0)).select("a[href*=mem]").attr("href").split("=")[1]);
                first = element.select("input[name*=securitytoken]").first();
                if (first != null) {
                    this.mUser.setToken(first.attr("value"));
                }
            }
            Elements select = element.select("td[id*=td_post]");
            Iterator it = select.iterator();
            String str = null;
            while (it.hasNext()) {
                String text;
                String str2 = null;
                post = new Post();
                Element element2 = (Element) it.next();
                first = element2.parent();
                if (select.select("div[class=smallfont]:has(strong)").first() != null) {
                    this.mTextTitle = select.select("div[class=smallfont]:has(strong)").first().text();
                }
                Element previousElementSibling = first.previousElementSibling();
                Element previousElementSibling2 = previousElementSibling.previousElementSibling();
                if (previousElementSibling.select("img[src*=avatars]").first() != null) {
                    str2 = previousElementSibling.select("img[src*=avatars]").attr("src");
                    if (!str2.contains(Global.URL)) {
                        str2 = Global.URL + str2;
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
                if (previousElementSibling.select("a[class=bigusername]").first() != null) {
                    text = previousElementSibling.select("a[class=bigusername]").text();
                    post.m_UserId = previousElementSibling.select("a[class=bigusername]").first().attr("href").split("u=")[1];
                } else {
                    text = str;
                }
                str = previousElementSibling2.text();
                previousElementSibling = element2.select("div[id*=post_message").first();
                if (previousElementSibling != null) {
                    if (previousElementSibling.attr("id").split("_").length > 2) {
                        post.setId(previousElementSibling.attr("id").split("_")[2]);
                    }
                    PaserPage3(previousElementSibling, post, true, false);
                }
                element2 = element2.select("fieldset[class=fieldset]").first();
                if (element2 != null) {
                    post.addText(IOUtils.LINE_SEPARATOR_UNIX);
                    PaserPage3(element2, post, true, false);
                }
                post.Info(text, BuildConfig.FLAVOR, str, null, str2);
                this.ListContains.add(post);
                this.adapter.notifyDataSetChanged();
                str = text;
            }
            this.mPostId = getPostIdFromUrl(this.mParser.getUrl());
            Iterator it2 = this.ListContains.iterator();
            while (it2.hasNext()) {
                post = (Post) it2.next();
                if (post.Id() != null && post.Id().equals(this.mPostId)) {
                    try {
                        new Handler().post(new AnonymousClass3(post));
                        break;
                    } catch (Exception e) {
                    }
                }
            }
            downloadAvatart();
            downloadAttach();
            this.mItemCount = 23;
            if (this.mItemOffsetY == null) {
                this.mItemOffsetY = new int[(this.mItemCount + 1)];
                this.mItemtemp = new int[(this.mItemCount + 1)];
            }
            this.scrollIsComputed = true;
        }
    }

    protected void TaskCancle() {
        super.TaskCancle();
        this.ListContains.clear();
        this.adapter.notifyDataSetChanged();
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 6) {
            setResult(-1);
            finish();
        }
    }

    public void onBackPressed() {
        if (this.iCallFromNotification != 1) {
            startActivity(new Intent(this, PageCP.class));
        }
        if (this.iCallFromNotificationQuote != 1) {
            Intent intent = new Intent(this, PageQuote.class);
            intent.putExtra("NUMQUOTE", this.iCallFromNotificationQuote);
            startActivity(intent);
        }
        super.onBackPressed();
    }

    protected void onCreate(Bundle bundle) {
        this.iPage = 11;
        super.onCreate(bundle);
        this.ListContains = new ArrayList();
        this.adapter = new listViewCustom3(this.mContext, this.ListContains, ImageLoader.getInstance(), null, this.mTextSize);
        this.mObjectAdapter = this.adapter;
        this.adapter.setSize(this.mTextSize);
        this.mList.setAdapter(this.adapter);
        this.mTextTitle = getIntent().getStringExtra("TITLE");
        this.url = getIntent().getStringExtra("URL");
        if (this.url.contains(Global.URL) || this.url.contains(Global.URL2)) {
            this.mParser.setUrl(getIntent().getStringExtra("URL"));
        } else {
            this.mParser.setUrl(Global.URL + getIntent().getStringExtra("URL"));
        }
        this.mTask.execute(new Integer[]{Integer.valueOf(0)});
        this.mList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (PagePMDetail.this.mUser.isLogin()) {
                    PagePMDetail.this.ActionPopup(view, i);
                }
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        this.adapter.destroy();
    }

    protected void onRestart() {
        super.onRestart();
        updateSetFont(this.mTextSize);
    }

    protected void updateSetFont(float f) {
        super.updateSetFont(f);
        this.adapter.setSize(f);
        this.adapter.notifyDataSetChanged();
    }
}
