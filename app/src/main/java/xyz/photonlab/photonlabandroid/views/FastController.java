package xyz.photonlab.photonlabandroid.views;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

import xyz.photonlab.photonlabandroid.R;
import xyz.photonlab.photonlabandroid.TinyDB;
import xyz.photonlab.photonlabandroid.network.Api;

/**
 * Implementation of App Widget functionality.
 */
public class FastController extends AppWidgetProvider {

    private static final String TAG = "FastController";

    public static final String className = "xyz.photonlab.photonlabandroid.views.FastController";

    public static final String ALL = "xyz.photonlab.photonlabandroid.all";
    public static final String SWITCH_LIGHT = "xyz.photonlab.photonlabandroid.switchLight";
    public static final String DEGREE = "xyz.photonlab.photonlabandroid.degree";
    public static final String ADD = "xyz.photonlab.photonlabandroid.add";
    public static final String COLORS = "xyz.photonlab.photonlabandroid.colors";

    private static int[] buttonsIds = new int[]{R.id.bt_1, R.id.bt_2, R.id.bt_3, R.id.bt_4};

    private static void refreshViews(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        TinyDB tinyDB = new TinyDB(context);
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.fast_controller);

            int brightness = tinyDB.getInt("Brightness");
            if (brightness < 0)
                brightness = 0;
            if (brightness > 100)
                brightness = 100;
            //Brightness Text
            views.setTextViewText(R.id.tv_brightness, brightness + "");
            //Bottom Color Buttons
            int[] colors = new int[]{
                    context.getResources().getColor(R.color.yellow, null),
                    context.getResources().getColor(R.color.blue, null),
                    context.getResources().getColor(R.color.orange, null),
                    context.getResources().getColor(R.color.purple, null)};

            if (tinyDB.getInt("color0") != -1) {
                colors[0] = tinyDB.getInt("color0");
                colors[1] = tinyDB.getInt("color1");
                colors[2] = tinyDB.getInt("color2");
                colors[3] = tinyDB.getInt("color3");
            }

            int selectLump = tinyDB.getInt("colorGroupIndex");

            for (int i = 0; i < 4; i++) {
                if (i == selectLump) {
                    views.setImageViewBitmap(buttonsIds[i], resourceToBitmap(context, R.drawable.oval_selected, colors[i]));
                } else {
                    views.setImageViewBitmap(buttonsIds[i], resourceToBitmap(context, R.drawable.widget_oval, colors[i]));
                }
            }

            //Power Button
            if (tinyDB.getInt("Power") == 0 || tinyDB.getInt("Power") == -1) {
                views.setImageViewBitmap(R.id.ib_power, BitmapFactory.decodeResource(context.getResources(), R.drawable.power_off));
            } else {
                views.setImageViewBitmap(R.id.ib_power, BitmapFactory.decodeResource(context.getResources(), R.drawable.power_on));
            }

            views.setOnClickPendingIntent(R.id.ib_power, getPendingIntent(context, SWITCH_LIGHT, 0));

            //add/degree buttons
            views.setOnClickPendingIntent(R.id.add_brightness, getPendingIntent(context, ADD, 1));
            views.setOnClickPendingIntent(R.id.degree_brightness, getPendingIntent(context, DEGREE, 2));
            for (int i = 0; i < 4; i++) {
                views.setOnClickPendingIntent(buttonsIds[i], getPendingIntent(context, i, i + 3));
            }
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.i(TAG, "onUpdate: ");
        refreshViews(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        Log.i(TAG, "onEnabled: ");
    }

    @Override
    public void onDisabled(Context context) {
        Log.i(TAG, "onDisabled: ");
    }

