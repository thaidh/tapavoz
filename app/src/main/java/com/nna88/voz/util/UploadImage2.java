package com.nna88.voz.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import com.google.android.gms.plus.PlusShare;
import com.loopj.android.http.AsyncHttpClient;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AUTH;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class UploadImage2 extends AsyncTask {
    private Context mContext;
    private Uri mUrlImage;
    public String url;
    public String urlPost;

    public UploadImage2(Context context, Uri uri) {
        this.url = null;
        this.mContext = context;
        this.mUrlImage = uri;
    }

    private String uploadimg(Uri uri) {
        try {
            File resizedBitmap = getResizedBitmap(decodeSampledBitmapFromUri(getRealPathFromURI(uri), 640, 640));
            HttpClient defaultHttpClient = new DefaultHttpClient();
            HttpContext basicHttpContext = new BasicHttpContext();
            HttpUriRequest httpPost = new HttpPost("https://api.imgur.com/3/upload.json");
            httpPost.setHeader(AUTH.WWW_AUTH_RESP, "Client-ID 6fb648c1b59b327");
            try {
                HttpEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//                multipartEntity.addPart("image", new FileBody(resizedBitmap));
//                multipartEntity.addPart("key", new StringBody("6fb648c1b59b327"));
//                multipartEntity.addPart(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION, new StringBody("test upimgae"));
//                httpPost.setEntity(multipartEntity);
                String str = "http://i.imgur.com/" + new JSONObject(EntityUtils.toString(defaultHttpClient.execute(httpPost, basicHttpContext).getEntity())).getJSONObject("data").getString("id") + "l.png";
                resizedBitmap.deleteOnExit();
                return str;
            } catch (Exception e) {
                e.printStackTrace();
                resizedBitmap.deleteOnExit();
                return null;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public int calculateInSampleSize(Options options, int i, int i2) {
        int i3 = options.outHeight;
        int i4 = options.outWidth;
        return (i3 > i2 || i4 > i) ? i4 > i3 ? Math.round(((float) i3) / ((float) i2)) : Math.round(((float) i4) / ((float) i)) : 1;
    }

    public Bitmap decodeSampledBitmapFromUri(String str, int i, int i2) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        options.inSampleSize = calculateInSampleSize(options, i, i2);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(str, options);
    }

    protected Object doInBackground(Object... objArr) {
        this.url = uploadimg(this.mUrlImage);
        return null;
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor query = this.mContext.getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
        int columnIndexOrThrow = query.getColumnIndexOrThrow("_data");
        query.moveToFirst();
        return query.getString(columnIndexOrThrow);
    }

    public File getResizedBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        File file = new File("/mnt/sdcard/vozforums/tmp");
        file.mkdirs();
        File file2 = new File(file, "Image-" + new Random().nextInt(AsyncHttpClient.DEFAULT_SOCKET_TIMEOUT) + ".jpg");
        if (file2.exists()) {
            file2.delete();
        }
        if (width >= 640) {
            float f = (float) (640.0d / ((double) width));
            Matrix matrix = new Matrix();
            matrix.postScale(f, f);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        }
        try {
            OutputStream fileOutputStream = new FileOutputStream(file2);
            bitmap.compress(CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return file2;
        } catch (Exception e) {
            e.printStackTrace();
            return file2;
        }
    }

    protected void onPostExecute(Object obj) {
        super.onPostExecute(obj);
    }
}
