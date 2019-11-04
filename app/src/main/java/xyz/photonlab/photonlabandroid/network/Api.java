package xyz.photonlab.photonlabandroid.network;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.ColorInt;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.utils.NetworkHelper;

/**
 * created by KIO on 2019/10/28
 */
public class Api {

    private static NetworkHelper helper = new NetworkHelper();

    public static void closeLight(@NotNull Context context) {
        setColor(context, Color.BLACK, 0);
    }

    public static void setColor(@NotNull Context context, @ColorInt int color, int brightness) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = hsv[2] * ((float) brightness) / 100f;
        color = Color.HSVToColor(255, hsv);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        String url = "http://" + Session.getInstance().getLocalIP(context) + "/" + "mode" + "/all"
                + "?red=" + r
                + "&green=" + g
                + "&blue=" + b;
        Log.i("api", url);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        helper.connect(request, new OkHttpClient.Builder().readTimeout(10, TimeUnit.MILLISECONDS).build());
    }

}
