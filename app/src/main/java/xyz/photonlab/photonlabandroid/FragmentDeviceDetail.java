package xyz.photonlab.photonlabandroid;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xyz.photonlab.photonlabandroid.model.Device;
import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;
import xyz.photonlab.photonlabandroid.utils.NetworkCallback;
import xyz.photonlab.photonlabandroid.utils.NetworkHelper;
import xyz.photonlab.photonlabandroid.utils.NetworkNodeScanner;

public class FragmentDeviceDetail extends Fragment implements NetworkNodeScanner.OnSearchFinishedListener, NetworkNodeScanner.OnSearchProgressChangedListener {

    private TextView fragment_title, tv_ip, tv_mac;
    private LinearLayout mask;
    private Button bt_research, bt_reset;
    private ImageButton bt_exit, bt_refresh;
    private TextView tv_progress;

    private Device device;
    private FragmentActivity mActivity;

    private NetworkNodeScanner scanner;

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
        tv_progress = contentView.findViewById(R.id.tv_progress);
    }

    @SuppressLint("SetTextI18n")
    private void addViewEvent() {
        bt_exit.setOnClickListener(v -> Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack());

        bt_refresh.setOnClickListener(v -> {
            if (!Session.getInstance().getLocalIP(mActivity).equals("")) {
                mask.setVisibility(View.VISIBLE);
                tv_progress.setVisibility(View.GONE);
                NetworkHelper helper = new NetworkHelper();
                Request request = new Request.Builder()
                        .url("http://" + Session.getInstance().getLocalIP(mActivity) + "/ip")
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

        final AlertDialog dialog = new AlertDialog.Builder(mActivity)
                .setTitle("Are you sure to reset?")
                .setIcon(R.drawable.lightbulb)
                .setCancelable(false)
                .setPositiveButton("Confirm", (dialog1, which) -> resetHardware())
                .setNegativeButton("Cancel", (dialog1, which) ->
                        dialog1.dismiss())
                .create();
        bt_reset.setOnClickListener(v -> dialog.show());

        bt_research.setOnClickListener(v -> {
            mask.setVisibility(View.VISIBLE);
            tv_progress.setVisibility(View.VISIBLE);
            tv_progress.setText("0%");
            try {
                scanner = new NetworkNodeScanner(InetAddress.getByName("192.168.1.1"),
                        InetAddress.getByName("255.255.255.0"));
                scanner.setOnSearchProgressChangedListener(this);
                scanner.setOnScanFinishedListener(this);
                scanner.scan();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                mask.setVisibility(View.GONE);
            }
        });
    }

    private void resetHardware() {
        if (Session.getInstance().getLocalIP(mActivity).equals("")) {
            Toast.makeText(mActivity, "Current Device Unavailable", Toast.LENGTH_SHORT).show();
            return;
        }
        mask.setVisibility(View.VISIBLE);
        tv_progress.setVisibility(View.GONE);
        String ipAddr = Session.getInstance().getLocalIP(mActivity);
        NetworkHelper helper = new NetworkHelper();
        Request req = new Request.Builder()
                .url("http://" + ipAddr + "/reset?secret=000000")
                .build();
        helper.setCallback(new NetworkCallback() {
            @Override
            public void onSuccess(Response response) {
                new TinyDB(mActivity).putString("LocalIp", "");
                Session.getInstance().setLocalIP("");
                mActivity.runOnUiThread(() -> {
                    mask.setVisibility(View.GONE);
                    if (Session.getInstance().isDarkMode(mActivity))
                        tv_ip.setTextColor(Theme.Dark.UNSELECTED_TEXT);
                    else
                        tv_ip.setTextColor(Theme.Normal.SELECTED_TEXT);
                });

            }

            @Override
            public void onFailed(String msg) {
                onSuccess(null);
                Log.e("reset error", msg);

            }
        });
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(2000, TimeUnit.MILLISECONDS).build();
        helper.connect(req, client);
    }

    private void initTheme() {
        if (Session.getInstance().isDarkMode(mActivity)) {
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
        mActivity.runOnUiThread(() -> {
            mask.setVisibility(View.GONE);
            String newIp = macToIp.get(new TinyDB(mActivity).getString("lightMac").toLowerCase());
            if (newIp != null) {
                tv_ip.setText(newIp);
                Session.getInstance().setLocalIP(newIp);
                new TinyDB(mActivity).putString("LocalIp", newIp);
                Log.i("Device Search Completed", newIp);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onProgressChanged(float progress) {
        mActivity.runOnUiThread(() -> this.tv_progress.setText(String.format(Locale.ENGLISH, "%.2f", progress) + "%"));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = getActivity();
    }

    @Override
    public void onDetach() {
        if (scanner != null)
            scanner.stop();
        super.onDetach();
    }
}
