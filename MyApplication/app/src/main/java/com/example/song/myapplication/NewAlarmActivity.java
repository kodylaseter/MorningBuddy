package com.example.song.myapplication;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.song.myapplication.db.AlarmDBHelper;
import com.example.song.myapplication.models.Alarm;
import com.example.song.myapplication.models.PlaceAutocompleteAdapter;
import com.example.song.myapplication.service.AlarmManagerService;
import com.example.song.myapplication.service.AlarmReceiver;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by song on 10/19/2015 0019.
 */
public class NewAlarmActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    AlarmDBHelper alarmDBHelper;
    EditText name;
    AutoCompleteTextView trafficLocation;
    Switch trafficSwitch;
    Switch weatherSwitch;
    EditText weatherLocation;
    AutoCompleteTextView trafficDestination;
    TimePicker timePicker;
    GoogleApiClient mGoogleApiClient;
    PlaceAutocompleteAdapter mAdapter;
    Button mStart;
    Button mDestination;
    Button mTime;
    TextView mdisplaytime;
    Button mBut1;
    Button mBut2;
    static String starter;
    static String ender;

    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));

    //WeatherMonitor wm = new WeatherMonitor(0, 0, 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        alarmDBHelper = AlarmDBHelper.getInstance(this);
        name = (EditText)findViewById(R.id.nameText);
        trafficSwitch = (Switch)findViewById(R.id.trafficSwitch);
        weatherSwitch = (Switch)findViewById(R.id.weatherSwitch);
        trafficLocation = (AutoCompleteTextView)findViewById(R.id.trafficLocation); //starting
        trafficDestination = (AutoCompleteTextView)findViewById(R.id.trafficDestination); //destination
        weatherLocation = (EditText)findViewById(R.id.weatherLocation);
        timePicker = (TimePicker)findViewById(R.id.timePicker);
        mStart = (Button)findViewById(R.id.mapbut1);
        mDestination = (Button)findViewById(R.id.mapbut2);
        mTime = (Button)findViewById(R.id.timebut);
        mdisplaytime = (TextView)findViewById(R.id.displaytime);
        mBut1 = (Button)findViewById(R.id.mapbut1);
        mBut2 = (Button)findViewById(R.id.mapbut2);

        CompoundButton.OnCheckedChangeListener switchListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.getId() == R.id.trafficSwitch) {
                    if (isChecked) {
                        trafficDestination.setVisibility(View.VISIBLE);
                        trafficLocation.setVisibility(View.VISIBLE);
                        mStart.setVisibility(View.VISIBLE);
                        mDestination.setVisibility(View.VISIBLE);
                        mTime.setVisibility(View.VISIBLE);
                        mdisplaytime.setVisibility(View.VISIBLE);
                    } else {
                        trafficDestination.setVisibility(View.GONE);
                        trafficLocation.setVisibility(View.GONE);
                        mStart.setVisibility(View.GONE);
                        mDestination.setVisibility(View.GONE);
                        mTime.setVisibility(View.GONE);
                        mdisplaytime.setVisibility(View.GONE);
                    }
                } else if (buttonView.getId() == R.id.weatherSwitch) {
                    if (isChecked) {
                        //weatherMonitoringOn();
                    }
                }
            }
        };
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();
        trafficDestination.setOnItemClickListener(mAutocompleteClickListener);
        trafficLocation.setOnItemClickListener(mAutocompleteClickListener);
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_GREATER_SYDNEY,
                null);
        trafficDestination.setAdapter(mAdapter);
        trafficLocation.setAdapter(mAdapter);

        mTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new DownloadTask().execute();
            }
        });

