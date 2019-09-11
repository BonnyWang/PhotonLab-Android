package xyz.photonlab.photonlabandroid;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;

import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;

/**
 * this Fragment will hide default navigation bar
 */
public class NormalStatusBarFragment extends Fragment {

    Activity mActivity;

    int defaultColor = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
        if (mActivity != null) {
            int color;
            if (Session.getInstance().isDarkMode(mActivity)) {
                color = Theme.Dark.MAIN_BACKGROUND;
            } else {
                color = Theme.Normal.MAIN_BACKGROUND;
            }
            defaultColor = mActivity.getWindow().getStatusBarColor();
            mActivity.getWindow().setStatusBarColor(color);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity.getWindow().setStatusBarColor(defaultColor);
    }
}
