package com.example.forecastapp;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().allowWritesOnUiThread(true).name("Myrealm.realm").build();
        Realm.setDefaultConfiguration(realmConfiguration);

        super.onCreate();
    }

}
