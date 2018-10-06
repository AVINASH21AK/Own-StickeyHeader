package com.apidemo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    //https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey=03ac4499ca9b42e9a5ecea60cf2839e3

    @GET("articles?")
    Call<DataList> getList(@Query("source") String source, @Query("sortBy") String sortBy, @Query("apiKey") String apiKey);

}