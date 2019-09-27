package xyz.photonlab.photonlabandroid.views;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

/**
 * created by KIO on 2019/9/20
 */
public class DynamicLightSymbol extends View {

    private float radius, strokeRadius;
    private Paint paint;
    private Path path;
    private float cx, cy, strokeWidth, strokeAlpha;
    private long duration;
    private long startTimestamp;

    private TimeInterpolator mInterpolator;
    private boolean repeat;
    private boolean reverse;

    //源
    private float sStrokeRadius, sStrokeWidth, sStrokeAlpha;

    //目标
    private float dStrokeRadius, dStrokeWidth, dStrokeAlpha;
    private boolean drawStroke;

    public DynamicLightSymbol(Context context) {
        super(context);
        init();
    }

    public DynamicLightSymbol(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(0xffcccccc);
        path = new Path();
        strokeWidth = 0;

        sStrokeWidth = 1;
        dStrokeWidth = 6;

        sStrokeAlpha = 255;
        dStrokeAlpha = 66;

        mInterpolator = new DecelerateInterpolator();
        duration = 600;
        startTimestamp = System.currentTimeMillis();
        repeat = true;
        reverse = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制六边形
        paint.setStrokeWidth(0);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(255);
        path.reset();
        path.moveTo(cx + radius, cy);
        for (int i = 1; i < 6; i++) {
            path.lineTo(cx + radius * (float) Math.cos(i * Math.PI / 3),
                    cy + radius * (float) Math.sin(i * Math.PI / 3));
        }
        path.close();
        canvas.drawPath(path, paint);
        if (this.drawStroke){
            //绘制边框
            paint.setStrokeWidth(strokeWidth);
            paint.setStyle(Paint.Style.STROKE);
            paint.setAlpha((int) strokeAlpha);
            path.reset();
            path.moveTo(cx + strokeRadius, cy);
            for (int i = 1; i < 6; i++) {
                path.lineTo(cx + strokeRadius * (float) Math.cos(i * Math.PI / 3),
                        cy + strokeRadius * (float) Math.sin(i * Math.PI / 3));
            }
            path.close();
            canvas.drawPath(path, paint);
        }
        invalidate();
        update();
    }

    private void update() {
        long timePassed = System.currentTimeMillis() - startTimestamp;
        float durationElapse = timePassed / (float) duration;
        if (durationElapse > 1) {//一次动画结束
            if (repeat)
                startTimestamp = System.currentTimeMillis();
            if (reverse) {//交换源和目标
                float temp = sStrokeRadius;
                sStrokeRadius = dStrokeRadius;
                dStrokeRadius = temp;

                temp = sStrokeWidth;
                sStrokeWidth = dStrokeWidth;
                dStrokeWidth = temp;

                temp = sStrokeAlpha;
                sStrokeAlpha = dStrokeAlpha;
                dStrokeAlpha = temp;
            }
            return;
        }
        strokeRadius = sStrokeRadius + (dStrokeRadius - sStrokeRadius) * mInterpolator.getInterpolation(durationElapse);
        strokeWidth = sStrokeWidth + (dStrokeWidth - sStrokeWidth) * mInterpolator.getInterpolation(durationElapse);
        strokeAlpha = sStrokeAlpha + (dStrokeAlpha - sStrokeAlpha) * mInterpolator.getInterpolation(durationElapse);
    }

    public void setColor(int color) {
        this.paint.setColor(color);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        radius = getMeasuredWidth() / 2f * 0.65f;

        strokeRadius = radius;
        sStrokeRadius = getMeasuredWidth() / 2f * 0.75f;
        dStrokeRadius = getMeasuredWidth() / 2f * 0.9f - sStrokeWidth;

        cx = getMeasuredWidth() / 2f;
        cy = getMeasuredWidth() / 2f;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setInterpolator(TimeInterpolator mInterpolator) {
        this.mInterpolator = mInterpolator;
    }

    public void setDrawStroke(boolean drawStroke) {
        this.drawStroke = drawStroke;
    }
}
