package com.nna88.voz.main;

import android.os.AsyncTask;
import android.util.Log;
import com.nna88.voz.util.UserInfo;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class MyNetwork {
    public static final int TIMEOUT = 5000;
    static UserInfo mUser;
    private Document doc;
    private OnResult mResultListener;
    private Task mTask;
    public String url;

    /* renamed from: com.nna88.voz.main.MyNetwork.1 */
    class AnonymousClass1 extends AsyncTask<Void, Void, Void> {
        final /* synthetic */ String val$humanverify;
        final /* synthetic */ String val$message;
        final /* synthetic */ String val$recaptcha_challenge_field;
        final /* synthetic */ String val$recaptcha_response_field;

        AnonymousClass1(String str, String str2, String str3, String str4) {
            this.val$message = str;
            this.val$humanverify = str2;
            this.val$recaptcha_challenge_field = str3;
            this.val$recaptcha_response_field = str4;
        }

        protected Void doInBackground(Void... voidArr) {
            Element first = MyNetwork.this.doc.select("form[action*=newreply.php?do=postreply]").first();
            Element first2 = MyNetwork.this.doc.select("input[name*=securitytoken]").first();
            Element first3 = MyNetwork.this.doc.select("input[name*=posthash]").first();
            Element first4 = MyNetwork.this.doc.select("input[name*=poststarttime]").first();
            Element first5 = MyNetwork.this.doc.select("input[name=title]").first();
            Element first6 = MyNetwork.this.doc.select("input[name=p]").first();
            MyNetwork.mUser.setToken(first2.attr("value"));
            MyNetwork.this.url = Global.URL + first.attr("action").replace("&amp;", "&");
            String attr = first3.attr("value");
            try {
                Response execute = Jsoup.connect(MyNetwork.this.url).timeout(MyNetwork.TIMEOUT).cookies(MyNetwork.mUser.cookies()).data("title:Re: ", first5.attr("value")).data("message", this.val$message).data("wysiwyg", "0").data("humanverify[hash]", this.val$humanverify).data("recaptcha_challenge_field", this.val$recaptcha_challenge_field).data("recaptcha_response_field", this.val$recaptcha_response_field).data("s", " ").data("securitytoken", MyNetwork.mUser.Token()).data("do", "postreply").data("t", MyNetwork.this.url.split("=")[2]).data("p", first6.attr("value")).data("specifiedpost", "1").data("posthash", attr).data("poststarttime", first4.attr("value")).data("loggedinuser", MyNetwork.mUser.UserId()).data("multiquoteempty:", " ").data("sbutton", "Submit Reply").data("parseurl", "1").data("rating", "0").method(Method.POST).execute();
                MyNetwork.this.url = execute.url().toString();
                MyNetwork.this.doc = execute.parse();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            if (MyNetwork.this.mResultListener != null) {
                MyNetwork.this.mResultListener.onResultListenter(MyNetwork.this.doc, 0);
            }
        }
    }

    public interface OnResult {
        void onResultListenter(Document document, int i);
    }

    private class Task extends AsyncTask<Integer, Void, Void> {
        private Task() {
        }

        protected Void doInBackground(Integer... numArr) {
            if (numArr[0].intValue() == 0) {
                try {
                    Response execute;
                    if (MyNetwork.mUser.cookies() == null) {
                        Log.d("nna", "cookies = null");
                        execute = Jsoup.connect(MyNetwork.this.url).timeout(MyNetwork.TIMEOUT).execute();
                    } else {
                        Log.d("nna", "cookies != null" + MyNetwork.mUser.cookies().containsKey("vbulletin_multiquote") + MyNetwork.mUser.getMultiquoteId());
                        execute = Jsoup.connect(MyNetwork.this.url).timeout(MyNetwork.TIMEOUT).cookies(MyNetwork.mUser.cookies()).execute();
                    }
                    if (execute != null) {
                        MyNetwork.this.doc = execute.parse();
                    }
                    if (MyNetwork.this.doc != null) {
                        MyNetwork.this.url = MyNetwork.this.doc.baseUri();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            if (MyNetwork.this.mResultListener != null) {
                MyNetwork.this.mResultListener.onResultListenter(MyNetwork.this.doc, 0);
            }
        }
    }

    static {
        mUser = new UserInfo();
    }

    public MyNetwork() {
        this.mTask = new Task();
        mUser = new UserInfo();
    }

    public void PostReplyQuoteCatcha(Document document, OnResult onResult, String str, String str2, String str3, String str4) {
        this.mTask.cancel(true);
        this.mTask = new Task();
        this.mResultListener = onResult;
        this.doc = document;
        new AnonymousClass1(str, str2, str3, str4).execute(new Void[0]);
    }

    public void getDoc(String str, OnResult onResult) {
        this.url = str;
        this.mTask.cancel(true);
        this.mTask = new Task();
        this.mResultListener = onResult;
        this.mTask.execute(new Integer[]{Integer.valueOf(0)});
    }
}
