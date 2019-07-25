package xyz.photonlab.photonlabandroid;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;

/**
 * this Fragment will hide default navigation bar
 */
public class FullScreenFragment extends Fragment {

    private View navBar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(context.toString(), "");
        navBar = ((Activity) context).findViewById(R.id.nav_view);
        if (navBar != null) {
            navBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (navBar != null)
            navBar.setVisibility(View.VISIBLE);
    }
}
