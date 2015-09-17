package com.nna88.voz.mysqlite;

public class Comment {
    public String LastPost;
    public String View;
    public String forum;
    public String iPage;
    public long id;
    public String idThread;
    public String title;
    public String url;
    public String urlFirstNews;
    public String urlLastPost;

    public Comment(){}

    public Comment(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9) {
        this.idThread = str;
        this.url = str3;
        this.title = str2;
        this.forum = str4;
        this.iPage = str5;
        this.View = str6;
        this.LastPost = str7;
        this.urlFirstNews = str8;
        this.urlLastPost = str9;
    }

    public String toString() {
        return this.id + ";" + this.url + ";" + this.forum + ";" + this.title + ";" + this.iPage;
    }
}
