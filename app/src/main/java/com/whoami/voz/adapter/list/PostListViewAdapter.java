package com.whoami.voz.adapter.list;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.stfalcon.frescoimageviewer.ImageViewer;
import com.whoami.voz.R;
import com.whoami.voz.contain.VozPost;
import com.whoami.voz.contain.item.ContentItem;
import com.whoami.voz.main.Global;
import com.whoami.voz.main.MainApplication;
import com.whoami.voz.utils.Utils;
import com.whoami.voz.widget.CircleProgressBarDrawable;

import java.util.ArrayList;

public class PostListViewAdapter extends BaseAdapter {
    private static final int PADDING = Utils.convertDpToPx(MainApplication.getAppContext(), 15);
    private static final int MARGIN = Utils.convertDpToPx(MainApplication.getAppContext(), 8);
    private static final int CONTENT_PHOTO_DEFAULT_SIZE = Utils.convertDpToPx(MainApplication.getAppContext(), 100);
    private static final int IMAGE_WITDH = 500;
    LayoutInflater inflater;

    Context context;
    int dp32;
    Drawable drawableAvatar;
    Drawable drawableOffline;
    Drawable drawableOnline;
    ArrayList<VozPost> mPosts;
    boolean isScrolling = false;
    Drawable presetDrawable ;

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

    public PostListViewAdapter(Context context, ArrayList<VozPost> arrayList) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        drawableOnline = ContextCompat.getDrawable(context, R.drawable.user_online);
        drawableOffline = ContextCompat.getDrawable(context, R.drawable.user_offline);
        drawableAvatar = ContextCompat.getDrawable(context, R.drawable.menu_usercp2);
        dp32 = Utils.convertDpToPx(context, 8);
        drawableOnline.setBounds(0, 0, dp32, dp32);
        drawableOffline.setBounds(0, 0, dp32, dp32);
        mPosts = arrayList;
        presetDrawable = ContextCompat.getDrawable(context, R.drawable.image_for_empty_url);
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
        ViewHolder holder;
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

        holder.usernameTv.setText(post.getUserName());

        if (post.getTime() != null && post.getTime().split(" ").length > 3) {
            StringBuilder indexTimeStr = new StringBuilder();
            indexTimeStr.append(post.getTime().split(" ")[1] + "  ");
            indexTimeStr.append(post.getTime().split(" ")[3] + " " + post.getTime().split(" ")[4]);
            holder.indexTv.setText(indexTimeStr.toString());
        }
        initDynamicContent(holder, context, post);

        return view;
    }


    public void initDynamicContent(ViewHolder holder, final Context context, VozPost vozPost) {
        if (vozPost.mContentLayout == null) {
            vozPost.mContentLayout = new LinearLayout(context);
            vozPost.mContentLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            vozPost.mContentLayout.setOrientation(LinearLayout.VERTICAL);
            for (final ContentItem item : vozPost.mContentItemList) {
                switch (item.type) {
                    case ContentItem.TYPE_PLAIN_TEXT:{
                        TextView textView = new TextView(context);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        textView.setText(item.mContent);
                        textView.setTextColor(ContextCompat.getColor(context, R.color.text_black_3b));
                        textView.setTextSize(16);
                        textView.setPadding(PADDING, 0, PADDING, 0);
                        textView.setLayoutParams(layoutParams);
                        textView.setLinkTextColor(ContextCompat.getColor(context, R.color.link_blue));
                        textView.setMovementMethod(LinkMovementMethod.getInstance());
                        vozPost.mContentLayout.addView(textView);
                        break;
                    }
                    case ContentItem.TYPE_QUOTE: {
                        TextView textView = new TextView(context);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(MARGIN, 0, MARGIN, 0);
                        textView.setText(item.mContent);
                        textView.setTextColor(ContextCompat.getColor(context, R.color.text_black_3b));
                        textView.setTextSize(16);
                        textView.setPadding(PADDING, PADDING, PADDING, PADDING);
                        textView.setBackground(ContextCompat.getDrawable(context, R.drawable.quote_background));
                        textView.setLinkTextColor(ContextCompat.getColor(context, R.color.link_blue));
                        textView.setLayoutParams(layoutParams);
                        textView.setMovementMethod(LinkMovementMethod.getInstance());
                        vozPost.mContentLayout.addView(textView);
                        break;
                    }
                    case ContentItem.TYPE_PHOTO: {
                        final SimpleDraweeView simpleDraweeView = new SimpleDraweeView(context);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(CONTENT_PHOTO_DEFAULT_SIZE, CONTENT_PHOTO_DEFAULT_SIZE);
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
                                .setResizeOptions(new ResizeOptions(IMAGE_WITDH, IMAGE_WITDH))
                                .build();
                        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                .setOldController(simpleDraweeView.getController())
                                .setControllerListener(controllerListener)
                                .setAutoPlayAnimations(true)
                                .setImageRequest(request)
                                .build();

                        GenericDraweeHierarchy hierarchy = simpleDraweeView.getHierarchy();
                        hierarchy.setProgressBarImage(new CircleProgressBarDrawable());
//                        hierarchy.setPlaceholderImage(R.drawable.image_for_empty_url);
//                        hierarchy.setFadeDuration(300);

                        simpleDraweeView.setLayoutParams(layoutParams);
                        simpleDraweeView.setController(controller);
                        vozPost.mContentLayout.addView(simpleDraweeView);


                        simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ArrayList<String> urls = new ArrayList<>();
                                urls.add(item.mData);
                                new ImageViewer.Builder<>(context, urls)
                                        .setStartPosition(0)
                                        .show();
                            }
                        });
                        break;
                    }
                    default:
                }
            }
        }
        if (vozPost.mContentLayout != null &&  vozPost.mContentLayout.getParent() != null) {
            ((LinearLayout) vozPost.mContentLayout.getParent()).removeView(vozPost.mContentLayout);
        }
        if (holder.mContentLayout.getChildCount() > 0 && !holder.mContentLayout.equals(vozPost.mContentLayout)) {
            holder.mContentLayout.removeAllViews();
        }
        holder.mContentLayout.addView(vozPost.mContentLayout);
    }

}
