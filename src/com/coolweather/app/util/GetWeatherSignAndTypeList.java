package com.coolweather.app.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class GetWeatherSignAndTypeList {
	static String address = "http://v.juhe.cn/weather/index?cityname=%E8%8B%8F%E5%B7%9E&dtype=&format=&key=d077e90edae520b6c8337132652096e9";
	public static void  getWeatherByWid(final String wid){
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(response)) {
					// 从服务器返回的数据中解析出天气
					
						 try {
							JSONObject jsonObject = new JSONObject(response);
							JSONArray result= jsonObject.getJSONArray("result");
							 for(int i=0; i<result.length();i++){//遍历数组
								JSONObject obj=  (JSONObject) result.opt(i);
				                
				                 //如果遍历到需要的数据后直接返回结果,根据key(wid)得到value判断是否等于传入参数
				                 if(obj.getString("wid").equals(wid)){
				                    String  result1=obj.getString("weather");
				                    seedMessage(result1);
				                 }
				             }
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
					 }
				
			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				
			}
			
		});//调用工具类获取接口数据
		
		
    }
	protected static String seedMessage(String result1) {
		// TODO Auto-generated method stub
		return result1;
	}
}
