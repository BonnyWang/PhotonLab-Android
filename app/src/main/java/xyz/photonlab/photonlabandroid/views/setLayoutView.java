package xyz.photonlab.photonlabandroid.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class setLayoutView extends View {

    private final static String TAG = "setLayoutView";

    Paint paint = new Paint();
    Path[] hex = new Path[20];
    float length = 100f;

    int hexNumber;


    private float[] hexX1 = new float[20];
    private float[] hexY1 = new float[20];

    public setLayoutView(Context context) {
        super(context);
        init(null);
    }

    public setLayoutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public setLayoutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public setLayoutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }


    //Initialize for all the constructor being called -Bonny
    private void init(@Nullable AttributeSet set){

        hexNumber = 0;

        for(int i = 1; i < 20; i++){
           hexX1[i] = 500;
           hexY1[i] = 500;
        }
    }

    @Override
    protected void onDraw(Canvas canvas){

        paint.setColor(Color.RED);
        paint.setAntiAlias(true);

        if(hexX1[0] == 0f || hexY1[0] == 0f){
            hexX1[0] = getWidth()/2f;
            hexY1[0] = getHeight()/2f;
        }

        for(int i = 0; i <= hexNumber; i++){
            drawHex(hexX1[i],hexY1[i], i);
            canvas.drawPath(hex[i], paint);
            Log.d(TAG, "onDraw: " + i);
        }

        //canvas.drawLine(0,0,100,0,paint);
        //canvas.drawLine(100,100,500,500,paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        boolean value = super.onTouchEvent(event);

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                return true;
            }

            case MotionEvent.ACTION_MOVE:{

                float x = event.getX();
                float y = event.getY();
                Log.d(TAG, "onTouchEvent: " +x);
                Log.d(TAG, "onTouchEvent: " +y);

                for(int i = 0; i <= hexNumber; i++){
                    if(x > hexX1[i]-length && x < hexX1[i] + length && y > hexY1[i] -length && y < hexY1[i] + length){
                        //touched the hex
                        hexX1[i] = x;
                        hexY1[i] = y;
                        Log.d(TAG, "onTouchEvent: " +x);
                        Log.d(TAG, "onTouchEvent: " +y);
                        postInvalidate();
                    }
                }

                return true;
            }
        }

        return value;
    }


    private void drawHex(float cX, float cY, int hexNumber){

        float[] x = new float[6];
        float[] y = new float[6];



        x[0] = cX - length/2f;
        y[0] = cY - (float)(Math.sqrt(3f))*(length/2f);

        x[1] = cX + length/2f;
        y[1] = cY - (float)(Math.sqrt(3f))*(length/2f);

        x[2] = cX + length;
        y[2] = cY;

        x[3] = cX + length/2;
        y[3] = cY + (float)(Math.sqrt(3f))*(length/2f);

        x[4] = cX - length/2;
        y[4] = cY + (float)(Math.sqrt(3f))*(length/2f);

        x[5] = cX - length;
        y[5] = cY;

        hex[hexNumber] = new Path();
        hex[hexNumber].moveTo(x[0],y[0]);

        for(int i = 1; i < 6; i++){
            hex[hexNumber].lineTo(x[i],y[i]);
        }

        hex[hexNumber].lineTo(x[0],y[0]);
        hex[hexNumber].close();
    }

    public void addHex(){

        if(hexNumber < 20){
            hexNumber++;
        }


        Log.d(TAG, "addHex: ");

        postInvalidate();
    }
}
