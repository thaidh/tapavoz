package com.nna88.voz.listview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ViewCompat;
import android.support.v7.internal.widget.ViewUtils;
import android.text.Layout.Alignment;
import android.text.SpannableString;
import android.text.TextUtils;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
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

import java.net.URI;
import java.util.ArrayList;

public class Page3ListViewAdapter extends BaseAdapter {
    private Bitmap bmImageFailed;
    private Bitmap bmImageStart;
    private Context context;
    private Drawable d;
    private int dp32;
    private Drawable drawableAvatar;
    private Drawable drawableOffline;
    private Drawable drawableOnline;
    private ViewHolder holder;
    protected ImageLoader imageLoader;
    private LayoutInflater inflater;
    private ArrayList<String> lEmoImage;
    private ArrayList<String> lImage;
    private OnImageClickListestener mImageClickListener;
    private ImageSize mImageSize;
    private float mTextSize;
    private float mTextSize1;
    private float mTextSize3;
    private float mTextSize4;
    private ArrayList<Post> mcontains;
    private DisplayImageOptions options;
    private DisplayImageOptions optionsEmo;
    private ImageSpan span;
    private boolean isScrolling = false;

    public void setIsScrolling(boolean isScrolling) {
        this.isScrolling = isScrolling;
    }

    class CustomClickableSpan extends ClickableSpan {
        final  String val$url;

        CustomClickableSpan(String str) {
            val$url = str;
        }

        public void onClick(View view) {
            if (mImageClickListener == null) {
                return;
            }
            if (val$url.contains("attachmentid") || val$url.contains("http://") || val$url.contains("https://")) {
                mImageClickListener.onImageClick(0, val$url);
            }
        }
    }

    public interface OnImageClickListestener {
        void onImageClick(int i, String str);
    }

    static class ViewHolder {
        SimpleDraweeView avatar;
        JellyBeanSpanFixTextView contain;
        TextView index;
        TextView jd;
        LinearLayout layout;
        TextView posts;
        TextView time;
        TextView txtViewDevider;
        TextView user;
        TextView userTitle;
        LinearLayout mGridImage;

        ViewHolder() {
        }
    }

