package com.nna88.voz.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;
import com.loopj.android.http.AsyncHttpClient;
import com.nna88.voz.PhotoView.PhotoViewAttacher;
import com.nna88.voz.util.UserInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Random;
import org.jsoup.Jsoup;

public class FullScreenImage implements ViewFactory, OnDismissListener {
    private static Bitmap bitmap;
    private static ImageLoader imageLoader;
    public static boolean isInitShow;
    static boolean isLandscape;
    static ImageView iv;
    private static ImageButton mCancelButton;
    private static Context mContext;
    private static boolean mIsShowing;
    private static String mLocalUri;
    private static PopupWindow mPopupWindow;
    private static ImageButton mSaveButton;
    private static ImageButton mShareButton;
    static int mode;
    private static DisplayImageOptions options;
    private static ProgressBar progressBar;
    private PhotoViewAttacher mAttacher;

    /* renamed from: com.nna88.voz.main.FullScreenImage.6 */
    class AnonymousClass6 implements OnClickListener {
        final /* synthetic */ String val$url;

        AnonymousClass6(String str) {
            this.val$url = str;
        }

        public void onClick(View view) {
            FullScreenImage.mLocalUri = this.val$url;
            FullScreenImage.this.setImage();
        }
    }

    private class DownImageAttach extends AsyncTask<String, Void, String> {
        Bitmap bitmap;

        private DownImageAttach() {
            this.bitmap = null;
        }

        protected String doInBackground(String... strArr) {
            Map cookies = new UserInfo().cookies();
            if (cookies == null) {
                return null;
            }
            String str = strArr[0];
            try {
                if (!str.contains(Global.URL)) {
                    str = Global.URL + str;
                }
                str = str.replace("thumb=1", "thumb=0");
                FullScreenImage.this.log(str);
                byte[] bodyAsBytes = Jsoup.connect(str).cookies(cookies).ignoreContentType(true).execute().bodyAsBytes();
                this.bitmap = BitmapFactory.decodeByteArray(bodyAsBytes, 0, bodyAsBytes.length);
                return "OK";
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            try {
                FullScreenImage.iv.setImageBitmap(this.bitmap);
                FullScreenImage.this.initialView(FullScreenImage.iv);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static {
        imageLoader = ImageLoader.getInstance();
    }

    public FullScreenImage(Context context, View view) {
        options = new Builder().cacheInMemory(true).cacheOnDisc(true).imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).bitmapConfig(Config.ARGB_8888).delayBeforeLoading(0).build();
        mIsShowing = false;
        mode = 0;
        mContext = context;
        isInitShow = false;
        initShow(view);
    }

    private void alertSaveImg() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Global.TAG);
        file.mkdirs();
        File file2 = new File(file, "Image-" + new Random().nextInt(AsyncHttpClient.DEFAULT_SOCKET_TIMEOUT) + ".jpg");
        try {
            OutputStream fileOutputStream = new FileOutputStream(file2);
            ((BitmapDrawable) iv.getDrawable()).getBitmap().compress(CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            mContext.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file2)));
            toast("Save image in " + file.getPath());
        } catch (Exception e) {
            e.printStackTrace();
            toast("Error when saving the image");
        }
    }

    private View insertPhoto(Bitmap bitmap, String str) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setLayoutParams(new LayoutParams(-2, -2));
        linearLayout.setGravity(17);
        ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        imageView.setScaleType(ScaleType.FIT_XY);
        imageView.setImageBitmap(bitmap);
        imageView.setOnClickListener(new AnonymousClass6(str));
        linearLayout.addView(imageView);
        return linearLayout;
    }

    private void log(String str) {
        Log.d("nna", str);
    }

    private void toast(String str) {
        Toast.makeText(mContext, str, 1).show();
    }

    public void hide() {
        try {
            mIsShowing = false;
            if (mPopupWindow != null) {
                mPopupWindow.dismiss();
                mPopupWindow = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initShow(View view) {
        View inflate = ((LayoutInflater) mContext.getSystemService("layout_inflater")).inflate(R.layout.fullscreen, null);
        mPopupWindow = new PopupWindow(inflate, -1, -1, true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.showAtLocation(view, 17, 0, 0);
        iv = (ImageView) inflate.findViewById(R.id.fullscreenimage);
        progressBar = (ProgressBar) inflate.findViewById(R.id.progressbar);
        mSaveButton = (ImageButton) inflate.findViewById(R.id.save);
        mCancelButton = (ImageButton) inflate.findViewById(R.id.cancel);
        mShareButton = (ImageButton) inflate.findViewById(R.id.share);
        mCancelButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FullScreenImage.this.hide();
            }
        });
        mShareButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.putExtra("android.intent.extra.TEXT", FullScreenImage.mLocalUri);
                intent.setType("text/plain");
                FullScreenImage.mContext.startActivity(intent);
            }
        });
        mSaveButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FullScreenImage.this.alertSaveImg();
            }
        });
    }

    public void initialView(ImageView imageView) {
        this.mAttacher = new PhotoViewAttacher(imageView);
        this.mAttacher.canZoom();
    }

    public boolean isShowing() {
        return mIsShowing;
    }

    public View makeView() {
        return null;
    }

    public void onDismiss() {
        mPopupWindow.dismiss();
    }

    public void setImage() {
        if (mLocalUri.contains("attachmentid")) {
            new DownImageAttach().execute(new String[]{mLocalUri});
            return;
        }
        imageLoader.displayImage(mLocalUri, iv, options, new ImageLoadingListener() {
            public void onLoadingCancelled(String str, View view) {
                FullScreenImage.progressBar.setVisibility(8);
            }

            public void onLoadingComplete(String str, View view, Bitmap bitmap) {
                FullScreenImage.this.initialView(FullScreenImage.iv);
                FullScreenImage.progressBar.setVisibility(8);
            }

            public void onLoadingFailed(String str, View view, FailReason failReason) {
                FullScreenImage.progressBar.setVisibility(8);
            }

            public void onLoadingStarted(String str, View view) {
                FullScreenImage.progressBar.setVisibility(0);
            }
        }, new ImageLoadingProgressListener() {
            public void onProgressUpdate(String str, View view, int i, int i2) {
                FullScreenImage.progressBar.setProgress((i * 100) / i2);
            }
        });
    }

    public void show(String str) {
        mLocalUri = str;
        mIsShowing = true;
        setImage();
    }
}
