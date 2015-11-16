package com.example.kenta.googlemapstest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnMapLongClickListener, View.OnClickListener {

    private GoogleMap mMap;
    private EditText destination_coordinates;
    private LatLng coordData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double gtLocation[] = getGTCoordinates();
        LatLng current_latlng = new LatLng(gtLocation[0], gtLocation[1]);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current_latlng, 15));

        mMap.setOnMapLongClickListener(this);

        Button pinpointMaps = (Button) findViewById(R.id.donebutton);
        pinpointMaps.setOnClickListener(this);

    }

    public void onClick(View v) {
        setContentView(R.layout.activity_start);

        Intent intent = new Intent();
        intent.putExtra("edittextvalue", coordData.latitude + "," + coordData.longitude);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onMapLongClick(LatLng point) {
        mMap.clear();
        MarkerOptions markerOptions = new MarkerOptions().position(point);
        mMap.addMarker(markerOptions);
        System.out.println(point.latitude);
        System.out.println(point.longitude);
        coordData = point;
    }

    /**
     * This method will return an array containing the latitude
     * and longitude of your current position.
     * For now this is hardcoded to Georgia Tech.
     * Todo: Look into finding current latitude and longitude from gps.
     * Usage: Latitude --> North is positive, South is negative
     * Longitude -> East is positive, West is Negative
     * Georgia Tech is at 33.7758North, 84.3947West == 33.7758, - 84.3947
     */
    public double[] getGTCoordinates() {
        double[] coordinates = new double[2];
        coordinates[0] = 33.776262417193344;
        coordinates[1] = -84.39778804779053;
        return coordinates;
    }
}

