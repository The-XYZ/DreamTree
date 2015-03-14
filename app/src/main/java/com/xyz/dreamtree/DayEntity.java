package com.xyz.dreamtree;

/**
 * Created by laavanye on 14/3/15.
 */
public class DayEntity {


    public String imageResId;
    public String date;
    public String data;
    public String mood;
    public String time;


    public DayEntity (String imageResId, String date, String data, String mood, String time){
        this.imageResId = imageResId;
        this.date= date;
        this.data = data;
        this.mood = mood;
        this.time = time;
    }


}
