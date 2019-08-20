package xyz.photonlab.photonlabandroid;

import android.app.Application;
import android.util.Log;

import xyz.photonlab.photonlabandroid.model.Session;

/**
 * created by KIO on 2019/8/20
 */
public class MyApplication extends Application implements Session.OnThemeChangeListener {

    private final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        Log.i(TAG,"On create");
        Session.getInstance().addOnThemeChangeListener(this);
        initTheme(Session.getInstance().isDarkMode(this));
        super.onCreate();
    }

    @Override
    public void initTheme(boolean dark) {
            setTheme(R.style.AppTheme);
    }
}
