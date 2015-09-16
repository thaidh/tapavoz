package com.nna88.voz.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.nna88.voz.contain.containEmo;
import com.nna88.voz.listview.GridAdapter;
import com.nna88.voz.mysqlite.MySQLiteHelper;
import com.nna88.voz.parserhtml.parser;
import com.nna88.voz.util.UploadImage2;
import com.nna88.voz.util.UserInfo;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;
import org.apache.http.conn.params.ConnManagerParams;
import org.jsoup.nodes.Document;

public class PageNewThread extends Activity {
    private Document doc;
    private GridView gridview;
    private int iPostType;
    private int iUpanh;
    private ArrayList<containEmo> listEmo;
    private Context mContext;
    OnItemClickListener mEmoListClick;
    public final Handler mHandler;
    protected Dialog mLoading;
    OnClickListener mOnCameraClick;
    private parser mParser;
    OnClickListener mPostCancleClick;
    OnClickListener mPostEmoClick;
    OnClickListener mPostReplyClick;
    private Task mTask;
    private UserInfo mUser;
    private ImageButton m_ButLink;
    private Button m_ButPost;
    private Button m_ButPostCancle;
    private ImageButton m_ButPostEmo;
    private ImageButton m_CameraButton;
    private EditText m_TextPost;
    private String pmMid;
    private String pmReciple;
    private String pmTitle;
    private String pmToken;
    private String pmUserId;
    private String postHash;
    private String sSignature;
    private String sSignature1;
    private String secToken;
    private String sforums;
    private String startTime;
    private EditText textTitle;

    /* renamed from: com.nna88.voz.main.PageNewThread.11 */
    class AnonymousClass11 extends UploadImage2 {
        final /* synthetic */ Uri val$chosenImageUri;

        AnonymousClass11(Context context, Uri uri, Uri uri2) {
            super(context, uri);
            this.val$chosenImageUri = uri2;
        }

        protected void onPostExecute(Object obj) {
            super.onPostExecute(obj);
            PageNewThread.this.m_TextPost.getSelectionStart();
            CharSequence charSequence = "[IMG]" + this.url + "[/IMG]\n";
            PageNewThread.this.m_TextPost.setText(PageNewThread.this.m_TextPost.getText().toString().replace("[IMG]" + this.val$chosenImageUri.toString() + "[/IMG]\n", charSequence));
            PageNewThread.this.m_TextPost.setSelection(PageNewThread.this.m_TextPost.getText().length());
            PageNewThread.this.iUpanh = PageNewThread.this.iUpanh - 1;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            PageNewThread.this.m_TextPost.getText().insert(PageNewThread.this.m_TextPost.getSelectionStart(), "[IMG]" + this.val$chosenImageUri.toString() + "[/IMG]\n");
        }
    }

    /* renamed from: com.nna88.voz.main.PageNewThread.6 */
    class AnonymousClass6 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ AutoCompleteTextView val$edit1;
        final /* synthetic */ RadioGroup val$radioButton;

