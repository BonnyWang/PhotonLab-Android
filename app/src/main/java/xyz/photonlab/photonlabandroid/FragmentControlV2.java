package xyz.photonlab.photonlabandroid;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;
import xyz.photonlab.photonlabandroid.utils.NetworkHelper;
import xyz.photonlab.photonlabandroid.views.Light;
import xyz.photonlab.photonlabandroid.views.LightStage;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentControlV2 extends Fragment implements fragment_layout.OnSavedLayoutListener, Session.OnThemeChangeListener, dialog_colorpicker.colorPick_Listener, LightStage.OnLightCheckedChangeListener {


    private static final String TAG = "FragmentControlV2";

    //    group UI
    ConstraintLayout cl_group;
    TextView tv_group, tv_brightness, tv_off;
    CardView cv_power_back, cv_seek_back;
    ToggleButton tb_power;
    ImageView iv_sun;
    SeekBar sb_brightness;
    RadioGroup rg_group;
    RadioButton[] radioButtonsGroup, radioButtonsSingle;
    Button bt_add_color_group;
    View divider;
    ViewGroup vg_radio_button_group_container;

    //    Single UI
    ConstraintLayout cl_single;
    LinearLayout ll_content_container;
    TextView tv_single, tv_go_to_layout;
    Button bt_add_color_single;
    LightStage lightStage;
    ViewGroup vg_radio_button_single_container;
    RadioGroup[] rg_single;

    //animation
    Animation anim_slide_in_left, anim_slide_out_right, anim_slide_out_left, anim_slide_in_right, anim_pop_enter, anim_pop_out;


    //models
    Session session = Session.getInstance();
    int colorSelected;
    int colorUnselected;
    int brightness = 100;
    TinyDB tinyDB;
    ArrayList<Integer> colorsQueueGroup = new ArrayList<>();
    ArrayList<Integer> colorsQueueSingle = new ArrayList<>();
    int currentGroupColor = Color.TRANSPARENT;
    int sunUnselectedColor;
    Vibrator vibrator;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tinyDB = new TinyDB(getContext());
        session.registerOnLayoutSaveListener(this);
        return inflater.inflate(R.layout.fragment_control_layout_v2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vibrator = (Vibrator) Objects.requireNonNull(getActivity()).getSystemService(VIBRATOR_SERVICE);
        initView(view);
        addViewEvent();
        initialize();
        initTheme(session.isDarkMode(getContext()));
    }

    private void initView(View contentView) {
        //group
        cl_group = contentView.findViewById(R.id.all_container);
        tv_group = contentView.findViewById(R.id.tv_select_all);
        cv_power_back = contentView.findViewById(R.id.powerCard);
        tb_power = contentView.findViewById(R.id.Power);
        sb_brightness = contentView.findViewById(R.id.seekBar_brightness);
        iv_sun = contentView.findViewById(R.id.sun);
        cv_seek_back = contentView.findViewById(R.id.seek_container);
        tv_brightness = contentView.findViewById(R.id.progress_tip);
        tv_off = contentView.findViewById(R.id.progress_off_tip);
        rg_group = contentView.findViewById(R.id.radioGroup);
        divider = contentView.findViewById(R.id.divider);
        vg_radio_button_group_container = contentView.findViewById(R.id.colors_1);
        bt_add_color_group = contentView.findViewById(R.id.AddColor);

        radioButtonsGroup = new RadioButton[4];
        radioButtonsGroup[0] = contentView.findViewById(R.id.rButton1);
        radioButtonsGroup[1] = contentView.findViewById(R.id.rButton2);
        radioButtonsGroup[2] = contentView.findViewById(R.id.rButton3);
        radioButtonsGroup[3] = contentView.findViewById(R.id.rButton4);
        vg_radio_button_single_container = contentView.findViewById(R.id.groups_container);

        //single
        cl_single = contentView.findViewById(R.id.single_container);
        tv_single = contentView.findViewById(R.id.tv_select_single);
        ll_content_container = contentView.findViewById(R.id.content_container);
        tv_go_to_layout = contentView.findViewById(R.id.tvGotoSetup);
        radioButtonsSingle = new RadioButton[9];
        radioButtonsSingle[0] = contentView.findViewById(R.id.rButton01);
        radioButtonsSingle[1] = contentView.findViewById(R.id.rButton02);
        radioButtonsSingle[2] = contentView.findViewById(R.id.rButton03);
        radioButtonsSingle[3] = contentView.findViewById(R.id.rButton04);
        radioButtonsSingle[4] = contentView.findViewById(R.id.rButton05);
        radioButtonsSingle[5] = contentView.findViewById(R.id.rButton06);
        radioButtonsSingle[6] = contentView.findViewById(R.id.rButton07);
        radioButtonsSingle[7] = contentView.findViewById(R.id.rButton08);
        radioButtonsSingle[8] = contentView.findViewById(R.id.rButton09);
        bt_add_color_single = contentView.findViewById(R.id.AddColor00);
        rg_single = new RadioGroup[2];
        rg_single[0] = contentView.findViewById(R.id.radioGroup0);
        rg_single[1] = contentView.findViewById(R.id.radioGroup00);

        //animations
        anim_slide_in_left = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);
        anim_slide_out_right = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_right);
        anim_slide_out_left = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_left);
        anim_slide_in_right = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right);

        anim_pop_enter = AnimationUtils.loadAnimation(getContext(), R.anim.pop_enter);
        anim_pop_out = AnimationUtils.loadAnimation(getContext(), R.anim.pop_out);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void addViewEvent() {
        //group
        tv_group.setOnClickListener(v -> {
            if (cl_group.getVisibility() == View.VISIBLE)
                return;
            switchMainView(new Animation[]{anim_slide_out_right, anim_slide_in_left});
        });
        cl_group.setOnTouchListener(new SlideListener(true));

        tv_single.setOnClickListener(v -> {
            if (cl_group.getVisibility() == View.GONE)
                return;
            switchMainView(new Animation[]{anim_slide_out_left, anim_slide_in_right});
        });
        cl_single.setOnTouchListener(new SlideListener(true));
        cv_power_back.setOnClickListener(view -> {
            tb_power.setChecked(!tb_power.isChecked());
            vibrator.vibrate(50);
            toggleGroup(tb_power.isChecked());
            toggleSingle(tb_power.isChecked());
            tinyDB.putInt("Power", tb_power.isChecked() ? 1 : 0);
            if (!tb_power.isChecked()) {
                requestGroupColorChange(Color.BLACK, 0);
                if (lightStage != null) {
                    List<Light> lights = lightStage.getLights();
                    if (lights != null) {
                        for (Light light : lights) {
                            light.setPlaneColor(Color.rgb(200, 200, 200));
                        }
                    }
                    session.saveLayoutToLocal(getContext(), lightStage);
                }
            } else {
                requestGroupColorChange(currentGroupColor, brightness);
                if (lightStage != null) {
                    List<Light> lights = lightStage.getLights();
                    if (lights != null) {
                        for (Light light : lights) {
                            light.setPlaneColor(currentGroupColor);
                        }
                    }
                    session.saveLayoutToLocal(getContext(), lightStage);
                }
            }
        });

        sb_brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_brightness.setText(progress + "%");
                brightness = progress;
                tinyDB.putInt("Brightness", brightness);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sb_brightness.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.i(TAG, "Drag Over");
                requestGroupColorChange(currentGroupColor, brightness);
                if (lightStage != null) {
                    List<Light> lights = lightStage.getLights();
                    if (lights != null) {

                        Log.i(TAG, "save" + lights.size() + "");
                        for (Light light : lights) {
                            light.setPlaneColor(currentGroupColor);
                            Log.i(TAG, light + "|");
                        }
                    }
                    session.saveLayoutToLocal(getContext(), lightStage);
                }
            }
            return false;
        });

        for (RadioButton button : radioButtonsGroup) {
            button.setOnClickListener(new GroupColorSelectListener());
        }

        bt_add_color_group.setOnClickListener(v -> {
            FragmentManager manager = getFragmentManager();
            if (manager != null) {
                Log.i(TAG, "addViewEvent: ");
                dialog_colorpicker colorpicker = dialog_colorpicker.newInstance(0);
                colorpicker.setListener(this);
                manager.beginTransaction()
                        .setCustomAnimations(R.anim.pop_enter, 0, 0, R.anim.pop_out)
                        .replace(R.id.container, colorpicker)
                        .addToBackStack(null)
                        .commit();
            }

        });


        tv_go_to_layout.setOnClickListener(v -> {
            fragment_layout fragmentLayout = new fragment_layout();
            FragmentTransaction ftl = getActivity().getSupportFragmentManager().beginTransaction();
            ftl.setCustomAnimations(R.anim.pop_enter, R.anim.pop_out, R.anim.pop_enter, R.anim.pop_out);
            ftl.replace(R.id.container, fragmentLayout).addToBackStack(null);
            ftl.commit();
        });

        bt_add_color_single.setOnClickListener(v -> {
            FragmentManager manager = getFragmentManager();
            if (manager != null) {
                dialog_colorpicker colorpicker = dialog_colorpicker.newInstance(1);
                colorpicker.setListener(this);
                Log.i(TAG, "addViewEvent: ");
                manager.beginTransaction()
                        .setCustomAnimations(R.anim.pop_enter, 0, 0, R.anim.pop_out)
                        .replace(R.id.container, colorpicker)
                        .addToBackStack(null)
                        .commit();
            }
        });

        for (RadioButton button : radioButtonsSingle) {
            button.setOnClickListener(new SingleColorSelectListener());
        }

    }

    @SuppressLint("SetTextI18n")
    private void initialize() {
        session.addOnThemeChangeListener(this);
        if (tinyDB.getInt("color0") == -1) {
            Log.d("kan", "initialize_Colors: 0");
            colorsQueueGroup.add(getResources().getColor(R.color.yellow, null));
            colorsQueueGroup.add(getResources().getColor(R.color.blue, null));
            colorsQueueGroup.add(getResources().getColor(R.color.orange, null));
            colorsQueueGroup.add(getResources().getColor(R.color.purple, null));
        } else {
            colorsQueueGroup.add(tinyDB.getInt("color0"));
            colorsQueueGroup.add(tinyDB.getInt("color1"));
            colorsQueueGroup.add(tinyDB.getInt("color2"));
            colorsQueueGroup.add(tinyDB.getInt("color3"));
        }

        if (tinyDB.getInt("color00") == -1) {
            Log.d("kan", "initialize_Colors1: 0");
            colorsQueueSingle.add(getResources().getColor(R.color.yellow, null));
            colorsQueueSingle.add(getResources().getColor(R.color.blue, null));
            colorsQueueSingle.add(getResources().getColor(R.color.orange, null));
            colorsQueueSingle.add(getResources().getColor(R.color.purple, null));
            colorsQueueSingle.add(getResources().getColor(R.color.purple, null));
            colorsQueueSingle.add(getResources().getColor(R.color.yellow, null));
            colorsQueueSingle.add(getResources().getColor(R.color.blue, null));
            colorsQueueSingle.add(getResources().getColor(R.color.orange, null));
            colorsQueueSingle.add(getResources().getColor(R.color.purple, null));
        } else {
            colorsQueueSingle.add(tinyDB.getInt("color00"));
            colorsQueueSingle.add(tinyDB.getInt("color01"));
            colorsQueueSingle.add(tinyDB.getInt("color02"));
            colorsQueueSingle.add(tinyDB.getInt("color03"));
            colorsQueueSingle.add(tinyDB.getInt("color04"));
            colorsQueueSingle.add(tinyDB.getInt("color05"));
            colorsQueueSingle.add(tinyDB.getInt("color06"));
            colorsQueueSingle.add(tinyDB.getInt("color07"));
            colorsQueueSingle.add(tinyDB.getInt("color08"));
        }

        brightness = tinyDB.getInt("Brightness");
        if (brightness == -1) {
            brightness = 0;
        }
        sb_brightness.setProgress(brightness);
        tv_brightness.setText(brightness + "%");
        if (tinyDB.getInt("Power") == 1) {
            cv_power_back.setCardBackgroundColor(0xff67d96a);
            tb_power.setChecked(true);
            toggleGroup(true);
            toggleSingle(true);
        }
        if (tinyDB.getInt("colorGroupIndex") != -1) {
            int index = tinyDB.getInt("colorGroupIndex");
            //radioButtonsGroup[index].setChecked(true);
            currentGroupColor = colorsQueueGroup.get(index);
            tinyDB.putInt("colorGroup", currentGroupColor);
            tinyDB.putInt("colorGroupIndex", index);
            radioButtonsGroup[index].setChecked(true);
            sb_brightness.setProgressTintList(ColorStateList.valueOf(currentGroupColor));
//            requestGroupColorChange(currentGroupColor, brightness);
//            requestGroupColorChange(currentGroupColor, brightness);
            if (lightStage != null) {
                Log.i(TAG, lightStage + "|for");
                List<Light> lights = lightStage.getLights();
                if (lights != null) {
                    for (Light light : lights) {
                        light.setPlaneColor(currentGroupColor);
                    }
                }
                session.saveLayoutToLocal(getContext(), lightStage);
            }
        }

        if (tinyDB.getInt("colorGroup") == -1) {
            currentGroupColor = colorsQueueGroup.get(0);
            sb_brightness.setProgressTintList(ColorStateList.valueOf(currentGroupColor));
            radioButtonsGroup[0].setChecked(true);
        } else {
            currentGroupColor = tinyDB.getInt("colorGroup");
            sb_brightness.setProgressTintList(ColorStateList.valueOf(tinyDB.getInt("colorGroup")));
        }
        refreshGroupRadioButtons();
        refreshLightStage();
        refreshSingleRadioButtons();
    }

    private void toggleSingle(boolean checked) {
        if (checked) {
            for (RadioButton radioButton : radioButtonsSingle) {
                radioButton.setEnabled(true);
            }
            bt_add_color_single.setEnabled(true);
            vg_radio_button_single_container.setAlpha(1f);
        } else {
            for (RadioButton radioButton : radioButtonsSingle) {
                radioButton.setEnabled(false);
            }
            bt_add_color_single.setEnabled(false);
            vg_radio_button_single_container.setAlpha(0.45f);
        }
    }


    @Override
    public void initTheme(boolean dark) {

        if (dark) {
            this.colorSelected = Theme.Dark.SELECTED_TEXT;
            this.colorUnselected = Theme.Dark.UNSELECTED_TEXT;
            iv_sun.setImageResource(R.drawable.ic_moon);
            cv_seek_back.setCardBackgroundColor(Color.parseColor("#ff505154"));
            divider.setBackgroundColor(Theme.Dark.UNSELECTED_TEXT);
            sunUnselectedColor = Theme.Dark.UNSELECTED_TEXT;
        } else {
            this.colorSelected = Theme.Normal.SELECTED_TEXT;
            this.colorUnselected = Theme.Normal.UNSELECTED_TEXT;
            iv_sun.setImageResource(R.drawable.bright_sun);
            cv_seek_back.setCardBackgroundColor(Color.parseColor("#ffededed"));
            divider.setBackgroundColor(Theme.Normal.UNSELECTED_TEXT);
            sunUnselectedColor = Theme.Normal.UNSELECTED_TEXT;
        }
        boolean isGroup = cl_group.getVisibility() == View.VISIBLE;
        tv_group.setTextColor(isGroup ? colorSelected : colorUnselected);
        tv_single.setTextColor(isGroup ? colorUnselected : colorSelected);
        boolean isPowerOn = tb_power.isChecked();
        iv_sun.setColorFilter(isPowerOn ? 0xffffd41f : sunUnselectedColor);
        if (!isPowerOn)
            cv_power_back.setCardBackgroundColor(colorUnselected);

    }

    private void switchMainView(Animation[] animation) {

        if (cl_group.getVisibility() == View.VISIBLE) {
            cl_group.startAnimation(animation[0]);
            cl_single.startAnimation(animation[1]);
            cl_group.setVisibility(View.GONE);
            cl_single.setVisibility(View.VISIBLE);
        } else {
            cl_single.startAnimation(animation[0]);
            cl_group.startAnimation(animation[1]);
            cl_single.setVisibility(View.GONE);
            cl_group.setVisibility(View.VISIBLE);
        }

        boolean isGroup = cl_group.getVisibility() == View.VISIBLE;
        tv_group.setTextColor(isGroup ? colorSelected : colorUnselected);
        tv_single.setTextColor(isGroup ? colorUnselected : colorSelected);

    }

    @SuppressLint("SetTextI18n")
    private void toggleGroup(boolean checked) {
        if (checked) {
            sb_brightness.setVisibility(View.VISIBLE);
            tv_brightness.setVisibility(View.VISIBLE);
            tv_off.setVisibility(View.GONE);
            sb_brightness.clearAnimation();
            sb_brightness.startAnimation(anim_pop_enter);
            iv_sun.setColorFilter(0xffffd41f);
            tinyDB.putInt("Power", 1);
            tv_brightness.setText(brightness + "%");
            cv_power_back.setCardBackgroundColor(0xff67d96a);
            for (RadioButton radioButton : radioButtonsGroup) {
                radioButton.setEnabled(true);
            }
            bt_add_color_group.setEnabled(true);
            vg_radio_button_group_container.setAlpha(1f);
        } else {
            iv_sun.setColorFilter(sunUnselectedColor);
            sb_brightness.setVisibility(View.GONE);
            tv_brightness.setVisibility(View.GONE);
            tv_off.setVisibility(View.VISIBLE);
            sb_brightness.clearAnimation();
            sb_brightness.startAnimation(anim_pop_out);
            cv_power_back.setCardBackgroundColor(colorUnselected);
            for (RadioButton radioButton : radioButtonsGroup) {
                radioButton.setEnabled(false);
            }
            bt_add_color_group.setEnabled(false);
            vg_radio_button_group_container.setAlpha(0.45f);
        }
    }

    @Override
    public int getRGB(int rgbValue, int which) {
        rgbValue = rgbValue & 0x00ffffff | 0xff000000;
        if (which == 0) {
            beSet(rgbValue, which);
            tinyDB.putInt("colorGroupIndex", 3);
            colorsQueueGroup.remove(0);
            colorsQueueGroup.add(rgbValue);
            radioButtonsGroup[3].setChecked(true);
            refreshGroupRadioButtons();
        } else {
            if (lightStage != null) {
                beSet(rgbValue, which);
                colorsQueueSingle.remove(0);
                colorsQueueSingle.add(rgbValue);
                refreshSingleRadioButtons();
                rg_single[0].clearCheck();
                rg_single[1].clearCheck();
                radioButtonsSingle[8].setChecked(true);
            }
        }
        return 0;
    }

    @Override
    public void beSet(int rgbValue, int which) {
        rgbValue = rgbValue & 0x00ffffff | 0xff000000;
        if (which == 0) {
            tb_power.setChecked(true);
            cv_power_back.setCardBackgroundColor(0xff67d96a);
            //clear selection
            rg_group.clearCheck();
            sb_brightness.getProgressDrawable().setTint(rgbValue);
            currentGroupColor = rgbValue;
            tinyDB.putInt("colorGroup", rgbValue);
            tinyDB.remove("colorGroupIndex");
            if (lightStage != null) {
                List<Light> lights = lightStage.getLights();
                if (lights != null) {
                    for (Light light : lights) {
                        light.setPlaneColor(currentGroupColor);
                    }
                }
                session.saveLayoutToLocal(getContext(), lightStage);
            }
            requestGroupColorChange(currentGroupColor, brightness);
        } else {
            if (lightStage != null) {
                lightStage.setPlaneColor(rgbValue);
                if (lightStage.getCurrentLight() != null)
                    requestSingleColorChange(rgbValue, brightness, lightStage.getCurrentLight().getNum());
                session.saveLayoutToLocal(getContext(), lightStage);
            }
        }
    }

    private void refreshGroupRadioButtons() {
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                32,
                getResources().getDisplayMetrics()
        );
        for (int i = 0; i < 4; i++) {
            tinyDB.putInt("color" + i, colorsQueueGroup.get(i));
            Objects.requireNonNull(radioButtonsGroup[i].getButtonDrawable())
                    .setTint(colorsQueueGroup.get(i));
        }
    }

    private void refreshSingleRadioButtons() {
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                32,
                getResources().getDisplayMetrics()
        );
        for (int i = 0; i < 9; i++) {
            tinyDB.putInt("color0" + i, colorsQueueSingle.get(i));
            Objects.requireNonNull(radioButtonsSingle[i].getButtonDrawable())
                    .setTint(colorsQueueSingle.get(i));
        }
    }


    @Override
    public void onSavedLayout(boolean saved) {
        if (saved)
            refreshLightStage();
    }

    private void requestGroupColorChange(int color, int brightness) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = hsv[2] * ((float) brightness) / 100f;
        color = Color.HSVToColor(255, hsv);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        String url = "http://" + session.getLocalIP(getContext()) + "/" + "mode" + "/all"
                + "?red=" + r
                + "&green=" + g
                + "&blue=" + b;
        Log.i(TAG, url);
        NetworkHelper helper = new NetworkHelper();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Log.w(TAG, "Group Color Change");
        helper.connect(request, new OkHttpClient.Builder().readTimeout(10, TimeUnit.MILLISECONDS).build());
    }

    private void requestSingleColorChange(int color, int brightness, long num) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = hsv[2] * ((float) brightness) / 100f;
        color = Color.HSVToColor(255, hsv);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        String url = "http://" + session.getLocalIP(getContext()) + "/" + "mode" + "/single"
                + "?red=" + r
                + "&green=" + g
                + "&blue=" + b
                + "&node=" + num;
        Log.i(TAG, url);
        NetworkHelper helper = new NetworkHelper();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Log.w(TAG, "Single Color Change");
        helper.connect(request, new OkHttpClient.Builder().readTimeout(10, TimeUnit.MILLISECONDS).build());
    }

    private void refreshLightStage() {
        lightStage = Session.getInstance().requireLayoutStage(getContext(), true);
        if (lightStage != null && ll_content_container != null) {
            Log.i(TAG, lightStage + "|refresh");
            ll_content_container.removeAllViews();
            ll_content_container.addView(lightStage);
            lightStage.denyMove();
            lightStage.requireCenter();
            lightStage.setOnLightCheckedChangeListener(this);
        }
    }

    @Override
    public void onLightCheckedChanged(Light light) {
        Log.i("lightChanged", light + "");
        rg_single[0].clearCheck();
        rg_single[1].clearCheck();
    }

    private class SlideListener implements View.OnTouchListener {

        private final boolean returnFlag;
        private float origin = 0f;
        private float sensor = 100f;

        SlideListener(boolean returnFlag) {
            this.returnFlag = returnFlag;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            view.performClick();
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                origin = motionEvent.getX();
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                float distance = motionEvent.getX() - origin;
                if (distance > sensor) {//swipe right
                    tv_group.performClick();
                } else if (distance < -sensor) {
                    tv_single.performClick();
                }
            }
            return returnFlag;
        }
    }

    private class GroupColorSelectListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Log.i(TAG, "Group Radio Button Clicked" + v);
            int index = 0;
            for (int i = 0; i < radioButtonsGroup.length; i++)
                if (radioButtonsGroup[i] == v) {
                    index = i;
                    break;
                }
            currentGroupColor = colorsQueueGroup.get(index);
            tinyDB.putInt("colorGroup", currentGroupColor);
            tinyDB.putInt("colorGroupIndex", index);
            sb_brightness.setProgressTintList(ColorStateList.valueOf(currentGroupColor));
            requestGroupColorChange(currentGroupColor, brightness);
            vibrator.vibrate(50);

            if (lightStage != null) {
                Log.i(TAG, lightStage + "|for");
                List<Light> lights = lightStage.getLights();
                if (lights != null) {
                    for (Light light : lights) {
                        light.setPlaneColor(currentGroupColor);
                    }
                }
                session.saveLayoutToLocal(getContext(), lightStage);
            }
        }
    }

    private class SingleColorSelectListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Log.i(TAG, "Single Radio Button Clicked" + v);
            int index = 0;
            for (int i = 0; i < radioButtonsSingle.length; i++)
                if (radioButtonsSingle[i] == v) {
                    index = i;
                    break;
                }
            rg_single[0].clearCheck();
            rg_single[1].clearCheck();

            ((RadioButton) v).setChecked(true);
            if (lightStage != null) {
                lightStage.setPlaneColor(colorsQueueSingle.get(index));
                session.saveLayoutToLocal(getContext(), lightStage);
                if (lightStage.getCurrentLight() != null) {
                    requestSingleColorChange(colorsQueueSingle.get(index), 100, lightStage.getCurrentLight().getNum());
                    vibrator.vibrate(50);
                }
            }
        }
    }

}
