package com.example.song.myapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.song.myapplication.adapters.PlaceAutocompleteAdapter;
import com.example.song.myapplication.data.WeatherMonitor;
import com.example.song.myapplication.db.AlarmDBHelper;
import com.example.song.myapplication.logger.Log;
import com.example.song.myapplication.models.Alarm;
import com.example.song.myapplication.models.AlarmType;
import com.example.song.myapplication.service.AlarmManagerService;
import com.example.song.myapplication.service.TrafficChecker;
import com.example.song.myapplication.service.Utilities;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by song on 10/19/2015 0019.
 */
public class NewAlarmActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    AlarmDBHelper alarmDBHelper;
    EditText name;
    AutoCompleteTextView trafficOrigin;
    Switch trafficSwitch;
    Switch weatherSwitch;
    AutoCompleteTextView trafficDestination;
    TimePicker timePicker;
    GoogleApiClient mGoogleApiClient;
    PlaceAutocompleteAdapter mAdapter;
    Button mStart;
    Button mDestination;
    Button mBut1;
    Button mBut2;
    String start, end;

    //preset location for testing and debugging
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        alarmDBHelper = AlarmDBHelper.getInstance(this);
        name = (EditText)findViewById(R.id.nameText);
        trafficSwitch = (Switch)findViewById(R.id.trafficSwitch);
        weatherSwitch = (Switch)findViewById(R.id.weatherSwitch);
        trafficOrigin = (AutoCompleteTextView)findViewById(R.id.trafficOrigin); //starting
        trafficDestination = (AutoCompleteTextView)findViewById(R.id.trafficDestination); //destination
        timePicker = (TimePicker)findViewById(R.id.timePicker);
        mStart = (Button)findViewById(R.id.mapbut1);
        mDestination = (Button)findViewById(R.id.mapbut2);
        findViewById(R.id.new_alarm_linearlayout).requestFocus();

        //switch listener, which listens on traffic and weather switches
        CompoundButton.OnCheckedChangeListener switchListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.getId() == R.id.trafficSwitch) {
                    if (isChecked) {
                        trafficDestination.setVisibility(View.VISIBLE);
                        trafficOrigin.setVisibility(View.VISIBLE);
                        mStart.setVisibility(View.VISIBLE);
                        mDestination.setVisibility(View.VISIBLE);
                    } else {
                        trafficDestination.setVisibility(View.GONE);
                        trafficOrigin.setVisibility(View.GONE);
                        mStart.setVisibility(View.GONE);
                        mDestination.setVisibility(View.GONE);
                    }
                }
            }
        };
        //google map functions for address look up
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();
        trafficDestination.setOnItemClickListener(mAutocompleteClickListener);
        trafficOrigin.setOnItemClickListener(mAutocompleteClickListener);
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_GREATER_SYDNEY,
                null);
        trafficDestination.setAdapter(mAdapter);
        trafficOrigin.setAdapter(mAdapter);

        //setting the address
        mStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), TrafficMapActivity.class);
                myIntent.putExtra("requestCode", 1);
                startActivityForResult(myIntent, 1);
            }
        });
        mDestination.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), TrafficMapActivity.class);
                myIntent.putExtra("requestCode", 2);
                startActivityForResult(myIntent, 2);
            }
        });

        //add listener to switches
        trafficSwitch.setOnCheckedChangeListener(switchListener);
        weatherSwitch.setOnCheckedChangeListener(switchListener);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.weather_settings) {
            startActivity(new Intent(this, WeatherActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * set address text input
     * @param requestCode, indicate which activity has been returned
     * @param resultCode, indicate weather the activity has been successfully completed
     * @param data, the data the might need for further execution
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String mapviewStartingLoc = data.getStringExtra("mapviewStartingLoc");
                trafficOrigin.setText(mapviewStartingLoc);
            }
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                String mapviewDestinationLoc = data.getStringExtra("mapviewDestinationLoc");
                trafficDestination.setText(mapviewDestinationLoc);
            }
        }
    }

    /**
     * do a traffic time check before finalizing set up the alarm
     * @param view
     */
    public void initialAddAlarm(View view) {
        start = trafficOrigin.getText().toString();
        end = trafficDestination.getText().toString();
        //if switch is enabled and both autocomplete boxes are non-empty
        if (trafficSwitch.isChecked() &&
                (start != null && !start.equals(""))
                &&
                (end != null && !end.equals(""))) {
            TrafficChecker trafficChecker = new TrafficChecker(this, this);
            Alarm alarm = new Alarm();
            alarm.setOrigin(start);
            alarm.setDestination(end);
            int alarmTime = (timePicker.getCurrentHour() * 60) + timePicker.getCurrentMinute();
            alarm.setTime(alarmTime);
            Log.d("mbuddy", "about to check time");
            trafficChecker.checkTime(alarm);
        } else {
            finishAddAlarm(0);
        }
    }

    /**
     * finalizing the alarm, which sets up the traffic and weather adjustment time
     * @param timeEstimate
     */
    //@TargetApi(Build.VERSION_CODES.M)
    public void finishAddAlarm(float timeEstimate) {
        Alarm alarm = new Alarm();
        alarm.setName(name.getText().toString());
        int alarmTime = (timePicker.getCurrentHour() * 60) + timePicker.getCurrentMinute();

        alarm.setTime(alarmTime);
        alarm.setTrafficEnabled(trafficSwitch.isChecked());
        alarm.setWeatherEnabled(weatherSwitch.isChecked());
        alarm.setOrigin(start);
        alarm.setDestination(end);
        alarm.setTimeEstimate(timeEstimate);
        alarm.setNewTime(Alarm.DUMMY_TIME);
        double test = timeEstimate;
        //Alarm realAlarm = alarmDBHelper.addAlarm(alarm);
        int bufferTime;
        //traffic has higher priority
        if (!alarm.isTrafficEnabled()) {
            if (alarm.isWeatherEnabled()) { //check on weather adjustment time
                bufferTime = WeatherMonitor.getInstance().getMaxTime();
                if (Utilities.isTimeFarEnoughAway(alarm.getTimeAsTime(), bufferTime)) {
                    //adjust the alarm based on weather information, then set up the alarm
                    Alarm realAlarm = alarmDBHelper.addAlarm(alarm);
                    AlarmManagerService.getInstance().setAlarm(realAlarm, AlarmType.CHECKWEATHER, this, realAlarm.getTime());
                    Intent i = new Intent(this,HomeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("EXIT", true);
                    startActivity(i);
                } else {
                    Toast.makeText(this, "Weather check requires at least " + bufferTime + " minutes.", Toast.LENGTH_SHORT).show();
                }

            } else {    //default alarm
                Alarm realAlarm = alarmDBHelper.addAlarm(alarm);
                AlarmManagerService.getInstance().setAlarm(realAlarm, AlarmType.ACTUALALARM, this, realAlarm.getTime());
                Intent i = new Intent(this,HomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("EXIT", true);
                startActivity(i);
            }
        } else {
            bufferTime = Alarm.TRAFFIC_BUFFER_TIME;
            String origin = alarm.getOrigin();
            String dest = alarm.getDestination();
            //adjust the alarm based on the pre-fetched traffic time
            if (origin == null || origin.equals("") || dest == null || dest.equals("")) {
                Toast.makeText(this, "Origin and Destination must be set to use traffic updates! ", Toast.LENGTH_SHORT).show();
            } else if (timeEstimate < 0.1) {
                Toast.makeText(this, "Time estimate was too small, something is wrong.", Toast.LENGTH_SHORT).show();
            } else {
                if (Utilities.isTimeFarEnoughAway(alarm.getTimeAsTime(), bufferTime)){
                    Alarm realAlarm = alarmDBHelper.addAlarm(alarm);
                    AlarmManagerService.getInstance().setAlarm(realAlarm, AlarmType.CHECKTRAFFIC, this, realAlarm.getTime() - Alarm.TRAFFIC_BUFFER_TIME);
                    Intent i = new Intent(this,HomeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("EXIT", true);
                    startActivity(i);
                } else {
                    Toast.makeText(this, "Traffic check requires at least " + bufferTime + " minutes.", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    /**
     * check on any layer clicked on the map
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

            places.release();
        }
    };

    /**
     * formatting the output string for the map addresss
     * @param res
     * @param name
     * @param id
     * @param address
     * @param phoneNumber
     * @param websiteUri
     * @return
     */
    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {

        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }
s
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
