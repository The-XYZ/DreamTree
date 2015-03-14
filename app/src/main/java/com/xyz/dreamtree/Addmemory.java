package com.xyz.dreamtree;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by laavanye on 14/3/15.
 */
public class Addmemory extends ActionBarActivity{




    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    Uri fileUri;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";



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

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                // start the image capture Intent
                startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

//
//
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view

                BitmapFactory.Options options = new BitmapFactory.Options();
                // downsizing image as it throws OutOfMemory Exception for larger
                // images
                options.inSampleSize = 8;
                final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                        options);
                camera.setImageBitmap(bitmap);


            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }


//        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            camera.setImageBitmap(photo);
//
//
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            byte[] imageBytes = baos.toByteArray();
//
//             byteArray = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//        }
    }





    public void writeJSON() {

        Calendar cal = Calendar.getInstance();
        int minute = cal.get(Calendar.MINUTE);

        int hour = cal.get(Calendar.HOUR);
        int dayofmonth = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year =cal.get(Calendar.YEAR);

        JSONObject dream = new JSONObject();
        try {
            dream.put("time", hour+":"+minute);
            dream.put("date", dayofmonth+"-"+month+"-"+year);
            dream.put("data", edit.getText());
            dream.put("mood", "Arts");
            dream.put("uri", fileUri.toString());

        } catch (JSONException e) {

            e.printStackTrace();
        }

        try {
            JSONObject object =new JSONObject(loadDreamsFromFile());
            JSONArray array=object.getJSONArray("memories");
            array.put(dream);
            jsonSting=object.toString();
            Log.d("LOL", jsonSting);

            File cacheFile = new File(getFilesDir(), "memories.json");


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


    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /*
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    /*
     * Here we restore the fileUri again
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }


}
