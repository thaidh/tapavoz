package com.whoami.voz.retrofit.converter;

import com.whoami.voz.retrofit.data.PostData;
import com.whoami.voz.contain.VozPost;
import com.whoami.voz.main.Global;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Iterator;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by thaidh on 6/26/17.
 */

public class PostConverter implements Converter<ResponseBody, PostData> {

    @Override
    public PostData convert(ResponseBody responseBody) throws IOException {
        Document doc = Jsoup.parse(responseBody.string());
        PostData data = new PostData();

        Element navigationElement = doc.select("div[class=pagenav]").first();
        if (navigationElement != null) {
            String strPage = navigationElement.select("td[class=vbmenu_control]").text();
            data.totalPage = Integer.parseInt(strPage.split(" ")[3]);
        }

        Element contentElement = doc.select("div[align=center]").size() > 1 ? (Element) doc.select("div[align=center]").get(1) : doc;
        Elements postElementsList = contentElement.select("td[id*=td_post]");
        Iterator it = postElementsList.iterator();
        String userTitle = null;
        while (it.hasNext()) {
            String username;
            String avatarUrl = null;
            VozPost post = new VozPost();
            String time = "";
            Element postElement = (Element) it.next();
            Element parent = postElement.parent();
            if (parent.select("div[class=smallfont]:has(strong)").first() != null) {
                data.title = parent.select("div[class=smallfont]:has(strong)").first().text();
            }
            //===================Parse header================
            Element headerElement = parent.previousElementSibling();
            Element postTimeElement = headerElement.previousElementSibling();
            if (headerElement.select("img[src*=avatars]").first() != null) {
                avatarUrl = headerElement.select("img[src*=avatars]").attr("src");
                if (!avatarUrl.contains(Global.URL)) {
                    avatarUrl = Global.URL + avatarUrl;
                }
            }
            if (headerElement.select("div:containsOwn(Join Date)").first() != null) {
                String joinDate = headerElement.select("div:containsOwn(Join Date)").first().text();
                if (joinDate.contains("Date:")) {
                    joinDate = joinDate.split("Date:")[1];
                }
                post.setJD("Jd:" + joinDate);
            } else {
                post.setJD("");
            }
            if (headerElement.select("div:containsOwn(Posts: )").first() != null) {
                post.setPosts(headerElement.select("div:containsOwn(Posts: )").first().text());
            } else {
                post.setPosts("");
            }
            if (headerElement.select("img[src*=line.gif]").first() != null) {
                if (headerElement.select("img[src*=line.gif]").attr("src").contains("online")) {
                    post.setOnline(true);
                } else {
                    post.setOnline(false);
                }
            }
            if (headerElement.select("a[class=bigusername]").first() != null) {
                username = headerElement.select("a[class=bigusername]").text();
                post.setUid(headerElement.select("a[class=bigusername]").first().attr("href").split("u=")[1]);
            } else {
                username = userTitle;
            }
            userTitle = headerElement.select("div[class=smallfont]").first() != null ? headerElement.select("div[class=smallfont]").first().text() : time;
            time = postTimeElement.text();
            post.setInfo(username, userTitle, time, avatarUrl);

            //===================Parse body================
            Element messageElement = postElement.select("div[id*=post_message]").first();
            if (messageElement != null) {
                if (messageElement.attr("id").split("_").length > 2) {
                    post.setId(messageElement.attr("id").split("_")[2]);
                }
                parseMessagePage3(messageElement, post);
            }
            Element fieldSetElement = postElement.select("fieldset[class=fieldset]").first();
            if (fieldSetElement != null) {
                post.addText("\n");
                parseMessagePage3(fieldSetElement, post);
            }

            if (Global.bSign && postElement.select("div:contains(_______)").first() != null) {
                post.addText("\n");
                parseMessagePage3(postElement.select("div:contains(_______)").first(), post);
            }
            post.initContent();
            data.vozPostList.add(post);
        }
        return data;
    }

