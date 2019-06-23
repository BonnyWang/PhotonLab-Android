package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Music extends Fragment implements RvAdapter.OnNoteListener {

    Context context;
    List<theme_Class> mMusic;
    ImageView imageView_Card;
    Fragment theme_Individual;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__music_layout, container, false);
        final RecyclerView rv = (RecyclerView)view.findViewById(R.id.rvMusic);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);
        imageView_Card = (ImageView)view.findViewById(R.id.imageView_Card);

        initializeData();

        RvAdapter adapter = new RvAdapter(mMusic, this);

        rv.setAdapter(adapter);
        return view;

    }

    private void initializeData() {
        mMusic = new ArrayList<>();

        mMusic.add(new theme_Class("Sky", new int[]{0xff00b7ff, 0xff00ffee}, "Photonlab", "Blue Blue"));
    }

    @Override
    public void onNoteClick(int position) {
        if (position == mMusic.size()){
            //do something
            Log.d("yes", "onNoteClick: success");
        }
        else{
            theme_Class current = mMusic.get(position);
            String name = current.getName();
            int[] gradient = current.getColors();
            theme_Individual = new fragement_theme_individual(current);
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            ft.replace(R.id.container, theme_Individual).addToBackStack(null);
            ft.commit();}
    }

}



