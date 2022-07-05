package com.example.forecastapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.example.forecastapp.Adapters.DailyAdapter;
import com.example.forecastapp.ApiModels.OpenWeatherMapModels.ModelWeather;
import com.example.forecastapp.Objects.FiveDayForecastObject;
import com.example.forecastapp.RestApi.OpenWeatherMapWebSite.ManagerAll;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DailyForecastAct extends AppCompatActivity {

    List<FiveDayForecastObject> DayForecastObjectList;
    ListView listView;
    RelativeLayout mainRl;
    LinearLayout progressL;
    Toolbar toolbarDaily;

    String apiKeyForRequest = ""; //api key ********************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);

        listView = findViewById(R.id.listViewDaily);
        progressL = findViewById(R.id.dailyProgressBarLl);
        mainRl = findViewById(R.id.dailyRl);
        toolbarDaily = findViewById(R.id.toolbarDaily);

        Intent intent = getIntent();
        String cityName = intent.getStringExtra("city");

        request(apiKeyForRequest, cityName);

        setSupportActionBar(toolbarDaily);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void request(String key, String city) {
        DayForecastObjectList = new ArrayList<>();

        Call<ModelWeather> call = ManagerAll.getInstance().getweather(city, key);
        call.enqueue(new Callback<ModelWeather>() {
            @Override
            public void onResponse(Call<ModelWeather> call, Response<ModelWeather> response) {

                if (response.isSuccessful()) {
                    for (int a = 0; a < 40; a++) {
                        double degreeK = response.body().getList().get(a).getMain().getTemp();
                        int degreeC = (int) (degreeK - 273.15);

                        String temp = String.valueOf(degreeC);
                        String icon = String.valueOf(response.body().getList().get(a).getWeather().get(0).getIcon());
                        String Date = String.valueOf(response.body().getList().get(a).getDtTxt());

                        FiveDayForecastObject item = new FiveDayForecastObject(Date, temp, icon);
                        DayForecastObjectList.add(item);

                        mainRl.setVisibility(View.VISIBLE);
                        progressL.setVisibility(View.GONE);
                    }

                }
                DailyAdapter dailyAdapter = new DailyAdapter(DayForecastObjectList, getApplicationContext());
                listView.setAdapter(dailyAdapter);


            }

            @Override
            public void onFailure(Call<ModelWeather> call, Throwable t) {

            }
        });
    }
}