    private void parseMessagePage3(Element element, VozPost post) {
        try {
            if (element != null) {
                for (Node node : element.childNodes()) {
                    if (node instanceof Element) {
                        Element curElement = (Element) node;
                        if (curElement.tagName().equals("div")) {
                            Elements quotes = curElement.getElementsByClass("voz-bbcode-quote");
                            if (!quotes.isEmpty()) {
                                for (int i = 0; i < quotes.size(); i++) {
                                    parseQuote(quotes.get(i).select("tbody").first().select("td").first(), post);
                                }
                            } else {
                                parseMessagePage3(curElement, post);
                            }
                        } else if (curElement.tagName().equals("blockquote")) {
                            post.addText("\n");
                            post.addText("\n");
                        } else if (curElement.tagName().equals("fieldset")) {
                            post.addText("\n");
                            post.addText("\n");
                        } else if (curElement.tagName().equals("b")) {
                            parseMessagePage3(curElement, post);
                        } else if (curElement.tagName().equals("i")) {
                            parseMessagePage3(curElement, post);
                        } else if (curElement.tagName().equals("img")) { // parse image tag
                            String imageUrl = curElement.select("img[src]").attr("src");
                            if (imageUrl.contains(Global.URL) && imageUrl.subSequence(0, 21).equals(Global.URL) && !imageUrl.contains("https://vozforums.com/attachment.php?attachmentid") && !imageUrl.contains("https://vozforums.com/customavatars/")) {
                                imageUrl = imageUrl.substring(21);
                            }
                            if (imageUrl.substring(0, 1).equals("/")) {
                                imageUrl = imageUrl.substring(1, imageUrl.length());
                            }
                            if (imageUrl.contains("http://") || imageUrl.contains("https://") || imageUrl.contains("attachment.php?attachmentid")) {
                                post.addPhoto(imageUrl);
                            } else if (imageUrl.contains(VozPost.EMO_PREFIX)) {
                                post.addEmo(imageUrl, false);
                                post.addImageUrl(imageUrl);
                                if (node.hasAttr("onload")) {
                                    post.addText("\n");
                                }
                            }
                        } else if (curElement.tagName().equals("br")) {
                            post.addText("\n");
                        } else if (curElement.tagName().equals("u")) {
                            parseMessagePage3(curElement, post);
                        } else if (curElement.tagName().equals("font")) {
                            parseMessagePage3(curElement, post);
                        } else if (curElement.tagName().equals("a")) {
                            Element first = curElement.select("a[href]").first();
                            if (first.select("img").first() == null) {
                                String r0 = curElement.select("a[href]").attr("href").replace("%3A", ":").replace("%2F", "/").replace("%3F", "?").replace("%3D", "=").replace("%26", "&");
                                if (r0.contains("mailto:")) {
                                    String r1 = r0.substring(7, r0.length());
                                    post.addUrl(r1, false);
                                } else if (r0.contains("http")) {
                                    String r1 = r0.substring(r0.indexOf("http"), r0.length());
                                    post.addUrl(r1, false);
                                }
                            } else {
                                parseMessagePage3(first, post);
                            }
                        } else {
                            post.addText(((Element) node).text());
                        }
                    } else if (node instanceof TextNode) {
                        post.addText(((TextNode) node).text().trim());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseQuote(Element element, VozPost post) {
        try {
            if (element != null) {
                if (element.childNodes().size() > 0) {
                    for (int i = 0; i < element.childNodes().size(); i++) {
                        Node node = element.childNodes().get(i);
                        if (node instanceof Element) {
                            Element curElement = (Element) node;
                            if (curElement.tagName().equals("div")) {
                                Element first = curElement.select("div").first();
                                if (first.attr("style").contains("padding")) {
                                    post.addQuote("\n");
                                }
                                if (first.ownText().contains("Originally Posted by")) {
                                    post.addQuote("Originally Posted by ");
                                    post.addQuote(first.select("strong").text());
                                    post.addQuote("\n");
                                } else {
                                    parseQuote(curElement, post);
                                }
                            } else if (curElement.tagName().equals("img")) { // parse image tag
                                String imageUrl = curElement.select("img[src]").attr("src");
                                if (imageUrl.contains(Global.URL) && imageUrl.subSequence(0, 21).equals(Global.URL) && !imageUrl.contains("https://vozforums.com/attachment.php?attachmentid") && !imageUrl.contains("https://vozforums.com/customavatars/")) {
                                    imageUrl = imageUrl.substring(21);
                                }
                                if (imageUrl.substring(0, 1).equals("/")) {
                                    imageUrl = imageUrl.substring(1, imageUrl.length());
                                }
                                if (imageUrl.contains("http://") || imageUrl.contains("https://") || imageUrl.contains("attachment.php?attachmentid")) {
                                    post.addPhoto(imageUrl);
                                } else if (imageUrl.contains(VozPost.EMO_PREFIX)) {
                                    post.addEmo(imageUrl, true);
                                    post.addImageUrl(imageUrl);
                                    if (node.hasAttr("onload")) {
                                        post.addQuote("\n");
                                    }
                                }
                            } else if (curElement.tagName().equals("br")) {
                                post.addQuote("\n");
                            } else if (curElement.tagName().equals("font")) {
                                parseQuote(curElement, post);
                            } else if (curElement.tagName().equals("a")) {
                                Element first = curElement.select("a[href]").first();
                                if (first.select("img").first() == null) {
                                    String r0 = curElement.select("a[href]").attr("href").replace("%3A", ":").replace("%2F", "/").replace("%3F", "?").replace("%3D", "=").replace("%26", "&");
                                    if (r0.contains("mailto:")) {
                                        String r1 = r0.substring(7, r0.length());
                                        post.addUrl(r1, true);
                                    } else if (r0.contains("http")) {
                                        String r1 = r0.substring(r0.indexOf("http"), r0.length());
                                        post.addUrl(r1, true);
                                    }
                                }
                            } else if (curElement.tagName().equals("b")) {
                                parseQuote(curElement, post);
                            } else if (curElement.tagName().equals("i")) {
                                parseQuote(curElement, post);
                            } else {
                                post.addQuote(curElement.text());
                            }
                        } else if (node instanceof TextNode) {
                            post.addQuote(((TextNode) node).text().trim());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
