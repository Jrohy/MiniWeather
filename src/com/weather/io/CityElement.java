package com.weather.io;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.JSONArray;

@SuppressWarnings("serial")
public class CityElement implements Serializable {
	private String cityName;
	private Date weaUpdateTime;
	private Date aqiUpdateTime;
	private JSONArray weatherJson = new JSONArray();
	private JSONArray aqiJson = new JSONArray();
	
	public CityElement(String cityName) {
		this.cityName = cityName;
	}

	public JSONArray getWeatherJson() {
		return weatherJson;
	}
	public void setWeatherJson(JSONArray weatherJson) {
		this.weatherJson = weatherJson;
	}
	public JSONArray getAqiJson() {
		return aqiJson;
	}
	public void setAqiJson(JSONArray aqiJson) {
		this.aqiJson = aqiJson;
	}
	public Date getWeaUpdateTime() {
		return weaUpdateTime;
	}
	public void setWeaUpdateTime(Date weaUpdateTime) {
		this.weaUpdateTime = weaUpdateTime;
	}
	public Date getAqiUpdateTime() {
		return aqiUpdateTime;
	}
	public void setAqiUpdateTime(Date aqiUpdateTime) {
		this.aqiUpdateTime = aqiUpdateTime;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
}
