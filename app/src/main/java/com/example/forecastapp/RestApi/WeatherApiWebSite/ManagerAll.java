package com.example.forecastapp.RestApi.WeatherApiWebSite;

import com.example.forecastapp.ApiModels.WeatherApiModels.WeatherModel;

import retrofit2.Call;

public class ManagerAll extends BaseManager {

    private static ManagerAll ourInstance = new ManagerAll();

    public static synchronized ManagerAll getInstance() {
        return ourInstance;
    }

    public Call<WeatherModel> getWeather(String apiKey, String city) {
        Call<WeatherModel> call = getRestApiClient().getWeather(apiKey, city);
        return call;
    }

}
