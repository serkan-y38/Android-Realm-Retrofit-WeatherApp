package com.example.forecastapp.ApiModels.OpenWeatherMapModels;

import java.util.List;

public class ModelWeather{
	private City city;
	private int cnt;
	private String cod;
	private int message;
	private List<ListItem> list;

	public City getCity(){
		return city;
	}

	public int getCnt(){
		return cnt;
	}

	public String getCod(){
		return cod;
	}

	public int getMessage(){
		return message;
	}

	public List<ListItem> getList(){
		return list;
	}
}