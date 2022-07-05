package com.example.forecastapp.ApiModels.WeatherApiModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Forecastday {

    @SerializedName("astro")
    private Astro astro;

    @SerializedName("hour")
    private List<Hour> hour;

    public Astro getAstro() {
        return astro;
    }

    public void setAstro(Astro astro) {
        this.astro = astro;
    }

    public List<Hour> getHour() {
        return hour;
    }

    public void setHour(List<Hour> hour) {
        this.hour = hour;
    }
}
