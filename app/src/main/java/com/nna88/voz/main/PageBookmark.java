package com.nna88.voz.main;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
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
import java.util.List;

public class PageBookmark extends Page {
    private listViewCustom2 adapter;
    private ArrayList<Thread> lContains;

    /* renamed from: com.nna88.voz.main.PageBookmark.3 */
    class AnonymousClass3 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ int val$pos;

        AnonymousClass3(int i, Dialog dialog) {
            this.val$pos = i;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            Intent intent = new Intent(PageBookmark.this.mContext, Page2.class);
            intent.putExtra("URL", ((Thread) PageBookmark.this.lContains.get(this.val$pos)).mPrefixLink);
            intent.putExtra("TITLE", BuildConfig.FLAVOR);
            PageBookmark.this.startActivity(intent);
            PageBookmark.this.overridePendingTransition(R.anim.rail, R.anim.rail);
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.PageBookmark.4 */
    class AnonymousClass4 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ int val$pos;

        AnonymousClass4(int i, Dialog dialog) {
            this.val$pos = i;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            int i = VERSION.SDK_INT;
            String UrlThread = ((Thread) PageBookmark.this.lContains.get(this.val$pos)).UrlThread();
            CharSequence charSequence;
            if (UrlThread.contains(Global.URL)) {
                charSequence = UrlThread;
            } else {
                charSequence = Global.URL + UrlThread;
            }
            if (i < 11) {
                ((ClipboardManager) PageBookmark.this.getSystemService("clipboard")).setText(charSequence);
            } else {
                ((android.content.ClipboardManager) PageBookmark.this.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("text label", charSequence));
            }
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.PageBookmark.5 */
    class AnonymousClass5 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ int val$pos;

        AnonymousClass5(int i, Dialog dialog) {
            this.val$pos = i;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            Intent intent = new Intent(PageBookmark.this.mContext, Page3.class);
            intent.putExtra("URL", ((Thread) PageBookmark.this.lContains.get(this.val$pos)).UrlThread());
            intent.putExtra("TITLE", BuildConfig.FLAVOR);
            PageBookmark.this.startActivity(intent);
            PageBookmark.this.overridePendingTransition(R.anim.rail, R.anim.rail);
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.PageBookmark.6 */
    class AnonymousClass6 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ int val$pos;

        AnonymousClass6(int i, Dialog dialog) {
            this.val$pos = i;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            if (((Thread) PageBookmark.this.lContains.get(this.val$pos)).UrlLastPosst().equals(BuildConfig.FLAVOR)) {
                PageBookmark.this.toast("Kh\u00f4ng c\u00f3 link");
                return;
            }
            Intent intent = new Intent(PageBookmark.this.mContext, Page3.class);
            intent.putExtra("URL", ((Thread) PageBookmark.this.lContains.get(this.val$pos)).UrlLastPosst());
            intent.putExtra("TITLE", ((Thread) PageBookmark.this.lContains.get(this.val$pos)).UrlLastPosst());
            PageBookmark.this.startActivity(intent);
            PageBookmark.this.overridePendingTransition(R.anim.rail, R.anim.rail);
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.PageBookmark.7 */
    class AnonymousClass7 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ int val$pos;

        AnonymousClass7(int i, Dialog dialog) {
            this.val$pos = i;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            if (((Thread) PageBookmark.this.lContains.get(this.val$pos)).getUrlLastLast() == null) {
                PageBookmark.this.toast("Kh\u00f4ng c\u00f3 link");
                return;
            }
            Intent intent = new Intent(PageBookmark.this.mContext, Page3.class);
            intent.putExtra("URL", ((Thread) PageBookmark.this.lContains.get(this.val$pos)).getUrlLastLast());
            intent.putExtra("TITLE", BuildConfig.FLAVOR);
            PageBookmark.this.startActivity(intent);
            PageBookmark.this.overridePendingTransition(R.anim.rail, R.anim.rail);
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.PageBookmark.8 */
    class AnonymousClass8 implements OnClickListener {
        final /* synthetic */ Thread val$contain;
        final /* synthetic */ Dialog val$dialog;

        AnonymousClass8(Thread thread, Dialog dialog) {
            this.val$contain = thread;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            PageBookmark.this.mDataBookmark.deleteBookmark(this.val$contain);
            this.val$contain.isBookmark = false;
            PageBookmark.this.lContains.remove(this.val$contain);
            PageBookmark.this.adapter.notifyDataSetChanged();
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
        TextView textView4 = (TextView) dialog.findViewById(R.id.copylink);
        TextView textView5 = (TextView) dialog.findViewById(R.id.bookmark);
        Thread thread = (Thread) this.lContains.get(i);
        thread.isBookmark = true;
        textView5.setText(getResources().getString(R.string.UnBookmark));
        TextView textView6 = (TextView) dialog.findViewById(R.id.Prefix);
        if (((Thread) this.lContains.get(i)).mPrefixLink != null) {
            textView6.setVisibility(0);
        }
        textView6.setOnClickListener(new AnonymousClass3(i, dialog));
        textView4.setOnClickListener(new AnonymousClass4(i, dialog));
        textView2.setOnClickListener(new AnonymousClass5(i, dialog));
        textView.setOnClickListener(new AnonymousClass6(i, dialog));
        textView3.setOnClickListener(new AnonymousClass7(i, dialog));
        textView5.setOnClickListener(new AnonymousClass8(thread, dialog));
        dialog.show();
        return dialog;
    }

    protected void onCreate(Bundle bundle) {
        this.iPage = 5;
        super.onCreate(bundle);
        this.lContains = new ArrayList();
        this.adapter = new listViewCustom2(this.mContext, this.lContains);
        this.mObjectAdapter = this.adapter;
        this.adapter.setSize(this.mTextSize);
        this.mList.setAdapter(this.adapter);
        List allComments = this.mDataBookmark.getAllComments();
        this.lContains.add(new Thread("ListBookmark", null, null, null, null, null, null));
        for (int size = allComments.size() - 1; size >= 0; size--) {
            Comment comment = (Comment) allComments.get(size);
            Thread thread = new Thread("  " + comment.title, comment.LastPost, comment.iPage, comment.View, null, comment.url, comment.urlFirstNews);
            thread.mIdThread = comment.idThread;
            thread.setUrlLastPost(comment.urlLastPost);
            this.lContains.add(thread);
        }
        this.adapter.notifyDataSetChanged();
        this.mItemCount = this.adapter.getCount();
        if (this.mItemOffsetY == null) {
            this.mItemOffsetY = new int[(this.mItemCount + 1)];
            this.mItemtemp = new int[(this.mItemCount + 1)];
        }
        this.scrollIsComputed = true;
        this.mList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Exception e;
                try {
                    Intent intent;
                    PageBookmark.this.vibrate();
                    if (((Thread) PageBookmark.this.lContains.get(i)).isUrl() == 0) {
                        intent = new Intent(PageBookmark.this.mContext, Page2.class);
                        try {
                            if (((Thread) PageBookmark.this.lContains.get(i)).UrlThread() != null) {
                                intent.putExtra("URL", ((Thread) PageBookmark.this.lContains.get(i)).UrlThread());
                            } else {
                                return;
                            }
                        } catch (Exception e2) {
                            e = e2;
                            e.printStackTrace();
                        }
                    } else if (((Thread) PageBookmark.this.lContains.get(i)).isUrl() == 1) {
                        intent = new Intent(PageBookmark.this.mContext, Page3.class);
                        if (((Thread) PageBookmark.this.lContains.get(i)).UrlLastPosst() == null || ((Thread) PageBookmark.this.lContains.get(i)).UrlLastPosst().equals(BuildConfig.FLAVOR)) {
                            if (((Thread) PageBookmark.this.lContains.get(i)).UrlThread() != null) {
                                intent.putExtra("URL", ((Thread) PageBookmark.this.lContains.get(i)).UrlThread());
                            } else {
                                return;
                            }
                        } else if (((Thread) PageBookmark.this.lContains.get(i)).UrlLastPosst() != null) {
                            intent.putExtra("URL", ((Thread) PageBookmark.this.lContains.get(i)).UrlLastPosst());
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                    intent.putExtra("TITLE", ((Thread) PageBookmark.this.lContains.get(i)).Thread());
                    PageBookmark.this.startActivity(intent);
                    PageBookmark.this.overridePendingTransition(R.anim.rail, R.anim.rail);
                } catch (Exception e3) {
                    e = e3;
                    e.printStackTrace();
                }
            }
        });
        this.mList.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
                PageBookmark.this.alertLongClick(i);
                PageBookmark.this.vibrate();
                return false;
            }
        });
        setProgress(false);
    }
}
