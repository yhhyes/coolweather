package com.coolweather.app.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

public class Utility {
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
}
