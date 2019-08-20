package xyz.photonlab.photonlabandroid;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.time.Duration;
import java.util.Iterator;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;
import xyz.photonlab.photonlabandroid.utils.NetworkHelper;
import xyz.photonlab.photonlabandroid.views.Light;
import xyz.photonlab.photonlabandroid.views.LightStage;

import static android.content.Context.VIBRATOR_SERVICE;
import static android.content.Context.WINDOW_SERVICE;


public class Fragment_Control extends Fragment implements dialog_colorpicker.colorPick_Listener, fragment_layout.OnSavedLayoutListener, LightStage.OnLightCheckedChangeListener, Session.OnThemeChangeListener {

    private TinyDB tinyDB;

    private int currentColor0, currentColor1;
    private GradientDrawable checked0, checked1;
    private boolean isAll;
    private LinearLayout content_container;

    private int progress;

    private ConstraintLayout allContainer, singleContainer;
    private TextView tvToAll, tvToSingle, brightness, tvGotoSetup, tvOff;
    private CardView powerBack;
    private ToggleButton power;
    private ImageView sun;
    private SeekBar seekBar;
    private RadioGroup group0, group1, group2;
    private RadioButton[] radioButtons0, radioButtons1;
    private Button add0, add1;
    private LightStage lightStage;

    private Queue<Integer> colorOptions0, colorOptions1;

    private Animation slide_in_left, slide_out_right, slide_out_left, slide_in_right, pop_enter, pop_out;
    private int brightness_value;
    private boolean isFirst = false;
    private int colorSelected;
    private int colorUnselected;
    private CardView seekBarContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void refreshLightStage() {
        lightStage = Session.getInstance().requireLayoutStage(getContext(), true);
        if (lightStage != null && content_container != null) {
            content_container.removeAllViews();
            content_container.addView(lightStage);
            lightStage.denyMove();
            lightStage.requireCenter();
            //lightStage.setOnTouchListener(new SlideListener(false));
            lightStage.setOnLightCheckedChangeListener(this);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DisplayMetrics dm = new DisplayMetrics();
        //check the screen
        if (getContext() != null) {
            WindowManager windowManager = (WindowManager) getContext().getSystemService(WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(dm);
            int heightInDP = Math.round(dm.heightPixels / dm.density);
            Log.d("fuck", "onCreateView: " + heightInDP);
            if (heightInDP < 570) {
                Toast.makeText(getContext(), "Please change a phone", Toast.LENGTH_SHORT).show();
                return new View(getContext());
            }
            slide_in_left = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);
            slide_out_right = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_right);
            slide_out_left = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_left);
            slide_in_right = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right);

