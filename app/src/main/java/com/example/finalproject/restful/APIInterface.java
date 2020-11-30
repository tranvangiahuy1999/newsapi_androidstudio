package com.example.finalproject.restful;
import com.example.finalproject.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {
    @GET("top-headlines")
    Call<ResponseModel> getLatestNews(@Query("country") String country, @Query("apiKey") String apiKey, @Query("page") String page);

    @GET("everything")
    Call<ResponseModel> getSearchNews(@Query("q") String query, @Query("apiKey") String apiKey, @Query("page") String page);
}
