package com.xyz.dreamtree;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by naman on 14/03/15.
 */
public class MoodActivity extends ActionBarActivity {

    private FrameLayout happy,angry,excited,sad;
    private String mood;

    MediaPlayer mPlayer;
    private TextView text,timet,datet;
    private ImageView image;
    private String data,date,time,moodi,uri;
    public String loadDreamsFromFile() {

        String jsonString = "";
        try {
            String currentLine;
            File cacheFile = new File(getFilesDir(), "memories.json");

            BufferedReader br = new BufferedReader(new FileReader(cacheFile));
            while ((currentLine = br.readLine()) != null) {
                jsonString += currentLine + '\n';
            }

            br.close();

        } catch (IOException e) {
            e.printStackTrace();

        }

        return jsonString;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_mood);

        text = (TextView) findViewById(R.id.text);
        timet=(TextView) findViewById(R.id.timet);
        datet=(TextView) findViewById(R.id.datet);
        image=(ImageView) findViewById(R.id.image);


        try {
            JSONObject response = new JSONObject(loadDreamsFromFile());
            JSONArray dreams = response.getJSONArray("memories");


            Log.d("dssd", response.toString());
            JSONObject object = dreams.getJSONObject(0);


            time = (object.getString("time"));
            data = (object.getString("data"));
            date = (object.getString("date"));
            uri = (object.getString("uri"));
            Log.d("LOL",uri);
            mood = (object.getString("mood"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        text.setText(data);
        timet.setText(time);
        datet.setText(date);

        BitmapFactory.Options options = new BitmapFactory.Options();
        // downsizing image as it throws OutOfMemory Exception for larger
        // images
        options.inSampleSize = 8;
        final Bitmap bitmap = BitmapFactory.decodeFile(Uri.parse(uri).getPath());

        image.setImageBitmap(bitmap);
        
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
    if (mPlayer==null)
        mPlayer = MediaPlayer.create(this, R.raw.happy);
        mPlayer.stop();

    }





}
