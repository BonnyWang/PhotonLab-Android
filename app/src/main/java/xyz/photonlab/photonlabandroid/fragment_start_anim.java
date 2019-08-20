package xyz.photonlab.photonlabandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import xyz.photonlab.photonlabandroid.R;
import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;


public class fragment_start_anim extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start_anim_layout, container, false);
        if (Session.getInstance().isDarkMode(getContext())) {
            ((ImageView) view.findViewById(R.id.imageView2)).setImageResource(R.drawable.logo_text_light);
            view.setBackgroundColor(Theme.Dark.MAIN_BACKGROUND);
        }
        return view;
    }

}
