package xyz.photonlab.photonlabandroid;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;


/**
 * A simple {@link Fragment} subclass.
 */
public class FirmwareUpdateFragment extends Fragment {

    private ConstraintLayout mainContainer, panelContainer;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_firm_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initView
        mainContainer = view.findViewById(R.id.firmware_update);
        panelContainer = view.findViewById(R.id.dark_mode);

        mainContainer.setOnClickListener(v -> {
            FragmentTransaction tx = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            tx.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
            tx.replace(R.id.container, new FirmwareUpdateIndividualFragment(FirmwareUpdateIndividualFragment.FragmentType.MAIN)).addToBackStack(null);
            tx.commit();
        });

        panelContainer.setOnClickListener(v -> {
            FragmentTransaction tx = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            tx.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
            tx.replace(R.id.container, new FirmwareUpdateIndividualFragment(FirmwareUpdateIndividualFragment.FragmentType.PANEL)).addToBackStack(null);
            tx.commit();
        });

        view.findViewById(R.id.backButton_System).setOnClickListener(v -> Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack());

        initTheme();

    }

    private void initTheme() {
        if (Session.getInstance().isDarkMode(getContext())) {
            Objects.requireNonNull(getView()).setBackgroundColor(Theme.Dark.MAIN_BACKGROUND);
            ((TextView) getView().findViewById(R.id.tv_dark_mode)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) getView().findViewById(R.id.tvDeviceName1)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) getView().findViewById(R.id.tvSystem)).setTextColor(Theme.Dark.SELECTED_TEXT);
        }
    }
}
