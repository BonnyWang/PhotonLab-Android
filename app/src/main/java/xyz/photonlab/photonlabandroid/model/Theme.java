package xyz.photonlab.photonlabandroid.model;

import android.graphics.Color;

import kotlin.Suppress;

/**
 * created by KIO on 2019/8/19
 */
public class Theme {

    public static abstract class ThemeColors {
        public static final int MAIN_BACKGROUND = Color.parseColor("#FAFAFA");
        public static final int SELECTED_TEXT = Color.rgb(220, 220, 220);
        public static final int UNSELECTED_TEXT = Color.rgb(100, 100, 100);
        public static final int TITLE = Color.TRANSPARENT;
    }

    public final static class Normal extends ThemeColors {
        public static final int MAIN_BACKGROUND = Color.parseColor("#FAFAFA");
        public static final int SELECTED_TEXT = Color.parseColor("#4a4a4a");
        public static final int UNSELECTED_TEXT = Color.parseColor("#ededed");
        public static final int CARD_BACKGROUND = Color.parseColor("#FAFAFA");
        public static final int TITLE = Color.parseColor("#000000");
    }

    public static class Dark extends ThemeColors {
        public static final int MAIN_BACKGROUND = Color.parseColor("#2C2D31");
        public static final int SELECTED_TEXT = Color.parseColor("#ffffff");
        public static final int UNSELECTED_TEXT = Color.parseColor("#6a6a6a");
        public static final int CARD_BACKGROUND = Color.parseColor("#2E2F33");
        public static final int TITLE = Color.parseColor("#ffffff");
    }

}
