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
    Path hex;


    private float hexX1, hexY1;

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

    }

    @Override
    protected void onDraw(Canvas canvas){

        paint.setColor(Color.RED);
        paint.setAntiAlias(true);

        if(hexX1 == 0f || hexY1 == 0f){
            hexX1 = getWidth()/2f;
            hexY1 = getHeight()/2f;
        }
        drawHex(hexX1,hexY1);
        canvas.drawPath(hex, paint);
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

                //if(x > hexX1 && x < hexX1 + 200 && y > hexY1 && y < hexY1 + 300){
                    //touched the hex
                    hexX1 = x;
                    hexY1 = y;
                    Log.d(TAG, "onTouchEvent: " +x);
                    Log.d(TAG, "onTouchEvent: " +y);
                    postInvalidate();
                //}



                return true;
            }
        }

        return value;
    }


    private void drawHex(float x1, float y1){
        hex = new Path();
        hex.moveTo(x1, y1);
        hex.lineTo(x1 + 200,y1);
        hex.lineTo(x1 + 200 + 100,y1 -173.2050f);
        hex.lineTo(x1+ 200, y1 - 173.2050f - 173.2050f);
        hex.lineTo(x1, y1 - 173.2050f - 173.2050f);
        hex.lineTo(x1-100, y1 - 173.2050f);
        hex.lineTo(x1,y1);
        hex.close();
    }
}
