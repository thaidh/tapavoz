package com.whoami.voz.ui.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.nna88.voz.contain.Forum;
import com.nna88.voz.listview.Page1ListViewAdapter;
import com.nna88.voz.main.R;
import com.nna88.voz.util.UserInfo;
import com.whoami.voz.ui.utils.HtmlLoader;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Page1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Page1Fragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean scrollIsComputed;
    private UserInfo mUser;
    private ListView mList;
    private ArrayList<Forum> forumsList;
    private Page1ListViewAdapter adapter;
    private StringBuilder sForum;
    private LinearLayout mLayoutProgress;


    public Page1Fragment() {
        // Required empty public constructor
        this.sForum = new StringBuilder();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Page1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Page1Fragment newInstance(String param1, String param2) {
        Page1Fragment fragment = new Page1Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_page1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.my_awesome_toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        mList = (ListView) view.findViewById(R.id.content_frame);
        mLayoutProgress = (LinearLayout) view.findViewById(R.id.layoutprogress);
        this.forumsList = new ArrayList();
        this.adapter = new Page1ListViewAdapter(getContext(), this.forumsList);
        this.mList.setAdapter(this.adapter);
        Uri data = null;
//        if (data != null) {
//        } else {
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
//        }
        this.mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (forumsList.get(i).UrlForum() != null) {
                    Page2Fragment fragment = Page2Fragment.newInstance(forumsList.get(i).UrlForum(), forumsList.get(i).Forum());
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.container, fragment, "AAAA");
                    ft.addToBackStack("Page2Fragment");
                    ft.commit();
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
                //            writeStringForums(this.sForum.toString());
                //            this.mItemCount = this.adapter.getCount();
                //            if (this.mItemOffsetY == null) {
                //                this.mItemOffsetY = new int[(this.mItemCount + 1)];
                //                this.mItemtemp = new int[(this.mItemCount + 1)];
                //            }
                //                this.adapter.notifyDataSetChanged();
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
