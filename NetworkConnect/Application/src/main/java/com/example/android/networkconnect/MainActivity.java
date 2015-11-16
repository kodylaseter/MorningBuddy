/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.networkconnect;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.common.logger.Log;
import com.example.android.common.logger.LogFragment;
import com.example.android.common.logger.LogWrapper;
import com.example.android.common.logger.MessageOnlyLogFilter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * Sample application demonstrating how to connect to the network and fetch raw
 * HTML. It uses AsyncTask to do the fetch on a background thread. To establish
 * the network connection, it uses HttpURLConnection.
 *
 * This sample uses the logging framework to display log output in the log
 * fragment (LogFragment).
 */
public class MainActivity extends FragmentActivity {

    public static final String TAG = "Network Connect";

    // Reference to the fragment showing events, so we can clear it with a button
    // as necessary.
    private LogFragment mLogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_main);

        // Initialize text fragment that displays intro text.
        SimpleTextFragment introFragment = (SimpleTextFragment)
                    getSupportFragmentManager().findFragmentById(R.id.intro_fragment);
        introFragment.setText(R.string.welcome_message);
        introFragment.getTextView().setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16.0f);

        // Initialize the logging framework.
        initializeLogging();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // When the user clicks FETCH, fetch the first 500 characters of
            // raw HTML from www.google.com.
            case R.id.fetch_action:
                new DownloadTask().execute();
                return true;
            // Clear the log view fragment.
            case R.id.clear_action:
              mLogFragment.getLogView().setText("");
              return true;
        }
        return false;
    }

    /**
     * Implementation of AsyncTask, to fetch the data in the background away from
     * the UI thread.
     */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            //Proof of Concept of ugliness.
            double totalTime = 0;
            User ken  = new User();
            //Waypoints newWaypoint = new Waypoints("7 Holly Downs Ct NW", "Georgia Tech", null, "driving"); //start, end, time to go, mode["driving","walking"]
            ken.addDay("Monday"); //this will need to interface with calender api to get the day of week
            ken.getDay("Monday").addWaypoint(new Waypoints("7 Holly Downs Ct NW", "Georgia Tech", null, "driving"));
            ken.getDay("Monday").addWaypoint(new Waypoints("Georgia Tech", "New York New York", null, "driving"));
            int numberOfWayPoints = ken.getDay("Monday").getWaypoints().size();
            for(int i = 0; i < numberOfWayPoints; i++){
                String theUrl = ken.getDay("Monday").wps.get(i).getWaypointQuery();
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
            return df.format(totalTime/60) + " Minutes";
        }

        /**
         * Uses the logging framework to display the output of the fetch
         * operation in the log fragment.
         */
        @Override
        protected void onPostExecute(String result) {
          Log.i(TAG, result);
        }
    }

    /**
     * Given a string representation of a URL, sets up a connection and gets
     * an input stream.
     * @param urlString A string representation of a URL.
     * @return An InputStream retrieved from a successful HttpURLConnection.
     * @throws java.io.IOException
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
    /** Create a chain of targets that will receive log data */
    public void initializeLogging() {

        // Using Log, front-end to the logging chain, emulates
        // android.util.log method signatures.

        // Wraps Android's native log framework
        LogWrapper logWrapper = new LogWrapper();
        Log.setLogNode(logWrapper);

        // A filter that strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        mLogFragment =
                (LogFragment) getSupportFragmentManager().findFragmentById(R.id.log_fragment);
        msgFilter.setNext(mLogFragment.getLogView());
    }
}
