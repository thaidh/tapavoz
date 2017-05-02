package com.whoami.voz.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.whoami.voz.ui.pageposts.PagePostActivity;

/**
 * Created by thaidh on 9/18/16.
 */
public class IntentHandlerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = getIntent().getData();
        if (uri != null) {
            try {
//                URL url = new URL(uri.getScheme(), uri.getHost(), uri.getPath());
                Intent newIntent = new Intent(this, PagePostActivity.class);
                newIntent.putExtra(BaseActivity.EXTRA_URL, uri.toString());
                newIntent.putExtra(BaseActivity.EXTRA_TITLE, "");
                startActivity(newIntent);

                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
