package com.nna88.voz.main;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.nna88.voz.contain.containEmo;
import com.nna88.voz.listview.GridAdapter;
import com.nna88.voz.listview.listViewEmo;
import com.nna88.voz.mysqlite.MySQLiteHelper;
import com.nna88.voz.util.HorizontialListView;
import java.io.InputStream;
import java.util.ArrayList;
import org.apache.http.conn.params.ConnManagerParams;

public class PageReply extends Activity {
    private Builder alert;
    private EditText edit1;
    private EditText edit2;
    private LayoutInflater factory;
    private GridView gridview;
    private int iPostType;
    private RelativeLayout layout;
    private ArrayList<containEmo> listEmo;
    OnClickListener mBoldClick;
    private Context mContext;
    OnItemClickListener mEmoListClick;
    public final Handler mHandler;
    OnClickListener mImageClick;
    OnClickListener mItalicClick;
    OnClickListener mLinkClick;
    private HorizontialListView mListEmo;
    OnClickListener mPostCancleClick;
    OnClickListener mPostEmoClick;
    OnClickListener mPostReplyClick;
    OnClickListener mUnderClick;
    private Button m_ButPost;
    private Button m_ButPostCancle;
    private ImageButton m_ButPostEmo;
    private EditText m_TextPost;
    private String sSignature;
    private TextView text1;
    private TextView text2;
    private View textEntryView;
    private EditText textTitle;

    /* renamed from: com.nna88.voz.main.PageReply.10 */
    class AnonymousClass10 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;

        AnonymousClass10(Dialog dialog) {
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.PageReply.6 */
    class AnonymousClass6 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ AutoCompleteTextView val$edit1;

        AnonymousClass6(AutoCompleteTextView autoCompleteTextView, Dialog dialog) {
            this.val$edit1 = autoCompleteTextView;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            PageReply.this.m_TextPost.getText().insert(PageReply.this.m_TextPost.getSelectionStart(), "[IMG]" + this.val$edit1.getText().toString() + "[/IMG]");
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.PageReply.7 */
    class AnonymousClass7 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;

        AnonymousClass7(Dialog dialog) {
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.PageReply.9 */
    class AnonymousClass9 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ AutoCompleteTextView val$edit1;

        AnonymousClass9(AutoCompleteTextView autoCompleteTextView, Dialog dialog) {
            this.val$edit1 = autoCompleteTextView;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            PageReply.this.m_TextPost.getText().insert(PageReply.this.m_TextPost.getSelectionStart(), "[URL=\"" + this.val$edit1.getText().toString() + "\"]" + this.val$edit1.getText().toString() + "[/URL]");
            this.val$dialog.dismiss();
        }
    }

    public PageReply() {
        this.mHandler = new Handler() {
            public void handleMessage(Message message) {
                switch (message.what) {
                    case ConnManagerParams.DEFAULT_MAX_TOTAL_CONNECTIONS /*20*/:
                        PageReply.this.m_TextPost.getText().insert(PageReply.this.m_TextPost.getSelectionStart(), (String) message.obj);
                    default:
                }
            }
        };
        this.mEmoListClick = new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                PageReply.this.mHandler.obtainMessage(20, ((containEmo) PageReply.this.listEmo.get(i)).text()).sendToTarget();
            }
        };
        this.mPostReplyClick = new OnClickListener() {
            public void onClick(View view) {
                PageReply.this.setPostVisible(8);
                PageReply.this.mListEmo.setVisibility(8);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(NotificationCompat.CATEGORY_MESSAGE, PageReply.this.m_TextPost.getText().toString() + "\n\n[I]Sent from my " + Global.sYourDevice + " using [URL=\"https://play.google.com/store/apps/details?id=com.nna88.voz.main\"]vozForums[/URL]" + "[/I]\n");
                intent.putExtras(bundle);
                PageReply.this.setResult(-1, intent);
                PageReply.this.finish();
            }
        };
        this.mPostCancleClick = new OnClickListener() {
            public void onClick(View view) {
                PageReply.this.setResult(0);
                PageReply.this.finish();
            }
        };
        this.mImageClick = new OnClickListener() {
            public void onClick(View view) {
                PageReply.this.alertImage();
            }
        };
        this.mLinkClick = new OnClickListener() {
            public void onClick(View view) {
                PageReply.this.alertLink();
            }
        };
        this.mBoldClick = new OnClickListener() {
            public void onClick(View view) {
                PageReply.this.m_TextPost.getText().insert(PageReply.this.m_TextPost.getSelectionStart(), "[B][/B]");
            }
        };
        this.mItalicClick = new OnClickListener() {
            public void onClick(View view) {
                PageReply.this.m_TextPost.getText().insert(PageReply.this.m_TextPost.getSelectionStart(), "[I][/I]");
            }
        };
        this.mUnderClick = new OnClickListener() {
            public void onClick(View view) {
                PageReply.this.m_TextPost.getText().insert(PageReply.this.m_TextPost.getSelectionStart(), "[U][/U]");
            }
        };
        this.mPostEmoClick = new OnClickListener() {
            public void onClick(View view) {
                if (PageReply.this.gridview.getVisibility() != 0) {
                    PageReply.this.gridview.setVisibility(0);
                } else {
                    PageReply.this.gridview.setVisibility(8);
                }
            }
        };
    }

