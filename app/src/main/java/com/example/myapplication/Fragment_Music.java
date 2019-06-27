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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Music extends Fragment implements RvAdapter.OnNoteListener,fragement_theme_individual.themeIndivListener {

    Context context;
    ArrayList<theme_Class> mMusic;
    static  ArrayList<theme_Class> mfavoriteMusic = new ArrayList<>();
    ImageView imageView_Card;
    Fragment theme_Individual;
    Spinner spinnerMenu;
    RvAdapter.OnNoteListener mOnNoteListner = this;


    private static Fragment_Music single_instance = null;
    RecyclerView rv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static Fragment_Music getInstance()
    {
        if (single_instance == null)
            single_instance = new Fragment_Music();

        return single_instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getContext();
        View view = inflater.inflate(R.layout.fragment__music_layout, container, false);
        rv = (RecyclerView)view.findViewById(R.id.rvMusic);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);
        imageView_Card = (ImageView)view.findViewById(R.id.imageView_Card);

        spinnerMenu = view.findViewById(R.id.spinnerMusic);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(context,
                R.array.Music_Menu, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMenu.setAdapter(spinnerAdapter);

        initializeData();

        RvAdapter adapter = new RvAdapter(mMusic, this);

        rv.setAdapter(adapter);

        spinnerMenu.setSelection(0);

        spinnerMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    RvAdapter adapter0 = new RvAdapter(mMusic, mOnNoteListner);
                    rv.setAdapter(adapter0);
                }
                if(position == 1) {
                    RvAdapter adapter1 = new RvAdapter(mfavoriteMusic, mOnNoteListner);
                    rv.setAdapter(adapter1);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;

    }

    private void initializeData() {
        mMusic = new ArrayList<>();

        mMusic.add(new theme_Class("Sky", new int[]{0xff00b7ff, 0xff00ffee}, "Photonlab", "Blue Blue"));
    }

    @Override
    public void onNoteClick(int position) {
//        boolean isFavorite = false;
//        if (position == mMusic.size()) {
//            //do something
//            Log.d("yes", "onNoteClick: success");
//        } else {


    if (spinnerMenu.getSelectedItemPosition() == 0) {
        gotoIndiv(mMusic,position);
//        theme_Class current = mMusic.get(position);
//        if (mfavoriteMusic.contains(current)) {
//            isFavorite = true;
//        }
//        String name = current.getName();
//        int[] gradient = current.getColors();
//        theme_Individual = new fragement_theme_individual(current, isFavorite);
//        ((fragement_theme_individual) theme_Individual).setListener(this);
//        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//        ft.replace(R.id.container, theme_Individual).addToBackStack(null);
//        ft.commit();
    }

    if (spinnerMenu.getSelectedItemPosition() == 1) {
        gotoIndiv(mfavoriteMusic,position);
//        theme_Class current = mfavoriteMusic.get(position);
//        String name = current.getName();
//        int[] gradient = current.getColors();
//        theme_Individual = new fragement_theme_individual(current, true);
//        ((fragement_theme_individual) theme_Individual).setListener(this);
//        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//        ft.replace(R.id.container, theme_Individual).addToBackStack(null);
//        ft.commit();
    }


}


    private  void gotoIndiv(ArrayList<theme_Class> themeList, int position) {
        boolean isFavorite = false;

        if (position == themeList.size()) {
            // the add button
        } else {
            theme_Class current = themeList.get(position);

            if (mfavoriteMusic.contains(current)) {
                isFavorite = true;
            }

            String name = current.getName();
            int[] gradient = current.getColors();
            theme_Individual = new fragement_theme_individual(current, isFavorite);
            ((fragement_theme_individual) theme_Individual).setListener(this);
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            ft.replace(R.id.container, theme_Individual).addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    public theme_Class Addavorite (theme_Class current){
        if(!mfavoriteMusic.contains(current)) {
            mfavoriteMusic.add(current);
        }

        if(spinnerMenu.getSelectedItemPosition() == 1){
            RvAdapter adapterInAddF = new RvAdapter(mfavoriteMusic, this);
            rv.setAdapter(adapterInAddF);
        }

        return current;
    }

    @Override
    public theme_Class RemoveFavorite(theme_Class current){
        mfavoriteMusic.remove(current);
        if(spinnerMenu.getSelectedItemPosition() == 1){
            RvAdapter adapterInRemF = new RvAdapter(mfavoriteMusic, this);
            rv.setAdapter(adapterInRemF);
        }

        return current;
    }

}



