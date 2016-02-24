package com.nna88.voz.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

import com.nna88.voz.main.R;

public class NavigationDrawerActivity extends ActionBarActivity {
    protected DrawerLayout mDrawerLayout;
    private ListView mDrawerLeftList;
    private CharSequence mDrawerTitle;
    protected ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle;
    private Toolbar toolbar;

    /* renamed from: com.nna88.voz.ui.NavigationDrawerActivity.1 */
    class AnonymousClass1 extends ActionBarDrawerToggle {
        AnonymousClass1(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int i, int i2) {
            super(activity, drawerLayout, i, i2);
        }

        public void onDrawerClosed(View view) {
            if (VERSION.SDK_INT > 11) {
                NavigationDrawerActivity.this.getSupportActionBar().setTitle(NavigationDrawerActivity.this.mTitle);
                NavigationDrawerActivity.this.invalidateOptionsMenu();
                return;
            }
            NavigationDrawerActivity.this.getSupportActionBar().setTitle(NavigationDrawerActivity.this.mTitle);
            NavigationDrawerActivity.this.supportInvalidateOptionsMenu();
        }

        public void onDrawerOpened(View view) {
            if (VERSION.SDK_INT > 11) {
                if (view.getId() == R.id.left_drawer) {
                    NavigationDrawerActivity.this.getSupportActionBar().setTitle((CharSequence) "User");
                    NavigationDrawerActivity.this.invalidateOptionsMenu();
                }
            } else if (view.getId() == R.id.left_drawer) {
                NavigationDrawerActivity.this.getSupportActionBar().setTitle((CharSequence) "User");
                NavigationDrawerActivity.this.supportInvalidateOptionsMenu();
            }
        }
    }

    protected void closeDrawer() {
        this.mDrawerLayout.closeDrawers();
    }

    protected DrawerLayout getmDrawerLayout() {
        return this.mDrawerLayout;
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mDrawerToggle.onConfigurationChanged(configuration);
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.navigationdrawer);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.mTitle = getTitle();
        this.mDrawerTitle = "Open navigation";
        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.mDrawerLayout.setDrawerShadow((int) R.drawable.drawer_shadow, (int) GravityCompat.START);
        this.mDrawerLeftList = (ListView) findViewById(R.id.left_drawer);
        this.mDrawerToggle = new AnonymousClass1(this, this.mDrawerLayout, this.toolbar, R.string.drawer_open, R.string.drawer_close);
        this.mDrawerLayout.setDrawerListener(this.mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().invalidateOptionsMenu();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
//        return super.onCreateOptionsMenu(menu);
    }

    protected void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        this.mDrawerToggle.syncState();
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    protected void setLeftDrawer(SidebarAdapter sidebarAdapter) {
        this.mDrawerLeftList.setAdapter(sidebarAdapter);
    }

    public void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        getSupportActionBar().setTitle(this.mTitle);
    }
}
