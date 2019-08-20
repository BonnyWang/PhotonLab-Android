package xyz.photonlab.photonlabandroid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;

/**
 * created by kio on 2019/08/15 15:27
 * TODO # add HTTP request function
 */
public class FragmentSchedule extends Fragment implements dialog_colorpicker.colorPick_Listener {

    //Menu items
    private ConstraintLayout time_container, theme_container, color_container;

    //dynamic ui components
    private Switch schedule_switch, repeat_switch;
    private TextView tv_time, tv_theme;
    private CardView color_shower;
    private Button exit;
    private TimePickerView timePickerView;

    //models
    private final Session session = Session.getInstance();
    private TinyDB tinyDB;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragment_schdule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tinyDB = new TinyDB(getContext());
        initView(view);
        addViewEvent();
        initialize();
    }

    private void initView(@NonNull View contentView) {
        color_container = contentView.findViewById(R.id.switch_color);
        theme_container = contentView.findViewById(R.id.theme);
        time_container = contentView.findViewById(R.id.time_delay);

        schedule_switch = contentView.findViewById(R.id.swMotion);
        repeat_switch = contentView.findViewById(R.id.swRepeat);
        tv_time = contentView.findViewById(R.id.tvCallDialog);
        tv_theme = contentView.findViewById(R.id.tvTriggerName);

        color_shower = contentView.findViewById(R.id.color_shower);
        exit = contentView.findViewById(R.id.backButton_Motion);

        timePickerView = new TimePickerBuilder(getContext(), (date, v) -> {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            String timeStr = format.format(date);
            tv_time.setText(timeStr);
            tinyDB.putString("scheduleTime", timeStr);
        }).setType(new boolean[]{false, false, false, true, true, true})
                .setLabel("Year", "Monty", "Day", "h", "min", "s")
                .setCancelText("Cancel")
                .setCancelColor(getResources().getColor(R.color.colorPrimary, null))
                .setSubmitText("Confirm")
                .setSubmitColor(getResources().getColor(R.color.colorPrimary, null))
                .isDialog(true)
                .setDate(Calendar.getInstance())
                .build();
        if (Session.getInstance().isDarkMode(getContext())) {
            contentView.setBackgroundColor(Theme.Dark.MAIN_BACKGROUND);
            ((TextView) contentView.findViewById(R.id.tvMotion)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) contentView.findViewById(R.id.tvTime)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) contentView.findViewById(R.id.tvRepeat)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) contentView.findViewById(R.id.textView2)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) contentView.findViewById(R.id.tvThemeTrigger)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) contentView.findViewById(R.id.color_title)).setTextColor(Theme.Dark.SELECTED_TEXT);
            ((TextView) contentView.findViewById(R.id.tvTriggerName)).setTextColor(Theme.Dark.SELECTED_TEXT);
            contentView.findViewById(R.id.divider).setBackgroundColor(Theme.Dark.UNSELECTED_TEXT);
            exit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EA7D38")));
        }
    }

    private void addViewEvent() {
        exit.setOnClickListener(v -> Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack());

        schedule_switch.setOnCheckedChangeListener((buttonView, isChecked) -> tinyDB.putBoolean("scheduleSwitch", isChecked));

        repeat_switch.setOnCheckedChangeListener(((buttonView, isChecked) -> tinyDB.putBoolean("scheduleRepeat", isChecked)));

        theme_container.setOnClickListener(v -> startActivityForResult(new Intent(getContext(), SelectMotionThemeActivity.class), 0));

        color_container.setOnClickListener(v -> {
            dialog_colorpicker newFragment = dialog_colorpicker.newInstance(0);
            newFragment.dismissAddFav();
            newFragment.setListener(FragmentSchedule.this);
            newFragment.show(getChildFragmentManager(), "dialog");
        });

        time_container.setOnClickListener(v -> this.timePickerView.show());

    }


    /**
     * read data from local shared preferences
     */
    @SuppressLint("SetTextI18n")
    private void initialize() {
        schedule_switch.setChecked(tinyDB.getBoolean("scheduleSwitch"));
        if (!tinyDB.getString("scheduleTime").equals(""))
            tv_time.setText(tinyDB.getString("scheduleTime"));
        repeat_switch.setChecked(tinyDB.getBoolean("scheduleRepeat"));
        if (tinyDB.getInt("currentScheduleType") == fragment_motion_detect.COLOR_TRIGGER) {
            color_container.setAlpha(1f);
            theme_container.setAlpha(0.5f);
        }

        session.requestTheme(getContext());

        if (tinyDB.getInt("scheduleThemeIndex") != -1)
            tv_theme.setText(session.getMtheme().get(tinyDB.getInt("scheduleThemeIndex")).getName());
        if (tinyDB.getInt("scheduleColor") != -1)
            color_shower.setCardBackgroundColor(tinyDB.getInt("scheduleColor"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Select Theme Back", "");
        if (data == null)
            return;
        int themeIndex = data.getIntExtra("themeIndex", -1);
        if (themeIndex != -1) {
            theme_container.setAlpha(1f);
            color_container.setAlpha(0.5f);
            tinyDB.putInt("currentScheduleType", fragment_motion_detect.THEME_TRIGGER);
            tinyDB.putInt("scheduleThemeIndex", themeIndex);
            tv_theme.setText(session.getMtheme().get(themeIndex).getName());
        }
    }

    @Override
    public int getRGB(int rgbValue, int which) {
        //ignore
        return 0;
    }

    @Override
    public void beSet(int rgbValue, int which) {
        this.color_shower.setCardBackgroundColor(rgbValue);
        this.color_container.setAlpha(1f);
        this.theme_container.setAlpha(0.5f);
        tinyDB.putInt("currentScheduleType", fragment_motion_detect.COLOR_TRIGGER);
        tinyDB.putInt("scheduleColor", rgbValue);
    }
}
