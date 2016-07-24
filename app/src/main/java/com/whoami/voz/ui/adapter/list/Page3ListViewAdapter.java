package com.whoami.voz.ui.adapter.list;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.internal.widget.ViewUtils;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.whoami.voz.R;
import com.whoami.voz.ui.contain.Post;
import com.whoami.voz.ui.contain.item.ContentItem;
import com.whoami.voz.ui.main.Global;
import com.whoami.voz.ui.utils.Util;

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
//    private float mTextSize;
//    private float mTextSize1;
//    private float mTextSize3;
//    private float mTextSize4;
    private ArrayList<Post> mPosts;
    private DisplayImageOptions options;
    private DisplayImageOptions optionsEmo;
    private ImageSpan span;
    private boolean isScrolling = false;

    public void setIsScrolling(boolean isScrolling) {
        this.isScrolling = isScrolling;
    }

    class CustomClickableSpan extends ClickableSpan {
        final  String url;

        CustomClickableSpan(String str) {
            url = str;
        }

        public void onClick(View view) {
            if (mImageClickListener == null) {
                return;
            }
            if (url.contains("attachmentid") || url.contains("http://") || url.contains("https://")) {
                mImageClickListener.onImageClick(0, url);
            }
        }
    }

    public interface OnImageClickListestener {
        void onImageClick(int i, String str);
    }

    static class ViewHolder {
        SimpleDraweeView avatar;
        TextView contain;
        TextView index;
        TextView jd;
        LinearLayout layout;
        TextView posts;
        TextView time;
        View txtViewDevider;
        TextView user;
        TextView userTitle;
        LinearLayout mGridImage;

        ViewHolder() {
        }
    }

    public Page3ListViewAdapter(Context context, ArrayList<Post> arrayList, ImageLoader imageLoader, Bitmap bitmap, float size) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        drawableOnline = ContextCompat.getDrawable(context, R.drawable.user_online);
        drawableOffline = ContextCompat.getDrawable(context, R.drawable.user_offline);
        drawableAvatar = ContextCompat.getDrawable(context, R.drawable.menu_usercp2);
        dp32 = Util.convertDpToPx(context, 8);
        drawableOnline.setBounds(0, 0, dp32, dp32);
        drawableOffline.setBounds(0, 0, dp32, dp32);
        mPosts = arrayList;
        lImage = new ArrayList();
        this.imageLoader = imageLoader;
        initUniversal();
        bmImageFailed = BitmapFactory.decodeResource(context.getResources(), R.drawable.image_for_empty_url);
        bmImageStart = bitmap;
    }


    private void initUniversal() {
        options = new Builder().cacheInMemory(true).imageScaleType(ImageScaleType.NONE).bitmapConfig(Config.RGB_565).build();
        optionsEmo = new Builder().cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Config.ARGB_8888).delayBeforeLoading(0).build();
        mImageSize = new ImageSize(Global.width - Util.convertDpToPx(context, 10), Global.width - Util.convertDpToPx(context, 10));
    }


    public int getCount() {
        return mPosts.size();
    }

    public Object getItem(int i) {
        return null;
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int position, View view, ViewGroup viewGroup) {
        Post post = mPosts.get(position);
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_item3, null);
            holder.layout = (LinearLayout) view.findViewById(R.id.listLayout);
            holder.avatar = (SimpleDraweeView) view.findViewById(R.id.list3_avatar);
            holder.user = (TextView) view.findViewById(R.id.list3_user);
            holder.time = (TextView) view.findViewById(R.id.list3_time);
            holder.index = (TextView) view.findViewById(R.id.list3_index);
//            holder.contain = (TextView) view.findViewById(R.id.list3_contain);
            holder.jd = (TextView) view.findViewById(R.id.list3_jd);
            holder.posts = (TextView) view.findViewById(R.id.list3_post);
            holder.userTitle = (TextView) view.findViewById(R.id.list3_usertitle);
            holder.txtViewDevider =  view.findViewById(R.id.textDevider);
            holder.mGridImage = (LinearLayout) view.findViewById(R.id.grid_image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.txtViewDevider.setVisibility(View.VISIBLE);
//        holder.contain.setMovementMethod(CustomLinkMovementMethod.getInstance());
//        holder.contain.setClickable(false);
        if (!TextUtils.isEmpty(post.getUrlAvatar()) && !isScrolling) {
//            imageLoader.displayImage(post.getUrlAvatar(), holder.avatar, optionsEmo);
            holder.avatar.setImageURI(Uri.parse(post.getUrlAvatar()));
        } else {
            holder.avatar.setImageDrawable(drawableAvatar);
        }

        holder.user.setText(post.User());

        if (post.Time() != null && post.Time().split(" ").length > 3) {
            holder.time.setText(post.Time().split(" ")[3] + " " + post.Time().split(" ")[4]);
            holder.index.setText(post.Time().split(" ")[1]);
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


//        holder.contain.setText(post.getContent());
        holder.mGridImage.removeAllViews();
        for (ContentItem item : post.mContentItemList) {
            addTextContent(holder.mGridImage, item);
        }
//        for (int i = 0; i < post.image.getSize(); i++) {
//            String imageUrl = post.image.getStr(i);
//            if (imageUrl.contains("http://") || imageUrl.contains("https://")) {
//                try {
//                    SimpleDraweeView simpleDraweeView = new SimpleDraweeView(context);
//                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mImageSize.getWidth(), mImageSize.getHeight());
//                    layoutParams.setMargins(0, 0, 0, 10);
//                    layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
//
//                    simpleDraweeView.setLayoutParams(layoutParams);
//                    simpleDraweeView.setImageURI(Uri.parse(imageUrl));
//                    simpleDraweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
//
//                    holder.mGridImage.addView(simpleDraweeView);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }


        return view;
    }

    public void setOnImageClickListener(OnImageClickListestener onImageClickListestener) {
        mImageClickListener = onImageClickListestener;
    }

    private void addTextContent(LinearLayout parent, ContentItem item) {
        switch (item.type) {
            case ContentItem.TYPE_PLAIN_TEXT:{
                TextView textView = new TextView(context);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textView.setText(item.mContent);
                textView.setTextColor(ContextCompat.getColor(context, R.color.black));
                textView.setTextSize(16);
                textView.setPadding(Util.convertDpToPx(context, 5), 0, 0, 0);
                textView.setLayoutParams(layoutParams);
                parent.addView(textView);
                break;
            }

            case ContentItem.TYPE_PHOTO: {
                SimpleDraweeView simpleDraweeView = new SimpleDraweeView(context);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mImageSize.getWidth(), mImageSize.getHeight());
                layoutParams.setMargins(0, 0, 0, 10);
                layoutParams.gravity = Gravity.CENTER_HORIZONTAL;

                simpleDraweeView.setLayoutParams(layoutParams);
                simpleDraweeView.setImageURI(Uri.parse(item.mData));
                simpleDraweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);

                parent.addView(simpleDraweeView);
                break;
            }
            default:
        }
    }

}
