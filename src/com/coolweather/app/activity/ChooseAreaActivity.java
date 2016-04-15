package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.app.R;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

public class ChooseAreaActivity extends Activity {
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	private List<String> dataList = new ArrayList<String>();
	/**
	* ʡ�б�
	*/
	private List<Province> provinceList;
	/**
	* ���б�
	*/
	private List<City> cityList;
	/**
	* ���б�
	*/
	private List<County> countyList;
	/**
	* ѡ�е�ʡ��
	*/
	private Province selectedProvince;
	/**
	* ѡ�еĳ���
	*/
	private City selectedCity;
	/**
	* ��ǰѡ�еļ���
	*/
	private int currentLevel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long arg3) {
				// TODO Auto-generated method stub
				if(currentLevel == LEVEL_PROVINCE){
					selectedProvince = provinceList.get(index);
					
					queryCities();
				}else if(currentLevel ==LEVEL_CITY){
					selectedCity = cityList.get(index);
				
					queryCounties();
				}
			}
		});
		queryProvinces();
	}
	/*
	 * ȫ�����е�ʡ�����ȴ����ݿ��ѯ�����û�в鵽��ȥ�������ϲ�ѯ�� 
	 * */
	private void queryProvinces() {
		// TODO Auto-generated method stub
		provinceList = coolWeatherDB.loadProvinces();
		if(provinceList.size()>0){
			dataList.clear();
			for(Province province : provinceList){
				dataList.add(province.getProvinceName());
				}
				adapter.notifyDataSetChanged();
				listView.setSelection(0);
				titleText.setText("�й�");
				currentLevel = LEVEL_PROVINCE;
		}else{
			queryFromServer(null, "province");
		}
	}
	/**
	* ��ѯѡ��ʡ�����е��У����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ��
	*/
	private void queryCities() {
		cityList = coolWeatherDB.loadCities(selectedProvince.getId());
		if (cityList.size() > 0) {
				dataList.clear();
				for (City city : cityList) {
						dataList.add(city.getCityName());
				}
				adapter.notifyDataSetChanged();
				listView.setSelection(0);
				titleText.setText(selectedProvince.getProvinceName());
				currentLevel = LEVEL_CITY;
				} else {
					queryFromServer(null, "city");
				}
	}

	/**
	* ��ѯѡ���������е��أ����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ��
	*/
	private void queryCounties() {
		countyList = coolWeatherDB.loadCounty(selectedCity.getId());
		if (countyList.size() > 0) {
				dataList.clear();
				for (County county : countyList) {
					dataList.add(county.getCountyName());
				}
				adapter.notifyDataSetChanged();
				listView.setSelection(0);
				titleText.setText(selectedCity.getCityName());
				currentLevel = LEVEL_COUNTY;
		} else {
			queryFromServer(null, "county");
		}
	}
	private void queryFromServer(final String code, final String type) {
		// TODO Auto-generated method stub
		String address = null;
		if (TextUtils.isEmpty(code)) {
			address = "http://v.juhe.cn/weather/citys?dtype=json&key=d077e90edae520b6c8337132652096e9";
		} 
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){

			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				boolean result = false;
				if("province".equals(type)){
					result = Utility.handleProvincesResponse(coolWeatherDB,response);
				}else if("city".equals(type)){
					result = Utility.handleCitiesResponse(coolWeatherDB, response, selectedProvince.getProvinceName(),selectedProvince.getId());
				}else if ("county".equals(type)) {
					result = Utility.handleCountiesResponse(coolWeatherDB,
							response, selectedCity.getCityName(),selectedCity.getId());
							}
				if(result){
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
						closeProgressDialog();
						if ("province".equals(type)) {
						queryProvinces();
						} else if ("city".equals(type)) {
						queryCities();
						} else if ("county".equals(type)) {
						queryCounties();
						}
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
					closeProgressDialog();
					Toast.makeText(ChooseAreaActivity.this,
					"����ʧ��", Toast.LENGTH_SHORT).show();
					}
					});
			}
		});
			
	
		
	}

	/*
	 * ����Back���������ݵ�ǰ�ļ������жϣ���ʱӦ�÷������б�ʡ�б�����ֱ���˳���
	 * */
	public void onBackPressed(){
		if(currentLevel == LEVEL_COUNTY){
			queryCities();
		}else if (currentLevel == LEVEL_CITY){
			queryProvinces();
		}else{
			finish();
		}
	}
	private void showProgressDialog() {
		// TODO Auto-generated method stub
		if(progressDialog == null){
			progressDialog =new ProgressDialog(this);
			progressDialog.setMessage("���ڼ���...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	private void closeProgressDialog() {
		// TODO Auto-generated method stub
		if (progressDialog != null) {
			progressDialog.dismiss();
			}
	}


}
