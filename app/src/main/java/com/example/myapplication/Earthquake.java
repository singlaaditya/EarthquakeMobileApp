package com.example.myapplication;

public class Earthquake {
    private double mMagnitude;
    private String mPlace;
    private Long mTime;
    private String mUrl;

    public Earthquake(double magnitude, String place, Long time, String url){
        mMagnitude = magnitude;
        mPlace = place;
        mTime = time;
        mUrl = url;
    }

    public double getmMagnitude() {
        return mMagnitude;
    }

    public String getmPlace() {
        return mPlace;
    }

    public Long getmTime() {
        return mTime;
    }

    public String getmUrl() {
        return mUrl;
    }
}
