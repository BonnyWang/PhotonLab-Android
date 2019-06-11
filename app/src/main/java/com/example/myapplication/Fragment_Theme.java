package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Theme extends Fragment {

    Context context;
    List<theme_Class> mtheme;
    ImageView imageView_Card;
    //RecyclerView rv = (RecyclerView)getView().findViewById(R.id.rv);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__theme_layout, container, false);
        RecyclerView rv = (RecyclerView)view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);
        imageView_Card = (ImageView)view.findViewById(R.id.imageView_Card);

        initializeData();

        RvAdapter adapter = new RvAdapter(mtheme);
        rv.setAdapter(adapter);
        return view;

    }

    private void initializeData(){
        mtheme = new ArrayList<>();
        mtheme.add(new theme_Class("Spring", new int[] {0xff009e00, 0xfffcee21}));
        mtheme.add(new theme_Class("Fizzy Peach", new int[] {0xfff24645, 0xffebc08d}));
        mtheme.add(new theme_Class("Sky", new int[] {0xff00b7ff, 0xff00ffee}));
        mtheme.add(new theme_Class("Spring", new int[] {0xff009e00, 0xfffcee21}));
        mtheme.add(new theme_Class("Fizzy Peach", new int[] {0xfff24645, 0xffebc08d}));
        mtheme.add(new theme_Class("Sky", new int[] {0xff00b7ff, 0xff00ffee}));
    }


}
