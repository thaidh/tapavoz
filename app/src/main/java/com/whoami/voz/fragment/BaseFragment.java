package com.whoami.voz.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.leakcanary.RefWatcher;
import com.whoami.voz.R;
import com.whoami.voz.activity.BaseActivity;
import com.whoami.voz.activity.MainTabActivity;
import com.whoami.voz.pagethreads.PageThreadsActivity;
import com.whoami.voz.main.Global;
import com.whoami.voz.main.MainApplication;
import com.whoami.voz.utils.Utils;

/**
 * Created by JonSnow on 2/18/2016.
 */
public class BaseFragment extends Fragment {
    protected LinearLayout mQuickReturnLayout;

    @Override public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MainApplication.getRefWatcher(getActivity());
        if (refWatcher != null) {
            refWatcher.watch(this);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initQuickReturn(view);
    }

    private void initQuickReturn(View view) {
        int i = 0;
        this.mQuickReturnLayout = (LinearLayout) view.findViewById(R.id.compose_button_bar);
        this.mQuickReturnLayout.removeAllViews();
        int convertDpToPx = Utils.convertDpToPx(getContext(), 40);

        SharedPreferences settings = getActivity().getSharedPreferences("Setting", 0);
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
            textView.setMinHeight(Utils.convertDpToPx(getContext(), 40));
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
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v instanceof  TextView) {
                        String charSequence = ((TextView)v).getText().toString();
                        Intent intent;
                        if (charSequence.equals("f=0")) {
                            intent = new Intent(getActivity(), MainTabActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        } else {
                            intent = new Intent(getActivity(), PageThreadsActivity.class);
                            intent.putExtra(BaseActivity.EXTRA_URL, "forumdisplay.php?" + charSequence);
                            intent.putExtra(BaseActivity.EXTRA_TITLE, charSequence);
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
}
