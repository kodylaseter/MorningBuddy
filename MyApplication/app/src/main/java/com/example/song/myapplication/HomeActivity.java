package com.example.song.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.song.myapplication.adapters.AlarmAdapter;
import com.example.song.myapplication.db.AlarmDBHelper;
import com.example.song.myapplication.models.Alarm;
<<<<<<< HEAD
import com.example.song.myapplication.service.CalendarService;
=======
import com.example.song.myapplication.service.SingleShotLocationProvider;
>>>>>>> 8c5ffb41e098960028638b8e5b35a7684c101037

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    ListView alarmListView;
    private AlarmDBHelper alarmDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        alarmListView = (ListView) findViewById(R.id.alarmListView);
        alarmDBHelper = AlarmDBHelper.getInstance(this);
<<<<<<< HEAD
        ArrayList<Alarm> values = alarmDBHelper.getAlarms();

        AlarmAdapter adapter = new AlarmAdapter(this, values);
=======
        AlarmAdapter adapter = new AlarmAdapter(this, alarmDBHelper.getAlarms());
>>>>>>> 8c5ffb41e098960028638b8e5b35a7684c101037
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.weather_settings) {
            Intent  i = new Intent(this, WeatherActivity.class);
            //startActivity(i);

            //i.putExtra("myobject", wm);
            startActivityForResult(i, 1);
            /**************************************************************************/
           // return result is not used at this moment, can be used for further development
            /**************************************************************************/
            //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1 && resultCode == RESULT_OK) {
//            //int s = data.getIntExtra("testing", 2);
//            Bundle b = data.getExtras();
//            if (b != null) {
//                wm = (WeatherMonitor) b.getSerializable("weatherMonitor");
//                System.out.println(wm.getSnowTime() + ", " + wm.getWindyTime() + ", " + wm.getStormTime());
//            }
//            //Log.d("result", ""+s);
//        }
//    }




        }

        return super.onOptionsItemSelected(item);
    }
}
