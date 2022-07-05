package com.example.forecastapp.ApiModels.WeatherApiModels;

import com.google.gson.annotations.SerializedName;

public class Current {

    @SerializedName("last_updated")
    private String last_updated;

    @SerializedName("temp_c")
    private String temp_c;

    @SerializedName("wind_kph")
    private String wind_kph;

    @SerializedName("pressure_mb")
    private String pressure_mb;

    @SerializedName("feelslike_c")
    private String feelslike_c;

    @SerializedName("humidity")
    private String humidity;

    @SerializedName("condition")
    private CurrentCondition condition;

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }

    public String getTemp_c() {
        return temp_c;
    }

    public void setTemp_c(String temp_c) {
        this.temp_c = temp_c;
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

    public String getFeelslike_c() {
        return feelslike_c;
    }

    public void setFeelslike_c(String feelslike_c) {
        this.feelslike_c = feelslike_c;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public CurrentCondition getCondition() {
        return condition;
    }

    public void setCondition(CurrentCondition condition) {
        this.condition = condition;
    }
}
