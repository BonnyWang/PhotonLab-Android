package xyz.photonlab.photonlabandroid.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.Nullable;

public class Dot {
    private float x, y;
    private final Paint paint;
    private float offsetX, offsetY;

    public Light getParent() {
        return parent;
    }

    private Light parent;

    Dot(float x, float y) {
        this.x = x;
        this.y = y;
        paint = new Paint();
        paint.setColor(Color.BLACK);
    }

    void update() {
        this.setX(parent.getX() + offsetX);
        this.setY(parent.getY() + getOffsetY());
    }

    void draw(Canvas canvas) {
        canvas.drawCircle(x, y, 4, paint);
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
