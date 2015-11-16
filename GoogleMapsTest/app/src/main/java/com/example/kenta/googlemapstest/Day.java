package com.example.kenta.googlemapstest;

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


}
