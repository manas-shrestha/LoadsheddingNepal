package com.example.loadshedding;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;



import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class GroupsInMaps extends FragmentActivity implements LocationListener{
	
	private GoogleMap mMap;
	LocationListener locationListener;
	LocationManager locationManager;
	double latitude, longitude;
	
	
@Override
protected void onCreate(Bundle arg0) {
	// TODO Auto-generated method stub
	super.onCreate(arg0);
	setContentView(R.layout.mapfragment);
	mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
			.getMap();
	locationManager = (LocationManager) GroupsInMaps.this
			.getSystemService(Context.LOCATION_SERVICE);
	locationManager.requestLocationUpdates(
			LocationManager.NETWORK_PROVIDER, 400, 1,
			GroupsInMaps.this);
	// Define the criteria how to select the locatioin provider ->
	// use
	// default
	Criteria criteria = new Criteria();
	criteria.setAccuracy(Criteria.ACCURACY_FINE);
	criteria.setAltitudeRequired(false);// true if required
	criteria.setBearingRequired(false);// true if required
	criteria.setCostAllowed(true);
	criteria.setPowerRequirement(Criteria.POWER_LOW);
	String provider = locationManager.getBestProvider(criteria, true);
	Location location = locationManager
			.getLastKnownLocation(provider);

	// Initialize the location fields
	if (location != null) {
		System.out.println("Provider " + provider
				+ " has been selected.");
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		Log.d("lat from location graber", "" + latitude);
		Log.d("long form location graber", "" + longitude);
		LatLng coordinate = new LatLng(latitude,longitude);
		CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(
				coordinate, 15);
		mMap.animateCamera(yourLocation);
		createMarker(mMap, latitude,longitude, "YOU ARE HERE",1);
		

	} 
	
	
	
		
}

public void createMarker(GoogleMap map, double latitude, double longitude,
		String title,int a) {
	map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
			.getMap();

	
	MarkerOptions markerOptions = new MarkerOptions();
	markerOptions.position(new LatLng(latitude, longitude));
	markerOptions.snippet("");
	markerOptions.title(title);
	if(a==1){
	markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.dotpointer));
	}else{
		markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.down));
		}
	Marker locationMarker = map.addMarker(markerOptions);
	locationMarker.setDraggable(true);
	locationMarker.showInfoWindow();
}
@Override
public void onLocationChanged(Location location) {
	// TODO Auto-generated method stub
	
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

}
