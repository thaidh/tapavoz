package com.whoami.voz.ui.adapter.list;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.support.v7.widget.ViewUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.whoami.voz.R;
import com.whoami.voz.ui.contain.VozPost;
import com.whoami.voz.ui.contain.item.ContentItem;
import com.whoami.voz.ui.main.Global;
import com.whoami.voz.ui.main.MainApplication;
import com.whoami.voz.ui.utils.Util;

import java.util.ArrayList;

public class PostListViewAdapter extends BaseAdapter {
    private static final int PADDING = Util.convertDpToPx(MainApplication.getAppContext(), 15);

    private Context context;
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
    private ArrayList<VozPost> mPosts;
    private DisplayImageOptions options;
    private DisplayImageOptions optionsEmo;
    private boolean isScrolling = false;

    public interface OnImageClickListestener {
        void onImageClick(int i, String str);
    }

    static class ViewHolder {
        SimpleDraweeView avatarView;
        TextView indexTv;
        LinearLayout mainLayout;
        View dividerView;
        TextView usernameTv;
        LinearLayout mContentLayout;

        ViewHolder() {
        }
    }

    public PostListViewAdapter(Context context, ArrayList<VozPost> arrayList, ImageLoader imageLoader, Bitmap bitmap, float size) {
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
        VozPost post = mPosts.get(position);
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.post_item_layout, null);
            holder.mainLayout = (LinearLayout) view.findViewById(R.id.main_layout);
            holder.avatarView = (SimpleDraweeView) view.findViewById(R.id.post_avatar_imv);
            holder.usernameTv = (TextView) view.findViewById(R.id.post_username_tv);
            holder.indexTv = (TextView) view.findViewById(R.id.post_index_tv);
            holder.dividerView =  view.findViewById(R.id.post_divider);
            holder.mContentLayout = (LinearLayout) view.findViewById(R.id.content_layout);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (!TextUtils.isEmpty(post.getUrlAvatar()) && !isScrolling) {
            holder.avatarView.setImageURI(Uri.parse(post.getUrlAvatar()));
        } else {
            holder.avatarView.setImageDrawable(drawableAvatar);
        }

        holder.usernameTv.setText(post.User());

        if (post.Time() != null && post.Time().split(" ").length > 3) {
            StringBuilder indexTimeStr = new StringBuilder();
            indexTimeStr.append(post.Time().split(" ")[1] + "  ");
            indexTimeStr.append(post.Time().split(" ")[3] + " " + post.Time().split(" ")[4]);
            holder.indexTv.setText(indexTimeStr.toString());
        }
        holder.mContentLayout.removeAllViews();
        for (ContentItem item : post.mContentItemList) {
            addDynamicContent(holder.mContentLayout, item);
        }
        return view;
    }

    public void setOnImageClickListener(OnImageClickListestener onImageClickListestener) {
        mImageClickListener = onImageClickListestener;
    }

    private void addDynamicContent(LinearLayout parent, ContentItem item) {
        switch (item.type) {
            case ContentItem.TYPE_PLAIN_TEXT:{
                TextView textView = new TextView(context);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textView.setText(item.mContent);
                textView.setTextColor(ContextCompat.getColor(context, R.color.black));
                textView.setTextSize(16);
                textView.setPadding(PADDING, 0, PADDING, 0);
                textView.setLayoutParams(layoutParams);
                parent.addView(textView);
                break;
            }
            case ContentItem.TYPE_QUOTE: {
                TextView textView = new TextView(context);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textView.setText(item.mContent);
                textView.setTextColor(ContextCompat.getColor(context, R.color.grey));
                textView.setTextSize(16);
                textView.setPadding(PADDING, 0, PADDING, 0);
                textView.setLayoutParams(layoutParams);
                parent.addView(textView);
                break;
            }
            case ContentItem.TYPE_PHOTO: {
                final SimpleDraweeView simpleDraweeView = new SimpleDraweeView(context);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Util.convertDpToPx(context, 100), Util.convertDpToPx(context, 100));
                layoutParams.setMargins(0, 0, 0, 0);
                ControllerListener controllerListener = new BaseControllerListener() {
                    @Override
                    public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {
                        if (imageInfo != null && imageInfo instanceof ImageInfo) {
                            float ratio = ((ImageInfo) imageInfo).getWidth() * 1.0f / ((ImageInfo) imageInfo).getHeight();
                            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) simpleDraweeView.getLayoutParams();
                            layoutParams.width = Global.width;
                            layoutParams.height = (int) (Global.width / ratio);
                            simpleDraweeView.setLayoutParams(layoutParams);
//                            simpleDraweeView.requestLayout();
                            simpleDraweeView.setAspectRatio(ratio);
                        }
                        super.onFinalImageSet(id, imageInfo, animatable);

                    }
                };
                ImageRequest request = ImageRequestBuilder
                        .newBuilderWithSource((Uri.parse(item.mData)))
                        .setResizeOptions(new ResizeOptions(mImageSize.getWidth(), mImageSize.getHeight()))
                        .build();
                PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setOldController(simpleDraweeView.getController())
                        .setControllerListener(controllerListener)
                        .setImageRequest(request)
                        .build();
//
//                GenericDraweeHierarchyBuilder genericDraweeHierarchyBuilder = new GenericDraweeHierarchyBuilder(context.getResources());
                GenericDraweeHierarchy hierarchy = simpleDraweeView.getHierarchy();
                hierarchy.setPlaceholderImage(R.drawable.image_for_empty_url);
                hierarchy.setFadeDuration(300);
                simpleDraweeView.setLayoutParams(layoutParams);
                simpleDraweeView.setController(controller);
                parent.addView(simpleDraweeView);
                break;
            }
            default:
        }
    }

}
