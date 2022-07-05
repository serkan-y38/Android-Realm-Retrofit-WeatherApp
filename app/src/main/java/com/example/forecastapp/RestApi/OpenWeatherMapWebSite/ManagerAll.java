package com.example.forecastapp.RestApi.OpenWeatherMapWebSite;

import com.example.forecastapp.ApiModels.OpenWeatherMapModels.ModelWeather;

import retrofit2.Call;

public class ManagerAll extends BaseManager {

    private static ManagerAll ourInstance = new ManagerAll();

    public static synchronized ManagerAll getInstance() {
        return ourInstance;
    }

    public Call<ModelWeather> getweather(String city, String key) {
        Call<ModelWeather> call = getRestApiClient().getWeather(city, key);
        return call;
    }

}
