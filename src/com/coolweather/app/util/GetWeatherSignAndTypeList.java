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
					// �ӷ��������ص������н���������
					
						 try {
							JSONObject jsonObject = new JSONObject(response);
							JSONArray result= jsonObject.getJSONArray("result");
							 for(int i=0; i<result.length();i++){//��������
								JSONObject obj=  (JSONObject) result.opt(i);
				                
				                 //�����������Ҫ�����ݺ�ֱ�ӷ��ؽ��,����key(wid)�õ�value�ж��Ƿ���ڴ������
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
			
		});//���ù������ȡ�ӿ�����
		
		
    }
	protected static String seedMessage(String result1) {
		// TODO Auto-generated method stub
		return result1;
	}
}
