package com.xyz.dreamtree;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoodSelector extends Fragment {



    private FrameLayout happy,angry,excited,sad;

    public MoodSelector() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_mood_selector, container, false);

        happy=(FrameLayout) v.findViewById(R.id.happy);
        happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1=new Intent(getActivity(),MoodActivity.class);
                i1.putExtra("mood","happy");
                startActivity(i1);
            }
        });
        sad=(FrameLayout) v.findViewById(R.id.sad);
        sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2=new Intent(getActivity(),MoodActivity.class);
                i2.putExtra("mood","sad");
                startActivity(i2);
            }
        });
        angry=(FrameLayout) v.findViewById(R.id.angry);
        angry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i3=new Intent(getActivity(),MoodActivity.class);
                i3.putExtra("mood","angry");
                startActivity(i3);
            }
        });
        excited=(FrameLayout) v.findViewById(R.id.excited);
        excited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i4=new Intent(getActivity(),MoodActivity.class);
                i4.putExtra("mood","excited");
                startActivity(i4);
            }
        });
        return v;
    }


}
