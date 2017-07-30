package com.whoami.voz.retrofit.converter;

import android.util.Log;

import com.whoami.voz.retrofit.data.ForumData;
import com.whoami.voz.ui.contain.Forum;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Iterator;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by thaidh on 6/26/17.
 */

public class ForumConverter implements Converter<ResponseBody, ForumData> {

    private static final String TAG = ForumConverter.class.getSimpleName();

    @Override
    public ForumData convert(ResponseBody responseBody) throws IOException {
        Document doc = Jsoup.parse(responseBody.string());
        ForumData data =  new ForumData();
        Element first;
        //                if (this.doc.select("a[href=private.php]").first() != null) {
        //                    this.mUser.setUserId(((Element) this.doc.select("td[class=alt2]").get(0)).select("a[href*=mem]").attr("href").split("=")[1]);
        //                    first = this.doc.select("input[name*=securitytoken]").first();
        //                    if (first != null) {
        //                        this.mUser.setToken(first.attr("value"));
        //                    }
        //                }
        Iterator it = doc.select("td[class*=tcat]").iterator();
        String str = null;
        String str2 = null;
        String str3 = null;
        while (it.hasNext()) {
            first = (Element) it.next();
            if (!first.text().contains("Welcome to the vozForums")) {
                data.forumList.add(new Forum(first.text(), first.select("[href~=forum]").attr("href"), null));
                first = first.parent().parent().nextElementSibling();
                if (first != null) {
                    Iterator it2 = first.select("tr").iterator();
                    while (it2.hasNext()) {
                        try {
                            Element element = (Element) it2.next();
                            first = element.select("div:has(a[href*=forumdisplay])").first();
                            if (first != null) {
                                str = first.select("a").text();
                                str3 = first.select("a").attr("href");
                                str2 = first.select("span").text();
                                data.forumList.add(new Forum(str, str3, str2));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return data;
    }
}
