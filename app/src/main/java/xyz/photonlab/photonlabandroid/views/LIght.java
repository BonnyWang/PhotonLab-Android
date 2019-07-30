package xyz.photonlab.photonlabandroid.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.List;

public class Light implements Comparable<Light> {

    @Override
    public int compareTo(Light light) {
        //sort
        return 0;
    }

    private boolean checked = false;
    private boolean settled = false;
    private Light from = null;

    protected int colorIdle = Color.argb(255, 230, 230, 230);
    protected int colorSettle = Color.argb(255, 230, 230, 141);

    static final float RADIUS = 80f;
    protected static final float deg60 = (float) Math.PI / 3;

    private float x;
    private float y;

    protected Paint paint;
    private Path path;
    private int direction = 4;//from 0 to 5

    Light(float x, float y) {
        this.x = x;
        this.y = y;
        this.paint = new Paint();
        paint.setColor(colorIdle);
        paint.setStrokeWidth(5);
    }

    public void update(List<Light> lights) {
        this.path = new Path();
        path.moveTo(x + RADIUS, y);
        for (int i = 1; i < 6; i++) {
            path.lineTo(x + RADIUS * (float) Math.cos(i * deg60), y + RADIUS * (float) Math.sin(i * deg60));
        }
        path.close();

        //find the source light
        float deg = (float) Math.PI / 6;
        float sX = lights.get(0).getX();
        float sY = lights.get(0).getY();

        from = null;
        for (Light light : lights) {
            if (this.equals(light))
                continue;
            from = lights.get(0);
        }
        if (from != null) {
            from = lights.get(0);
            System.out.println(from.isSettled());
            setSettled(from.isSettled());
        } else {
            setSettled(false);
        }

        if (this.settled) {
            paint.setColor(colorSettle);
        } else {
            paint.setColor(colorIdle);
        }
    }

    void draw(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(this.path, paint);
        if (checked) {
            paint.setStrokeWidth(5);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.argb(100, 33, 33, 230));
            canvas.drawPath(path, paint);
        }
        paint.setStrokeWidth(10);
        paint.setColor(Color.argb(100, 200, 120, 33));
        float interfazeRadius = RADIUS + 5;
        canvas.drawLine(x + interfazeRadius * (float) Math.cos(direction * deg60),
                y + interfazeRadius * (float) Math.sin(direction * deg60),
                x + interfazeRadius * (float) Math.cos((direction + 1) * deg60),
                y + interfazeRadius * (float) Math.sin((direction + 1) * deg60), paint);
    }

    public boolean conflictTo(Light light) {
        float tx = light.getX(), ty = light.getY();
        float distance = (getX() - tx) * (getX() - tx) + (getY() - ty) * (getY() - ty);
        return distance <= RADIUS * RADIUS - RADIUS;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isSettled() {
        return settled;
    }

    public void setSettled(boolean settled) {
        this.settled = settled;
    }
}