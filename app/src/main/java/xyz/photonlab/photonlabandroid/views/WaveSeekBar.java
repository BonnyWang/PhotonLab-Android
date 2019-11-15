package xyz.photonlab.photonlabandroid.views;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * created by KIO on 2019/9/21
 */
public class WaveSeekBar extends View {

    private int progress;
    private float waveHeight;//振幅
    private Path mPath;
    private Paint mPaint;
    private OnProgressChangedListener mListener;

    private int mWidth, mHeight;
    private float time, frequency;
    private boolean touched = false;
    private int destinationColor;
    private long setColorTimeStamp;
    private ArgbEvaluator argbEvaluator;

    public WaveSeekBar(Context context) {
        super(context);
        init();
    }

    public WaveSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        progress = 90;
        waveHeight = 0;
        frequency = 0.005f;
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(Color.TRANSPARENT);
        setColorTimeStamp = -1;
        this.argbEvaluator = new ArgbEvaluator();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        float cY = mHeight * (100 - progress) / 100f;
        //起始点
        mPath.moveTo(0, cY);
        for (int i = 0; i < mWidth; i += 10) {
            float waveY = (float) (cY + waveHeight * Math.sin(i * frequency + time));
            mPath.lineTo(i, waveY);
        }
        float waveY = (float) (cY + waveHeight * Math.sin(mWidth * frequency + time));
        mPath.lineTo(mWidth, waveY);
        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(0, mHeight);
        mPath.close();
        float step = (System.currentTimeMillis() - setColorTimeStamp) / 2000f;
        if (step > 1f)
            step = 1f;
        int currentColor = ((int) argbEvaluator.evaluate(step,
                mPaint.getColor(), destinationColor));
        mPaint.setColor(currentColor);
        canvas.drawPath(mPath, mPaint);
        update();
        invalidate();
    }

    private void update() {
        time += 0.1f;
        if (touched) {
            waveHeight = 35;
        } else {
            waveHeight -= 0.4f;
            if (waveHeight < 0) {
                waveHeight = 0;
            }
        }
    }

    public void active() {
        touched = false;
        waveHeight = 35;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.performClick();
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            this.touched = true;
        if (event.getAction() == MotionEvent.ACTION_UP)
            this.touched = false;
        this.progress = (int) ((mHeight - event.getY()) / mHeight * 100);
        if (progress > 100)
            progress = 100;
        if (progress < 0)
            progress = 0;
        Log.i("Progress", progress + "");
        if (mListener != null)
            mListener.onProgressChanged(progress);
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mWidth = getMeasuredWidth();
        this.mHeight = getMeasuredHeight();
    }

    public void setColor(int color) {
        if (setColorTimeStamp == -1)
            mPaint.setColor(color);
        this.destinationColor = color;
        this.setColorTimeStamp = System.currentTimeMillis();
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setOnProgressChangedListener(OnProgressChangedListener listener) {
        this.mListener = listener;
    }

    public interface OnProgressChangedListener {
        void onProgressChanged(int progress);
    }
}
