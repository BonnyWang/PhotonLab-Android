package xyz.photonlab.photonlabandroid.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.List;

public class Light implements Comparable<Light> {

    private List<Light> lights;

    @Override
    public int compareTo(Light light) {
        //sort
        return 0;
    }

    private boolean checked = false;
    private boolean settled = false;

    private float sx, sy;
    private int colorIdle = Color.argb(255, 230, 230, 230);
    private int colorSettle = Color.argb(255, 230, 230, 141);

    static final float RADIUS = 80f;
    private static final float deg60 = (float) Math.PI / 3;
    private static final float deg30 = (float) Math.PI / 6;

    private float x;
    private float y;

    Paint paint;
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
        this.lights = lights;
        //find the source light
        sx = x + (RADIUS * 2) * (float) Math.cos(2 * (direction + 1) * deg30 - deg30);
        sy = y + (RADIUS * 2) * (float) Math.sin(2 * (direction + 1) * deg30 - deg30);
        boolean flag = false;
        for (Light light : lights) {
            if (light.equals(this))
                continue;
            if (Math.abs(light.getX() - sx) < 10 && Math.abs(light.getY() - sy) < 10) {
                if (light.isSettled() && Math.abs(light.getDirection() - direction) != 3)
                    flag = true;
                break;
            }
        }

        setSettled(flag);

        if (this.isSettled()) {
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
        float interfazeRadius = RADIUS;
        canvas.drawLine(x + interfazeRadius * (float) Math.cos(direction * deg60),
                y + interfazeRadius * (float) Math.sin(direction * deg60),
                x + interfazeRadius * (float) Math.cos((direction + 1) * deg60),
                y + interfazeRadius * (float) Math.sin((direction + 1) * deg60), paint);
        //canvas.drawCircle(sx, sy, 20, paint);
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