package com.xyz.dreamtree;

import android.net.Uri;

/**
 * Created by yogesh on 14/3/15.
 */


public class DreamEntity {
    public String imageResId;
    public String date;
    public String data;
    public String mood;
    public String time;


    public DreamEntity (String imageResId, String date, String data, String mood, String time){
        this.imageResId = imageResId;
        this.date= date;
        this.data = data;
        this.mood = mood;
        this.time = time;
    }
}
