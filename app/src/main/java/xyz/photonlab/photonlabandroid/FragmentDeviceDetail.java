package xyz.photonlab.photonlabandroid;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Objects;

import okhttp3.Request;
import okhttp3.Response;
import xyz.photonlab.photonlabandroid.model.Device;
import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;
import xyz.photonlab.photonlabandroid.utils.NetworkCallback;
import xyz.photonlab.photonlabandroid.utils.NetworkHelper;
import xyz.photonlab.photonlabandroid.utils.NetworkNodeScanner;

public class FragmentDeviceDetail extends Fragment implements NetworkNodeScanner.OnSearchFinishedListener {

    private TextView fragment_title, tv_ip, tv_mac;
    private LinearLayout mask;
    private Button bt_research, bt_reset;
    private ImageButton bt_exit, bt_refresh;

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
        bt_reset = contentView.findViewById(R.id.reset);
        bt_research = contentView.findViewById(R.id.research);
    }

    @SuppressLint("SetTextI18n")
    private void addViewEvent() {
        bt_exit.setOnClickListener(v -> Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack());

        bt_refresh.setOnClickListener(v -> {
            if (!Session.getInstance().getLocalIP(getContext()).equals("")) {
                mask.setVisibility(View.VISIBLE);
                NetworkHelper helper = new NetworkHelper();
                Request request = new Request.Builder()
                        .url("http://" + Session.getInstance().getLocalIP(getContext()) + "/ip")
                        .get()
                        .build();
                helper.connect(request);
                helper.setCallback(new NetworkCallback() {
                    @Override
                    public void onSuccess(Response response) {
                        Activity activity = getActivity();
                        if (activity != null) {
                            activity.runOnUiThread(() -> {
                                Toast.makeText(activity, "Connected!", Toast.LENGTH_SHORT).show();
                                mask.setVisibility(View.GONE);
                            });
                        }

                    }

                    @Override
                    public void onFailed(String msg) {
                        Activity activity = getActivity();
                        if (activity != null) {
                            activity.runOnUiThread(() -> {
                                Toast.makeText(activity, R.string.not_connect, Toast.LENGTH_SHORT).show();
                                mask.setVisibility(View.GONE);
                            });
                        }
                    }
                });
            }
        });

        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Are you sure to reset?")
                .setIcon(R.drawable.lightbulb)
                .setCancelable(false)
                .setPositiveButton("Confirm", (dialog1, which) -> resetHardware())
                .setNegativeButton("Cancel", (dialog1, which) ->
                        dialog1.dismiss())
                .create();
        bt_reset.setOnClickListener(v -> dialog.show());

        bt_research.setOnClickListener(v -> {
            //TODO Add SubMask
            mask.setVisibility(View.VISIBLE);
            try {
                NetworkNodeScanner scanner = new NetworkNodeScanner(InetAddress.getByName("192.168.1.1"),
                        InetAddress.getByName("255.255.255.0"));
                scanner.setOnScanFinishedListener(this);
                scanner.scan();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });
    }

    private void resetHardware() {
        mask.setVisibility(View.VISIBLE);
        String ipAddr = Session.getInstance().getLocalIP(getContext());
        NetworkHelper helper = new NetworkHelper();
        Request req = new Request.Builder()
                .url("http://" + ipAddr + "/reset?secret=000000")
                .build();
        helper.setCallback(new NetworkCallback() {
            @Override
            public void onSuccess(Response response) {
                new TinyDB(getContext()).putString("LocalIp", "");
                Session.getInstance().setLocalIP("");
                Activity activity = getActivity();
                if (activity != null)
                    activity.runOnUiThread(() -> mask.setVisibility(View.GONE)
                    );

            }

            @Override
            public void onFailed(String msg) {
                onSuccess(null);
                Log.e("reset error", msg);

            }
        });
        helper.connect(req);
    }

    private void initTheme() {
        if (Session.getInstance().isDarkMode(getContext())) {
            Objects.requireNonNull(getView()).setBackgroundColor(Theme.Dark.MAIN_BACKGROUND);
            ((TextView) getView().findViewById(R.id.tvDevice)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) getView().findViewById(R.id.tv_ip_title)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) getView().findViewById(R.id.tv_mac_title)).setTextColor(Theme.Dark.SELECTED_TEXT);
            tv_mac.setTextColor(Theme.Dark.SELECTED_TEXT);
            tv_ip.setTextColor(Theme.Dark.SELECTED_TEXT);
            mask.setBackgroundColor(Color.parseColor("#88333333"));
            getView().findViewById(R.id.divider).setBackgroundColor(0xff888888);
        }
    }

    @Override
    public void onSearchFinished(Map<String, String> macToIp) {
        Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(() -> {
                mask.setVisibility(View.GONE);
                String newIp = macToIp.get(new TinyDB(getContext()).getString("lightMac").toLowerCase());
                if (newIp != null) {
                    tv_ip.setText(newIp);
                    Session.getInstance().setLocalIP(newIp);
                    new TinyDB(getContext()).putString("LocalIp", newIp);
                    Log.i("Device Search Completed", newIp);
                }
            });
        }
    }
}
