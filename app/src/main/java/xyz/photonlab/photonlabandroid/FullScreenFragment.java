package xyz.photonlab.photonlabandroid;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.fragment.app.Fragment;

/**
 * this Fragment will hide default navigation bar
 */
public class FullScreenFragment extends Fragment {

    private static Animation showAnimation;
    private static Animation hideAnimation;

    private View navBar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(context.toString(), "");

        showAnimation = AnimationUtils.loadAnimation(context, R.anim.float_up);
        hideAnimation = AnimationUtils.loadAnimation(context, R.anim.float_down);

        navBar = ((Activity) context).findViewById(R.id.nav_view);
        if (navBar != null) {
            navBar.clearAnimation();
            navBar.startAnimation(hideAnimation);
            navBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (navBar != null) {
            navBar.clearAnimation();
            navBar.startAnimation(showAnimation);
            navBar.setVisibility(View.VISIBLE);
        }
    }
}
