package xyz.photonlab.photonlabandroid;

import android.annotation.SuppressLint;
import android.app.Service;
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

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Request;
import okhttp3.Response;
import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.utils.NetworkCallback;
import xyz.photonlab.photonlabandroid.utils.NetworkHelper;
import xyz.photonlab.photonlabandroid.views.Light;
import xyz.photonlab.photonlabandroid.views.LightStage;


@SuppressLint("SetTextI18n")
public class fragment_layout extends Fragment {

    private FragmentActivity activity;

    private OnSavedLayoutListener listener;

    private Animation in, out;

    private Session session;
    private LightStage lightStage;
    @SuppressWarnings("local")
    private LinearLayout mainStage;
    private Button exit, add, rotate, delete, next, done;
    private ConstraintLayout step0BtnsParent;
    private ImageButton step1Next;
    private TextView tip, tv_node_num;

    private int currentNum = 0;
    private Light checkedLight;

    private ArrayList<Long> lightNums;

    public void setListener(OnSavedLayoutListener listener) {
        this.listener = listener;
    }

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

        if (!session.getLocalIP(getContext()).equals("")) {//try to load light num info
            lightNums = new ArrayList<>();
            NetworkHelper helper = new NetworkHelper();
            Request request = new Request.Builder().url("http://" + session.getLocalIP(getContext()) + "/nodes?_t=" + System.currentTimeMillis())
                    .get().build();
            helper.setCallback(new NetworkCallback() {
                @Override
                public void onSuccess(Response response) {
                    try {
                        String nodesStr = Objects.requireNonNull(response.body()).string().trim();
                        Log.i("nodes", nodesStr);
                        nodesStr = nodesStr.replaceAll("[\\[\\]]", "");
                        Log.i("nodes2", nodesStr);
                        String[] nodeArray = nodesStr.split(",");
                        for (String s : nodeArray) {
                            Log.i("node3", s);
                            lightNums.add(Long.parseLong(s));
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
        exit.setOnClickListener(v -> {
            if (activity != null)
                activity.getSupportFragmentManager().popBackStack();
        });

        next.setOnClickListener(v -> {
            if (lightStage.getLights().size() > 0 && lightStage.getUselessLightNum() == 0) {
                session.saveLayoutToLocal(getContext(), lightStage);
                if (listener != null)
                    listener.onSavedLayout(true);
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
            if (checkedLight != null && !checkedLight.isLitUp() && lightNums.size() > 0) {
                checkedLight.setNum(lightNums.get(currentNum));
                checkedLight.litUp();
                vibrator.vibrate(50);
                if (lightStage.allLitUp()) {
                    Toast.makeText(getContext(), "You are all set!", Toast.LENGTH_SHORT).show();
                    session.saveLayoutToLocal(getContext(), lightStage);
                    done.setVisibility(View.VISIBLE);
                } else {
                    step1Next.setEnabled(false);
                    currentNum++;
                    nextLight(currentNum);
                }
            }
        });

        done.setOnClickListener(v -> {
            boolean flag = session.saveLayoutToLocal(getContext(), lightStage);
            if (flag) {
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
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
        mainStage = contentView.findViewById(R.id.layoutContainer);
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
    }


    private void nextLight(int index) {

        NetworkHelper helper = new NetworkHelper();
        Request request = new Request.Builder()
                .url("http://" + Session.getInstance().getLocalIP(getContext()) + "/mode/single?node="
                        + lightNums.get(index) + "&red=100&green=100&blue=100&brightness=255")
                .get().build();
        helper.setCallback(new NetworkCallback() {
            @Override
            public void onSuccess(Response response) {
                try {
                    Log.i("nextLightLitUp", Objects.requireNonNull(response.body()).string());
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> step1Next.setEnabled(true));

                } catch (Exception e) {
                    e.printStackTrace();
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> step1Next.setEnabled(true));
                }
            }

            @Override
            public void onFailed(String msg) {
                Log.e("nextLightLitUp failed", msg);
                Objects.requireNonNull(getActivity()).runOnUiThread(() -> step1Next.setEnabled(true));
            }
        });
        helper.connect(request);
    }

    interface OnSavedLayoutListener {
        void onSavedLayout(boolean saved);
    }

}
