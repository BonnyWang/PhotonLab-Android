package xyz.photonlab.photonlabandroid.model;

import android.graphics.drawable.GradientDrawable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * created by KIO on 2019/8/28
 */
@IgnoreExtraProperties
public class MyTheme implements Comparable<MyTheme> {

    private String creater;
    private String mood;
    private String name;
    private int number;
    private int[] gradientColors;
    private int[] vars;

    @Exclude
    private boolean favorite;

    @Exclude
    private boolean music;

    public MyTheme() {

    }

    public MyTheme(String creater, String mood, String name, int number, int[] gradientColors, int[] vars, boolean favorite, boolean music) {
        this.creater = creater;
        this.mood = mood;
        this.name = name;
        this.number = number;
        this.gradientColors = gradientColors;
        this.vars = vars;
        this.music = music;
        this.favorite = favorite;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int[] getGradientColors() {
        return gradientColors;
    }

    public void setGradientColors(int[] gradientColors) {
        this.gradientColors = gradientColors;
    }

    public int[] getVars() {
        return vars;
    }

    public void setVars(int[] vars) {
        this.vars = vars;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public GradientDrawable getGradientDrawable() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColors(gradientColors);
        drawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        return drawable;
    }

    public boolean isMusic() {
        return music;
    }

    public void setMusic(boolean music) {
        this.music = music;
    }

    @Override
    public int compareTo(MyTheme o) {
        return name.compareTo(o.getName());
    }
}
