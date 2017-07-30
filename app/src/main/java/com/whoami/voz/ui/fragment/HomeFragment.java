package com.whoami.voz.ui.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.whoami.voz.R;
import com.whoami.voz.retrofit.VozService;
import com.whoami.voz.retrofit.data.ForumData;
import com.whoami.voz.ui.activity.BaseActivity;
import com.whoami.voz.ui.adapter.list.Page1ListViewAdapter;
import com.whoami.voz.ui.contain.Forum;
import com.whoami.voz.ui.pagethreads.PageThreadsActivity;
import com.whoami.voz.ui.utils.UserInfo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private UserInfo mUser;
    private ObservableListView mList;
    private ArrayList<Forum> forumsList;
    private Page1ListViewAdapter adapter;
    private StringBuilder sForum;
    private LinearLayout mLayoutProgress;


    public HomeFragment() {
        // Required empty public constructor
        this.sForum = new StringBuilder();
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_page1, container, false);
        final ObservableListView scrollView = (ObservableListView) view.findViewById(R.id.scroll);
        scrollView.addHeaderView(inflater.inflate(R.layout.padding, scrollView, false));
        Activity parentActivity = getActivity();
        if (parentActivity instanceof ObservableScrollViewCallbacks) {
            // Scroll to the specified offset after layout
//            Bundle args = getArguments();
//            if (args != null && args.containsKey(ARG_SCROLL_Y)) {
//                final int scrollY = args.getInt(ARG_SCROLL_Y, 0);
//                ScrollUtils.addOnGlobalLayoutListener(scrollView, new Runnable() {
//                    @Override
//                    public void run() {
//                        scrollView.scrollTo(0, scrollY);
//                    }
//                });
//            }

            // TouchInterceptionViewGroup should be a parent view other than ViewPager.
            // This is a workaround for the issue #117:
            // https://github.com/ksoichiro/Android-ObservableScrollView/issues/117
            scrollView.setTouchInterceptionViewGroup((ViewGroup) parentActivity.findViewById(R.id.root));

            scrollView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentActivity);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList = (ObservableListView) view.findViewById(R.id.scroll);
        mLayoutProgress = (LinearLayout) view.findViewById(R.id.layoutprogress);
        this.forumsList = new ArrayList();
        this.adapter = new Page1ListViewAdapter(getContext(), this.forumsList);
        this.mList.setAdapter(this.adapter);
        showLoadingView(true);
        VozService.getInstance().getForums("https://vozforums.com").enqueue(new Callback<ForumData>() {
            @Override
            public void onResponse(Call<ForumData> call, Response<ForumData> response) {
                if (response.isSuccessful()) {
                    ForumData forumData = response.body();
                    forumsList = forumData.forumList;
                    showLoadingView(false);
                    adapter.setData(forumsList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ForumData> call, Throwable t) {
                t.printStackTrace();
            }
        });
        
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int i = position - mList.getHeaderViewsCount();
                if (forumsList.get(i).UrlForum() != null) {
                    Intent intent = new Intent(getActivity(), PageThreadsActivity.class);
                    intent.putExtra(BaseActivity.EXTRA_URL, forumsList.get(i).UrlForum());
                    intent.putExtra(BaseActivity.EXTRA_TITLE, forumsList.get(i).Forum());
                    startActivity(intent);
                }
            }
        });
    }

    private void showLoadingView(boolean isShow) {
        if (mLayoutProgress != null) {
            mLayoutProgress.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
    }

}
