package com.coolweather.app.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.coolweather.app.R;

import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.Activity;
import android.content.Intent;
//import android.content.DialogInterface;
//import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity implements OnClickListener {

	private LinearLayout weatherInfoLayout;
	private LinearLayout weatherLayout1;
	private LinearLayout weatherLayout2;
	private TextView cityNameText;

	/*
	 * 用于显示当前天气描述信息
	 */
	private TextView weatherDespText;

	/*
	 * 用于显示今日天气
	 */
	private TextView weather;

	/*
	 * 切换城市按钮
	 */
	//private Button switchCity;
	/*
	 * 更新天气按钮
	 */
	//private Button refreshWeather;
	/*
	 * 用于切换今天天气
	 */
	private Button switchtoday;
	/*
	 * 用于切换未来天气
	 */
	private Button switchfuture;
	private TextView date_y;
	private TextView temperature;
	private TextView week;
	private TextView wind;
	private TextView temp;
	private TextView publishText;
	private TextView picture;
	private boolean a;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		// 初始化控件
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		weatherLayout1 = (LinearLayout) findViewById(R.id.weather_layout1);
		weatherLayout2 = (LinearLayout) findViewById(R.id.weather_layout2);
		//picture = (TextView) findViewById(R.id.picture);
		cityNameText = (TextView) findViewById(R.id.city_name);
		temp = (TextView) findViewById(R.id.temp);
		publishText = (TextView) findViewById(R.id.current_date);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		week = (TextView) findViewById(R.id.week);
		wind = (TextView) findViewById(R.id.wind);
		weather = (TextView) findViewById(R.id.weather);
		// weather = (TextView) findViewById(R.id.weather);
		date_y = (TextView) findViewById(R.id.date_y);
	   temperature = (TextView) findViewById(R.id.temperature);
		// weather = (TextView) findViewById(R.id.weather);
		 switchtoday = (Button) findViewById(R.id.button1);
		 switchfuture = (Button) findViewById(R.id.button2);
		// switchCity = (Button) findViewById(R.id.switch_city);
		// refreshWeather = (Button) findViewById(R.id.refresh_weather);
		String cityname = getIntent().getStringExtra("city_name");
		switchtoday.setOnClickListener(this);
		switchfuture.setOnClickListener(this);
		cityNameText.setOnClickListener(this);
		if (!TextUtils.isEmpty(cityname)) {
			// 有县级代号时就去查询天气
			publishText.setText("同步中...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			
			
			String address = "";
			try {
				address = "http://v.juhe.cn/weather/index?cityname="
						+ URLEncoder.encode(cityname, "UTF-8")
						+ "&dtype=json&format=1&key=d077e90edae520b6c8337132652096e9";
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			Log.d("", address.toString());
			queryFromServer(address);
		} else {
			// 没有县级代号时就直接显示本地天气
			showWeather();
		}
		// switchCity.setOnClickListener(this);
		// refreshWeather.setOnClickListener(this);
	}

	private void showWeather() {
		// TODO Auto-generated method stub
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", ""));
		
		publishText.setText("今天" + prefs.getString("current_date", "") + "发布"+""+""+prefs.getString("time", ""));
		temp.setText(prefs.getString("temp", "")+"℃");
		weatherDespText.setText(prefs.getString("wind_direction", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		//date_y.setText(prefs.getString("date_y", ""));
		// temperature.setText(prefs.getString("temperature", ""));
		// weather.setText(prefs.getString("weather", ""));
	}

	private void queryFromServer(String address) {
		// TODO Auto-generated method stub

		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
				Log.d("ytyu",response);
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(response)) {
					// 从服务器返回的数据中解析出天气
					Utility.handleWeatherResponse(WeatherActivity.this,
							response);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							showWeather();
						}

					});
				}
			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						publishText.setText("同步失败");
					}

				});
			}

		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			 a= true;
			showWeather1();
			break;
		case R.id.button2:
			a= true;
			showWeather2();
			break;
		case R.id.city_name:
		
			Intent intent = new Intent(WeatherActivity.this,ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent); 
			finish();
			break;
			
		}
	}

	private void showWeather2() {
		// TODO Auto-generated method stub
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(a = true){
//		date_y.setText(prefs.getString("date_y", ""));
//		temperature.setText(prefs.getString("temperature", ""));
//		
//		week.setText(prefs.getString("week", ""));
//		
//		wind.setText(prefs.getString("wind", ""));
//	
//		weather.setText(prefs.getString("weather", ""));
//	
//		weatherInfoLayout.setVisibility(View.VISIBLE);
//		cityNameText.setVisibility(View.VISIBLE);
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		weatherLayout1.setVisibility(View.INVISIBLE);
		weatherLayout2.setVisibility(View.VISIBLE);
		a= false;
		}
		
	}

	private void showWeather1() {
		// TODO Auto-generated method stub
		if(a =true){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		date_y.setText(prefs.getString("date_y", ""));
		temperature.setText(prefs.getString("temperature", ""));
		Log.d("temperature:",prefs.getString("temperature", "") );
		week.setText(prefs.getString("week", ""));
		Log.d("week:",prefs.getString("week", "") );
		wind.setText(prefs.getString("wind", ""));
		Log.d("wind:",prefs.getString("wind", "") );
		weather.setText(prefs.getString("weather", ""));
		Log.d("weather:",prefs.getString("weather", "") );
		//picture.setText(prefs.getString("picture", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		weatherLayout1.setVisibility(View.VISIBLE);
		weatherLayout2.setVisibility(View.INVISIBLE);
		a=false;
		}
		
	}

}
