package com.example.myapplication;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class fragement_theme_individual extends Fragment {
    List<theme_Content_Class> items =new ArrayList<>();
    int[] gradient;
    String themeName;
    GradientDrawable topCircle_Background, setButton_Background;
    ImageView topCircle;
    TextView title;
    Button setButton;
    theme_Class mtheme;
    Button backButton;

    public fragement_theme_individual(theme_Class mtheme){
        this.gradient = mtheme.getColors();
        this.themeName = mtheme.getName();
        this.mtheme = mtheme;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) { ;
        View view = inflater.inflate(R.layout.fragement_theme_individual_layout, container, false);
        ListView lv = (ListView)view.findViewById(R.id.info_list) ;

        initializeData();
        theme_Content_Adapter adapter = new theme_Content_Adapter(getActivity(),R.layout.theme_info_item,items);
        lv.setAdapter(adapter);

        topCircle = (ImageView)view.findViewById(R.id.topCircle);
        topCircle_Background = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradient);
        topCircle_Background.setShape(GradientDrawable.OVAL);
        topCircle.setImageDrawable(topCircle_Background);

        title = view.findViewById(R.id.themeName);
        title.setText(themeName);

        setButton = view.findViewById(R.id.setbutton);
        setButton_Background = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradient);
        setButton_Background.setShape(GradientDrawable.RECTANGLE);
        setButton_Background.setCornerRadius(50);
        setButton_Background.setSize(1000,50);
        setButton.setBackground(setButton_Background);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Data Transfer Back-End -Bonny
            }
        });


        backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    private void initializeData(){
        items = new ArrayList<>();
        items.add(new theme_Content_Class("Creator",mtheme.getCreater()));
        items.add(new theme_Content_Class("Mood",mtheme.getMood()));
    }


}
