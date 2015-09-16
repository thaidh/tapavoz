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

public class PageSearch extends Page {
    private ArrayList<Thread> ListContains;
    private listViewCustom2 adapter;
    List<Comment> lComment;

    /* renamed from: com.nna88.voz.main.PageSearch.3 */
    class AnonymousClass3 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ int val$pos;

        AnonymousClass3(int i, Dialog dialog) {
            this.val$pos = i;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            Intent intent = new Intent(PageSearch.this.mContext, Page2.class);
            intent.putExtra("URL", ((Thread) PageSearch.this.ListContains.get(this.val$pos)).mPrefixLink);
            intent.putExtra("TITLE", BuildConfig.FLAVOR);
            PageSearch.this.startActivity(intent);
            PageSearch.this.overridePendingTransition(R.anim.rail, R.anim.rail);
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.PageSearch.4 */
    class AnonymousClass4 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ int val$pos;

        AnonymousClass4(int i, Dialog dialog) {
            this.val$pos = i;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            int i = VERSION.SDK_INT;
            String UrlThread = ((Thread) PageSearch.this.ListContains.get(this.val$pos)).UrlThread();
            CharSequence charSequence;
            if (UrlThread.contains(Global.URL)) {
                charSequence = UrlThread;
            } else {
                charSequence = Global.URL + UrlThread;
            }
            if (i < 11) {
                ((ClipboardManager) PageSearch.this.getSystemService("clipboard")).setText(charSequence);
            } else {
                ((android.content.ClipboardManager) PageSearch.this.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("text label", charSequence));
            }
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.PageSearch.5 */
    class AnonymousClass5 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ int val$pos;

        AnonymousClass5(int i, Dialog dialog) {
            this.val$pos = i;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            Intent intent = new Intent(PageSearch.this.mContext, Page3.class);
            intent.putExtra("URL", ((Thread) PageSearch.this.ListContains.get(this.val$pos)).UrlThread());
            intent.putExtra("TITLE", BuildConfig.FLAVOR);
            PageSearch.this.startActivity(intent);
            PageSearch.this.overridePendingTransition(R.anim.rail, R.anim.rail);
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.PageSearch.6 */
    class AnonymousClass6 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ int val$pos;

        AnonymousClass6(int i, Dialog dialog) {
            this.val$pos = i;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            if (((Thread) PageSearch.this.ListContains.get(this.val$pos)).UrlLastPosst() == null) {
                PageSearch.this.toast("Kh\u00f4ng co link");
                return;
            }
            Intent intent = new Intent(PageSearch.this.mContext, Page3.class);
            intent.putExtra("URL", ((Thread) PageSearch.this.ListContains.get(this.val$pos)).UrlLastPosst());
            intent.putExtra("TITLE", ((Thread) PageSearch.this.ListContains.get(this.val$pos)).UrlLastPosst());
            PageSearch.this.startActivity(intent);
            PageSearch.this.overridePendingTransition(R.anim.rail, R.anim.rail);
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.PageSearch.7 */
    class AnonymousClass7 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ int val$pos;

        AnonymousClass7(int i, Dialog dialog) {
            this.val$pos = i;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            if (((Thread) PageSearch.this.ListContains.get(this.val$pos)).getUrlLastLast() == null) {
                PageSearch.this.toast("Kh\u00f4ng c\u00f3 link");
                return;
            }
            Intent intent = new Intent(PageSearch.this.mContext, Page3.class);
            intent.putExtra("URL", ((Thread) PageSearch.this.ListContains.get(this.val$pos)).getUrlLastLast());
            intent.putExtra("TITLE", BuildConfig.FLAVOR);
            PageSearch.this.startActivity(intent);
            PageSearch.this.overridePendingTransition(R.anim.rail, R.anim.rail);
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.PageSearch.8 */
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
                PageSearch.this.mDataBookmark.deleteBookmark(this.val$contain);
            } else {
                this.val$contain.isBookmark = true;
                PageSearch.this.mDataBookmark.addBookmark(this.val$contain);
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

    void Parser(int i) throws Exception {
        super.Parser(i);
        if (this.doc != null) {
            this.ListContains.clear();
            String str = BuildConfig.FLAVOR;
            setPage(parsePage(0, 0));
            String str2;
            Bitmap bitmap;
            String str3;
            if (this.sSearch_ShowPost.equals("0")) {
                Iterator it = this.doc.select("tr:has(td[id*=td_threadstatusicon])").iterator();
                str2 = null;
                bitmap = null;
                String str4 = null;
                while (it.hasNext()) {
                    it.hasNext();
                    Element element = (Element) it.next();
                    String str5 = BuildConfig.FLAVOR;
                    str = BuildConfig.FLAVOR;
                    if (element.select("a[id*=thread_title]").first() != null) {
                        str5 = element.select("a[id*=thread_title]").first().text();
                        if (element.select("div:has(span[onclick*=member.php])").first() != null) {
                            str5 = str5 + " " + this.mColorText2 + "'>- " + element.select("div:has(span[onclick*=member.php])").first().text();
                        }
                        str2 = element.select("a[id*=thread_title]").first().attr("href");
                        str3 = str5;
                    } else {
                        str3 = str5;
                    }
                    Elements select = element.select("a[href*=forumdisplay]");
                    if (select.size() > 1) {
                        str = "Forums: " + ((Element) select.get(1)).text() + IOUtils.LINE_SEPARATOR_UNIX;
                        str3 = ((Element) select.get(0)).text() + " - " + str3;
                        str5 = str;
                    } else {
                        str5 = select.size() == 1 ? "Forums: " + ((Element) select.get(0)).text() + IOUtils.LINE_SEPARATOR_UNIX : str;
                    }
                    if (element.select("div[style*=text-align:right]").first() != null) {
                        str5 = str5 + element.select("div[style*=text-align:right]").first().text() + IOUtils.LINE_SEPARATOR_UNIX;
                    }
                    str = element.select("td[title*=Replies]").first() != null ? str5 + element.select("td[title*=Replies]").first().attr(MySQLiteHelper.COLUMN_TITLE) : str5;
                    if (Global.bTopicHeader && element.select("img[id*=thread_statusicon]").first() != null) {
                        bitmap = loadBitmapAssert(element.select("img[id*=thread_statusicon]").first().attr("src"));
                    }
                    if (element.select("a:has(img[src*=last])").first() != null) {
                        str4 = element.select("a:has(img[src*=last])").attr("href");
                    }
                    Thread thread = new Thread(str3, str, null, null, bitmap, str2, str4);
                    if (element.select("a[id*=thread_gotonew]").first() != null) {
                        thread.setUrlLastPost(element.select("a[id*=thread_gotonew]").attr("href"));
                    }
                    thread.setSticky(false);
                    thread.mIdThread = str2.split("t=")[1];
                    this.ListContains.add(thread);
                }
            } else {
                Iterator it2 = this.doc.select("table[id]").iterator();
                if (it2.hasNext()) {
                    it2.next();
                    str3 = null;
                    bitmap = null;
                    str2 = null;
                } else {
                    str3 = null;
                    bitmap = null;
                    str2 = null;
                }
                while (it2.hasNext()) {
                    Element element2 = (Element) it2.next();
                    Element first = element2.select("td[class=thead]").first();
                    if (first != null) {
                        str = ("Forums: " + first.select("a").first().text()) + "<br />" + first.ownText();
                    }
                    if (element2.select("a[href*=showthread.php]").first() != null) {
                        str3 = element2.select("a[href*=showthread.php]").first().text();
                        str2 = element2.select("a[href*=showthread.php]").first().attr("href");
                    }
                    if (element2.select("a[href*=member.php]").first() != null) {
                    }
                    if (element2.select("em").first() != null) {
                        str = str + "<br />" + element2.select("em").first().ownText();
                        str2 = element2.select("em").first().select("a").attr("href");
                    }
                    if (Global.bTopicHeader && element2.select("img[id*=thread_statusicon]").first() != null) {
                        bitmap = loadBitmapAssert(element2.select("img[id*=thread_statusicon]").first().attr("src"));
                    }
                    this.ListContains.add(new Thread(str3, str, null, null, bitmap, str2, null));
                }
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
                    PageSearch.this.mList.setSelection(0);
                }
            });
        }
    }

    protected void onCreate(Bundle bundle) {
        this.iPage = 13;
        super.onCreate(bundle);
        this.ListContains = new ArrayList();
        this.adapter = new listViewCustom2(this.mContext, this.ListContains);
        this.adapter.setSize(this.mTextSize);
        this.mObjectAdapter = this.adapter;
        this.mList.setAdapter(this.adapter);
        this.sMessSearch = getIntent().getStringExtra("MESS");
        this.sForum = getIntent().getStringExtra("FORUM");
        this.sSearch_ShowPost = getIntent().getStringExtra("SHOWPOST");
        if (this.sForum != null) {
            this.mTask.execute(new Integer[]{Integer.valueOf(13)});
        } else {
            this.mTask.execute(new Integer[]{Integer.valueOf(14)});
        }
        this.mList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Exception e;
                try {
                    Intent intent;
                    PageSearch.this.vibrate();
                    if (((Thread) PageSearch.this.ListContains.get(i)).isUrl() == 0) {
                        intent = new Intent(PageSearch.this.mContext, Page2.class);
                        try {
                            if (((Thread) PageSearch.this.ListContains.get(i)).UrlThread() != null) {
                                intent.putExtra("URL", ((Thread) PageSearch.this.ListContains.get(i)).UrlThread());
                            } else {
                                return;
                            }
                        } catch (Exception e2) {
                            e = e2;
                            e.printStackTrace();
                        }
                    } else if (((Thread) PageSearch.this.ListContains.get(i)).isUrl() == 1) {
                        intent = new Intent(PageSearch.this.mContext, Page3.class);
                        if (((Thread) PageSearch.this.ListContains.get(i)).UrlLastPosst() == null || ((Thread) PageSearch.this.ListContains.get(i)).UrlLastPosst().equals(BuildConfig.FLAVOR)) {
                            if (((Thread) PageSearch.this.ListContains.get(i)).UrlThread() != null) {
                                intent.putExtra("URL", ((Thread) PageSearch.this.ListContains.get(i)).UrlThread());
                            } else {
                                return;
                            }
                        } else if (((Thread) PageSearch.this.ListContains.get(i)).UrlLastPosst() != null) {
                            intent.putExtra("URL", ((Thread) PageSearch.this.ListContains.get(i)).UrlLastPosst());
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                    intent.putExtra("TITLE", ((Thread) PageSearch.this.ListContains.get(i)).Thread());
                    PageSearch.this.startActivity(intent);
                    PageSearch.this.overridePendingTransition(R.anim.rail, R.anim.rail);
                } catch (Exception e3) {
                    e = e3;
                    e.printStackTrace();
                }
            }
        });
        this.mList.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
                PageSearch.this.alertLongClick(i);
                PageSearch.this.vibrate();
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
