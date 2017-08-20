package com.whoami.voz.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.leakcanary.RefWatcher;
import com.whoami.voz.R;
import com.whoami.voz.ui.main.Global;
import com.whoami.voz.ui.main.MainApplication;
import com.whoami.voz.ui.pagethreads.PageThreadsActivity;
import com.whoami.voz.ui.utils.Util;

public class BaseActivity extends AppCompatActivity {
    public static final String EXTRA_URL = "extra_url";
    public static final String EXTRA_TITLE = "extra_title";

    public static final int MAX_ENTRIES = 5;


    protected Toolbar mToolbar;
    private LinearLayout mQuickReturnLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_base);
        getRevolution();
//        Page3Fragment fragment = Page3Fragment.newInstance("Test", "https://vozforums.com/showthread.php?t=5232013");
//        Page3Fragment fragment = Page3Fragment.newInstance("Test", "https://vozforums.com/showthread.php?t=5139353");
        /*Page1Fragment fragment = Page1Fragment.newInstance("test", "");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container, fragment,"AAAA");
        ft.commit();*/
    }

    protected void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(getString(R.string.app_name));
            setSupportActionBar(mToolbar);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void getRevolution() {
        Point point = new Point();
        WindowManager windowManager = getWindowManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            windowManager.getDefaultDisplay().getSize(point);
            Global.width = point.x;
            Global.height = point.y;
            return;
        }
        Display defaultDisplay = windowManager.getDefaultDisplay();
        Global.width = defaultDisplay.getWidth();
        Global.height = defaultDisplay.getHeight();
    }

    protected void initQuickReturn() {
        int i = 0;
        this.mQuickReturnLayout = (LinearLayout) findViewById(R.id.compose_button_bar);
        this.mQuickReturnLayout.removeAllViews();
        int convertDpToPx = Util.convertDpToPx(this, 40);

        SharedPreferences settings = getSharedPreferences("Setting", 0);
        Global.iNumQuickLink = settings.getString("NUMQUICKLINK", "5");
        int parseInt = Integer.parseInt(Global.iNumQuickLink);
        ImageView imageView = new ImageView(this.mQuickReturnLayout.getContext());
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.menu_up));
        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(convertDpToPx, convertDpToPx);
//        Global.setBackgroundItemThread(imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(layoutParams);
        this.mQuickReturnLayout.addView(imageView);
        ViewGroup.LayoutParams layoutParams2 =
                parseInt >= 5 ? new LinearLayout.LayoutParams((Global.width - (convertDpToPx * 2)) / 5, -1) : new LinearLayout.LayoutParams(0, -1, 1);
        while (i < Integer.parseInt(Global.iNumQuickLink)) {
            TextView textView = new TextView(this.mQuickReturnLayout.getContext());
//            Global.setBackgroundItemThread(textView);
            textView.setMinHeight(Util.convertDpToPx(this, 40));
            if (i % 5 == 0) {
                textView.setTextColor(getResources().getColor(R.color.colorquiclink1));
                textView.setText(settings.getString("QUICKLINK" + i, "f=0"));
            } else if (i % 5 == 1) {
                textView.setTextColor(getResources().getColor(R.color.colorquiclink2));
                textView.setText(settings.getString("QUICKLINK" + i, "f=32"));
            } else if (i % 5 == 2) {
                textView.setTextColor(getResources().getColor(R.color.colorquiclink3));
                textView.setText(settings.getString("QUICKLINK" + i, "f=26"));
            } else if (i % 5 == 3) {
                textView.setTextColor(getResources().getColor(R.color.colorquiclink4));
                textView.setText(settings.getString("QUICKLINK" + i, "f=17"));
            } else if (i % 5 == 4) {
                textView.setTextColor(getResources().getColor(R.color.colorquiclink1));
                textView.setText(settings.getString("QUICKLINK" + i, "f=33"));
            }
            textView.setGravity(17);
            textView.setLayoutParams(layoutParams2);
//            textView.setOnLongClickListener(new AnonymousClass16(i));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v instanceof  TextView) {
                        String charSequence = ((TextView)v).getText().toString();
                        Intent intent;
                        if (charSequence.equals("f=0")) {
                            intent = new Intent(BaseActivity.this, MainTabActivity.class);
                        } else {
                            intent = new Intent(BaseActivity.this, PageThreadsActivity.class);
                            intent.putExtra(EXTRA_URL, "forumdisplay.php?" + charSequence);
                            intent.putExtra(EXTRA_TITLE, charSequence);
                        }
                        if (intent != null) {
                            startActivity(intent);
                        }
                    }
                }
            });
            this.mQuickReturnLayout.addView(textView);
            i += 1;
        }
        ImageView imageView2 = new ImageView(this.mQuickReturnLayout.getContext());
        imageView2.setImageDrawable(getResources().getDrawable(R.drawable.menu_down));
        ViewGroup.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(convertDpToPx, convertDpToPx);
//        Global.setBackgroundItemThread(imageView2);
        imageView2.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView2.setLayoutParams(layoutParams3);
        this.mQuickReturnLayout.addView(imageView2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MainApplication.getRefWatcher(this);
        if (refWatcher != null) {
            refWatcher.watch(this);
        }
    }
}
