package xyz.photonlab.photonlabandroid;

import android.os.Bundle;
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

import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.views.Light;
import xyz.photonlab.photonlabandroid.views.LightStage;


public class fragment_layout extends Fragment {

    private FragmentActivity activity;

    private OnSavedLayoutListener listener;

    private Animation in, out;

    private Session session;
    private LightStage lightStage;
    private LinearLayout mainStage;
    private Button exit, add, rotate, delete, next, done;
    private ConstraintLayout step0BtnsParent;
    private ImageButton step1Next;
    private TextView tip;

    private int currentNum;
    private Light checkedLight;

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
    }

    private void addViewEvent() {
        exit.setOnClickListener(v -> {
            if (activity != null)
                activity.getSupportFragmentManager().popBackStack();
        });

        next.setOnClickListener(v -> {
            if (lightStage.getLights().size() > 0 && lightStage.getUselessLightNum() == 0) {
                //todo send layout request
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
            } else {
                Toast.makeText(getContext(), "Layout Error", Toast.LENGTH_SHORT).show();
            }
        });

        add.setOnClickListener(v -> lightStage.addLight());

        delete.setOnClickListener(v -> lightStage.deleteLight());

        rotate.setOnClickListener(v -> lightStage.rotateLight());

        lightStage.setOnLightCheckedChangeListener(light -> {
            this.checkedLight = light;
        });

        step1Next.setOnClickListener(v -> {
            if (checkedLight != null && !checkedLight.isLitUp()) {
                checkedLight.setNum(currentNum);
                checkedLight.litUp();
                if (lightStage.allLitUp()) {
                    Toast.makeText(getContext(), "Address pair completed!", Toast.LENGTH_SHORT).show();
                    session.saveLayoutToLocal(getContext(), lightStage);
                    done.setVisibility(View.VISIBLE);
                } else {
                    //todo send layout request,will receive a num
                    currentNum++;
                }
            }
        });

        done.setOnClickListener(v -> {
            boolean flag = session.saveLayoutToLocal(getContext(), lightStage);
            if (flag) {
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Save Error!", Toast.LENGTH_SHORT).show();
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
    }


    interface OnSavedLayoutListener {
        void onSavedLayout(boolean saved);
    }

}
