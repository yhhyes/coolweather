package com.coolweather.app.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;
import com.coolweather.app.model.Weekday;

public class Utility {
	private static final String GetWeatherSignAndTypeList = null;
	/*
	 * 解析和服务器返回的省级数据
	 */
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,String response){
		if(!TextUtils.isEmpty(response)){
			Province province = new Province();
				try {
					JSONObject jsonObject2 = new JSONObject(response);
					JSONArray cityArray = jsonObject2.getJSONArray("result");
					ArrayList<String> ppname = new ArrayList<String> ();
					for(int i =0; i<cityArray.length();i++){
						JSONObject jsonObject = (JSONObject)cityArray.opt(i); 
						String pname = jsonObject.getString("province");
						Log.d("值",pname.toString());
					if(ppname.indexOf(pname)==-1){
							province.setProvinceName(pname);
							
							coolWeatherDB.saveProvince(province);
							ppname.add(pname);
						}
					
						
					}
					}catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d("错误",e.toString());
				}
			
					
					return true;
				}
	

	
		return false;
	}
	/*
	 * 解析和处理服务器返回的市级数据
	 */
	public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,String response,String provincename ,int provinceId){
		if(! TextUtils.isEmpty(response)){
			City city = new City();
			try {
				JSONObject jsonObject2 = new JSONObject(response);
				JSONArray cityArray = jsonObject2.getJSONArray("result");
		
				ArrayList<String> ccname = new ArrayList<String>();
				for(int i =0; i<cityArray.length();i++){
					
					JSONObject jsonObject = (JSONObject)cityArray.opt(i); 
					String pname = jsonObject.getString("province");
					if(pname.equals(provincename)){
						String cname = jsonObject.getString("city");
						if(ccname.indexOf(cname)==-1){
							city.setCityName(cname);
						    city.setProvinceId(provinceId);
							coolWeatherDB.saveCity(city);
							 ccname.add(cname);
						}
						
						
					}
					
			
					
					
				}
				}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				return true;
			}
	
		return false;
		
	}
	/*
	 * 解析和处理服务器返回的县级数据
	 * */
	public static boolean handleCountiesResponse(CoolWeatherDB coolweatherDB,String response,String cityname, int cityId){
		if(!TextUtils.isEmpty(response)){
			County county = new County();
			try {
				Log.d("dew",response.toString());
				JSONObject jsonObject2 = new JSONObject(response);
				JSONArray cityArray = jsonObject2.getJSONArray("result");
		
				ArrayList<String> ccooname = new ArrayList<String>();
				for(int i =0; i<cityArray.length();i++){
					
					JSONObject jsonObject = (JSONObject)cityArray.opt(i); 
					String ccname = jsonObject.getString("city");
					if(ccname.equals(cityname)){
						String cconame = jsonObject.getString("district");
						if(ccname.indexOf(cconame)==-1){
							county.setCountyName(cconame);
						    county.setCityId(cityId);
							coolweatherDB.saveCounty(county);
							 ccooname.add(cconame);
						}
						
						
					}
					
			
					
					
				}
				}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
			}

		return false;	
	}
	/*
	 * 解析返回的天气图标JSON数据
	 * */
	
	 
	 /*
	 * 解析服务器返回的JSON数据，并将解析出的数据存储到本地。
	 * */
	public static void handleWeatherResponse(Context context,String response){
		try{
			Log.d("response",response.toString());
			JSONObject jsonObject = new JSONObject(response);
			JSONObject result= jsonObject.getJSONObject("result");
			
				
				JSONObject sk = result.getJSONObject("sk");
				String time = sk.getString("time");
				String temp = sk.getString("temp");   //温度
				String wind_direction = sk.getString("wind_direction");    //风向
				//获得今日天气
				JSONObject today = result.getJSONObject("today");
				String city = today.getString("city");
				String date_y = today.getString("date_y");
				String week = today.getString("week");
				String temperature = today.getString("temperature");
				String weather = today.getString("weather");
				String wind = today.getString("wind");
				JSONObject weather_idObject = today.getJSONObject("weather_id");
				String fa = weather_idObject.getString("fa");
				String fb= weather_idObject.getString("fb");
				
				//获得未来几天天气
				ArrayList<Weekday> afuture = new ArrayList<Weekday>();
				Weekday[] day = new Weekday[6];
				
				JSONObject future = result.getJSONObject("future");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd",Locale.CHINA);
				  String date = sdf.format(new Date());
				  int Date=Integer.parseInt(date);
				  
				  for(int i = 1; i<7;i++){
					int DDate =Date+ i;
					
					JSONObject dayObject = future.getJSONObject("day_"+DDate);
					String week1 = dayObject.getString("week");
					String temperature1 = dayObject.getString("temperature");
					String weather1 = dayObject.getString("weather");
					String wind1 = dayObject.getString("wind");
					String date1 = dayObject.getString("date");
					Weekday test = new Weekday();
					test.setWeek1(week1);
					day[i-1] = test;
					day[i-1].setWeek1(week1);
					day[i-1].setTemperature1(temperature1);
					day[i-1].setWeather1(weather1);
					day[i-1].setWind1(wind1);
					day[i-1].setDate1(date1);
					afuture.add(day[i-1]);
				 }
				saveWeatherInfo(context,time,temp,wind_direction,city,date_y,week,temperature,weather,wind,afuture,fa,fb);
		
			
		}catch(JSONException e){
			e.printStackTrace();
			Log.d("",e.toString());
		}
	}
	/*
	 * 将服务器返回的天气信息存储到sharedpreferences文件中。
	 * */
	private static void saveWeatherInfo(Context context, String time,
			String temp, String wind_direction, String city, String date_y,
			String week, String temperature, String weather, String wind,
			ArrayList<Weekday> afuture, String fa, String fb) {
		// TODO Auto-generated method stub
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
			SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
			editor.putBoolean("city_selected", true);
			editor.putString("city_name",city);
			editor.putString("temp",temp);
			editor.putString("wind_direction",wind_direction);
			editor.putString("time",time);
			editor.putString("date_y",date_y);
			editor.putString("week",week);
			editor.putString("temperature", temperature);
			editor.putString("weather",weather);
			editor.putString("wind",wind);
			editor.putString("fa",fa);
			editor.putString("fb",fb);
			editor.putString("current_date", sdf1.format(new Date()));
			for(int i =0;i<6;i++){
				
			editor.putString("week"+i,afuture.get(i).getWeek1());
			editor.putString("temperature"+i,afuture.get(i).getTemperature1());
			editor.putString("weather"+i,afuture.get(i).getWeather1());
			editor.putString("wind"+i, afuture.get(i).getWind1());
			editor.putString("date"+i,afuture.get(i).getDate1());
			
			editor.commit();
			
			}
			
	}
}
