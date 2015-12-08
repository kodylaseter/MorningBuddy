package com.example.song.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.song.myapplication.adapters.CalendarAdapter;
import com.example.song.myapplication.data.Channel;
import com.example.song.myapplication.data.Item;
import com.example.song.myapplication.service.AlarmSoundAndVibrateService;
import com.example.song.myapplication.service.CalendarService;
import com.example.song.myapplication.service.WeatherService;
import com.example.song.myapplication.service.WeatherServiceCallback;
import com.example.song.myapplication.service.SingleShotLocationProvider;

public class AlarmActivity extends AppCompatActivity implements WeatherServiceCallback {

    private ImageView weatherIconImageView;
    private TextView temperatureTextView;
    private TextView conditionTextView;
    private TextView locationTextView;
    private ListView calendarEventListView;
    private AlarmSoundAndVibrateService alarm;

    private WeatherService service;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //small snippet code for waking up device
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        setContentView(R.layout.activity_alarm);

	//Setting up all the different services
        weatherIconImageView = (ImageView)findViewById(R.id.weatherIconImageView);
        temperatureTextView = (TextView)findViewById(R.id.temperatureTextView);
        conditionTextView = (TextView)findViewById(R.id.conditionTextView);
        locationTextView = (TextView)findViewById(R.id.locationTextView);
        alarm = new AlarmSoundAndVibrateService(this);

        calendarEventListView = (ListView)findViewById(R.id.calendarEventListView);
        CalendarService calendarService = new CalendarService(this);
        CalendarAdapter adapter = new CalendarAdapter(this, calendarService.getEvents());
        calendarEventListView.setAdapter(adapter);
        
        service = new WeatherService(this);

        Handler handler = new Handler();

        //small progress bar to indicate data fetching
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        //obtaining current location for weather checking
        SingleShotLocationProvider.requestSingleUpdate(this,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override
                    public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        //Log.d("Location", "my location is " + location.toString());
                        service.refreshWeather(location.toString());
                    }
                });
// dismiss the progress bar if no location information is found after 5 seconds
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }
        }, 5000);
alarm.startAlarm();
        Log.d("mbuddy", "alarm activity launched!");
		
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void endAlarm(View view) {
        alarm.stopAlarm();
    }

    /**
     *
     * @param view, current page view
     * dismiss the alarm and return to home
     */
    public void dismissAlarm(View view) {
        alarm.stopAlarm();
        Intent i = new Intent(this,HomeActivity.class);
        i.putExtra("EXIT", true);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Display weather information on screen
     * @param channel, queried results
     */
    @Override
    public void serviceSuccess(Channel channel) {
        dialog.hide();

        Item item = channel.getItem();
        int res = getResources().getIdentifier("drawable/c_" + item.getCondition().getCode(), null, getPackageName());

        Drawable weatherIconDrawable = getResources().getDrawable(res);
        weatherIconImageView.setImageDrawable(weatherIconDrawable);
        temperatureTextView.setText(item.getCondition().getTemperature() + "\u00B0" + channel.getUnits().getTemperature());
        conditionTextView.setText(item.getCondition().getDescription());
        locationTextView.setText(channel.getLocation());
    }

    /**
     * Raise exception if error
     * @param exception
     */
    @Override
    public void serviceFailure(Exception exception) {
        dialog.hide();
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }


}
