//package com.nna88.voz.listview;
//
//import java.io.IOException;
//import java.net.CookieManager;
//import java.net.CookiePolicy;
//import java.net.CookieStore;
//import java.net.URI;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import org.apache.http.cookie.SM;
//
//public class WebkitCookieManagerProxy extends CookieManager {
//    private android.webkit.CookieManager webkitCookieManager;
//
//    public WebkitCookieManagerProxy() {
//        this(null, null);
//    }
//
//    public WebkitCookieManagerProxy(CookieStore cookieStore, CookiePolicy cookiePolicy) {
//        super(null, cookiePolicy);
//        this.webkitCookieManager = android.webkit.CookieManager.getInstance();
//    }
//
//    public Map<String, List<String>> get(URI uri, Map<String, List<String>> map) throws IOException {
//        if (uri == null || map == null) {
//            throw new IllegalArgumentException("Argument is null");
//        }
//        String uri2 = uri.toString();
//        Map<String, List<String>> hashMap = new HashMap();
//        if (this.webkitCookieManager.getCookie(uri2) != null) {
//            hashMap.put(SM.COOKIE, Arrays.asList(new String[]{uri2}));
//        }
//        return hashMap;
//    }
//
//    public CookieStore getCookieStore() {
//        throw new UnsupportedOperationException();
//    }
//
//    public void put(URI uri, Map<String, List<String>> map) throws IOException {
//        if (uri != null && map != null) {
//            String uri2 = uri.toString();
//            for (String str : map.keySet()) {
//                if (str != null && (str.equalsIgnoreCase(SM.SET_COOKIE2) || str.equalsIgnoreCase(SM.SET_COOKIE))) {
//                    for (String str2 : (List<String>) map.get(str)) {
//                        this.webkitCookieManager.setCookie(uri2, str2);
//                    }
//                }
//            }
//        }
//    }
//}
