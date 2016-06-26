package com.ms.outlook;

/**
 * Created by green on 2016/6/23.
 */
public class Event {
    private String mBeginTime;
    private String mTimeCost;
    private String mTitle;
    private String mLocation;
    private String mDescription;

    public Event(String mBeginTime, String mTimeCost, String mTitle, String mLocation, String mDescription) {
        this.mBeginTime = mBeginTime;
        this.mTimeCost = mTimeCost;
        this.mTitle = mTitle;
        this.mLocation = mLocation;
        this.mDescription = mDescription;
    }


    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getBeginTime() {
        return mBeginTime;
    }

    public void setBeginTime(String mBeginTime) {
        this.mBeginTime = mBeginTime;
    }

    public String getTimeCost() {
        return mTimeCost;
    }

    public void setTimeCost(String mTimeCost) {
        this.mTimeCost = mTimeCost;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String mLocation) {
        this.mLocation = mLocation;
    }
}
