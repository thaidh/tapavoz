package com.whoami.voz.retrofit;

import com.whoami.voz.mysqlite.DatabaseHelper;

import retrofit2.Retrofit;

public class VozService {

    private volatile static VozApi instance;

    public static VozApi getInstance() {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    Retrofit.Builder builder = new Retrofit.Builder()
                            .addConverterFactory(new VozConverterFactory())
                            .baseUrl("https://vozforums.com");
                    instance = builder.build().create(VozApi.class);
                }
            }
        }
        return instance;
    }
}
