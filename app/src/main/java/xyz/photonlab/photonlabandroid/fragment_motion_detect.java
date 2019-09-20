package xyz.photonlab.photonlabandroid;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;


public class fragment_motion_detect extends NormalStatusBarFragment implements dialog_colorpicker.colorPick_Listener {


    public static final int COLOR_TRIGGER = 0b01;
    public static final int THEME_TRIGGER = 0b10;

    TinyDB tinyDB;

    final Context context = getContext();
    ConstraintLayout timeContainer, colorContainer, themeContainer;
    LinearLayout mainContainer;
    TextView tvCallDial;
    ImageButton backButton;
    Switch swMotion;
    TextView currentThemeTip;
    CardView colorShower;

    Calendar initialdate = Calendar.getInstance();

    TimePickerView pvTime;
    int swCheck;
    ArrayList<Integer> timeDelay;

    static Date settedDate = null;

    private int currentMotionDetect = THEME_TRIGGER;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_motion_detect, container, false);
        tvCallDial = view.findViewById(R.id.tvCallDialog);
        backButton = view.findViewById(R.id.backButton_Motion);
        swMotion = view.findViewById(R.id.swMotion);
        mainContainer = view.findViewById(R.id.main_container);
        currentThemeTip = view.findViewById(R.id.tvTriggerName);
        timeContainer = view.findViewById(R.id.time_delay);
        colorContainer = view.findViewById(R.id.switch_color);
        colorShower = view.findViewById(R.id.color_shower);
        themeContainer = view.findViewById(R.id.theme);

        timeContainer.setOnClickListener(v -> pvTime.show());
        colorContainer.setOnClickListener(v -> {
            FragmentManager manager = getFragmentManager();
            if (manager != null) {
                dialog_colorpicker colorpicker = dialog_colorpicker.newInstance(0);
                colorpicker.setListener(this);
                colorpicker.dismissAddFav();
                manager.beginTransaction()
                        .setCustomAnimations(R.anim.pop_enter, 0, 0, R.anim.pop_out)
                        .add(R.id.container, colorpicker)
                        .addToBackStack(null)
                        .commit();
            }
        });

        final TinyDB tinyDB = new TinyDB(getContext());

        initialdate.set(0, 0, 0, 0, 0, 0);


        initialize();

        swMotion.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                swCheck = 1;
                tinyDB.putInt("swCheck", swCheck);

            } else {
                swCheck = 0;
                tinyDB.putInt("swCheck", swCheck);
            }
        });

        pvTime = new TimePickerBuilder(getContext(), (date, v) -> {
            tvCallDial.setText(getTime(date));

            timeDelay.clear();
            timeDelay.add(date.getHours());
            timeDelay.add(date.getMinutes());
            timeDelay.add(date.getSeconds());
            TinyDB tinyDB1 = new TinyDB(getContext());
            tinyDB1.putListInt("delayTime", timeDelay);

            settedDate = date;
            initialdate.setTime(settedDate);
            Log.i("pvTime", "onTimeSelect");

        }).setType(new boolean[]{false, false, false, true, true, true})
                .setLabel("Year", "Monty", "Day", "h", "min", "s")
                .setCancelText("Cancel")
                .setCancelColor(getResources().getColor(R.color.colorPrimary, null))
                .setSubmitText("Confirm")
                .setSubmitColor(getResources().getColor(R.color.colorPrimary, null))
                .isDialog(true)
                .setDate(initialdate)
                .build();

        backButton.setOnClickListener(v -> Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack());

        mainContainer.findViewById(R.id.theme).setOnClickListener(v -> {
            Intent i = new Intent(getContext(), SelectMotionThemeActivity.class);
            startActivityForResult(i, 0);
        });
        if (Session.getInstance().isDarkMode(getContext())) {
            view.setBackgroundColor(Theme.Dark.MAIN_BACKGROUND);
            ((TextView) view.findViewById(R.id.textView2)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) view.findViewById(R.id.tvTime)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) view.findViewById(R.id.tvThemeTrigger)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) view.findViewById(R.id.color_title)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) view.findViewById(R.id.textView2)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) view.findViewById(R.id.tvTriggerName)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) view.findViewById(R.id.tvCallDialog)).setTextColor(Theme.Dark.UNSELECTED_TEXT);
            ((TextView) view.findViewById(R.id.tvMotion)).setTextColor(Theme.Dark.SELECTED_TEXT);
            view.findViewById(R.id.divider).setBackgroundColor(Theme.Dark.UNSELECTED_TEXT);
            backButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EA7D38")));
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Select Theme Back", "");
        if (data == null)
            return;
        int themeIndex = data.getIntExtra("themeIndex", -1);
        if (themeIndex != -1) {
            themeContainer.setAlpha(1f);
            colorContainer.setAlpha(0.5f);
            tinyDB.putInt("currentMotionDetect", this.currentMotionDetect);
            tinyDB.putInt("motionDetectThemeIndex", themeIndex);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("viewCreated", "motion_detect");
        Session.getInstance().requestTheme(getContext());
        int currentThemeIndex = new TinyDB(getContext()).getInt("CurrentTheme");
        if (currentThemeIndex == -1)
            currentThemeIndex = 0;
        if (currentThemeTip != null)
            currentThemeTip.setText(Session.getInstance().getAllThemes().get(currentThemeIndex).getName());
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        return format.format(date);
    }

    private void initialize() {
        tinyDB = new TinyDB(this.getContext());
        timeDelay = new ArrayList<>();

        if (tinyDB.getInt("currentMotionDetect") == COLOR_TRIGGER) {
            this.colorContainer.setAlpha(1f);
            this.themeContainer.setAlpha(0.5f);
        }

        if (tinyDB.getInt("motionDetectColor") != -1) {
            colorShower.setCardBackgroundColor(tinyDB.getInt("motionDetectColor"));
        }

        if (tinyDB.getInt("swCheck") == -1) {
            swCheck = 0;
        } else {
            swCheck = tinyDB.getInt("swCheck");
        }

        if (tinyDB.getListInt("delayTime").size() == 0) {
            initialdate.set(0, 0, 0, 0, 0, 0);
        } else {
            timeDelay = tinyDB.getListInt("delayTime");
            initialdate.set(0, 0, 0, timeDelay.get(0), timeDelay.get(1), timeDelay.get(2));
            settedDate = new Date();
            settedDate.setHours(timeDelay.get(0));
            settedDate.setMinutes(timeDelay.get(1));
            settedDate.setSeconds(timeDelay.get(2));
            tvCallDial.setText(getTime(settedDate));
        }

        if (settedDate != null) {
            tvCallDial.setText(getTime(settedDate));
            initialdate.setTime(settedDate);
        }

        if (swCheck == 1) {
            swMotion.setChecked(true);
        } else {
            swMotion.setChecked(false);
        }
    }

    @Override
    public int getRGB(int rgbValue, int which) {
        return 0;
    }

    @Override
    public void beSet(int rgbValue, int which) {
        this.colorShower.setCardBackgroundColor(rgbValue);
        this.colorContainer.setAlpha(1f);
        this.themeContainer.setAlpha(0.5f);
        this.currentMotionDetect = COLOR_TRIGGER;
        tinyDB.putInt("currentMotionDetect", this.currentMotionDetect);
        tinyDB.putInt("motionDetectColor", rgbValue);
    }
}

