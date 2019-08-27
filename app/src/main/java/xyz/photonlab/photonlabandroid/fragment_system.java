package xyz.photonlab.photonlabandroid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

import okhttp3.Request;
import okhttp3.Response;
import xyz.photonlab.photonlabandroid.model.Theme;
import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.utils.NetworkCallback;
import xyz.photonlab.photonlabandroid.utils.NetworkHelper;


@SuppressLint("SetTextI18n")
public class fragment_system extends FullScreenFragment {

    ImageButton btBack;

    ProgressBar loading;

    ConstraintLayout pairState, reset_container, firmware_upadate;
    Switch switch_dark_mode;
    TextView tvDeviceName;

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

        pairState = view.findViewById(R.id.pair_state);
        pairState.setOnClickListener((v) -> {
            loading.setVisibility(View.VISIBLE);
            if (!ipAddr.equals("")) {
                //new JsonTask().execute("http://" + ipAddr + "/ip");
                NetworkHelper helper = new NetworkHelper();
                Request request = new Request.Builder()
                        .url("http://" + ipAddr + "/ip")
                        .get()
                        .build();
                helper.connect(request);
                helper.setCallback(new NetworkCallback() {
                    @Override
                    public void onSuccess(Response response) {
                        Activity activity = getActivity();
                        if (activity != null) {
                            activity.runOnUiThread(() -> {
                                tvDeviceName.setText(ipAddr);
                                loading.setVisibility(View.GONE);
                            });
                        }
                    }

                    @Override
                    public void onFailed(String msg) {
                        Activity activity = getActivity();
                        if (activity != null) {
                            activity.runOnUiThread(() -> {
                                tvDeviceName.setText("Not Connect");
                                loading.setVisibility(View.GONE);
                            });
                        }
                    }
                });
            } else {
                tvDeviceName.setText("No Device");
                loading.setVisibility(View.GONE);
            }
        });

        reset_container = view.findViewById(R.id.reset_container);
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Are you sure to reset?")
                .setIcon(R.drawable.lightbulb)
                .setCancelable(false)
                .setPositiveButton("Confirm", (dialog1, which) -> resetHardware())
                .setNegativeButton("Cancel", (dialog1, which) ->
                        dialog1.dismiss())
                .create();
        reset_container.setOnClickListener(v -> dialog.show());
        loading = view.findViewById(R.id.pair_state_load);
        tvDeviceName = view.findViewById(R.id.tvDeviceName);
        firmware_upadate = view.findViewById(R.id.firmware_update);

        firmware_upadate.setOnClickListener(v -> {
            FragmentTransaction tx = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            tx.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
            tx.replace(R.id.container, new FragmentFirmwareUpdate()).addToBackStack(null);
            tx.commit();
        });
        if (Session.getInstance().isDarkMode(getContext())) {
            view.setBackgroundColor(Theme.Dark.MAIN_BACKGROUND);
            ((TextView) view.findViewById(R.id.tvDeviceName)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) view.findViewById(R.id.tv_dark_mode)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) view.findViewById(R.id.tvDeviceName1)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) view.findViewById(R.id.tvReset)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) view.findViewById(R.id.tvSystem)).setTextColor(Theme.Dark.SELECTED_TEXT);
            btBack.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EA7D38")));
        }
        return view;
    }

    private void resetHardware() {
        if (!ipAddr.equals("")) {//has device
            NetworkHelper helper = new NetworkHelper();
            Request req = new Request.Builder()
                    .url("http://" + ipAddr + "/reset?secret=000000")
                    .build();
            helper.setCallback(new NetworkCallback() {
                @Override
                public void onSuccess(Response response) {
                    ipAddr = "";
                    tinyDB.putString("LocalIp", "");
                    Session.getInstance().setLocalIP("");
                    Activity activity = getActivity();
                    if (activity != null)
                        activity.runOnUiThread(() -> {
                                    Toast.makeText(getContext(), "Reset Succeed", Toast.LENGTH_SHORT).show();
                                    pairState.performClick();
                                }
                        );

                }

                @Override
                public void onFailed(String msg) {
                    Activity activity = getActivity();
                    if (activity != null)
                        activity.runOnUiThread(() ->
                                Toast.makeText(getContext(), "Reset Failed:" + msg, Toast.LENGTH_LONG).show());
                    Log.e("reset error", msg);
                }
            });
            helper.connect(req);
        } else {
            Toast.makeText(getContext(), "Please Pair First", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (pairState != null)
            pairState.performClick();
    }

}