    private ArrayList<containEmo> Emo2() {
        ArrayList<containEmo> arrayList = new ArrayList();
//        String[] list = this.mContext.getAssets().list("images/smilies/Off");
//        for (int i = 0; i < list.length; i++) {
//            String str;
//            String substring = list[i].substring(0, list[i].length() - 4);
//            InputStream open = substring.equals("brick") ? this.mContext.getAssets().open("images/smilies/Off/" + substring + ".png") : this.mContext.getAssets().open("images/smilies/Off/" + substring + ".gif");
//            Bitmap decodeStream = BitmapFactory.decodeStream(open);
//            if (substring.equals("embarrassed")) {
//                str = ":\">";
//            } else if (substring.equals("nosebleed")) {
//                str = ":chaymau:";
//            } else if (substring.equals("feel_good")) {
//                str = ":sogood:";
//            } else {
//                try {
//                    str = substring.equals("sexy_girl") ? ":sexy:" : substring.equals("look_down") ? ":look_down:" : substring.equals("beat_brick") ? "brick" : substring.equals("cry") ? ":((" : substring.equals("beat_plaster") ? ":-s" : substring.equals("confuse") ? ":chaymau:" : substring.equals("big_smile") ? ":D" : substring.equals("brick") ? ":gach:" : substring.equals("burn_joss_stick") ? ":stick:" : substring.equals("bad_smell") ? ":badsmell:" : substring.equals("cool") ? ":kool:" : substring.equals("after_boom") ? ":aboom:" : substring.equals("beat_shot") ? ":shot:" : substring.equals("sweet_kiss") ? ":*" : substring.equals("beat_shot") ? ":shot:" : substring.equals("smile") ? ":)" : substring.equals("still_dreaming") ? ":dreaming:" : substring.equals("beat_shot") ? ":shot:" : substring.equals("beat_shot") ? ":shot:" : ":" + substring + ":";
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            arrayList.add(new containEmo(decodeStream, str));
//            open.close();
//        }
        return arrayList;
    }

    private void alertDialog() {
        this.factory = LayoutInflater.from(this);
        this.textEntryView = this.factory.inflate(R.layout.login, null);
        this.edit1 = (EditText) this.textEntryView.findViewById(R.id.alert_edit1);
        this.edit2 = (EditText) this.textEntryView.findViewById(R.id.alert_edit2);
        this.text1 = (TextView) this.textEntryView.findViewById(R.id.alert_txt1);
        this.text2 = (TextView) this.textEntryView.findViewById(R.id.alert_txt2);
        this.alert = new Builder(this);
        this.alert.setView(this.textEntryView);
    }