//        mBut1.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Intent myIntent = new Intent(view.getContext(), TrafficMapActivity.class);
//                startActivityForResult(myIntent, 1);
//            }
//        });

        trafficSwitch.setOnCheckedChangeListener(switchListener);
        weatherSwitch.setOnCheckedChangeListener(switchListener);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String stredittext = data.getStringExtra("edittextvalue");
                trafficLocation.setText(stredittext);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void createAlarmClicked (View view) {
        Alarm alarm = new Alarm();
        alarm.setName(name.getText().toString());
        int alarmTime = (timePicker.getCurrentHour() * 60) + timePicker.getCurrentMinute();
        alarm.setTime(alarmTime);
        alarm.setTrafficEnabled(trafficSwitch.isChecked());
        alarm.setWeatherEnabled(weatherSwitch.isChecked());
        alarmDBHelper.addAlarm(alarm);
        AlarmManagerService.getInstance().setAlarm(alarm, this);
        Intent i = new Intent(this,HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("EXIT", true);
        startActivity(i);
    }

    public String getWaypointQuery(String origin, String destination){
        String query = "https://maps.googleapis.com/maps/api/distancematrix/json?";
        if(origin != null){
            query += "origins=" + origin.replace(" ", "+");
        }
        if(destination != null){
            query += "&destinations=" + destination.replace(" ", "+");;
        }
//        if(mode != null){
//            query += "&mode=" + mode;
//        }
//        if(departure_time != null){
//            query += "&departure_time" + departure_time;
//        }
        return query;
    }
    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            //Proof of Concept of ugliness.
            double totalTime = 0;
            String theUrl = getWaypointQuery(starter,ender);
            InputStream inputStream = null;
            String json = "";
            String time = "";
            try{
                inputStream = downloadUrl(theUrl);
                json = readStream(inputStream);
                time = json_getTime(json);
                totalTime += Double.parseDouble(time); //I need to get my types figured out between string and double.
            }catch(IOException e){
                return "Connection Error";
            }

            //Formats Decimal just in case of floating point.
            DecimalFormat df = new DecimalFormat("#,###,##0.00");
            return df.format(totalTime/60) + " Minutes";
        }

        /**
         * Uses the logging framework to display the output of the fetch
         * operation in the log fragment.
         */
        @Override
        protected void onPostExecute(String result) {
            mdisplaytime.setText(result);
        }
    }

    /**
     * Given a string representation of a URL, sets up a connection and gets
     * an input stream.
     * @param urlString A string representation of a URL.
     * @return An InputStream retrieved from a successful HttpURLConnection.
     * @throws IOException
     */
    private InputStream downloadUrl(String urlString) throws IOException {
        // BEGIN_INCLUDE(get_inputstream)
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Start the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
        // END_INCLUDE(get_inputstream)
    }

    /**
     * Takes an input stream and reads it into a string format.
     * @param inputStream an inputstream from reading an http request
     * @return returns a String containing the http request data.
     */
    private String readStream(InputStream inputStream){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
            StringBuilder sbuild = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sbuild.append(line);
            }
            inputStream.close();
            return sbuild.toString();
        } catch (Exception e) {
            return "JSON read failed";
        }
    }

    /**
     * Takes the HTTP string data and converts it into a JSON format to extract time information.
     * The extracted time is the total time in seconds.
     * @param json The http request data
     * @return returns the total time in seconds from point A to point B
     */
    private String json_getTime(String json){
        String timeReturn = "";
        try {
            JSONObject json1 = new JSONObject(json);
            JSONArray rows = json1.getJSONArray("rows");
            for (int i = 0; i < rows.length(); i++) { //traverse the rows to find elements...
                JSONObject obj = rows.getJSONObject(i);
                JSONArray elements = obj.getJSONArray("elements");
                for (int j = 0; j < elements.length(); j++) {
                    JSONObject elem = elements.getJSONObject(j);
                    JSONObject duration = elem.getJSONObject("duration");
                    System.out.println(duration.getString("text"));
                    System.out.println(duration.getString("value"));
                    timeReturn = duration.getString("value");
                }
            }
            return timeReturn;
        } catch (Exception e) {
            return "Failed to Read Time";
        }
    }

    private void weatherMonitoringOn() {
        //for future weather monitor switch function
    }

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




            Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
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

            if(trafficLocation != null || !trafficLocation.getText().toString().equals("")){
                starter = trafficLocation.getText().toString();
            }
            if(trafficDestination != null || !trafficDestination.getText().toString().equals("")){
                ender = trafficDestination.getText().toString();
            }

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
