package com.whoami.voz.ui.activity;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.nna88.voz.main.Global;
import com.nna88.voz.main.R;
import com.whoami.voz.ui.fragment.Page1Fragment;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base);
        getRevolution();
//        String url = getIntent().getStringExtra("URL");
//        if (url.contains(Global.URL) || url.contains(Global.URL2)) {
//        } else {
//            url = Global.URL + url;
//        }
//        Page3Fragment fragment = Page3Fragment.newInstance(100, url);
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.add(R.id.container, fragment,"AAAA");
//        ft.commit();


        Page1Fragment fragment = Page1Fragment.newInstance("test", "");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container, fragment,"AAAA");
        ft.commit();
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
}
