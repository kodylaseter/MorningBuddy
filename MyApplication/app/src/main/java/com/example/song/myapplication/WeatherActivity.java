package com.example.song.myapplication;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
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
import com.example.song.myapplication.models.Alarm;
import com.example.song.myapplication.service.SingleShotLocationProvider;
import com.example.song.myapplication.service.WeatherService;
import com.example.song.myapplication.service.WeatherServiceCallback;

public class WeatherActivity extends AppCompatActivity {

    private EditText snowEditText;
    private EditText windyEditText;
    private EditText stormEditText;
    private EditText rainEditText;

    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        snowEditText = (EditText)findViewById(R.id.snowEditText);
        snowEditText.setText(String.valueOf(WeatherMonitor.getInstance().getSnowTime()));
        windyEditText = (EditText)findViewById(R.id.windEditText);
        windyEditText.setText(String.valueOf(WeatherMonitor.getInstance().getWindyTime()));
        stormEditText = (EditText)findViewById(R.id.stormEditText);
        stormEditText.setText(String.valueOf(WeatherMonitor.getInstance().getStormTime()));
        rainEditText = (EditText)findViewById(R.id.rainEditText);
        rainEditText.setText(String.valueOf(WeatherMonitor.getInstance().getRainTime()));
        saveButton = (Button)findViewById(R.id.saveButton);

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
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return true;
    }


    public void weatherAdjustSaveButton(View view) {
        WeatherMonitor wm = WeatherMonitor.getInstance();
        int snowTime = Integer.parseInt(snowEditText.getText().toString());
        int stormTime = Integer.parseInt(stormEditText.getText().toString());
        int windTime = Integer.parseInt(windyEditText.getText().toString());
        int rainTime = Integer.parseInt(rainEditText.getText().toString());
        int buffer = Alarm.TRAFFIC_BUFFER_TIME - 5;
        if (snowTime > buffer || stormTime > buffer || windTime > buffer || rainTime > buffer) {
            Toast.makeText(this, "Weather delay times cannot exceed " + buffer + " minutes.", Toast.LENGTH_SHORT).show();
        } else {
            wm.setSnowTime(snowTime);
            wm.setStormTime(stormTime);
            wm.setWindyTime(windTime);
            wm.setRainTime(rainTime);
            finish(); //terminate current screen and return to previous screen
        }

    }
}