        AnonymousClass6(AutoCompleteTextView autoCompleteTextView, RadioGroup radioGroup, Dialog dialog) {
            this.val$edit1 = autoCompleteTextView;
            this.val$radioButton = radioGroup;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            int selectionStart = PageNewThread.this.m_TextPost.getSelectionStart();
            CharSequence charSequence = "[IMG]" + this.val$edit1.getText().toString() + "[/IMG]";
            if (this.val$radioButton.getCheckedRadioButtonId() == R.id.insert_img) {
                charSequence = "[IMG]" + this.val$edit1.getText().toString() + "[/IMG]";
            } else if (this.val$radioButton.getCheckedRadioButtonId() == R.id.insert_link) {
                charSequence = "[URL]" + this.val$edit1.getText().toString() + "[/URL]";
            } else if (this.val$radioButton.getCheckedRadioButtonId() == R.id.insert_B) {
                charSequence = "[B]" + this.val$edit1.getText().toString() + "[/B]";
            } else if (this.val$radioButton.getCheckedRadioButtonId() == R.id.insert_I) {
                charSequence = "[I]" + this.val$edit1.getText().toString() + "[/I]";
            } else if (this.val$radioButton.getCheckedRadioButtonId() == R.id.insert_U) {
                charSequence = "[U]" + this.val$edit1.getText().toString() + "[/U]";
            } else if (this.val$radioButton.getCheckedRadioButtonId() == R.id.insert_quote) {
                charSequence = "[QUOTE]" + this.val$edit1.getText().toString() + "[/QUOTE]";
            }
            PageNewThread.this.m_TextPost.getText().insert(selectionStart, charSequence);
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.PageNewThread.7 */
    class AnonymousClass7 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;

        AnonymousClass7(Dialog dialog) {
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.PageNewThread.8 */
    class AnonymousClass8 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ RadioGroup val$radioButton;

        AnonymousClass8(RadioGroup radioGroup, Dialog dialog) {
            this.val$radioButton = radioGroup;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            if (this.val$radioButton.getCheckedRadioButtonId() == R.id.uploadCamera) {
                this.val$dialog.dismiss();
                PageNewThread.this.startActivityForResult(Intent.createChooser(new Intent("android.media.action.IMAGE_CAPTURE"), PageNewThread.this.getString(R.string.choose_a_viewer)), 8);
            } else if (this.val$radioButton.getCheckedRadioButtonId() == R.id.uploadGallery) {
                this.val$dialog.dismiss();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.PICK");
                PageNewThread.this.startActivityForResult(Intent.createChooser(intent, PageNewThread.this.getString(R.string.choose_a_viewer)), 8);
            }
        }
    }

    /* renamed from: com.nna88.voz.main.PageNewThread.9 */
    class AnonymousClass9 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;

        AnonymousClass9(Dialog dialog) {
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            this.val$dialog.dismiss();
        }
    }

    private class Task extends AsyncTask<Integer, Void, Integer> {
        private Task() {
        }

        protected Integer doInBackground(Integer... numArr) {
            String str = PageNewThread.this.m_TextPost.getText().toString() + "\n\n[I]Sent from my " + Global.sYourDevice + " using [URL=\"https://play.google.com/store/apps/details?id=com.nna88.voz.main\"]vozForums[/URL][/I]";
            if (numArr[0].intValue() == 0) {
                PageNewThread.this.doc = PageNewThread.this.mParser.getDoc();
                return Integer.valueOf(0);
            } else if (numArr[0].intValue() == 9) {
                PageNewThread.this.mParser.NewThread(PageNewThread.this.textTitle.getText().toString(), str, PageNewThread.this.sforums, PageNewThread.this.secToken, PageNewThread.this.postHash, PageNewThread.this.startTime);
                return Integer.valueOf(9);
            } else if (numArr[0].intValue() == 16) {
                PageNewThread.this.mParser.PostPMReply(PageNewThread.this.pmUserId, PageNewThread.this.pmReciple, PageNewThread.this.pmTitle, str, PageNewThread.this.pmMid, PageNewThread.this.pmToken);
                return Integer.valueOf(16);
            } else if (numArr[0].intValue() != 17) {
                return null;
            } else {
                PageNewThread.this.mParser.PostPMNew(PageNewThread.this.pmReciple, PageNewThread.this.textTitle.getText().toString(), PageNewThread.this.m_TextPost.getText().toString(), PageNewThread.this.pmToken);
                return Integer.valueOf(17);
            }
        }

        protected void onPostExecute(Integer num) {
            super.onPostExecute(num);
            if (num.intValue() == 0) {
                PageNewThread.this.parser();
            } else if (num.intValue() == 9) {
                PageNewThread.this.setResult(-1);
                PageNewThread.this.finish();
            } else if (num.intValue() == 16) {
                PageNewThread.this.setResult(-1);
                PageNewThread.this.finish();
            } else if (num.intValue() == 17) {
                PageNewThread.this.setResult(-1);
                PageNewThread.this.finish();
            }
        }
    }

