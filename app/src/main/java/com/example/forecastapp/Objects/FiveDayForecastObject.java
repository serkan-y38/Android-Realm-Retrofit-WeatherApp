package com.example.forecastapp.Objects;

public class FiveDayForecastObject {

    private String date;
    private String temp;
    private String icon;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public FiveDayForecastObject(String date, String temp, String icon) {
        this.date = date;
        this.temp = temp;
        this.icon = icon;
    }
}
