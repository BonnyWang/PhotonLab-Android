package xyz.photonlab.photonlabandroid;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;

class FragmentFirmwareUpdate extends Fragment {

    private ImageButton btn_exit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_firmware_upadate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        addViewEvent();
    }


    private void initView(@NonNull View contentView) {
        this.btn_exit = contentView.findViewById(R.id.backButton);
        if (Session.getInstance().isDarkMode(getContext())) {
            contentView.setBackgroundColor(Theme.Dark.MAIN_BACKGROUND);
            ((TextView) contentView.findViewById(R.id.textView2)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) contentView.findViewById(R.id.current_version)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) contentView.findViewById(R.id.tvMotion)).setTextColor(Theme.Dark.SELECTED_TEXT);
            btn_exit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EA7D38")));
        }
    }

    private void addViewEvent() {
        btn_exit.setOnClickListener(v -> Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack());
    }

}