    private Dialog alertImage() {
        Dialog dialog = new Dialog(this.mContext);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.go_address);
        LayoutParams layoutParams = new LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = -1;
        layoutParams.height = -2;
        dialog.getWindow().setAttributes(layoutParams);
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) dialog.findViewById(R.id.alert_edit1);
        Button button = (Button) dialog.findViewById(R.id.alert_ok);
        Button button2 = (Button) dialog.findViewById(R.id.alert_cancle);
        ((TextView) dialog.findViewById(R.id.alert_title)).setText("Please enter the url of your link");
        button.setOnClickListener(new AnonymousClass6(autoCompleteTextView, dialog));
        button2.setOnClickListener(new AnonymousClass7(dialog));
        dialog.show();
        return dialog;
    }

    private void alertInsert() {
        this.edit2.setVisibility(8);
        this.text2.setVisibility(8);
        this.text1.setVisibility(8);
        this.edit1.setText("http://");
        this.edit1.setSelection(this.edit1.getText().length());
    }

    private Dialog alertLink() {
        Dialog dialog = new Dialog(this.mContext);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.go_address);
        LayoutParams layoutParams = new LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = -1;
        layoutParams.height = -2;
        dialog.getWindow().setAttributes(layoutParams);
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) dialog.findViewById(R.id.alert_edit1);
        Button button = (Button) dialog.findViewById(R.id.alert_ok);
        Button button2 = (Button) dialog.findViewById(R.id.alert_cancle);
        ((TextView) dialog.findViewById(R.id.alert_title)).setText("Please enter the url of your link");
        button.setOnClickListener(new AnonymousClass9(autoCompleteTextView, dialog));
        button2.setOnClickListener(new AnonymousClass10(dialog));
        dialog.show();
        return dialog;
    }

    private String getMesseage() {
        String string = getIntent().getExtras().getString(NotificationCompat.CATEGORY_MESSAGE);
        if (this.iPostType != 2) {
            return string;
        }
        if (string.lastIndexOf(this.sSignature) != -1) {
            return string.substring(0, string.lastIndexOf(this.sSignature));
        }
        int lastIndexOf = string.lastIndexOf("[I]Sent from my ");
        return (lastIndexOf == -1 || string.lastIndexOf("[URL=\"https://play.google.com/store/apps/details?id=com.nna88.voz.main\"]vozForums[/URL][/I]") == -1) ? string : string.substring(0, lastIndexOf);
    }

    private void setPostVisible(int i) {
        this.m_ButPost.setVisibility(i);
        this.m_ButPostCancle.setVisibility(i);
        this.m_ButPostEmo.setVisibility(i);
        this.m_TextPost.setVisibility(i);
    }

    private void toast(String str) {
        Toast.makeText(this, str, 0).show();
    }

    protected void onCreate(Bundle bundle) {
        getWindow().requestFeature(1);
        setContentView(R.layout.pagereply);
        super.onCreate(bundle);
        this.sSignature = "[I]Sent from my " + Global.sYourDevice + " using [URL=\"https://play.google.com/store/apps/details?id=com.nna88.voz.main\"]vozForums[/URL][/I]";
        this.mContext = this;
        this.iPostType = getIntent().getIntExtra("POSTTYPE", -1);
        this.textTitle = (EditText) findViewById(R.id.title);
        this.textTitle.setText(getIntent().getExtras().getString(MySQLiteHelper.COLUMN_TITLE));
        this.textTitle.setEnabled(false);
        this.mListEmo = (HorizontialListView) findViewById(R.id.Page3_emo);
        this.m_TextPost = (EditText) findViewById(R.id.Page3_postReply);
        this.m_TextPost.requestFocus();
        this.m_TextPost.setFocusable(true);
        this.m_ButPost = (Button) findViewById(R.id.Page3_butReply);
        this.m_ButPostEmo = (ImageButton) findViewById(R.id.Page3_butReplyEmo);
        this.m_ButPostCancle = (Button) findViewById(R.id.Page3_butReplyCancle);
        this.mListEmo.setOnItemClickListener(this.mEmoListClick);
        this.m_ButPost.setOnClickListener(this.mPostReplyClick);
        this.m_ButPostCancle.setOnClickListener(this.mPostCancleClick);
        this.m_ButPostEmo.setOnClickListener(this.mPostEmoClick);
        this.listEmo = new ArrayList();
        this.listEmo = Emo2();
        this.mListEmo.setAdapter(new listViewEmo(this, this.listEmo));
        this.m_TextPost.setText(getMesseage());
        this.m_TextPost.setSelection(this.m_TextPost.getText().length());
        this.mListEmo.setVisibility(View.GONE);
        this.layout = (RelativeLayout) findViewById(R.id.layout_pagerepley);
        this.gridview = (GridView) findViewById(R.id.grid_view);
        GridAdapter gridAdapter = new GridAdapter(this.mContext, this.listEmo);
        this.gridview.setAdapter(gridAdapter);
        this.gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                PageReply.this.mHandler.obtainMessage(20, ((containEmo) PageReply.this.listEmo.get(i)).text()).sendToTarget();
            }
        });
        Global.setBackgroundItemThread(this.m_TextPost);
        Global.setTextContain(this.m_TextPost);
        Global.setBackgroundMenuLogo(this.textTitle);
        Global.setTextMenuTitle(this.textTitle);
        this.gridview.requestLayout();
        gridAdapter.notifyDataSetChanged();
    }

    protected void onStop() {
        super.onStop();
    }
}
