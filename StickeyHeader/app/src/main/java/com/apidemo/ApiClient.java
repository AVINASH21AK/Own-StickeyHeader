package com.apidemo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static String strBaseUrl = "https://newsapi.org/v1/";


    /*-----start Retrofit-----*/

    public static OkHttpClient getClient() {
        //OkHttpClient client =

        return new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build();
        //return client;
    }

    public static Retrofit getRetrofitBuilder() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        return new Retrofit.Builder()
                .baseUrl(strBaseUrl)
                .client(getClient()) // it's optional for adding client
                .addConverterFactory(GsonConverterFactory.create(gson))
                //.addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiInterface getRetrofitApiService() {
        return getRetrofitBuilder().create(ApiInterface.class);
    }

    /*-----end Retrofit-----*/

}
