package com.nna88.voz.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nna88.voz.PhotoView.PhotoViewAttacher;
import com.nna88.voz.colorpicker.ColorPicker;
import com.nna88.voz.colorpicker.ColorPicker.OnColorChangedListener;
import com.nna88.voz.colorpicker.OpacityBar;
import com.nna88.voz.colorpicker.SVBar;
import com.nna88.voz.listview.SettingAdapter;
import com.nna88.voz.listview.SettingAdapter.OnActionItemClickListener;
import com.nna88.voz.listview.WebkitCookieManagerProxy;
import com.nna88.voz.listview.listViewCustom1;
import com.nna88.voz.listview.listViewCustom2;
import com.nna88.voz.listview.listViewCustom3;
import com.nna88.voz.listview.listViewCustom3Html;
import com.nna88.voz.mysqlite.CommentsDataSource;
import com.nna88.voz.parserhtml.parser;
import com.nna88.voz.ui.ExpandableHeightGridView;
import com.nna88.voz.ui.NavigationDrawerActivity;
import com.nna88.voz.ui.SidebarAdapter;
import com.nna88.voz.ui.SidebarAdapter.Category;
import com.nna88.voz.ui.SidebarAdapter.Item;
import com.nna88.voz.ui.UtilLayout;
import com.nna88.voz.util.ChangeLog;
import com.nna88.voz.util.Help;
import com.nna88.voz.util.ImageLoad;
import com.nna88.voz.util.UserInfo;
import com.nna88.voz.util.Util;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.conn.routing.HttpRouteDirector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Page extends NavigationDrawerActivity {
    protected static final int STATE_OFFSCREEN = 1;
    protected static final int STATE_ONSCREEN = 0;
    protected static final int STATE_RETURNING = 2;
    protected TranslateAnimation anim;
    protected TextView butPageFooter;
    protected TextView butPageHeader;
    protected ChangeLog cl;
    protected Help clHelp;
    protected Document doc;
    protected Document doc2;
    protected int iPage;
    protected int iPositiion;
    protected boolean isSubscribe;
    protected ArrayList<String> lIdForum;
    protected LinearLayout linearFooter;
    protected LinearLayout linearHeader;
    protected ArrayList<String> lsForum;
    protected SidebarAdapter mAdapterSideMenu1;
    protected SidebarAdapter mAdapterSideMenu2;
    protected ArrayList<String> mArrayPass;
    protected ArrayList<String> mArrayUsered;
    protected ArrayList<String> mArrayUseredId;
    protected int mColorText2;
    protected Context mContext;
    protected CommentsDataSource mDataBookmark;
    protected ImageLoad mImageLoad;
    protected ImageView mImg1Footer;
    protected ImageView mImg1Header;
    protected ImageView mImg2Footer;
    protected ImageView mImg2Header;
    protected ImageView mImg3Footer;
    protected ImageView mImg3Header;
    protected ImageView mImg4Footer;
    protected ImageView mImg4Header;
    protected int mItemCount;
    protected int[] mItemOffsetY;
    protected int[] mItemtemp;
//    protected RelativeLayout mLayoutAds;
    private LinearLayout mLayoutProgress;
    protected ListView mList;
    protected ArrayList<Object> mListSideMenu1;
    protected ArrayList<Object> mListSideMenu2;
    protected int mMinRawY;
    protected Object mObjectAdapter;
    protected parser mParser;
    private String mPassword;
    protected int mQuickReturnHeight;
    protected LinearLayout mQuickReturnLayout;
    protected String mRadioUser;
    protected int mScrollY;
    protected int mState;
    protected TaskGetHtml mTask;
    protected float mTextSize;
    protected String mTitle;
    protected UserInfo mUser;
    private String mUsername;
    protected Vibrator myVib;
    protected String sForum;
    protected String sIdThread;
    protected String sMessSearch;
    protected String sPost;
    protected String sSearch_ShowPost;
    protected boolean scrollIsComputed;
    protected SharedPreferences settings;
    protected String specifiedpost;
    protected TextView textTitle;

    /* renamed from: com.nna88.voz.main.Page.12 */
    class AnonymousClass12 implements OnClickListener {
        final /* synthetic */ EditText val$edit1;
        final /* synthetic */ TextView val$tx;
        final /* synthetic */ int val$value;

        AnonymousClass12(EditText editText, int i, TextView textView) {
            this.val$edit1 = editText;
            this.val$value = i;
            this.val$tx = textView;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
//            Global.iHome = Integer.parseInt(this.val$edit1.getText().toString());
//            Page.this.writeSetHome(Global.iHome);
//            CharSequence charSequence = "f=" + this.val$edit1.getText().toString();
//            Page.this.writeQuickreturn(this.val$value, charSequence);
//            this.val$tx.setText(charSequence);
        }
    }

    /* renamed from: com.nna88.voz.main.Page.14 */
    class AnonymousClass14 implements OnClickListener {
        final /* synthetic */ EditText val$editText;
        final /* synthetic */ TextView val$txtHome;

        AnonymousClass14(EditText editText, TextView textView) {
            this.val$editText = editText;
            this.val$txtHome = textView;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            try {
                Global.iHome = Integer.parseInt(this.val$editText.getText().toString());
            } catch (Exception e) {
                Global.iHome = Page.STATE_ONSCREEN;
            }
            this.val$txtHome.setText(BuildConfig.FLAVOR + Global.iHome);
            Page.this.writeSetHome(Global.iHome);
        }
    }

    /* renamed from: com.nna88.voz.main.Page.16 */
    class AnonymousClass16 implements OnLongClickListener {
        final /* synthetic */ int val$finalI;

        AnonymousClass16(int i) {
            this.val$finalI = i;
        }

        public boolean onLongClick(View view) {
            Page.this.alertSetQuickmenu((TextView) view, this.val$finalI);
            return false;
        }
    }

    /* renamed from: com.nna88.voz.main.Page.17 */
    class AnonymousClass17 implements View.OnClickListener {
        final /* synthetic */ TextView val$finalTx;

        AnonymousClass17(TextView textView) {
            this.val$finalTx = textView;
        }

        public void onClick(View view) {
            String charSequence = this.val$finalTx.getText().toString();
            if (charSequence.equals("f=0")) {
                Intent intent = new Intent(Page.this.mContext, Page1.class);
                intent.setData(Uri.parse("https://vozforums.com"));
                Page.this.startActivity(intent);
                return;
            }
            Intent intent2 = new Intent(Page.this.mContext, Page2.class);
            intent2.putExtra("URL", "forumdisplay.php?" + charSequence);
            intent2.putExtra("TITLE", charSequence);
            Page.this.startActivity(intent2);
        }
    }

    /* renamed from: com.nna88.voz.main.Page.22 */
    class AnonymousClass22 implements OnClickListener {
        final /* synthetic */ EditText val$editText;
        final /* synthetic */ TextView val$textSign;

        AnonymousClass22(EditText editText, TextView textView) {
            this.val$editText = editText;
            this.val$textSign = textView;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            Global.sYourDevice = this.val$editText.getText().toString();
            this.val$textSign.setText(Global.sYourDevice);
            Page.this.writeSetYourDevices(Global.sYourDevice);
        }
    }

    /* renamed from: com.nna88.voz.main.Page.26 */
    class AnonymousClass26 implements OnColorChangedListener {
        final /* synthetic */ EditText val$editHexColor;
        final /* synthetic */ int val$id;

        AnonymousClass26(EditText editText, int i) {
            this.val$editHexColor = editText;
            this.val$id = i;
        }

        public void onColorChanged(int i) {
            this.val$editHexColor.setText(String.valueOf(Integer.toHexString(i)));
            if (this.val$id == Page.STATE_RETURNING) {
                Global.mCusThemeTitleBg = i;
                Global.themeColor[8][Page.STATE_RETURNING] = i;
            } else if (this.val$id == 0) {
                Global.mCusThemeBg = i;
                Global.themeColor[8][Page.STATE_ONSCREEN] = i;
            } else if (this.val$id == Page.STATE_OFFSCREEN) {
                Global.mCusThemeBgFocus = i;
            } else if (this.val$id == 4) {
                Global.mCusThemeTxt1 = i;
                Global.themeColor[8][4] = i;
            } else if (this.val$id == 5) {
                Global.mCusThemeTxt2 = i;
                Global.themeColor[8][5] = i;
            } else if (this.val$id == 3) {
                Global.mCusThemeTxtTitle = i;
                Global.themeColor[8][3] = i;
            } else if (this.val$id == 6) {
                Global.mCusThemeQuicklink = i;
                Global.themeColor[8][6] = i;
            }
            Page.this.UpdateUI();
            Page.this.writeSetCusTheme();
        }
    }

    /* renamed from: com.nna88.voz.main.Page.28 */
    class AnonymousClass28 implements OnClickListener {
        final /* synthetic */ String[] val$stringTheme;
        final /* synthetic */ TextView val$textTheme;

        AnonymousClass28(TextView textView, String[] strArr) {
            this.val$textTheme = textView;
            this.val$stringTheme = strArr;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            Global.iTheme = i;
            if (this.val$textTheme != null) {
                Page.this.writeSetTheme(Global.iTheme);
            }
            this.val$textTheme.setText(this.val$stringTheme[i]);
            Page.this.UpdateUI();
            if (Global.iTheme == 8) {
                Page.this.readCustomTheme();
                Page.this.alertSeCustomTheme();
            }
            dialogInterface.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.Page.29 */
    class AnonymousClass29 implements OnCheckedChangeListener {
        final /* synthetic */ RadioGroup val$radioSizeImage;

        AnonymousClass29(RadioGroup radioGroup) {
            this.val$radioSizeImage = radioGroup;
        }

        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if (this.val$radioSizeImage.getCheckedRadioButtonId() == R.id.typeSizeImage_1) {
                Global.iSizeImage = Page.STATE_OFFSCREEN;
            } else if (this.val$radioSizeImage.getCheckedRadioButtonId() == R.id.typeSizeImage_2) {
                Global.iSizeImage = Page.STATE_RETURNING;
            } else if (this.val$radioSizeImage.getCheckedRadioButtonId() == R.id.typeSizeImage_4) {
                Global.iSizeImage = 4;
            } else if (this.val$radioSizeImage.getCheckedRadioButtonId() == R.id.typeSizeImage_8) {
                Global.iSizeImage = 8;
            } else if (this.val$radioSizeImage.getCheckedRadioButtonId() == R.id.typeSizeImage_none) {
                Global.iSizeImage = Page.STATE_ONSCREEN;
            }
            Page.this.writeSetImageSize(Global.iSizeImage);
        }
    }

    /* renamed from: com.nna88.voz.main.Page.30 */
    class AnonymousClass30 implements OnCheckedChangeListener {
        final /* synthetic */ RadioGroup val$radioType;

        AnonymousClass30(RadioGroup radioGroup) {
            this.val$radioType = radioGroup;
        }

        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if (this.val$radioType.getCheckedRadioButtonId() == R.id.showpost) {
                Page.this.sSearch_ShowPost = "1";
                return;
            }
            Page.this.sSearch_ShowPost = "0";
        }
    }

    /* renamed from: com.nna88.voz.main.Page.31 */
    class AnonymousClass31 implements View.OnClickListener {
        final /* synthetic */ Button val$butAdvanceSearch;
        final /* synthetic */ EditText val$edit1;

        /* renamed from: com.nna88.voz.main.Page.31.1 */
        class AnonymousClass1 implements OnItemClickListener {
            final /* synthetic */ Dialog val$dialog1;

            AnonymousClass1(Dialog dialog) {
                this.val$dialog1 = dialog;
            }

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                AnonymousClass31.this.val$butAdvanceSearch.setText("Advance Search \nSearch in forums:" + ((String) Page.this.lsForum.get(i)));
                if (!(Page.this.doc == null || Page.this.doc.select("input[name=securitytoken]").first() == null)) {
                    Page.this.mUser.setToken(Page.this.doc.select("input[name=securitytoken]").first().attr("value"));
                }
                Page.this.sForum = (String) Page.this.lIdForum.get(i);
                Page.this.sMessSearch = AnonymousClass31.this.val$edit1.getText().toString();
                Intent intent = new Intent(Page.this.mContext, PageSearch.class);
                intent.putExtra("MESS", Page.this.sMessSearch);
                intent.putExtra("FORUM", Page.this.sForum);
                intent.putExtra("SHOWPOST", Page.this.sSearch_ShowPost);
                Page.this.startActivity(intent);
                this.val$dialog1.dismiss();
            }
        }

        AnonymousClass31(Button button, EditText editText) {
            this.val$butAdvanceSearch = button;
            this.val$edit1 = editText;
        }

        public void onClick(View view) {
            Page.this.readStringForum();
            Dialog dialog = new Dialog(Page.this.mContext);
            dialog.requestWindowFeature(Page.STATE_OFFSCREEN);
            dialog.setContentView(R.layout.listforum);
            dialog.getWindow().setLayout(-1, -2);
            ListView listView = (ListView) dialog.findViewById(R.id.list);
//            listView.setAdapter(new ArrayAdapter(Page.this.mContext, 17367043, Page.this.lsForum));
            listView.setOnItemClickListener(new AnonymousClass1(dialog));
            dialog.show();
        }
    }

    /* renamed from: com.nna88.voz.main.Page.32 */
    class AnonymousClass32 implements View.OnClickListener {
        final /* synthetic */ Dialog val$dialog;

        AnonymousClass32(Dialog dialog) {
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            Page.this.startActivity(new Intent(Page.this.mContext, PageRecentThread.class));
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.Page.33 */
    class AnonymousClass33 implements View.OnClickListener {
        final /* synthetic */ Dialog val$dialog;

        AnonymousClass33(Dialog dialog) {
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            Page.this.startActivity(new Intent(Page.this.mContext, PageRecentPost.class));
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.Page.34 */
    class AnonymousClass34 implements View.OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ EditText val$edit1;

        AnonymousClass34(EditText editText, Dialog dialog) {
            this.val$edit1 = editText;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            Intent intent = new Intent(Page.this.mContext, PageSearch.class);
            Page.this.sMessSearch = this.val$edit1.getText().toString();
            intent.putExtra("MESS", Page.this.sMessSearch);
            intent.putExtra("SHOWPOST", Page.this.sSearch_ShowPost);
            Page.this.startActivity(intent);
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.Page.35 */
    class AnonymousClass35 implements View.OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ AutoCompleteTextView val$edit1;
        final /* synthetic */ RadioGroup val$radio;

        AnonymousClass35(AutoCompleteTextView autoCompleteTextView, RadioGroup radioGroup, Dialog dialog) {
            this.val$edit1 = autoCompleteTextView;
            this.val$radio = radioGroup;
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            String obj = this.val$edit1.getText().toString();
            Intent intent;
            if (this.val$radio.getCheckedRadioButtonId() == R.id.address_forum) {
                intent = new Intent(Page.this.mContext, Page2.class);
                intent.putExtra("URL", "forumdisplay.php?f=" + obj);
                intent.putExtra("TITLE", obj);
                Page.this.startActivity(intent);
            } else if (this.val$radio.getCheckedRadioButtonId() == R.id.address_thread) {
                intent = new Intent(Page.this.mContext, Page3.class);
                intent.putExtra("URL", "showthread.php?t=" + obj);
                intent.putExtra("TITLE", obj);
                Page.this.startActivity(intent);
            } else if (this.val$radio.getCheckedRadioButtonId() == R.id.address_post) {
                intent = new Intent(Page.this.mContext, Page3.class);
                intent.putExtra("URL", "showpost.php?p=" + obj);
                intent.putExtra("TITLE", obj);
                Page.this.startActivity(intent);
            } else if (this.val$radio.getCheckedRadioButtonId() == R.id.address_home) {
                Intent intent2 = new Intent(Page.this.mContext, Page1.class);
                intent2.setData(Uri.parse("https://vozforums.com"));
                Page.this.startActivity(intent2);
            }
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.Page.36 */
    class AnonymousClass36 implements View.OnClickListener {
        final /* synthetic */ Dialog val$dialog;

        AnonymousClass36(Dialog dialog) {
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.Page.38 */
    class AnonymousClass38 implements OnClickListener {
        final /* synthetic */ EditText val$editText;
        final /* synthetic */ TextView val$textPath;

        AnonymousClass38(EditText editText, TextView textView) {
            this.val$editText = editText;
            this.val$textPath = textView;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            Global.mSavePath = this.val$editText.getText().toString();
            this.val$textPath.setText(Global.mSavePath);
            Page.this.writeSetPathSave(Global.mSavePath);
        }
    }

    /* renamed from: com.nna88.voz.main.Page.40 */
    class AnonymousClass40 implements OnClickListener {
        final /* synthetic */ EditText val$editText;
        final /* synthetic */ TextView val$textQuickLink;

        AnonymousClass40(EditText editText, TextView textView) {
            this.val$editText = editText;
            this.val$textQuickLink = textView;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            Global.iNumQuickLink = this.val$editText.getText().toString();
            this.val$textQuickLink.setText(Global.iNumQuickLink);
            Page.this.writeSetNumQuickLink(Global.iNumQuickLink);
            Page.this.initQuickReturn();
        }
    }

    /* renamed from: com.nna88.voz.main.Page.42 */
    class AnonymousClass42 implements OnSeekBarChangeListener {
        final /* synthetic */ TextView val$text1;

        AnonymousClass42(TextView textView) {
            this.val$text1 = textView;
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            this.val$text1.setText(String.valueOf(i));
            Page.this.mTextSize = (float) ((0.092d * ((double) i)) + Global.FONTSCALE_MIN);
            Page.this.updateSetFont(Page.this.mTextSize);
            Global.iSize = Page.this.mTextSize;
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            Page.this.writeSetFont(Page.this.mTextSize);
        }
    }

    /* renamed from: com.nna88.voz.main.Page.44 */
    class AnonymousClass44 implements View.OnClickListener {
        final /* synthetic */ TextView val$textTheme;

        AnonymousClass44(TextView textView) {
            this.val$textTheme = textView;
        }

        public void onClick(View view) {
            Page.this.alertSetTheme(this.val$textTheme);
        }
    }

    /* renamed from: com.nna88.voz.main.Page.45 */
    class AnonymousClass45 implements View.OnClickListener {
        final /* synthetic */ TextView val$textSign;

        AnonymousClass45(TextView textView) {
            this.val$textSign = textView;
        }

        public void onClick(View view) {
            Page.this.alertSetSign(this.val$textSign);
        }
    }

    /* renamed from: com.nna88.voz.main.Page.46 */
    class AnonymousClass46 implements View.OnClickListener {
        final /* synthetic */ TextView val$textHome;

        AnonymousClass46(TextView textView) {
            this.val$textHome = textView;
        }

        public void onClick(View view) {
            Page.this.alertSetHome(this.val$textHome);
        }
    }

    /* renamed from: com.nna88.voz.main.Page.47 */
    class AnonymousClass47 implements View.OnClickListener {
        final /* synthetic */ TextView val$textQuickLink;

        AnonymousClass47(TextView textView) {
            this.val$textQuickLink = textView;
        }

        public void onClick(View view) {
            Page.this.alertSetQuickLink(this.val$textQuickLink);
        }
    }

    /* renamed from: com.nna88.voz.main.Page.48 */
    class AnonymousClass48 implements View.OnClickListener {
        final /* synthetic */ TextView val$textPathSave;

        AnonymousClass48(TextView textView) {
            this.val$textPathSave = textView;
        }

        public void onClick(View view) {
            Page.this.alertSetPathSave(this.val$textPathSave);
        }
    }

    /* renamed from: com.nna88.voz.main.Page.49 */
    class AnonymousClass49 implements View.OnClickListener {
        final /* synthetic */ EditText val$edit1;
        final /* synthetic */ Dialog val$mDialog;

        AnonymousClass49(EditText editText, Dialog dialog) {
            this.val$edit1 = editText;
            this.val$mDialog = dialog;
        }

        public void onClick(View view) {
            try {
                Page.this.GoPage(Page.STATE_OFFSCREEN, Integer.parseInt(this.val$edit1.getText().toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.val$mDialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.Page.50 */
    class AnonymousClass50 implements View.OnClickListener {
        final /* synthetic */ Dialog val$mDialog;

        AnonymousClass50(Dialog dialog) {
            this.val$mDialog = dialog;
        }

        public void onClick(View view) {
            this.val$mDialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.Page.51 */
    class AnonymousClass51 implements View.OnClickListener {
        final /* synthetic */ Dialog val$dialog;

        AnonymousClass51(Dialog dialog) {
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            Page.this.TaskCancle();
            Page.this.mTask = new TaskGetHtml();
            TaskGetHtml taskGetHtml = Page.this.mTask;
            Integer[] numArr = new Integer[Page.STATE_OFFSCREEN];
            numArr[Page.STATE_ONSCREEN] = Integer.valueOf(Page.STATE_ONSCREEN);
            taskGetHtml.execute(numArr);
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.Page.52 */
    class AnonymousClass52 implements View.OnClickListener {
        final /* synthetic */ Dialog val$dialog;

        AnonymousClass52(Dialog dialog) {
            this.val$dialog = dialog;
        }

        public void onClick(View view) {
            this.val$dialog.dismiss();
            Page.this.onBackPressed();
        }
    }

    /* renamed from: com.nna88.voz.main.Page.53 */
    class AnonymousClass53 implements View.OnClickListener {
        final /* synthetic */ Button val$buttonCancle;
        final /* synthetic */ Button val$buttonOK;
        final /* synthetic */ EditText val$edit1;
        final /* synthetic */ EditText val$edit2;
        final /* synthetic */ TextView val$text1;
        final /* synthetic */ TextView val$text2;

        AnonymousClass53(EditText editText, EditText editText2, TextView textView, TextView textView2, Button button, Button button2) {
            this.val$edit1 = editText;
            this.val$edit2 = editText2;
            this.val$text1 = textView;
            this.val$text2 = textView2;
            this.val$buttonOK = button;
            this.val$buttonCancle = button2;
        }

        public void onClick(View view) {
            Page.this.mRadioUser = ((Button) view).getText().toString();
            this.val$edit1.setVisibility(View.GONE);
            this.val$edit2.setVisibility(View.GONE);
            this.val$text1.setVisibility(View.GONE);
            this.val$text2.setVisibility(View.GONE);
            Page.this.mUsername = null;
            Page.this.mPassword = null;
            if (Page.this.mRadioUser.equals(Page.this.mUser.User())) {
                this.val$buttonOK.setText("Log out");
                this.val$buttonCancle.setText("Cancel");
                return;
            }
            this.val$buttonOK.setText("Log in");
            this.val$buttonCancle.setText("Clear");
        }
    }

    /* renamed from: com.nna88.voz.main.Page.54 */
    class AnonymousClass54 implements View.OnClickListener {
        final /* synthetic */ Button val$buttonCancle;
        final /* synthetic */ Button val$buttonOK;
        final /* synthetic */ EditText val$edit1;
        final /* synthetic */ EditText val$edit2;
        final /* synthetic */ TextView val$text1;
        final /* synthetic */ TextView val$text2;

        AnonymousClass54(EditText editText, EditText editText2, TextView textView, TextView textView2, Button button, Button button2) {
            this.val$edit1 = editText;
            this.val$edit2 = editText2;
            this.val$text2 = textView;
            this.val$text1 = textView2;
            this.val$buttonOK = button;
            this.val$buttonCancle = button2;
        }

        public void onClick(View view) {
            this.val$edit1.setVisibility(View.VISIBLE);
            this.val$edit2.setVisibility(View.VISIBLE);
            this.val$text2.setVisibility(View.VISIBLE);
            this.val$text1.setVisibility(View.VISIBLE);
            Page.this.mRadioUser = ((Button) view).getText().toString();
            this.val$buttonOK.setText("Log in");
            this.val$buttonCancle.setText("Cancel");
        }
    }

    /* renamed from: com.nna88.voz.main.Page.55 */
    class AnonymousClass55 implements View.OnClickListener {
        final /* synthetic */ EditText val$edit1;
        final /* synthetic */ EditText val$edit2;
        final /* synthetic */ Dialog val$mDialog;

        AnonymousClass55(EditText editText, EditText editText2, Dialog dialog) {
            this.val$edit1 = editText;
            this.val$edit2 = editText2;
            this.val$mDialog = dialog;
        }

        public void onClick(View view) {
            Page.this.TaskCancle();
            Page.this.mTask = new TaskGetHtml();
            TaskGetHtml taskGetHtml;
            Integer[] numArr;
            if (Page.this.mRadioUser.equals("Add")) {
                Page.this.mUsername = this.val$edit1.getText().toString();
                Page.this.mPassword = this.val$edit2.getText().toString();
                Page.this.mUser.add(Page.this.mUsername, Page.this.mPassword);
                taskGetHtml = Page.this.mTask;
                numArr = new Integer[Page.STATE_OFFSCREEN];
                numArr[Page.STATE_ONSCREEN] = Integer.valueOf(Page.STATE_OFFSCREEN);
                taskGetHtml.execute(numArr);
            } else if (Page.this.mRadioUser == null || Page.this.mRadioUser.equals(Page.this.mUser.User())) {
                taskGetHtml = Page.this.mTask;
                numArr = new Integer[Page.STATE_OFFSCREEN];
                numArr[Page.STATE_ONSCREEN] = Integer.valueOf(Page.STATE_RETURNING);
                taskGetHtml.execute(numArr);
            } else {
                Page.this.mUser.add(Page.this.mRadioUser, (String) Page.this.mArrayPass.get(Page.this.mArrayUsered.indexOf(Page.this.mRadioUser)));
                taskGetHtml = Page.this.mTask;
                numArr = new Integer[Page.STATE_OFFSCREEN];
                numArr[Page.STATE_ONSCREEN] = Integer.valueOf(Page.STATE_OFFSCREEN);
                taskGetHtml.execute(numArr);
            }
            this.val$mDialog.dismiss();
        }
    }

    /* renamed from: com.nna88.voz.main.Page.56 */
    class AnonymousClass56 implements View.OnClickListener {
        final /* synthetic */ Button val$buttonCancle;
        final /* synthetic */ Dialog val$mDialog;

        AnonymousClass56(Button button, Dialog dialog) {
            this.val$buttonCancle = button;
            this.val$mDialog = dialog;
        }

        public void onClick(View view) {
            if (this.val$buttonCancle.getText().toString().equals("Clear")) {
                Editor edit = Page.this.settings.edit();
                String str = (String) Page.this.mArrayUseredId.get(Page.this.mArrayUsered.indexOf(Page.this.mRadioUser));
                edit.remove("username-" + str);
                edit.remove("userid-" + str);
                edit.remove("password-" + str);
                str = Page.this.settings.getString("usered", null).replace(str, BuildConfig.FLAVOR);
                if (str.startsWith(";")) {
                    str = str.substring(Page.STATE_OFFSCREEN, str.length());
                } else if (str.endsWith(";")) {
                    str = str.substring(Page.STATE_ONSCREEN, str.length() - 1);
                } else if (str.contains(";;")) {
                    str = str.replace(";;", ";");
                }
                edit.putString("usered", str);
                edit.commit();
            }
            this.val$mDialog.dismiss();
        }
    }

    class TaskGetHtml extends AsyncTask<Integer, Void, Integer> {
        TaskGetHtml() {
        }

        protected Integer doInBackground(Integer... numArr) {
            Element element;
            Element first;
            Element first2;
            String attr;
            String attr2;
            switch (numArr[Page.STATE_ONSCREEN].intValue()) {
                case Page.STATE_ONSCREEN /*0*/:
                    if (!Global.OFFILNE.booleanValue()) {
                        Page.this.doc = Page.this.mParser.getDoc();
                        break;
                    }
                    try {
                        Page.this.doc = Jsoup.parse(FileUtils.readFileToString(new File("/mnt/sdcard/paga3.html"), AsyncHttpResponseHandler.DEFAULT_CHARSET));
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                case Page.STATE_OFFSCREEN /*1*/:
                    Page.this.doc = Page.this.mParser.login(Page.this.mUser);
                    break;
                case Page.STATE_RETURNING /*2*/:
                    Page.this.doc = Page.this.mParser.Logout();
                    break;
                case HttpRouteDirector.TUNNEL_TARGET /*3*/:
                    Page.this.doc = Page.this.mParser.quoteGet(numArr[Page.STATE_OFFSCREEN].intValue(), numArr[Page.STATE_RETURNING].intValue());
                    break;
                case HttpRouteDirector.TUNNEL_PROXY /*4*/:
                    try {
                        Page.this.doc = Page.this.mParser.PostReplyQuote(Page.this.doc2, Page.this.mUser, Page.this.sPost, Page.this.specifiedpost);
                        break;
                    } catch (Exception e2) {
                        break;
                    }
                case HttpRouteDirector.LAYER_PROTOCOL /*5*/:
                    try {
                        element = (Element) Page.this.doc2.select("div[align=center]").get(Page.STATE_OFFSCREEN);
                        first = element.select("input[name*=securitytoken]").first();
                        first2 = element.select("input[name*=posthash]").first();
                        Element first3 = element.select("input[name*=poststarttime]").first();
                        Element first4 = element.select("input[name=p]").first();
                        element = element.select("input[name=title]").first();
                        attr = first != null ? first.attr("value") : null;
                        String attr3 = first2 != null ? first2.attr("value") : null;
                        String attr4 = first3 != null ? first3.attr("value") : null;
                        attr2 = first4 != null ? first4.attr("value") : null;
                        Page.this.doc = Page.this.mParser.PostEditReply(Page.STATE_ONSCREEN, Page.this.sPost, "editpost.php?do=updatepost&p=" + attr2, element != null ? element.attr("value") : null, attr, attr2, attr3, attr4);
                        break;
                    } catch (Exception e3) {
                        break;
                    }
                case R.styleable.Toolbar_contentInsetLeft /*6*/:
                    try {
                        element = (Element) Page.this.doc2.select("div[align=center]").get(Page.STATE_OFFSCREEN);
                        first = element.select("input[name*=securitytoken]").first();
                        first2 = element.select("input[name=p]").first();
                        element = element.select("input[name=title]").first();
                        attr = first != null ? first.attr("value") : null;
                        attr2 = first2 != null ? first2.attr("value") : null;
                        Page.this.doc = Page.this.mParser.PostEditReply(Page.STATE_OFFSCREEN, Page.this.sPost, "editpost.php?do=deletepost&p=" + attr2, element != null ? element.attr("value") : null, attr, attr2, null, null);
                        break;
                    } catch (Exception e4) {
                        break;
                    }
                case R.styleable.Toolbar_contentInsetRight /*7*/:
                    element = Page.this.doc.select("input[name*=securitytoken]").first();
                    Page.this.doc = Page.this.mParser.Subscribe(Page.this.sIdThread, element != null ? element.attr("value") : null);
                    break;
                case R.styleable.Toolbar_popupTheme /*8*/:
                    Page.this.doc = Page.this.mParser.UnSubscribe(Page.this.sIdThread);
                    break;
//                case MetaData.DEFAULT_MAX_ADS /*10*/:
//                    if (!(Page.this.doc == null || Page.this.doc.select("input[name=securitytoken]").first() == null)) {
//                        Page.this.mUser.setToken(Page.this.doc.select("input[name=securitytoken]").first().attr("value"));
//                    }
//                    Page.this.doc = Page.this.mParser.SearchQuote();
//                    break;
                case R.styleable.Toolbar_titleMargins /*11*/:
                    Page.this.doc = Page.this.mParser.myRecentPost();
                    break;
                case R.styleable.Toolbar_titleMarginStart /*12*/:
                    Page.this.doc = Page.this.mParser.myRecentThread();
                    break;
                case R.styleable.Toolbar_titleMarginEnd /*13*/:
                    Page.this.doc = Page.this.mParser.AdvanceSearch(Page.this.mUser.Token(), Page.this.sMessSearch, Page.this.sForum, Page.this.sSearch_ShowPost);
                    break;
                case R.styleable.Toolbar_titleMarginTop /*14*/:
                    Page.this.doc = Page.this.mParser.Search(Page.this.mUser.Token(), Page.this.sMessSearch, Page.this.sSearch_ShowPost);
                    break;
                case R.styleable.Toolbar_titleMarginBottom /*15*/:
                    Page.this.doc = Page.this.mParser.getPM();
                    break;
            }
            return numArr[Page.STATE_ONSCREEN];
        }

        protected void onPostExecute(Integer num) {
            try {
                Page.this.setProgress(false);
                Page.this.Parser(num.intValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
            switch (num.intValue()) {
                case Page.STATE_OFFSCREEN /*1*/:
                    if (Page.this.doc != null) {
                        Page.this.writeSetingUser(Page.this.mUser.cookies());
                        Page.this.mUser.setLogin(true);
                        ((Item) Page.this.mListSideMenu2.get(Page.STATE_ONSCREEN)).mTitle = Page.this.mUser.User();
                        Page.this.mUser.setAvatart(null);
                        Page.this.mAdapterSideMenu2.notifyDataSetInvalidated();
                        break;
                    }
                    Page.this.toast("T\u00ean \u0111\u0103ng nh\u1eadp ho\u1eb7c m\u1eadt kh\u1ea9u sai!");
                    Page.this.alertLogin();
                    break;
                case Page.STATE_RETURNING /*2*/:
                    UserInfo userInfo = Page.this.mUser;
                    UserInfo.mCookieStore = null;
                    Page.this.mUser.cookies(null);
                    Page.this.mUser.setLogin(false);
                    Page.this.mUser.setAvatart(null);
                    Page.this.mUser.SetUser(null);
                    ((Item) Page.this.mListSideMenu2.get(Page.STATE_ONSCREEN)).mTitle = Page.this.getResources().getString(R.string.Login);
                    Page.this.mAdapterSideMenu2.notifyDataSetInvalidated();
                    Page.this.writeSetingUser(Page.this.mUser.cookies());
                    break;
            }
            super.onPostExecute(num);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            Page.this.setProgress(true);
        }
    }

    private class getAvatar extends AsyncTask<Void, Void, String> {
        private getAvatar() {
        }

        protected String doInBackground(Void... voidArr) {
            String str = null;
            if (Page.this.mUser.cookies() != null) {
                try {
                    Element first = Jsoup.connect("https://vozforums.com/profile.php?do=editavatar").cookies(Page.this.mUser.cookies()).get().select("img[alt=Custom Avatar]").first();
                    if (first != null) {
                        str = Global.URL + first.attr("src");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return str;
        }

        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            if (str != null) {
                Page.this.mImageLoad.imageLoader.loadImage(str, Page.this.mImageLoad.options, new ImageLoadingListener() {
                    public void onLoadingCancelled(String str, View view) {
                    }

                    public void onLoadingComplete(String str, View view, Bitmap bitmap) {
                        Page.this.mUser.setAvatart(bitmap);
                        ((Item) Page.this.mListSideMenu2.get(Page.STATE_ONSCREEN)).mIcon = new BitmapDrawable(Page.this.getResources(), bitmap);
                        Page.this.mAdapterSideMenu2.notifyDataSetChanged();
                    }

                    public void onLoadingFailed(String str, View view, FailReason failReason) {
                    }

                    public void onLoadingStarted(String str, View view) {
                    }
                });
            }
        }
    }

    public Page() {
        this.iPage = STATE_ONSCREEN;
        this.isSubscribe = false;
        this.sSearch_ShowPost = "0";
        this.scrollIsComputed = false;
        this.mState = STATE_ONSCREEN;
        this.mMinRawY = STATE_ONSCREEN;
        this.iPositiion = STATE_ONSCREEN;
        this.mArrayUsered = new ArrayList();
        this.mArrayPass = new ArrayList();
        this.mArrayUseredId = new ArrayList();
        this.specifiedpost = "0";
    }

    private void UpdateUI() {
        this.mList.requestLayout();
        if (Global.bDevider) {
            UtilLayout.setMargin(this.mContext, this.mList, 6, STATE_ONSCREEN, 6, STATE_ONSCREEN);
            this.mList.setDividerHeight(Util.convertDpToPx(this.mContext, 6));
        } else {
            UtilLayout.setMargin(this.mContext, this.mList, STATE_OFFSCREEN, STATE_ONSCREEN, STATE_OFFSCREEN, STATE_ONSCREEN);
            this.mList.setDividerHeight(Util.convertDpToPx(this.mContext, STATE_OFFSCREEN));
        }
        Global.setBackgoundMain(getmDrawerLayout());
//        Global.setBackgroundItemThread(this.mLayoutAds);
        Global.setBackgroundItemThread(this.mQuickReturnLayout);
        Global.setBackgoundMain(this.mList);
        getSupportActionBar().setBackgroundDrawable(getColorBackground(Global.themeColor[Global.iTheme][STATE_RETURNING], Global.themeColor[Global.iTheme][STATE_RETURNING]));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        invalidateOptionsMenu();
        try {
            if (this.iPage == STATE_OFFSCREEN) {
                ((listViewCustom1) this.mObjectAdapter).notifyDataSetChanged();
            } else if (this.iPage == STATE_RETURNING) {
                Global.setBackgroundItemThread(this.linearFooter);
                ((listViewCustom2) this.mObjectAdapter).notifyDataSetChanged();
            } else if (this.iPage == 3) {
                Global.setBackgroundItemThread(this.linearHeader);
                Global.setBackgroundItemThread(this.linearFooter);
                ((listViewCustom3) this.mObjectAdapter).notifyDataSetChanged();
            } else if (this.iPage == 14) {
                Global.setBackgroundItemThread(this.linearHeader);
                Global.setBackgroundItemThread(this.linearFooter);
                ((listViewCustom3) this.mObjectAdapter).notifyDataSetChanged();
            } else if (this.iPage == 11) {
                ((listViewCustom3) this.mObjectAdapter).notifyDataSetChanged();
            } else {
                ((listViewCustom2) this.mObjectAdapter).notifyDataSetChanged();
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    private Dialog alert(String str) throws Exception {
        Dialog dialog = new Dialog(this.mContext);
        dialog.requestWindowFeature(STATE_OFFSCREEN);
        dialog.setContentView(R.layout.alert);
        dialog.getWindow().setLayout(-1, -2);
        Button button = (Button) dialog.findViewById(R.id.alert_ok);
        Button button2 = (Button) dialog.findViewById(R.id.alert_cancle);
        ((TextView) dialog.findViewById(R.id.tx_alarm)).setText(str);
        button2.setText("Tr\u1edf v\u1ec1");
        button.setText("T\u1ea3i l\u1ea1i");
        button.setOnClickListener(new AnonymousClass51(dialog));
        button2.setOnClickListener(new AnonymousClass52(dialog));
        dialog.show();
        return dialog;
    }

    private void alertSetSign(TextView textView) {
        Builder builder = new Builder(this);
        View inflate = getLayoutInflater().inflate(R.layout.setsign, null);
        EditText editText = (EditText) inflate.findViewById(R.id.editYourDevice);
        editText.setText(Global.sYourDevice);
        editText.setSelection(editText.getText().length());
        builder.setTitle("Sign").setView(inflate).setPositiveButton("OK", new AnonymousClass22(editText, textView));
        builder.create().show();
    }

    private void alertSettings() {
        Builder builder = new Builder(this);
        View inflate = getLayoutInflater().inflate(R.layout.setfont2, null);
        builder.setTitle("Settings").setView(inflate).setNegativeButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        TextView textView = (TextView) ((LinearLayout) inflate.findViewById(R.id.layout_seekbar)).findViewById(R.id.alert_txt1);
        textView.setGravity(17);
        SeekBar seekBar = (SeekBar) inflate.findViewById(R.id.alert_seekbar);
        seekBar.setMax(25);
        seekBar.setProgress((int) ((25.0d * (((double) this.mTextSize) - Global.FONTSCALE_MIN)) / 2.3d));
        textView.setText(String.valueOf(seekBar.getProgress()));
        seekBar.setOnSeekBarChangeListener(new AnonymousClass42(textView));
        ArrayList arrayList = new ArrayList();
        arrayList.add(new SettingAdapter.Item(this.settings, "Fullscreen", "FULLSCREEN", Global.bFullScreen, Global.bFullScreen));
        arrayList.add(new SettingAdapter.Item(this.settings, "Icon header", "TOPICHEADER", Global.bTopicHeader, Global.bTopicHeader));
        arrayList.add(new SettingAdapter.Item(this.settings, "Vibrate", "VIBRATE", Global.bVibrate, Global.bVibrate));
        arrayList.add(new SettingAdapter.Item(this.settings, "Notification", "NOTIFICATIONSUBSCRIBE", Global.bNotifSubscribe, Global.bNotifSubscribe));
        arrayList.add(new SettingAdapter.Item(this.settings, "Divider", "DEVIDER", Global.bDevider, Global.bDevider));
        arrayList.add(new SettingAdapter.Item(this.settings, "Sign", "SIGN", Global.bSign, Global.bSign));
        ExpandableHeightGridView expandableHeightGridView = (ExpandableHeightGridView) inflate.findViewById(R.id.gridview_setting);
        ListAdapter settingAdapter = new SettingAdapter(this.mContext, arrayList);
//        settingAdapter.setOnActionItemClickListener(new OnActionItemClickListener() {
//            public void onItemClick(int i, boolean z, SettingAdapter.Item item) {
//                if (i == 0) {
//                    Page.this.toggleFullScreen(z);
//                } else if (i == 6) {
//                    Page.this.UpdateUI();
//                }
//            }
//        });
        expandableHeightGridView.setAdapter(settingAdapter);
        expandableHeightGridView.setExpanded(true);
        LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.layouttheme);
        TextView textView2 = (TextView) inflate.findViewById(R.id.theme);
        String[] strArr = new String[]{"Voz", "Black", "Wood", "Theme 1", "Theme 2", "Theme 3", "Theme 4", "Theme 5", "Custom"};
        textView2.setText(strArr[Global.iTheme]);
        linearLayout.setOnClickListener(new AnonymousClass44(textView2));
        linearLayout = (LinearLayout) inflate.findViewById(R.id.layoutYourSign);
        textView2 = (TextView) inflate.findViewById(R.id.edityourdevice);
        textView2.setText(Global.sYourDevice);
        linearLayout.setOnClickListener(new AnonymousClass45(textView2));
        linearLayout = (LinearLayout) inflate.findViewById(R.id.layoutHome);
        textView2 = (TextView) inflate.findViewById(R.id.edithome);
        textView2.setText(BuildConfig.FLAVOR + Global.iHome);
        linearLayout.setOnClickListener(new AnonymousClass46(textView2));
        linearLayout = (LinearLayout) inflate.findViewById(R.id.layout_quickink);
        textView2 = (TextView) inflate.findViewById(R.id.editQuicklink);
        textView2.setText(Global.iNumQuickLink);
        linearLayout.setOnClickListener(new AnonymousClass47(textView2));
        linearLayout = (LinearLayout) inflate.findViewById(R.id.layout_pathsave);
        textView2 = (TextView) inflate.findViewById(R.id.editPathSave);
        textView2.setText(Global.mSavePath);
        linearLayout.setOnClickListener(new AnonymousClass48(textView2));
        builder.create().show();
    }

    private View getFooterView(int i) {
        this.linearFooter = (LinearLayout) ((LayoutInflater) this.mContext.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.threadfoot, null);
        Global.setBackgroundItemThread(this.linearFooter);
        this.mImg1Footer = (ImageView) this.linearFooter.findViewById(R.id.fast_prev);
        this.mImg2Footer = (ImageView) this.linearFooter.findViewById(R.id.prev);
        this.butPageFooter = (TextView) this.linearFooter.findViewById(R.id.page_list);
        this.mImg3Footer = (ImageView) this.linearFooter.findViewById(R.id.next);
        this.mImg4Footer = (ImageView) this.linearFooter.findViewById(R.id.fast_next);
        this.mImg1Footer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    Page.this.GoPage(4, Page.STATE_ONSCREEN);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.mImg2Footer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    Page.this.GoPage(3, Page.STATE_ONSCREEN);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.butPageFooter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Page.this.alertGoPage();
            }
        });
        this.mImg3Footer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    Page.this.GoPage(Page.STATE_RETURNING, Page.STATE_ONSCREEN);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.mImg4Footer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    Page.this.GoPage(5, Page.STATE_ONSCREEN);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.linearFooter.setGravity(i);
        return this.linearFooter;
    }

    private View getHeaderView(int i) {
        this.linearHeader = (LinearLayout) ((LayoutInflater) this.mContext.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.threadfoot, null);
        Global.setBackgroundItemThread(this.linearHeader);
        this.mImg1Header = (ImageView) this.linearHeader.findViewById(R.id.fast_prev);
        this.mImg2Header = (ImageView) this.linearHeader.findViewById(R.id.prev);
        this.butPageHeader = (TextView) this.linearHeader.findViewById(R.id.page_list);
        this.mImg3Header = (ImageView) this.linearHeader.findViewById(R.id.next);
        this.mImg4Header = (ImageView) this.linearHeader.findViewById(R.id.fast_next);
        this.mImg1Header.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    Page.this.GoPage(4, Page.STATE_ONSCREEN);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.mImg2Header.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    Page.this.GoPage(3, Page.STATE_ONSCREEN);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.butPageHeader.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Page.this.alertGoPage();
            }
        });
        this.mImg3Header.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    Page.this.GoPage(Page.STATE_RETURNING, Page.STATE_ONSCREEN);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.mImg4Header.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    Page.this.GoPage(5, Page.STATE_ONSCREEN);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.linearHeader.setGravity(i);
        return this.linearHeader;
    }

    private void initMenu() {
        initMenu2();
    }

    private void initMenu2() {
//        ((ListView) LayoutInflater.from(this).inflate(R.layout.sidebar, null).findViewById(R.id.list)).setFooterDividersEnabled(true);
        this.mListSideMenu2 = new ArrayList();
        this.mListSideMenu2.add(new Item("Login", getResources().getDrawable(R.drawable.logovoz), true));
        if (this.mUser.getAvatar() != null) {
            ((Item) this.mListSideMenu2.get(STATE_ONSCREEN)).mIcon = new BitmapDrawable(getResources(), this.mUser.getAvatar());
        }
        this.mListSideMenu2.add(new Category("User"));
        this.mListSideMenu2.add(new Item(getResources().getString(R.string.usercp), getResources().getDrawable(R.drawable.menu_usercp2)));
        if (this.iPage == 3) {
            if (this.isSubscribe) {
                this.mListSideMenu2.add(new Item(getResources().getString(R.string.UnSubscribe), getResources().getDrawable(R.drawable.unsubscribe)));
            } else {
                this.mListSideMenu2.add(new Item(getResources().getString(R.string.Subscribe), getResources().getDrawable(R.drawable.subscribe)));
            }
        }
        if (this.iPage == STATE_RETURNING) {
            this.mListSideMenu2.add(new Item(getResources().getString(R.string.NewThread), getResources().getDrawable(R.drawable.menu_newthread)));
        }
        this.mListSideMenu2.add(new Item(getResources().getString(R.string.ListBookmark), getResources().getDrawable(R.drawable.list_bookmark)));
        this.mListSideMenu2.add(new Item(getResources().getString(R.string.ListQuote), getResources().getDrawable(R.drawable.menu_quote)));
        this.mListSideMenu2.add(new Item(getResources().getString(R.string.Search), getResources().getDrawable(R.drawable.menu_search)));
        this.mListSideMenu2.add(new Item(getResources().getString(R.string.PM), getResources().getDrawable(R.drawable.menu_pm)));
        this.mListSideMenu2.add(new Item(getResources().getString(R.string.NewPost), getResources().getDrawable(R.drawable.menu_newpost)));
        this.mListSideMenu2.add(new Category("Go Address"));
        this.mListSideMenu2.add(new Item(getResources().getString(R.string.GoAddress), getResources().getDrawable(R.drawable.menu_forum)));
        this.mAdapterSideMenu2 = new SidebarAdapter(this.mContext, this.mListSideMenu2);
        setLeftDrawer(this.mAdapterSideMenu2);
        this.mAdapterSideMenu2.setOnActionItemClickListener(new SidebarAdapter.OnActionItemClickListener() {
            public void onItemClick(int i, Object obj) {
                String str = ((Item) obj).mTitle;
                if (str.equals(Page.this.getResources().getString(R.string.usercp))) {
                    if (Page.this.mUser.isLogin()) {
                        Page.this.startActivity(new Intent(Page.this.mContext, PageCP.class));
                    } else {
                        Page.this.toast("H\u00e3y \u0111\u0103ng nh\u1eadp");
                    }
                } else if (str.equals(Page.this.getResources().getString(R.string.GoAddress))) {
                    Page.this.alertGoAddress();
                } else if (str.equals(Page.this.getResources().getString(R.string.Subscribe))) {
                    Page.this.TaskCancle();
                    Page.this.mTask = new TaskGetHtml();
                    TaskGetHtml r0 = Page.this.mTask;
                    Integer[] r1 = new Integer[Page.STATE_OFFSCREEN];
                    r1[Page.STATE_ONSCREEN] = Integer.valueOf(7);
                    r0.execute(r1);
                } else if (str.equals(Page.this.getResources().getString(R.string.UnSubscribe))) {
                    Page.this.TaskCancle();
                    Page.this.mTask = new TaskGetHtml();
                    TaskGetHtml r0 = Page.this.mTask;
                    Integer[] r1 = new Integer[Page.STATE_OFFSCREEN];
                    r1[Page.STATE_ONSCREEN] = Integer.valueOf(8);
                    r0.execute(r1);
                } else if (str.equals(Page.this.getResources().getString(R.string.ListBookmark))) {
                    Page.this.startActivity(new Intent(Page.this.mContext, PageBookmark.class));
                } else if (str.equals(Page.this.getResources().getString(R.string.ListQuote))) {
                    Page.this.startActivity(new Intent(Page.this.mContext, PageQuote.class));
                } else if (str.equals(Page.this.getResources().getString(R.string.NewPost))) {
                    if (Page.this.mUser.isLogin()) {
                        Page.this.startActivity(new Intent(Page.this.mContext, PageNewPost.class));
                        Page.this.overridePendingTransition(R.anim.rail, R.anim.rail);
                    } else {
                        Page.this.toast("Ch\u01b0a \u0111\u0103ng nh\u1eadp");
                    }
                } else if (str.equals(Page.this.getResources().getString(R.string.NewThread))) {
                    if (Page.this.mUser.isLogin()) {
                        Intent intent = new Intent(Page.this.mContext, PageNewThread.class);
                        Bundle bundle = new Bundle();
                        if (Page.this.mParser.getUrl().contains("f=")) {
                            bundle.putString("FORUM", Page.this.mParser.getUrl().split("f=")[Page.STATE_OFFSCREEN]);
                        }
                        bundle.putInt("POSTTYPE", 5);
                        intent.putExtras(bundle);
                        Page.this.startActivityForResult(intent, 5);
                    } else {
                        Page.this.toast("Ch\u01b0a \u0111\u0103ng nh\u1eadp");
                    }
                } else if (str.equals(Page.this.getResources().getString(R.string.Search))) {
                    if (Page.this.mUser.isLogin()) {
                        Page.this.alertSearch();
                    } else {
                        Page.this.toast("Ch\u01b0a \u0111\u0103ng nh\u1eadp");
                    }
                } else if (str.equals(Page.this.getResources().getString(R.string.PM))) {
                    if (Page.this.mUser.isLogin()) {
                        Page.this.startActivity(new Intent(Page.this.mContext, PagePM.class));
                    } else {
                        Page.this.toast("Ch\u01b0a \u0111\u0103ng nh\u1eadp");
                    }
                }
                if (i == 0) {
                    Page.this.alertLogin();
                }
                Page.this.closeDrawer();
            }
        });
    }

    private void initQuickReturn() {
        int i = STATE_ONSCREEN;
        this.mQuickReturnLayout = (LinearLayout) findViewById(R.id.compose_button_bar);
        this.mQuickReturnLayout.removeAllViews();
        int convertDpToPx = Util.convertDpToPx(this.mContext, 40);
        Global.iNumQuickLink = this.settings.getString("NUMQUICKLINK", "5");
        int parseInt = Integer.parseInt(Global.iNumQuickLink);
        ImageView imageView = new ImageView(this.mQuickReturnLayout.getContext());
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.menu_up));
        LayoutParams layoutParams = new LinearLayout.LayoutParams(convertDpToPx, convertDpToPx);
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    Page.this.mList.clearFocus();
                    Page.this.mList.post(new Runnable() {
                        public void run() {
                            Page.this.mList.setSelection(Page.STATE_ONSCREEN);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Global.setBackgroundItemThread(imageView);
        imageView.setScaleType(ScaleType.FIT_XY);
        imageView.setLayoutParams(layoutParams);
        this.mQuickReturnLayout.addView(imageView);
        LayoutParams layoutParams2 =
                parseInt >= 5 ? new LinearLayout.LayoutParams((Global.width - (convertDpToPx * STATE_RETURNING)) / 5, -1) : new LinearLayout.LayoutParams(STATE_ONSCREEN, -1, PhotoViewAttacher.DEFAULT_MIN_SCALE);
        while (i < Integer.parseInt(Global.iNumQuickLink)) {
            TextView textView = new TextView(this.mQuickReturnLayout.getContext());
            Global.setBackgroundItemThread(textView);
            textView.setMinHeight(Util.convertDpToPx(this.mContext, 40));
            if (i % 5 == 0) {
                textView.setTextColor(getResources().getColor(R.color.colorquiclink1));
                textView.setText(this.settings.getString("QUICKLINK" + i, "f=0"));
            } else if (i % 5 == STATE_OFFSCREEN) {
                textView.setTextColor(getResources().getColor(R.color.colorquiclink2));
                textView.setText(this.settings.getString("QUICKLINK" + i, "f=32"));
            } else if (i % 5 == STATE_RETURNING) {
                textView.setTextColor(getResources().getColor(R.color.colorquiclink3));
                textView.setText(this.settings.getString("QUICKLINK" + i, "f=26"));
            } else if (i % 5 == 3) {
                textView.setTextColor(getResources().getColor(R.color.colorquiclink4));
                textView.setText(this.settings.getString("QUICKLINK" + i, "f=17"));
            } else if (i % 5 == 4) {
                textView.setTextColor(getResources().getColor(R.color.colorquiclink1));
                textView.setText(this.settings.getString("QUICKLINK" + i, "f=33"));
            }
            textView.setGravity(17);
            textView.setLayoutParams(layoutParams2);
            textView.setOnLongClickListener(new AnonymousClass16(i));
            textView.setOnClickListener(new AnonymousClass17(textView));
            this.mQuickReturnLayout.addView(textView);
            i += STATE_OFFSCREEN;
        }
        ImageView imageView2 = new ImageView(this.mQuickReturnLayout.getContext());
        imageView2.setImageDrawable(getResources().getDrawable(R.drawable.menu_down));
        LayoutParams layoutParams3 = new LinearLayout.LayoutParams(convertDpToPx, convertDpToPx);
        Global.setBackgroundItemThread(imageView2);
        imageView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    Page.this.mList.clearFocus();
                    Page.this.mList.post(new Runnable() {
                        public void run() {
                            int count = Page.this.iPage == Page.STATE_OFFSCREEN ? ((listViewCustom1) Page.this.mObjectAdapter).getCount() : Page.this.iPage == Page.STATE_RETURNING ? ((listViewCustom2) Page.this.mObjectAdapter).getCount() + Page.STATE_OFFSCREEN : Page.this.iPage == 3 ? ((listViewCustom3Html) Page.this.mObjectAdapter).getCount() + Page.STATE_OFFSCREEN : Page.this.iPage == 11 ? ((listViewCustom3) Page.this.mObjectAdapter).getCount() + Page.STATE_OFFSCREEN : ((listViewCustom2) Page.this.mObjectAdapter).getCount();
                            Page.this.mList.setSelection(count);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        imageView2.setScaleType(ScaleType.FIT_XY);
        imageView2.setLayoutParams(layoutParams3);
        this.mQuickReturnLayout.addView(imageView2);
        this.mList.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                Page.this.mQuickReturnHeight = Page.this.mQuickReturnLayout.getHeight();
            }
        });
        this.mList.setOnScrollListener(new OnScrollListener() {
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                Page.this.mScrollY = Page.STATE_ONSCREEN;
                if (Page.this.scrollIsComputed) {
                    Page.this.mScrollY = Page.this.getComputedScrollY();
                }
                int i4 = Page.this.mScrollY;
                switch (Page.this.mState) {
                    case Page.STATE_ONSCREEN /*0*/:
                        if (i4 > Page.this.mQuickReturnHeight) {
                            Page.this.mState = Page.STATE_OFFSCREEN;
                            Page.this.mMinRawY = i4;
                        }
                        Page.this.mQuickReturnLayout.setVisibility(Page.STATE_ONSCREEN);
                        break;
                    case Page.STATE_OFFSCREEN /*1*/:
                        if (i4 >= Page.this.mMinRawY) {
                            Page.this.mMinRawY = i4;
                        } else {
                            Page.this.mState = Page.STATE_RETURNING;
                        }
                        Page.this.mQuickReturnLayout.setVisibility(8);
                        break;
                    case Page.STATE_RETURNING /*2*/:
                        int i5 = (i4 - Page.this.mMinRawY) + Page.this.mQuickReturnHeight;
                        if (i5 < 0) {
                            Page.this.mMinRawY = Page.this.mQuickReturnHeight + i4;
                            i5 = Page.STATE_ONSCREEN;
                        }
                        if (i4 == 0) {
                            Page.this.mState = Page.STATE_ONSCREEN;
                            i5 = Page.STATE_ONSCREEN;
                        }
                        if (i5 > Page.this.mQuickReturnHeight) {
                            Page.this.mState = Page.STATE_OFFSCREEN;
                            Page.this.mMinRawY = i4;
                        }
                        if (i5 != Page.this.mQuickReturnHeight) {
                            Page.this.mQuickReturnLayout.setVisibility(Page.STATE_ONSCREEN);
                            i4 = i5;
                            break;
                        }
                        Page.this.mQuickReturnLayout.setVisibility(8);
                        i4 = i5;
                        break;
                    default:
                        i4 = Page.STATE_ONSCREEN;
                        break;
                }
                if (VERSION.SDK_INT <= 11) {
                    Page.this.anim = new TranslateAnimation(0.0f, 0.0f, (float) i4, (float) i4);
                    Page.this.anim.setFillAfter(true);
                    Page.this.anim.setDuration(0);
                    Page.this.mQuickReturnLayout.startAnimation(Page.this.anim);
                    return;
                }
                Page.this.mQuickReturnLayout.setTranslationY((float) i4);
            }

            @SuppressLint({"NewApi"})
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case Page.STATE_OFFSCREEN /*1*/:
                        Global.bScrolling = true;
                    default:
                }
            }
        });
    }

    private void readCustomTheme() {
        Global.mCusThemeBg = this.settings.getInt("THEMEBG", -1);
        Global.mCusThemeBgFocus = this.settings.getInt("THEMEBGFOCUS", -13388315);
        Global.mCusThemeTitleBg = this.settings.getInt("THEMETITLEBG", -4995021);
        Global.mCusThemeTxtTitle = this.settings.getInt("THEMETXTTITLE", -1);
        Global.mCusThemeTxt1 = this.settings.getInt("THEMETXT1", ViewCompat.MEASURED_STATE_MASK);
        Global.mCusThemeTxt2 = this.settings.getInt("THEMETXT2", -11184811);
        Global.mCusThemeQuicklink = this.settings.getInt("THEMEQUICKLINK", -4995021);
        Global.themeColor[8][STATE_ONSCREEN] = Global.mCusThemeBg;
        Global.themeColor[8][STATE_RETURNING] = Global.mCusThemeTitleBg;
        Global.themeColor[8][3] = Global.mCusThemeTxtTitle;
        Global.themeColor[8][4] = Global.mCusThemeTxt1;
        Global.themeColor[8][5] = Global.mCusThemeTxt2;
        Global.themeColor[8][6] = Global.mCusThemeQuicklink;
    }

    private void setEnableBackFooter(boolean z) {
        if (z) {
            this.mImg1Footer.setEnabled(true);
            this.mImg2Footer.setEnabled(true);
            this.mImg1Footer.setImageResource(R.drawable.tapatalk_fast_back_common_dark);
            this.mImg2Footer.setImageResource(R.drawable.tapatalk_back_common_dark);
            return;
        }
        this.mImg1Footer.setEnabled(false);
        this.mImg2Footer.setEnabled(false);
        this.mImg1Footer.setImageResource(R.drawable.tapatalk_fast_back_disable_dark);
        this.mImg2Footer.setImageResource(R.drawable.tapatalk_back_disable_dark);
    }

    private void setEnableBackHeader(boolean z) {
        if (z) {
            this.mImg1Header.setEnabled(true);
            this.mImg2Header.setEnabled(true);
            this.mImg1Header.setImageResource(R.drawable.tapatalk_fast_back_common_dark);
            this.mImg2Header.setImageResource(R.drawable.tapatalk_back_common_dark);
            return;
        }
        this.mImg1Header.setEnabled(false);
        this.mImg2Header.setEnabled(false);
        this.mImg1Header.setImageResource(R.drawable.tapatalk_fast_back_disable_dark);
        this.mImg2Header.setImageResource(R.drawable.tapatalk_back_disable_dark);
    }

    private void setEnableNextFooter(boolean z) {
        if (z) {
            this.mImg3Footer.setEnabled(true);
            this.mImg4Footer.setEnabled(true);
            this.mImg4Footer.setImageResource(R.drawable.tapatalk_fast_forward_common_dark);
            this.mImg3Footer.setImageResource(R.drawable.tapatalk_forward_common_dark);
            return;
        }
        this.mImg3Footer.setEnabled(false);
        this.mImg4Footer.setEnabled(false);
        this.mImg4Footer.setImageResource(R.drawable.tapatalk_fast_forward_disable_dark);
        this.mImg3Footer.setImageResource(R.drawable.tapatalk_forward_disable_dark);
    }

    private void setEnableNextHeader(boolean z) {
        if (z) {
            this.mImg3Header.setEnabled(true);
            this.mImg4Header.setEnabled(true);
            this.mImg4Header.setImageResource(R.drawable.tapatalk_fast_forward_common_dark);
            this.mImg3Header.setImageResource(R.drawable.tapatalk_forward_common_dark);
            return;
        }
        this.mImg3Header.setEnabled(false);
        this.mImg4Header.setEnabled(false);
        this.mImg4Header.setImageResource(R.drawable.tapatalk_fast_forward_disable_dark);
        this.mImg3Header.setImageResource(R.drawable.tapatalk_forward_disable_dark);
    }

    protected void GoPage(int i, int i2) throws Exception {
        if (parsePage(i, i2) != null) {
            this.mParser.setUrl(parsePage(i, i2));
            TaskCancle();
            this.mTask = new TaskGetHtml();
            TaskGetHtml taskGetHtml = this.mTask;
            Integer[] numArr = new Integer[STATE_OFFSCREEN];
            numArr[STATE_ONSCREEN] = Integer.valueOf(STATE_ONSCREEN);
            taskGetHtml.execute(numArr);
        }
    }

    void Parser(int i) throws Exception {
        if (this.doc == null && this.mParser.sNotif != null) {
            alert(this.mParser.sNotif);
        }
    }

    protected void TaskCancle() {
        if (this.mTask != null) {
            this.mTask.cancel(true);
        }
    }

    protected void addHeader() {
        if (this.iPage == STATE_RETURNING || this.iPage == 12 || this.iPage == 8 || this.iPage == 9 || this.iPage == 13 || this.iPage == 7) {
            this.mList.addFooterView(getFooterView(17));
        } else if (this.iPage == 3 || this.iPage == 14) {
            this.mList.addHeaderView(getHeaderView(17));
            this.mList.addFooterView(getFooterView(17));
        }
    }

    protected Dialog alertGoAddress() {
        Dialog dialog = new Dialog(this.mContext);
        dialog.requestWindowFeature(STATE_OFFSCREEN);
        dialog.setContentView(R.layout.go_address);
        dialog.getWindow().setLayout(-1, -2);
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) dialog.findViewById(R.id.alert_edit1);
        Button button = (Button) dialog.findViewById(R.id.alert_ok);
        Button button2 = (Button) dialog.findViewById(R.id.alert_cancle);
        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.type);
        autoCompleteTextView.setFocusable(true);
        autoCompleteTextView.requestFocus();
        autoCompleteTextView.setInputType(STATE_RETURNING);
        dialog.getWindow().setSoftInputMode(5);
//        ListAdapter uRLHistory = new URLHistory(this, 17367050);
//        try {
//            if (new File(getFilesDir(), Global.HISTORY_FILE).exists()) {
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openFileInput(Global.HISTORY_FILE)));
//                StringBuilder stringBuilder = new StringBuilder();
//                while (true) {
//                    String readLine = bufferedReader.readLine();
//                    if (readLine == null) {
//                        break;
//                    }
//                    stringBuilder.append(readLine);
//                    stringBuilder.append(IOUtils.LINE_SEPARATOR_UNIX);
//                }
//                bufferedReader.close();
//                uRLHistory.load(stringBuilder.toString());
//            }
//        } catch (Throwable th) {
//            Log.e("EVPlayer", "Exception in loading history", th);
//        }
//        autoCompleteTextView.setAdapter(uRLHistory);
//        button.setOnClickListener(new AnonymousClass35(autoCompleteTextView, radioGroup, dialog));
//        button2.setOnClickListener(new AnonymousClass36(dialog));
//        dialog.show();
        return dialog;
    }

    protected void alertGoPage() {
        Builder builder = new Builder(this);
        View inflate = getLayoutInflater().inflate(R.layout.login, null);
        EditText editText = (EditText) inflate.findViewById(R.id.alert_edit1);
        TextView textView = (TextView) inflate.findViewById(R.id.alert_txt1);
        TextView textView2 = (TextView) inflate.findViewById(R.id.alert_txt2);
        Button button = (Button) inflate.findViewById(R.id.alert_ok);
        Button button2 = (Button) inflate.findViewById(R.id.alert_cancle);
        ((EditText) inflate.findViewById(R.id.alert_edit2)).setVisibility(View.GONE);
        editText.setInputType(STATE_RETURNING);
        textView.setVisibility(View.GONE);
        textView2.setVisibility(View.GONE);
        builder.setTitle("Page").setView(inflate);
        Dialog create = builder.create();
        create.show();
        button.setOnClickListener(new AnonymousClass49(editText, create));
        button2.setOnClickListener(new AnonymousClass50(create));
    }

    protected void alertLogin() {
        int length;
        Builder builder = new Builder(this);
        View inflate = getLayoutInflater().inflate(R.layout.login, null);
        EditText editText = (EditText) inflate.findViewById(R.id.alert_edit1);
        EditText editText2 = (EditText) inflate.findViewById(R.id.alert_edit2);
        TextView textView = (TextView) inflate.findViewById(R.id.alert_txt1);
        TextView textView2 = (TextView) inflate.findViewById(R.id.alert_txt2);
        Button button = (Button) inflate.findViewById(R.id.alert_ok);
        Button button2 = (Button) inflate.findViewById(R.id.alert_cancle);
        RadioGroup radioGroup = (RadioGroup) inflate.findViewById(R.id.rg_usered);
        builder.setTitle("Login").setView(inflate);
        Dialog create = builder.create();
        create.show();
        this.mRadioUser = null;
        String string = this.settings.getString("usered", null);
        this.mArrayUsered = new ArrayList();
        this.mArrayPass = new ArrayList();
        this.mArrayUseredId = new ArrayList();
        if (string != null) {
            String[] split = string.split(";");
            length = split.length;
            for (int i = STATE_ONSCREEN; i < length; i += STATE_OFFSCREEN) {
                String str = split[i];
                this.mArrayUsered.add(this.settings.getString("username-" + str, BuildConfig.FLAVOR));
                this.mArrayPass.add(this.settings.getString("password-" + str, BuildConfig.FLAVOR));
                this.mArrayUseredId.add(this.settings.getString("userid-" + str, BuildConfig.FLAVOR));
            }
        }
        length = STATE_ONSCREEN;
        while (length < this.mArrayUsered.size()) {
            View radioButton = new RadioButton(this);
//            radioButton.setText(this.mArrayUsered.get(length).toString());
            radioButton.setOnClickListener(new AnonymousClass53(editText, editText2, textView, textView2, button, button2));
            radioGroup.addView(radioButton);
            if (this.mUser.User() != null && this.mUser.User().equals(this.mArrayUsered.get(length))) {
                this.mRadioUser = this.mUser.User();
                radioGroup.check(radioButton.getId());
                editText.setVisibility(View.GONE);
                editText2.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                textView2.setVisibility(View.GONE);
                button.setText("Log out");
                button2.setText("Cancel");
            }
            length += STATE_OFFSCREEN;
        }
        View radioButton2 = new RadioButton(this);
//        radioButton2.setText("Add");
        radioButton2.setOnClickListener(new AnonymousClass54(editText, editText2, textView2, textView, button, button2));
        radioGroup.addView(radioButton2);
        if (this.mRadioUser == null) {
            this.mRadioUser = "Add";
            radioGroup.check(radioButton2.getId());
            button.setText("Log in");
            button2.setText("Cancel");
        }
        button.setOnClickListener(new AnonymousClass55(editText, editText2, create));
        button2.setOnClickListener(new AnonymousClass56(button2, create));
    }

    protected void alertPickerColor(int i) {
        Builder builder = new Builder(this);
        View inflate = getLayoutInflater().inflate(R.layout.pickercolor, null);
        builder.setTitle("Choose color").setView(inflate).setPositiveButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        ColorPicker colorPicker = (ColorPicker) inflate.findViewById(R.id.picker);
        EditText editText = (EditText) inflate.findViewById(R.id.editColor);
        SVBar sVBar = (SVBar) inflate.findViewById(R.id.svbar);
        OpacityBar opacityBar = (OpacityBar) inflate.findViewById(R.id.opacitybar);
        colorPicker.addSVBar(sVBar);
        colorPicker.addOpacityBar(opacityBar);
        int i2 = i == STATE_RETURNING ? Global.mCusThemeTitleBg : i == 0 ? Global.mCusThemeBg : i == STATE_OFFSCREEN ? Global.mCusThemeBgFocus : i == 4 ? Global.mCusThemeTxt1 : i == 5 ? Global.mCusThemeTxt2 : i == R.id.typeTextTitle ? 3 : i == 6 ? Global.mCusThemeQuicklink : STATE_ONSCREEN;
        colorPicker.setColor(i2);
        editText.setText(String.valueOf(Integer.toHexString(i2)));
        editText.setEnabled(false);
        colorPicker.setOnColorChangedListener(new AnonymousClass26(editText, i));
        builder.create().show();
    }

    protected void alertSeCustomTheme() {
        Builder builder = new Builder(this);
        builder.setTitle("Custom Theme").setSingleChoiceItems(R.array.customtheme, STATE_OFFSCREEN, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Page.this.alertPickerColor(i);
            }
        }).setPositiveButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }

    protected Dialog alertSearch() {
        Dialog dialog = new Dialog(this.mContext);
        dialog.requestWindowFeature(STATE_OFFSCREEN);
        dialog.setContentView(R.layout.search);
        dialog.getWindow().setLayout(-1, -2);
        EditText editText = (EditText) dialog.findViewById(R.id.edit);
        Button button = (Button) dialog.findViewById(R.id.alert_ok);
        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.type);
        Button button2 = (Button) dialog.findViewById(R.id.MyRecentPosts);
        Button button3 = (Button) dialog.findViewById(R.id.MyRecentThreads);
        Button button4 = (Button) dialog.findViewById(R.id.AdvanceSearch);
        radioGroup.setOnCheckedChangeListener(new AnonymousClass30(radioGroup));
        button4.setOnClickListener(new AnonymousClass31(button4, editText));
        button3.setOnClickListener(new AnonymousClass32(dialog));
        button2.setOnClickListener(new AnonymousClass33(dialog));
        button.setOnClickListener(new AnonymousClass34(editText, dialog));
        dialog.show();
        return dialog;
    }

    protected void alertSetHome(TextView textView) {
        Builder builder = new Builder(this);
        View inflate = getLayoutInflater().inflate(R.layout.sethome, null);
        EditText editText = (EditText) inflate.findViewById(R.id.alert_edit1);
        try {
            editText.setText(BuildConfig.FLAVOR + Global.iHome);
        } catch (Exception e) {
            editText.setText("0");
            e.printStackTrace();
        }
        editText.setSelection(editText.getText().length());
        builder.setTitle("Home").setView(inflate).setPositiveButton("OK", new AnonymousClass14(editText, textView)).setNegativeButton("Cancel", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }

    protected void alertSetPathSave(TextView textView) {
        Builder builder = new Builder(this);
        View inflate = getLayoutInflater().inflate(R.layout.sethome, null);
        EditText editText = (EditText) inflate.findViewById(R.id.alert_edit1);
        editText.setText(Global.mSavePath);
        editText.setSelection(editText.getText().length());
        builder.setTitle("Path for save image").setView(inflate).setPositiveButton("OK", new AnonymousClass38(editText, textView)).setNegativeButton("Cancel", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }

    protected void alertSetQuickLink(TextView textView) {
        Builder builder = new Builder(this);
        View inflate = getLayoutInflater().inflate(R.layout.setquicklink, null);
        EditText editText = (EditText) inflate.findViewById(R.id.numQuickLink);
        editText.setText(Global.iNumQuickLink);
        editText.setSelection(editText.getText().length());
        builder.setTitle("Quick Link").setView(inflate).setPositiveButton("OK", new AnonymousClass40(editText, textView)).setNegativeButton("Cancel", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }

    protected void alertSetQuickmenu(TextView textView, int i) {
        Builder builder = new Builder(this);
        View inflate = getLayoutInflater().inflate(R.layout.setquickmenu, null);
        EditText editText = (EditText) inflate.findViewById(R.id.alert_edit1);
        editText.setFocusable(true);
        editText.requestFocus();
        editText.setInputType(STATE_RETURNING);
        try {
            editText.setText(textView.getText().toString().split("=")[STATE_OFFSCREEN]);
        } catch (Exception e) {
            editText.setText("0");
            e.printStackTrace();
        }
        builder.setTitle("Quick menu").setView(inflate).setPositiveButton("OK", new AnonymousClass12(editText, i, textView)).setNegativeButton("Cancel", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }

    protected Dialog alertSetSizeImage() {
        Dialog dialog = new Dialog(this.mContext);
        dialog.setTitle("Set Image");
        dialog.setContentView(R.layout.setsizeimge);
        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.typeSizeImage);
        if (Global.iSizeImage == STATE_OFFSCREEN) {
            radioGroup.check(R.id.typeSizeImage_1);
        } else if (Global.iSizeImage == STATE_RETURNING) {
            radioGroup.check(R.id.typeSizeImage_2);
        } else if (Global.iSizeImage == 4) {
            radioGroup.check(R.id.typeSizeImage_4);
        } else if (Global.iSizeImage == 8) {
            radioGroup.check(R.id.typeSizeImage_8);
        } else if (Global.iSizeImage == 0) {
            radioGroup.check(R.id.typeSizeImage_none);
        }
        radioGroup.setOnCheckedChangeListener(new AnonymousClass29(radioGroup));
        dialog.show();
        return dialog;
    }

    protected void alertSetTheme(TextView textView) {
        Builder builder = new Builder(this);
        String[] charSequenceArr = new String[]{"Voz", "Black", "Wood", "Theme 1", "Theme 2", "Theme 3", "Theme 4", "Theme 5"};
        builder.setTitle("Theme").setSingleChoiceItems(charSequenceArr, Global.iTheme, new AnonymousClass28(textView, charSequenceArr)).setNegativeButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }

    public StateListDrawable getColorBackground(int i, int i2) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        Drawable colorDrawable = new ColorDrawable(i);
        Drawable colorDrawable2 = new ColorDrawable(i2);
        int[] iArr = new int[STATE_OFFSCREEN];
        iArr[STATE_ONSCREEN] = -16842910;
        stateListDrawable.addState(iArr, colorDrawable);
        iArr = new int[STATE_OFFSCREEN];
        iArr[STATE_ONSCREEN] = 16842914;
        stateListDrawable.addState(iArr, colorDrawable);
        int[] iArr2 = new int[STATE_OFFSCREEN];
        iArr2[STATE_ONSCREEN] = 16842913;
        stateListDrawable.addState(iArr2, colorDrawable2);
        iArr2 = new int[STATE_OFFSCREEN];
        iArr2[STATE_ONSCREEN] = 16842919;
        stateListDrawable.addState(iArr2, colorDrawable2);
        return stateListDrawable;
    }

    public int getComputedScrollY() {
        int i = STATE_ONSCREEN;
        int firstVisiblePosition = this.mList.getFirstVisiblePosition();
        View childAt = this.mList.getChildAt(STATE_ONSCREEN);
        if (childAt == null) {
            return STATE_ONSCREEN;
        }
        int top = childAt.getTop();
        if (firstVisiblePosition >= this.mItemtemp.length) {
            return STATE_ONSCREEN;
        }
        this.mItemtemp[firstVisiblePosition] = childAt.getHeight();
        this.mItemOffsetY[firstVisiblePosition] = STATE_ONSCREEN;
        while (i < firstVisiblePosition) {
            int[] iArr = this.mItemOffsetY;
            iArr[firstVisiblePosition] = iArr[firstVisiblePosition] + this.mItemtemp[i];
            i += STATE_OFFSCREEN;
        }
        return this.mItemOffsetY[firstVisiblePosition] - top;
    }

    void getRevolution() {
        Point point = new Point();
        WindowManager windowManager = getWindowManager();
        if (VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            windowManager.getDefaultDisplay().getSize(point);
            Global.width = point.x;
            Global.height = point.y;
            return;
        }
        Display defaultDisplay = windowManager.getDefaultDisplay();
        Global.width = defaultDisplay.getWidth();
        Global.height = defaultDisplay.getHeight();
    }

    protected String getlinkBitmapAssert(String str) {
        int i = STATE_ONSCREEN;
        if (str.contains("smilies")) {
            String str2 = str.substring(STATE_ONSCREEN, str.length() - 3) + "gif";
            try {
                String[] list = this.mContext.getAssets().list("images/smilies/Off");
                int length = list.length;
                while (i < length) {
                    if ((str.substring(STATE_ONSCREEN, str.length() - 3) + "png").contains(list[i])) {
                        str = str.substring(STATE_ONSCREEN, str.length() - 3) + "png";
                        break;
                    }
                    i += STATE_OFFSCREEN;
                }
                str = str2;
            } catch (Exception e) {
                e.printStackTrace();
                str = str2;
            }
        }
        return "assets://" + str;
    }

    protected void hideAds() {
//        mAd.hideAds();
    }

    void initUI() {
        this.mContext = this;
        this.mDataBookmark = new CommentsDataSource(this.mContext);
        this.mDataBookmark.open();
        this.mImageLoad = new ImageLoad();
        this.mList = (ListView) findViewById(R.id.content_frame);
        if (this.iPage == STATE_RETURNING || this.iPage == 12 || this.iPage == 8 || this.iPage == 9 || this.iPage == 13 || this.iPage == 7) {
            this.mList.addFooterView(getFooterView(17));
        } else if (this.iPage == 3 || this.iPage == 14) {
            this.mList.addHeaderView(getHeaderView(17));
            this.mList.addFooterView(getFooterView(17));
        }
        initMenu();
        initQuickReturn();
    }

    protected void loadAds() {
//        mAd.loadAds();
    }

    Bitmap loadBitmapAssert(String str) {
        int i = STATE_ONSCREEN;
        if (str.contains("smilies")) {
            String str2 = str.substring(STATE_ONSCREEN, str.length() - 3) + "gif";
            try {
                String[] list = this.mContext.getAssets().list("images/smilies/Off");
                int length = list.length;
                while (i < length) {
                    if ((str.substring(STATE_ONSCREEN, str.length() - 3) + "png").contains(list[i])) {
                        str = str.substring(STATE_ONSCREEN, str.length() - 3) + "png";
                        break;
                    }
                    i += STATE_OFFSCREEN;
                }
                str = str2;
            } catch (Exception e) {
                e.printStackTrace();
                str = str2;
            }
        }
        try {
            InputStream open = this.mContext.getAssets().open(str);
            Bitmap decodeStream = BitmapFactory.decodeStream(open);
            if (str.contains("statusicon")) {
                int convertDpToPx = Util.convertDpToPx(this.mContext.getApplicationContext(), 24);
                open.close();
                return Bitmap.createScaledBitmap(decodeStream, convertDpToPx, convertDpToPx, false);
            }
            open.close();
            return decodeStream;
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    void log(String str) {
        Log.d("nna", str);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        switch (getResources().getConfiguration().orientation) {
            case STATE_OFFSCREEN /*1*/:
                getRevolution();
                initQuickReturn();
            case STATE_RETURNING /*2*/:
                getRevolution();
                initQuickReturn();
            default:
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mColorText2 = Global.getTextClolor2();
        this.settings = getSharedPreferences("Setting", STATE_ONSCREEN);
        this.mUser = new UserInfo();
        Global.iTheme = this.settings.getInt("THEME", STATE_OFFSCREEN);
        if (Global.iTheme == 8) {
            readCustomTheme();
        }
        getRevolution();
        initUI();
//        this.mLayoutAds = (RelativeLayout) findViewById(R.id.myAd);
//        this.mLayoutAds.setVisibility(View.GONE);
        readSettings();
        this.mParser = new parser(Global.URL);
        this.mTask = new TaskGetHtml();
        this.textTitle = (TextView) findViewById(Resources.getSystem().getIdentifier("action_bar_title", "id", "android"));
//        mAd = new Ad(this, this.mLayoutAds);
        CookieSyncManager.createInstance(this);
        CookieManager.getInstance().setAcceptCookie(true);
        CookieHandler.setDefault(new WebkitCookieManagerProxy(null, CookiePolicy.ACCEPT_ALL));
        this.mLayoutProgress = (LinearLayout) findViewById(R.id.layoutprogress);
        Global.iDensity = getResources().getDisplayMetrics().density;
        log("density:" + Global.iDensity);
    }

    protected void onDestroy() {
        TaskCancle();
        this.mDataBookmark.close();
        this.mContext = null;
        this.mObjectAdapter = null;
        super.onDestroy();
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (this.mDrawerToggle.onOptionsItemSelected(menuItem)) {
            return true;
        }
        int itemId = menuItem.getItemId();
        TaskGetHtml taskGetHtml;
        Integer[] numArr;
        if (itemId == R.id.action_refresh) {
            TaskCancle();
            this.mTask = new TaskGetHtml();
            taskGetHtml = this.mTask;
            numArr = new Integer[STATE_OFFSCREEN];
            numArr[STATE_ONSCREEN] = Integer.valueOf(STATE_ONSCREEN);
            taskGetHtml.execute(numArr);
            return true;
        } else if (itemId == R.id.action_refresh2) {
            TaskCancle();
            this.mTask = new TaskGetHtml();
            taskGetHtml = this.mTask;
            numArr = new Integer[STATE_OFFSCREEN];
            numArr[STATE_ONSCREEN] = Integer.valueOf(STATE_ONSCREEN);
            taskGetHtml.execute(numArr);
            return true;
        } else if (itemId == R.id.action_help) {
            new Help(this.mContext).getLogDialog().show();
            return true;
        } else if (itemId == R.id.action_exit) {
            Intent intent = new Intent(this.mContext, Page1.class);
            intent.setFlags(67108864);
            intent.putExtra("Exit me", true);
            startActivity(intent);
            finish();
            return true;
        } else if (itemId == R.id.action_websearch) {
            alertSettings();
            return true;
        } else if (itemId == R.id.action_share) {
            Intent intent2 = new Intent("android.intent.action.SEND");
            intent2.setType("text/plain");
            String url = this.mParser.getUrl();
            if (!url.contains("vozforums.com")) {
                url = Global.URL + url;
            }
            intent2.putExtra("android.intent.extra.TEXT", url);
            intent2.putExtra("android.intent.extra.STREAM", R.drawable.back_click);
            startActivity(Intent.createChooser(intent2, "Your Chooser"));
            return true;
        } else if (itemId != R.id.action_back) {
            return super.onOptionsItemSelected(menuItem);
        } else {
            onBackPressed();
            return true;
        }
    }

    protected void onPause() {
        super.onPause();
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        if (this.mUser.isLogin()) {
            new getAvatar().execute(new Void[STATE_ONSCREEN]);
        } else {
            ((Item) this.mListSideMenu2.get(STATE_ONSCREEN)).mIcon = getResources().getDrawable(R.drawable.login);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    protected void onResume() {
        UpdateUI();
        super.onResume();
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
    }

    String parsePage(int i, int i2) throws Exception {
        if (this.doc == null) {
            return null;
        }
        Element first = this.doc.select("div[class=pagenav").first();
        if (first == null) {
            return null;
        }
        String attr;
        String text = first.select("td[class=vbmenu_control]").text();
        int parseInt = Integer.parseInt(text.split(" ")[STATE_OFFSCREEN]);
        int parseInt2 = Integer.parseInt(text.split(" ")[3]);
        if (this.mParser.getUrl().contains("showthread.php?p=")) {
            int size = first.getElementsByAttribute("href").size();
            int i3 = STATE_ONSCREEN;
            while (i3 < size) {
                Element element = (Element) first.getElementsByAttribute("href").get(i3);
                if (element != null) {
                    attr = element.attr("href");
                    if (!attr.contains("&page=")) {
                        attr = attr.concat("&page=1");
                    }
                    attr = attr.substring(attr.indexOf("?t=") + 3, attr.indexOf("&page"));
                    attr = "https://vozforums.com/showthread.php?t=" + attr + "&page=" + String.valueOf(parseInt);
                } else {
                    i3 += STATE_OFFSCREEN;
                }
            }
            attr = null;
            attr = "https://vozforums.com/showthread.php?t=" + attr + "&page=" + String.valueOf(parseInt);
        } else {
            attr = this.mParser.getUrl();
            if (!attr.contains("&page=")) {
                attr = attr.concat("&page=1");
            }
        }
        switch (i) {
            case STATE_ONSCREEN /*0*/:
                String[] split = text.split(" ");
                return split[STATE_OFFSCREEN] + "/" + split[3];
            case STATE_OFFSCREEN /*1*/:
                return attr.split("&page")[STATE_ONSCREEN].concat("&page=" + String.valueOf(i2));
            case STATE_RETURNING /*2*/:
                return attr.substring(STATE_ONSCREEN, attr.lastIndexOf("=") + STATE_OFFSCREEN).concat(String.valueOf(parseInt + STATE_OFFSCREEN));
            case HttpRouteDirector.TUNNEL_TARGET /*3*/:
                String concat = attr.substring(STATE_ONSCREEN, attr.lastIndexOf("=") + STATE_OFFSCREEN).concat(String.valueOf(parseInt - 1));
                return concat.contains("&page=0") ? concat.split("&page")[STATE_ONSCREEN] : concat;
            case HttpRouteDirector.TUNNEL_PROXY /*4*/:
                return attr.split("&page")[STATE_ONSCREEN];
            case HttpRouteDirector.LAYER_PROTOCOL /*5*/:
                return attr.split("&page")[STATE_ONSCREEN].concat("&page=" + String.valueOf(parseInt2));
            default:
                return null;
        }
    }

    void parserNotification() {
        if (this.doc != null) {
            Element first = this.doc.select("tr:has(td[class=tcat])").first();
            if (first != null) {
                first = first.nextElementSibling();
                if (first != null) {
                    try {
                        alert(first.text());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void readSettings() {
        this.mTextSize = this.settings.getFloat("FontSize", PhotoViewAttacher.DEFAULT_MIN_SCALE);
        Global.iSize = this.mTextSize;
        Global.bSwipe = this.settings.getBoolean("SWIPE", true);
        Global.bTopicHeader = this.settings.getBoolean("TOPICHEADER", false);
        Global.bIconRefresh = this.settings.getBoolean("ICONREFRESH", false);
        Global.bClickAd = this.settings.getBoolean("CLICKAD", false);
        Global.bEffect = this.settings.getBoolean("EFFECT", false);
        Global.bSign = this.settings.getBoolean("SIGN", false);
        Global.bDevider = this.settings.getBoolean("DEVIDER", true);
        Global.iHome = this.settings.getInt("SETHOMEVOZ", STATE_ONSCREEN);
        Global.mSavePath = this.settings.getString("PATHSAVE", "/mnt/sdcard/vozforums");
        Global.bNotifSubscribe = this.settings.getBoolean("NOTIFICATIONSUBSCRIBE", true);
        Global.bNotifQuote = this.settings.getBoolean("NOTIFICATIONQUOTE", true);
        Global.iNotifminutes = this.settings.getInt("NOTIFICATIONTIMER", 30);
        Global.iNumQuote = this.settings.getInt("NUMQUOTE", STATE_ONSCREEN);
        Global.bVibrate = this.settings.getBoolean("VIBRATE", true);
        Global.bFullScreen = this.settings.getBoolean("FULLSCREEN", false);
        Global.iSizeImage = this.settings.getInt("SIZEIMAGE", 8);
        Global.sYourDevice = this.settings.getString("YOURDEVICE", Build.MODEL);
//        toggleFullScreen(Global.bFullScreen);
        Map hashMap = new HashMap();
        if (!this.settings.getString("vfuserid", BuildConfig.FLAVOR).equals(BuildConfig.FLAVOR) && !this.settings.getString("vfuserid", BuildConfig.FLAVOR).equals("deleted")) {
            hashMap.put("vflastvisit", this.settings.getString("vflastvisit", BuildConfig.FLAVOR));
            hashMap.put("vflastactivity", this.settings.getString("vflastactivity", BuildConfig.FLAVOR));
            hashMap.put("vfuserid", this.settings.getString("vfuserid", BuildConfig.FLAVOR));
            hashMap.put("vfpassword", this.settings.getString("vfpassword", BuildConfig.FLAVOR));
            hashMap.put("vfimloggedin", this.settings.getString("vfimloggedin", BuildConfig.FLAVOR));
            hashMap.put("vfsessionhash", this.settings.getString("vfsessionhash", BuildConfig.FLAVOR));
            this.mUser.cookies(hashMap);
            this.mUser.setCookieStore(hashMap);
            this.mUser.setUserId(this.settings.getString("vfuserid", BuildConfig.FLAVOR));
            String string = this.settings.getString("usered", null);
            if (string != null) {
                String[] split = string.split(";");
                int length = split.length;
                for (int i = STATE_ONSCREEN; i < length; i += STATE_OFFSCREEN) {
                    String str = split[i];
                    this.mArrayUsered.add(this.settings.getString("username-" + str, BuildConfig.FLAVOR));
                    this.mArrayPass.add(this.settings.getString("password-" + str, BuildConfig.FLAVOR));
                    this.mArrayUseredId.add(this.settings.getString("userid-" + str, BuildConfig.FLAVOR));
                }
            }
            this.mUser.SetUser(this.settings.getString("username-" + this.mUser.UserId(), getResources().getString(R.string.Login)));
            ((Item) this.mListSideMenu2.get(STATE_ONSCREEN)).mTitle = this.mUser.User();
            this.mAdapterSideMenu2.notifyDataSetInvalidated();
        }
    }

    public void readStringForum() {
        String[] split = this.settings.getString("FORUMS", BuildConfig.FLAVOR).split(";");
        int length = split.length;
        if (length > 0) {
            this.lsForum = new ArrayList();
            this.lIdForum = new ArrayList();
            for (int i = STATE_ONSCREEN; i < length; i += STATE_OFFSCREEN) {
                String str = split[i];
                if (str.contains(",")) {
                    this.lsForum.add(str.split(",")[STATE_ONSCREEN]);
                    if (str.split(",")[STATE_OFFSCREEN].contains("f=")) {
                        this.lIdForum.add(str.split(",")[STATE_OFFSCREEN].split("f=")[STATE_OFFSCREEN]);
                    } else {
                        this.lIdForum.add(null);
                    }
                }
            }
        }
    }

    protected void setPage(String str) {
        setEnableBackFooter(true);
        setEnableNextFooter(true);
        if (this.iPage == 3) {
            setEnableBackHeader(true);
            setEnableNextHeader(true);
        }
        if (str == null) {
            this.butPageFooter.setText("Page 1/1");
            setEnableBackFooter(false);
            setEnableNextFooter(false);
            if (this.iPage == 3) {
                this.butPageHeader.setText("Page 1/1");
                setEnableBackHeader(false);
                setEnableNextHeader(false);
                return;
            }
            return;
        }
        if (str.contains("/")) {
            if (str.split("/")[STATE_ONSCREEN].equals("1")) {
                setEnableBackFooter(false);
            }
            if (str.split("/")[STATE_ONSCREEN].equals(str.split("/")[STATE_OFFSCREEN])) {
                setEnableNextFooter(false);
            }
        }
        this.butPageFooter.setText(" Page " + str + " ");
        if (this.iPage == 3) {
            if (str.contains("/")) {
                if (str.split("/")[STATE_ONSCREEN].equals("1")) {
                    setEnableBackHeader(false);
                } else {
                    setEnableBackHeader(true);
                }
                if (str.split("/")[STATE_ONSCREEN].equals(str.split("/")[STATE_OFFSCREEN])) {
                    setEnableNextHeader(false);
                } else {
                    setEnableNextHeader(true);
                }
            }
            this.butPageHeader.setText(" Page " + str + " ");
        }
    }

    protected void setProgress(boolean z) {
        if (z) {
            this.mLayoutProgress.setVisibility(View.VISIBLE);
        } else {
            this.mLayoutProgress.setVisibility(View.GONE);
        }
    }

    void toast(String str) {
        Toast.makeText(this.mContext, str, Toast.LENGTH_SHORT).show();
    }

    void toggleFullScreen(boolean z) {
        if (z) {
            getWindow().addFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
            getWindow().clearFlags(AccessibilityNodeInfoCompat.ACTION_PREVIOUS_HTML_ELEMENT);
        } else {
            getWindow().addFlags(AccessibilityNodeInfoCompat.ACTION_PREVIOUS_HTML_ELEMENT);
            getWindow().clearFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        }
        this.mList.requestLayout();
    }

    protected void updateSetFont(float f) {
    }

    protected void vibrate() {
        if (Global.bVibrate) {
            if (this.myVib == null) {
                this.myVib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            }
            this.myVib.vibrate(50);
        }
    }

    public void writeQuickreturn(int i, String str) {
        Editor edit = this.settings.edit();
        edit.putString("QUICKLINK" + i, str);
        edit.commit();
    }

    public void writeSetCusTheme() {
        Editor edit = this.settings.edit();
        edit.putInt("THEMEBG", Global.mCusThemeBg);
        edit.putInt("THEMEBGFOCUS", Global.mCusThemeBgFocus);
        edit.putInt("THEMETITLEBG", Global.mCusThemeTitleBg);
        edit.putInt("THEMETXTTITLE", Global.mCusThemeTxtTitle);
        edit.putInt("THEMETXT1", Global.mCusThemeTxt1);
        edit.putInt("THEMETXT2", Global.mCusThemeTxt2);
        edit.putInt("THEMEQUICKLINK", Global.mCusThemeQuicklink);
        edit.commit();
    }

    public void writeSetFont(float f) {
        Editor edit = this.settings.edit();
        edit.putFloat("FontSize", f);
        edit.commit();
    }

    public void writeSetHome(int i) {
        Editor edit = this.settings.edit();
        edit.putInt("SETHOMEVOZ", i);
        edit.commit();
    }

    public void writeSetImageSize(int i) {
        Editor edit = this.settings.edit();
        edit.putInt("SIZEIMAGE", i);
        edit.commit();
    }

    public void writeSetNumQuickLink(String str) {
        Editor edit = this.settings.edit();
        edit.putString("NUMQUICKLINK", str);
        edit.commit();
    }

    protected void writeSetNumQuote(int i) {
        Editor edit = this.settings.edit();
        edit.putInt("NUMQUOTE", i);
        edit.commit();
    }

    public void writeSetPathSave(String str) {
        Editor edit = this.settings.edit();
        edit.putString("PATHSAVE", str);
        edit.commit();
    }

    public void writeSetTheme(int i) {
        Editor edit = this.settings.edit();
        edit.putInt("THEME", i);
        edit.commit();
    }

    public void writeSetYourDevices(String str) {
        Editor edit = this.settings.edit();
        edit.putString("YOURDEVICE", str);
        edit.commit();
    }

    public void writeSetingUser(Map<String, String> map) {
        Editor edit = this.settings.edit();
        if (map != null) {
            edit.putString("vflastvisit", (String) map.get("vflastvisit"));
            edit.putString("vflastactivity", (String) map.get("vflastactivity"));
            edit.putString("vfuserid", (String) map.get("vfuserid"));
            edit.putString("vfpassword", (String) map.get("vfpassword"));
            edit.putString("vfimloggedin", (String) map.get("vfimloggedin"));
            edit.putString("vfsessionhash", (String) map.get("vfsessionhash"));
            this.mUser.setCookieStore(map);
            String str = (String) map.get("vfuserid");
            if (!(this.mUsername == null || this.mPassword == null)) {
                edit.putString("username-" + str, this.mUsername);
                edit.putString("password-" + str, this.mPassword);
                edit.putString("userid-" + str, str);
                this.mUsername = null;
                this.mPassword = null;
            }
            String string = this.settings.getString("usered", null);
            if (string == null) {
                edit.putString("usered", str);
            } else if (!string.contains(str)) {
                edit.putString("usered", string + ";" + str);
            }
        } else {
            edit.putString("vflastvisit", BuildConfig.FLAVOR);
            edit.putString("vflastactivity", BuildConfig.FLAVOR);
            edit.putString("vfuserid", BuildConfig.FLAVOR);
            edit.putString("vfpassword", BuildConfig.FLAVOR);
            edit.putString("vfimloggedin", BuildConfig.FLAVOR);
            edit.putString("vfsessionhash", BuildConfig.FLAVOR);
            this.mUser.setCookieStore(null);
        }
        edit.commit();
    }

    public void writeStringForums(String str) {
        Editor edit = this.settings.edit();
        edit.putString("FORUMS", str);
        edit.commit();
    }
}
