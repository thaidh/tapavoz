package com.whoami.voz.retrofit.converter;

import com.whoami.voz.retrofit.data.PostData;
import com.whoami.voz.ui.contain.VozPost;
import com.whoami.voz.ui.main.Global;

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

        Element navigationElement = doc.select("div[class=pagenav").first();
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
            if (headerElement.select("img[src*=line.gif").first() != null) {
                if (headerElement.select("img[src*=line.gif").attr("src").contains("online")) {
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
            Element messageElement = postElement.select("div[id*=post_message").first();
            if (messageElement != null) {
                if (messageElement.attr("id").split("_").length > 2) {
                    post.setId(messageElement.attr("id").split("_")[2]);
                }
                parseMessagePage3(messageElement, post, false);
            }
            Element fieldSetElement = postElement.select("fieldset[class=fieldset]").first();
            if (fieldSetElement != null) {
                post.addText("\n");
                parseMessagePage3(fieldSetElement, post, false);
            }

            if (Global.bSign && postElement.select("div:contains(_______)").first() != null) {
                post.addText("\n");
                parseMessagePage3(postElement.select("div:contains(_______)").first(), post, false);
            }
            post.initContent();
            data.vozPostList.add(post);
        }
        return data;
    }

    private void parseMessagePage3(Element element, VozPost post, boolean isGetWholeText) {
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
                                parseMessagePage3(curElement, post, isGetWholeText);
                            }
                        } else if (curElement.tagName().equals("blockquote")) {
                            Element first = curElement.select("blockquote").first();
                            post.addText("\n");
                            post.addText("\n");
                        } else if (curElement.tagName().equals("fieldset")) {
                            Element first = curElement.select("fieldset").first();
                            post.addText("\n");
                            post.addText("\n");
                        } else if (curElement.tagName().equals("b")) {
                            Element first = curElement.select("b").first();
                            int length = post.getText().length();
//                            post.type.add("", length, post.getText().length(), 1);
                        } else if (curElement.tagName().equals("i")) {
                            Element first = curElement.select("i").first();
                            int length = post.getText().length();
//                            post.type.add("", length, post.getText().length(), 2);
                        } else if (curElement.tagName().equals("ol")) {
                        } else if (curElement.tagName().equals("tbody")) {
                            Element first = curElement.select("tbody").first();
                        } else if (((Element) node).tagName().equals("li")) {
                            Element first = curElement.select("li").first();
                        } else if (curElement.tagName().equals("tr")) {
                            Element first = curElement.select("tr").first();
                            post.addText("\n");
                        } else if (curElement.tagName().equals("td")) {
                            Element first = curElement.select("td").first();
                        } else if (curElement.tagName().equals("img")) { // parse image tag
                            String imageUrl = curElement.select("img[src]").attr("src");
                            if (imageUrl.contains(Global.URL) && imageUrl.subSequence(0, 21).equals(Global.URL) && !imageUrl.contains("https://vozforums.com/attachment.php?attachmentid") && !imageUrl.contains("https://vozforums.com/customavatars/")) {
                                imageUrl = imageUrl.substring(21);
                            }
                            if (imageUrl.substring(0, 1).equals("/")) {
                                imageUrl = imageUrl.substring(1, imageUrl.length());
                            }
                            int messageLength = post.getText().length();
                            int imageUrlLength = imageUrl.length();
                            if (imageUrl.contains("http://") || imageUrl.contains("https://") || imageUrl.contains("attachment.php?attachmentid")) {
//                                post.image.add(imageUrl, messageLength, imageUrlLength + messageLength, null);
                                post.addPhoto(imageUrl);
                            } else if (imageUrl.contains(VozPost.EMO_PREFIX)) {
                                post.addEmo(imageUrl, false);
                                post.addImageUrl(imageUrl);
                                imageUrl = "  ";
                                post.addText(imageUrl);
                                if (node.hasAttr("onload")) {
                                    post.addText("\n");
                                }
                            }
                        } else if (curElement.tagName().equals("br")) {
                            post.addText("\n");
                        } else if (curElement.tagName().equals("u")) {
                            Element first = curElement.select("u").first();
                            int length = post.getText().length();
                            //todo typeU
//                            post.typeU.add("", length, post.getText().length());
                        } else if (curElement.tagName().equals("font")) {
                            Element first = curElement.select("font").first();
                            String str = "while";
                            String r1 = "3";
                            if (curElement.select("font[color]").first() != null) {
                                str = curElement.select("font[color]").attr("color");
                            }
                            String attr = curElement.select("font[size]").first() != null ? curElement.select("font[size]").attr("size") : r1;
                            int length2 = post.getText().length();
                            //todo type font
//                            post.font.add("", length2, post.getText().length(), str, Integer.parseInt(attr));
                        } else if (curElement.tagName().equals("a")) {
                            Element first = curElement.select("a[href]").first();
                            if (first.select("img").first() == null) {
                                String r0;
                                r0 = curElement.select("a[href]").attr("href").replace("%3A", ":").replace("%2F", "/").replace("%3F", "?").replace("%3D", "=").replace("%26", "&");
                                if (r0.contains("mailto:")) {
                                    String r1 = r0.substring(7, r0.length());
                                    r0 = curElement.select("a[href]").text();
                                    int length2 = post.getText().length();
                                    //todo type web
//                                    post.web.add(r1, length2, r0.length() + length2);
                                    post.addText(r0);
                                } else if (r0.contains("http")) {
                                    String r1 = r0.substring(r0.indexOf("http"), r0.length());
                                    r0 = curElement.select("a[href]").text();
                                    int length2 = post.getText().length();
                                    //todo type web
//                                    post.web.add(r1, length2, r0.length() + length2);
                                    post.addText(r1);
                                }
                            }
                            //todo
                            else {
                                parseMessagePage3(first, post, true);
                            }
                        } else {
                            post.addText(((Element) node).text());
                        }
                    } else if (node instanceof TextNode) {
                        // IMPORTANT: add plain message !!!
                        if (isGetWholeText) {
                            post.addText(((TextNode) node).getWholeText());
                        } else {
                            post.addText(((TextNode) node).text().trim());
                        }
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
                                    int length = post.getText().length();
                                    post.addQuote(first.select("strong").text());
                                    int length2 = post.getText().length();
                                    //todo add web
//                                post.web.add(Global.URL + first.select("a").attr("href"), length, length2);
//                                post.type.add("", length, length2, 1);
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
                                    imageUrl = "  ";
                                    post.addQuote(imageUrl);
                                    if (node.hasAttr("onload")) {
                                        post.addQuote("\n");
                                    }
                                }
                            } else if (curElement.tagName().equals("br")) {
                                post.addQuote("\n");
                            } else if (curElement.tagName().equals("font")) {
                                //todo type font
//                            post.font.add("", length2, post.getText().length(), str, Integer.parseInt(attr));
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
