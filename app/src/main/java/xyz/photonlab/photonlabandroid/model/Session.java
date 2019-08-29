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

import xyz.photonlab.photonlabandroid.TinyDB;
import xyz.photonlab.photonlabandroid.fragment_layout;
import xyz.photonlab.photonlabandroid.views.Light;
import xyz.photonlab.photonlabandroid.views.LightStage;
import xyz.photonlab.photonlabandroid.views.MotherLight;

public class Session {

    private static Session instance;
    private static Bitmap shake;
    private ArrayList<MyTheme> allThemes;
    private String localIP = "";
    private boolean permissionFlag = true;
    private boolean darkMode = false;

    private ArrayList<OnThemeChangeListener> onThemeChangeListeners = new ArrayList<>();
    private ArrayList<fragment_layout.OnSavedLayoutListener> onSavedLayoutListeners = new ArrayList<>();
    private String mac;

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
        if (allThemes == null) {
            allThemes = new ArrayList<>();
            loadTheme(context);
        }
    }

    private void loadTheme(Context context) {
        //read the file
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(context.openFileInput("themes")))) {
            //dynamic themes
            String data = reader.readLine();
            JSONArray jThemes = new JSONArray(data);
            for (int i = 0; i < jThemes.length(); i++) {
                JSONObject jTheme = jThemes.getJSONObject(i);
                JSONArray jGradientColor = jTheme.getJSONArray("gradientColors");
                JSONArray jVars = jTheme.getJSONArray("vars");
                int[] gradientColor = new int[jGradientColor.length()];
                int[] vars = new int[jVars.length()];
                for (int j = 0; j < jGradientColor.length(); j++) {
                    gradientColor[j] = jGradientColor.getInt(j);
                }
                for (int j = 0; j < jVars.length(); j++) {
                    vars[j] = jVars.getInt(j);
                }

                MyTheme myTheme = new MyTheme(
                        jTheme.getString("creater"),
                        jTheme.getString("mood"),
                        jTheme.getString("name"),
                        jTheme.getInt("number"),
                        gradientColor,
                        vars,
                        jTheme.getBoolean("fav"),
                        jTheme.getBoolean("music"));
                allThemes.add(myTheme);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //static themes
            allThemes.clear();
            Log.i("Session", "Generate Themes");
            allThemes.add(new MyTheme("Photonlab",
                    "Home Happy Sunset",
                    "Spring",
                    2, new int[]{0xff009e00, 0xfffcee21}, new int[]{0, 255, 0, 100, 120, 0, 20}, false, false));
            allThemes.add(new MyTheme("Photonlab",
                    "Honey",
                    "Tri Impulse",
                    10, new int[]{0xffff0000, 0xff0000ff}, new int[]{255, 0, 0, 0, 0, 255, 15}, false, false));
            allThemes.add(new MyTheme("Photonlab",
                    "Sweet sweet",
                    "Fizzy Peach",
                    13, new int[]{0xfff24645, 0xffebc08d}, new int[]{100, 20, 20}, false, true));
            allThemes.add(new MyTheme("Photonlab",
                    "High",
                    "Neon Glow",
                    6, new int[]{0xff00ffa1, 0xff00ffff}, new int[]{255, 0, 0, 0, 0, 255, 15}, false, false));
            allThemes.add(new MyTheme("Photonlab",
                    "Honey",
                    "Rainbow",
                    11, new int[]{0xfff72323, 0xfff7ec23, 0xff27f723, 0xff232ef7, 0xfff023f7}, new int[]{40, 2}, false, false));
            allThemes.add(new MyTheme("Photonlab",
                    "Honey",
                    "Rainbow-Rainbow",
                    12, new int[]{0xfff72323, 0xfff7ec23, 0xff27f723, 0xff232ef7, 0xfff023f7}, new int[]{40, 2}, false, false));
            saveTheme(context);
        }
    }

    public void saveTheme(Context context) {
        try (PrintWriter writer = new PrintWriter(context.openFileOutput("themes", Context.MODE_PRIVATE))) {
            JSONArray jArray = new JSONArray();
            for (int n = 0; n < allThemes.size(); n++) {
                MyTheme theme = allThemes.get(n);
                JSONObject jTheme = new JSONObject();
                jTheme.put("creater", theme.getCreater());
                jTheme.put("mood", theme.getMood());
                jTheme.put("name", theme.getName());
                jTheme.put("number", theme.getNumber());
                jTheme.put("fav", theme.isFavorite());
                jTheme.put("music", theme.isMusic());

                JSONArray jGradientColors = new JSONArray();
                JSONArray jVars = new JSONArray();

                int[] gradientColors = theme.getGradientColors();
                int[] vars = theme.getVars();

                for (int i : gradientColors) {
                    jGradientColors.put(i);
                }
                for (int i : vars) {
                    jVars.put(i);
                }
                jTheme.put("gradientColors", jGradientColors);
                jTheme.put("vars", jVars);
                jArray.put(jTheme);
            }
            Log.e("Themes", jArray.toString());
            writer.println(jArray);
            writer.flush();
        } catch (Exception e) {
            Log.e("SESSION", "Theme Save Error");
        }
    }

    public ArrayList<MyTheme> getAllThemes() {
        return allThemes;
    }

    public String getMac(Context context) {
        if (this.mac == null) {
            mac = new TinyDB(context).getString("lightMac");
        }
        return mac;
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

        } catch (
                Exception e) {
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

    public void notifyLayoutChanged() {
        for (fragment_layout.OnSavedLayoutListener listener : onSavedLayoutListeners) {
            listener.onSavedLayout(true);
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

    public void saveLightMac(Context c, String mac) {
        TinyDB db = new TinyDB(c);
        db.putString("lightMac", mac);
        this.mac = mac;
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

    public void registerOnLayoutSaveListener(fragment_layout.OnSavedLayoutListener listener) {
        this.onSavedLayoutListeners.add(listener);
    }

}
