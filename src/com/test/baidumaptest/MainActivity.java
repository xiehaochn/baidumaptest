package com.test.baidumaptest;

import java.util.List;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, OnMarkerClickListener,OnGetGeoCoderResultListener{
	private MapView mMapView = null;
	private BaiduMap mBaiduMap; 
	private ImageButton normal_satellite;
	private ImageButton traffic;
	private ImageButton show_location;
	private ImageButton search;
	private LocationManager locationManager;
	private String provider;
	private Location location;
	private LatLng my_point;
	private EditText search_edittext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext()); 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main); 
        mMapView = (MapView) findViewById(R.id.bmapView); 
        mBaiduMap=mMapView.getMap();
        normal_satellite=(ImageButton)findViewById(R.id.normal_satellite);
        normal_satellite.setOnClickListener(this);
        traffic=(ImageButton)findViewById(R.id.traffic);
        traffic.setOnClickListener(this);     
        show_location=(ImageButton)findViewById(R.id.show_location);
        show_location.setOnClickListener(this);     
        search=(ImageButton)findViewById(R.id.search);
        search.setOnClickListener(this);
        search_edittext=(EditText)findViewById(R.id.title_search);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka1);
		LatLng point=new LatLng(39.963175, 116.400244);
		OverlayOptions option = new MarkerOptions()
				.position(point)
				.icon(bitmap);
				
		Marker marker1 = (Marker) mBaiduMap.addOverlay(option);
		mBaiduMap.setOnMarkerClickListener(this);
        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
		List<String> providerList=locationManager.getProviders(true);
		if(providerList.contains(LocationManager.GPS_PROVIDER)){
			provider=LocationManager.GPS_PROVIDER;
		}else if(providerList.contains(LocationManager.NETWORK_PROVIDER)){
			provider=LocationManager.NETWORK_PROVIDER;
		}else{
			Toast.makeText(this, "LocationService is unavilable", Toast.LENGTH_SHORT).show();
			return;
		}
		location=locationManager.getLastKnownLocation(provider);

		if(location!=null){
			showlocation(location);
		}
		
		locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
		
	}
		
		LocationListener locationListener=new LocationListener(){

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				showlocation(location);
			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
		};
		

	private void showlocation(final Location location) {
		// TODO Auto-generated method stub
		double address_latitude=location.getLatitude();
		double address_longitude=location.getLongitude();
	
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka1);
		my_point=new LatLng(address_latitude,address_longitude);
		
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newLatLng(my_point);
		mBaiduMap.animateMapStatus(mMapStatusUpdate);
		OverlayOptions my_option = new MarkerOptions().position(my_point).icon(bitmap);				
		Marker my_marker = (Marker) mBaiduMap.addOverlay(my_option); 
		
		}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

@Override  
protected void onDestroy() {  
    super.onDestroy();  
    //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
    mMapView.onDestroy();  
	if(locationManager!=null){
	locationManager.removeUpdates(locationListener);
	}
}  
@Override  
protected void onResume() {  
    super.onResume();  
    //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
    mMapView.onResume();  
    }  
@Override  
protected void onPause() {  
    super.onPause();  
    //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
    mMapView.onPause();  
    }

@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	switch(v.getId()){
	case(R.id.normal_satellite):{
		if(mBaiduMap.getMapType()==BaiduMap.MAP_TYPE_SATELLITE){
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		}else{
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
		}
		break;
	}
	case(R.id.traffic):{
		if(mBaiduMap.isTrafficEnabled()){
			mBaiduMap.setTrafficEnabled(false);
		}else{
			mBaiduMap.setTrafficEnabled(true);
		}
		break;
	}
	case(R.id.show_location):{
		GeoCoder coder = GeoCoder.newInstance();
		ReverseGeoCodeOption my_location=new ReverseGeoCodeOption();
		ReverseGeoCodeOption result=my_location.location(my_point);
		coder.reverseGeoCode(result);
		coder.setOnGetGeoCodeResultListener(this);
		break;
	}
	default:
		break;
	}
}


@Override
public boolean onMarkerClick(Marker arg0) {
	// TODO Auto-generated method stub
	
	Toast.makeText(MainActivity.this, "You clicked "+arg0.getPosition(), Toast.LENGTH_SHORT).show();
	return false;
}


@Override
public void onGetGeoCodeResult(GeoCodeResult arg0) {
	// TODO Auto-generated method stub
	
}


@Override
public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
	// TODO Auto-generated method stub
	Toast.makeText(MainActivity.this, "Your location is "+arg0.getAddress(), Toast.LENGTH_SHORT).show();
}
}

