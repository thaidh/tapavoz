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
import com.nna88.voz.mysqlite.MySQLiteHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PageNewPost extends Page {
    private ArrayList<Thread> ListContains;
    private listViewCustom2 adapter;
    List<Comment> lComment;

    /* renamed from: com.nna88.voz.main.PageNewPost.3 */
    class AnonymousClass3 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ int val$pos;

        AnonymousClass3(int i, Dialog dialog) {
            this.val$pos = i;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            Intent intent = new Intent(PageNewPost.this.mContext, Page2.class);
            intent.putExtra("URL", ((Thread) PageNewPost.this.ListContains.get(this.val$pos)).mPrefixLink);
            intent.putExtra("TITLE", BuildConfig.FLAVOR);
            PageNewPost.this.startActivity(intent);
            PageNewPost.this.overridePendingTransition(R.anim.rail, R.anim.rail);
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.PageNewPost.4 */
    class AnonymousClass4 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ int val$pos;

        AnonymousClass4(int i, Dialog dialog) {
            this.val$pos = i;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            int i = VERSION.SDK_INT;
            String UrlThread = ((Thread) PageNewPost.this.ListContains.get(this.val$pos)).UrlThread();
            CharSequence charSequence;
            if (UrlThread.contains(Global.URL)) {
                charSequence = UrlThread;
            } else {
                charSequence = Global.URL + UrlThread;
            }
            if (i < 11) {
                ((ClipboardManager) PageNewPost.this.getSystemService("clipboard")).setText(charSequence);
            } else {
                ((android.content.ClipboardManager) PageNewPost.this.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("text label", charSequence));
            }
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.PageNewPost.5 */
    class AnonymousClass5 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ int val$pos;

        AnonymousClass5(int i, Dialog dialog) {
            this.val$pos = i;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            Intent intent = new Intent(PageNewPost.this.mContext, Page3.class);
            intent.putExtra("URL", ((Thread) PageNewPost.this.ListContains.get(this.val$pos)).UrlThread());
            intent.putExtra("TITLE", BuildConfig.FLAVOR);
            PageNewPost.this.startActivity(intent);
            PageNewPost.this.overridePendingTransition(R.anim.rail, R.anim.rail);
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.PageNewPost.6 */
    class AnonymousClass6 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ int val$pos;

        AnonymousClass6(int i, Dialog dialog) {
            this.val$pos = i;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            if (((Thread) PageNewPost.this.ListContains.get(this.val$pos)).UrlLastPosst() == null) {
                PageNewPost.this.toast("Kh\u00f4ng co link");
                return;
            }
            Intent intent = new Intent(PageNewPost.this.mContext, Page3.class);
            intent.putExtra("URL", ((Thread) PageNewPost.this.ListContains.get(this.val$pos)).UrlLastPosst());
            intent.putExtra("TITLE", ((Thread) PageNewPost.this.ListContains.get(this.val$pos)).UrlLastPosst());
            PageNewPost.this.startActivity(intent);
            PageNewPost.this.overridePendingTransition(R.anim.rail, R.anim.rail);
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.PageNewPost.7 */
    class AnonymousClass7 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ int val$pos;

        AnonymousClass7(int i, Dialog dialog) {
            this.val$pos = i;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            if (((Thread) PageNewPost.this.ListContains.get(this.val$pos)).getUrlLastLast() == null) {
                PageNewPost.this.toast("Kh\u00f4ng c\u00f3 link");
                return;
            }
            Intent intent = new Intent(PageNewPost.this.mContext, Page3.class);
            intent.putExtra("URL", ((Thread) PageNewPost.this.ListContains.get(this.val$pos)).getUrlLastLast());
            intent.putExtra("TITLE", BuildConfig.FLAVOR);
            PageNewPost.this.startActivity(intent);
            PageNewPost.this.overridePendingTransition(R.anim.rail, R.anim.rail);
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.PageNewPost.8 */
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
                PageNewPost.this.mDataBookmark.deleteBookmark(this.val$contain);
            } else {
                this.val$contain.isBookmark = true;
                PageNewPost.this.mDataBookmark.addBookmark(this.val$contain);
            }
            this.val$dialog.dismiss();
        }
    }

    private Dialog alertLongClick(int i) {
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
            textView6.setVisibility(View.GONE);
        }
        textView6.setOnClickListener(new AnonymousClass3(i, dialog));
        textView5.setOnClickListener(new AnonymousClass4(i, dialog));
        textView2.setOnClickListener(new AnonymousClass5(i, dialog));
        textView.setOnClickListener(new AnonymousClass6(i, dialog));
        textView3.setOnClickListener(new AnonymousClass7(i, dialog));
        textView4.setOnClickListener(new AnonymousClass8(thread, dialog));
        dialog.show();
        return dialog;
    }

    void Parser(int i) throws Exception {
        super.Parser(i);
        if (this.doc != null) {
            this.ListContains.clear();
            Iterator it = this.doc.select("tr:has(td[id*=td_threadstatusicon])").iterator();
            setPage(parsePage(0, 0));
            String str = null;
            Bitmap bitmap = null;
            while (it.hasNext()) {
                String str2;
                it.hasNext();
                Element element = (Element) it.next();
                String str3 = BuildConfig.FLAVOR;
                String str4 = BuildConfig.FLAVOR;
                if (element.select("a[id*=thread_title]").first() != null) {
                    str3 = element.select("a[id*=thread_title]").first().text();
                    str = element.select("a[id*=thread_title]").first().attr("href");
                    str2 = str3;
                } else {
                    str2 = str3;
                }
                String attr = element.select("a[id*=thread_gotonew]").first() != null ? element.select("a[id*=thread_gotonew]").first().attr("href") : null;
                Elements select = element.select("a[href*=forumdisplay]");
                if (select.size() > 1) {
                    str4 = "Forums: " + ((Element) select.get(1)).text() + "\n ";
                    str3 = ((Element) select.get(0)).text() + " - " + str2;
                    str2 = str4;
                } else if (select.size() == 1) {
                    String str5 = str2;
                    str2 = "Forums: " + ((Element) select.get(0)).text() + IOUtils.LINE_SEPARATOR_UNIX;
                    str3 = str5;
                } else {
                    str3 = str2;
                    str2 = str4;
                }
                Element first = element.select("div[style*=text-align:right]").first();
                if (first != null) {
                    str2 = str2 + element.select("div:has(span[onclick*=member.php])").first().text() + " - " + first.ownText() + " " + first.select("a[href*=member.php]").text();
                }
                if (element.select("td[title*=Replies]").first() != null) {
                    str2 = str2 + IOUtils.LINE_SEPARATOR_UNIX + element.select("td[title*=Replies]").first().attr(MySQLiteHelper.COLUMN_TITLE);
                }
                if (Global.bTopicHeader && element.select("img[id*=thread_statusicon]").first() != null) {
                    bitmap = loadBitmapAssert(element.select("img[id*=thread_statusicon]").first().attr("src"));
                }
                Thread thread = new Thread(str3, str2, null, null, bitmap, str, attr);
                if (first == null || first.select("a[href*=showthread.php]").first() == null) {
                    thread.setUrlLastPost(BuildConfig.FLAVOR);
                } else {
                    thread.setUrlLastPost(first.select("a[href*=showthread.php]").first().attr("href"));
                }
                thread.setSticky(false);
                if (str.contains("t=")) {
                    thread.mIdThread = str.split("t=")[1];
                }
                this.ListContains.add(thread);
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
                    PageNewPost.this.mList.setSelection(0);
                }
            });
        }
    }

    protected void onCreate(Bundle bundle) {
        this.iPage = 12;
        super.onCreate(bundle);
        this.ListContains = new ArrayList();
        this.adapter = new listViewCustom2(this.mContext, this.ListContains);
        this.adapter.setSize(this.mTextSize);
        this.mObjectAdapter = this.adapter;
        this.mList.setAdapter(this.adapter);
        this.mParser.setUrl("https://vozforums.com/search.php?do=getnew");
        this.mTask.execute(new Integer[]{Integer.valueOf(0)});
        this.mList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Exception e;
                try {
                    Intent intent;
                    PageNewPost.this.vibrate();
                    if (((Thread) PageNewPost.this.ListContains.get(i)).isUrl() == 0) {
                        intent = new Intent(PageNewPost.this.mContext, Page2.class);
                        try {
                            if (((Thread) PageNewPost.this.ListContains.get(i)).UrlThread() != null) {
                                intent.putExtra("URL", ((Thread) PageNewPost.this.ListContains.get(i)).UrlThread());
                            } else {
                                return;
                            }
                        } catch (Exception e2) {
                            e = e2;
                            e.printStackTrace();
                        }
                    } else if (((Thread) PageNewPost.this.ListContains.get(i)).isUrl() == 1) {
                        intent = new Intent(PageNewPost.this.mContext, Page3.class);
                        if (((Thread) PageNewPost.this.ListContains.get(i)).UrlLastPosst() == null || ((Thread) PageNewPost.this.ListContains.get(i)).UrlLastPosst().equals(BuildConfig.FLAVOR)) {
                            if (((Thread) PageNewPost.this.ListContains.get(i)).UrlThread() != null) {
                                intent.putExtra("URL", ((Thread) PageNewPost.this.ListContains.get(i)).UrlThread());
                            } else {
                                return;
                            }
                        } else if (((Thread) PageNewPost.this.ListContains.get(i)).UrlLastPosst() != null) {
                            intent.putExtra("URL", ((Thread) PageNewPost.this.ListContains.get(i)).UrlLastPosst());
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                    intent.putExtra("TITLE", ((Thread) PageNewPost.this.ListContains.get(i)).Thread());
                    PageNewPost.this.startActivity(intent);
                    PageNewPost.this.overridePendingTransition(R.anim.rail, R.anim.rail);
                } catch (Exception e3) {
                    e = e3;
                    e.printStackTrace();
                }
            }
        });
        this.mList.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
                PageNewPost.this.alertLongClick(i);
                PageNewPost.this.vibrate();
                return false;
            }
        });
        this.lComment = this.mDataBookmark.getAllComments();
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
