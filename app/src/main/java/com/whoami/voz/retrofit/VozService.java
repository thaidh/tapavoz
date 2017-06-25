package com.whoami.voz.retrofit;

import retrofit2.Retrofit;

import static java.lang.String.format;

public class VozService {

    private VozService() {
    }

    public static VozApi createVozService() {
        Retrofit.Builder builder = new Retrofit.Builder()
//                .addCallAdapterFactory(new ToStringConverterFactory())
              .addConverterFactory(new ToStringConverterFactory())
              .baseUrl("https://vozforums.com");

//        if (!TextUtils.isEmpty(githubToken)) {
//
//            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
//                Request request = chain.request();
//                Request newReq = request.newBuilder()
//                      .addHeader("Authorization", format("token %s", githubToken))
//                      .build();
//                return chain.proceed(newReq);
//            }).build();
//
//            builder.client(client);
//        }

        return builder.build().create(VozApi.class);
    }
}
