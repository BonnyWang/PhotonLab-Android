package xyz.photonlab.photonlabandroid.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import xyz.photonlab.photonlabandroid.R;

public class setLayoutView extends View {

    private final static String TAG = "setLayoutView";

    Paint paint = new Paint();
    Paint paintStroke = new Paint();
    Paint paintSelected = new Paint();
    Path[] hex = new Path[20];
    float length = 100f;
    float dividerLength = 15f;

    int hexNumber;


    float initPosX = 300f;
    float initPosY = 300f;

    int whichHex = 0;
    boolean findPotential;

    int hexColor;
    int hexChosenColor;

    private float[] hexX1 = new float[20];
    private float[] hexY1 = new float[20];

    private float[][] potentialPosi = new float[999][];
    int potentialNumber;

    Matrix matrix = new Matrix();


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
        potentialNumber = 0;


        hexColor = getResources().getColor(R.color.Light_Grey, null);
        hexChosenColor = getResources().getColor(R.color.Deep_Grey, null);


        //initalize the position where the added hex will appear
        for(int i = 1; i < 20; i++){
           hexX1[i] = initPosX;
           hexY1[i] = initPosY;
        }

//        hexX1[0] = getWidth()/2f;
//        hexY1[0] = getHeight()/2f;
//        Log.d(TAG, "onDraw: "+ hexX1[0]+hexY1[0]);
//
//        potentialPosi[0] = new float[]{hexX1[0],hexY1[0]
//                - 2*(float)(Math.sqrt(3f))*(length/2f)};
//
//        potentialPosi[1] = new float[]{hexX1[0]
//                + 3f*length/2f,hexY1[0]
//                - (float)(Math.sqrt(3f))*(length/2f)};
//
//        potentialPosi[2] = new float[]{hexX1[0]
//                + 3f*length/2f,hexY1[0]
//                + (float)(Math.sqrt(3f))*(length/2f)};
//
//        potentialPosi[3] = new float[]{hexX1[0],hexY1[0]
//                + 2*(float)(Math.sqrt(3f))*(length/2f)};
//        potentialPosi[4] = new float[]{hexX1[0]
//                - 3f*length/2f,hexY1[0]
//                + (float)(Math.sqrt(3f))*(length/2f)};
//
//        potentialPosi[5] = new float[]{hexX1[0]
//                - 3f*length/2f,hexY1[0]
//                - (float)(Math.sqrt(3f))*(length/2f)};


    }

    @Override
    protected void onDraw(Canvas canvas){

        //TODO:Scale the canvas
//        if(hexNumber > 10){
//            length = 50f;
//        }

        paint.setColor(hexColor);
        paint.setAntiAlias(true);

        paintStroke.setColor(getResources().getColor(R.color.backGround,null));
        paintStroke.setStyle(Paint.Style.STROKE);
        paintStroke.setStrokeWidth(dividerLength);
        paintStroke.setAntiAlias(true);

        paintSelected.setColor(hexChosenColor);
        paintSelected.setAntiAlias(true);

        if(hexX1[0] == 0f || hexY1[0] == 0f){
            hexX1[0] = getWidth()/2f;
            hexY1[0] = getHeight()/2f;
            addPotentialPosi(hexX1[0],hexY1[0]);
           
        }

        Log.d(TAG, "onDraw: "+ hexX1[0]+hexY1[0]);
        for(int i = 0; i <= hexNumber; i++){
            drawHex(hexX1[i],hexY1[i], i);
            canvas.drawPath(hex[i], paint);
            canvas.drawPath(hex[i],paintStroke);
            Log.d(TAG, "onDraw: " + i);
        }

        if(whichHex != 0){
            canvas.drawPath(hex[whichHex], paintSelected);
            canvas.drawPath(hex[whichHex],paintStroke);
        }




        for(int i = 0; i < potentialNumber; i++){
            //For debug purpose-Bonny
            //canvas.drawCircle(potentialPosi[i][0],potentialPosi[i][1],5,paint);
        }

        if(hexNumber > 10){
            canvas.scale(0.5f,0.5f);
        }

    }



    @Override
    public boolean onTouchEvent(MotionEvent event){
        boolean value = super.onTouchEvent(event);
        


        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{

                float x = event.getX();
                float y = event.getY();
                Log.d(TAG, "onTouchEvent: " +x);
                Log.d(TAG, "onTouchEvent: " +y);


                for(int i = 1; i <= hexNumber; i++){
                    if(x > hexX1[i]-length && x < hexX1[i] + length && y > hexY1[i] -length && y < hexY1[i] + length) {
                        whichHex = i;
                        postInvalidate();

                    }
                }
                return true;
            }

            case MotionEvent.ACTION_MOVE:{

                float x = event.getX();
                float y = event.getY();
                Log.d(TAG, "onTouchEvent: " +x);
                Log.d(TAG, "onTouchEvent: " +y);

                for(int i = 1; i <= hexNumber; i++){
                    if(x > hexX1[i]-length && x < hexX1[i] + length && y > hexY1[i] -length && y < hexY1[i] + length){
                        //touched the hex
                        hexX1[i] = x;
                        hexY1[i] = y;
                        Log.d(TAG, "onTouchEvent: " +x);
                        Log.d(TAG, "onTouchEvent: " +y);
                        whichHex = i;
                        postInvalidate();

                        return true;
                    }
                }


            }

            case MotionEvent.ACTION_UP:{

                for(int i = 0; i <  potentialNumber; i++){
                    float dx = hexX1[whichHex] - potentialPosi[i][0];
                    float dy = hexY1[whichHex] - potentialPosi[i][1];

                    Log.d(TAG, "onTouchEvent: " + dx + dy + event.getX()+event.getY());
                    
                    if (Math.abs(dx) < 100 && Math.abs(dy) < 100){
                        hexX1[whichHex] = potentialPosi[i][0];
                        hexY1[whichHex] = potentialPosi[i][1];

                        findPotential = true;

                        postInvalidate();
                        Log.d(TAG, "onTouchEvent: find potential");
                    }

                }

                if(findPotential){
                    addPotentialPosi(hexX1[whichHex],hexY1[whichHex]);
                    postInvalidate();
                }

                findPotential = false;
                //whichHex = 0;
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

        if(hexNumber < 19){
            hexNumber++;
        }

        Log.d(TAG, "addHex: ");

        postInvalidate();
    }


    public void addPotentialPosi(float cX, float cY){

        if(potentialNumber < 999) {

            potentialPosi[potentialNumber] = new float[]{cX, cY
                    - 2 * (float) (Math.sqrt(3f)) * (length / 2f)};

            potentialNumber++;
            potentialPosi[potentialNumber] = new float[]{cX
                    + 3f * length / 2f, (cY
                    - (float) (Math.sqrt(3f)) * (length / 2f))};

            potentialNumber++;
            potentialPosi[potentialNumber] = new float[]{cX
                    + 3f * length / 2f, cY
                    + (float) (Math.sqrt(3f)) * (length / 2f)};

            potentialNumber++;
            potentialPosi[potentialNumber] = new float[]{cX, cY
                    + 2 * (float) (Math.sqrt(3f)) * (length / 2f)};

            potentialNumber++;
            potentialPosi[potentialNumber] = new float[]{cX
                    - 3f * length / 2f, cY
                    + (float) (Math.sqrt(3f)) * (length / 2f)};

            potentialNumber++;
            potentialPosi[potentialNumber] = new float[]{cX
                    - 3f * length / 2f, (cY
                    - (float) (Math.sqrt(3f)) * (length / 2f))};

            potentialNumber++;

        }


//        for(int i = 0; i < potentialNumber; i++){
////            for(int j = 0; j <= hexNumber; j++){
////                if(potentialPosi[i][0] == hexX1[j] && potentialPosi[i][1] == hexY1[j]){
////                    //delete this position if already ocupied by an existing hex - Bonny
////                    potentialPosi[i] = new float[]{-1,-1};
////                }
////            }
////
////        }
    }

    public void deleteHex(){
        if(hexNumber > 0 && whichHex !=0){
            for(int i = whichHex; i < hexNumber; i++){
                hexX1[i] = hexX1[i+1];
                hexY1[i] = hexY1[i+1];
            }

            hexX1[hexNumber] = initPosX;
            hexY1[hexNumber] = initPosY;


            whichHex = 0;
            hexNumber--;
            postInvalidate();
        }


//        if(hexNumber > 0){
//            hexX1[hexNumber] = initPosX;
//            hexY1[hexNumber] = initPosY;
//            hexNumber--;
//        }


    }

    public void rotateHex(){

        postInvalidate();
    }
}
