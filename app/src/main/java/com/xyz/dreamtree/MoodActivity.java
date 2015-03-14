package com.xyz.dreamtree;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.FrameLayout;

/**
 * Created by naman on 14/03/15.
 */
public class MoodActivity extends ActionBarActivity {

    private FrameLayout happy,angry,excited,sad;
    private String mood;

    MediaPlayer mPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_mood);
        
        mood=getIntent().getStringExtra("mood");

        if(mood.equals("happy"))
        {
            mPlayer = MediaPlayer.create(this, R.raw.happy);
            mPlayer.start();
        }

        if(mood.equals("angry"))
        {
            MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.angry);
            mPlayer.start();
        }

        if(mood.equals("sad"))
        {
            MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.sad);
            mPlayer.start();
        }

        if(mood.equals("excited"))
        {
            MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.excited);
            mPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
    }


}
