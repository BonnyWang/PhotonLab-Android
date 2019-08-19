package xyz.photonlab.photonlabandroid.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import java.io.Serializable;
import java.util.List;

public class Light implements Serializable {

    private boolean checked = false;
    private boolean settled = false;

    private int colorIdle = Color.argb(255, 155, 155, 155);
    private int colorSettle = Color.argb(255, 80, 227, 194);

    int currentColor;

    private long num = -1;//-1 for not set yet

    static float RADIUS = 100f;
    private static final float deg60 = (float) Math.PI / 3;
    private static final float deg30 = (float) Math.PI / 6;

    private Paint paint = new Paint();

    protected float x;
    protected float y;

    private float positionX, positionY;

    private Path path;
    private int direction = 1;//from 0 to 5
    private boolean plane = false;
    private int planeColor = Color.rgb(200, 200, 200);
    protected float originDirection = 1;
    private int litUp = -1;//-1 for useless,0 for not lit up,1 for lit up

    public Light(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void update(List<Light> lights) {
        positionX = x + LightStage.offsetX;
        positionY = y + LightStage.offsetY;

        if (Math.abs(originDirection - direction) >= 0.1f) {
            originDirection += 0.1f;
            if (originDirection >= 6)
                originDirection = 0;
        } else {
            originDirection = direction;
        }

        this.path = new Path();
        path.moveTo(positionX + (RADIUS + paint.getStrokeWidth() / 2) * (float) Math.cos((0 + originDirection) * deg60),
                positionY + (RADIUS + paint.getStrokeWidth() / 2) * (float) Math.sin((0 + originDirection) * deg60));
        for (int i = 1; i < 6; i++) {
            path.lineTo(positionX + (RADIUS + paint.getStrokeWidth() / 2) * (float) Math.cos((i + originDirection) * deg60),
                    positionY + (RADIUS + paint.getStrokeWidth() / 2) * (float) Math.sin((i + originDirection) * deg60));
        }
        path.close();
        //find the source light
        float sx = x + (RADIUS * 2) * (float) Math.cos(2 * (direction + 1) * deg30 - deg30);
        float sy = y + (RADIUS * 2) * (float) Math.sin(2 * (direction + 1) * deg30 - deg30);
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
            currentColor = colorSettle;
        } else {
            currentColor = colorIdle;
        }

    }

    void draw(Canvas canvas) {
        canvas.save();
        paint.setColor(currentColor);
        if (plane) {
            paint.setColor(planeColor);
        }
        if (litUp == 0) {
            paint.setColor(colorIdle);
        } else if (litUp == 1) {
            paint.setColor(colorSettle);
        }

        paint.setStrokeWidth(0);
        paint.setStyle(Paint.Style.FILL);
        if (checked) {
            paint.setAlpha(153);
        }
        canvas.drawPath(this.path, paint);


        //draw direction pointer
        paint.setStrokeWidth(0.15f * RADIUS);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Color.argb(255, 255, 255, 255));
        float interfazeRadius = RADIUS * 0.82f;
        if (!plane)
            canvas.drawLine(positionX + interfazeRadius * (float) Math.cos(originDirection * deg60),
                    positionY + interfazeRadius * (float) Math.sin(originDirection * deg60),
                    positionX + interfazeRadius * (float) Math.cos((originDirection + 1) * deg60),
                    positionY + interfazeRadius * (float) Math.sin((originDirection + 1) * deg60), paint);

        canvas.restore();
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

    public void setPlaneColor(int planeColor) {
        this.planeColor = planeColor;
    }

    public void setPlane(boolean plane) {
        this.plane = plane;
    }

    public int getPlaneColor() {
        return planeColor;
    }

    public void setDirection(int direction) {
        this.originDirection = this.direction;
        this.direction = direction;
    }

    public void setDirection(int direction, boolean shouldAnimate) {
        setDirection(direction);
        if (!shouldAnimate)
            originDirection = this.direction;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public void litUp() {
        this.litUp = 1;
    }

    public void litDown() {
        this.litUp = 0;
    }

    public boolean isLitUp() {
        return litUp == 1;
    }
}