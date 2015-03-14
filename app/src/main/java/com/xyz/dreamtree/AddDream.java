package com.xyz.dreamtree;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Calendar;

/**
 * Created by naman on 14/03/15.
 */
public class AddDream extends ActionBarActivity {

    private BootstrapEditText edit;
    private BootstrapButton save,takePic;
    private String jsonSting;
    private ImageView camera;
    private static final int CAMERA_REQUEST = 1888;
    private String byteArray;
    public String loadDreamsFromFile() {

        String jsonString = "";
        try {
            String currentLine;
            File cacheFile = new File(getFilesDir(), "dreams.json");

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
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.home);
        edit=(BootstrapEditText) findViewById(R.id.edit);
        save=(BootstrapButton) findViewById(R.id.save);
        takePic=(BootstrapButton) findViewById(R.id.takepic);
        camera=(ImageView) findViewById(R.id.camera);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                writeJSON();
                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            camera.setImageBitmap(photo);
            int bytes = photo.getByteCount();
            ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
            photo.copyPixelsToBuffer(buffer);
            byte[] array = buffer.array();
            byteArray=array.toString();

        }
    }

    public void writeJSON() {

        Calendar cal = Calendar.getInstance();
        int minute = cal.get(Calendar.MINUTE);

        int hour = cal.get(Calendar.HOUR);
        int dayofmonth = cal.get(Calendar.DAY_OF_MONTH);

        JSONObject dream = new JSONObject();
        try {
            dream.put("time", hour+":"+minute);
            dream.put("date", dayofmonth);
            dream.put("data", edit.getText());
            dream.put("mood", "Arts");
            dream.put("uri", byteArray);

        } catch (JSONException e) {

            e.printStackTrace();
        }

        try {
            JSONObject object =new JSONObject(loadDreamsFromFile());
            JSONArray array=object.getJSONArray("dreams");
            array.put(dream);
            jsonSting=object.toString();
           Log.d("LOL",jsonSting);

            File cacheFile = new File(getFilesDir(), "dreams.json");


            BufferedWriter bw = null;
            try {
                if (!cacheFile.exists()) {
                    cacheFile.createNewFile();
                }

                FileWriter fw = new FileWriter(cacheFile.getAbsoluteFile());
                bw = new BufferedWriter(fw);

                if (jsonSting!=null) {
                    bw.write(jsonSting);
                }

            } catch (Exception e){
                e.printStackTrace();

            } finally {
                try {
                    bw.close();
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }

        }

        catch (JSONException e){
            e.printStackTrace();
        }

    }
}
