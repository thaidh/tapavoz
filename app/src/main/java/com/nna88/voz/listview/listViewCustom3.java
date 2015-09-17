package com.nna88.voz.listview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ViewCompat;
import android.text.Layout.Alignment;
import android.text.SpannableString;
import android.text.style.AlignmentSpan.Standard;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nna88.voz.PhotoView.PhotoViewAttacher;
import com.nna88.voz.contain.Post;
import com.nna88.voz.listview.TextViewFixTouchConsume.LocalLinkMovementMethod;
import com.nna88.voz.main.Global;
import com.nna88.voz.main.R;
import com.nna88.voz.quickaction.QuickAction;
import com.nna88.voz.ui.JellyBeanSpanFixTextView;
import com.nna88.voz.util.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import java.util.ArrayList;

public class listViewCustom3 extends BaseAdapter {
    private Bitmap bmImageFailed;
    private Bitmap bmImageStart;
    private Context context;
    private Drawable d;
    private int dp32;
    private Drawable drawableAvatar;
    private Drawable drawableOffline;
    private Drawable drawableOnline;
    private int end;
    private ViewHolder holder;
    private int i2;
    protected ImageLoader imageLoader;
    private LayoutInflater inflater;
    private ArrayList<String> lEmoImage;
    private ArrayList<String> lImage;
    private listViewCustom3 mAdapter;
    private OnImageClickListestener mImageClickListener;
    private ImageSize mImageSize;
    private float mTextSize;
    private float mTextSize1;
    private float mTextSize3;
    private float mTextSize4;
    private ArrayList<Post> mcontains;
    private DisplayImageOptions options;
    private ImageSpan span;
    private SpannableString ss;
    private int start;

    /* renamed from: com.nna88.voz.listview.listViewCustom3.1 */
    class AnonymousClass1 extends ClickableSpan {
        final /* synthetic */ String val$url;

        AnonymousClass1(String str) {
            this.val$url = str;
        }

        public void onClick(View view) {
            if (listViewCustom3.this.mImageClickListener == null) {
                return;
            }
            if (this.val$url.contains("attachmentid") || this.val$url.contains("http://") || this.val$url.contains("https://")) {
                listViewCustom3.this.mImageClickListener.onImageClick(0, this.val$url);
            }
        }
    }

    /* renamed from: com.nna88.voz.listview.listViewCustom3.2 */
    class AnonymousClass2 implements ImageLoadingListener {
        final /* synthetic */ int val$i;
        final /* synthetic */ Post val$mPost;

        AnonymousClass2(Post post, int i) {
            this.val$mPost = post;
            this.val$i = i;
        }

        public void onLoadingCancelled(String str, View view) {
            this.val$mPost.image.SetBitmap(this.val$i, listViewCustom3.this.bmImageFailed);
            listViewCustom3.this.mAdapter.notifyDataSetChanged();
            listViewCustom3.this.loadIamge(this.val$mPost, this.val$i + 1);
        }

        public void onLoadingComplete(String str, View view, Bitmap bitmap) {
            if (bitmap != null) {
                try {
                    this.val$mPost.image.SetBitmap(this.val$i, bitmap);
                } catch (Exception e) {
                    this.val$mPost.image.SetBitmap(this.val$i, listViewCustom3.this.bmImageFailed);
                    listViewCustom3.this.mAdapter.notifyDataSetChanged();
                    listViewCustom3.this.loadIamge(this.val$mPost, this.val$i + 1);
                    e.printStackTrace();
                    return;
                }
            }
            listViewCustom3.this.mAdapter.notifyDataSetChanged();
            listViewCustom3.this.loadIamge(this.val$mPost, this.val$i + 1);
        }

        public void onLoadingFailed(String str, View view, FailReason failReason) {
            this.val$mPost.image.SetBitmap(this.val$i, listViewCustom3.this.bmImageFailed);
            listViewCustom3.this.mAdapter.notifyDataSetChanged();
            listViewCustom3.this.loadIamge(this.val$mPost, this.val$i + 1);
        }

        public void onLoadingStarted(String str, View view) {
        }
    }

    public interface OnImageClickListestener {
        void onImageClick(int i, String str);
    }

