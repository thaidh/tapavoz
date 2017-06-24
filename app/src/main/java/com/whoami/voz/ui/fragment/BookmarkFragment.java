package com.whoami.voz.ui.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.whoami.voz.R;
import com.whoami.voz.ui.activity.BaseActivity;
import com.whoami.voz.ui.pageposts.PagePostActivity;
import com.whoami.voz.ui.adapter.list.Page2ListViewAdapter;
import com.whoami.voz.ui.contain.VozThread;
import com.whoami.voz.ui.main.MainApplication;
import com.whoami.voz.ui.mysqlite.DatabaseHelper;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookmarkFragment extends Fragment {

    private View mRootView;
    private ObservableListView mListView;
    private Page2ListViewAdapter mAdapter;
    private ArrayList<VozThread> mBookmarkList = new ArrayList<>();
    public BookmarkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_bookmark, container, false);
        mListView = (ObservableListView) mRootView.findViewById(R.id.lv_bookmark);
        mListView.addHeaderView(inflater.inflate(R.layout.padding, mListView, false));
        Activity parentActivity = getActivity();
        if (parentActivity instanceof ObservableScrollViewCallbacks) {
            mListView.setTouchInterceptionViewGroup((ViewGroup) parentActivity.findViewById(R.id.root));
            mListView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentActivity);
        }

        mAdapter = new Page2ListViewAdapter(getActivity(), mBookmarkList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PagePostActivity.class);
                VozThread vozThread = mBookmarkList.get(position - mListView.getHeaderViewsCount());
                intent.putExtra(BaseActivity.EXTRA_URL, vozThread.UrlThread());
                intent.putExtra(BaseActivity.EXTRA_TITLE, vozThread.Thread());
                startActivity(intent);
            }
        });
        return mRootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        mBookmarkList = DatabaseHelper.getInstance(MainApplication.getAppContext()).getAllBookmarkThread();
        Collections.reverse(mBookmarkList);
        mAdapter.setData(mBookmarkList);
        mAdapter.notifyDataSetChanged();
    }
}
