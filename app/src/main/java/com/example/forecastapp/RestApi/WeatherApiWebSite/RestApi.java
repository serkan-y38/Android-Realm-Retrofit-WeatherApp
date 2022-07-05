package com.example.forecastapp.RestApi.WeatherApiWebSite;

import com.example.forecastapp.ApiModels.WeatherApiModels.WeatherModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestApi {

    @GET("forecast.json")
    Call<WeatherModel> getWeather(@Query("key") String apiKey,
                                  @Query("q") String city);


}
