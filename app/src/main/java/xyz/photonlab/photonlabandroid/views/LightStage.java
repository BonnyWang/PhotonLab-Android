package xyz.photonlab.photonlabandroid.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import xyz.photonlab.photonlabandroid.R;

import static xyz.photonlab.photonlabandroid.views.Light.RADIUS;

public class LightStage extends View {

    private RectF screenArea;
    private Paint paint;
    private List<Light> lights;
    List<Dot> dots = new ArrayList<>();

    public LightStage(Context context) {
        super(context);
        init();
    }

    public LightStage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        screenArea = new RectF(0f, 0f, 0f, 0f);
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.backGround, null));
        lights = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        super.onFinishInflate();
        this.screenArea.right = getMeasuredWidth();
        this.screenArea.bottom = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(screenArea, paint);
        for (Light light : lights) {
            light.update(lights);
            light.draw(canvas);
        }
        for (Dot dot : dots) {
            dot.update();
            //dot.draw(canvas);
        }
        invalidate();
    }


    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        performClick();
        float x = motionEvent.getX();
        float y = motionEvent.getY();

        if (MotionEvent.ACTION_DOWN == motionEvent.getAction()) {
            Light down = null;
            float downDistance = Float.MAX_VALUE;
            for (Light light : lights) {//find the nearest light
                float distance = (x - light.getX()) * (x - light.getX()) + (y - light.getY()) * (y - light.getY());
                distance = (float) Math.sqrt(distance);
                if (distance <= RADIUS) {
                    if (downDistance > distance) {
                        down = light;
                        downDistance = distance;
                    }
                }
            }
            clearChecked();
            if (down != null) {
                down.setChecked(true);
            }

        } else if (MotionEvent.ACTION_MOVE == motionEvent.getAction()) {
            Light toMove = null;
            for (Light light : lights) {
                if (light.isChecked()) {
                    toMove = light;
                }
            }
            if (toMove != null) {
                toMove.setX(x);
                toMove.setY(y);
            } else {
                //todo translate screen
            }
        } else if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
            for (Light light : lights) {
                if (light.isChecked()) {
                    settleLight(light);
                }
            }
        }
        return true;
    }

    private void generateDot(Light light) {
        float deg = (float) Math.PI / 6;
        float cx = light.getX(), cy = light.getY();

        for (int j = 0; j < 6; j++) {
            float x = cx + (RADIUS * 2) * (float) Math.cos(2 * j * deg - deg);
            float y = cy + (RADIUS * 2) * (float) Math.sin(2 * j * deg - deg);

            Dot dot = new Dot(x, y);
            dot.setOffsetX(light.getX() - x);
            dot.setOffsetY(light.getY() - y);
            dot.setParent(light);
            dots.add(dot);
        }
    }

    private void settleLight(Light light) {
        //position find
        Dot nearest = null;
        float distance = Float.MAX_VALUE;
        for (Dot dot : dots) {
            if (dot.getParent().equals(light))
                continue;
            float x = light.getX();
            float y = light.getY();
            float d = (x - dot.getX()) * (x - dot.getX()) + (y - dot.getY()) * (y - dot.getY());
            if (d < distance) {
                boolean flag = true;
                for (Light toCompare : lights) {
                    if (Math.abs(toCompare.getX() - dot.getX()) < 10
                            && Math.abs(toCompare.getY() - dot.getY()) < 10) {//conflicted
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    nearest = dot;
                    distance = d;
                }
            }
        }
        if (nearest != null) {
            light.setX(nearest.getX());
            light.setY(nearest.getY());
        }
    }


    private void clearChecked() {
        for (Light light : lights) {
            light.setChecked(false);
        }
    }

    public void addLight() {
        Light light;
        if (lights.size() == 0) {
            light = new MotherLight(screenArea.right * (float) Math.random(),
                    screenArea.bottom * (float) Math.random());
        } else {
            light = new Light(screenArea.right * (float) Math.random(),
                    screenArea.bottom * (float) Math.random());
        }
        generateDot(light);
        this.lights.add(light);
        clearChecked();
        light.setChecked(true);
    }

    public void deleteLight() {
        for (Light light : lights)
            if (light.isChecked()) {
                if (!(light instanceof MotherLight)) {
                    lights.remove(light);
                    Iterator<Dot> iterator = dots.iterator();
                    while (iterator.hasNext()) {
                        if (iterator.next().getParent().equals(light)) {
                            iterator.remove();
                        }
                    }
                }
                return;
            }
    }

    public void rotateLight() {
        for (Light light : lights) {
            if (light.isChecked()) {
                light.setDirection((light.getDirection() + 1) % 6);
                return;
            }
        }
    }

}