    private static Bitmap resourceToBitmap(Context context, @DrawableRes int resId, @ColorInt int colorTint) {
        Drawable drawable = context.getResources().getDrawable(resId, null);
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.setTint(colorTint);
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();

        if (action == null || action.equals("android.appwidget.action.APPWIDGET_UPDATE"))
            return;
        Log.i(TAG, "onReceive: UPDATE_INTENT" + intent);
        AppWidgetManager manager = ((AppWidgetManager) context.getSystemService(Context.APPWIDGET_SERVICE));
        int[] widgetIds = manager.getAppWidgetIds(new ComponentName(context.getApplicationContext(), className));
        TinyDB tinyDB = new TinyDB(context);
        int brightness = tinyDB.getInt("Brightness");

        Vibrator vibrator = ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE));
        if (!action.equals(ALL))
            vibrator.vibrate(50);

        boolean power = tinyDB.getInt("Power") == 1;

        switch (action) {
            case SWITCH_LIGHT: {
                for (int widgetId : widgetIds) {
                    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.fast_controller);
                    if (power) {
                        views.setImageViewBitmap(R.id.ib_power, BitmapFactory.decodeResource(context.getResources(), R.drawable.power_off));
                    } else {
                        views.setImageViewBitmap(R.id.ib_power, BitmapFactory.decodeResource(context.getResources(), R.drawable.power_on));
                    }
                    manager.updateAppWidget(widgetId, views);
                }
                if (power) {
                    tinyDB.putInt("Power", 0);
                    Api.closeLight(context);
                } else {
                    tinyDB.putInt("Power", 1);
                    Api.setColor(context, tinyDB.getInt("colorGroup"), brightness);
                }
                Log.i(TAG, "onReceive: SWITCH_LIGHT");
                sendIntent(context, SWITCH_LIGHT);
            }
            break;
            case ALL:
                Log.i(TAG, "onReceive: all");
                refreshViews(context, manager, widgetIds);
                break;
            case ADD: {
                Log.i(TAG, "onReceive: add");
                if (!power)
                    return;
                int result = brightness + 5;
                if (result > 100)
                    result = 100;
                for (int widgetId : widgetIds) {
                    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.fast_controller);
                    views.setTextViewText(R.id.tv_brightness, result + "");
                    manager.updateAppWidget(widgetId, views);
                }
                tinyDB.putInt("Brightness", result);
                Api.setColor(context, tinyDB.getInt("colorGroup"), result);
                sendIntent(context, ADD);
            }
            break;
            case DEGREE: {
                if (!power)
                    return;
                Log.i(TAG, "onReceive: degree");
                int result = brightness - 5;
                if (result < 0)
                    result = 0;
                for (int widgetId : widgetIds) {
                    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.fast_controller);
                    views.setTextViewText(R.id.tv_brightness, result + "");
                    manager.updateAppWidget(widgetId, views);
                }
                tinyDB.putInt("Brightness", result);
                Api.setColor(context, tinyDB.getInt("colorGroup"), result);
                sendIntent(context, DEGREE);
            }
            break;
            case COLORS:
                if (!power)
                    return;
                int var = intent.getIntExtra("var", -1);
                Log.i(TAG, "onReceive: colors" + var);
                if (var == -1)
                    return;
                int[] colors = new int[]{
                        context.getResources().getColor(R.color.yellow, null),
                        context.getResources().getColor(R.color.blue, null),
                        context.getResources().getColor(R.color.orange, null),
                        context.getResources().getColor(R.color.purple, null)};

                if (tinyDB.getInt("color0") != -1) {
                    colors[0] = tinyDB.getInt("color0");
                    colors[1] = tinyDB.getInt("color1");
                    colors[2] = tinyDB.getInt("color2");
                    colors[3] = tinyDB.getInt("color3");
                }

                Api.setColor(context, colors[var], brightness);

                for (int widgetId : widgetIds) {
                    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.fast_controller);

                    for (int i = 0; i < 4; i++) {
                        if (i == var) {
                            views.setImageViewBitmap(buttonsIds[i], resourceToBitmap(context, R.drawable.oval_selected, colors[i]));
                        } else {
                            views.setImageViewBitmap(buttonsIds[i], resourceToBitmap(context, R.drawable.widget_oval, colors[i]));
                        }
                    }
                    manager.updateAppWidget(widgetId, views);
                }
                tinyDB.putInt("colorGroup", colors[var]);
                tinyDB.putInt("colorGroupIndex", var);
                sendIntent(context, COLORS);
                break;
        }
    }

    private static PendingIntent getPendingIntent(Context context, String action, int flag) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.setComponent(new ComponentName(context.getApplicationContext(), className));
        return PendingIntent.getBroadcast(context, 0, intent, flag);
    }

    private static PendingIntent getPendingIntent(Context context, int var, int flag) {
        Log.i(TAG, "getPendingIntent: " + var);
        Intent intent = new Intent();
        intent.setAction(FastController.COLORS);
        intent.putExtra("var", var);
        intent.setComponent(new ComponentName(context.getApplicationContext(), className));
        return PendingIntent.getBroadcast(context, 0, intent, flag);
    }

    private static void sendIntent(Context context, String action) {
        Intent toSend = new Intent(action);
        context.sendBroadcast(toSend);
    }

}



