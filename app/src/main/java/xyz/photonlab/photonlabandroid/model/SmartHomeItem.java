package xyz.photonlab.photonlabandroid.model;

import android.graphics.Color;

public class SmartHomeItem {
    private String title;
    private int color;

    public SmartHomeItem(String title, int color) {
        this.title = title;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public String getTitle() {
        return title;
    }
}
