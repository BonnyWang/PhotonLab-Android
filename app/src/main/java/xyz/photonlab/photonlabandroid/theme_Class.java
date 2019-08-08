package xyz.photonlab.photonlabandroid;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class theme_Class {

    String name;
    String start_Color;
    String end_Color;
    String creater;
    String mood;

    @Exclude
    int startColorInt = Color.RED;

    @Exclude
    int endColorInt = Color.BLUE;

    //TODO:Can delet the gradient drawable file now -Bonny

    public theme_Class() {

    }

    //will be reflect by fireBase store
    public void setStart_Color(String start_Color) {
        this.start_Color = start_Color;
        this.startColorInt = Color.parseColor(start_Color);
    }

    //will be reflect by fireBase store
    public void setEnd_Color(String end_Color) {
        this.end_Color = end_Color;
        this.endColorInt = Color.parseColor(end_Color);
    }

    public theme_Class(String name, int start_Color, int end_Color, String creater, String mood) {
        Log.i("int Constructor", "I was invoked!");
        this.name = name;
        this.startColorInt = start_Color;
        this.endColorInt = end_Color;
        this.creater = creater;
        this.mood = mood;
    }


    public String getName() {
        return name;
    }


    public String getCreater() {
        return creater;
    }

    public String getMood() {
        return mood;
    }

    @Exclude
    public GradientDrawable getGradientDrawablt() {
        GradientDrawable gradientDrawable;
        gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{startColorInt, endColorInt});
        return gradientDrawable;
    }

    @Exclude
    public int[] getColors() {
        int[] colors = {startColorInt, endColorInt};
        return colors;
    }

}


