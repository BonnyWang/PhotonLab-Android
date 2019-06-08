package com.example.myapplication;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class theme_Class {
    public class theme{
        String name;

        public theme(String name){
            this.name = name;
        }
    }

    public theme_Class(){

    }

    private List<theme> mtheme;

    private void initializeData(){
        mtheme = new ArrayList<>();
        mtheme.add(new theme("Spring"));

    }




}


