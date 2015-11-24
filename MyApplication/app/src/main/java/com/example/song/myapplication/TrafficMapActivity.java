package com.example.song.myapplication;

/**
 * Created by kenta on 11/5/2015.
 */

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.song.myapplication.adapters.PlaceAutocompleteAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class TrafficMapActivity extends FragmentActivity
        implements GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, OnMapLongClickListener, View.OnClickListener {

    /**
     * GoogleApiClient wraps our service connection to Google Play Services and provides access
     * to the user's sign in state as well as the Google's APIs.
     */
    protected GoogleApiClient mGoogleApiClient;

    private PlaceAutocompleteAdapter mAdapter;

    private AutoCompleteTextView mAutocompleteView;

    private GoogleMap mMap;

    private LatLng coordData;

    private MarkerOptions markerOptions;


    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Construct a GoogleApiClient for the {@link Places#GEO_DATA_API} using AutoManage
        // functionality, which automatically sets up the API client to handle Activity lifecycle
        // events. If your activity does not extend FragmentActivity, make sure to call connect()
        // and disconnect() explicitly.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();

        setContentView(R.layout.activity_maps);

        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        mAutocompleteView = (AutoCompleteTextView)
                findViewById(R.id.autocomplete_places);

        // Register a listener that receives callbacks when a suggestion has been selected
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);


        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_GREATER_SYDNEY,
                null);
        mAutocompleteView.setAdapter(mAdapter);

        // Set up the 'clear text' button that clears the text in the autocomplete view
        Button clearButton = (Button) findViewById(R.id.button_clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAutocompleteView.setText("");
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double gtLocation[] = getGTCoordinates();
        LatLng current_latlng = new LatLng(gtLocation[0], gtLocation[1]);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current_latlng, 15));

        mMap.setOnMapLongClickListener(this);

        FloatingActionButton pinpointMaps = (FloatingActionButton) findViewById(R.id.donebutton);
        pinpointMaps.setOnClickListener(this);

    }
    @Override
    public void onMapLongClick(LatLng point) {
        mMap.clear();
        markerOptions = new MarkerOptions().position(point);
        mMap.addMarker(markerOptions).isDraggable();
        System.out.println(point.latitude);
        System.out.println(point.longitude);
        coordData = point;
        mAutocompleteView.setText(point.latitude+","+point.longitude);
    }

    public void onClick(View v) {
        int requestCode = getIntent().getExtras().getInt("requestCode");
        if(requestCode == 1){
            Intent intent = new Intent();
            if(coordData != null){
                intent.putExtra("mapviewStartingLoc", coordData.latitude + "," + coordData.longitude);
            }else{
                intent.putExtra("mapviewStartingLoc", "");
            }
            coordData = null;
            setResult(RESULT_OK, intent);
            finish();
        }else{
            Intent intent = new Intent();
            if(coordData != null){
                intent.putExtra("mapviewDestinationLoc", coordData.latitude + "," + coordData.longitude);
            }else{
                intent.putExtra("mapviewDestinationLoc", "");
            }
            coordData = null;
            setResult(RESULT_OK, intent);
            finish();
        }

    }


    public double[] getGTCoordinates() {
        double[] coordinates = new double[2];
        coordinates[0] = 33.776262417193344;
        coordinates[1] = -84.39778804779053;
        return coordinates;
    }

    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);




            //Toast.makeText(getApplicationContext(), "Clicked: " + primaryText, Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            mMap.clear();
            LatLng nP = place.getLatLng();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nP, 15));
            markerOptions = new MarkerOptions().position(nP);
            mMap.addMarker(markerOptions).isDraggable();

            coordData = nP;

            places.release();
        }
    };

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {

        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }

    /**
     * Called when the Activity could not connect to Google Play services and the auto manager
     * could resolve the error automatically.
     * In this case the API is not available and notify the user.
     *
     * @param connectionResult can be inspected to determine the cause of the failure
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }
}
