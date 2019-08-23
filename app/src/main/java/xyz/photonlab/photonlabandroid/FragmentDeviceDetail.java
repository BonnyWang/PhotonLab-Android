package xyz.photonlab.photonlabandroid;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

import xyz.photonlab.photonlabandroid.model.Device;
import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;

public class FragmentDeviceDetail extends Fragment {

    private TextView fragment_title, tv_ip, tv_mac;
    private LinearLayout mask;
    private Button bt_exit, bt_refresh;

    private Device device;

    public FragmentDeviceDetail() {
        this.device = new Device("Unknown", "0.0.0.0", "00:00:00:00");
    }

    public FragmentDeviceDetail(@NonNull Device device) {
        this.device = device;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        addViewEvent();
        initTheme();
        tv_ip.setText(device.getIp());
        tv_mac.setText(device.getMac());
        fragment_title.setText(device.getName());
    }

    private void initView(View contentView) {
        bt_exit = contentView.findViewById(R.id.exit);
        tv_ip = contentView.findViewById(R.id.tv_ip_value);
        tv_mac = contentView.findViewById(R.id.tv_mac_value);
        mask = contentView.findViewById(R.id.mask);
        bt_refresh = contentView.findViewById(R.id.refresh);
        fragment_title = contentView.findViewById(R.id.tvDevice);
    }

    private void addViewEvent() {
        bt_exit.setOnClickListener(v -> Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack());
        //TODO add refresh agent
        bt_refresh.setOnClickListener(v -> mask.setVisibility(View.VISIBLE));
    }

    private void initTheme() {
        if (Session.getInstance().isDarkMode(getContext())) {
            Objects.requireNonNull(getView()).setBackgroundColor(Theme.Dark.MAIN_BACKGROUND);
            ((TextView) getView().findViewById(R.id.tvDevice)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) getView().findViewById(R.id.tv_ip_title)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) getView().findViewById(R.id.tv_mac_title)).setTextColor(Theme.Dark.SELECTED_TEXT);
            tv_mac.setTextColor(Theme.Dark.SELECTED_TEXT);
            tv_ip.setTextColor(Theme.Dark.SELECTED_TEXT);
        }
    }
}
