package com.example.android.networkconnect;

/**
 * Created by kenta on 10/22/2015.
 */
public class Waypoints {
    private String origin;
    private String destination;
    private String departure_time;
    private String mode;

    public Waypoints(String origin, String destination, String departure_time, String mode){
        this.origin = origin.replace(' ', '+');
        this.destination = destination.replace(' ', '+');
        this.departure_time = departure_time;
        this.mode = mode;
    }
    public void setOrigin(String sOrigin){
        origin = sOrigin.replace(' ', '+');
    }
    public void setDestination(String sDestination){
        destination = sDestination.replace(' ', '+');
    }

    /**
     * Input as Military Time 2400=midnight 1200=noon
     * @param sDT
     */
    public void setDeparture_Time(String sDT){
        departure_time = sDT;
    }
    public String timeConvert(String military){
        return "0";
    }
    public void setMode(String sMode){
        if(!sMode.contains("walking") || !sMode.contains("driving")){
            mode = "driving";
        }else{
            mode = sMode;
        }
    }
    public String getOrigin(){
        return origin;
    }
    public String getDestination(){
        return destination;
    }
    public String getDepature_Time(){
        return departure_time;
    }
    public String getMode(){
        return mode;
    }
    public String getWaypointQuery(){
        String query = "https://maps.googleapis.com/maps/api/distancematrix/json?";
        if(origin != null){
            query += "origins=" + origin;
        }
        if(destination != null){
            query += "&destinations=" + destination;
        }
        if(mode != null){
            query += "&mode=" + mode;
        }
        if(departure_time != null){
            query += "&departure_time" + departure_time;
        }
        return query;
    }
}