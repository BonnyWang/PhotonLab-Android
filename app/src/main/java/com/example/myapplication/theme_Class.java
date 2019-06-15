package com.example.myapplication;

import android.graphics.drawable.GradientDrawable;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class theme_Class {

        String name;
        int[] colors;
        GradientDrawable gradientDrawable;
        String creater;
        String mood;

        //TODO:Can delet the gradient drawable file now -Bonny



    public theme_Class(String name, int[] colors, String creater, String mood){

        this.name = name;
        this.colors = colors;
        gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);

        this.creater = creater;
        this.mood = mood;
    }

    public String getName() {
        return name;
    }

    public int[] getColors() {
        return colors;
    }

    public String getCreater() {
        return creater;
    }

    public String getMood(){
        return mood;
    }
}


