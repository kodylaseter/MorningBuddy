package com.example.song.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.song.myapplication.adapters.AlarmAdapter;
import com.example.song.myapplication.db.AlarmDBHelper;

public class HomeActivity extends AppCompatActivity {

    ListView alarmListView;
    private AlarmDBHelper alarmDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //Toast.makeText(this, " is Enable in your device", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(this, "GPS is not Enable in your device", Toast.LENGTH_SHORT).show();
            enableGLSAlert(); //enable google's location service to pin down current location
        }


        alarmListView = (ListView) findViewById(R.id.alarmListView);
        alarmDBHelper = AlarmDBHelper.getInstance(this);


        AlarmAdapter adapter = new AlarmAdapter(this, alarmDBHelper.getAlarms());
        alarmListView.setAdapter(adapter);
    }

    public void newAlarmButtonClicked (View view) {
        Intent intent = new Intent(this, NewAlarmActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.weather_settings) {
            Intent  i = new Intent(this, WeatherActivity.class);
            startActivityForResult(i, 1);
        } else if (id == R.id.view_database) {
            startActivity(new Intent(this, AndroidDatabaseManager.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void enableGLSAlert() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setMessage("Google's Location Service is disabled in your device. Would you like to enable it?")
            .setCancelable(false)
            .setPositiveButton("Enable Location Service", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent GPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(GPSSettingIntent);
                }
            });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = adb.create();
        alert.show();
    }
}
