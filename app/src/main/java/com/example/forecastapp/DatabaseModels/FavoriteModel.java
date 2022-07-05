package com.example.forecastapp.DatabaseModels;

import io.realm.RealmObject;

public class FavoriteModel extends RealmObject {

    private String date;
    private String city;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "FavoriteModel{" +
                "date='" + date + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
