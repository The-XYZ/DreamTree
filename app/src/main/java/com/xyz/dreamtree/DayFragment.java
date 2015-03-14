package com.xyz.dreamtree;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;

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
import java.util.ArrayList;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

/**
 * Created by naman on 14/03/15.
 */
public class DayFragment extends Fragment {

    private FeatureCoverFlow mCoverFlow;
    private CoverFlowAdapter mAdapter;
    private ArrayList<DreamEntity> mData = new ArrayList<>(0);
    private TextSwitcher mTitle;

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

        ActionBarActivity activity = (ActionBarActivity) getActivity();
        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8BC34A")));

//        mData.add(new DreamEntity(R.drawable.image_1, R.string.title_activity_check));
//        mData.add(new DreamEntity(R.drawable.image_2, R.string.title_activity_check));
//        mData.add(new DreamEntity(R.drawable.image_3, R.string.title_activity_check));
//        mData.add(new DreamEntity(R.drawable.image_4, R.string.title_activity_check));

        //mTitle = (TextSwitcher) v.findViewById(R.id.title);
//        mTitle.setFactory(new ViewSwitcher.ViewFactory() {
//            @Override
//            public View makeView() {
//                LayoutInflater inflater = LayoutInflater.from(getActivity());
//                TextView textView = (TextView) inflater.inflate(R.layout.item_title, null);
//                return textView;
//            }
//        });
        Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_top);
        Animation out = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_bottom);
        mTitle.setInAnimation(in);
        mTitle.setOutAnimation(out);

        mAdapter = new CoverFlowAdapter(getActivity());
        mAdapter.setData(mData);
        mCoverFlow = (FeatureCoverFlow) v.findViewById(R.id.coverflow);
        mCoverFlow.setAdapter(mAdapter);

//        mCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(),
//                        getResources().getString(mData.get(position).titleResId),
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        mCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
//            @Override
//            public void onScrolledToPosition(int position) {
//                mTitle.setText(getResources().getString(mData.get(position).titleResId));
//            }
//
//            @Override
//            public void onScrolling() {
//                mTitle.setText("");
//            }
//        });



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





    public class CoverFlowAdapter extends BaseAdapter {

        private ArrayList<DreamEntity> mData = new ArrayList<>(0);
        private Context mContext;

        public CoverFlowAdapter(Context context) {
            mContext = context;
        }

        public void setData(ArrayList<DreamEntity> data) {
            mData = data;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int pos) {
            return mData.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View rowView = convertView;

            if (rowView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.item_coverflow, null);

                ViewHolder viewHolder = new ViewHolder();
                viewHolder.data = (TextView) rowView.findViewById(R.id.data);
                viewHolder.image = (ImageView) rowView.findViewById(R.id.image);
                viewHolder.date = (TextView) rowView.findViewById(R.id.date);
                viewHolder.time = (TextView) rowView.findViewById(R.id.time);
//                viewHolder.mood = (TextView) rowView.findViewById(R.id.mood);


                rowView.setTag(viewHolder);
            }

            ViewHolder holder = (ViewHolder) rowView.getTag();

            holder.image.setImageBitmap(BitmapFactory.decodeByteArray(mData.get(position).imageResId, 0, mData.get(position).imageResId.length));
            holder.date.setText(mData.get(position).date);
            holder.time.setText(mData.get(position).time);
            holder.mood.setText(mData.get(position).mood);
            holder.data.setText(mData.get(position).data);


            return rowView;
        }


        public class ViewHolder {
            public TextView date;
            public ImageView image;
            public TextView time;
            public TextView mood ;
            public TextView data;
        }
    }


}
