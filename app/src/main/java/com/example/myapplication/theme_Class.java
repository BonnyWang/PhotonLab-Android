package com.example.myapplication;

import android.graphics.drawable.GradientDrawable;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class theme_Class {

        String name;
        int[] colors;
        GradientDrawable gradientDrawable;
        //TODO:Can delet the gradient drawable file now -Bonny



    public theme_Class(String name, int[] colors){

        this.name = name;
        this.colors = colors;
        gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
    }

    public String getThemeName(){
        return name;
    }

    public int[] getColorCode(){
        return colors;
    }



}


