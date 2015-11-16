package com.example.android.networkconnect;

/**
 * Created by kenta on 10/23/2015.
 */

import java.util.ArrayList;
import java.util.List;

public class Day {

    String dayName;
    List<Waypoints> wps = new ArrayList<Waypoints>();
    //each day may have different waypoints
    int alarmHour;
    int alarmMinute;

    public Day(String theDay) {
        dayName = theDay;
    }

    public void setDayName(String theDay) {
        dayName = theDay;
    }

    /**
     * Returns the day name.
     *
     * @return the day name.
     */
    public String getDayName() {
        return dayName;
    }

    /**
     * Adds a whole list of waypoints for the day, previous waypoints are lost.
     *
     * @param newwps a list of waypoints to add for the day.
     */
    public void setWaypoints(List<Waypoints> newwps) {
        wps.clear();
        wps = newwps;
    }

    /**
     * Adds a waypoint to the list of waypoints for the day.
     *
     * @param newwp a waypoint object to add.
     */
    public void addWaypoint(Waypoints newwp) {
        wps.add(newwp);
    }

    /**
     * Gets all the waypoints in a list for the specified day.
     *
     * @return a list containing all the waypoints for the day.
     */
    public List<Waypoints> getWaypoints() {
        return wps;
    }

    /**
     * Waypoints are in sequential order, this returns which waypoint to recover.
     *
     * @param index the waypoint number of the day you wish to get
     * @return the waypoint object specified by the index.
     */
    public Waypoints getWaypoint(int index) {
        return wps.get(index);
    }

    /**
     * Clears all the waypoints for the day.
     */
    public void clearWaypoints() {
        wps.clear();
    }

    /**
     * This calculates the time from origin to destination of all the waypoints.
     *
     * @return the total time for the specified day to travel
     */
//    public double calcTrafficTime() {
//        double totalTime = 0;
//
//        for (int i = 0; i < wps.size(); i++) {
//            InputStream inputStream = null;
//            String json = "";
//
//            try {
//                HttpClient client = new DefaultHttpClient();
//                HttpPost post = new HttpPost(wps.get(i).getWaypointQuery());
//                HttpResponse response = client.execute(post);
//                HttpEntity entity = response.getEntity();
//                inputStream = entity.getContent();
//
//                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
//                StringBuilder sbuild = new StringBuilder();
//                String line = null;
//                while ((line = reader.readLine()) != null) {
//                    sbuild.append(line);
//                }
//                inputStream.close();
//                json = sbuild.toString();
//            } catch (Exception e) {
//            }
//
//            try {
//                JSONObject json1 = new JSONObject(json);
//                JSONArray rows = json1.getJSONArray("rows");
//                for (int k = 0; k < rows.length(); k++) { //traverse the rows to find elements...
//                    JSONObject obj = rows.getJSONObject(k);
//                    JSONArray elements = obj.getJSONArray("elements");
//                    for (int j = 0; j < elements.length(); j++) {
//                        JSONObject elem = elements.getJSONObject(j);
//                        JSONObject duration = elem.getJSONObject("duration");
//                        String temp = duration.getString("value");
//                        double toAdd = Double.parseDouble(temp);
//                        totalTime += (toAdd / 60);
//                        //System.out.println(duration.getString("value"));
//                    }
//                }
//            } catch (Exception e) {
//            }
//        }
//        return totalTime;
////repo1.maven.org/maven2/org/codehaus/jettison/jettison/1.3.3/jettison-1.3.3.jar
//    }
}
