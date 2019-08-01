package xyz.photonlab.photonlabandroid.views;

import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Dot implements Serializable {
    private float x, y;
    private float offsetX, offsetY;
    private float positionX, positionY;

    public Light getParent() {
        return parent;
    }

    private Light parent;

    Dot(float x, float y) {
        this.x = x;
        this.y = y;
    }

    void update() {
        positionX = parent.getX() + offsetX + LightStage.offsetX;
        positionY = parent.getY() + offsetY + LightStage.offsetY;
        this.setX(parent.getX() + offsetX);
        this.setY(parent.getY() + offsetY);
    }

    void draw(Canvas canvas) {
        canvas.drawCircle(positionX, positionY, 4, new Paint());
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Dot))
            return false;
        Dot dot = (Dot) obj;
        return dot.getX() == this.getX() && dot.getY() == this.getY();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public void setParent(Light parent) {
        this.parent = parent;
    }
}
