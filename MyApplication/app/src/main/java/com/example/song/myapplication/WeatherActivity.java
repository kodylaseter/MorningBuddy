package com.example.song.myapplication;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.song.myapplication.data.Channel;
import com.example.song.myapplication.data.Item;
import com.example.song.myapplication.data.WeatherMonitor;
import com.example.song.myapplication.service.WeatherService;
import com.example.song.myapplication.service.WeatherServiceCallback;

public class WeatherActivity extends AppCompatActivity implements WeatherServiceCallback {

    private ImageView weatherIconImageView;
    private TextView temperatureTextView;
    private TextView conditionTextView;
    private TextView locationTextView;

    private EditText snowAdjustTime;
    private EditText windyAdjustTime;
    private EditText stormAdjustTime;
    private Switch snowOnSwitch;
    private Switch windyOnSwitch;
    private Switch stormOnSwitch;

    private Button saveButton;

    private WeatherService service;
    private ProgressDialog dialog;

    public static int onPassSnowAdjustTime = 0;
    public static int onPassWindyAdjustTime = 0;
    public static int onPassStormAdjustTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherIconImageView = (ImageView)findViewById(R.id.weatherIconImageView);
        temperatureTextView = (TextView)findViewById(R.id.temperatureTextView);
        conditionTextView = (TextView)findViewById(R.id.conditionTextView);
        locationTextView = (TextView)findViewById(R.id.locationTextView);
        snowAdjustTime = (EditText)findViewById(R.id.snowAdjustTime);
        windyAdjustTime = (EditText)findViewById(R.id.windyAdjustTime);
        stormAdjustTime = (EditText)findViewById(R.id.stormAdjustTime);
        snowOnSwitch = (Switch)findViewById(R.id.snowSwitch);
        windyOnSwitch = (Switch)findViewById(R.id.windySwitch);
        stormOnSwitch = (Switch)findViewById(R.id.stormSwitch);
        saveButton = (Button)findViewById(R.id.saveButton);

        CompoundButton.OnCheckedChangeListener switchListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.getId() == R.id.snowSwitch) {
                    if (isChecked) {
                        onPassSnowAdjustTime = Integer.parseInt(snowAdjustTime.getText().toString());
                    }
                } else if (buttonView.getId() == R.id.windySwitch) {
                    if (isChecked) {
                        onPassWindyAdjustTime = Integer.parseInt(windyAdjustTime.getText().toString());
                    }
                } else if (buttonView.getId() == R.id.stormSwitch) {
                    if (isChecked) {
                        onPassStormAdjustTime = Integer.parseInt(stormAdjustTime.getText().toString());
                    }
                }
            }
        };

        snowOnSwitch.setOnCheckedChangeListener(switchListener);
        windyOnSwitch.setOnCheckedChangeListener(switchListener);
        stormOnSwitch.setOnCheckedChangeListener(switchListener);

        service = new WeatherService(this);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();
        service.refreshWeather("new york, NY");

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void serviceSuccess(Channel channel) {
        dialog.hide();

        Item item = channel.getItem();
        int res = getResources().getIdentifier("drawable/c_" + item.getCondition().getCode(), null, getPackageName());

        Drawable weatherIconDrawable = getResources().getDrawable(res);
        weatherIconImageView.setImageDrawable(weatherIconDrawable);
        temperatureTextView.setText(item.getCondition().getTemperature() + "\u00B0" + channel.getUnits().getTemperature());
        conditionTextView.setText(item.getCondition().getDescription());
        locationTextView.setText(service.getLocation());
    }

    @Override
    public void serviceFailure(Exception exception) {
        dialog.hide();
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }
    @TargetApi(Build.VERSION_CODES.M)
    public void weatherAdjustSaveButton(View view) {

        //WeatherMonitor wm = new WeatherMonitor(onPassSnowAdjustTime, onPassWindyAdjustTime, onPassStormAdjustTime);
        WeatherMonitor wm = WeatherMonitor.getInstance();
        wm.setSnowTime(onPassSnowAdjustTime);
        wm.setStormTime(onPassStormAdjustTime);
        wm.setWindyTime(onPassWindyAdjustTime);

        Intent i = new Intent();
        i.putExtra("weatherMonitor", wm);
        setResult(RESULT_OK, i);

        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        finish();
    }
}
