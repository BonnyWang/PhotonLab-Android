package xyz.photonlab.photonlabandroid;

import android.graphics.drawable.GradientDrawable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class theme_Class {

        String name;
        int start_Color;
        int end_Color;
        String creater;
        String mood;

        //TODO:Can delet the gradient drawable file now -Bonny

    public theme_Class(){

    }



    public theme_Class(String name, int start_Color, int end_Color, String creater, String mood){

        this.name = name;
        this.start_Color = start_Color;
        this.end_Color = end_Color;

        this.creater = creater;
        this.mood = mood;
    }


    public String getName() {
        return name;
    }




    public String getCreater() {
        return creater;
    }

    public String getMood(){
        return mood;
    }

    @Exclude
    public GradientDrawable getGradientDrawablt(){
        GradientDrawable gradientDrawable;
        gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{start_Color,end_Color});
        return gradientDrawable;
    }

    @Exclude
    public int[] getColors() {
        int[] colors = {start_Color,end_Color};
        return colors ;
    }
}


