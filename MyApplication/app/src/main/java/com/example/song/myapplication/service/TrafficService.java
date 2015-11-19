package com.example.song.myapplication.service;

import android.os.AsyncTask;

import com.example.song.myapplication.NewAlarmActivity;
import com.example.song.myapplication.logger.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Kody on 11/16/2015.
 */
public class TrafficService {

    private static TrafficService trafficService;

    public static TrafficService getInstance() {
        if (trafficService == null) trafficService = new TrafficService();
        return trafficService;
    }

    public void getTimeEstimate(String origin, String destination, NewAlarmActivity a) {
        new DownloadTask(origin, destination, a).execute();
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        String start;
        String end;
        NewAlarmActivity activity;
        private DownloadTask(String start, String end, NewAlarmActivity activity) {
            this.start = start;
            this.end = end;
            this.activity = activity;
        }

        @Override
        protected String doInBackground(String... urls) {
            //Proof of Concept of ugliness.
            double totalTime = 0;
            String theUrl = getQuery(start, end);
            InputStream inputStream = null;
            String json = "";
            String time = "";
            try{
                inputStream = downloadUrl(theUrl);
                json = readStream(inputStream);
                time = json_getTime(json);
                //totalTime += Double.parseDouble(time); //I need to get my types figured out between string and double.
            }catch(IOException e){
                Log.w("connectionError", e.getLocalizedMessage());
            }

            //Formats Decimal just in case of floating point.
            return time;
        }

        /**
         * Uses the logging framework to display the output of the fetch
         * operation in the log fragment.
         */
        @Override
        protected void onPostExecute(String result) {
            float res = Float.parseFloat(result);
            activity.finishAddAlarm(res);
        }
    }

    public String getQuery(String origin, String destination) {
        String query = "https://maps.googleapis.com/maps/api/distancematrix/json?";
        if(origin != null){
            query += "origins=" + origin.replace(" ", "+");
        }
        if(destination != null){
            query += "&destinations=" + destination.replace(" ", "+");
        }
        return query;
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
