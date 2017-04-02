package com.whoami.voz.retrofit;

import org.jsoup.nodes.Document;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface VozApi {

    /**
     * See https://developer.github.com/v3/repos/#list-contributors
     */
//    @GET("/repos/{owner}/{repo}/contributors")
//    Observable<List<Contributor>> contributors(@Path("owner") String owner, @Path("repo") String repo);

    @GET("/repos/{owner}/{repo}/contributors")
    List<Contributor> getContributors(@Path("owner") String owner, @Path("repo") String repo);

    /**
     * See https://developer.github.com/v3/users/
     */
//    @GET("/users/{user}")
//    Observable<User> user(@Path("user") String user);

    /**
     * See https://developer.github.com/v3/users/
     */
    @GET("/users/{user}")
    User getUser(@Path("user") String user);

    @GET
    Call<Document>  getHomePage(@Url String url);
}