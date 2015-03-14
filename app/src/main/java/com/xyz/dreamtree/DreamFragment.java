package com.xyz.dreamtree;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;

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

        mAdapter = new CoverFlowAdapter(getActivity());
        mCoverFlow = (FeatureCoverFlow) v.findViewById(R.id.coverflow);
        createDream =(FloatingActionButton)v.findViewById(R.id.fab);
//        mTitle = (TextSwitcher) v.findViewById(R.id.title);



        final File cacheFile = new File(getActivity().getFilesDir(), "dreams.json");

        if (cacheFile.exists()) {

            parseDreams();

        }
        else

        {
            createJsonForFisrtTime();
            parseDreams();

        }


        createDream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), NotifyService.class);
                getActivity().startService(intent);

                Intent startAddDream = new Intent(getActivity(), AddDream.class);
                startActivity(startAddDream);
            }
        });



        mCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent =new Intent(getActivity(),DetailDream.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });

        mCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
//                mTitle.setText((mData.get(position).data));
            }

            @Override
            public void onScrolling() {
//                mTitle.setText("");
            }
        });




//        mTitle.setFactory(new ViewSwitcher.ViewFactory() {
//            @Override
//            public View makeView() {
//                LayoutInflater inflater = LayoutInflater.from(getActivity());
//                TextView textView = (TextView) inflater.inflate(R.layout.item_title, null);
//                return textView;
//            }
//        });


//        Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_top);
//        Animation out = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_bottom);
//        mTitle.setInAnimation(in);
//        mTitle.setOutAnimation(out);





        return v;

    }

    private void parseDreams() {
        try {
            JSONObject response = new JSONObject(loadDreamsFromFile());
            JSONArray dreams = response.getJSONArray("dreams");


            Log.d("dssd", response.toString());
//            JSONObject object =dreams.getJSONObject(0);



            for (int i = 0; i < dreams.length(); i++) {

                String time = (dreams.getJSONObject(i).getString("time"));
                String data = (dreams.getJSONObject(i).getString("data"));
                String date = (dreams.getJSONObject(i).getString("date"));
                String uri =(dreams.getJSONObject(i).getString("uri"));

                String mood = (dreams.getJSONObject(i).getString("mood"));


                mData.add(new DreamEntity(uri, date, data, mood, time ));


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        mAdapter.setData(mData);
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


            BitmapFactory.Options options = new BitmapFactory.Options();
            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;
            final Bitmap bitmap = BitmapFactory.decodeFile(Uri.parse(mData.get(position).imageResId).getPath(),
                    options);

            holder.image.setImageBitmap(bitmap);

            holder.date.setText(mData.get(position).date);
        holder.time.setText(mData.get(position).time);
//        holder.mood.setText(mData.get(position).mood);
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
