package xyz.photonlab.photonlabandroid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import xyz.photonlab.photonlabandroid.R;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class ColorPicker extends View {
    private Context mContext;
    private Paint mRightPaint;            //画笔
    private int mHeight;                  //view高
    private int mWidth;                   //view宽
    private Bitmap mLeftBitmap;
    private Bitmap bitmapTemp;

    private Paint mBitmapPaint;//画笔
    private PointF mLeftSelectPoint;//坐标
    private OnColorBackListener onColorBackListener;
    private int mLeftBitmapRadius;
    public String colorStr = "";
    private int initX = 0;
    private int initY = 0;
    private int r;//半径
    private int colorcode;

    boolean hideCursor;


    public ColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public ColorPicker(Context context) {
        this(context, null);
    }

    public void setOnColorBackListener(OnColorBackListener listener) {
        onColorBackListener = listener;
    }

    private void init() {
        bitmapTemp = BitmapFactory.decodeResource(getResources(), R.drawable.colorpicker);
        mRightPaint = new Paint();
        mRightPaint.setStyle(Paint.Style.FILL);
        mRightPaint.setStrokeWidth(1);
        mBitmapPaint = new Paint();
        //TODO:Need to change -Bonny
        mLeftBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.cursor0);
        mLeftBitmapRadius = mLeftBitmap.getWidth() / 2;

        //获取圆心
        initX = bitmapTemp.getWidth() / 2;
        initY = bitmapTemp.getHeight() / 2;
        r = bitmapTemp.getHeight() / 2;
        Log.d(TAG, "init: " + mLeftBitmapRadius + "  " + r + "  " + initX);
        mLeftSelectPoint = new PointF(0, 0);

        hideCursor = false;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmapTemp, null, new Rect(0, 0, getWidth(), getWidth()), mBitmapPaint);

        if (!hideCursor) {
            if (mLeftSelectPoint.x != 0 || mLeftSelectPoint.y != 0) {
                Log.d(TAG, "onDraw: draw cursor");
                canvas.drawBitmap(mLeftBitmap, mLeftSelectPoint.x - mLeftBitmapRadius,
                        mLeftSelectPoint.y - mLeftBitmapRadius, mBitmapPaint);
            }
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = widthMeasureSpec;
        mHeight = widthMeasureSpec;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.d(TAG, "onTouchEvent: " + hideCursor);
        float x = event.getX();
        float y = event.getY();
        Log.d("hello", "onTouchEvent: " + x + " and " + y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                proofLeft(x, y);
                invalidate();
                getRGB();
                hideCursor = false;
                break;
            case MotionEvent.ACTION_UP:
                getRGB();
                invalidate();
                hideCursor = false;
        }
        getParent().requestDisallowInterceptTouchEvent(true);
        return true;
    }

    public String toBrowserHexValue(int number) {
        StringBuilder builder = new StringBuilder(
                Integer.toHexString(number & 0xff));
        while (builder.length() < 2) {
            builder.append("0");
        }
        return builder.toString().toUpperCase();
    }


    private void getRGB() {

        float rate = bitmapTemp.getWidth() / (float) getWidth();
        int mX = (int) (mLeftSelectPoint.x * rate) % bitmapTemp.getWidth();
        int mY = (int) (mLeftSelectPoint.y * rate) % bitmapTemp.getHeight();

        int pixel = bitmapTemp.getPixel(mX, mY);
        int r = Color.red(pixel);
        int g = Color.green(pixel);
        int b = Color.blue(pixel);
        int a = Color.alpha(pixel);
        Log.d("shit", "getRGB: " + r + " " + g + " " + b + " " + a + " ");
        colorStr = "#" + toBrowserHexValue(r) + toBrowserHexValue(g)
                + toBrowserHexValue(b);
        //十六进制的颜色字符串。
        colorcode = (a & 0xff) << 24 | (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
        Log.e("colorStr", colorStr + ":" + rate);
        if (onColorBackListener != null) {
            onColorBackListener.onColorBack(a, r, g, b);
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        if (mLeftBitmap != null && mLeftBitmap.isRecycled() == false) {
            mLeftBitmap.recycle();//图片回收
        }
        if (bitmapTemp != null && bitmapTemp.isRecycled() == false) {
            bitmapTemp.recycle();//图片回收
        }
        super.onDetachedFromWindow();
    }

    private void proofLeft(float x, float y) {
        int r = getWidth() / 2 - mLeftBitmap.getWidth() / 2;//圆半径
        //北
        PointF N = new PointF(initX, initY - r);//北
        PointF S = new PointF(initX, initY + r);//南
        PointF W = new PointF(initX - r, initY);//西
        PointF E = new PointF(initX + r, initY);//东
        int a = twoSpotGetLine(initX, initY, x, y);//圆心到点

        if (a < r) {//在圆内
            mLeftSelectPoint.x = x;
            mLeftSelectPoint.y = y;
        } else {
            initX = getWidth() / 2;
            initY = getHeight() / 2;
            double angle = 0;
            int b;
            int newx = 0;
            int newy = 0;
            if (x > initX) {//二四象限 NE SE
                if (y > initY) {//四象限 东南ES
                    b = twoSpotGetLine(S.x, S.y, x, y);//南点到点
                    double aoccos = (Math.pow(a, 2) + Math.pow(r, 2) - Math.pow(b, 2)) / (2 * a * r);
                    angle = 90d - (Math.acos(aoccos) * (180 / Math.PI));//角度
                    Log.e("getLeftColor", "角度东南ES a: " + a + ",b: " + b + ",c: " + r + ",angle:" + angle);
                    if (angle % 90 == 0) {
                        newx = initX;
                        newy = initY + r;
                    }
                } else if (y < initY) {//二象限 北东EN
                    b = twoSpotGetLine(E.x, E.y, x, y);//北点到点
                    double aoccos = (Math.pow(a, 2) + Math.pow(r, 2) - Math.pow(b, 2)) / (2 * a * r);
                    angle = 360d - (Math.acos(aoccos) * (180 / Math.PI));//角度
                    Log.e("getLeftColor", "角度北东EN a: " + a + ",b: " + b + ",c: " + r + ",angle:" + angle);
                    if (angle % 90 == 0) {
                        newx = initX + r;
                        newy = initY;
                    }
                }
            } else {//一三象限
                if (y > initY) {//一象限 西北WN
                    b = twoSpotGetLine(W.x, W.y, x, y);//西点到点
                    double aoccos = (Math.pow(a, 2) + Math.pow(r, 2) - Math.pow(b, 2)) / (2 * a * r);
                    angle = 180d - (Math.acos(aoccos) * (180 / Math.PI));//角度
                    Log.e("getLeftColor", "角度西北WN a: " + a + ",b: " + b + ",c: " + r + ",angle:" + angle);
                    if (angle % 90 == 0) {
                        newx = initX - r;
                        newy = initY;
                    }
                } else if (y < initY) {//三象限 南西SW
                    b = twoSpotGetLine(N.x, N.y, x, y);//东点到点
                    double aoccos = (Math.pow(a, 2) + Math.pow(r, 2) - Math.pow(b, 2)) / (2 * a * r);
                    angle = 270d - (Math.acos(aoccos) * (180 / Math.PI));//角度
                    Log.e("getLeftColor", "角度南西SW a: " + a + ",b: " + b + ",c: " + r + ",angle:" + angle);
                    if (angle % 90 == 0) {
                        newx = initX;
                        newy = initY - r;
                    }
                }
            }
            if (angle % 90 != 0) {
                newx = (int) (initX + r * Math.cos(angle * Math.PI / 180));
                newy = (int) (initY + r * Math.sin(angle * Math.PI / 180));
            }
            Log.e("getLeftColor", "新坐标 x: " + newx + ",y: " + newy);
            mLeftSelectPoint.x = newx;
            mLeftSelectPoint.y = newy;
        }
    }

    private int twoSpotGetLine(float x1, float y1, float x2, float y2) {
        double line2 = Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2);
        return (int) Math.abs(Math.sqrt(line2));
    }

    public String getColorStr() {
        return colorStr;
    }

    public void setColorStr(String colorStr) {
        this.colorStr = colorStr;
    }

    public interface OnColorBackListener {
        public void onColorBack(int a, int r, int g, int b);
    }

    public int getColorcode() {
        return colorcode;
    }

    public boolean movecursor() {
        return true;
    }

    public void hideCursor() {
        hideCursor = true;
    }
}
