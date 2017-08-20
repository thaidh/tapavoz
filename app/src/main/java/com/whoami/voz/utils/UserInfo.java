package com.whoami.voz.utils;

import android.graphics.Bitmap;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


public class UserInfo {
    public static String UserId;
    public static Bitmap avatar;
    public static Map<String, String> cookies;
    public static boolean isLogin;
    public static String mPass;
    public static String mUsername;
    public static String securityToken;
//    BasicCookieStore mCookieStore;

    static {
        avatar = null;
        securityToken = "guest";
    }

    public UserInfo() {
    }

    public UserInfo(String username, String pass) {
        mUsername = username;
        mPass = pass;
    }

    private String md5(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            byte[] digest = MessageDigest.getInstance("MD5").digest(str.getBytes());
            System.out.println(digest);
            for (byte b : digest) {
                stringBuffer.append(Integer.toHexString((b & MotionEventCompat.ACTION_MASK) | AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY).substring(1, 3));
            }
        } catch (NoSuchAlgorithmException e) {
        }
        return stringBuffer.toString();
    }

    public String Pass() {
        return mPass;
    }

    public void SetUser(String str) {
        mUsername = str;
    }

    public String Token() {
        return securityToken;
    }

    public String User() {
        return mUsername;
    }

    public String UserId() {
        return UserId;
    }

    public void add(String str, String str2) {
        mUsername = str;
        mPass = str2;
    }

    public void addMultiquote(String str) {
//        try {
//            mCookieStore.addCookie(new BasicClientCookie("vbulletin_multiquote", str));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public Map<String, String> cookies() {
        Map<String, String> hashMap = new HashMap();
        try {
//            for (org.apache.http.cookie.Cookie cookie : mCookieStore.getCookies()) {
//                hashMap.put(cookie.getName(), cookie.getValue());
//            }
            hashMap.put("vflastactivity", "0");
            hashMap.put("vflastvisit", "1400060756");
            hashMap.put("nnc", "1");
            return hashMap;
        } catch (Exception e) {
            return null;
        }
    }

    public void cookies(Map<String, String> map) {
        cookies = map;
        setCookieStore(cookies);
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public String getCookiStore() {
        String str = "";
        try {
//            for (Cookie cookie : mCookieStore.getCookies()) {
//                str = str + cookie.getName() + "=" + cookie.getValue() + ";";
//            }
            return str;
        } catch (Exception e) {
            return null;
        }
    }

//    public Map<String, String> getCookieStore() {
//        BasicCookieStore basicCookieStore = new BasicCookieStore();
//        basicCookieStore.addCookie(new BasicClientCookie("vflastvisit", (String) cookies.get("vflastvisit")));
//        basicCookieStore.addCookie(new BasicClientCookie("vflastactivity", (String) cookies.get("vflastactivity")));
//        basicCookieStore.addCookie(new BasicClientCookie("vfuserid", (String) cookies.get("vffuserid")));
//        basicCookieStore.addCookie(new BasicClientCookie("vfpassword", (String) cookies.get("vfpassword")));
//        basicCookieStore.addCookie(new BasicClientCookie("vfimloggedin", (String) cookies.get("vfimloggedin")));
//        basicCookieStore.addCookie(new BasicClientCookie("vfsessionhash", (String) cookies.get("vfsessionhash")));
////        return basicCookieStore;
//        return cookies;
//    }

    public boolean isLogin() {
        return isLogin;
    }

    public String md5Pass() {
        return md5(mPass);
    }

    public void setAvatart(Bitmap bitmap) {
        avatar = bitmap;
    }

    public void setCookieStore(Map<String, String> map) {
//        mCookieStore = new BasicCookieStore();
//        if (map != null) {
//            mCookieStore.addCookie(new BasicClientCookie("vfuserid", (String) map.get("vfuserid")));
//            mCookieStore.addCookie(new BasicClientCookie("vfpassword", (String) map.get("vfpassword")));
//            mCookieStore.addCookie(new BasicClientCookie("vfimloggedin", (String) map.get("vfimloggedin")));
//            mCookieStore.addCookie(new BasicClientCookie("vfsessionhash", (String) map.get("vfsessionhash")));
//        }
    }

    public void setLogin(boolean z) {
        isLogin = z;
    }

    public void setToken(String str) {
        securityToken = str;
    }

    public void setUser(String str) {
        mUsername = str;
    }

    public void setUserId(String str) {
        UserId = str;
    }
}
