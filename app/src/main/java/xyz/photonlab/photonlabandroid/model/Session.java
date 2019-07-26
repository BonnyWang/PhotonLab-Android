package xyz.photonlab.photonlabandroid.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import xyz.photonlab.photonlabandroid.TinyDB;
import xyz.photonlab.photonlabandroid.theme_Class;

public class Session {

    private static Session instance;

    private ArrayList<theme_Class> mtheme;
    private ArrayList<theme_Class> mfavoriteTheme;
    private ArrayList<theme_Class> sweetTheme;

    private int currentThemeIndex = -1;

    private Session() {

    }

    public synchronized static Session getInstance() {
        if (instance == null)
            instance = new Session();
        return instance;
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
        sweetTheme.add(new theme_Class("Fizzy Peach", 0xfff24645, 0xffebc08d, "Photonlab", "Sweet sweet"));
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
}
