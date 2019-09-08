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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import okhttp3.Request;
import okhttp3.Response;
import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;
import xyz.photonlab.photonlabandroid.utils.NetworkCallback;
import xyz.photonlab.photonlabandroid.utils.NetworkHelper;

class FirmwareUpdateIndividualFragment extends Fragment {

    public enum FragmentType {
        MAIN,
        PANEL
    }

    private ImageButton btn_exit;
    private FragmentType mType;
    private ProgressBar progressBar;

    public FirmwareUpdateIndividualFragment(FragmentType type) {
        this.mType = type;
    }

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
        if (mType == FragmentType.MAIN) {
            refreshCurrentVersion();
        } else {
            refreshPanelsDetected();
            Objects.requireNonNull(getView()).findViewById(R.id.switch_detect).setAlpha(1f);
        }
    }


    @SuppressLint("SetTextI18n")
    private void initView(@NonNull View contentView) {
        this.btn_exit = contentView.findViewById(R.id.backButton);
        progressBar = contentView.findViewById(R.id.load);
        if (Session.getInstance().isDarkMode(getContext())) {
            contentView.setBackgroundColor(Theme.Dark.MAIN_BACKGROUND);
            ((TextView) contentView.findViewById(R.id.textView2)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) contentView.findViewById(R.id.current_version)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) contentView.findViewById(R.id.tvMotion)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) contentView.findViewById(R.id.up_to_date)).setTextColor(0xffcccccc);
            btn_exit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EA7D38")));
        }
        if (mType == FragmentType.MAIN) {
            ((TextView) contentView.findViewById(R.id.tvMotion)).setText("Main Controller");
        } else {
            ((TextView) contentView.findViewById(R.id.tvMotion)).setText("Light Panels");
            ((TextView) contentView.findViewById(R.id.current_version)).setText("Panels Detected");
            ((TextView) contentView.findViewById(R.id.tvCallDialog)).setText("0");
        }
    }

    private void addViewEvent() {
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Notice")
                .setMessage("Are you sure to update the firmware? This process might take a while.")
                .setIcon(R.drawable.lightbulb)
                .setCancelable(false)
                .setPositiveButton("Confirm", (dialog1, which) -> downLoadFirmware())
                .setNegativeButton("Cancel", (dialog1, which) ->
                        dialog1.dismiss())
                .create();
        btn_exit.setOnClickListener(v -> Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack());
        Objects.requireNonNull(getView()).findViewById(R.id.switch_detect).setOnClickListener(v -> {
            if (v.getAlpha() != 1f)
                return;
            dialog.show();
        });
        Objects.requireNonNull(getView()).findViewById(R.id.time_delay).setOnClickListener(v -> {
            if (mType == FragmentType.MAIN) {
                refreshCurrentVersion();
                Objects.requireNonNull(getView()).findViewById(R.id.switch_detect).setAlpha(0.5f);
                Objects.requireNonNull(getView()).findViewById(R.id.up_to_date).setVisibility(View.GONE);
            } else {
                refreshPanelsDetected();
            }
            progressBar.setVisibility(View.VISIBLE);
            Objects.requireNonNull(getView()).findViewById(R.id.tvCallDialog).setVisibility(View.GONE);
        });
    }

    private void refreshCurrentVersion() {
        NetworkHelper helper = new NetworkHelper();
        Request request = new Request.Builder()
                .url("http://" + Session.getInstance().getLocalIP(getContext()) + "/ota/core/version")
                .build();

        helper.setCallback(new NetworkCallback() {
            @Override
            public void onSuccess(Response response) {

                try {
                    String s = Objects.requireNonNull(response.body()).string();
                    JSONObject jRes = new JSONObject(s);
                    runOnUiThread(() -> {
                        try {
                            ((TextView) Objects.requireNonNull(getView()).findViewById(R.id.tvCallDialog)).setText(jRes.getString("version"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });

                    NetworkHelper helper1 = new NetworkHelper();
                    Request request1 = new Request.Builder().url("http://tonylabs-oss.oss-cn-shanghai.aliyuncs.com/elementlight_esp32_master.bin").get().build();

                    helper1.setCallback(new NetworkCallback() {
                        @Override
                        public void onSuccess(Response response) {
                            String cloudVersion = response.header("x-oss-meta-firmware-version");
                            Log.i("Cloud Version", cloudVersion);
                            try {
                                if (!jRes.getString("version").equals(cloudVersion)) {
                                    runOnUiThread(() -> {
                                        progressBar.setVisibility(View.GONE);
                                        Objects.requireNonNull(getView()).findViewById(R.id.tvCallDialog).setVisibility(View.VISIBLE);
                                    });
                                } else {
                                    runOnUiThread(() -> {
                                        Objects.requireNonNull(getView()).findViewById(R.id.up_to_date).setVisibility(View.VISIBLE);
                                        Objects.requireNonNull(getView()).findViewById(R.id.tvCallDialog).setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(String msg) {
                            runOnUiThread(() -> {
                                Objects.requireNonNull(getView()).findViewById(R.id.tvCallDialog).setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            });
                        }
                    });

                    helper1.connect(request1);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String msg) {
                runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Can't get any response", Toast.LENGTH_SHORT).show();
//                    Objects.requireNonNull(getView()).findViewById(R.id.up_to_date).setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                });
            }
        });

        helper.connect(request);
    }

    private void refreshPanelsDetected() {
        if (!Session.getInstance().getLocalIP(getContext()).equals("")) {//try to load light num info
            NetworkHelper helper = new NetworkHelper();
            Request request = new Request.Builder().url("http://" + Session.getInstance().getLocalIP(getContext()) + "/nodes?_t=" + System.currentTimeMillis())
                    .get().build();
            helper.setCallback(new NetworkCallback() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onSuccess(Response response) {
                    try {
                        String nodesStr = Objects.requireNonNull(response.body()).string().trim();
                        JSONObject jsonResp = new JSONObject(nodesStr);
                        JSONArray nodeArray = jsonResp.getJSONArray("nodes");
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            Objects.requireNonNull(getView()).findViewById(R.id.tvCallDialog).setVisibility(View.VISIBLE);
                            ((TextView) Objects.requireNonNull(getView()).findViewById(R.id.tvCallDialog)).setText(nodeArray.length() + "");
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailed(String msg) {
                    Log.e("nodes get failed", msg);
                }
            });
            helper.connect(request);
        }
    }

    private void downLoadFirmware() {
        Log.w("DownloadFirmware", "");
        if (mType == FragmentType.MAIN) {
            NetworkHelper helper = new NetworkHelper();
            helper.connect(new Request.Builder()
                    .url("http://" + Session.getInstance().getLocalIP(getContext()) + "/ota/core/update?secret=000000").build());
        } else {
            NetworkHelper helper = new NetworkHelper();
            helper.connect(new Request.Builder()
                    .url("http://" + Session.getInstance().getLocalIP(getContext()) + "/ota/slave/update?secret=000000").build());
        }
    }

    private void runOnUiThread(Runnable runnable) {
        Activity activity = getActivity();
        if (activity != null)
            activity.runOnUiThread(runnable);
    }

}
