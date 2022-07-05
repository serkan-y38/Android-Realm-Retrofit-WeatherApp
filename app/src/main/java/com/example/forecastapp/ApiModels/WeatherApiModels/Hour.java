package com.example.forecastapp.ApiModels.WeatherApiModels;

import com.google.gson.annotations.SerializedName;

public class Hour {

    @SerializedName("time")
    private String time;

    @SerializedName("temp_c")
    private String temp_c;

    @SerializedName("condition")
    private HourlyCondition hourlyCondition;

    @SerializedName("wind_kph")
    private  String wind_kph;

    @SerializedName("pressure_mb")
    private String pressure_mb;

    @SerializedName("humidity")
    private String humidity;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemp_c() {
        return temp_c;
    }

    public void setTemp_c(String temp_c) {
        this.temp_c = temp_c;
    }

    public HourlyCondition getHourlyCondition() {
        return hourlyCondition;
    }

    public void setHourlyCondition(HourlyCondition hourlyCondition) {
        this.hourlyCondition = hourlyCondition;
    }

    public String getWind_kph() {
        return wind_kph;
    }

    public void setWind_kph(String wind_kph) {
        this.wind_kph = wind_kph;
    }

    public String getPressure_mb() {
        return pressure_mb;
    }

    public void setPressure_mb(String pressure_mb) {
        this.pressure_mb = pressure_mb;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
}
