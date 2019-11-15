package xyz.photonlab.photonlabandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import org.json.JSONObject;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;
import xyz.photonlab.photonlabandroid.utils.NetworkCallback;
import xyz.photonlab.photonlabandroid.utils.NetworkHelper;

/**
 * created by KIO on 2019/11/13
 */
public class PairFragmentV2 extends Fragment {

    private FragmentActivity mActivity;

    //views
    ImageButton exit_btn;
    EditText password_et;
    CardView continue_ct;
    ConstraintLayout step1, step2, step3;
    TextView point1, point2, point3, pair_tv, success_tv, fail_tv, ssid_tv;
    Animation scaleInfinity;
    ImageView pairing_iv;
    ImageButton more;
    CardView gotoWifi;

    //custom variables
    int currentStep = 0;
    String wifiSsid = "";
    String password = "";
    final boolean[] secondTime = {false};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pair_layout_v2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        addViewEvent();
        initialize();
    }

    private void gotoSystemWifi() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_WIFI_SETTINGS);
        mActivity.startActivity(intent);
    }

    private void initialize() {
        scaleInfinity = AnimationUtils.loadAnimation(mActivity, R.anim.scale_infinity);
        if (Session.getInstance().isDarkMode(mActivity)) {
            View root;
            if ((root = getView()) != null)
                root.setBackgroundColor(Theme.Dark.CARD_BACKGROUND);
            exit_btn.setImageTintList(ColorStateList.valueOf(Theme.Dark.SELECTED_TEXT));
            ((TextView) step1.findViewById(R.id.textView9)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((CardView) step1.findViewById(R.id.cardView)).setCardBackgroundColor(Theme.Dark.UNSELECTED_TEXT);
            ((CardView) step1.findViewById(R.id.card_password)).setCardBackgroundColor(0xff404040);
            ssid_tv.setTextColor(Theme.Dark.SELECTED_TEXT);
            password_et.setTextColor(Theme.Dark.SELECTED_TEXT);
            password_et.setHintTextColor(Theme.Dark.UNSELECTED_TEXT);
            more.setImageTintList(ColorStateList.valueOf(Theme.Dark.SELECTED_TEXT));

            ((TextView) step2.findViewById(R.id.tv_title_step2)).setTextColor(Theme.Dark.SELECTED_TEXT);
            gotoWifi.setCardBackgroundColor(Theme.Dark.UNSELECTED_TEXT);
            ((ImageButton) step2.findViewById(R.id.ib_wifi)).setImageTintList(ColorStateList.valueOf(Theme.Dark.SELECTED_TEXT));
            ((TextView) step2.findViewById(R.id.goToWifiTv)).setTextColor(Theme.Dark.SELECTED_TEXT);
            pair_tv.setTextColor(Theme.Dark.SELECTED_TEXT);
            success_tv.setTextColor(Theme.Dark.SELECTED_TEXT);
            fail_tv.setTextColor(Theme.Dark.SELECTED_TEXT);
        }
    }

    private void addViewEvent() {
        exit_btn.setOnClickListener(v -> mActivity.getSupportFragmentManager().popBackStack());
        password_et.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                password_et.setHint("");
            } else {
                password_et.setHint("Password");
            }
        });
        continue_ct.setOnClickListener(v -> {
            if (currentStep == 0) {//change to step2
                this.wifiSsid = ssid_tv.getText().toString();
                this.password = password_et.getText().toString();
                if (wifiSsid == null ||
                        wifiSsid.equals("Not Connect") || wifiSsid.equals("") || password.equals("")) {
                    Toast.makeText(mActivity, "Unavailable Wifi Information!", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentStep = 1;
                this.point2.setTextColor(0xfffad1a9);
                this.step1.setVisibility(View.GONE);
                this.step2.setVisibility(View.VISIBLE);
                this.step1.setAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.slide_out_left));
                this.step2.setAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.slide_in_right));
            } else if (currentStep == 1) {//change to step3
                String wifiSSID = getWifiInfo();
                if (wifiSSID == null) {
                    Toast.makeText(mActivity, "You are not connect to ElementLight", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!wifiSSID.toLowerCase().startsWith("elementlight-")) {
                    Toast.makeText(mActivity, "You are not connect to ElementLight", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentStep = 2;
                this.point3.setTextColor(0xfffad1a9);
                this.step2.setVisibility(View.GONE);
                this.step3.setVisibility(View.VISIBLE);
                this.step2.startAnimation(AnimationUtils.loadAnimation(mActivity, android.R.anim.fade_out));
                this.step3.startAnimation(AnimationUtils.loadAnimation(mActivity, android.R.anim.fade_in));
                this.continue_ct.setVisibility(View.INVISIBLE);
                this.continue_ct.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.pop_out));
                this.pair_tv.setVisibility(View.VISIBLE);
                this.pairing_iv.startAnimation(scaleInfinity);
                tellLightWifiInfo();
            }
        });
        gotoWifi.setOnClickListener(v -> gotoSystemWifi());
        more.setOnClickListener(v -> gotoSystemWifi());
        success_tv.setOnClickListener(v -> mActivity.getSupportFragmentManager().popBackStack());
        fail_tv.setOnClickListener(v -> {//restart all view and state
            point2.setTextColor(ColorStateList.valueOf(0xffcccccc));
            point3.setTextColor(ColorStateList.valueOf(0xffcccccc));
            pairing_iv.setImageTintList(ColorStateList.valueOf(0xfffad1a9));
            step1.setVisibility(View.VISIBLE);
            step2.setVisibility(View.GONE);
            step3.setVisibility(View.GONE);
            fail_tv.setVisibility(View.GONE);
            continue_ct.setVisibility(View.VISIBLE);
            this.currentStep = 0;
            this.secondTime[0] = false;
        });
    }

    private String getGateWayIp() {
        WifiManager wifiManager = ((WifiManager) mActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE));
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
            Log.i("gateway_ip", stringBuilder.toString());
            return stringBuilder.toString();
        }
        return null;
    }

    private void tellLightWifiInfo() {
        String ip = getGateWayIp();
        if (ip == null) {
            Toast.makeText(mActivity, "Unable to get gateway information", Toast.LENGTH_SHORT).show();
            toFail();
        } else {
            NetworkHelper helper = new NetworkHelper();
            Request request = new Request.Builder().url(" http://" + ip
                    + "/join?ssid=" + wifiSsid + "&password=" + password)
                    .get()
                    .build();
            Log.i("tellLightWifiInfo", " http://" + ip
                    + "/join?ssid=" + wifiSsid + "&password=" + password);
            OkHttpClient client = new OkHttpClient.Builder().readTimeout(20, TimeUnit.SECONDS).build();
            helper.connect(request, client);
            helper.setCallback(new NetworkCallback() {
                @Override
                public void onSuccess(Response response) {
                    try {
                        String respStr = Objects.requireNonNull(response.body()).string();
                        Log.i("response", respStr);
                        JSONObject respObj = new JSONObject(respStr);
                        if (!respObj.getBoolean("status"))
                            throw new RuntimeException();
                        String localIp = respObj.getString("ip");
                        String mac = respObj.getString("mac");
                        Session.getInstance().setLocalIP(getContext(), localIp);
                        new TinyDB(getContext()).putString("LocalIp", localIp);
                        Log.i("LocalIp", localIp);
                        new TinyDB(getContext()).putString("lightMac", mac);
                        mActivity.runOnUiThread(() -> toSuccess());
                    } catch (Exception e) {
                        e.printStackTrace();
                        //to failure page
                        onFailed(e.getMessage());
                    }
                }

                @Override
                public void onFailed(String msg) {
                    if (secondTime[0]) {
                        Log.i("SecondTime", "onFailed: ");
                        secondTime[0] = false;
                        mActivity.runOnUiThread(() -> toFail());
                    } else {
                        //try again
                        Log.i("SecondTime", "retry start");
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                secondTime[0] = true;
                                tellLightWifiInfo();
                            }
                        }, 5000);
                    }
                }
            });
        }
    }

    private void toSuccess() {
        this.pairing_iv.clearAnimation();
        Animation fadeOut = AnimationUtils.loadAnimation(mActivity, android.R.anim.fade_out);
        this.pair_tv.startAnimation(fadeOut);
        this.pair_tv.setVisibility(View.GONE);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                success_tv.setVisibility(View.VISIBLE);
                success_tv.startAnimation(AnimationUtils.loadAnimation(mActivity, android.R.anim.fade_in));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void toFail() {
        this.pairing_iv.setImageTintList(ColorStateList.valueOf(0xffcccccc));
        this.pairing_iv.clearAnimation();
        Animation fadeOut = AnimationUtils.loadAnimation(mActivity, android.R.anim.fade_out);
        this.pair_tv.startAnimation(fadeOut);
        this.pair_tv.setVisibility(View.GONE);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fail_tv.setVisibility(View.VISIBLE);
                fail_tv.startAnimation(AnimationUtils.loadAnimation(mActivity, android.R.anim.fade_in));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void initView(@NonNull View view) {
        exit_btn = view.findViewById(R.id.backButton_pair);
        password_et = view.findViewById(R.id.editText);
        continue_ct = view.findViewById(R.id.ct);
        step1 = view.findViewById(R.id.step1);
        step2 = view.findViewById(R.id.step2);
        step3 = view.findViewById(R.id.step3);
        point1 = view.findViewById(R.id.textView4);
        point2 = view.findViewById(R.id.textView6);
        point3 = view.findViewById(R.id.textView8);
        pair_tv = view.findViewById(R.id.tv_pair);
        pairing_iv = view.findViewById(R.id.pairing);
        fail_tv = view.findViewById(R.id.tv_fail);
        success_tv = view.findViewById(R.id.tv_success);
        more = view.findViewById(R.id.more);
        gotoWifi = view.findViewById(R.id.cardView2);
        ssid_tv = view.findViewById(R.id.wifi_ssid);
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
                    .setPositiveButton(R.string.go_to_location, (dialog1, which) -> getActivity().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton("Cancel", (dialog1, which) ->
                            dialog1.dismiss())
                    .create();
            dialog.show();
        }
    }

    public void onStart() {
        super.onStart();
        String ssid = getWifiSsid();
        ssid_tv.setText(ssid);
        Log.i("start", "FragmentPairV2");

    }

    private String getWifiSsid() {
        checkLocation();
        String cSsid = getWifiInfo();
        if (cSsid == null)
            return "Not Connect";
        Log.i("cSsid", cSsid);
        return cSsid;
    }

    private String getWifiInfo() {
        WifiManager wifiManager = ((WifiManager) mActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE));
        Log.i("CurrentWifiState", wifiManager.getWifiState() + "");
        if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            String ssid = wifiManager.getConnectionInfo().getSSID();
            Log.i("SSID", ssid);
            if (ssid.replaceAll("\"", "").toLowerCase().equals("<unknown ssid>"))
                return null;
            return ssid.replaceAll("\"", "");
        }
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = getActivity();
    }
}
