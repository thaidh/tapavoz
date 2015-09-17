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

public class PageCP extends Page {
    static final String urlListSubscribe = "https://vozforums.com/subscription.php?do=viewsubscription";
    static final String urlNewSubscribe = "https://vozforums.com/usercp.php";
    private ArrayList<Thread> ListContains;
    private listViewCustom2 adapter;
    List<Comment> lComment;

    /* renamed from: com.nna88.voz.main.PageCP.3 */
    class AnonymousClass3 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ int val$pos;

        AnonymousClass3(int i, Dialog dialog) {
            this.val$pos = i;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            Intent intent = new Intent(PageCP.this.mContext, Page2.class);
            intent.putExtra("URL", ((Thread) PageCP.this.ListContains.get(this.val$pos)).mPrefixLink);
            intent.putExtra("TITLE", BuildConfig.FLAVOR);
            PageCP.this.startActivity(intent);
            PageCP.this.overridePendingTransition(R.anim.rail, R.anim.rail);
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.PageCP.4 */
    class AnonymousClass4 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ int val$pos;

        AnonymousClass4(int i, Dialog dialog) {
            this.val$pos = i;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            int i = VERSION.SDK_INT;
            String UrlThread = ((Thread) PageCP.this.ListContains.get(this.val$pos)).UrlThread();
            CharSequence charSequence;
            if (UrlThread.contains(Global.URL)) {
                charSequence = UrlThread;
            } else {
                charSequence = Global.URL + UrlThread;
            }
            if (i < 11) {
                ((ClipboardManager) PageCP.this.getSystemService("clipboard")).setText(charSequence);
            } else {
                ((android.content.ClipboardManager) PageCP.this.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("text label", charSequence));
            }
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.PageCP.5 */
    class AnonymousClass5 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ int val$pos;

        AnonymousClass5(int i, Dialog dialog) {
            this.val$pos = i;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            if (((Thread) PageCP.this.ListContains.get(this.val$pos)).UrlThread() != null) {
                Intent intent = new Intent(PageCP.this.mContext, Page3.class);
                intent.putExtra("URL", ((Thread) PageCP.this.ListContains.get(this.val$pos)).UrlThread());
                intent.putExtra("TITLE", BuildConfig.FLAVOR);
                PageCP.this.startActivity(intent);
                PageCP.this.overridePendingTransition(R.anim.rail, R.anim.rail);
                this.val$dialog.dismiss();
            }
        }
    }

    /* renamed from: com.nna88.voz.main.PageCP.6 */
    class AnonymousClass6 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ int val$pos;

        AnonymousClass6(int i, Dialog dialog) {
            this.val$pos = i;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            if (((Thread) PageCP.this.ListContains.get(this.val$pos)).UrlLastPosst() == null) {
                PageCP.this.toast("Kh\u00f4ng c\u00f3 link");
                return;
            }
            Intent intent = new Intent(PageCP.this.mContext, Page3.class);
            intent.putExtra("URL", ((Thread) PageCP.this.ListContains.get(this.val$pos)).UrlLastPosst());
            intent.putExtra("TITLE", ((Thread) PageCP.this.ListContains.get(this.val$pos)).UrlLastPosst());
            PageCP.this.startActivity(intent);
            PageCP.this.overridePendingTransition(R.anim.rail, R.anim.rail);
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.PageCP.7 */
    class AnonymousClass7 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ int val$pos;

        AnonymousClass7(int i, Dialog dialog) {
            this.val$pos = i;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            if (((Thread) PageCP.this.ListContains.get(this.val$pos)).getUrlLastLast() == null) {
                PageCP.this.toast("Kh\u00f4ng c\u00f3 link");
                return;
            }
            Intent intent = new Intent(PageCP.this.mContext, Page3.class);
            intent.putExtra("URL", ((Thread) PageCP.this.ListContains.get(this.val$pos)).getUrlLastLast());
            intent.putExtra("TITLE", BuildConfig.FLAVOR);
            PageCP.this.startActivity(intent);
            PageCP.this.overridePendingTransition(R.anim.rail, R.anim.rail);
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.PageCP.8 */
    class AnonymousClass8 implements OnClickListener {
        final /* synthetic */ Thread val$contain;
        final /* synthetic */ Dialog val$dialog;

        AnonymousClass8(Thread thread, Dialog dialog) {
            this.val$contain = thread;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            if (this.val$contain.isBookmark) {
                PageCP.this.mDataBookmark.deleteBookmark(this.val$contain);
                this.val$contain.isBookmark = false;
            } else {
                this.val$contain.isBookmark = true;
                PageCP.this.mDataBookmark.addBookmark(this.val$contain);
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
            textView6.setVisibility(0);
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

    private boolean checkBookmark(String str) {
        for (Comment comment : this.lComment) {
            if (comment.idThread.equals(str)) {
                return true;
            }
        }
        return false;
    }

    void Parser(int i) throws Exception {
        super.Parser(i);
        if (this.doc != null) {
            String str;
            String str2;
            Bitmap bitmap;
            String str3;
            Iterator it = this.doc.select("tr:has(td[id*=td_threads])").iterator();
            if (it.hasNext()) {
                it.next();
                str = null;
                str2 = null;
                bitmap = null;
                str3 = null;
            } else {
                str = null;
                str2 = null;
                bitmap = null;
                str3 = null;
            }
            while (it.hasNext()) {
                String attr;
                Element element = (Element) it.next();
                if (element.select("a[id*=thread_title").first() != null) {
                    str = element.select("a[id*=thread_title").first().text();
                    str2 = element.select("a[id*=thread_title").first().attr("href");
                }
                Element first = element.select("td[title*=Replies]").first();
                if (first != null) {
                    Element first2 = first.select("div").first();
                    if (first2 != null) {
                        str3 = first2.ownText() + " " + first2.select("a[href*=member.php]").text();
                    }
                    if (element.select("div:has(span[onclick*=member.php])").first() != null) {
                        str3 = element.select("div:has(span[onclick*=member.php])").first().text() + " - " + str3;
                    }
                    String str4 = str3 + IOUtils.LINE_SEPARATOR_UNIX + first.attr(MySQLiteHelper.COLUMN_TITLE);
                    if (first.select("a[href*=showthread]").first() != null) {
                        attr = first.select("a[href*=showthread]").attr("href");
                        str3 = str4;
                    } else {
                        attr = null;
                        str3 = str4;
                    }
                } else {
                    attr = null;
                }
                if (element.select("img[id*=thread_statusicon]").first() != null) {
                    bitmap = loadBitmapAssert(element.select("img[id*=thread_statusicon]").first().attr("src"));
                }
                Thread thread = new Thread(str, str3, null, null, bitmap, str2, element.select("a[id*=thread_gotonew]").first() != null ? element.select("a[id*=thread_gotonew]").first().attr("href") : null);
                thread.mIdThread = str2.split("t=")[1];
                thread.setUrlLastPost(attr);
                if (checkBookmark(thread.mIdThread)) {
                    thread.isBookmark = true;
                    this.mDataBookmark.updateBookmark(thread);
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
        }
    }

    protected void onCreate(Bundle bundle) {
        this.iPage = 4;
        super.onCreate(bundle);
        this.ListContains = new ArrayList();
        this.adapter = new listViewCustom2(this.mContext, this.ListContains);
        this.adapter.setSize(this.mTextSize);
        this.mObjectAdapter = this.adapter;
        this.mList.setAdapter(this.adapter);
        this.mParser.setUrl(urlListSubscribe);
        this.mTask.execute(new Integer[]{Integer.valueOf(0)});
        this.mList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Exception e;
                try {
                    Intent intent;
                    PageCP.this.vibrate();
                    if (((Thread) PageCP.this.ListContains.get(i)).isUrl() == 0) {
                        intent = new Intent(PageCP.this.mContext, Page2.class);
                        try {
                            if (((Thread) PageCP.this.ListContains.get(i)).UrlThread() != null) {
                                intent.putExtra("URL", ((Thread) PageCP.this.ListContains.get(i)).UrlThread());
                            } else {
                                return;
                            }
                        } catch (Exception e2) {
                            e = e2;
                            e.printStackTrace();
                        }
                    } else if (((Thread) PageCP.this.ListContains.get(i)).isUrl() == 1) {
                        intent = new Intent(PageCP.this.mContext, Page3.class);
                        if (((Thread) PageCP.this.ListContains.get(i)).UrlLastPosst() == null || ((Thread) PageCP.this.ListContains.get(i)).UrlLastPosst().equals(BuildConfig.FLAVOR)) {
                            if (((Thread) PageCP.this.ListContains.get(i)).UrlThread() != null) {
                                intent.putExtra("URL", ((Thread) PageCP.this.ListContains.get(i)).UrlThread());
                            } else {
                                return;
                            }
                        } else if (((Thread) PageCP.this.ListContains.get(i)).UrlLastPosst() != null) {
                            intent.putExtra("URL", ((Thread) PageCP.this.ListContains.get(i)).UrlLastPosst());
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                    intent.putExtra("TITLE", ((Thread) PageCP.this.ListContains.get(i)).Thread());
                    PageCP.this.startActivity(intent);
                    PageCP.this.overridePendingTransition(R.anim.rail, R.anim.rail);
                } catch (Exception e3) {
                    e = e3;
                    e.printStackTrace();
                }
            }
        });
        this.mList.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
                PageCP.this.alertLongClick(i);
                PageCP.this.vibrate();
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
