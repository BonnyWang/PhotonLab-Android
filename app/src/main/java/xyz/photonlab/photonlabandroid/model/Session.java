package xyz.photonlab.photonlabandroid.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import xyz.photonlab.photonlabandroid.ColorfulThemeClass;
import xyz.photonlab.photonlabandroid.TinyDB;
import xyz.photonlab.photonlabandroid.theme_Class;
import xyz.photonlab.photonlabandroid.views.Light;
import xyz.photonlab.photonlabandroid.views.LightStage;
import xyz.photonlab.photonlabandroid.views.MotherLight;

public class Session {

    private static Session instance;
    private static Bitmap shake;
    private ArrayList<theme_Class> mtheme;
    private ArrayList<theme_Class> mfavoriteTheme;
    private ArrayList<theme_Class> sweetTheme;
    private String localIP = "";
    private boolean permissionFlag = true;
    private boolean darkMode = false;

    private int currentThemeIndex = -1;

    private ArrayList<OnThemeChangeListener> onThemeChangeListeners = new ArrayList<>();

    private Session() {

    }


    public synchronized static Session getInstance() {
        if (instance == null)
            instance = new Session();
        return instance;
    }

    public static Bitmap getShake() {
        return shake;
    }

    public static void setShake(Bitmap shake) {
        Session.shake = shake;
    }

    public void requestTheme(Context context) {
        if (mtheme == null && mfavoriteTheme == null && sweetTheme == null) {
            loadTheme(context);
        }
    }

    private void loadTheme(Context context) {
        TinyDB tinyDB = new TinyDB(context);

        mtheme = new ArrayList<>();
        mtheme.add(new theme_Class("Spring", 0xff009e00, 0xfffcee21, "Photonlab", "Home Happy Sunset"));
        mtheme.add(new theme_Class("Fizzy Peach", 0xfff24645, 0xffebc08d, "Photonlab", "Sweet sweet"));
        mtheme.add(new theme_Class("Sky", 0xff00b7ff, 0xff00ffee, "Photonlab", "Blue Blue"));
        mtheme.add(new theme_Class("Neon Glow", 0xff00ffa1, 0xff00ffff, "Photonlab", "High"));
        mtheme.add(new ColorfulThemeClass("Rainbow", 0, 0, "Photonlab", "Honey"));

        int dlThemeNo = tinyDB.getInt("dlThemeNo");
        if (tinyDB.getInt("dlThemeNo") != -1) {
            for (int i = 0; i <= dlThemeNo; i++) {
                mtheme.add(tinyDB.getObject("dlTheme" + i, theme_Class.class));
            }
        }

        mfavoriteTheme = new ArrayList<>();
        List<Integer> favOrder;


        if (tinyDB.getListInt("favOrder").size() != 0) {
            favOrder = tinyDB.getListInt("favOrder");
            for (int i = 0; i < favOrder.size(); i++) {
                mfavoriteTheme.add(mtheme.get(favOrder.get(i)));
            }
        }

        sweetTheme = new ArrayList<>();
        sweetTheme.add(mtheme.get(1));
    }

    public ArrayList<theme_Class> getMtheme() {
        return mtheme;
    }

    public void setMtheme(ArrayList<theme_Class> mtheme) {
        this.mtheme = mtheme;
    }

    public ArrayList<theme_Class> getMfavoriteTheme() {
        return mfavoriteTheme;
    }

    public void setMfavoriteTheme(ArrayList<theme_Class> mfavoriteTheme) {
        this.mfavoriteTheme = mfavoriteTheme;
    }

    public ArrayList<theme_Class> getSweetTheme() {
        return sweetTheme;
    }

    public void setSweetTheme(ArrayList<theme_Class> sweetTheme) {
        this.sweetTheme = sweetTheme;
    }

    public int getCurrentThemeIndex(Context context) {
        if (currentThemeIndex == -1) {
            TinyDB tinyDB = new TinyDB(context);
            int dbIndex = tinyDB.getInt("current_theme_index");
            currentThemeIndex = dbIndex == -1 ? 0 : dbIndex;
        }
        return currentThemeIndex;
    }

