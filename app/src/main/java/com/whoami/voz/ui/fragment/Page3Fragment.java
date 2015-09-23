package com.whoami.voz.ui.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.nna88.voz.contain.Post;
import com.nna88.voz.listview.listViewCustom3;
import com.nna88.voz.main.FullScreenImage;
import com.nna88.voz.main.Global;
import com.nna88.voz.main.MyNetwork;
import com.nna88.voz.main.R;
import com.nna88.voz.parserhtml.parser;
import com.nna88.voz.quickaction.QuickAction;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Page3Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Page3Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;


//    private TextView mTextView;
//
//    private ArrayList<Post> ListContains;
//    private listViewCustom3 adapter;
//    private Bitmap bmImageFailed;
//    private Bitmap bmImageStart;
//    private FullScreenImage fullScreen;
//    private String humanverify_hash;
//    private int iCallFromNotification;
//    private int iCallFromNotificationQuote;
//    private int iPostType;
//    ImageLoader imageLoader;
//    int indexI;
//    int indexJ;
//    private ArrayList<EmoClass2> lEmo;
//    private ArrayList<String> lImage;
//    private String linkapicaptcha;
//    private ArrayList<TaskLoadAvatart> mListTaskDownAvatart;
//    private ArrayList<DownImageAttach> mListTaskImageAttach;
//    private String mPostId;
//    private QuickAction mQuickAction;
//    private String mTextTitle;
//    private MyNetwork myNetwork;
//    private String recaptcha_challenge_field;
//    private String url;
//    private String urlCaptcha;
//
//    // base activity
//    private parser mParser;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Page3Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Page3Fragment newInstance(int param1, String param2) {
        Page3Fragment fragment = new Page3Fragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Page3Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_page3, container, false);

//        mTextView = (TextView) view.findViewById(R.id.tv_test);
//        if (mTextView != null) {
//            mTextView.setText(mParam1  + "");
//        }

//        this.ListContains = new ArrayList();
//        this.mListTaskImageAttach = new ArrayList();
//        this.mListTaskDownAvatart = new ArrayList();
//        this.bmImageStart = BitmapFactory.decodeResource(getResources(), R.drawable.stub_image);
//        this.bmImageFailed = BitmapFactory.decodeResource(getResources(), R.drawable.image_for_empty_url);
//        this.adapter = new listViewCustom3(getActivity(), ListContains, mImageLoad.imageLoader, bmImageStart, mTextSize);
//        this.adapter.setSize(this.mTextSize);
//        this.mList.setAdapter(this.adapter);
//        this.myNetwork = new MyNetwork();
//        if (getIntent().getIntExtra("NOTIFICATION", 1) != 1) {
//            this.iCallFromNotification = getIntent().getIntExtra("NOTIFICATION", 1);
//        }
//        if (getIntent().getIntExtra("NOTIFICATIONQUOTE", 1) != 1) {
//            this.iCallFromNotificationQuote = getIntent().getIntExtra("NOTIFICATIONQUOTE", 1);
//        }
//        this.url = getIntent().getStringExtra("URL");
//        if (this.url.contains(Global.URL) || this.url.contains(Global.URL2)) {
//            this.mParser.setUrl(getIntent().getStringExtra("URL"));
//        } else {
//            this.mParser.setUrl(Global.URL + getIntent().getStringExtra("URL"));
//        }
//        this.mTask.execute(new Integer[]{Integer.valueOf(0)});


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

}
