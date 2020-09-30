package com.dpk.hammoq.utilsNetwork;

import java.util.concurrent.TimeUnit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.OkHttpClient.*;

public class RetrofitClient {

    private static final String BASE_URL = "https://devcust.avoidpoints.com/api/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        Builder client = new Builder();
        client.connectTimeout(30, TimeUnit.SECONDS);
        client.readTimeout(30, TimeUnit.SECONDS);
        client.writeTimeout(30, TimeUnit.SECONDS);

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client.build())
                    .build();
        }
        return retrofit;
    }
}