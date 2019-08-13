package xyz.photonlab.photonlabandroid;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

public class ColorfulThemeClass extends theme_Class {
    ColorfulThemeClass() {
        this.colors = new int[] {
                Color.parseColor("#f72323"),
//            Color.parseColor("#f79823"),
                Color.parseColor("#f7ec23"),
                Color.parseColor("#27f723"),
//            Color.parseColor("#23f7ec"),
                Color.parseColor("#232ef7"),
                Color.parseColor("#f023f7"),
        };
    }

    @Override
    public GradientDrawable getGradientDrawablt() {
        return new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
    }
}
