package xyz.photonlab.photonlabandroid;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
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
import androidx.fragment.app.FragmentActivity;

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

public class FirmwareUpdateIndividualFragment extends Fragment {

    private FragmentActivity mActivity;
    private View mView;

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
        this.mView = view;
        initView(view);
        addViewEvent();
        if (mType == FragmentType.MAIN) {
            refreshCurrentVersion();
        } else {
            refreshPanelsDetected();
        }
    }


    @SuppressLint("SetTextI18n")
    private void initView(@NonNull View contentView) {
        this.btn_exit = contentView.findViewById(R.id.backButton);
        progressBar = contentView.findViewById(R.id.load);
        if (Session.getInstance().isDarkMode(mActivity)) {
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
        final AlertDialog dialog = new AlertDialog.Builder(mActivity)
                .setTitle("Notice")
                .setMessage("Are you sure to update the firmware? This process might take a while.")
                .setIcon(R.drawable.lightbulb)
                .setCancelable(false)
                .setPositiveButton("Confirm", (dialog1, which) -> downLoadFirmware())
                .setNegativeButton("Cancel", (dialog1, which) ->
                        dialog1.dismiss())
                .create();
        btn_exit.setOnClickListener(v -> mActivity.getSupportFragmentManager().popBackStack());
        mView.findViewById(R.id.switch_detect).setOnClickListener(v -> {
            if (v.getAlpha() != 1f)
                return;
            dialog.show();
        });
        mView.findViewById(R.id.time_delay).setOnClickListener(v -> {
            mView.findViewById(R.id.switch_detect).setAlpha(0.5f);
            mView.findViewById(R.id.up_to_date).setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            mView.findViewById(R.id.tvCallDialog).setVisibility(View.GONE);
            if (mType == FragmentType.MAIN) {
                refreshCurrentVersion();
            } else {
                refreshPanelsDetected();
            }
        });
    }

    private void refreshCurrentVersion() {
        NetworkHelper helper = new NetworkHelper();
        Request request = new Request.Builder()
                .url("http://" + Session.getInstance().getLocalIP(mActivity) + "/ota/core/version")
                .build();
        mView.findViewById(R.id.up_to_date).setVisibility(View.GONE);
        helper.setCallback(new NetworkCallback() {
            @Override
            public void onSuccess(Response response) {

                try {
                    String s = Objects.requireNonNull(response.body()).string();
                    JSONObject jRes = new JSONObject(s);
                    mActivity.runOnUiThread(() -> {
                        try {
                            ((TextView) mView.findViewById(R.id.tvCallDialog)).setText(jRes.getString("version"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });

                    NetworkHelper helper1 = new NetworkHelper();
                    Request request1 = new Request.Builder().url("https://elementlight-us-west.oss-us-west-1.aliyuncs.com/elementlight_esp32_master.bin").get().build();

                    helper1.setCallback(new NetworkCallback() {
                        @Override
                        public void onSuccess(Response response) {
                            String cloudVersion = response.header("x-oss-meta-firmware-version");
                            Log.i("Cloud Version", cloudVersion);
                            try {
                                if (!jRes.getString("version").equals(cloudVersion)) {
                                    mActivity.runOnUiThread(() -> {
                                        progressBar.setVisibility(View.GONE);
                                        mView.findViewById(R.id.tvCallDialog).setVisibility(View.VISIBLE);
                                        mView.findViewById(R.id.up_to_date).setVisibility(View.VISIBLE);
                                    });
                                } else {
                                    mActivity.runOnUiThread(() -> {
                                        mView.findViewById(R.id.up_to_date).setVisibility(View.VISIBLE);
                                        mView.findViewById(R.id.tvCallDialog).setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onFailed(String msg) {
                            mActivity.runOnUiThread(() -> {
                                mView.findViewById(R.id.tvCallDialog).setVisibility(View.VISIBLE);
                                mView.findViewById(R.id.tvCallDialog).setVisibility(View.VISIBLE);
                                mView.findViewById(R.id.tvCallDialog).setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            });
                        }
                    });

                    helper1.connect(request1);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailed(String msg) {
                mActivity.runOnUiThread(() -> {
                    Toast.makeText(mActivity, "Can't get any response", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    mView.findViewById(R.id.tvCallDialog).setVisibility(View.VISIBLE);
                });
            }
        });

        helper.connect(request);
    }

    private void refreshPanelsDetected() {
        if (!Session.getInstance().getLocalIP(mActivity).equals("")) {//try to load light num info
            NetworkHelper helper = new NetworkHelper();
            Request request = new Request.Builder().url("http://" + Session.getInstance().getLocalIP(mActivity) + "/nodes?_t=" + System.currentTimeMillis())
                    .get().build();
            helper.setCallback(new NetworkCallback() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onSuccess(Response response) {
                    Log.i("FirmwareIndividual", "onSuccess: ");
                    try {
                        String nodesStr = Objects.requireNonNull(response.body()).string().trim();
                        JSONObject jsonResp = new JSONObject(nodesStr);
                        JSONArray nodeArray = jsonResp.getJSONArray("nodes");
                        mActivity.runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            mView.findViewById(R.id.tvCallDialog).setVisibility(View.VISIBLE);
                            ((TextView) mView.findViewById(R.id.tvCallDialog)).setText(nodeArray.length() + "");
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        mActivity.runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            mView.findViewById(R.id.tvCallDialog).setVisibility(View.VISIBLE);
                            ((TextView) mView.findViewById(R.id.tvCallDialog)).setText("0");
                        });
                    }
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onFailed(String msg) {
                    Log.e("nodes get failed", msg);
                    mActivity.runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        mView.findViewById(R.id.tvCallDialog).setVisibility(View.VISIBLE);
                        ((TextView) mView.findViewById(R.id.tvCallDialog)).setText("0");
                    });
                }
            });
            helper.connect(request);
        } else {
            Toast.makeText(mActivity, "No Devices", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
            mView.findViewById(R.id.tvCallDialog).setVisibility(View.VISIBLE);
            ((TextView) mView.findViewById(R.id.tvCallDialog)).setText("0");
        }
    }

    private void downLoadFirmware() {
        Log.w("DownloadFirmware", "");
        if (mType == FragmentType.MAIN) {
            NetworkHelper helper = new NetworkHelper();
            helper.connect(new Request.Builder()
                    .url("http://" + Session.getInstance().getLocalIP(mActivity) + "/ota/core/update?secret=000000").build());
        } else {
            NetworkHelper helper = new NetworkHelper();
            helper.connect(new Request.Builder()
                    .url("http://" + Session.getInstance().getLocalIP(mActivity) + "/ota/slave/update?secret=000000").build());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = getActivity();
    }
}