            pop_enter = AnimationUtils.loadAnimation(getContext(), R.anim.pop_enter);
            pop_out = AnimationUtils.loadAnimation(getContext(), R.anim.pop_out);
        }
        if (Session.getInstance().getLocalIP(getContext()).equals("")) {
            Toast.makeText(getContext(), "Please Pair First", Toast.LENGTH_SHORT).show();
        }
        isAll = true;
        tinyDB = new TinyDB(getContext());
        final View contentView = inflater.inflate(R.layout.fragment_control_layout_v2, container, false);
        initView(contentView);
        addViewEvent();

        if (tinyDB.getInt("Brightness") != -1) {
            seekBar.setProgress(tinyDB.getInt("Brightness"));
            brightness.setText(tinyDB.getInt("Brightness") + "%");
        } else {
            seekBar.setProgress(100);
        }

        colorOptions0 = new LinkedBlockingQueue<>();
        colorOptions1 = new LinkedBlockingQueue<>();
        initializeColorQueues();
        wrapRadios();

        if (tinyDB.getInt("Power") == 1) {
            isFirst = true;
            power.performClick();
            seekBar.getProgressDrawable().setTint(currentColor0);
        }

        if (tinyDB.getInt("rbutton") != -1) {
            radioButtons0[tinyDB.getInt("rbutton")].performClick();
        } else {
            radioButtons0[0].performClick();
        }
        singleContainer.setVisibility(View.GONE);
        Session.getInstance().addOnThemeChangeListener(this);
        initTheme(Session.getInstance().isDarkMode(getContext()));
        return contentView;
    }

    private void wrapRadios() {
        for (int i = 0; i < 4; i++) {
            radioButtons0[i].getButtonDrawable().setTint(colorOptions0.peek());
            colorOptions0.add(colorOptions0.remove());
        }

        for (int i = 0; i < 9; i++) {
            radioButtons1[i].getButtonDrawable().setTint(colorOptions1.peek());
            colorOptions1.add(colorOptions1.remove());
        }
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                32,
                getResources().getDisplayMetrics()
        );

        int ipx = (int) px;
        checked0 = new GradientDrawable();
        checked0.setShape(GradientDrawable.OVAL);
        checked0.setSize(ipx, ipx);
        checked0.setColors(new int[]{0x00000000, 0x00000000});

        checked1 = new GradientDrawable();
        checked1.setShape(GradientDrawable.OVAL);
        checked1.setSize(ipx, ipx);
        checked1.setColors(new int[]{0x00000000, 0x00000000});
    }

    private void initializeColorQueues() {
        if (tinyDB.getInt("color0") == -1) {
            Log.d("kan", "initialize_Colors: 0");
            colorOptions0.add(getResources().getColor(R.color.yellow, null));
            colorOptions0.add(getResources().getColor(R.color.blue, null));
            colorOptions0.add(getResources().getColor(R.color.orange, null));
            colorOptions0.add(getResources().getColor(R.color.purple, null));
        } else {
            colorOptions0.add(tinyDB.getInt("color0"));
            colorOptions0.add(tinyDB.getInt("color1"));
            colorOptions0.add(tinyDB.getInt("color2"));
            colorOptions0.add(tinyDB.getInt("color3"));
        }
        if (tinyDB.getInt("rbutton") == -1)
            currentColor0 = colorOptions0.peek();
        else
            currentColor0 = setCheckedColor(tinyDB.getInt("rbutton"));

        if (tinyDB.getInt("color00") == -1) {
            Log.d("kan", "initialize_Colors1: 0");
            colorOptions1.add(getResources().getColor(R.color.yellow, null));
            colorOptions1.add(getResources().getColor(R.color.blue, null));
            colorOptions1.add(getResources().getColor(R.color.orange, null));
            colorOptions1.add(getResources().getColor(R.color.purple, null));
            colorOptions1.add(getResources().getColor(R.color.purple, null));
            colorOptions1.add(getResources().getColor(R.color.yellow, null));
            colorOptions1.add(getResources().getColor(R.color.blue, null));
            colorOptions1.add(getResources().getColor(R.color.orange, null));
            colorOptions1.add(getResources().getColor(R.color.purple, null));
        } else {
            colorOptions1.add(tinyDB.getInt("color00"));
            colorOptions1.add(tinyDB.getInt("color01"));
            colorOptions1.add(tinyDB.getInt("color02"));
            colorOptions1.add(tinyDB.getInt("color03"));
            colorOptions1.add(tinyDB.getInt("color04"));
            colorOptions1.add(tinyDB.getInt("color05"));
            colorOptions1.add(tinyDB.getInt("color06"));
            colorOptions1.add(tinyDB.getInt("color07"));
            colorOptions1.add(tinyDB.getInt("color08"));
        }
        currentColor1 = colorOptions1.peek();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addViewEvent() {
        tvToAll.setOnClickListener((view) -> {
            if (!isAll)
                toggleCurrentMainView();
        });
        tvToSingle.setOnClickListener((view) -> {
            if (isAll)
                toggleCurrentMainView();
        });

        allContainer.setOnTouchListener(new SlideListener(true));
        singleContainer.setOnTouchListener(new SlideListener(true));

        seekBar.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.i("requestColorChange", "==============================");
                float[] hsv = new float[3];
                Color.colorToHSV(currentColor0, hsv);
                hsv[2] = hsv[2] * brightness_value / 100;
                requestColorChange(Color.HSVToColor(hsv), true);
            }
            return false;
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String progressStr = progress + "%";
                brightness.setText(progressStr);
                Fragment_Control.this.progress = progress;
                tinyDB.putInt("Brightness", Fragment_Control.this.progress);
                brightness_value = seekBar.getProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });

        Vibrator vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);

        power.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                pop_enter.setDuration(8 * brightness_value);
                powerBack.setCardBackgroundColor(Color.parseColor("#67D96A"));
                seekBar.getProgressDrawable().setTint(currentColor0);
                seekBar.setVisibility(View.VISIBLE);
                if (isFirst) {
                    isFirst = false;
                } else {
                    seekBar.clearAnimation();
                    vibrator.vibrate(50);
                    seekBar.startAnimation(pop_enter);
                }
                brightness.setVisibility(View.VISIBLE);
                tvOff.setVisibility(View.GONE);
                sun.setColorFilter(0xffffd41f);
                tinyDB.putInt("Power", 1);
                Request request = new Request.Builder().url(" http://" + Session.getInstance().getLocalIP(getContext()) +
                        "/power/on").build();
                new NetworkHelper().connect(request);
            } else {
                pop_out.setDuration(8 * brightness_value);
                seekBar.setVisibility(View.GONE);
                if (isFirst) {
                    isFirst = false;
                } else {
                    seekBar.clearAnimation();
                    seekBar.startAnimation(pop_out);
                    vibrator.vibrate(50);
                }
                brightness.setVisibility(View.GONE);
                tvOff.setVisibility(View.VISIBLE);
                sun.setColorFilter(getResources().getColor(R.color.seekBar_Default, null));
                tinyDB.putInt("Power", 0);
                powerBack.setCardBackgroundColor(getResources().getColor(R.color.seekBar_Default, null));
                Request request = new Request.Builder().url(" http://" + Session.getInstance().getLocalIP(getContext()) + "/power/off").build();
                new NetworkHelper().connect(request);
            }
        });

        tvGotoSetup.setOnClickListener(v -> {
            //TODO: need to add sth -Bonny
            fragment_layout mfragment_layout = new fragment_layout();
            mfragment_layout.setListener(this);
            FragmentTransaction ftl = getActivity().getSupportFragmentManager().beginTransaction();
            ftl.setCustomAnimations(R.anim.pop_enter, R.anim.pop_out, R.anim.pop_enter, R.anim.pop_out);
            ftl.replace(R.id.container, mfragment_layout).addToBackStack(null);
            ftl.commit();
        });

        group0.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rButton1) {
                setColor(0);
                tinyDB.putInt("rbutton", 0);
            }

            if (checkedId == R.id.rButton2) {
                setColor(1);
                tinyDB.putInt("rbutton", 1);
            }

            if (checkedId == R.id.rButton3) {
                setColor(2);
                tinyDB.putInt("rbutton", 2);
            }

            if (checkedId == R.id.rButton4) {
                setColor(3);
                tinyDB.putInt("rbutton", 3);
            }

        });
        add0.setOnClickListener(view -> {
            DialogFragment newFragment = dialog_colorpicker.newInstance(0);
            ((dialog_colorpicker) newFragment).setListener(Fragment_Control.this);
            newFragment.show(getChildFragmentManager(), "dialog");
        });
        add1.setOnClickListener(v -> {
            DialogFragment newFragment = dialog_colorpicker.newInstance(1);
            ((dialog_colorpicker) newFragment).setListener(Fragment_Control.this);
            newFragment.show(getChildFragmentManager(), "dialog");
        });
        group1.setOnCheckedChangeListener((group, checkedId) -> {
            Log.i("checked change count", "");
            for (int i = 5; i < 9; i++) {
                radioButtons1[i].setChecked(false);
            }
            if (checkedId == R.id.rButton01) {
                setColor0(0);
                setPlaneColorByRadio(0);
            }

            if (checkedId == R.id.rButton02) {
                setColor0(1);
                setPlaneColorByRadio(1);
            }

            if (checkedId == R.id.rButton03) {
                setColor0(2);
                setPlaneColorByRadio(2);
            }

            if (checkedId == R.id.rButton04) {
                setColor0(3);
                setPlaneColorByRadio(3);
            }
            if (checkedId == R.id.rButton05) {
                setColor0(4);
                setPlaneColorByRadio(4);
            }


        });

        group2.setOnCheckedChangeListener((group, checkedId) -> {
            for (int i = 0; i < 5; i++) {
                radioButtons1[i].setChecked(false);
            }
            if (checkedId == R.id.rButton06) {
                setColor0(5);
                setPlaneColorByRadio(5);
            }

            if (checkedId == R.id.rButton07) {
                setColor0(6);
                setPlaneColorByRadio(6);
            }

            if (checkedId == R.id.rButton08) {
                setColor0(7);
                setPlaneColorByRadio(7);
            }
            if (checkedId == R.id.rButton09) {
                setColor0(8);
                setPlaneColorByRadio(8);
            }

        });
    }

    private void setPlaneColorByRadio(int which) {
        if (!radioButtons1[which].isChecked())
            return;
        if (lightStage != null) {
            Iterator<Integer> iterator = colorOptions1.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                int color = iterator.next();
                if (i == which) {
                    lightStage.setPlaneColor(color);
                    break;
                }
                i++;
            }
            Session.getInstance().saveLayoutToLocal(getContext(), lightStage);
        }
    }

    private void setPlaneColor(int src) {
        if (lightStage != null) {
            lightStage.setPlaneColor(src);
        }
        Session.getInstance().saveLayoutToLocal(getContext(), lightStage);
    }


    private void setColor(int checkedOrder) {
        currentColor0 = setCheckedColor(checkedOrder);
        if (power.isChecked()) {
            seekBar.getProgressDrawable().setTint(currentColor0);
        }
        //all radio selected color
        float[] hsv = new float[3];
        Color.colorToHSV(currentColor0, hsv);
        hsv[2] = hsv[2] * brightness_value / 100;
        requestColorChange(Color.HSVToColor(hsv), true);
        checked0.setStroke(5, setCheckedColor(checkedOrder));
        radioButtons0[checkedOrder].setBackground(checked0);
    }

    private void requestColorChange(int color, boolean global) {
        NetworkHelper helper = new NetworkHelper();
        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);
        String url;
        if (global)
            url = "http://" + Session.getInstance().getLocalIP(getContext())
                    + "/mode/all?red="
                    + red + "&green="
                    + green + "&blue="
                    + blue + "&brightness="
                    + ((int) (brightness_value / 100.0 * 253 + 2));
        else {
            Light light = null;
            if (lightStage != null)
                light = lightStage.getCurrentLight();
            if (light == null)
                return;
            url = "http://" + Session.getInstance().getLocalIP(getContext()) + "/mode/single?red=" + red + "&green=" + green + "&blue=" + blue
                    + "&node=" + light.getNum() + "&brightness=255";
        }
        Request request = new Request.Builder().get()
                .url(url)
                .build();
        helper.connect(request);
    }

    public void setColor0(int checkedOrder) {
        //all radio selected color
        currentColor1 = setCheckedColor0(checkedOrder);
        checked1.setStroke(5, setCheckedColor0(checkedOrder));
        radioButtons1[checkedOrder].setBackground(checked1);
        requestColorChange(currentColor1, false);
    }

    int setCheckedColor0(int which) {
        int thisColor;
        for (int i = 0; i < 9; i++) {
            radioButtons1[i].setBackground(null);
        }

        int j;
        for (j = 0; j < which; j++) {
            colorOptions1.add(colorOptions1.remove());
        }

        thisColor = colorOptions1.peek();
        for (int k = which; j < 9; j++) {
            colorOptions1.add(colorOptions1.remove());
        }

        return thisColor;

    }

    int setCheckedColor(int which) {
        int thisColor;
        for (int i = 0; i < 4; i++) {
            radioButtons0[i].setBackground(null);
        }
        int j;
        for (j = 0; j < which; j++) {
            colorOptions0.add(colorOptions0.remove());
        }
        thisColor = colorOptions0.peek();
        for (int k = which; j < 4; j++) {
            colorOptions0.add(colorOptions0.remove());
        }
        return thisColor;
    }

    private void toggleCurrentMainView() {
        allContainer.clearAnimation();
        singleContainer.clearAnimation();
        if (isAll) {
            allContainer.startAnimation(slide_out_left);
            singleContainer.startAnimation(slide_in_right);
            allContainer.setVisibility(View.GONE);
            singleContainer.setVisibility(View.VISIBLE);
            tvToSingle.setTextColor(colorSelected);
            tvToAll.setTextColor(colorUnselected);
        } else {
            allContainer.startAnimation(slide_in_left);
            singleContainer.startAnimation(slide_out_right);
            allContainer.setVisibility(View.VISIBLE);
            singleContainer.setVisibility(View.GONE);
            tvToAll.setTextColor(colorSelected);
            tvToSingle.setTextColor(colorUnselected);
        }
        isAll = !isAll;
    }

    private void initView(View contentView) {
        allContainer = contentView.findViewById(R.id.all_container);
        singleContainer = contentView.findViewById(R.id.single_container);
        tvToAll = contentView.findViewById(R.id.tv_select_all);
        tvToSingle = contentView.findViewById(R.id.tv_select_single);
        tvGotoSetup = contentView.findViewById(R.id.tvGotoSetup);
        brightness = contentView.findViewById(R.id.progress_tip);
        power = contentView.findViewById(R.id.Power);
        powerBack = contentView.findViewById(R.id.powerCard);
        sun = contentView.findViewById(R.id.sun);
        seekBar = contentView.findViewById(R.id.seekBar_brightness);
        add0 = contentView.findViewById(R.id.AddColor);
        add1 = contentView.findViewById(R.id.AddColor00);
        seekBarContainer = contentView.findViewById(R.id.seek_container);
        tvOff = contentView.findViewById(R.id.progress_off_tip);

        radioButtons0 = new RadioButton[4];
        radioButtons1 = new RadioButton[9];

        radioButtons0[0] = contentView.findViewById(R.id.rButton1);
        radioButtons0[1] = contentView.findViewById(R.id.rButton2);
        radioButtons0[2] = contentView.findViewById(R.id.rButton3);
        radioButtons0[3] = contentView.findViewById(R.id.rButton4);

        radioButtons1[0] = contentView.findViewById(R.id.rButton01);
        radioButtons1[1] = contentView.findViewById(R.id.rButton02);
        radioButtons1[2] = contentView.findViewById(R.id.rButton03);
        radioButtons1[3] = contentView.findViewById(R.id.rButton04);
        radioButtons1[4] = contentView.findViewById(R.id.rButton05);
        radioButtons1[5] = contentView.findViewById(R.id.rButton06);
        radioButtons1[6] = contentView.findViewById(R.id.rButton07);
        radioButtons1[7] = contentView.findViewById(R.id.rButton08);
        radioButtons1[8] = contentView.findViewById(R.id.rButton09);
        group0 = contentView.findViewById(R.id.radioGroup);
        group1 = contentView.findViewById(R.id.radioGroup0);
        group2 = contentView.findViewById(R.id.radioGroup00);
        content_container = contentView.findViewById(R.id.content_container);
        LinearLayout groups_container = contentView.findViewById(R.id.groups_container);
        groups_container.getLayoutParams().width = groups_container.getMeasuredHeight();
        refreshLightStage();
    }


    @Override
    public int getRGB(int rgbValue, int which) {

        if (which == 0) {

            TinyDB tinydb = new TinyDB(getContext());

            colorOptions0.remove();
            colorOptions0.add(rgbValue);
            tinydb.remove("color0");
            tinydb.remove("color1");
            tinydb.remove("color2");
            tinydb.remove("color3");
            tinydb.putInt("color0", colorOptions0.peek());
            colorOptions0.add(colorOptions0.remove());
            tinydb.putInt("color1", colorOptions0.peek());
            colorOptions0.add(colorOptions0.remove());
            tinydb.putInt("color2", colorOptions0.peek());
            colorOptions0.add(colorOptions0.remove());
            tinydb.putInt("color3", colorOptions0.peek());
            colorOptions0.add(colorOptions0.remove());


            wrapRadios();
            radioButtons0[3].setChecked(false);
            radioButtons0[3].setChecked(true);
            seekBar.setProgress(100);
        } else {
            TinyDB tinydb = new TinyDB(getContext());
            colorOptions1.remove();
            colorOptions1.add(rgbValue);
            int a = 1;

            for (int i = 0; i < 9; i++) {
                tinydb.remove("color0" + i);
            }

            for (int i = 0; i < 9; i++) {
                String key = "color0" + i;
                tinydb.putInt(key, colorOptions1.peek());
                colorOptions1.add(colorOptions1.remove());
            }
            wrapRadios();
            radioButtons1[8].setChecked(false);
            radioButtons1[8].setChecked(true);
        }
        return rgbValue;
    }


    @Override
    public void beSet(int rgbValue, int which) {
        if (which == 0) {
            power.setChecked(true);
            //clear selection
            group0.clearCheck();
            for (int i = 0; i < 4; i++) {
                radioButtons0[i].setBackground(null);
            }
            seekBar.setProgress(100);
            seekBar.getProgressDrawable().setTint(rgbValue);
            currentColor0 = rgbValue;
            requestColorChange(currentColor0, true);
        } else {
            group1.clearCheck();
            group2.clearCheck();
            for (RadioButton button : radioButtons1) {
                button.setBackground(null);
            }
            setPlaneColor(rgbValue);
            requestColorChange(rgbValue, false);
        }
    }

    @Override
    public void onSavedLayout(boolean saved) {
        if (saved)
            refreshLightStage();
    }

    @Override
    public void onLightCheckedChanged(Light light) {
        Log.i("lightChanged", light + "");
        if (group1 != null)
            group1.clearCheck();
        if (group2 != null)
            group2.clearCheck();
        for (RadioButton button : radioButtons1) {
            button.setBackground(null);
        }
    }

    @Override
    public void initTheme(boolean dark) {
        Class<? extends Theme.ThemeColors> colors;
        if (dark) {
            colors = Theme.Dark.class;
            seekBarContainer.setCardBackgroundColor(Color.parseColor("#505154"));
        } else
        {
            colors = Theme.Normal.class;
            seekBarContainer.setCardBackgroundColor(Color.parseColor("#ffededed"));
        }
        try {
            lightStage.setBackgroundColor(colors.getField("MAIN_BACKGROUND").getInt(null));
            this.colorSelected = colors.getField("SELECTED_TEXT").getInt(null);
            this.colorUnselected = colors.getField("UNSELECTED_TEXT").getInt(null);
            tvToAll.setTextColor(colorSelected);
            tvToSingle.setTextColor(colorUnselected);
            tvToAll.performClick();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    tvToAll.performClick();
                } else if (distance < -sensor) {
                    tvToSingle.performClick();
                }
            }
            return returnFlag;
        }
    }
}