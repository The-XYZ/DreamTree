package com.xyz.dreamtree;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by naman on 14/03/15.
 */
public class DayFragment extends Fragment {

    public String loadDreamsFromFile() {

        String jsonString = "";
        try {
            String currentLine;
            File cacheFile = new File(getActivity().getFilesDir(), "memories.json");

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


    public String loadJSONFromAsset() {

        String json = null;
        try {

            InputStream is = getActivity().getAssets().open("memoriesDefault.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_memories, container, false);




        final File cacheFile = new File(getActivity().getFilesDir(), "memories.json");

        if (cacheFile.exists()) {

            parseDreams();

        }
        else

        {
            createJsonForFisrtTime();
            parseDreams();

        }

        return v;

    }

    private void parseDreams() {
        try {
            JSONObject response = new JSONObject(loadDreamsFromFile());
            JSONArray memories = response.getJSONArray("memories");

            JSONObject object =memories.getJSONObject(0);



            for (int i = 0; i < memories.length(); i++) {

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void createJsonForFisrtTime() {
        File cacheFile = new File(getActivity().getFilesDir(), "memories.json");
        String r=loadJSONFromAsset();

        BufferedWriter bw = null;
        try {
            if (!cacheFile.exists()) {
                cacheFile.createNewFile();
            }

            FileWriter fw = new FileWriter(cacheFile.getAbsoluteFile());
            bw = new BufferedWriter(fw);

            if (r!=null) {
                bw.write(r);
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
}
