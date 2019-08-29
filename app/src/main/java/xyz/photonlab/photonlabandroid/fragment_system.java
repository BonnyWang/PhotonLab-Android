package xyz.photonlab.photonlabandroid;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;


@SuppressLint("SetTextI18n")
public class fragment_system extends FullScreenFragment {

    ImageButton btBack;

    ConstraintLayout firmware_upadate;
    Switch switch_dark_mode;

    TinyDB tinyDB;
    String ipAddr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tinyDB = new TinyDB(getContext());
        ipAddr = Session.getInstance().getLocalIP(getContext());

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_system, container, false);

        btBack = view.findViewById(R.id.backButton_System);
        btBack.setOnClickListener(v -> Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack());
        switch_dark_mode = view.findViewById(R.id.switch_dark_mode);
        switch_dark_mode.setChecked(Session.getInstance().isDarkMode(getContext()));
        switch_dark_mode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Session.getInstance().setDarkMode(getContext(), isChecked);
            Session.getInstance().notifyThemeChange(getContext());
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
        });

        firmware_upadate = view.findViewById(R.id.firmware_update);

        firmware_upadate.setOnClickListener(v -> {
            FragmentTransaction tx = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            tx.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
            tx.replace(R.id.container, new FirmwareUpdateFragment()).addToBackStack(null);
            tx.commit();
        });
        if (Session.getInstance().isDarkMode(getContext())) {
            view.setBackgroundColor(Theme.Dark.MAIN_BACKGROUND);
            ((TextView) view.findViewById(R.id.tv_dark_mode)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) view.findViewById(R.id.tvDeviceName1)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) view.findViewById(R.id.tvSystem)).setTextColor(Theme.Dark.SELECTED_TEXT);
            btBack.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EA7D38")));
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

}
