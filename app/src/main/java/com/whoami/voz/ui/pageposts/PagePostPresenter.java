package com.whoami.voz.ui.pageposts;

import com.whoami.voz.ui.activity.BaseActivity;
import com.whoami.voz.ui.contain.VozPost;
import com.whoami.voz.ui.main.Global;
import com.whoami.voz.ui.utils.HtmlLoader;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by thaidh on 5/2/17.
 */

public class PagePostPresenter implements PagePostContract.Presenter {

    private PagePostContract.View mPagePostView;

    private Map<Integer, ArrayList<VozPost>> mMapPostPerPage = new LinkedHashMap() {
        public boolean removeEldestEntry(Map.Entry eldest) {
            return size() > BaseActivity.MAX_ENTRIES;
        }
    };
    private String mUrl;
    private int mTotalPage;
    private String mTitle;

    public PagePostPresenter(PagePostContract.View mPagePostView, String mUrl) {
        this.mPagePostView = mPagePostView;
        this.mUrl = mUrl;
    }

    @Override
    public void start() {

    }

    public void loadPage(final int curPage, final boolean refres) {
        try {
            if (refres || !mMapPostPerPage.containsKey(Integer.valueOf(curPage))) {
                final String url = getUrlWithPage(curPage);
                if (url != null) {
                    HtmlLoader.getInstance().fetchData(url, curPage, new HtmlLoader.HtmlLoaderListener() {
                        @Override
                        public void onCallback(Document doc, int curPage) {
                            parseDataPage3(doc, refres, curPage);
                        }
                    });
                }
            } else {
                mPagePostView.refreshCurrentPage(mTotalPage, curPage, false, mMapPostPerPage.get(curPage));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUrlWithPage(int mPage) {
        if (!mUrl.contains("&page=")) {
            mUrl = mUrl.concat("&page=1");
        }
        String concat = mUrl.substring(0, mUrl.lastIndexOf("=") + 1).concat(String.valueOf(mPage));
        return concat.contains("&page=0") ? concat.split("&page")[0] : concat;
    }

    void parseDataPage3(Document doc, final boolean refresh, final int curPage) {
        if (doc != null) {
//            UserInfo mUser = new UserInfo();
//            String mPostId;
            VozPost post;
            ArrayList<VozPost> mListPost = new ArrayList<>();
            Element navigationElement = doc.select("div[class=pagenav").first();
            if (navigationElement != null) {
                String strPage = navigationElement.select("td[class=vbmenu_control]").text();
                mTotalPage = Integer.parseInt(strPage.split(" ")[3]);
            }

            Element contentElement = doc.select("div[align=center]").size() > 1 ? (Element) doc.select("div[align=center]").get(1) : doc;
//                    Element r0 = element.select("a[href=private.php]").first();
//            mPostId = getPostIdFromUrl(url);
//                    if (r0 != null) {
//                        mUser.setUserId(((Element) element.select("td[class=alt2]").get(0)).select("a[href*=mem]").attr("href").split("=")[1]);
//                        r0 = element.select("input[name*=securitytoken]").first();
//                        if (r0 != null) {
//                            mUser.setToken(r0.attr("value"));
//                        }
//                        r0 = doc.select("div[style=margin-top:6px]:has(input[value=Post Quick Reply])").first();
//                        if (r0 != null) {
//                            String sIdThread = r0.select("input[name=t]").attr("value");
//                        }
//                        r0 = doc.select("td[class=alt1]:has(a[href *= subscription.php]").first();
//                        if (r0 != null) {
//                            if (r0.select("a[href *= subscription.php]").attr("href").contains("removesubscription")) {
//                                this.isSubscribe = true;
//                                ((SidebarAdapter.Item) this.mListSideMenu2.get(3)).mTitle = getResources().getString(R.string.UnSubscribe);
//                                this.mAdapterSideMenu2.notifyDataSetInvalidated();
//                            } else {
//                                ((SidebarAdapter.Item) this.mListSideMenu2.get(3)).mTitle = getResources().getString(R.string.Subscribe);
//                                this.mAdapterSideMenu2.notifyDataSetInvalidated();
//                                this.isSubscribe = false;
//                            }
//                        }
//                    }
            Elements postElementsList = contentElement.select("td[id*=td_post]");
            Iterator it = postElementsList.iterator();
            String userTitle = null;
            while (it.hasNext()) {
                String username;
                String avatarUrl = null;
                post = new VozPost();
                String time = "";
                Element postElement = (Element) it.next();
                Element parent = postElement.parent();
                if (parent.select("div[class=smallfont]:has(strong)").first() != null) {
                    mTitle = parent.select("div[class=smallfont]:has(strong)").first().text();
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
                        post.isOnline = true;
                    } else {
                        post.isOnline = false;
                    }
                }
                if (headerElement.select("a[class=bigusername]").first() != null) {
                    username = headerElement.select("a[class=bigusername]").text();
                    post.m_UserId = headerElement.select("a[class=bigusername]").first().attr("href").split("u=")[1];
                } else {
                    username = userTitle;
                }
                userTitle = headerElement.select("div[class=smallfont]").first() != null ? headerElement.select("div[class=smallfont]").first().text() : time;
                time = postTimeElement.text();
                post.Info(username, userTitle, time, avatarUrl);

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
                    post.addText("\n", false);
                    parseMessagePage3(fieldSetElement, post, false);
                }

                if (Global.bSign && postElement.select("div:contains(_______)").first() != null) {
                    post.addText("\n", false);
                    parseMessagePage3(postElement.select("div:contains(_______)").first(), post, false);
                }
                post.initContent();
                mListPost.add(post);
//                        if (post.Id().equals(mPostId)) {
//                            userTitle = username;
//                        } else {
//
//                            userTitle = username;
//                        }
            }
            mMapPostPerPage.put(Integer.valueOf(curPage), mListPost);
            mPagePostView.refreshCurrentPage(mTotalPage, curPage, refresh, mListPost);
        }
    }

    private void parseMessagePage3(Element element, VozPost post, boolean isGetWholeText) {
        parseMessagePage3(element, post, isGetWholeText, false);
    }

    private void parseMessagePage3(Element element, VozPost post, boolean isGetWholeText, boolean isQuote) {
        try {
            if (element != null) {
                for (Node node : element.childNodes()) {
                    if (node instanceof Element) {
                        if (((Element) node).tagName().equals("div")) {
                            Element first = ((Element) node).select("div").first();
                            if (first.attr("style").contains("padding")) {
                                post.addText("\n", isQuote);
                            }
                            if (first.ownText().contains("Originally Posted by")) {
                                post.addText("Originally Posted by ", isQuote);
                                int length = post.getText().length();
                                post.addText(first.select("strong").text(), isQuote);
                                int length2 = post.getText().length();
                                post.web.add(Global.URL + first.select("a").attr("href"), length, length2);
                                post.type.add("", length, length2, 1);
                                post.addText("\n", isQuote);
                            } else {
                                parseMessagePage3(first, post, isGetWholeText, isQuote);
                                post.addText("\n", isQuote);
                            }
                        } else if (((Element) node).tagName().equals("blockquote")) {
                            Element first = ((Element) node).select("blockquote").first();
                            post.addText("\n", isQuote);
                            parseMessagePage3(first, post, isGetWholeText, isQuote);
                            post.addText("\n", isQuote);
                        } else if (((Element) node).tagName().equals("fieldset")) {
                            Element first = ((Element) node).select("fieldset").first();
                            post.addText("\n", isQuote);
                            parseMessagePage3(first, post, isGetWholeText, isQuote);
                            post.addText("\n", isQuote);
                        } else if (((Element) node).tagName().equals("b")) {
                            Element first = ((Element) node).select("b").first();
                            int length = post.getText().length();
                            parseMessagePage3(first, post, isGetWholeText, isQuote);
                            post.type.add("", length, post.getText().length(), 1);
                        } else if (((Element) node).tagName().equals("i")) {
                            Element first = ((Element) node).select("i").first();
                            int length = post.getText().length();
                            parseMessagePage3(first, post, isGetWholeText, isQuote);
                            post.type.add("", length, post.getText().length(), 2);
                        }
                        /*else if (((Element) node).tagName().equals("pre")) {
                            Element first = ((Element) node).select("pre").first();
                            int length = post.getText().length();
                            int startQuote = post.getLastTextItem().mData.length();
                            parseMessagePage3(first, post, isGetWholeText, isQuote);
                            post.quote.add("", length, post.getText().length());
                            int endQuote = post.getLastTextItem().mData.length();
                            post.getLastTextItem().addQuote(startQuote, endQuote);

                        }*/ else if (((Element) node).tagName().equals("table")) {
                            Element first = ((Element) node).select("table").first();
//                            int length = post.getText().length();
//                            int startQuote = post.getLastTextItem().mData.length();
                            parseMessagePage3(first, post, isGetWholeText, true);
//                            post.quote.add("", length, post.getText().length());
//                            int endQuote = post.getLastTextItem().mData.length();
//                            post.getLastTextItem().addQuote(startQuote, endQuote);
                        } else if (((Element) node).tagName().equals("ol")) {
                        } else if (((Element) node).tagName().equals("tbody")) {
                            Element first = ((Element) node).select("tbody").first();
                            parseMessagePage3(first, post, isGetWholeText, isQuote);
                        } else if (((Element) node).tagName().equals("li")) {
                            Element first = ((Element) node).select("li").first();
                            parseMessagePage3(first, post, isGetWholeText, isQuote);
                        } else if (((Element) node).tagName().equals("tr")) {
                            Element first = ((Element) node).select("tr").first();
                            post.addText("\n", isQuote);
                            parseMessagePage3(first, post, isGetWholeText, isQuote);
                        } else if (((Element) node).tagName().equals("td")) {
                            Element first = ((Element) node).select("td").first();
                            parseMessagePage3(first, post, isGetWholeText, isQuote);
                        } else if (((Element) node).tagName().equals("img")) { // parse image tag
                            String imageUrl = ((Element) node).select("img[src]").attr("src");
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
                                post.addEmo(imageUrl, isQuote);
                                post.image.add(imageUrl, messageLength, messageLength + 2, null);
                                imageUrl = "  ";
//                                if (/*!imageUrl.contains("images/buttons/viewpost.gif") &&*/) {
//                                }
                                post.addText(imageUrl, isQuote);
                                if (node.hasAttr("onload")) {
                                    post.addText("\n", isQuote);
                                }
                            }
                        } else if (((Element) node).tagName().equals("br")) {
                            post.addText("\n", isQuote);
                        } else if (((Element) node).tagName().equals("u")) {
                            Element first = ((Element) node).select("u").first();
                            int length = post.getText().length();
                            parseMessagePage3(first, post, isGetWholeText, isQuote);
                            post.typeU.add("", length, post.getText().length());
                        } else if (((Element) node).tagName().equals("font")) {
                            Element first = ((Element) node).select("font").first();
                            String str = "while";
                            String r1 = "3";
                            if (((Element) node).select("font[color]").first() != null) {
                                str = ((Element) node).select("font[color]").attr("color");
                            }
                            String attr = ((Element) node).select("font[size]").first() != null ? ((Element) node).select("font[size]").attr("size") : r1;
                            int length2 = post.getText().length();
                            parseMessagePage3(first, post, isGetWholeText, isQuote);
                            post.font.add("", length2, post.getText().length(), str, Integer.parseInt(attr));
                        } else if (((Element) node).tagName().equals("a")) {
                            Element first = ((Element) node).select("a[href]").first();
                            if (first.select("img").first() == null) {
                                String r0;
                                r0 = ((Element) node).select("a[href]").attr("href").replace("%3A", ":").replace("%2F", "/").replace("%3F", "?").replace("%3D", "=").replace("%26", "&");
                                if (r0.contains("mailto:")) {
                                    String r1 = r0.substring(7, r0.length());
                                    r0 = ((Element) node).select("a[href]").text();
                                    int length2 = post.getText().length();
                                    post.web.add(r1, length2, r0.length() + length2);
                                    post.addText(r0 , isQuote);
                                } else if (r0.contains("http")) {
                                    String r1 = r0.substring(r0.indexOf("http"), r0.length());
                                    r0 = ((Element) node).select("a[href]").text();
                                    int length2 = post.getText().length();
                                    post.web.add(r1, length2, r0.length() + length2);
                                    post.addText(r0, isQuote);
                                }
                            } else {
                                parseMessagePage3(first, post, true, isQuote);
                            }
                        } else {
                            post.addText(((Element) node).text(), isQuote);
                        }
                    } else if (node instanceof TextNode) {
                        // IMPORTANT: add plain message !!!
                        if (isGetWholeText) {
                            post.addText(((TextNode) node).getWholeText(), isQuote);
                        } else {
                            post.addText(((TextNode) node).text(), isQuote);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}