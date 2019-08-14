package xyz.photonlab.photonlabandroid.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import java.util.List;

import xyz.photonlab.photonlabandroid.model.Session;

import static xyz.photonlab.photonlabandroid.views.LightStage.offsetX;
import static xyz.photonlab.photonlabandroid.views.LightStage.offsetY;

public class MotherLight extends Light {

    private Bitmap shake;
    private Paint paint;
    private Matrix matrix;
    private boolean showShake = true;

    private MotherLight(float x, float y) {
        super(x, y);
        setSettled(true);
    }

    public MotherLight(Context context, float x, float y) {
        this(x, y);
        Log.i("shakeContext", context.toString());
        Bitmap src = Session.getShake();
        shake = Bitmap.createScaledBitmap(src, (int) RADIUS, (int) RADIUS, false);
        Log.i("shakeBitmap", shake + "");
        paint = new Paint();
        matrix = new Matrix();
    }


    @Override
    public void update(List<Light> light) {
        super.update(light);
        currentColor = Color.rgb(12, 126, 252);
        setSettled(true);
    }

    @Override
    void draw(Canvas canvas) {
        super.draw(canvas);
        if (shake != null && showShake) {
            matrix.reset();
            matrix.postRotate(60 * (originDirection - 1), (int) (0.5 * RADIUS), (int) (0.5 * RADIUS));
            matrix.postTranslate(x + offsetX - 0.5f * RADIUS, y + offsetY - 0.5f * RADIUS);
            canvas.drawBitmap(shake, matrix, paint);
        }
    }

    public void preventIcon() {
        this.showShake = false;
    }
}