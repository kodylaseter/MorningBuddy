package com.example.android.networkconnect;

import java.util.ArrayList;

/**
 * Created by kenta on 10/22/2015.
 */
public class User {

    ArrayList<Day> day = new ArrayList<Day>();

    public void addDay(String newDay){
        day.add(new Day(newDay));
    }
    public Day getDay(String dayName){
        for(int i = 0; i < day.size(); i++){
            if(day.get(i).getDayName().contains(dayName)){
                return day.get(i);
            }
        }
        return null;
    }
    public String[] listDays(){
        String[] toRet = new String[day.size()];
        for(int i = 0; i < day.size(); i++){
            toRet[i] = day.get(i).getDayName();
        }
        return toRet;
    }
    public void removeDay(String dayName){
        for(int i = 0; i < day.size(); i++){
            if(dayName == day.get(i).getDayName()){
                day.remove(i);
            }
        }
    }
}