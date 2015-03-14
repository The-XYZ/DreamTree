package com.xyz.dreamtree;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.FrameLayout;

/**
 * Created by naman on 14/03/15.
 */
public class MoodActivity extends ActionBarActivity {

    private FrameLayout happy,angry,excited,sad;
    private String mood;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_mood);
        
        mood=getIntent().getStringExtra("mood");

    }
}
