package com.whoami.voz.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.nna88.voz.main.Global;
import com.nna88.voz.main.R;
import com.whoami.voz.ui.adapter.MyFragmentAdapter;
import com.whoami.voz.ui.fragment.Page3Fragment;

public class BaseActivity extends AppCompatActivity {
    ViewPager viewPager;
    MyFragmentAdapter myFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        viewPager = (ViewPager) findViewById(R.id.pager);
        String url = getIntent().getStringExtra("URL");
        if (url.contains(Global.URL) || url.contains(Global.URL2)) {
        } else {
            url = Global.URL + url;
        }
        Page3Fragment fragment = Page3Fragment.newInstance(100, url);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container, fragment,"AAAA");
        ft.commit();

//        if (!TextUtils.isEmpty(url)) {
//            myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), url);
//            viewPager.setAdapter(myFragmentAdapter);
//
//        }
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
}
