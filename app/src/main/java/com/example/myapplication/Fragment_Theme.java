package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.myapplication.data.Theme_Info;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Theme extends Fragment implements RvAdapter.OnNoteListener {

    Context context;
    ImageView imageView_Card;
    Fragment fragment_Useless;
    List<theme_Class> mtheme = new Theme_Info().classlist();
//    List<theme_Class>mtheme;
    //hello bonny
    //RecyclerView rv = (RecyclerView)getView().findViewById(R.id.rv);


//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//
//    }

    @Override
    public void onNoteClick(int position) {
        List<theme_Class> mtheme = new Theme_Info().classlist();
        theme_Class theme_class = mtheme.get(position);
        String a=theme_class.getThemeName();
        int[] b = theme_class.getColorCode();
        fragment_Useless = new Fragment_Useless(b,a);
        FragmentTransaction ft3 = getChildFragmentManager().beginTransaction();
        ft3.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        ft3.replace(R.id.fgm, fragment_Useless);
        ft3.commit();
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
//        initializeData();
        RvAdapter adapter = new RvAdapter(mtheme,this);
        rv.setAdapter(adapter);
        return view;


    }

//
//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
//        theme_Class theme_class = mtheme.get(position);
//        theme_class.getThemeName();
//        Fragment useless = new Fragment_Useless();
//        FragmentTransaction fm = getChildFragmentManager().beginTransaction();
//        fm.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//        fm.replace(R.id.my_container,useless);
//        fm.commit();
//    }

//        private void initializeData(){
//        mtheme = new ArrayList<>();
//        mtheme.add(new theme_Class("Spring", new int[] {0xff009e00, 0xfffcee21}));
//        mtheme.add(new theme_Class("Fizzy Peach", new int[] {0xfff24645, 0xffebc08d}));
//        mtheme.add(new theme_Class("Sky", new int[] {0xff00b7ff, 0xff00ffee}));
//        mtheme.add(new theme_Class("Spring", new int[] {0xff009e00, 0xfffcee21}));
//        mtheme.add(new theme_Class("Fizzy Peach", new int[] {0xfff24645, 0xffebc08d}));
//        mtheme.add(new theme_Class("Sky", new int[] {0xff00b7ff, 0xff00ffee}));
//    }



}
