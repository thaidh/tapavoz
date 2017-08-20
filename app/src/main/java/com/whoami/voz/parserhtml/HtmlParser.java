//package com.whoami.voz.parserhtml;
//
//import android.support.v4.view.MotionEventCompat;
//import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
//import android.util.Log;
//
//import com.whoami.voz.main.Global;
//import com.whoami.voz.utils.UserInfo;
//import com.whoami.voz.utils.Util;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.jsoup.Connection.Method;
//import org.jsoup.Connection.Response;
//import org.jsoup.HttpStatusException;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//
//import java.io.IOException;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.Map;
//
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//
//public class HtmlParser {
//    public static final int TIMEOUT = 20000;
//    static UserInfo mUser;
//    private String mUserIdRecentPost;
//    private String m_url;
//    public String sNotif;
//
//    static {
//        mUser = new UserInfo();
//    }
//
//    public HtmlParser(String str) {
//        this.sNotif = "";
//        this.mUserIdRecentPost = null;
//        this.m_url = str;
//        mUser = new UserInfo();
//    }
//
//    private void log(String str) {
//        Log.d("nna", str);
//    }
//
//    public Document Logout() {
//        try {
//            mUser.cookies(Jsoup.connect("https://vozforums.com/login.php?do=logout&logouthash=" + mUser.Token()).timeout(TIMEOUT).method(Method.POST).execute().cookies());
//            return getDoc();
//        } catch (Exception e) {
//            this.sNotif = "Cannot access vozForums\n";
//            this.sNotif = e.getLocalizedMessage();
//            if (e instanceof HttpStatusException) {
//                this.sNotif += "\nStatus: " + ((HttpStatusException) e).getStatusCode();
//            }
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public Document NewThread(String str, String str2, String str3, String str4, String str5, String str6) {
//        try {
//            Response execute = Jsoup.connect("https://vozforums.com/newthread.php?do=postthread&f=" + str3).timeout(TIMEOUT).cookies(mUser.cookies()).data("subject", str).data("message", str2).data("wysiwyg", "0").data("s", " ").data("securitytoken", str4).data("f", str3).data("do", "postthread").data("posthash", str5).data("poststarttime", str6).data("loggedinuser", mUser.UserId()).data("sbutton", "Submit New Thread").data("signature", "1").data("parseurl", "1").data("emailupdate", "9999").method(Method.POST).execute();
//            this.m_url = execute.url().toString();
//            return execute.parse();
//        } catch (Exception e) {
//            this.sNotif = "Cannot access vozForums\n";
//            this.sNotif = e.getLocalizedMessage();
//            if (e instanceof HttpStatusException) {
//                this.sNotif += "\nStatus: " + ((HttpStatusException) e).getStatusCode();
//            }
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//
//    public Document PostQuickReply(String str, String str2, String str3) {
//        try {
//            Response execute = Jsoup.connect(Global.URL + str2).timeout(TIMEOUT).cookies(mUser.cookies()).data("message", str).data("wysiwyg", "0").data("styleid", "0").data("fromquickreply", "1").data("s", " ").data("securitytoken", str3).data("do", "postreply").data("t", str2.split("=")[2]).data("p", "who cares").data("specifiedpost", "0").data("parseurl", "1").data("loggedinuser", mUser.UserId()).data("sbutton", "Post Quick Reply").data("signature", "1").method(Method.POST).execute();
//            this.m_url = execute.url().toString();
//            return execute.parse();
//        } catch (Exception e) {
//            this.sNotif = "Cannot access vozForums\n";
//            this.sNotif = e.getLocalizedMessage();
//            if (e instanceof HttpStatusException) {
//                this.sNotif += "\nStatus: " + ((HttpStatusException) e).getStatusCode();
//            }
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public Document PostReplyQuote(Document document, UserInfo userInfo, String str, String str2) throws Exception {
//        if (userInfo.cookies() == null) {
//            return null;
//        }
//        Element first = document.select("form[action*=newreply.php?do=postreply]").first();
//        Element first2 = document.select("input[name*=securitytoken]").first();
//        Element first3 = document.select("input[name*=posthash]").first();
//        Element first4 = document.select("input[name*=poststarttime]").first();
//        Element first5 = document.select("input[name=title]").first();
//        Element first6 = document.select("input[name=p]").first();
//        userInfo.setToken(first2.attr("value"));
//        String str3 = Global.URL + first.attr("action").replace("&amp;", "&");
//        String str4 = str3.split("=")[2];
//        String attr = first3.attr("value");
//        try {
//            Response execute = Jsoup.connect(str3).timeout(TIMEOUT).cookies(userInfo.cookies()).data("title:Re: ", first5.attr("value")).data("message", str).data("wysiwyg", "0").data("s", " ").data("securitytoken", userInfo.Token()).data("do", "postreply").data("t", str4).data("p", first6.attr("value")).data("specifiedpost", str2).data("posthash", attr).data("poststarttime", first4.attr("value")).data("loggedinuser", userInfo.UserId()).data("multiquoteempty:", " ").data("sbutton", "Submit Reply").data("signature", "1").data("parseurl", "1").method(Method.POST).execute();
//            this.m_url = execute.url().toString();
//            return execute.parse();
//        } catch (Exception e) {
//            this.sNotif = "Cannot access vozForums\n";
//            this.sNotif = e.getLocalizedMessage();
//            if (e instanceof HttpStatusException) {
//                this.sNotif += "\nStatus: " + ((HttpStatusException) e).getStatusCode();
//            }
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public Document Search(String str, String str2, String str3) {
//        Map cookies = mUser.cookies();
//        if (cookies == null || str == null) {
//            return null;
//        }
//        try {
//            Document parse = Jsoup.connect("https://vozforums.com/search.php?do=process").timeout(TIMEOUT).cookies(cookies).data("s", " ").data("securitytoken", str).data("do", "process").data("query", str2).data("showposts", str3).data("quicksearch", "1").data("childforums", "1").data("exactname", "1").execute().parse();
//            this.m_url = parse.baseUri();
//            return parse;
//        } catch (Exception e) {
//            this.sNotif = "Cannot access vozForums\n";
//            this.sNotif = e.getLocalizedMessage();
//            if (e instanceof HttpStatusException) {
//                this.sNotif += "\nStatus: " + ((HttpStatusException) e).getStatusCode();
//            }
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public Document SearchQuote() {
//        if (!mUser.isLogin()) {
//            return null;
//        }
//        try {
//            String attr;
//            Document document = Jsoup.connect("https://vozforums.com/usercp.php").cookies(mUser.cookies()).get();
//            if (document != null) {
//                Element first = document.select("form[action*=profile.php]").first();
//                if (first != null) {
//                    attr = first.select("input[name=securitytoken]").attr("value");
//                } else {
//                    Log.d("nna", "Sectoken null");
//                    return null;
//                }
//            }
//            attr = null;
//            document = Jsoup.connect("https://vozforums.com/search.php?do=process").timeout(TIMEOUT).cookies(mUser.cookies()).data("do", "process").data("quicksearch", "1").data("childforums", "1").data("exactname", "1").data("s", " ").data("securitytoken", attr).data("query", mUser.User()).data("showposts", "1").method(Method.POST).execute().parse();
//            this.m_url = document.baseUri();
//            return document;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public Document SearchQuote(Map<String, String> map) {
//        Map<String, String> cookies = null;
//        if (mUser.isLogin()) {
//            cookies = mUser.cookies();
//        }
//        try {
//            String attr;
//            String User;
//            Document document = Jsoup.connect("https://vozforums.com/usercp.php").cookies(cookies).timeout(TIMEOUT).get();
//            if (document != null) {
//                Element first = document.select("form[action*=profile.php]").first();
//                if (first != null) {
//                    attr = first.select("input[name=securitytoken]").attr("value");
//                    User = mUser.User() != null ? mUser.User() : document.select("a[href*=member.php]").first().text();
//                } else {
//                    Log.d("nna", "Sectoken null");
//                    return null;
//                }
//            }
//            attr = null;
//            User = null;
//            Document parse = Jsoup.connect("https://vozforums.com/search.php?do=process").timeout(TIMEOUT).cookies(cookies).data("do", "process").data("quicksearch", "1").data("childforums", "1").data("exactname", "1").data("s", " ").data("securitytoken", attr).data("query", User).data("showposts", "1").method(Method.POST).execute().parse();
//            this.m_url = parse.baseUri();
//            return parse;
//        } catch (Exception e) {
//            this.sNotif = "Cannot access vozForums\n";
//            this.sNotif = e.getLocalizedMessage();
//            if (e instanceof HttpStatusException) {
//                this.sNotif += "\nStatus: " + ((HttpStatusException) e).getStatusCode();
//            }
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public Document UnSubscribe(String str) {
//        try {
//            Jsoup.connect("https://vozforums.com/subscription.php?do=removesubscription&t=" + str).timeout(TIMEOUT).cookies(mUser.cookies()).method(Method.GET).execute();
//            return getDoc();
//        } catch (Exception e) {
//            this.sNotif = "Cannot access vozForums\n";
//            this.sNotif = e.getLocalizedMessage();
//            if (e instanceof HttpStatusException) {
//                this.sNotif += "\nStatus: " + ((HttpStatusException) e).getStatusCode();
//            }
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public Document getDoc() {
//        try {
//            OkHttpClient client = new OkHttpClient();
//            Request request = new Request.Builder().url(m_url)
//                    .build();
//            okhttp3.Response response = client.newCall(request).execute();
//            return Jsoup.parse(response.body().string());
//
//
////            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
////            HttpUriRequest httpGet = new HttpGet(this.m_url);
////            if (mUser.getCookiStore() != null) {
////                httpGet.addHeader(SM.COOKIE, mUser.getCookiStore());
////            }
////            HttpClientParams.setRedirecting(defaultHttpClient.getParams(), false);
////            HttpResponse execute = defaultHttpClient.execute(httpGet);
////            if (execute.getStatusLine().getStatusCode() != 200) {
////                Header[] headers = execute.getHeaders("Location");
////                if (!(headers == null || headers.length == 0)) {
////                    this.m_url = headers[headers.length - 1].getValue();
////                    return getDoc();
////                }
////            }
////            HttpEntity entity = execute.getEntity();
////            String entityUtils = entity != null ? EntityUtils.toString(entity) : null;
////            if (entity != null) {
////                entity.consumeContent();
////            }
////            List cookies = defaultHttpClient.getCookieStore().getCookies();
////            if (cookies.isEmpty()) {
////                mUser.setLogin(false);
////            } else {
////                for (int i = 0; i < cookies.size(); i++) {
////                    if (((Cookie) cookies.get(i)).getName().equals("vfsessionhash")) {
////                        mUser.setLogin(true);
////                        break;
////                    }
////                }
////            }
////            defaultHttpClient.getConnectionManager().shutdown();
////            return Jsoup.parse(entityUtils);
//        } catch (IOException e) {
//                e.printStackTrace();
//        }
//        return null;
//    }
//
//    public Document getPM() {
//        try {
//            return Jsoup.connect("https://vozforums.com/private.php").timeout(TIMEOUT).cookies(mUser.cookies()).execute().parse();
//        } catch (Exception e) {
//            this.sNotif = "Cannot access vozForums\n";
//            this.sNotif = e.getLocalizedMessage();
//            if (e instanceof HttpStatusException) {
//                this.sNotif += "\nStatus: " + ((HttpStatusException) e).getStatusCode();
//            }
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public String getUrl() {
//        return this.m_url;
//    }
//
//    private String md5(String str) {
//        StringBuffer stringBuffer = new StringBuffer();
//        try {
//            byte[] digest = MessageDigest.getInstance("MD5").digest(str.getBytes());
//            System.out.println(digest);
//            for (byte b : digest) {
//                stringBuffer.append(Integer.toHexString((b & MotionEventCompat.ACTION_MASK) | AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY).substring(1, 3));
//            }
//        } catch (NoSuchAlgorithmException e) {
//        }
//        return stringBuffer.toString();
//    }
//
//    public Document login() {
//        String userName = "";
//        String pass = "";
//        mUser = new UserInfo(userName, pass);
//        String capcha;
////        Response execute;
//        JSONException jSONException;
//        JSONException jSONException2;
//        Document document;
////        String str2;
//        JSONObject jSONObject;
//        Map cookies;
//        JSONException e;
//        String str3 = "";
//        String str4 = "";
//        try {
//            Response execute2 = Jsoup.connect("https://vozforums.com/vbdev/login_api.php").
//                    timeout(TIMEOUT).data("do", "login")
//                    .data("api_cookieuser", "1")
//                    .data("securitytoken", "guest")
//                    .data("api_vb_login_md5password", mUser.md5Pass())
//                    .data("api_vb_login_md5password_utf", mUser.md5Pass())
//                    .data("api_vb_login_password", mUser.Pass())
//                    .data("api_vb_login_username", mUser.User())
//                    .data("api_2factor", "").data("api_captcha", "")
//                    .data("api_salt", "").method(Method.POST).execute();
//            Document parse = execute2.parse();
//            try {
//                capcha = (String) new JSONObject(parse.text()).get("captcha");
//
//                Response execute1 = Jsoup.connect("https://vozforums.com/vbdev/login_api.php")
//                        .timeout(TIMEOUT).data("do", "login")
//                        .data("api_cookieuser", "1")
//                        .data("securitytoken", "guest")
//                        .data("api_vb_login_md5password", mUser.md5Pass())
//                        .data("api_vb_login_md5password_utf", mUser.md5Pass())
//                        .data("api_vb_login_password", mUser.Pass())
//                        .data("api_vb_login_username", mUser.User())
//                        .data("api_2factor", "")
//                        .data("api_captcha", capcha).data("api_salt", "").method(Method.POST).execute();
//                document = execute1.parse();
//                jSONObject = new JSONObject(document.text());
//                JSONObject jsUserInfo = (JSONObject) jSONObject.get("userinfo");
//
//                String uid =  jsUserInfo.getString("userid");
//                if (document.select("div:contains(You have entered an invalid username or password)").first() == null) {
//                    cookies = execute2.cookies();
//                    cookies.put("vfuserid", uid);
//                    mUser.cookies(cookies);
//                    mUser.setLogin(true);
//                    Util.showMess( mUser.User() + "login success!X");
//                    return getDoc();
//                }
//                this.sNotif = null;
//                return null;
//            } catch (JSONException e4) {
//            }
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//        return getDoc();
//    }
//
//    public Document myRecentPost() {
//        Map cookies = mUser.cookies();
//        String UserId = this.mUserIdRecentPost != null ? this.mUserIdRecentPost : mUser.UserId();
//        this.mUserIdRecentPost = null;
//        if (cookies == null || UserId == null) {
//            return null;
//        }
//        try {
//            Document parse = Jsoup.connect("https://vozforums.com/search.php?do=finduser&u=" + UserId).timeout(TIMEOUT).cookies(cookies).execute().parse();
//            this.m_url = parse.baseUri();
//            return parse;
//        } catch (Exception e) {
//            this.sNotif = "Cannot access vozForums\n";
//            this.sNotif = e.getLocalizedMessage();
//            if (e instanceof HttpStatusException) {
//                this.sNotif += "\nStatus: " + ((HttpStatusException) e).getStatusCode();
//            }
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public Document myRecentThread() {
//        Map cookies = mUser.cookies();
//        String UserId = mUser.UserId();
//        if (cookies == null || UserId == null) {
//            return null;
//        }
//        try {
//            Document parse = Jsoup.connect(("https://vozforums.com/search.php?do=finduser&u=" + UserId) + "&starteronly=1").timeout(TIMEOUT).cookies(cookies).execute().parse();
//            this.m_url = parse.baseUri();
//            return parse;
//        } catch (Exception e) {
//            this.sNotif = "Cannot access vozForums\n";
//            this.sNotif = e.getLocalizedMessage();
//            if (e instanceof HttpStatusException) {
//                this.sNotif += "\nStatus: " + ((HttpStatusException) e).getStatusCode();
//            }
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public Document quoteGet(int i, int i2) {
//        String str;
//        if (i == 1) {
//            str = "https://vozforums.com/newreply.php?do=newreply&p=";
//        } else if (i == 2 || i == 3) {
//            str = "https://vozforums.com/editpost.php?do=editpost&p=";
//        } else if (i == 0) {
//            return null;
//        } else {
//            str = null;
//        }
//        try {
//            this.m_url = str + String.valueOf(i2);
//            Response execute = Jsoup.connect(this.m_url).timeout(TIMEOUT).cookies(mUser.cookies()).method(Method.GET).execute();
//            Log.d("nna", "quoteget ok");
//            return execute.parse();
//        } catch (Exception e) {
//            this.sNotif = "Cannot access vozForums\n";
//            this.sNotif = e.getLocalizedMessage();
//            if (e instanceof HttpStatusException) {
//                this.sNotif += "\nStatus: " + ((HttpStatusException) e).getStatusCode();
//            }
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public void setUrl(String str) {
//        this.m_url = str;
//    }
//
//    public void setUserRecentPost(String str) {
//        this.mUserIdRecentPost = str;
//    }
//
//    public void testHtml() {
//    }
//}