    public void setCurrentThemeIndex(Context c, int currentThemeIndex) {
        if (currentThemeIndex > mtheme.size() || currentThemeIndex < 0)
            throw new IllegalArgumentException();
        TinyDB tinyDB = new TinyDB(c);
        tinyDB.putInt("current_theme_index", currentThemeIndex);
        this.currentThemeIndex = currentThemeIndex;
    }

    public LightStage requireLayoutStage(Context context, boolean plane) {
        return loadLayoutFromLocal(context, plane);
    }

    private LightStage loadLayoutFromLocal(Context context, boolean plane) {
        LightStage stage;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.openFileInput("layoutManager.data")));
            String json = reader.readLine();
            Log.e("read", json);
            stage = new LightStage(context);
            JSONObject src = new JSONObject(json);
            JSONArray lights = src.getJSONArray("lights");
            LightStage finalStage = stage;
            stage.setOnViewCreatedListener(() -> {
                try {
                    finalStage.getLights().clear();
                    finalStage.getDots().clear();
                    for (int i = 0; i < lights.length(); i++) {
                        Light light;
                        JSONObject o = lights.getJSONObject(i);
                        if (i == 0)
                            light = new MotherLight(context, (float) o.getDouble("x"), (float) o.getDouble("y"));
                        else
                            light = new Light((float) o.getDouble("x"), (float) o.getDouble("y"));
                        light.setDirection(o.getInt("direction"), false);
                        light.setNum(o.getLong("num"));
                        if (plane) {
                            light.setPlane(true);
                            if (light instanceof MotherLight)
                                ((MotherLight) light).preventIcon();
                        }
                        light.setPlaneColor(o.getInt("plane_color"));
                        finalStage.addLight(light);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            stage = null;
        }
        return stage;
    }

    public boolean saveLayoutToLocal(Context context, LightStage stage) {
        PrintWriter writer = null;
        try {
            //parse
            JSONObject lightStage = new JSONObject();
            JSONArray lights = new JSONArray();
            lightStage.put("timestamp", System.currentTimeMillis());
            lightStage.put("lights", lights);
            List<Light> lightList = stage.getLights();
            for (Light light : lightList) {
                lights.put(parseLight(light));
            }
            //save
            writer = new PrintWriter(context.openFileOutput("layoutManager.data", Context.MODE_PRIVATE));
            writer.println(lightStage);
            Log.e("save", lightStage.toString());
            writer.flush();
            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private JSONObject parseLight(Light light) {
        JSONObject object = new JSONObject();
        try {
            object.put("direction", light.getDirection());
            object.put("x", light.getX());
            object.put("y", light.getY());
            object.put("plane_color", light.getPlaneColor());
            object.put("num", light.getNum());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public String getLocalIP(Context context) {
        if (localIP.equals(""))
            localIP = new TinyDB(context).getString("LocalIp");
        return localIP;
    }

    public void setLocalIP(String localIP) {
        this.localIP = localIP;
    }

    public void setPermissionFlag(boolean permissionFlag) {
        this.permissionFlag = permissionFlag;
    }

    public boolean isPermissionFlag() {
        return permissionFlag;
    }

    public boolean isDarkMode(Context context) {
        TinyDB db = new TinyDB(context);
        if (db.getBoolean("darkMode"))
            darkMode = true;
        return darkMode;
    }

    public void setDarkMode(Context context, boolean darkMode) {
        this.darkMode = darkMode;
        TinyDB db = new TinyDB(context);
        db.putBoolean("darkMode", darkMode);
    }

    public interface OnThemeChangeListener {
        void initTheme(boolean dark);
    }

    public void addOnThemeChangeListener(@NonNull OnThemeChangeListener listener) {
        this.onThemeChangeListeners.add(listener);
    }

    public synchronized void notifyThemeChange(Context context) {
        boolean flag = isDarkMode(context);
        for (OnThemeChangeListener listener :
                onThemeChangeListeners) {
            listener.initTheme(flag);
        }
    }

}
