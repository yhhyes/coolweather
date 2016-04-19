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
					Log.d("����ǰ��",address.toString());
					URL url = new URL(address);
					connection=(HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					//���������
					InputStream in = connection.getInputStream();
					//��������ɸ߼������
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line= reader.readLine();
					while(line !=null){
						response.append(line);
						line=null;
					}
					Log.d("���ú�������ݣ�",response.toString());
					if(listener !=null){
						//�ص�onFinish()����
						listener.onFinish(response.toString());
					}
					}catch(Exception e){
						if (listener !=null){
							//�ص�onError()����
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
