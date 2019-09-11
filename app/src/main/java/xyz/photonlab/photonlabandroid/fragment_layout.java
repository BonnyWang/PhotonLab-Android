package xyz.photonlab.photonlabandroid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Request;
import okhttp3.Response;
import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;
import xyz.photonlab.photonlabandroid.utils.NetworkCallback;
import xyz.photonlab.photonlabandroid.utils.NetworkHelper;
import xyz.photonlab.photonlabandroid.views.Light;
import xyz.photonlab.photonlabandroid.views.LightStage;


@SuppressLint("SetTextI18n")
public class fragment_layout extends NormalStatusBarFragment {

    private FragmentActivity activity;

    private Animation in, out;

    private Session session;
    private LightStage lightStage;

    private ImageButton exit, add, rotate, delete;
    private Button next, done;
    private ConstraintLayout step0BtnsParent;
    private ImageButton step1Next;
    private TextView tip, tv_node_num;

    private int currentNum = 0;
    private Light checkedLight;

    private ArrayList<Long> lightNums;
    private ImageButton help;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        session = Session.getInstance();
        in = AnimationUtils.loadAnimation(getContext(), R.anim.pop_enter);
        out = AnimationUtils.loadAnimation(getContext(), R.anim.pop_out);
        initView(view);
        addViewEvent();
        lightNums = new ArrayList<>();
        if (!session.getLocalIP(getContext()).equals("")) {//try to load light num info
            NetworkHelper helper = new NetworkHelper();
            Request request = new Request.Builder().url("http://" + session.getLocalIP(getContext()) + "/nodes?_t=" + System.currentTimeMillis())
                    .get().build();
            helper.setCallback(new NetworkCallback() {
                @Override
                public void onSuccess(Response response) {
                    try {
                        String nodesStr = Objects.requireNonNull(response.body()).string().trim();

                        JSONObject jsonResp = new JSONObject(nodesStr);
                        JSONArray nodeArray = jsonResp.getJSONArray("nodes");
                        for (int i = 0; i < nodeArray.length(); i++) {
                            lightNums.add(nodeArray.getLong(i));
                        }

                        Objects.requireNonNull(getActivity()).runOnUiThread(() -> tv_node_num.setText("Panels Detected: " + lightNums.size()));

                        Log.i("node in list", lightNums.toString());
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

    private void addViewEvent() {

        help.setOnClickListener(v -> {
            Activity activity = getActivity();
            if (activity != null) {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://photonlab.xyz/help.html")));
            }
        });

        exit.setOnClickListener(v -> {
            if (activity != null)
                activity.getSupportFragmentManager().popBackStack();
        });

        next.setOnClickListener(v -> {
            if (lightStage.getLights().size() > 0 && lightStage.getUselessLightNum() == 0) {
                session.saveLayoutToLocal(getContext(), lightStage);
                session.notifyLayoutChanged();
                if (lightNums.size() != lightStage.getLights().size()) {//check the num between ui and physic
                    Toast.makeText(getContext(), "Incorrect Node Num!", Toast.LENGTH_SHORT).show();
                    return;
                }

//                NetworkHelper helper = new NetworkHelper();
//                Request request = new Request.Builder()
//                        .url("http://" + Session.getInstance().getLocalIP(getContext()) + "/"
//                                + "mode/all?red=0&green=0&blue=0&brightness=0")
//                        .build();
//                helper.connect(request);

                step0BtnsParent.setVisibility(View.GONE);
                step1Next.setVisibility(View.VISIBLE);
                step0BtnsParent.startAnimation(out);
                step1Next.startAnimation(in);
                tip.setText(R.string.select_the_lit_light);
                lightStage.denyMove();
                lightStage.enterSetupMode();
                //after layout Request you will receive a num
                currentNum = 0;
                nextLight(0);
            } else {
                Toast.makeText(getContext(), "Layout Error", Toast.LENGTH_SHORT).show();
            }
        });

        add.setOnClickListener(v -> lightStage.addLight());

        delete.setOnClickListener(v -> lightStage.deleteLight());

        rotate.setOnClickListener(v -> lightStage.rotateLight());

        lightStage.setOnLightCheckedChangeListener(light -> this.checkedLight = light);
        Vibrator vibrator = (Vibrator) (Objects.requireNonNull(getActivity()).getSystemService(Service.VIBRATOR_SERVICE));
        step1Next.setOnClickListener(v -> {
            vibrator.vibrate(50);
            if (checkedLight != null && !checkedLight.isLitted() && lightNums.size() > 0) {
                checkedLight.setNum(lightNums.get(currentNum));
                checkedLight.litUp();
                if (lightStage.allLitUp()) {
                    Toast.makeText(getContext(), "You are all set!", Toast.LENGTH_SHORT).show();
                    session.saveLayoutToLocal(getContext(), lightStage);
                    session.notifyLayoutChanged();
                    done.setVisibility(View.VISIBLE);
                    help.setVisibility(View.GONE);
                } else {
                    currentNum++;
                    nextLight(currentNum);
                }
            }
        });

        done.setOnClickListener(v -> {
            boolean flag = session.saveLayoutToLocal(getContext(), lightStage);
            if (flag) {
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                session.notifyLayoutChanged();
            } else {
                Toast.makeText(getContext(), "Save Error", Toast.LENGTH_SHORT).show();
            }
            activity.getSupportFragmentManager().popBackStack();
        });
    }

    private void initView(@NonNull View contentView) {
        lightStage = session.requireLayoutStage(getContext(), false);
        if (lightStage == null) {//no layout file
            lightStage = new LightStage(getContext());
        }
        lightStage.requireCenter();
        LinearLayout mainStage = contentView.findViewById(R.id.layoutContainer);
        mainStage.addView(lightStage);
        step0BtnsParent = contentView.findViewById(R.id.step0_btns);
        exit = contentView.findViewById(R.id.btBackLayout);
        tip = contentView.findViewById(R.id.tvLayout);
        next = contentView.findViewById(R.id.btNext);
        add = contentView.findViewById(R.id.btAddHex);
        rotate = contentView.findViewById(R.id.btRotate);
        delete = contentView.findViewById(R.id.btDelete);
        step1Next = contentView.findViewById(R.id.step1_next);
        done = contentView.findViewById(R.id.done);
        tv_node_num = contentView.findViewById(R.id.tv_node_num);
        help = contentView.findViewById(R.id.button3);
        if (Session.getInstance().isDarkMode(getContext())) {
            contentView.setBackgroundColor(Theme.Dark.MAIN_BACKGROUND);
            ((TextView) contentView.findViewById(R.id.tvLayout)).setTextColor(Theme.Dark.SELECTED_TEXT);
            tv_node_num.setTextColor(Theme.Dark.UNSELECTED_TEXT);
            step0BtnsParent.setBackgroundColor(Theme.Dark.MAIN_BACKGROUND);
            exit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EA7D38")));
            done.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EA7D38")));
            help.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EA7D38")));
        }
    }


    private void nextLight(int index) {
        if (index > lightNums.size() - 1 || index < 0)//avoid illegal parameter
            return;

        NetworkHelper helper = new NetworkHelper();
        Request request = new Request.Builder()
                .url("http://" + Session.getInstance().getLocalIP(getContext()) + "/mode/single?node="
                        + lightNums.get(index) + "&red=100&green=100&blue=100")
                .get().build();
        helper.connect(request);
        Log.i("HHH", "sss");
    }

    public interface OnSavedLayoutListener {
        void onSavedLayout(boolean saved);
    }

    private void runOnUiThread(Runnable runnable) {
        Activity activity = getActivity();
        if (activity != null)
            activity.runOnUiThread(runnable);
    }

}
