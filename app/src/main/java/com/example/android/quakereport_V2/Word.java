package com.example.android.quakereport_V2;

import static com.example.android.quakereport_V2.R.id.mag;

/**
 * Created by ayush2009 on 29/3/17.
 */

public class Word {

    private Double mMag;

    private String mPlace;

    private Long mTimeInMilliseconds;

    private String mUrl;

    private Double mLat;

    private Double mLongi;

    public Word(Double mag, String place, Long timeInMilliseconds,String url,Double lat,Double longi)
    {
        mMag=mag;
        mPlace=place;
        mTimeInMilliseconds=timeInMilliseconds;
        mUrl=url;
        mLat=lat;
        mLongi=longi;
    }

    public Double getMag(){
        return mMag;
    }
    public String getPlace(){ return mPlace; }
    public Long getTimeInMilliseconds(){
        return mTimeInMilliseconds;
    }
    public String getUrl(){
        return mUrl;
    }
    public Double getLat(){ return mLat; }
    public Double getLongi(){ return mLongi; }
}
