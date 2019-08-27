package xyz.photonlab.photonlabandroid;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;

public class FragmentAbout extends Fragment {

    ConstraintLayout policy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        policy = view.findViewById(R.id.clPrivacy);
        policy.setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.photonlab.xyz/privacypolicy.html"))));
        ImageButton btBack = view.findViewById(R.id.backButton_System);
        btBack.setOnClickListener(v -> Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack());
        if (Session.getInstance().isDarkMode(getContext())) {
            view.setBackgroundColor(Theme.Dark.MAIN_BACKGROUND);
            ((TextView) view.findViewById(R.id.tvSystem)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) view.findViewById(R.id.tvThemeTrigger)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) view.findViewById(R.id.tvAppSoft)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) view.findViewById(R.id.tvVersion)).setTextColor(Theme.Dark.SELECTED_TEXT);
            btBack.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EA7D38")));
        }
    }
}
