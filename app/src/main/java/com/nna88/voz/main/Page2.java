package com.nna88.voz.main;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;
import com.nna88.voz.contain.Thread;
import com.nna88.voz.listview.listViewCustom2;
import com.nna88.voz.mysqlite.Comment;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Page2 extends Page {
    private ArrayList<Thread> ListContains;
    private listViewCustom2 adapter;
    List<Comment> lComment;
    private String mTextTitle;
    private String url;

    /* renamed from: com.nna88.voz.main.Page2.3 */
    class AnonymousClass3 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ int val$pos;

        AnonymousClass3(int i, Dialog dialog) {
            this.val$pos = i;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            Intent intent = new Intent(Page2.this.mContext, Page2.class);
            intent.putExtra("URL", ((Thread) Page2.this.ListContains.get(this.val$pos)).mPrefixLink);
            intent.putExtra("TITLE", BuildConfig.FLAVOR);
            Page2.this.startActivity(intent);
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.Page2.4 */
    class AnonymousClass4 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ int val$pos;

        AnonymousClass4(int i, Dialog dialog) {
            this.val$pos = i;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            int i = VERSION.SDK_INT;
            String UrlThread = ((Thread) Page2.this.ListContains.get(this.val$pos)).UrlThread();
            CharSequence charSequence;
            if (UrlThread.contains(Global.URL)) {
                charSequence = UrlThread;
            } else {
                charSequence = Global.URL + UrlThread;
            }
            if (i < 11) {
                ((ClipboardManager) Page2.this.getSystemService("clipboard")).setText(charSequence);
            } else {
                ((android.content.ClipboardManager) Page2.this.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("text label", charSequence));
            }
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.Page2.5 */
    class AnonymousClass5 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ int val$pos;

        AnonymousClass5(int i, Dialog dialog) {
            this.val$pos = i;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            Intent intent = new Intent(Page2.this.mContext, Page3.class);
            intent.putExtra("URL", ((Thread) Page2.this.ListContains.get(this.val$pos)).UrlThread());
            intent.putExtra("TITLE", BuildConfig.FLAVOR);
            Page2.this.startActivity(intent);
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.Page2.6 */
    class AnonymousClass6 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ int val$pos;

        AnonymousClass6(int i, Dialog dialog) {
            this.val$pos = i;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            if (((Thread) Page2.this.ListContains.get(this.val$pos)).UrlLastPosst() == null) {
                Page2.this.toast("Kh\u00f4ng co link");
                return;
            }
            Intent intent = new Intent(Page2.this.mContext, Page3.class);
            intent.putExtra("URL", ((Thread) Page2.this.ListContains.get(this.val$pos)).UrlLastPosst());
            intent.putExtra("TITLE", ((Thread) Page2.this.ListContains.get(this.val$pos)).UrlLastPosst());
            Page2.this.startActivity(intent);
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.Page2.7 */
    class AnonymousClass7 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ int val$pos;

        AnonymousClass7(int i, Dialog dialog) {
            this.val$pos = i;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            if (((Thread) Page2.this.ListContains.get(this.val$pos)).getUrlLastLast() == null) {
                Page2.this.toast("Kh\u00f4ng c\u00f3 link");
                return;
            }
            Intent intent = new Intent(Page2.this.mContext, Page3.class);
            intent.putExtra("URL", ((Thread) Page2.this.ListContains.get(this.val$pos)).getUrlLastLast());
            intent.putExtra("TITLE", BuildConfig.FLAVOR);
            Page2.this.startActivity(intent);
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.Page2.8 */
    class AnonymousClass8 implements OnClickListener {
        final /* synthetic */ Thread val$contain;
        final /* synthetic */ Dialog val$dialog;

        AnonymousClass8(Thread thread, Dialog dialog) {
            this.val$contain = thread;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            if (this.val$contain.isBookmark) {
                this.val$contain.isBookmark = false;
                Page2.this.mDataBookmark.deleteBookmark(this.val$contain);
            } else {
                this.val$contain.isBookmark = true;
                Page2.this.mDataBookmark.addBookmark(this.val$contain);
            }
            this.val$dialog.dismiss();
        }
    }

    private void alertLongClick(int i) {
        Dialog dialog = new Dialog(this.mContext);
        dialog.setTitle("Please choose");
        dialog.setContentView(R.layout.pagelongclick);
        TextView textView = (TextView) dialog.findViewById(R.id.firstnew);
        TextView textView2 = (TextView) dialog.findViewById(R.id.firstpost);
        TextView textView3 = (TextView) dialog.findViewById(R.id.lastpost);
        TextView textView4 = (TextView) dialog.findViewById(R.id.bookmark);
        Thread thread = (Thread) this.ListContains.get(i);
        if (thread.isBookmark) {
            textView4.setText(getResources().getString(R.string.UnBookmark));
        } else {
            textView4.setText(getResources().getString(R.string.Bookmark));
        }
        TextView textView5 = (TextView) dialog.findViewById(R.id.copylink);
        TextView textView6 = (TextView) dialog.findViewById(R.id.Prefix);
        if (((Thread) this.ListContains.get(i)).mPrefixLink != null) {
            textView6.setVisibility(0);
        }
        textView6.setOnClickListener(new AnonymousClass3(i, dialog));
        textView5.setOnClickListener(new AnonymousClass4(i, dialog));
        textView2.setOnClickListener(new AnonymousClass5(i, dialog));
        textView.setOnClickListener(new AnonymousClass6(i, dialog));
        textView3.setOnClickListener(new AnonymousClass7(i, dialog));
        textView4.setOnClickListener(new AnonymousClass8(thread, dialog));
        dialog.show();
    }

    private boolean checkBookmark(String str) {
        for (Comment comment : this.lComment) {
            if (comment.idThread.equals(str)) {
                return true;
            }
        }
        return false;
    }

    protected void GoPage(int i, int i2) throws Exception {
        super.GoPage(i, i2);
    }

    void Parser(int i) throws Exception {
        super.Parser(i);
        if (this.doc != null) {
            this.ListContains.clear();
            Element first = this.doc.select("a[href=private.php]").first();
            setPage(parsePage(0, 0));
            if (first != null) {
                this.mUser.setUserId(((Element) this.doc.select("td[class=alt2]").get(0)).select("a[href*=mem]").attr("href").split("=")[1]);
                first = this.doc.select("input[name*=securitytoken]").first();
                if (first != null) {
                    this.mUser.setToken(first.attr("value"));
                }
            }
            first = this.doc.select("tbody:has(form[id])").first();
            if (first == null) {
                toast("main Element null");
                parserNotification();
                return;
            }
            Element first2 = first.select("td[class=tcat]:contains(Threads in Forum)").first();
            if (first2 != null) {
                this.mTitle = first2.select("span").text();
                this.mTitle = this.mTitle.substring(2, this.mTitle.length());
                setTitle(this.mTitle);
            }
            first2 = first.select("tbody[id*=threadbits_forum]").first();
            if (first2 == null) {
                toast("elebody null");
                return;
            }
            Bitmap loadBitmapAssert;
            String attr;
            String text;
            Elements select = first2.select("tr:has(td[id*=td_threads])");
            Elements select2 = first.select("tr > td[class=alt1Active]");
            Iterator it = select.iterator();
            Iterator it2 = select2.iterator();
            if (it2.hasNext()) {
                this.ListContains.add(new Thread("Sub-Forum", null, null, null, null, null, null));
            }
            String str = null;
            String str2 = null;
            Bitmap bitmap = null;
            String str3 = null;
            String str4 = null;
            while (it2.hasNext()) {
                first = (Element) it2.next();
                Element first3 = first.select("td > img[id]").first();
                Element nextElementSibling = first.nextElementSibling();
                Element nextElementSibling2 = nextElementSibling.nextElementSibling();
                Element nextElementSibling3 = nextElementSibling2.nextElementSibling();
                loadBitmapAssert = (!Global.bTopicHeader || first3 == null) ? bitmap : loadBitmapAssert(first3.select("img").attr("src"));
                if (first != null) {
                    str2 = first.text();
                }
                attr = first.select("div > a").first() != null ? first.select("div > a").first().attr("href") : str3;
                if (nextElementSibling.select("span > a").first() != null) {
                    text = nextElementSibling.select("span > a").first().text();
                    str = nextElementSibling.select("span > a").first().attr("href");
                } else {
                    text = str4;
                }
                if (!(nextElementSibling2 == null || nextElementSibling3 == null)) {
                    text = text + "\nReplie:" + nextElementSibling2.text() + " - View:" + nextElementSibling3.text();
                }
                this.ListContains.add(new Thread(str2, text, null, null, loadBitmapAssert, attr, str));
                bitmap = loadBitmapAssert;
                str3 = attr;
                str4 = text;
            }
            this.ListContains.add(new Thread("Thread", null, null, null, null, null, null));
            text = null;
            loadBitmapAssert = bitmap;
            attr = str3;
            String str5 = str4;
            while (it.hasNext()) {
                String attr2;
                str = null;
                str2 = BuildConfig.FLAVOR;
                Element element = (Element) it.next();
                Element first4 = element.select("a[id*=thread_title").first();
                if (first4 != null) {
                    if (element.select("a[href*=prefixid]").first() != null) {
                        str2 = element.select("a[href*=prefixid]").first().text() + "-";
                        attr2 = element.select("a[href*=prefixid]").attr("href");
                    } else {
                        attr2 = text;
                    }
                    str2 = str2 + element.select("a[id*=thread_title").first().text();
                    attr = element.select("a[id*=thread_title").first().attr("href");
                    str3 = attr2;
                } else {
                    str3 = text;
                }
                attr2 = element.select("td[class=alt2]:has(span)").first() != null ? element.select("td[class=alt2]:has(span)").first().text() : str5;
                if (element.select("td[align=center]").first() != null) {
                    attr2 = attr2 + "\nReplies:" + ((Element) element.select("td[align=center]").get(0)).text() + " - Views:" + ((Element) element.select("td[align=center]").get(1)).text();
                }
                text = element.select("div:has(span[onclick*=member.php])").first() != null ? element.select("div:has(span[onclick*=member.php])").first().text() + " - " + attr2 : attr2;
                if (Global.bTopicHeader && element.select("img[id*=thread_statusicon]").first() != null) {
                    loadBitmapAssert = loadBitmapAssert(element.select("img[id*=thread_statusicon]").first().attr("src"));
                }
                if (element.select("a[id*=thread_gotonew]").first() != null) {
                    str = element.select("a[id*=thread_gotonew]").first().attr("href");
                }
                Thread thread = new Thread(str2, text, null, null, loadBitmapAssert, attr, str);
                thread.mPrefixLink = str3;
                thread.mIdThread = attr.split("t=")[1];
                if (element.select("a:has(img[src*=lastpost])").first() != null) {
                    thread.setUrlLastPost(element.select("a:has(img[src*=lastpost])").first().attr("href"));
                }
                if (first4.hasClass("vozsticky")) {
                    thread.setSticky(true);
                } else {
                    thread.setSticky(false);
                }
                if (checkBookmark(thread.mIdThread)) {
                    thread.isBookmark = true;
                    this.mDataBookmark.updateBookmark(thread);
                }
                this.ListContains.add(thread);
                str5 = text;
                text = str3;
            }
            this.mItemCount = this.adapter.getCount();
            if (this.mItemOffsetY == null) {
                this.mItemOffsetY = new int[(this.mItemCount + 1)];
                this.mItemtemp = new int[(this.mItemCount + 1)];
            }
            this.scrollIsComputed = true;
            this.adapter.notifyDataSetChanged();
            this.mList.clearFocus();
            this.mList.post(new Runnable() {
                public void run() {
                    Page2.this.mList.setSelection(0);
                }
            });
        }
    }

    protected void TaskCancle() {
        super.TaskCancle();
        this.ListContains.clear();
        this.adapter.notifyDataSetChanged();
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        this.mParser.setUrl(Global.URL + this.url);
        TaskCancle();
        this.mTask = new TaskGetHtml();
        this.mTask.execute(new Integer[]{Integer.valueOf(0)});
    }

    protected void onCreate(Bundle bundle) {
        this.iPage = 2;
        super.onCreate(bundle);
        this.ListContains = new ArrayList();
        this.adapter = new listViewCustom2(this.mContext, this.ListContains);
        this.mObjectAdapter = this.adapter;
        this.adapter.setSize(this.mTextSize);
        this.mList.setAdapter(this.adapter);
        this.mTextTitle = getIntent().getStringExtra("TITLE");
        setTitle(this.mTextTitle);
        this.url = getIntent().getStringExtra("URL");
        if (this.url.contains(Global.URL) || this.url.contains(Global.URL2)) {
            this.mParser.setUrl(getIntent().getStringExtra("URL"));
        } else {
            this.mParser.setUrl(Global.URL + getIntent().getStringExtra("URL"));
        }
        this.mTask.execute(new Integer[]{Integer.valueOf(0)});
        this.mList.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
                Page2.this.alertLongClick(i);
                Page2.this.vibrate();
                return false;
            }
        });
        this.mList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Page2.this.vibrate();
                try {
                    Intent intent;
                    if (((Thread) Page2.this.ListContains.get(i)).isUrl() == 0) {
                        intent = new Intent(Page2.this.mContext, Page2.class);
                        if (((Thread) Page2.this.ListContains.get(i)).UrlThread() != null) {
                            intent.putExtra("URL", ((Thread) Page2.this.ListContains.get(i)).UrlThread());
                        } else {
                            return;
                        }
                    } else if (((Thread) Page2.this.ListContains.get(i)).isUrl() == 1) {
                        intent = new Intent(Page2.this.mContext, Page3.class);
                        if (((Thread) Page2.this.ListContains.get(i)).UrlLastPosst() != null) {
                            if (((Thread) Page2.this.ListContains.get(i)).UrlLastPosst() != null) {
                                intent.putExtra("URL", ((Thread) Page2.this.ListContains.get(i)).UrlLastPosst());
                            } else {
                                return;
                            }
                        } else if (((Thread) Page2.this.ListContains.get(i)).UrlThread() != null) {
                            intent.putExtra("URL", ((Thread) Page2.this.ListContains.get(i)).UrlThread());
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                    intent.putExtra("TITLE", ((Thread) Page2.this.ListContains.get(i)).Thread());
                    Page2.this.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.lComment = this.mDataBookmark.getAllComments();
        hideAds();
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
