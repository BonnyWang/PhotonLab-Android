package xyz.photonlab.photonlabandroid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONObject;

import okhttp3.Request;
import okhttp3.Response;
import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.utils.NetworkCallback;
import xyz.photonlab.photonlabandroid.utils.NetworkHelper;

public class FragmentPair extends Fragment {

    private Animation in, out;
    private Button exit;
    private ProgressBar progressBar;
    private String wifi_ssid, wifi_password, current_gateway_ip;
    private ConstraintLayout mask;


    //Step 1
    private ConstraintLayout step1_container;
    private TextView tv_SSID, tv_help;
    private EditText et_wifi_password;
    private Button next;

    //Step 2
    private ConstraintLayout step2_container;
    private Button goToSettings, yes;

    //stepSuccess
    private ConstraintLayout success_container;
    private Button doneButton, goLayoutButton, goMainButton;

    //stepFailed
    private ConstraintLayout faile_container;
    private Button try_again_btn;
    private TextView tvErrorHelp;
    private AppCompatActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pair_layout, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        in = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right);
        out = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_left);
        findView(view);
        addViewEvent();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void addViewEvent() {
        exit.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStack();
            cleanEditTextProblems();
        });

        tv_help.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://photonlab.xyz/help.html"));
            startActivity(i);
        });

        next.setOnClickListener(v -> {
            wifi_password = et_wifi_password.getText().toString();
            if (wifi_ssid != null) {
                cleanEditTextProblems();
                step1_container.startAnimation(out);
                step1_container.setVisibility(View.GONE);
                step2_container.startAnimation(in);
                step2_container.setVisibility(View.VISIBLE);
                progressBar.setProgress(75);
            } else {
                Toast.makeText(getContext(), "You are not connected to Wi-Fi", Toast.LENGTH_SHORT).show();
            }
        });

        //step2
        goToSettings.setOnClickListener(v -> {
            if (getContext() != null)
                getContext().startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        });

        yes.setOnClickListener(v -> {
            String currentSsid = getWifiInfo();
            if (currentSsid != null && currentSsid.startsWith("ElementLight-")) {
                tellLightTheWifiInfo();
                mask.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getContext(), "Not connected to the light", Toast.LENGTH_SHORT).show();
            }
        });

        //step3
        doneButton.setOnClickListener((v) -> exit.performClick());
        try_again_btn.setOnClickListener((v) -> {
            progressBar.setVisibility(View.VISIBLE);
            activity.getSupportFragmentManager().popBackStackImmediate();

            //Reload to new fragment -bbb
            FragmentPair fragment_pair = new FragmentPair();
            Log.i("pairFragment", activity + "");
            FragmentTransaction ft0 = activity.getSupportFragmentManager().beginTransaction();
            ft0.replace(R.id.container, fragment_pair).addToBackStack(null);
            ft0.commit();
        });

        goMainButton.setOnClickListener(v -> {
            Activity activity = getActivity();
            if (activity instanceof MainActivity) {
                ((MainActivity) activity).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ((MainActivity) activity).goMain();
            }
        });

        goLayoutButton.setOnClickListener(v -> {
            if (getActivity() == null)
                return;
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction tx = fragmentManager.beginTransaction();
            fragmentManager.popBackStack();
            tx.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            tx.replace(R.id.container, new fragment_layout()).addToBackStack(null);
            tx.commit();
        });

        //step failed
        tvErrorHelp.setOnClickListener((v) -> tv_help.performClick());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AppCompatActivity)
            this.activity = (AppCompatActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("start", "FragmentPair");
        checkLocation();
        String cSsid;
        if ((cSsid = getWifiInfo()) != null) {
            Log.i("cSsid", cSsid);
            if (step1_container.getVisibility() != View.GONE) {
                wifi_ssid = cSsid;
                tv_SSID.setText(wifi_ssid);
                Log.i("wifi_ssid_changed:", wifi_ssid);
            }
        } else {
            tv_SSID.setText("Not Connected!");
        }
    }

    private void checkLocation() {
        assert getActivity() != null;
        LocationManager lm = (LocationManager) getActivity().getSystemService(Activity.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setTitle("Please Open Your Location Service")
                    .setIcon(R.drawable.lightbulb)
                    .setCancelable(false)
                    .setPositiveButton(R.string.go_to_location, (dialog1, which) -> {
                        getActivity().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }).create();
            dialog.show();
        }
    }

    private void tellLightTheWifiInfo() {
        NetworkHelper helper = new NetworkHelper();
        Request request = new Request.Builder().url(" http://" + current_gateway_ip
                + "/join?ssid=" + wifi_ssid + "&password=" + wifi_password)
                .get()
                .build();
        Log.i("tellLightWifiInfo", " http://" + current_gateway_ip
                + "/join?ssid=" + wifi_ssid + "&password=" + wifi_password);
        helper.connect(request);
        helper.setCallback(new NetworkCallback() {
            @Override
            public void onSuccess(Response response) {
                try {
                    String respStr = response.body().string();
                    Log.i("response", respStr);
                    JSONObject respObj = new JSONObject(respStr);
                    if (!respObj.getBoolean("status"))
                        throw new RuntimeException();
                    String localIp = respObj.getString("ip");
                    Session.getInstance().setLocalIP(localIp);
                    new TinyDB(getContext()).putString("LocalIp", localIp);
                    Log.i("LocalIp", localIp);
                    runOnUIThread(() -> toSuccess());
                } catch (Exception e) {
                    e.printStackTrace();
                    //to failure page
                    runOnUIThread(() -> toFail());
                }
                runOnUIThread(() -> mask.setVisibility(View.GONE));
            }

            @Override
            public void onFailed(String msg) {
                Log.e("request failed", msg);
                runOnUIThread(() -> toFail());
            }
        });
    }

    private void toFail() {
        mask.setVisibility(View.GONE);
        faile_container.setVisibility(View.VISIBLE);
        step2_container.setVisibility(View.GONE);
        faile_container.startAnimation(in);
        step2_container.startAnimation(out);
        progressBar.setProgress(33);
        progressBar.setVisibility(View.GONE);
    }

    private void toSuccess() {
        success_container.setVisibility(View.VISIBLE);
        step2_container.setVisibility(View.GONE);
        success_container.startAnimation(in);
        step2_container.startAnimation(out);
        doneButton.setVisibility(View.VISIBLE);
        progressBar.setProgress(100);
    }

    private void runOnUIThread(Runnable runnable) {
        Activity activity = getActivity();
        if (activity != null)
            activity.runOnUiThread(runnable);
    }

    private String getWifiInfo() {
        WifiManager wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Log.i("CurrentWifiState", wifiManager.getWifiState() + "");
        if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            int ipInt = wifiManager.getDhcpInfo().gateway;
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                // 每 8 位为一段，这里取当前要处理的最高位的位置
                int pos = i * 8;
                // 取当前处理的 ip 段的值
                int and = ipInt & (255 << pos);
                // 将当前 ip 段转换为 0 ~ 255 的数字，注意这里必须使用无符号右移
                stringBuilder.append(and >>> pos);
                if (i != 3)
                    stringBuilder.append(".");
            }
            this.current_gateway_ip = stringBuilder.toString();
            Log.i("gateway_ip", current_gateway_ip);
            String ssid = wifiManager.getConnectionInfo().getSSID();
            Log.i("SSID", ssid);
            if (ssid.replaceAll("\"", "").toLowerCase().equals("<unknown ssid>"))
                return null;
            return ssid.replaceAll("\"", "");
        }
        return null;
    }

    private void findView(@NonNull View contentView) {
        mask = contentView.findViewById(R.id.pair_mask);
        progressBar = contentView.findViewById(R.id.progressBar);

        //step1
        step1_container = contentView.findViewById(R.id.pair_step_1);
        et_wifi_password = contentView.findViewById(R.id.wifi_pwd);
        tv_help = contentView.findViewById(R.id.tv_help);
        tv_SSID = contentView.findViewById(R.id.ssid);
        next = contentView.findViewById(R.id.step1_next);
        exit = contentView.findViewById(R.id.backButton_pair);

        //step2
        step2_container = contentView.findViewById(R.id.pair_step_2);
        goToSettings = contentView.findViewById(R.id.to_system_wifi);
        yes = contentView.findViewById(R.id.yes_connected);

        //stepSuccess
        success_container = contentView.findViewById(R.id.success);
        doneButton = contentView.findViewById(R.id.done);
        goLayoutButton = contentView.findViewById(R.id.go_to_layout);
        goMainButton = contentView.findViewById(R.id.to_main);

        //stepFailed
        tvErrorHelp = contentView.findViewById(R.id.tvErrorHelp);
        faile_container = contentView.findViewById(R.id.stepFailed);
        try_again_btn = contentView.findViewById(R.id.btTryAgain);

    }

    @Override
    public void onStop() {
        Log.i("FragmentPair", "stop");
        cleanEditTextProblems();
        super.onStop();
    }

    private void cleanEditTextProblems() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        }
        et_wifi_password.clearFocus();
    }
}
