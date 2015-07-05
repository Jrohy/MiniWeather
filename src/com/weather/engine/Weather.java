package com.weather.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.weather.string.BasicString;

public class Weather {	
	    
    public static JSONArray getAQIJson(String city) throws UnknownHostException {
    	String datas = null;
		try {
			URL url = new URL("http://www.pm25.in/api/querys/pm2_5.json?city=" + URLEncoder.encode(city, "UTF-8") + "&token=5j1znBVAsnSf5xQyNQyq");
	        URLConnection connection = url.openConnection();
	        connection.setConnectTimeout(1000); 
            BufferedReader br = new BufferedReader(new InputStreamReader( 
				connection.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder(); 
            String line = null; 
            while ((line = br.readLine()) != null) 
                sb.append(line); 
            datas = sb.toString();
		} catch (IOException  e) {
			// TODO Auto-generated catch block
			if (e.getMessage().equals(BasicString.AQIURL))
				throw new UnknownHostException(BasicString.OFFLINE);
			else
				e.printStackTrace();
		}
		
		try {
			return JSONArray.parseArray(datas);   	
		} catch (JSONException e1) {
			if (JSONObject.parseObject(datas).get("error").equals(BasicString.AQINULL)) {
				throw new JSONException(BasicString.AQINULL);
			}
			else if (JSONObject.parseObject(datas).get("error").equals(BasicString.NOAQI)) {
				throw new JSONException(BasicString.NOAQI);
			}
		}
		return null;
    }
    
    public static JSONArray getWeatherJson(String city) throws UnknownHostException {
    	String datas = null;
		try {
			URL url = new URL("http://api.k780.com:88/?app=weather.future&weaid=" + URLEncoder.encode(city, "UTF-8") + 
					"&appkey=13539&sign=5ee05a50cc99777c8dffbea4c80d6438&format=json");
	        URLConnection connection = url.openConnection();
	        connection.setConnectTimeout(1000); 
            BufferedReader br = new BufferedReader(new InputStreamReader( 
                    connection.getInputStream(), "UTF-8")); 
            StringBuilder sb = new StringBuilder(); 
            String line = null; 
            while ((line = br.readLine()) != null) 
                sb.append(line); 
            datas = sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if (e.getMessage().equals(BasicString.WEAURL))
				throw new UnknownHostException(BasicString.OFFLINE);
			else
				e.printStackTrace();
		}

		if (JSONObject.parseObject(datas).containsKey("msgid")) {
			throw new JSONException(BasicString.NOWEA);
		}
		return JSONObject.parseObject(datas).getJSONArray("result");   	
    }
}
