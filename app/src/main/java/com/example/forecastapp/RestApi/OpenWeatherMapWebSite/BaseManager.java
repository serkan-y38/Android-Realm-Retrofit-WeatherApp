package com.example.forecastapp.RestApi.OpenWeatherMapWebSite;

public class BaseManager {

    protected RestApi getRestApiClient() {
        RestApiClient restApiClient = new RestApiClient(BaseUrl.baseurl);
        return restApiClient.getRestApi();
    }


}
