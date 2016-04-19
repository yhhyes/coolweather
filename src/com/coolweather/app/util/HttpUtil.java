package com.coolweather.app.util;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.client.utils.URLEncodedUtils;

import android.util.Log;

public class HttpUtil {
	public static void sendHttpRequest(final String address,final HttpCallbackListener  listener){
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection = null;
				try{
					Log.d("调用前：",address.toString());
					URL url = new URL(address);
					connection=(HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					//获得输入流
					InputStream in = connection.getInputStream();
					//输入流变成高级输出流
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line= reader.readLine();
					while(line !=null){
						response.append(line);
						line=null;
					}
					Log.d("调用后处理的数据：",response.toString());
					if(listener !=null){
						//回调onFinish()方法
						listener.onFinish(response.toString());
					}
					}catch(Exception e){
						if (listener !=null){
							//回调onError()方法
							listener.onError(e);
						}
						Log.e("httputil.sendHttpRequest", e.getMessage());
					}finally{
						if(connection !=null){
							connection.disconnect();
						}
					}
			}

			
		}).start();
	}
}
