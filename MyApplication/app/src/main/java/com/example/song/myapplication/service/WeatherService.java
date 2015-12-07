package com.example.song.myapplication.service;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import android.util.Log;

import com.example.song.myapplication.data.Channel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by song on 9/29/2015 0029.
 */
public class WeatherService {
    private WeatherServiceCallback callback;
    private String location;
    private Exception error;

    public WeatherService(WeatherServiceCallback callback) {
        this.callback = callback;
    }

    public String getLocation() {
        return location;
    }

    /**
     * Query method for weather information based on location from Yahoo!Weather
     * @param l, location
     */
    public void refreshWeather(String l) {
        this.location = l;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                Log.d("location", strings[0]);
                //query statement based on location
                String query = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text in (select line2 from geo.placefinder where text=\"%s\" and gflags=\"R\"))", strings[0]);
                //API uri address
                String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(query));

                try {
                    //get returning weather information from Yahoo weather server
                    URL url = new URL(endpoint);
                    URLConnection conn = url.openConnection();
                    InputStream inputStream = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;
                    //parse the information
                    while((line=reader.readLine()) != null) {
                        result.append(line);
                    }

                    return result.toString();
                } catch (Exception e) {
                    error = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                if (s == null && error != null) {
                    callback.serviceFailure(error);
                    return;
                }
                try {
                    JSONObject data = new JSONObject(s);    //return result as JSON for further parsing
                    JSONObject queryResult = data.optJSONObject("query");   //return result from server
                    int count = queryResult.optInt("count");    //if successfully fetch data from yahoo weather server, 0 indicates fail fetching
                    if (count == 0) {
                        callback.serviceFailure(new LocationWeatherException("No Weather information found for " + location));
                        return;
                    }
                    //store weather information into channel for further parsing, such as weather condition, temperature
                    Channel channel = new Channel();
                    channel.populate(queryResult.optJSONObject("results").optJSONObject("channel")); //return queried results
                    callback.serviceSuccess(channel);
                } catch (JSONException e) {
                    callback.serviceFailure(e);
                }
            }
        }.execute(location);
    }

    /**
     * if error occurs while fetching weather data from the server
     */
    public class LocationWeatherException extends Exception {
        public LocationWeatherException(String detailMessage) {
            super(detailMessage);
        }
    }

}
