package xyz.photonlab.photonlabandroid;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import xyz.photonlab.photonlabandroid.model.Session;


public class fragment_motion_detect extends FullScreenFragment {


    final Context context = getContext();
    LinearLayout mainContainer;
    TextView tvCallDial;
    Button backButton;
    Switch swMotion;
    TextView currentThemeTip;

    Calendar initialdate = Calendar.getInstance();

    TimePickerView pvTime;
    int swCheck;
    ArrayList<Integer> timeDelay;

    static Date settedDate = null;

    private static fragment_motion_detect single_instance;


    public static fragment_motion_detect getInstance() {
        if (single_instance == null)
            single_instance = new fragment_motion_detect();

        return single_instance;
    }

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

        final TinyDB tinyDB = new TinyDB(getContext());

        initialdate.set(0, 0, 0, 0, 0, 0);


        initialize();

        swMotion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    swCheck = 1;
                    tinyDB.putInt("swCheck", swCheck);

                } else {
                    swCheck = 0;
                    tinyDB.putInt("swCheck", swCheck);
                }
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

        tvCallDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        mainContainer.findViewById(R.id.switch_detect).setOnClickListener(v -> {

        });

        mainContainer.findViewById(R.id.time_delay).setOnClickListener(v -> {

        });

        mainContainer.findViewById(R.id.theme).setOnClickListener(v -> {
            ActivityOptions options = ActivityOptions.makeCustomAnimation(getContext(), R.anim.float_up, R.anim.float_down);
            Intent i = new Intent(getContext(), SelectMotionThemeActivity.class);
            ((Activity) getContext()).startActivityForResult(i, 0);
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("viewCreated", "motion_detect");
        Session.getInstance().requestTheme(getContext());
        int currentThemeIndex = Session.getInstance().getCurrentThemeIndex(getContext());
        if (currentThemeTip != null)
            currentThemeTip.setText(Session.getInstance().getMtheme().get(currentThemeIndex).getName());
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        return format.format(date);
    }

    private void initialize() {
        TinyDB tinyDB = new TinyDB(this.getContext());
        timeDelay = new ArrayList<>();

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

}


//Useless Code for the default TimePicker

//timePickerDialog = new TimePickerDialog(getContext(), onTimeSetListener,8, 60, true);
// timePickerDialog.show();
//        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                timePickerDialog.dismiss();
//            }
//        };

