package com.whoami.voz.ui.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.whoami.voz.R;
import com.whoami.voz.ui.activity.BaseActivity;
import com.whoami.voz.ui.pagethreads.PageThreadsActivity;
import com.whoami.voz.ui.adapter.list.Page1ListViewAdapter;
import com.whoami.voz.ui.contain.Forum;
import com.whoami.voz.ui.utils.HtmlLoader;
import com.whoami.voz.ui.utils.UserInfo;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment {
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
        HtmlLoader.getInstance().fetchData("https://vozforums.com", 0, new HtmlLoader.HtmlLoaderListener() {
            @Override
            public void onCallback(Document doc, int page) {
                try {
                    parseDataPage1(doc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    void parseDataPage1(Document doc) throws Exception {
        try {
            if (doc != null) {
                Element first;
                forumsList.clear();
                //                if (this.doc.select("a[href=private.php]").first() != null) {
                //                    this.mUser.setUserId(((Element) this.doc.select("td[class=alt2]").get(0)).select("a[href*=mem]").attr("href").split("=")[1]);
                //                    first = this.doc.select("input[name*=securitytoken]").first();
                //                    if (first != null) {
                //                        this.mUser.setToken(first.attr("value"));
                //                    }
                //                }
                Iterator it = doc.select("td[class*=tcat]").iterator();
                String str = null;
                String str2 = null;
                String str3 = null;
                while (it.hasNext()) {
                    first = (Element) it.next();
                    if (!first.text().contains("Welcome to the vozForums")) {
                        forumsList.add(new Forum(first.text(), first.select("[href~=forum]").attr("href"), null));
                        first = first.parent().parent().nextElementSibling();
                        if (first != null) {
                            Iterator it2 = first.select("tr").iterator();
                            while (it2.hasNext()) {
                                first = ((Element) it2.next()).select("div:has(a[href*=forumdisplay]").first();
                                if (first != null) {
                                    str = first.select("a").text();
                                    str3 = first.select("a").attr("href");
                                    str2 = first.select("span").text();
                                }
                                forumsList.add(new Forum(str, str3, str2));
                                //                                this.sForum.append(str).append(",").append(str3).append(";");
                            }
                        }
                    }
                }
                if (forumsList.size() < 2) {
                    //                    parserNotification();
                    return;
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showLoadingView(false);
                            adapter.setData(forumsList);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showLoadingView(boolean isShow) {
        if (mLayoutProgress != null) {
            mLayoutProgress.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
    }

}
