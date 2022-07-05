package com.example.forecastapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.forecastapp.Adapters.FavoriteAdapter;
import com.example.forecastapp.Adapters.HourlyAdapter;
import com.example.forecastapp.DatabaseModels.FavoriteModel;
import com.example.forecastapp.ApiModels.WeatherApiModels.WeatherModel;
import com.example.forecastapp.Objects.HourlyObject;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ListView favoriteListView;
    RecyclerView recyclerView;
    TextView CountyCityT, measureTimeT, currentlyConditionsT, currentlyFeelsT, currentlyDegreeT, currentlyWindT, currentlyHumidityT, currentlyPressureT, moonsetT, moonriseT, sunriseT, sunsetT;
    ImageView currentConditionImg, ImageView;
    Toolbar toolbar;
    RelativeLayout progressRl, networkRl;
    LinearLayout mainLl;
    List<HourlyObject> hourlyObjectList;
    LinearLayoutManager linearLayoutManager;
    ImageButton getCityNameBtn, addFavoriteButton;
    EditText getCityNameEt, addFavoriteEt;
    LocationManager locationManager;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Realm realm;
    List<FavoriteModel> dbModelList;
    BottomNavigationView bottomNavigationView;

    String cityName;
    String apiKeyForRequest = ""; //api key ********************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        defineVariable();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        realm = Realm.getDefaultInstance();

        setBottomNav();
        setDrawerNav();
        checkLocationPermission();
        networkEnabled();
        locationEnabled();
        getCityName();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.tool_bar_items, menu);
        MenuItem searchIcon = menu.findItem(R.id.searchCity);

        searchIcon.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.search_alert_dialog, null);

                getCityNameBtn = v.findViewById(R.id.seachButtonAlert);
                getCityNameEt = v.findViewById(R.id.getCityNameEt);

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setView(v);
                alert.setCancelable(true);
                AlertDialog dialog = alert.create();

                getCityNameBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String city = getCityNameEt.getText().toString();
                        cityName = city;
                        request1(apiKeyForRequest, cityName);
                        dialog.cancel();
                    }
                });
                dialog.show();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Allowed ", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void defineVariable() {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        ImageView = findViewById(R.id.imageViewMainAct);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), linearLayoutManager.HORIZONTAL, false);
        toolbar = findViewById(R.id.toolbarMainAct);
        mainLl = findViewById(R.id.mainLl);
        progressRl = findViewById(R.id.progressLayout);
        recyclerView = findViewById(R.id.rcyclView);
        CountyCityT = findViewById(R.id.cityCountryText);
        measureTimeT = findViewById(R.id.measuredTimeText);
        currentlyConditionsT = findViewById(R.id.currentConditionText);
        currentlyFeelsT = findViewById(R.id.currentConditionFeelsLikeText);
        currentlyDegreeT = findViewById(R.id.currentWeatherConditionDegreeText);
        currentlyWindT = findViewById(R.id.currentWindText);
        currentlyHumidityT = findViewById(R.id.currentHumidityText);
        currentlyPressureT = findViewById(R.id.currentPressureText);
        currentConditionImg = findViewById(R.id.currentWeatherConditionImage);
        moonriseT = findViewById(R.id.moonriseT);
        moonsetT = findViewById(R.id.moonsetT);
        sunriseT = findViewById(R.id.sunriseT);
        sunsetT = findViewById(R.id.sunsetT);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        networkRl = findViewById(R.id.checkConnectionRl);
    }

    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }
    }

    public void getCityName() {
        //get city name from favorite favorite adapter
        Intent intent = getIntent();
        String getName = intent.getStringExtra("cityname");
        request1(apiKeyForRequest, getName);
    }

    //set bottom nav -------------------------------------------------------------------------------
    public void setBottomNav() {

        bottomNavigationView.setSelectedItemId(R.id.homeBottomNav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.favoriteBottomNav):
                        LayoutInflater inflater = getLayoutInflater();
                        View v = inflater.inflate(R.layout.favorite_alert_dialog, null);

                        favoriteListView = v.findViewById(R.id.favoriteListView);
                        addFavoriteButton = v.findViewById(R.id.addFavoriteAlertButton);
                        addFavoriteEt = v.findViewById(R.id.addFavoriteEt);

                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setView(v);
                        alert.setCancelable(true);
                        AlertDialog dialog = alert.create();

                        RealmResults<FavoriteModel> results = realm.where(FavoriteModel.class).findAll();
                        dbModelList = new ArrayList<>();

                        for (FavoriteModel m : results) {
                            dbModelList.add(m);
                        }

                        if (results.size() > 0) {
                            FavoriteAdapter adapter = new FavoriteAdapter(dbModelList, getApplicationContext());
                            favoriteListView.setAdapter(adapter);
                        }

                        addFavoriteButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                RealmResults<FavoriteModel> results = realm.where(FavoriteModel.class).findAll();
                                final String city = addFavoriteEt.getText().toString();
                                final String date = new SimpleDateFormat("ss.mm.HH.dd.MM.yyyy").format(new java.util.Date());

                                if (city.isEmpty()) {
                                    Toast.makeText(getApplicationContext(), "Please enter city name", Toast.LENGTH_SHORT).show();
                                } else {
                                    realm.executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            FavoriteModel model = realm.createObject(FavoriteModel.class);
                                            model.setCity(city);
                                            model.setDate(date);
                                            dialog.cancel();
                                        }
                                    }, new Realm.Transaction.OnSuccess() {
                                        @Override
                                        public void onSuccess() {
                                            Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
                                            RealmResults<FavoriteModel> models = realm.where(FavoriteModel.class).findAll();
                                            for (FavoriteModel m : models) {
                                                Log.i("link*models***********", m.toString());
                                            }
                                        }
                                    }, new Realm.Transaction.OnError() {
                                        @Override
                                        public void onError(Throwable error) {
                                            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                        dialog.show();
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.dailyForecastBottomNav:
                        String city = CountyCityT.getText().toString();
                        Intent intent = new Intent(getApplicationContext(), DailyForecastAct.class);
                        intent.putExtra("city", city);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.homeBottomNav:
                        return true;
                }
                return false;
            }
        });
    }
    //set bottom nav -------------------------------------------------------------------------------


    // set drawer menu------------------------------------------------------------------------------
    public void setDrawerNav() {

        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.menu_open, R.string.close_menu);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                // close drawer when item is tapped
                switch (item.getItemId()) {
                    case R.id.shareDrawer: {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, CountyCityT.getText().toString() + " current weather is " + currentlyConditionsT.getText().toString() + " and " + currentlyDegreeT.getText().toString() + "C°");
                        startActivity(Intent.createChooser(intent, "Share Now"));
                        break;
                    }
                    case R.id.HelpDrawer: {
                        Toast.makeText(getApplicationContext(), "Please Describe Your Issue", Toast.LENGTH_LONG).show();

                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "example@example.com"));
                        String subject = "Please describe your issue here";
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                        startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
                        break;
                    }
                    case R.id.contactUsDrawer: {
                        Toast.makeText(getApplicationContext(), "Send an email to us", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.favoriteDrawer:
                        LayoutInflater inflater = getLayoutInflater();
                        View v = inflater.inflate(R.layout.favorite_alert_dialog, null);

                        favoriteListView = v.findViewById(R.id.favoriteListView);
                        addFavoriteButton = v.findViewById(R.id.addFavoriteAlertButton);
                        addFavoriteEt = v.findViewById(R.id.addFavoriteEt);

                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setView(v);
                        alert.setCancelable(true);
                        AlertDialog dialog = alert.create();
                        //dialog.cancel();

                        RealmResults<FavoriteModel> results = realm.where(FavoriteModel.class).findAll();
                        dbModelList = new ArrayList<>();

                        for (FavoriteModel m : results) {
                            dbModelList.add(m);
                        }

                        if (results.size() > 0) {
                            FavoriteAdapter adapter = new FavoriteAdapter(dbModelList, getApplicationContext());
                            favoriteListView.setAdapter(adapter);
                        }

                        addFavoriteButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                RealmResults<FavoriteModel> results = realm.where(FavoriteModel.class).findAll();
                                final String city = addFavoriteEt.getText().toString();
                                final String date = new SimpleDateFormat("ss.mm.HH.dd.MM.yyyy").format(new java.util.Date());

                                if (city.isEmpty()) {
                                    Toast.makeText(getApplicationContext(), "Please enter city name", Toast.LENGTH_SHORT).show();
                                } else {
                                    realm.executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            FavoriteModel model = realm.createObject(FavoriteModel.class);
                                            model.setCity(city);
                                            model.setDate(date);
                                            dialog.cancel();
                                        }
                                    }, new Realm.Transaction.OnSuccess() {
                                        @Override
                                        public void onSuccess() {
                                            Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
                                            RealmResults<FavoriteModel> models = realm.where(FavoriteModel.class).findAll();
                                            for (FavoriteModel m : models) {
                                                Log.i("link*models***********", m.toString());
                                            }
                                        }
                                    }, new Realm.Transaction.OnError() {
                                        @Override
                                        public void onError(Throwable error) {
                                            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                        dialog.show();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    // set drawer menu------------------------------------------------------------------------------


    // check network connection---------------------------------------------------------------------
    public void networkEnabled() {
        if (!isConnected()) {
            progressRl.setVisibility(View.GONE);
            networkRl.setVisibility(View.VISIBLE);
        }
    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }
    // check network connection---------------------------------------------------------------------

    // check location services----------------------------------------------------------------------
    public void locationEnabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (gps_enabled && network_enabled) {
            getLocation();
        } else {
            cityName = "istanbul";
            request1(apiKeyForRequest, cityName);
        }
    }
    // check location services----------------------------------------------------------------------

    //get location ---------------------------------------------------------------------------------
    public void getLocation() {

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }


        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        cityName = getCityName(location.getLongitude(), location.getLatitude());
        request1(apiKeyForRequest, cityName);
    }

    public String getCityName(double lon, double lat) {

        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> address = geocoder.getFromLocation(lat, lon, 10);

            for (Address adr : address) {
                if (adr != null) {
                    String city = adr.getLocality();
                    if (city != null && !city.equals("")) {
                        cityName = city;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "City was not fount", Toast.LENGTH_SHORT).show();
                    cityName = "istanbul";
                    request1(apiKeyForRequest, cityName);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cityName;
    }
    //get location ---------------------------------------------------------------------------------


    // request-response functions-------------------------------------------------------------------

    // from weatherapi.com
    public void request1(String key, String cityName) {
        hourlyObjectList = new ArrayList<>();

        Call<WeatherModel> call = com.example.forecastapp.RestApi.WeatherApiWebSite.ManagerAll.getInstance().getWeather(key, cityName);
        call.enqueue(new Callback<WeatherModel>() {

            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                if (response.isSuccessful()) {
                    CountyCityT.setText(response.body().getLocation().getCountry() + ", " + response.body().getLocation().getName());
                    currentlyFeelsT.setText(response.body().getCurrent().getFeelslike_c() + "C°");
                    currentlyWindT.setText("Wind " + response.body().getCurrent().getWind_kph() + "kp/h");
                    currentlyHumidityT.setText("Humidity % " + response.body().getCurrent().getHumidity());
                    currentlyPressureT.setText("Pressure " + response.body().getCurrent().getPressure_mb() + " hPa");
                    currentlyDegreeT.setText(response.body().getCurrent().getTemp_c());
                    currentlyConditionsT.setText(response.body().getCurrent().getCondition().getText());
                    measureTimeT.setText("Measured at: " + response.body().getCurrent().getLast_updated());
                    moonriseT.setText(response.body().getForecast().getForecastday().get(0).getAstro().getMoonrise());
                    sunriseT.setText(response.body().getForecast().getForecastday().get(0).getAstro().getSunrise());
                    sunsetT.setText(response.body().getForecast().getForecastday().get(0).getAstro().getSunset());

                    if (response.body().getForecast().getForecastday().get(0).getAstro().getMoonset().equals("No moonset")) {
                        moonsetT.setText("--:-- --");
                    } else {
                        moonsetT.setText(response.body().getForecast().getForecastday().get(0).getAstro().getMoonset());
                    }

                    Picasso.with(getApplicationContext()).load("https:" + response.body().getCurrent().getCondition().getIcon()).into(currentConditionImg);
                    setImageView(response.body().getCurrent().getCondition().getText());

                    for (int a = 0; a < 24; a++) {
                        String degree = response.body().getForecast().getForecastday().get(0).getHour().get(a).getTemp_c();
                        String time = response.body().getForecast().getForecastday().get(0).getHour().get(a).getTime();
                        String icon = response.body().getForecast().getForecastday().get(0).getHour().get(a).getHourlyCondition().getIcon();

                        HourlyObject hourlyItem = new HourlyObject(icon, degree, time);
                        hourlyObjectList.add(hourlyItem);
                    }

                    recyclerView.setLayoutManager(linearLayoutManager);
                    HourlyAdapter hourlyAdapter = new HourlyAdapter(hourlyObjectList, getApplicationContext());
                    recyclerView.setAdapter(hourlyAdapter);
                }


                mainLl.setVisibility(View.VISIBLE);
                progressRl.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {

            }
        });
    }

    public void setImageView(String weatherCondition) {

        if (weatherCondition.equals("Sunny")) {
            ImageView.setImageResource(R.drawable.sunny);
        } else if (weatherCondition.equals("Cloudy") || weatherCondition.equals("Overcast")) {
            ImageView.setImageResource(R.drawable.cloudy);
        } else if (weatherCondition.equals("Partly cloudy")) {
            ImageView.setImageResource(R.drawable.partylycloudy);
        } else if (weatherCondition.equals("Clear")) {
            ImageView.setImageResource(R.drawable.clearnight);
        } else if (weatherCondition.equals("Mist")) {
            ImageView.setImageResource(R.drawable.mist);
        } else if (weatherCondition.equals("Blizzard") || weatherCondition.equals("Patchy sleet possible") || weatherCondition.equals("Patchy snow possible") || weatherCondition.equals("Blowing snow")
                || weatherCondition.equals("Light sleet") || weatherCondition.equals("Moderate or heavy sleet") || weatherCondition.equals("Light snow") || weatherCondition.equals("Patchy moderate snow")
                || weatherCondition.equals("Moderate snow") || weatherCondition.equals("Patchy heavy snow") || weatherCondition.equals("Heavy snow") || weatherCondition.equals("Ice pallets")
                || weatherCondition.equals("Light snow showers") || weatherCondition.equals("Moderate or heavy snow showers") || weatherCondition.equals("Light showers of ice pallets")
                || weatherCondition.equals("Moderate or heavy showers of ice pallets")) {
            ImageView.setImageResource(R.drawable.snowy);
        } else if (weatherCondition.equals("Patchy rain possible") || weatherCondition.equals("Patchy freezing drizzle possible") || weatherCondition.equals("Patchy light drizzle possible")
                || weatherCondition.equals("Light drizzle") || weatherCondition.equals("Freezing drizzle") || weatherCondition.equals("Heavy freezing drizzle") || weatherCondition.equals("Patchy light rain")
                || weatherCondition.equals("Light rain") || weatherCondition.equals("Moderate rain at times") || weatherCondition.equals("Moderate rain") || weatherCondition.equals("Heavy rain at times")
                || weatherCondition.equals("Heavy rain") || weatherCondition.equals("Light freezing rain") || weatherCondition.equals("Moderate or heavy freezing rain") || weatherCondition.equals("Light rain shower")
                || weatherCondition.equals("Moderate or heavy rain shower") || weatherCondition.equals("Torrential rain shower") || weatherCondition.equals("Light sleet showers") || weatherCondition.equals("Moderate or heavy sleet showers")) {
            ImageView.setImageResource(R.drawable.rainy1);
        } else if (weatherCondition.equals("Fog") || weatherCondition.equals("Freezing fog")) {
            ImageView.setImageResource(R.drawable.fog);
        } else if (weatherCondition.equals("Patch light rain with thunder") || weatherCondition.equals("Moderate or heavy rain with thunder") || weatherCondition.equals("Patch light snow with thunder")
                || weatherCondition.equals("Moderate or heavy snow with thunder") || weatherCondition.equals("Thundery outbreaks possible")) {
            ImageView.setImageResource(R.drawable.thundery);
        } else {
            ImageView.setImageResource(R.drawable.cloudy);
        }
    }

    /**
     //another way to built retrofit

     public void request3(){
     Retrofit retrofit=new Retrofit.Builder().
     baseUrl("https://api.openweathermap.org/data/2.5/")
     .addConverterFactory(GsonConverterFactory.create())
     .build();
     RestApi restApi =retrofit.create(RestApi.class);
     Call<ModelWeather> called= restApi.getWeather(city,apiKeyForRequest2);
     called.enqueue(new Callback<ModelWeather>() {
    @Override public void onResponse(Call<ModelWeather> call, Response<ModelWeather> response) {
    Log.i("********************",response.body().toString());
    }

    @Override public void onFailure(Call<ModelWeather> call, Throwable t) {

    }
    });
     }
     **/

    // request-response functions-------------------------------------------------------------------

}