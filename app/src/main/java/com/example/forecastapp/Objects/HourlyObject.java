package com.example.forecastapp.Objects;

public class HourlyObject {

    private String icon;
    private String degree;
    private String time;

    public HourlyObject(String icon, String degree, String time) {
        this.icon = icon;
        this.degree = degree;
        this.time = time;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
