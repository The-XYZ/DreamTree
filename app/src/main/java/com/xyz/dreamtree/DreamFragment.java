package com.xyz.dreamtree;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.getbase.floatingactionbutton.FloatingActionButton;

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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

/**
 * Created by naman on 13/03/15.
 */
public class DreamFragment extends Fragment {

    private FeatureCoverFlow mCoverFlow;
    private CoverFlowAdapter mAdapter;
    private ArrayList<DreamEntity> mData = new ArrayList<>(0);
    private TextSwitcher mTitle;

    ArrayList<String> timeList = new ArrayList<String>();
    ArrayList<String> dataList = new ArrayList<String>();
    ArrayList<String> dateList = new ArrayList<String>();
    ArrayList<String> uriList = new ArrayList<String>();
    ArrayList<String> moodList = new ArrayList<String>();


    FloatingActionButton createDream;

    public String loadDreamsFromFile() {

        String jsonString = "";
        try {
            String currentLine;
            File cacheFile = new File(getActivity().getFilesDir(), "dreams.json");

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

            InputStream is = getActivity().getAssets().open("dreamsDefault.json");

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

        final View v = inflater.inflate(R.layout.fragment_dream, container, false);


        final File cacheFile = new File(getActivity().getFilesDir(), "dreams.json");

        if (cacheFile.exists()) {

            parseDreams();

        }
        else

        {
            createJsonForFisrtTime();
            parseDreams();

        }


        createDream =(FloatingActionButton)v.findViewById(R.id.fab);
        createDream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), NotifyService.class);
                getActivity().startService(intent);
            }
        });



        mCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),
                       (mData.get(position).data),
                        Toast.LENGTH_SHORT).show();
            }
        });

        mCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                mTitle.setText((mData.get(position).data));
            }

            @Override
            public void onScrolling() {
                mTitle.setText("");
            }
        });




        mTitle = (TextSwitcher) v.findViewById(R.id.title);
        mTitle.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                TextView textView = (TextView) inflater.inflate(R.layout.item_title, null);
                return textView;
            }
        });
        Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_top);
        Animation out = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_bottom);
        mTitle.setInAnimation(in);
        mTitle.setOutAnimation(out);

        mAdapter = new CoverFlowAdapter(getActivity());
        mAdapter.setData(mData);
        mCoverFlow = (FeatureCoverFlow) v.findViewById(R.id.coverflow);




        return v;

    }

    private void parseDreams() {
        try {
            JSONObject response = new JSONObject(loadDreamsFromFile());
            JSONArray dreams = response.getJSONArray("dreams");

//            JSONObject object =dreams.getJSONObject(0);



            for (int i = 1; i < dreams.length(); i++) {

                String time = (dreams.getJSONObject(i).getString("time"));
                String data = (dreams.getJSONObject(i).getString("data"));
                String date = (dreams.getJSONObject(i).getString("date"));
                byte[] uri = new byte[0];
                try {
                    uri = (dreams.getJSONObject(i).getString("uri")).getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String mood = (dreams.getJSONObject(i).getString("mood"));


                mData.add(new DreamEntity(uri, date, data, mood, time ));


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }



        mCoverFlow.setAdapter(mAdapter);


    }
    private void createJsonForFisrtTime() {
        File cacheFile = new File(getActivity().getFilesDir(), "dreams.json");
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