    public PageNewThread() {
        this.iUpanh = 0;
        this.mHandler = new Handler() {
            public void handleMessage(Message message) {
                switch (message.what) {
                    case ConnManagerParams.DEFAULT_MAX_TOTAL_CONNECTIONS /*20*/:
                        PageNewThread.this.m_TextPost.getText().insert(PageNewThread.this.m_TextPost.getSelectionStart(), (String) message.obj);
                    default:
                }
            }
        };
        this.mEmoListClick = new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                PageNewThread.this.mHandler.obtainMessage(20, ((containEmo) PageNewThread.this.listEmo.get(i)).text()).sendToTarget();
            }
        };
        this.mPostCancleClick = new OnClickListener() {
            public void onClick(View view) {
                PageNewThread.this.setResult(0);
                PageNewThread.this.finish();
            }
        };
        this.mOnCameraClick = new OnClickListener() {
            public void onClick(View view) {
                PageNewThread.this.alertSelectImage();
            }
        };
        this.mPostEmoClick = new OnClickListener() {
            public void onClick(View view) {
                if (PageNewThread.this.gridview.getVisibility() != 0) {
                    PageNewThread.this.gridview.setVisibility(0);
                } else {
                    PageNewThread.this.gridview.setVisibility(8);
                }
            }
        };
        this.mPostReplyClick = new OnClickListener() {
            public void onClick(View view) {
                if (PageNewThread.this.iUpanh != 0) {
                    PageNewThread.this.toast("Vui l\u00f2ng ch\u1edd up \u1ea3nh xong!");
                    return;
                }
                String obj = PageNewThread.this.m_TextPost.getText().toString();
                try {
                    if (VERSION.SDK_INT < 11) {
                        ((ClipboardManager) PageNewThread.this.getSystemService("clipboard")).setText(obj);
                    } else {
                        ((android.content.ClipboardManager) PageNewThread.this.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("text label", obj));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String str = obj + "\n\n[I]Sent from my " + Global.sYourDevice + " using [URL=\"https://play.google.com/store/apps/details?id=com.nna88.voz.main\"]vozForums[/URL][/I]";
                if (PageNewThread.this.iPostType == 5) {
                    PageNewThread.this.mTask.cancel(true);
                    PageNewThread.this.mTask = new Task();
                    PageNewThread.this.mTask.execute(new Integer[]{Integer.valueOf(9)});
                } else if (PageNewThread.this.iPostType == 6) {
                    PageNewThread.this.mTask.cancel(true);
                    PageNewThread.this.mTask = new Task();
                    PageNewThread.this.mTask.execute(new Integer[]{Integer.valueOf(16)});
                } else if (PageNewThread.this.iPostType == 7) {
                    PageNewThread.this.mTask.cancel(true);
                    PageNewThread.this.mTask = new Task();
                    PageNewThread.this.mTask.execute(new Integer[]{Integer.valueOf(17)});
                } else {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(NotificationCompat.CATEGORY_MESSAGE, str);
                    intent.putExtras(bundle);
                    PageNewThread.this.setResult(-1, intent);
                    PageNewThread.this.finish();
                }
            }
        };
    }

    private ArrayList<containEmo> Emo2() {
//        int i;
        ArrayList<containEmo> arrayList = new ArrayList();
//        String[] list = this.mContext.getAssets().list("images/smilies/Off");
//        for (i = 0; i < list.length; i++) {
//            String substring;
//            if (list[i].contains(".png")) {
//                substring = list[i].substring(0, list[i].length() - 4);
//                InputStream open = this.mContext.getAssets().open("images/smilies/Off/" + list[i]);
//                Bitmap decodeStream = BitmapFactory.decodeStream(open);
//                if (substring.equals("embarrassed")) {
//                    substring = ":\">";
//                } else if (substring.equals("nosebleed")) {
//                    substring = ":chaymau:";
//                } else if (substring.equals("feel_good")) {
//                    substring = ":sogood:";
//                } else if (substring.equals("sexy_girl")) {
//                    substring = ":sexy:";
//                } else if (substring.equals("look_down")) {
//                    substring = ":look_down:";
//                } else if (substring.equals("beat_brick")) {
//                    substring = ":brick:";
//                } else if (substring.equals("cry")) {
//                    substring = ":((";
//                } else if (substring.equals("beat_plaster")) {
//                    substring = ":plaster:";
//                } else if (substring.equals("confuse")) {
//                    substring = ":-s";
//                } else if (substring.equals("big_smile")) {
//                    substring = ":D";
//                } else if (substring.equals("brick")) {
//                    substring = ":gach:";
//                } else if (substring.equals("burn_joss_stick")) {
//                    substring = ":stick:";
//                } else if (substring.equals("bad_smell")) {
//                    substring = ":badsmell:";
//                } else if (substring.equals("cool")) {
//                    substring = ":kool:";
//                } else if (substring.equals("after_boom")) {
//                    substring = ":aboom:";
//                } else if (substring.equals("beat_shot")) {
//                    substring = ":shot:";
//                } else if (substring.equals("sweet_kiss")) {
//                    substring = ":*";
//                } else if (substring.equals("beat_shot")) {
//                    substring = ":shot:";
//                } else if (substring.equals("smile")) {
//                    substring = ":)";
//                } else if (substring.equals("still_dreaming")) {
//                    substring = ":dreaming:";
//                } else if (substring.equals("beat_shot")) {
//                    substring = ":shot:";
//                } else if (substring.equals("beat_shot")) {
//                    substring = ":shot:";
//                } else if (substring.equals("bad_smelly")) {
//                    substring = ":badsmell:";
//                } else if (substring.equals("too_sad")) {
//                    substring = ":sosad:";
//                } else if (substring.equals("lay")) {
//                    substring = "^:)^";
//                } else if (substring.equals("sleep")) {
//                    substring = "-_-";
//                } else if (substring.equals("bann")) {
//                    substring = ":ban:";
//                } else {
//                    try {
//                        substring = substring.equals("shit") ? ":shitty:" : ":" + substring + ":";
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                arrayList.add(new containEmo(decodeStream, substring));
//                open.close();
//            }
//        }
//        for (i = 0; i < list.length; i++) {
//            if (list[i].contains(".gif")) {
//                substring = list[i].substring(0, list[i].length() - 4);
//                open = this.mContext.getAssets().open("images/smilies/Off/" + list[i]);
//                decodeStream = BitmapFactory.decodeStream(open);
//                substring = substring.equals("embarrassed") ? ":\">" : substring.equals("nosebleed") ? ":chaymau:" : substring.equals("feel_good") ? ":sogood:" : substring.equals("sexy_girl") ? ":sexy:" : substring.equals("look_down") ? ":look_down:" : substring.equals("beat_brick") ? ":brick:" : substring.equals("cry") ? ":((" : substring.equals("beat_plaster") ? ":plaster:" : substring.equals("confuse") ? ":-s" : substring.equals("big_smile") ? ":D" : substring.equals("brick") ? ":gach:" : substring.equals("burn_joss_stick") ? ":stick:" : substring.equals("bad_smell") ? ":badsmell:" : substring.equals("cool") ? ":kool:" : substring.equals("after_boom") ? ":aboom:" : substring.equals("beat_shot") ? ":shot:" : substring.equals("sweet_kiss") ? ":*" : substring.equals("beat_shot") ? ":shot:" : substring.equals("smile") ? ":)" : substring.equals("still_dreaming") ? ":dreaming:" : substring.equals("beat_shot") ? ":shot:" : substring.equals("beat_shot") ? ":shot:" : substring.equals("bad_smelly") ? ":badsmell:" : substring.equals("too_sad") ? ":sosad:" : substring.equals("lay") ? "^:)^" : substring.equals("sleep") ? "-_-" : substring.equals("bann") ? ":ban:" : substring.equals("shit") ? ":shitty:" : ":" + substring + ":";
//                arrayList.add(new containEmo(decodeStream, substring));
//                open.close();
//            }
//        }
//        String[] list2 = this.mContext.getAssets().list("images/smilies/emos");
//        for (int i2 = 0; i2 < list2.length; i2++) {
//            String substring2 = list2[i2].substring(0, list2[i2].length() - 4);
//            InputStream open2 = this.mContext.getAssets().open("images/smilies/emos/" + list2[i2]);
//            Bitmap decodeStream2 = BitmapFactory.decodeStream(open2);
//            substring2 = substring2.equals("embarrassed") ? ":\">" : substring2.equals("nosebleed") ? ":chaymau:" : ":" + substring2 + ":";
//            arrayList.add(new containEmo(decodeStream2, substring2));
//            open2.close();
//        }
//        InputStream open3 = this.mContext.getAssets().open("+1/button/images/icon.png");
//        arrayList.add(new containEmo(BitmapFactory.decodeStream(open3), ":+1:"));
//        open3.close();
        return arrayList;
    }

    private Dialog alertImage() {
        Dialog dialog = new Dialog(this.mContext);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_insert);
        dialog.getWindow().setLayout(-1, -2);
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) dialog.findViewById(R.id.alert_edit1);
        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.typeInsert);
        Button button = (Button) dialog.findViewById(R.id.alert_ok);
        Button button2 = (Button) dialog.findViewById(R.id.alert_cancle);
        dialog.getWindow().setSoftInputMode(5);
        button.setOnClickListener(new AnonymousClass6(autoCompleteTextView, radioGroup, dialog));
        button2.setOnClickListener(new AnonymousClass7(dialog));
        dialog.show();
        return dialog;
    }

    private Dialog alertLoading() {
        Dialog dialog = Global.bFullScreen ? new Dialog(this.mContext, 16973841) : new Dialog(this.mContext, 16973840);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.loading);
        dialog.getWindow().setLayout(-1, -1);
        ((TextView) dialog.findViewById(R.id.txloadding)).setVisibility(0);
        return dialog;
    }

    private Dialog alertSelectImage() {
        Dialog dialog = new Dialog(this.mContext);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_select_image);
        dialog.getWindow().setLayout(-1, -2);
        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.typeUpload);
        Button button = (Button) dialog.findViewById(R.id.alert_ok);
        Button button2 = (Button) dialog.findViewById(R.id.alert_cancle);
        dialog.getWindow().setSoftInputMode(5);
        button.setOnClickListener(new AnonymousClass8(radioGroup, dialog));
        button2.setOnClickListener(new AnonymousClass9(dialog));
        dialog.show();
        return dialog;
    }

    private String getMesseage() {
        String string = getIntent().getExtras().getString(NotificationCompat.CATEGORY_MESSAGE);
        return this.iPostType == 2 ? string.lastIndexOf(this.sSignature) != -1 ? string.substring(0, string.lastIndexOf(this.sSignature)) : string.lastIndexOf(this.sSignature1) != -1 ? string.substring(0, string.lastIndexOf(this.sSignature1)) : string : string;
    }

    private void initUI() {
        this.mContext = this;
        this.textTitle = (EditText) findViewById(R.id.title);
        this.textTitle.setText(this.sforums);
        this.textTitle.setFocusable(true);
        this.m_TextPost = (EditText) findViewById(R.id.Page3_postReply);
        this.m_ButPost = (Button) findViewById(R.id.Page3_butReply);
        this.m_ButLink = (ImageButton) findViewById(R.id.link_button);
        this.m_CameraButton = (ImageButton) findViewById(R.id.camera_button);
        this.m_CameraButton.setOnClickListener(this.mOnCameraClick);
        this.m_ButPost.setText("Submit");
        this.m_ButPostEmo = (ImageButton) findViewById(R.id.Page3_butReplyEmo);
        this.m_ButPostCancle = (Button) findViewById(R.id.Page3_butReplyCancle);
        this.m_ButPost.setOnClickListener(this.mPostReplyClick);
        this.m_ButPostCancle.setOnClickListener(this.mPostCancleClick);
        this.m_ButPostEmo.setOnClickListener(this.mPostEmoClick);
        this.listEmo = new ArrayList();
        this.listEmo = Emo2();
        this.gridview = (GridView) findViewById(R.id.grid_view);
        this.gridview.setAdapter(new GridAdapter(this.mContext, this.listEmo));
        this.gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                PageNewThread.this.mHandler.obtainMessage(20, ((containEmo) PageNewThread.this.listEmo.get(i)).text()).sendToTarget();
            }
        });
        this.m_ButLink.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PageNewThread.this.alertImage();
            }
        });
        Global.setBackgroundItemThread(this.m_TextPost);
        Global.setTextContain(this.m_TextPost);
        Global.setBackgroundMenuLogo(this.textTitle);
        Global.setTextMenuTitle(this.textTitle);
        if (Global.iTheme == 0) {
            this.gridview.setBackgroundColor(getResources().getColor(R.color.vozThread));
        } else if (Global.iTheme == 1) {
            this.gridview.setBackgroundColor(getResources().getColor(R.color.themeblack_background));
        } else if (Global.iTheme == 2) {
            this.gridview.setBackgroundColor(getResources().getColor(R.color.themewood_background));
        }
        this.mLoading = alertLoading();
    }

    private void log(String str) {
        Log.d("nna", str);
    }

    private void parser() {
        if (this.doc == null) {
            log("doc =null");
        } else if (this.iPostType == 5) {
            this.secToken = this.doc.select("input[name=securitytoken]").attr("value");
            this.postHash = this.doc.select("input[name=posthash]").attr("value");
            this.startTime = this.doc.select("input[name=poststarttime]").attr("value");
            this.mUser.setToken(this.secToken);
        } else if (this.iPostType == 7) {
            this.pmToken = this.doc.select("input[name=securitytoken]").attr("value");
            this.pmMid = this.mParser.getUrl().split("u=")[1];
            if (this.doc.select("div[id=pmrecips]").first() != null) {
                this.pmReciple = this.doc.select("div[id=pmrecips]").first().text();
            }
            this.mUser.setToken(this.pmToken);
        }
    }

    private void toast(String str) {
        Toast.makeText(this, str, 0).show();
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 8 && intent != null) {
            Uri data = intent.getData();
            if (data != null) {
                this.iUpanh++;
                new AnonymousClass11(this.mContext, data, data).execute(new Object[0]);
            }
        }
    }

    protected void onCreate(Bundle bundle) {
        getWindow().requestFeature(1);
        setContentView(R.layout.pagereply);
        super.onCreate(bundle);
        initUI();
        this.mUser = new UserInfo();
        this.sSignature = "\n\n[I]Sent from my " + Global.sYourDevice + " using [URL=\"https://play.google.com/store/apps/details?id=com.nna88.voz.main\"]vozForums[/URL][/I]";
        this.sSignature1 = "\n\n[I]Sent from my " + Global.sYourDevice + " using vozForums[/I]";
        this.iPostType = getIntent().getIntExtra("POSTTYPE", 0);
        if (this.iPostType == 5) {
            this.sforums = getIntent().getStringExtra("FORUM");
            this.mParser = new parser("https://vozforums.com/newthread.php?do=newthread&f=" + this.sforums);
            this.mTask = new Task();
            this.mTask.execute(new Integer[]{Integer.valueOf(0)});
        } else if (this.iPostType == 6) {
            this.mTask = new Task();
            this.mParser = new parser("https://vozforums.com/newthread.php?do=newthread&f=" + this.sforums);
            this.pmToken = getIntent().getStringExtra("TOKEN");
            this.pmMid = getIntent().getStringExtra("MID");
            this.pmUserId = getIntent().getStringExtra("USERID");
            this.pmReciple = getIntent().getStringExtra("RECIPLIES");
            this.pmTitle = getIntent().getStringExtra(MySQLiteHelper.COLUMN_TITLE);
            this.textTitle.setText(getIntent().getExtras().getString(MySQLiteHelper.COLUMN_TITLE));
            this.textTitle.setEnabled(false);
            this.m_TextPost.setText(getMesseage());
            this.m_TextPost.setSelection(this.m_TextPost.getText().length());
            this.m_TextPost.requestFocus();
            this.m_TextPost.setFocusable(true);
        } else if (this.iPostType == 7) {
            this.mTask = new Task();
            this.pmMid = getIntent().getStringExtra("MID");
            this.mParser = new parser("https://vozforums.com/private.php?do=newpm&u=" + this.pmMid);
            this.mTask.execute(new Integer[]{Integer.valueOf(0)});
        } else {
            this.textTitle.setText(getIntent().getExtras().getString(MySQLiteHelper.COLUMN_TITLE));
            this.textTitle.setEnabled(false);
            this.m_TextPost.setText(getMesseage());
            this.m_TextPost.setSelection(this.m_TextPost.getText().length());
            this.m_TextPost.requestFocus();
            this.m_TextPost.setFocusable(true);
        }
    }

    protected void onStop() {
        super.onStop();
    }

    protected void write(String str) {
        try {
            FileUtils.writeStringToFile(new File("/mnt/sdcard/test.html"), str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
