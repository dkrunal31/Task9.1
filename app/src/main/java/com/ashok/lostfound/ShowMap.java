package com.ashok.lostfound;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.ashok.lostfound.database.DatabaseHelper;
import com.ashok.lostfound.model.LostFound;
import com.ashok.lostfound.model.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class ShowMap extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(ShowMap.this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap=map;
        double lan;
        double lon;
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        List<LostFound>  userData= databaseHelper.getAllData();
        for ( LostFound lostFound :userData) {
            Log.d("location", "onMapReady: "+ lostFound.getLocation());
           List<Place> place= new Details().JsonTOobj();
            for (Place p:place) {
                if(p.getName().equals(lostFound.getLocation())){
                     lan= Double.parseDouble(p.getLatitude());
                     lon= Double.parseDouble(p.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(lan, lon)).title(lostFound.getName()));
                }else{

                    googleMap.addMarker(new MarkerOptions().position(new LatLng(37.4219761, -122.0840068)).title(lostFound.getName()));

                }


            }

        }
        addPointsOnMap();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    private void addPointsOnMap() {


//        // Add points on the map based on the given latitude and longitude coordinates
//        LatLng point1 = new LatLng(37.7749, -122.4194);
//        googleMap.addMarker(new MarkerOptions().position(point1).title("Point 1"));
//
//        LatLng point2 = new LatLng(34.0522, -118.2437);
//        googleMap.addMarker(new MarkerOptions().position(point2).title("Point 2"));
//
//        LatLng point3 = new LatLng(40.7128, -74.0060);
//        googleMap.addMarker(new MarkerOptions().position(point3).title("Point 3"));
//
//
//        // Zoom to a specific location
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point1, 12));
    }


    private double getRandomLatitude() {
        Random random = new Random();
        double latitude = random.nextDouble() * 180 - 90; // Range: -90 to +90
        return latitude;
    }

    private double getRandomLongitude() {
        Random random = new Random();
        double longitude = random.nextDouble() * 360 - 180; // Range: -180 to +180
        return longitude;
    }
}