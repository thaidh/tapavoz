package com.whoami.voz.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.whoami.voz.ui.fragment.Page3Fragment;

import java.util.Random;

/**
 * Created by CPU11307-local on 9/22/2015.
 */
public class MyFragmentAdapter extends FragmentStatePagerAdapter{

    public MyFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return Page3Fragment.newInstance((new Random()).nextInt(), "");
    }

    @Override
    public int getCount() {
        return 5;
    }
}
