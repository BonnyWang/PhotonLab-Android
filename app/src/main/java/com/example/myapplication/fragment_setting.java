package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;


public class fragment_setting extends Fragment implements SettingRvAdapter.OnNoteListener {

    Context context;
    List<setting_Content> mSettings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        final RecyclerView rv = (RecyclerView)view.findViewById(R.id.setRV);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);

        initializeData();

        SettingRvAdapter adapter = new SettingRvAdapter(mSettings, this);

        rv.setAdapter(adapter);
        return view;
    }

    public void initializeData(){
        mSettings = new ArrayList<>();
        mSettings.add(new setting_Content("Pairing"));
        mSettings.add(new setting_Content("Feedback"));
        mSettings.add(new setting_Content("Night Light"));
        mSettings.add(new setting_Content("Clock"));
        mSettings.add(new setting_Content("IoT API"));
        mSettings.add(new setting_Content("Layout Manager"));
        mSettings.add(new setting_Content("Update Firmware"));

    }

    @Override
    public void onNoteClick(int position) {
        switch (position) {
            case 0:
                fragment_Pair fragment_pair = new fragment_Pair();
                FragmentTransaction ft0 = getActivity().getSupportFragmentManager().beginTransaction();
                ft0.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                ft0.replace(R.id.fgm,fragment_pair).addToBackStack(null);
                ft0.commit();
                break;


        }

    }



}
