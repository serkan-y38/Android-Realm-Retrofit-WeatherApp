package com.example.forecastapp.RestApi.OpenWeatherMapWebSite;

import com.example.forecastapp.ApiModels.OpenWeatherMapModels.ModelWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestApi {

    @GET("forecast")
    Call<ModelWeather> getWeather(@Query("q") String city, @Query("appid") String key);

}
