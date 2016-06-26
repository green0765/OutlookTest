package com.ms.outlook.utils;

import android.util.ArrayMap;

import com.ms.outlook.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventUtils {

    /**
     * Get the Event list of certain day from Server
     * For test I simply create some test data.
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static  List<Event> getEventListByDate(int year, int month, int day){
        ArrayList<Event> eventList =  new ArrayList<Event>();
        if (year == 2016 && month == 5){
            String beginTime = "8:00 PM";
            String timeCost = "2h30m";
            String title = "Team Dinner";
            String location  = "L'aile ou la cuisse";
            String description = "Team Dinner";
            Event event = new Event(beginTime,timeCost,title,location,description);
            eventList.add(event);
        }
        return eventList;
    }

    /**
     * Get the list of days that have events from server
     * For test I simply create some test data.
     * @param year
     * @param month
     * @return
     */
    public static List<Integer> getEventListOfMonth(int year, int month){
        List<Integer> dayList = new ArrayList<Integer>();
        dayList = new ArrayList<Integer>();
        dayList.add(10);
        dayList.add(15);
        return dayList;
    }
}
