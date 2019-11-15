package xyz.photonlab.photonlabandroid.utils;

import android.view.View;
import android.view.ViewGroup;

/**
 * created by KIO on 2019/11/8
 */
public class ViewGroupDisableHelper {

    public static void disableViewGroup(ViewGroup viewGroup) {
        try {
            handleViewGroup(viewGroup, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void enableViewGroup(ViewGroup viewGroup) {
        try {
            handleViewGroup(viewGroup, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleViewGroup(ViewGroup viewGroup, boolean enable) {
        int cCount = viewGroup.getChildCount();
        for (int i = 0; i < cCount; i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                handleViewGroup(((ViewGroup) child), enable);
            } else {
                child.setEnabled(enable);
            }
        }
    }

}
