package com.whoami.voz.ui.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import com.nna88.voz.contain.Thread;
import com.nna88.voz.listview.listViewCustom2;
import com.nna88.voz.main.BuildConfig;
import com.nna88.voz.main.Global;
import com.nna88.voz.main.R;
import com.nna88.voz.mysqlite.Comment;
import com.nna88.voz.util.Util;
import com.whoami.voz.ui.activity.BaseActivity;
import com.whoami.voz.ui.utils.HtmlLoader;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Page2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Page2Fragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<Thread> ListContains;
    private listViewCustom2 adapter;
    List<Comment> lComment;
    private String mTextTitle;
    private String url;

    // TODO: Rename and change types of parameters
    private String mParamUrl;
    private String mParamTitle;
    private ListView mList;
    private Toolbar toolbar;
    private LinearLayout mLayoutProgress;


    public Page2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Page2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Page2Fragment newInstance(String param1, String param2) {
        Page2Fragment fragment = new Page2Fragment();
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
            mParamUrl = getArguments().getString(ARG_PARAM1);
            mParamTitle = getArguments().getString(ARG_PARAM2);
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
        toolbar = (Toolbar) view.findViewById(R.id.my_awesome_toolbar);
        toolbar.setTitle(mParamTitle);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        mLayoutProgress = (LinearLayout) view.findViewById(R.id.layoutprogress);
        mList = (ListView) view.findViewById(R.id.content_frame);
        ListContains = new ArrayList();
        adapter = new listViewCustom2(getContext(), this.ListContains);
        adapter.setSize(1);
        mList.setAdapter(this.adapter);

        showLoadingView(true);
        HtmlLoader.getInstance().fetchData(Global.URL + mParamUrl, new HtmlLoader.HtmlLoaderListener() {
            @Override
            public void onCallback(Document doc) {
                try {

                    parseDataPage2(doc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        this.mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                try {
                    if (ListContains.get(i).isUrl() == 0) {
                        if (ListContains.get(i).UrlThread() != null) {
                            Page2Fragment fragment = Page2Fragment.newInstance(ListContains.get(i).UrlThread(), ListContains.get(i).Thread());
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.add(R.id.container, fragment, "AAAA");
                            ft.addToBackStack("Page2Fragment");
                            ft.commit();
                        }
                    } else if (ListContains.get(i).isUrl() == 1) {
                        Page3Fragment fragment = null;

                        if (ListContains.get(i).UrlLastPosst() != null) {
                            if (ListContains.get(i).UrlLastPosst() != null) {
                                fragment = Page3Fragment.newInstance("", ListContains.get(i).UrlLastPosst());
                            }
                        } else if (ListContains.get(i).UrlThread() != null) {
                            fragment = Page3Fragment.newInstance(ListContains.get(i).Thread(), ListContains.get(i).UrlThread());
                        }
                        if (fragment != null) {
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.add(R.id.container, fragment, "AAAA");
                            ft.addToBackStack("Page3Fragment");
                            ft.commit();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void parseDataPage2(Document doc) throws Exception {
        if (doc != null) {
            this.ListContains.clear();
            Element first = doc.select("a[href=private.php]").first();
            //todo set page
//            setPage(parsePage(0, 0));
//            if (first != null) {
//                this.mUser.setUserId(((Element) doc.select("td[class=alt2]").get(0)).select("a[href*=mem]").attr("href").split("=")[1]);
//                first = doc.select("input[name*=securitytoken]").first();
//                if (first != null) {
//                    this.mUser.setToken(first.attr("value"));
//                }
//            }
            first = doc.select("tbody:has(form[id])").first();
            if (first == null) {
                toast("main Element null");
                parserNotification();
                return;
            }
            Element first2 = first.select("td[class=tcat]:contains(Threads in Forum)").first();
            if (first2 != null) {
//                String mTitle = first2.select("span").text();
//                mTitle = mTitle.substring(2, mTitle.length());
//                toolbar.setTitle(mTitle);
            }
            first2 = first.select("tbody[id*=threadbits_forum]").first();
            if (first2 == null) {
                toast("elebody null");
                return;
            }
            Bitmap loadBitmapAssert;
            String attr;
            String text;
            Elements select = first2.select("tr:has(td[id*=td_threads])");
            Elements select2 = first.select("tr > td[class=alt1Active]");
            Iterator it = select.iterator();
            Iterator it2 = select2.iterator();
            if (it2.hasNext()) {
                this.ListContains.add(new Thread("Sub-Forum", null, null, null, null, null, null));
            }
            String str = null;
            String str2 = null;
            Bitmap bitmap = null;
            String str3 = null;
            String str4 = null;
            while (it2.hasNext()) {
                first = (Element) it2.next();
                Element first3 = first.select("td > img[id]").first();
                Element nextElementSibling = first.nextElementSibling();
                Element nextElementSibling2 = nextElementSibling.nextElementSibling();
                Element nextElementSibling3 = nextElementSibling2.nextElementSibling();
                loadBitmapAssert = (!Global.bTopicHeader || first3 == null) ? bitmap : loadBitmapAssert(first3.select("img").attr("src"));
                if (first != null) {
                    str2 = first.text();
                }
                attr = first.select("div > a").first() != null ? first.select("div > a").first().attr("href") : str3;
                if (nextElementSibling.select("span > a").first() != null) {
                    text = nextElementSibling.select("span > a").first().text();
                    str = nextElementSibling.select("span > a").first().attr("href");
                } else {
                    text = str4;
                }
                if (!(nextElementSibling2 == null || nextElementSibling3 == null)) {
                    text = text + "\nReplie:" + nextElementSibling2.text() + " - View:" + nextElementSibling3.text();
                }
                this.ListContains.add(new Thread(str2, text, null, null, loadBitmapAssert, attr, str));
                bitmap = loadBitmapAssert;
                str3 = attr;
                str4 = text;
            }
            this.ListContains.add(new Thread("Thread", null, null, null, null, null, null));
            text = null;
            loadBitmapAssert = bitmap;
            attr = str3;
            String str5 = str4;
            while (it.hasNext()) {
                String attr2;
                str = null;
                str2 = BuildConfig.FLAVOR;
                Element element = (Element) it.next();
                Element first4 = element.select("a[id*=thread_title").first();
                if (first4 != null) {
                    if (element.select("a[href*=prefixid]").first() != null) {
                        str2 = element.select("a[href*=prefixid]").first().text() + "-";
                        attr2 = element.select("a[href*=prefixid]").attr("href");
                    } else {
                        attr2 = text;
                    }
                    str2 = str2 + element.select("a[id*=thread_title").first().text();
                    attr = element.select("a[id*=thread_title").first().attr("href");
                    str3 = attr2;
                } else {
                    str3 = text;
                }
                attr2 = element.select("td[class=alt2]:has(span)").first() != null ? element.select("td[class=alt2]:has(span)").first().text() : str5;
                if (element.select("td[align=center]").first() != null) {
                    attr2 = attr2 + "\nReplies:" + ((Element) element.select("td[align=center]").get(0)).text() + " - Views:" + ((Element) element.select("td[align=center]").get(1)).text();
                }
                text = element.select("div:has(span[onclick*=member.php])").first() != null ? element.select("div:has(span[onclick*=member.php])").first().text() + " - " + attr2 : attr2;
                if (Global.bTopicHeader && element.select("img[id*=thread_statusicon]").first() != null) {
                    loadBitmapAssert = loadBitmapAssert(element.select("img[id*=thread_statusicon]").first().attr("src"));
                }
                if (element.select("a[id*=thread_gotonew]").first() != null) {
                    str = element.select("a[id*=thread_gotonew]").first().attr("href");
                }
                Thread thread = new Thread(str2, text, null, null, loadBitmapAssert, attr, str);
                thread.mPrefixLink = str3;
                thread.mIdThread = attr.split("t=")[1];
                if (element.select("a:has(img[src*=lastpost])").first() != null) {
                    thread.setUrlLastPost(element.select("a:has(img[src*=lastpost])").first().attr("href"));
                }
                if (first4.hasClass("vozsticky")) {
                    thread.setSticky(true);
                } else {
                    thread.setSticky(false);
                }
//                if (checkBookmark(thread.mIdThread)) {
//                    thread.isBookmark = true;
//                    this.mDataBookmark.updateBookmark(thread);
//                }
                this.ListContains.add(thread);
                str5 = text;
                text = str3;
            }
//            this.mItemCount = this.adapter.getCount();
//            if (this.mItemOffsetY == null) {
//                this.mItemOffsetY = new int[(this.mItemCount + 1)];
//                this.mItemtemp = new int[(this.mItemCount + 1)];
//            }
//            this.scrollIsComputed = true;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showLoadingView(false);
                    adapter.notifyDataSetChanged();
                    mList.clearFocus();
                }
            });

//            mList.post(new Runnable() {
//                public void run() {
//                    mList.setSelection(0);
//                }
//            });
        }
    }

    private void parserNotification() {

    }


    protected void setPage(String str) {
//        setEnableBackFooter(true);
//        setEnableNextFooter(true);
//        if (this.iPage == 3) {
//            setEnableBackHeader(true);
//            setEnableNextHeader(true);
//        }
//        if (str == null) {
//            this.butPageFooter.setText("Page 1/1");
//            setEnableBackFooter(false);
//            setEnableNextFooter(false);
//            if (this.iPage == 3) {
//                this.butPageHeader.setText("Page 1/1");
//                setEnableBackHeader(false);
//                setEnableNextHeader(false);
//                return;
//            }
//            return;
//        }
//        if (str.contains("/")) {
//            if (str.split("/")[STATE_ONSCREEN].equals("1")) {
//                setEnableBackFooter(false);
//            }
//            if (str.split("/")[STATE_ONSCREEN].equals(str.split("/")[STATE_OFFSCREEN])) {
//                setEnableNextFooter(false);
//            }
//        }
//        this.butPageFooter.setText(" Page " + str + " ");
//        if (this.iPage == 3) {
//            if (str.contains("/")) {
//                if (str.split("/")[STATE_ONSCREEN].equals("1")) {
//                    setEnableBackHeader(false);
//                } else {
//                    setEnableBackHeader(true);
//                }
//                if (str.split("/")[STATE_ONSCREEN].equals(str.split("/")[STATE_OFFSCREEN])) {
//                    setEnableNextHeader(false);
//                } else {
//                    setEnableNextHeader(true);
//                }
//            }
//            this.butPageHeader.setText(" Page " + str + " ");
//        }
    }

    void toast(String str) {
        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
    }

    Bitmap loadBitmapAssert(String str) {
        int i = 0;
        if (str.contains("smilies")) {
            String str2 = str.substring(0, str.length() - 3) + "gif";
            try {
                String[] list = getContext().getAssets().list("images/smilies/Off");
                int length = list.length;
                while (i < length) {
                    if ((str.substring(0, str.length() - 3) + "png").contains(list[i])) {
                        str = str.substring(0, str.length() - 3) + "png";
                        break;
                    }
                    i += 1;
                }
                str = str2;
            } catch (Exception e) {
                e.printStackTrace();
                str = str2;
            }
        }
        try {
            InputStream open = getContext().getAssets().open(str);
            Bitmap decodeStream = BitmapFactory.decodeStream(open);
            if (str.contains("statusicon")) {
                int convertDpToPx = Util.convertDpToPx(getContext().getApplicationContext(), 24);
                open.close();
                return Bitmap.createScaledBitmap(decodeStream, convertDpToPx, convertDpToPx, false);
            }
            open.close();
            return decodeStream;
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private void showLoadingView(boolean isShow) {
        if (mLayoutProgress != null) {
            mLayoutProgress.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
    }
}
