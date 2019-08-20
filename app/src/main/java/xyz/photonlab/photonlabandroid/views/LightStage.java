package xyz.photonlab.photonlabandroid.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import xyz.photonlab.photonlabandroid.R;

import static xyz.photonlab.photonlabandroid.views.Light.RADIUS;

public class LightStage extends View implements Serializable {

    private RectF screenArea, bound = new RectF();
    private List<Light> lights;
    private Paint paint = new Paint();
    private OnViewCreatedListener onViewCreatedListener;
    private boolean movable = true;
    public static float offsetX, offsetY;
    private float tempX, tempY;
    private OnLightCheckedChangeListener onLightCheckedChangeListener;

    private Light selectedLight;

    List<Dot> dots = new ArrayList<>();
    private boolean needCenter = false;


    private final float verticalOffset = 0.5f * RADIUS * (float) Math.sqrt(3);

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

        lights = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        super.onFinishInflate();
        this.screenArea.right = getMeasuredWidth();
        this.screenArea.bottom = getMeasuredHeight();
        RADIUS = this.screenArea.width() / 20;
        if (this.onViewCreatedListener != null)
            this.onViewCreatedListener.onViewCreated();
        if (needCenter && getMotherLight() != null) {
            if (bound != null) {
                offsetX = screenArea.centerX() - bound.centerX();
                offsetY = screenArea.centerY() - bound.centerY();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Light light : lights) {
            light.update(lights);
            light.draw(canvas);
        }
        for (Dot dot : dots) {
            dot.update();
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
        float positionX = x - offsetX;
        float positionY = y - offsetY;
        if (MotionEvent.ACTION_DOWN == motionEvent.getAction()) {
            Light down = null;
            float downDistance = Float.MAX_VALUE;
            for (Light light : lights) {//find the nearest light
                float distance = (positionX - light.getX()) * (positionX - light.getX()) + (positionY - light.getY()) * (positionY - light.getY());
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
                if (onLightCheckedChangeListener != null && !down.equals(selectedLight)) {
                    selectedLight = down;
                    onLightCheckedChangeListener.onLightCheckedChanged(down);
                }
            } else {
                tempY = y;
                tempX = x;
                if (onLightCheckedChangeListener != null) {
                    selectedLight = null;
                    onLightCheckedChangeListener.onLightCheckedChanged(null);
                }
            }

        } else if (MotionEvent.ACTION_MOVE == motionEvent.getAction()) {
            Light toMove = null;
            for (Light light : lights) {
                if (light.isChecked()) {
                    toMove = light;
                }
            }
            if (toMove != null) {
                if (movable) {
                    toMove.setX(positionX);
                    toMove.setY(positionY);
                    refreshBound();
                }
            } else {
                if (bound != null) {
                    offsetX += x - tempX;
                    offsetY += y - tempY;
                    tempX = x;
                    tempY = y;
                    Log.i("offsetX", offsetX + "");
                    Log.i("offsetY", offsetY + "");
                    Log.i("boundRender", "[" + (bound.left + offsetX) + ","
                            + (bound.top + offsetY) + "," + (bound.right + offsetX) + ","
                            + (bound.bottom + offsetY) + "]");
                    if (bound.width() < screenArea.width()) {
                        if (bound.left + offsetX < 0) {
                            offsetX = -bound.left;
                        }
                        if (bound.right + offsetX > screenArea.right) {
                            offsetX = screenArea.right - bound.right;
                        }
                    } else {
                        if (bound.left + offsetX > 0)
                            offsetX = -bound.left;
                        if (bound.right + offsetX < screenArea.right)
                            offsetX = screenArea.right - bound.right;
                    }
                    if (bound.height() < screenArea.height()) {
                        if (bound.top + offsetY < 0)
                            offsetY = -bound.top;
                        if (bound.bottom + offsetY > screenArea.bottom)
                            offsetY = screenArea.bottom - bound.bottom;
                    } else {
                        if (bound.top + offsetY > 0)
                            offsetY = -bound.top;
                        if (bound.bottom + offsetY < screenArea.bottom)
                            offsetY = screenArea.bottom - bound.bottom;
                    }
                }
            }
        } else if (MotionEvent.ACTION_UP == motionEvent.getAction() && movable) {
            for (Light light : lights) {
                if (light.isChecked()) {
                    settleLight(light);
                    break;
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
                    if (toCompare.equals(light))
                        continue;
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
            refreshBound();
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
            light = new MotherLight(getContext(), screenArea.right / 2,
                    screenArea.bottom / 2);
        } else {
            light = new Light(screenArea.width() / 2 - offsetX, 2 * RADIUS - offsetY);
        }
        this.addLight(light);
    }

    public void addLight(Light light) {
        if (lights.size() >= 20)
            return;
        generateDot(light);
        this.lights.add(light);
        clearChecked();
        light.setChecked(true);
        refreshBound();
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
                refreshBound();
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

    public int getUselessLightNum() {
        int num = 0;
        for (Light light : lights)
            if (!light.isSettled())
                num++;
        return num;
    }

    public List<Light> getLights() {
        return this.lights;
    }

    public void setOnViewCreatedListener(OnViewCreatedListener onViewCreatedListener) {
        this.onViewCreatedListener = onViewCreatedListener;
    }

    public List<Dot> getDots() {
        return this.dots;
    }

    public MotherLight getMotherLight() {
        if (lights.size() == 0)
            return null;
        else
            return (MotherLight) lights.get(0);
    }

    public void requireCenter() {
        this.needCenter = true;
    }

    public void setPlaneColor(int color) {
        for (Light light : lights) {
            if (light.isChecked())
                light.setPlaneColor(color);
        }
    }

    public void setOnLightCheckedChangeListener(OnLightCheckedChangeListener
                                                        onLightCheckedChangeListener) {
        this.onLightCheckedChangeListener = onLightCheckedChangeListener;
    }

    private void refreshBound() {
        float left = Float.MAX_VALUE, right = -Float.MAX_VALUE,
                top = Float.MAX_VALUE, bottom = -Float.MAX_VALUE;
        for (Light item : lights) {
            if (item.getX() - RADIUS < left)
                left = item.getX() - RADIUS;
            if (item.getX() + RADIUS > right)
                right = item.getX() + RADIUS;
            if (item.getY() + verticalOffset > bottom)
                bottom = item.getY() + verticalOffset;
            if (item.getY() - verticalOffset < top)
                top = item.getY() - verticalOffset;
        }
        float r = RADIUS * 4;
        bound = new RectF(left - r, top - r, right + r, bottom + r);
        Log.i("bound", bound.toString());
    }

    public void enterSetupMode() {
        for (Light light : lights) {
            light.setNum(-1);
            light.litDown();
            light.setChecked(false);
        }
    }

    public boolean allLitUp() {
        for (Light light : lights)
            if (!light.isLitUp())
                return false;
        return true;
    }

    public Light getCurrentLight() {
        for (Light light : lights)
            if (light.isChecked())
                return light;
        return null;
    }

    public interface OnViewCreatedListener {
        void onViewCreated();
    }

    public interface OnLightCheckedChangeListener {
        void onLightCheckedChanged(Light light);
    }

    public void denyMove() {
        this.movable = false;
    }
}