    public Page3ListViewAdapter(Context context, ArrayList<Post> arrayList, ImageLoader imageLoader, Bitmap bitmap, float f) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        drawableOnline = context.getResources().getDrawable(R.drawable.user_online);
        drawableOffline = context.getResources().getDrawable(R.drawable.user_offline);
        drawableAvatar = context.getResources().getDrawable(R.drawable.menu_usercp2);
        dp32 = Util.convertDpToPx(context, 32);
        int i = (int) ((((float) dp32) * f) / 4.0f);
        int i2 = (int) ((((float) dp32) * f) / 4.0f);
        drawableOnline.setBounds(0, 0, i, i2);
        drawableOffline.setBounds(0, 0, i, i2);
        mcontains = arrayList;
        lImage = new ArrayList();
        this.imageLoader = imageLoader;
        initUniversal();
        bmImageFailed = BitmapFactory.decodeResource(context.getResources(), R.drawable.image_for_empty_url);
        bmImageStart = bitmap;
        setSize(f);
    }

    private String getlinkBitmapAssert(String str) {
//        int i = 0;
        if (str.contains("smilies")) {
            String str2 = str.substring(0, str.length() - 3) + "gif";
            return "assets://" + str2;
//            try {
//                String[] list = context.getAssets().list("images/smilies/Off");
//                int length = list.length;
//                while (i < length) {
//                    if ((str.substring(0, str.length() - 3) + "png").contains(list[i])) {
//                        str = str.substring(0, str.length() - 3) + "png";
//                        break;
//                    }
//                    i++;
//                }
//                str = str2;
//            } catch (Exception e) {
//                e.printStackTrace();
//                str = str2;
//            }
        }
        return "assets://" + str;
    }

    private void initUniversal() {
        options = new Builder().cacheInMemory(true).imageScaleType(ImageScaleType.NONE).bitmapConfig(Config.RGB_565).build();
        optionsEmo = new Builder().cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Config.ARGB_8888).delayBeforeLoading(0).build();
        mImageSize = new ImageSize(Global.width - Util.convertDpToPx(context, 10), Global.width - Util.convertDpToPx(context, 10));
    }

    private void log(String str) {
        Log.d("nna", str);
    }


    public void addImage(TextView textView, Post post) {
        try {
            SpannableString content = new SpannableString(post.getText());
            int curPos = 0;
            int start;
            int end;
            while (curPos < post.font.getSize()) {
                start = post.font.getStart(curPos).intValue();
                end = post.font.getEnd(curPos).intValue();
                if (post.font.color(curPos).equals("Purple")) {
                    content.setSpan(new ForegroundColorSpan(-8388480), start, end, 18);
                } else if (post.font.color(curPos).equals("Indigo")) {
                    content.setSpan(new ForegroundColorSpan(-11861886), start, end, 18);
                } else if (post.font.color(curPos).equals("SlateGray")) {
                    content.setSpan(new ForegroundColorSpan(-9404272), start, end, 18);
                } else if (post.font.color(curPos).equals("DarkOrchid")) {
                    content.setSpan(new ForegroundColorSpan(-6737204), start, end, 18);
                } else if (post.font.color(curPos).equals("Plum")) {
                    content.setSpan(new ForegroundColorSpan(-2252579), start, end, 18);
                } else if (post.font.color(curPos).equals("Black")) {
                    content.setSpan(new ForegroundColorSpan(ViewCompat.MEASURED_STATE_MASK), start, end, 18);
                } else if (post.font.color(curPos).equals("DarkRed")) {
                    content.setSpan(new ForegroundColorSpan(-7667712), start, end, 18);
                } else if (post.font.color(curPos).equals("Red")) {
                    content.setSpan(new ForegroundColorSpan(SupportMenu.CATEGORY_MASK), start, end, 18);
                } else if (post.font.color(curPos).equals("Magenta")) {
                    content.setSpan(new ForegroundColorSpan(-65281), start, end, 18);
                } else if (post.font.color(curPos).equals("Pink")) {
                    content.setSpan(new ForegroundColorSpan(-16181), start, end, 18);
                } else if (post.font.color(curPos).equals("Sienna")) {
                    content.setSpan(new ForegroundColorSpan(-6270419), start, end, 18);
                } else if (post.font.color(curPos).equals("DarkOrange")) {
                    content.setSpan(new ForegroundColorSpan(-29696), start, end, 18);
                } else if (post.font.color(curPos).equals("SandyBrown")) {
                    content.setSpan(new ForegroundColorSpan(-744352), start, end, 18);
                } else if (post.font.color(curPos).equals("Orange")) {
                    content.setSpan(new ForegroundColorSpan(-23296), start, end, 18);
                } else if (post.font.color(curPos).equals("Wheat")) {
                    content.setSpan(new ForegroundColorSpan(-663885), start, end, 18);
                } else if (post.font.color(curPos).equals("DarkOliveGreen")) {
                    content.setSpan(new ForegroundColorSpan(-11179217), start, end, 18);
                } else if (post.font.color(curPos).equals("Olive")) {
                    content.setSpan(new ForegroundColorSpan(-8355840), start, end, 18);
                } else if (post.font.color(curPos).equals("YellowGreen")) {
                    content.setSpan(new ForegroundColorSpan(-6632142), start, end, 18);
                } else if (post.font.color(curPos).equals("Yellow")) {
                    content.setSpan(new ForegroundColorSpan(-256), start, end, 18);
                } else if (post.font.color(curPos).equals("LemonChiffon")) {
                    content.setSpan(new ForegroundColorSpan(-1331), start, end, 18);
                } else if (post.font.color(curPos).equals("DarkGreen")) {
                    content.setSpan(new ForegroundColorSpan(-16751616), start, end, 18);
                } else if (post.font.color(curPos).equals("Green")) {
                    content.setSpan(new ForegroundColorSpan(-16744448), start, end, 18);
                } else if (post.font.color(curPos).equals("SeaGreen")) {
                    content.setSpan(new ForegroundColorSpan(-13726889), start, end, 18);
                } else if (post.font.color(curPos).equals("Lime")) {
                    content.setSpan(new ForegroundColorSpan(-16711936), start, end, 18);
                } else if (post.font.color(curPos).equals("PaleGreen")) {
                    content.setSpan(new ForegroundColorSpan(-6751336), start, end, 18);
                } else if (post.font.color(curPos).equals("DarkSlateBlue")) {
                    content.setSpan(new ForegroundColorSpan(-12042869), start, end, 18);
                } else if (post.font.color(curPos).equals("Teal")) {
                    content.setSpan(new ForegroundColorSpan(-16744320), start, end, 18);
                } else if (post.font.color(curPos).equals("MediumTurquoise")) {
                    content.setSpan(new ForegroundColorSpan(-12004916), start, end, 18);
                } else if (post.font.color(curPos).equals("Cyan")) {
                    content.setSpan(new ForegroundColorSpan(-16711681), start, end, 18);
                } else if (post.font.color(curPos).equals("PaleTurquoise")) {
                    content.setSpan(new ForegroundColorSpan(-5247250), start, end, 18);
                } else if (post.font.color(curPos).equals("Navy")) {
                    content.setSpan(new ForegroundColorSpan(-16777088), start, end, 18);
                } else if (post.font.color(curPos).equals("Blue")) {
                    content.setSpan(new ForegroundColorSpan(-16776961), start, end, 18);
                } else if (post.font.color(curPos).equals("RoyalBlue")) {
                    content.setSpan(new ForegroundColorSpan(-12490271), start, end, 18);
                } else if (post.font.color(curPos).equals("DeepSkyBlue")) {
                    content.setSpan(new ForegroundColorSpan(-16728065), start, end, 18);
                } else if (post.font.color(curPos).equals("LightBlue")) {
                    content.setSpan(new ForegroundColorSpan(-5383962), start, end, 18);
                } else if (post.font.color(curPos).equals("DarkSlateGray")) {
                    content.setSpan(new ForegroundColorSpan(-13676721), start, end, 18);
                } else if (post.font.color(curPos).equals("DimGray")) {
                    content.setSpan(new ForegroundColorSpan(-9868951), start, end, 18);
                } else if (post.font.color(curPos).equals("Gray")) {
                    content.setSpan(new ForegroundColorSpan(-8355712), start, end, 18);
                } else if (post.font.color(curPos).equals("Silver")) {
                    content.setSpan(new ForegroundColorSpan(-4144960), start, end, 18);
                } else if (post.font.color(curPos).equals("White")) {
                    content.setSpan(new ForegroundColorSpan(-1), start, end, 18);
                }
                content.setSpan(new RelativeSizeSpan((post.font.size(curPos) > 3 ? (float) (((double) (post.font.size(curPos) - 3)) / 10.0d)
                        : (float) (((double) (-(post.font.size(curPos) - 3))) / 10.0d)) + PhotoViewAttacher.DEFAULT_MIN_SCALE), start, end, 18);
                curPos++;
            }
            curPos = 0;
            while (curPos < post.web.getSize()) {
                start = post.web.getStart(curPos).intValue();
                end = post.web.getEnd(curPos).intValue();
                content.setSpan(new URLSpan(post.web.getStr(curPos)), start, end, 18);
                curPos++;
            }
            curPos = 0;
            while (curPos < post.quote.getSize()) {
                start = post.quote.getStart(curPos).intValue();
                end = post.quote.getEnd(curPos).intValue();
                content.setSpan(new QuoteSpan(context.getResources().getColor(R.color.ics_blue_dark)), start, end, 18);
                content.setSpan(new StyleSpan(2), start, end, 18);
                content.setSpan(new ForegroundColorSpan(QuickAction.WOOD_TEXT_TITLE), start, end, 18);
                curPos++;
            }
            curPos = 0;
            while (curPos < post.type.getSize()) {
                start = post.type.getStart(curPos).intValue();
                end = post.type.getEnd(curPos).intValue();
                content.setSpan(new StyleSpan(post.type.type()), start, end, 18);
                curPos++;
            }
            curPos = 0;
            while (curPos < post.typeU.getSize()) {
                start = post.typeU.getStart(curPos).intValue();
                end = post.typeU.getEnd(curPos).intValue();
                content.setSpan(new UnderlineSpan(), start, end, 18);
                curPos++;
            }
            if (!isScrolling) {
                curPos = 0;
                while (curPos < post.image.getSize()) {
                    if (post.image.getStr(curPos).contains("http://") || post.image.getStr(curPos).contains("https://")) {
                        if (curPos < post.image.getSize()
                                && post.image.getStart(curPos).intValue() < content.length()
                                && post.image.getEnd(curPos).intValue() < content.length()) {
                            Standard standard = new Standard(Alignment.ALIGN_CENTER);
                            span = new ImageSpan(context, post.image.getBitmap(curPos), 0);
                            start = post.image.getStart(curPos).intValue();
                            end = post.image.getEnd(curPos).intValue();
                            CustomClickableSpan customClickableSpan = new CustomClickableSpan(content.subSequence(start, end).toString());
                            content.setSpan(span, start, end, 18);
                            content.setSpan(standard, start + -2 >= 0 ? start - 2 : 0, end, 18);
                            content.setSpan(customClickableSpan, start, end, 33);
                        }
                    } else {
                        d = new BitmapDrawable(context.getResources(), post.image.getBitmap(curPos));
                        if (post.image.getStr(curPos).contains("attachmentid")) {
                            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                        } else {
                            d.setBounds(0, 0, (int) (((float) dp32) * mTextSize), (int) (((float) dp32) * mTextSize));
                        }
                        span = new ImageSpan(d, 0);
                        start = post.image.getStart(curPos).intValue();
                        end = post.image.getEnd(curPos).intValue();
                        content.setSpan(span, start, end, 18);
                    }
                    curPos++;
                }

            }

            textView.setText(content);
            textView.setMovementMethod(LocalLinkMovementMethod.getInstance());
            textView.setClickable(false);
            textView.setFocusable(false);
            textView.setLongClickable(false);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
    }

    public int getCount() {
        return mcontains.size();
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

    public View getView(int position, View view, ViewGroup viewGroup) {
        Post post = mcontains.get(position);
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_item3, null);
            holder.layout = (LinearLayout) view.findViewById(R.id.listLayout);
            holder.avatar = (SimpleDraweeView) view.findViewById(R.id.list3_avatar);
            holder.user = (TextView) view.findViewById(R.id.list3_user);
            holder.time = (TextView) view.findViewById(R.id.list3_time);
            holder.index = (TextView) view.findViewById(R.id.list3_index);
            holder.contain = (JellyBeanSpanFixTextView) view.findViewById(R.id.list3_contain);
            holder.jd = (TextView) view.findViewById(R.id.list3_jd);
            holder.posts = (TextView) view.findViewById(R.id.list3_post);
            holder.userTitle = (TextView) view.findViewById(R.id.list3_usertitle);
            holder.txtViewDevider = (TextView) view.findViewById(R.id.textDevider);
            holder.mGridImage = (LinearLayout) view.findViewById(R.id.grid_image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (Global.bDevider) {
            Global.setBackgoundMain(holder.txtViewDevider);
            holder.txtViewDevider.setVisibility(View.VISIBLE);
        } else {
            holder.txtViewDevider.setVisibility(View.GONE);
        }
        Global.setTextContain(holder.contain);
        Global.setTextColor1(holder.time);
        Global.setTextColor1(holder.index);
        Global.setTextColor1(holder.userTitle);
        Global.setTextColor1(holder.jd);
        Global.setTextColor1(holder.user);
        Global.setTextColor1(holder.posts);
        Global.setTextColor1(holder.userTitle);
        holder.user.setTextSize(mTextSize3);
        holder.time.setTextSize(mTextSize3);
        holder.index.setTextSize(mTextSize3);
        holder.jd.setTextSize(mTextSize4);
        holder.posts.setTextSize(mTextSize4);
        holder.userTitle.setTextSize(mTextSize4);
        holder.contain.setTextSize(mTextSize1);
        if (!TextUtils.isEmpty(post.getUrlAvatar()) && !isScrolling) {
//            imageLoader.displayImage(post.getUrlAvatar(), holder.avatar, optionsEmo);
            holder.avatar.setImageURI(Uri.parse(post.getUrlAvatar()));
        } else {
            holder.avatar.setImageDrawable(drawableAvatar);
        }

        holder.user.setText(post.User());
        if (post.Time() != null && post.Time().split(" ").length > 3) {
            holder.time.setText(post.Time().split(" ")[2] + " " + post.Time().split(" ")[3]);
            holder.index.setText(post.Time().split(" ")[0]);
        }
        holder.userTitle.setText(post.UserTitle());
        holder.jd.setText(post.JD());
        holder.posts.setText(post.Posts());
        if (post.isOnline) {
            holder.user.setCompoundDrawables(drawableOnline, null, null, null);
        } else {
            holder.user.setCompoundDrawables(drawableOffline, null, null, null);
        }

//        addImage(holder.contain, post);
        if (post.isMultiQuote()) {
            Global.setBackgroundItemThreadMultiQuote(holder.layout);
        } else {
            Global.setBackgroundItemThread(holder.layout);
        }
        holder.contain.setText(post.getContent());
        holder.mGridImage.removeAllViews();
        for (int i = 0; i < post.image.getSize(); i++) {
            String imageUrl = post.image.getStr(i);
            if (imageUrl.contains("http://") || imageUrl.contains("https://")) {
                try {
                    SimpleDraweeView simpleDraweeView = new SimpleDraweeView(context);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mImageSize.getWidth(), mImageSize.getHeight());
                    layoutParams.setMargins(0, 0, 0, 10);
                    layoutParams.gravity = Gravity.CENTER_HORIZONTAL;

                    simpleDraweeView.setLayoutParams(layoutParams);
                    simpleDraweeView.setImageURI(Uri.parse(imageUrl));
                    simpleDraweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
//                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imageUrl))
//                            .setResizeOptions(new ResizeOptions(mImageSize.getWidth(), mImageSize.getHeight()))
//                            .build();
//                    DraweeController controller = Fresco.newDraweeControllerBuilder()
//                            .setImageRequest(request)
//                            .build();
//                    simpleDraweeView.setController(controller);

                    holder.mGridImage.addView(simpleDraweeView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        return view;
    }

    public void setOnImageClickListener(OnImageClickListestener onImageClickListestener) {
        mImageClickListener = onImageClickListestener;
    }

    public void setSize(float f) {
        mTextSize = f;
        mTextSize1 = context.getResources().getDimension(R.dimen.textSize2) * f;
        mTextSize3 = context.getResources().getDimension(R.dimen.textSize3) * f;
        mTextSize4 = context.getResources().getDimension(R.dimen.textSize4) * f;
    }
}
