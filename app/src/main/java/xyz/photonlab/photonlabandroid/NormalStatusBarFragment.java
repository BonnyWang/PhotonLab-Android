package xyz.photonlab.photonlabandroid;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;

import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;

/**
 * this Fragment will hide default navigation bar
 */
public abstract class NormalStatusBarFragment extends Fragment {

    Activity mActivity;

    int defaultColor = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