    static class ViewHolder {
        ImageView avatar;
        JellyBeanSpanFixTextView contain;
        TextView index;
        TextView jd;
        LinearLayout layout;
        TextView posts;
        TextView time;
        TextView txtViewDevider;
        TextView user;
        TextView userTitle;

        ViewHolder() {
        }
    }

    public listViewCustom3(Context context, ArrayList<Post> arrayList, ImageLoader imageLoader, Bitmap bitmap, float f) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.drawableOnline = context.getResources().getDrawable(R.drawable.user_online);
        this.drawableOffline = context.getResources().getDrawable(R.drawable.user_offline);
        this.drawableAvatar = context.getResources().getDrawable(R.drawable.menu_usercp2);
        this.dp32 = Util.convertDpToPx(context, 32);
        int i = (int) ((((float) this.dp32) * f) / 4.0f);
        int i2 = (int) ((((float) this.dp32) * f) / 4.0f);
        this.drawableOnline.setBounds(0, 0, i, i2);
        this.drawableOffline.setBounds(0, 0, i, i2);
        this.mcontains = arrayList;
        this.mAdapter = this;
        this.lImage = new ArrayList();
        this.imageLoader = imageLoader;
        initUniversal();
        this.bmImageFailed = BitmapFactory.decodeResource(context.getResources(), R.drawable.image_for_empty_url);
        this.bmImageStart = bitmap;
    }

    private String getlinkBitmapAssert(String str) {
        int i = 0;
        if (str.contains("smilies")) {
            String str2 = str.substring(0, str.length() - 3) + "gif";
            try {
                String[] list = this.context.getAssets().list("images/smilies/Off");
                int length = list.length;
                while (i < length) {
                    if ((str.substring(0, str.length() - 3) + "png").contains(list[i])) {
                        str = str.substring(0, str.length() - 3) + "png";
                        break;
                    }
                    i++;
                }
                str = str2;
            } catch (Exception e) {
                e.printStackTrace();
                str = str2;
            }
        }
        return "assets://" + str;
    }

    private void initUniversal() {
        this.options = new Builder().imageScaleType(ImageScaleType.NONE).bitmapConfig(Config.RGB_565).build();
        setImageSize();
    }

    private void loadIamge(Post post, int i) {
        try {
            if (i < post.image.getSize()) {
                String str = post.image.getStr(i);
                if (!str.contains("http://") && !str.contains("https://")) {
                    loadIamge(post, i + 1);
                } else if (str.contains("attachmentid")) {
                    loadIamge(post, i + 1);
                } else if (Global.iSizeImage == 0) {
                    loadIamge(post, i + 1);
                } else if (post.image.getBitmap(i).equals(this.bmImageStart)) {
                    this.imageLoader.loadImage(str, this.options, new AnonymousClass2(post, i));
                } else {
                    loadIamge(post, i + 1);
                }
            }
        } catch (Exception e) {
            loadIamge(post, i + 1);
            e.printStackTrace();
        }
    }

    private void log(String str) {
        Log.d("nna", str);
    }

    private void setImageSize() {
        if (Global.iSizeImage != 0) {
            this.mImageSize = new ImageSize(Global.width / Global.iSizeImage, Global.height / Global.iSizeImage);
        } else {
            this.mImageSize = new ImageSize(Global.width / 2, Global.height);
        }
    }

    public void addImage(TextView textView, Post post) {
        this.ss = new SpannableString(post.getText());
        this.i2 = 0;
        while (this.i2 < post.font.getSize()) {
            this.start = post.font.getStart(this.i2).intValue();
            this.end = post.font.getEnd(this.i2).intValue();
            if (post.font.color(this.i2).equals("Purple")) {
                this.ss.setSpan(new ForegroundColorSpan(-8388480), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("Indigo")) {
                this.ss.setSpan(new ForegroundColorSpan(-11861886), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("SlateGray")) {
                this.ss.setSpan(new ForegroundColorSpan(-9404272), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("DarkOrchid")) {
                this.ss.setSpan(new ForegroundColorSpan(-6737204), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("Plum")) {
                this.ss.setSpan(new ForegroundColorSpan(-2252579), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("Black")) {
                this.ss.setSpan(new ForegroundColorSpan(ViewCompat.MEASURED_STATE_MASK), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("DarkRed")) {
                this.ss.setSpan(new ForegroundColorSpan(-7667712), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("Red")) {
                this.ss.setSpan(new ForegroundColorSpan(SupportMenu.CATEGORY_MASK), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("Magenta")) {
                this.ss.setSpan(new ForegroundColorSpan(-65281), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("Pink")) {
                this.ss.setSpan(new ForegroundColorSpan(-16181), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("Sienna")) {
                this.ss.setSpan(new ForegroundColorSpan(-6270419), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("DarkOrange")) {
                this.ss.setSpan(new ForegroundColorSpan(-29696), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("SandyBrown")) {
                this.ss.setSpan(new ForegroundColorSpan(-744352), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("Orange")) {
                this.ss.setSpan(new ForegroundColorSpan(-23296), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("Wheat")) {
                this.ss.setSpan(new ForegroundColorSpan(-663885), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("DarkOliveGreen")) {
                this.ss.setSpan(new ForegroundColorSpan(-11179217), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("Olive")) {
                this.ss.setSpan(new ForegroundColorSpan(-8355840), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("YellowGreen")) {
                this.ss.setSpan(new ForegroundColorSpan(-6632142), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("Yellow")) {
                this.ss.setSpan(new ForegroundColorSpan(-256), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("LemonChiffon")) {
                this.ss.setSpan(new ForegroundColorSpan(-1331), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("DarkGreen")) {
                this.ss.setSpan(new ForegroundColorSpan(-16751616), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("Green")) {
                this.ss.setSpan(new ForegroundColorSpan(-16744448), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("SeaGreen")) {
                this.ss.setSpan(new ForegroundColorSpan(-13726889), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("Lime")) {
                this.ss.setSpan(new ForegroundColorSpan(-16711936), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("PaleGreen")) {
                this.ss.setSpan(new ForegroundColorSpan(-6751336), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("DarkSlateBlue")) {
                this.ss.setSpan(new ForegroundColorSpan(-12042869), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("Teal")) {
                this.ss.setSpan(new ForegroundColorSpan(-16744320), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("MediumTurquoise")) {
                this.ss.setSpan(new ForegroundColorSpan(-12004916), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("Cyan")) {
                this.ss.setSpan(new ForegroundColorSpan(-16711681), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("PaleTurquoise")) {
                this.ss.setSpan(new ForegroundColorSpan(-5247250), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("Navy")) {
                this.ss.setSpan(new ForegroundColorSpan(-16777088), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("Blue")) {
                this.ss.setSpan(new ForegroundColorSpan(-16776961), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("RoyalBlue")) {
                this.ss.setSpan(new ForegroundColorSpan(-12490271), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("DeepSkyBlue")) {
                this.ss.setSpan(new ForegroundColorSpan(-16728065), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("LightBlue")) {
                this.ss.setSpan(new ForegroundColorSpan(-5383962), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("DarkSlateGray")) {
                this.ss.setSpan(new ForegroundColorSpan(-13676721), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("DimGray")) {
                this.ss.setSpan(new ForegroundColorSpan(-9868951), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("Gray")) {
                this.ss.setSpan(new ForegroundColorSpan(-8355712), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("Silver")) {
                this.ss.setSpan(new ForegroundColorSpan(-4144960), this.start, this.end, 18);
            } else if (post.font.color(this.i2).equals("White")) {
                this.ss.setSpan(new ForegroundColorSpan(-1), this.start, this.end, 18);
            }
            this.ss.setSpan(new RelativeSizeSpan((post.font.size(this.i2) > 3 ? (float) (((double) (post.font.size(this.i2) - 3)) / 10.0d) : (float) (((double) (-(post.font.size(this.i2) - 3))) / 10.0d)) + PhotoViewAttacher.DEFAULT_MIN_SCALE), this.start, this.end, 18);
            this.i2++;
        }
        this.i2 = 0;
        while (this.i2 < post.web.getSize()) {
            this.start = post.web.getStart(this.i2).intValue();
            this.end = post.web.getEnd(this.i2).intValue();
            this.ss.setSpan(new URLSpan(post.web.getStr(this.i2)), this.start, this.end, 18);
            this.i2++;
        }
        this.i2 = 0;
        while (this.i2 < post.quote.getSize()) {
            this.start = post.quote.getStart(this.i2).intValue();
            this.end = post.quote.getEnd(this.i2).intValue();
            this.ss.setSpan(new QuoteSpan(this.context.getResources().getColor(R.color.ics_blue_dark)), this.start, this.end, 18);
            this.ss.setSpan(new StyleSpan(2), this.start, this.end, 18);
            this.ss.setSpan(new ForegroundColorSpan(QuickAction.WOOD_TEXT_TITLE), this.start, this.end, 18);
            this.i2++;
        }
        this.i2 = 0;
        while (this.i2 < post.type.getSize()) {
            this.start = post.type.getStart(this.i2).intValue();
            this.end = post.type.getEnd(this.i2).intValue();
            this.ss.setSpan(new StyleSpan(post.type.type()), this.start, this.end, 18);
            this.i2++;
        }
        this.i2 = 0;
        while (this.i2 < post.typeU.getSize()) {
            this.start = post.typeU.getStart(this.i2).intValue();
            this.end = post.typeU.getEnd(this.i2).intValue();
            this.ss.setSpan(new UnderlineSpan(), this.start, this.end, 18);
            this.i2++;
        }
        this.i2 = 0;
        while (this.i2 < post.image.getSize()) {
            if (post.image.getStr(this.i2).contains("http://") || post.image.getStr(this.i2).contains("https://")) {
                try {
                    if (this.i2 < post.image.getSize() && post.image.getStart(this.i2).intValue() < this.ss.length() && post.image.getEnd(this.i2).intValue() < this.ss.length()) {
                        Standard standard = new Standard(Alignment.ALIGN_CENTER);
                        this.span = new ImageSpan(this.context, post.image.getBitmap(this.i2), 0);
                        this.start = post.image.getStart(this.i2).intValue();
                        this.end = post.image.getEnd(this.i2).intValue();
                        AnonymousClass1 anonymousClass1 = new AnonymousClass1(this.ss.subSequence(this.start, this.end).toString());
                        this.ss.setSpan(this.span, this.start, this.end, 18);
                        this.ss.setSpan(standard, this.start + -2 >= 0 ? this.start - 2 : 0, this.end, 18);
                        this.ss.setSpan(anonymousClass1, this.start, this.end, 33);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                this.d = new BitmapDrawable(this.context.getResources(), post.image.getBitmap(this.i2));
                if (post.image.getStr(this.i2).contains("attachmentid")) {
                    this.d.setBounds(0, 0, this.d.getIntrinsicWidth(), this.d.getIntrinsicHeight());
                } else {
                    this.d.setBounds(0, 0, (int) (((float) this.dp32) * this.mTextSize), (int) (((float) this.dp32) * this.mTextSize));
                }
                try {
                    this.span = new ImageSpan(this.d, 0);
                    this.start = post.image.getStart(this.i2).intValue();
                    this.end = post.image.getEnd(this.i2).intValue();
                    this.ss.setSpan(this.span, this.start, this.end, 18);
                } catch (IndexOutOfBoundsException e2) {
                    e2.printStackTrace();
                }
            }
            this.i2++;
        }
        try {
            textView.setText(this.ss);
            textView.setMovementMethod(LocalLinkMovementMethod.getInstance());
            textView.setClickable(false);
            textView.setFocusable(false);
            textView.setLongClickable(false);
        } catch (ArrayIndexOutOfBoundsException e3) {
            e3.printStackTrace();
        } catch (IndexOutOfBoundsException e22) {
            e22.printStackTrace();
        }
    }

    public void destroy() {
    }

    public int getCount() {
        return this.mcontains.size();
    }

    public Object getItem(int i) {
        return null;
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public Bitmap getResizedBitmap(Bitmap bitmap) {
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            if (((double) width) < (((double) Global.width) * 9.0d) / 10.0d) {
                return bitmap;
            }
            float f = ((float) ((((double) Global.width) * 9.0d) / 10.0d)) / ((float) width);
            Matrix matrix = new Matrix();
            matrix.postScale(f, f);
            return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        Post post = (Post) this.mcontains.get(i);
        if (view == null) {
            this.holder = new ViewHolder();
            view = this.inflater.inflate(R.layout.list_item3, null);
            this.holder.layout = (LinearLayout) view.findViewById(R.id.listLayout);
            this.holder.avatar = (ImageView) view.findViewById(R.id.list3_avatar);
            this.holder.user = (TextView) view.findViewById(R.id.list3_user);
            this.holder.time = (TextView) view.findViewById(R.id.list3_time);
            this.holder.index = (TextView) view.findViewById(R.id.list3_index);
            this.holder.contain = (JellyBeanSpanFixTextView) view.findViewById(R.id.list3_contain);
            this.holder.jd = (TextView) view.findViewById(R.id.list3_jd);
            this.holder.posts = (TextView) view.findViewById(R.id.list3_post);
            this.holder.userTitle = (TextView) view.findViewById(R.id.list3_usertitle);
            this.holder.txtViewDevider = (TextView) view.findViewById(R.id.textDevider);
            view.setTag(this.holder);
        } else {
            this.holder = (ViewHolder) view.getTag();
        }
        if (Global.bDevider) {
            Global.setBackgoundMain(this.holder.txtViewDevider);
            this.holder.txtViewDevider.setVisibility(0);
        } else {
            this.holder.txtViewDevider.setVisibility(8);
        }
        Global.setTextContain(this.holder.contain);
        Global.setTextColor1(this.holder.time);
        Global.setTextColor1(this.holder.index);
        Global.setTextColor1(this.holder.userTitle);
        Global.setTextColor1(this.holder.jd);
        Global.setTextColor1(this.holder.user);
        Global.setTextColor1(this.holder.posts);
        Global.setTextColor1(this.holder.userTitle);
        this.holder.user.setTextSize(this.mTextSize3);
        this.holder.time.setTextSize(this.mTextSize3);
        this.holder.index.setTextSize(this.mTextSize3);
        this.holder.jd.setTextSize(this.mTextSize4);
        this.holder.posts.setTextSize(this.mTextSize4);
        this.holder.userTitle.setTextSize(this.mTextSize4);
        this.holder.contain.setTextSize(this.mTextSize1);
        if (post.Avatar() != null) {
            this.holder.avatar.setImageBitmap(post.Avatar());
        } else {
            this.holder.avatar.setImageDrawable(this.drawableAvatar);
        }
        this.holder.user.setText(post.User());
        if (post.Time() != null && post.Time().split(" ").length > 3) {
            this.holder.time.setText(post.Time().split(" ")[2] + " " + post.Time().split(" ")[3]);
            this.holder.index.setText(post.Time().split(" ")[0]);
        }
        this.holder.userTitle.setText(post.UserTitle());
        this.holder.jd.setText(post.JD());
        this.holder.posts.setText(post.Posts());
        if (post.isOnline) {
            this.holder.user.setCompoundDrawables(this.drawableOnline, null, null, null);
        } else {
            this.holder.user.setCompoundDrawables(this.drawableOffline, null, null, null);
        }
        loadIamge(post, 0);
        addImage(this.holder.contain, post);
        if (post.isMultiQuote()) {
            Global.setBackgroundItemThreadMultiQuote(this.holder.layout);
        } else {
            Global.setBackgroundItemThread(this.holder.layout);
        }
        return view;
    }

    public void loadImage(int i, int i2) {
        while (i <= i2) {
            if (i >= 0 && i < this.mcontains.size()) {
                loadIamge((Post) this.mcontains.get(i), 0);
            }
            i++;
        }
    }

    public void setOnImageClickListener(OnImageClickListestener onImageClickListestener) {
        this.mImageClickListener = onImageClickListestener;
    }

    public void setSize(float f) {
        this.mTextSize = f;
        this.mTextSize1 = this.context.getResources().getDimension(R.dimen.textSize2) * f;
        this.mTextSize3 = this.context.getResources().getDimension(R.dimen.textSize3) * f;
        this.mTextSize4 = this.context.getResources().getDimension(R.dimen.textSize4) * f;
    }
}
