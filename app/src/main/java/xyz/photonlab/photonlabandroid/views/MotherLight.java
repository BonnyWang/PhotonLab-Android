package xyz.photonlab.photonlabandroid.views;

import android.graphics.Canvas;
import android.graphics.Color;

import java.util.List;

public class MotherLight extends Light {

    public MotherLight(float x, float y) {
        super(x, y);
        setSettled(true);
    }


    @Override
    public void update(List<Light> light) {
        super.update(light);
        paint.setColor(Color.argb(255, 240, 118, 58));
    }

    @Override
    void draw(Canvas canvas) {
        super.draw(canvas);
    }
}
