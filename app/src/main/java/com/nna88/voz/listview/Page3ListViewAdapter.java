package com.nna88.voz.listview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nna88.voz.contain.Post;
import com.nna88.voz.listview.TextViewFixTouchConsume.LocalLinkMovementMethod;
import com.nna88.voz.main.Global;
import com.nna88.voz.main.R;
import com.nna88.voz.ui.JellyBeanSpanFixTextView;
import com.nna88.voz.util.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;

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
//        Global.setBackgoundMain(holder.txtViewDevider);
//        holder.txtViewDevider.setVisibility(View.VISIBLE);
        if (Global.bDevider) {
            Global.setBackgoundMain(holder.txtViewDevider);
            holder.txtViewDevider.setVisibility(View.VISIBLE);
        } else {
            holder.txtViewDevider.setVisibility(View.GONE);
        }
//        Global.setTextContain(holder.contain);
//        Global.setTextColor1(holder.time);
//        Global.setTextColor1(holder.index);
//        Global.setTextColor1(holder.userTitle);
//        Global.setTextColor1(holder.jd);
//        Global.setTextColor1(holder.user);
//        Global.setTextColor1(holder.posts);
//        Global.setTextColor1(holder.userTitle);
        holder.user.setTextSize(mTextSize3);
        holder.time.setTextSize(mTextSize3);
        holder.index.setTextSize(mTextSize3);
        holder.jd.setTextSize(mTextSize4);
        holder.posts.setTextSize(mTextSize4);
        holder.userTitle.setTextSize(mTextSize4);
        holder.contain.setTextSize(mTextSize1);
        holder.contain.setMovementMethod(LocalLinkMovementMethod.getInstance());
        holder.contain.setClickable(false);
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
//        if (post.isMultiQuote()) {
//            Global.setBackgroundItemThreadMultiQuote(holder.layout);
//        } else {
//            Global.setBackgroundItemThread(holder.layout);
//        }
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
                    simpleDraweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
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
