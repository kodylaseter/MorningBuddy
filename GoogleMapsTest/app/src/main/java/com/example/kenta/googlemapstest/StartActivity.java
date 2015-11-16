package com.example.kenta.googlemapstest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class StartActivity extends AppCompatActivity {
    private EditText destination_coordinates;
    private EditText start_coordinates;
    private EditText day;
    private User ken = new User();
    private String theDay;
    private TextView timeThing;
    private String theTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setUp();
        destination_coordinates= (EditText)findViewById(R.id.destloc);
        day =(EditText)findViewById(R.id.dayid);
        start_coordinates = (EditText)findViewById(R.id.startloc);
        timeThing = (TextView)findViewById(R.id.textView4);

        Button pinpointMaps = (Button) findViewById(R.id.button);
        pinpointMaps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), MapsActivity.class);
                startActivityForResult(myIntent, 1);
            }
        });

        Button addWaypoint = (Button) findViewById(R.id.button2);
        addWaypoint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String daytxt=day.getText().toString();
                theDay = daytxt;
                String starttxt = start_coordinates.getText().toString();
                String desttxt = destination_coordinates.getText().toString();
                ken.getDay(daytxt).addWaypoint(new Waypoints(starttxt, desttxt, null, "driving"));
                start_coordinates.setText(desttxt);
                destination_coordinates.setText("");
            }
        });

        Button timeButton = (Button) findViewById(R.id.button3);
        timeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new DownloadTask().execute();
                timeThing.setText(theTime);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String stredittext = data.getStringExtra("edittextvalue");
                destination_coordinates.setText(stredittext);
            }
        }
    }

    /**
     * Lazy hack shit yolo no scope 360 headshot
     */
    private void setUp(){
        ken.addDay("Monday");
        ken.addDay("Tuesday");
        ken.addDay("Wednesday");
        ken.addDay("Thursday");
        ken.addDay("Friday");
        ken.addDay("Saturday");
        ken.addDay("Sunday");
    }
    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            //Proof of Concept of ugliness.
            double totalTime = 0;
            int numberOfWayPoints = ken.getDay(theDay).getWaypoints().size();
            for(int i = 0; i < numberOfWayPoints; i++){
                String theUrl = ken.getDay(theDay).wps.get(i).getWaypointQuery();
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
            }
            //Formats Decimal just in case of floating point.
            DecimalFormat df = new DecimalFormat("#,###,##0.00");
            theTime = df.format(totalTime/60) + " Minutes";
            return df.format(totalTime/60) + " Minutes";
        }

        /**
         * Uses the logging framework to display the output of the fetch
         * operation in the log fragment.
         */
        @Override
        protected void onPostExecute(String result) {
            timeThing.setText(result);
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
}
