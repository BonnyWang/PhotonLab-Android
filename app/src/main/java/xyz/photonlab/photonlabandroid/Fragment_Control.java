package xyz.photonlab.photonlabandroid;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import xyz.photonlab.photonlabandroid.R;
import java.util.LinkedList;
import java.util.Queue;

import static android.content.Context.WINDOW_SERVICE;


public class Fragment_Control extends Fragment implements dialog_colorpicker.colorPick_Listener{

    SeekBar seek_bar;
    TextView text_view;
    TextView brightness;
    int progressValue;
    CardView cardView;
    ToggleButton power;
    ImageView sun;
    int checkedOrder;
    RadioGroup radioGroup;
    RadioButton[] rbuttons;
    static Queue<Integer> colorOptions;


    GradientDrawable checked;
    RadioButton checkedButton;

    Button add;

    int setMode;
    int seekBarColor;

    //TODO:Need to later write it in main activity instead, use interface to call the function in main
    WebView webView;

    ToggleButton SingleButton;
    ToggleButton AllButton;
    CardView Single;
    CardView All;
    Fragment fragment_ColorPicker;
    static final String TAG = "fragment_Control";
    FrameLayout fl;
    CardView powerCard;


    //Single layout
    TextView tvNoLayout;
    TextView tvGotoSetup;
    RadioGroup radioGroup0;
    RadioGroup radioGroup00;
    RadioButton[] rbuttons0;
    static Queue<Integer> colorOptions0;
    Button add0;
    GradientDrawable checked0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getContext().getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);



        final View view = inflater.inflate(R.layout.fragment__control_layout, container, false);
        webView = view.findViewById(R.id.webView);
        seekbar(view);
        //TODO:webview
        webView = view.findViewById(R.id.webView);
        power = view.findViewById(R.id.Power);
        sun = view.findViewById(R.id.sun);
        final SeekBar seekBar = view.findViewById(R.id.seekBar5);
        int heightInDP = Math.round(dm.heightPixels / dm.density);
        Log.d("fuck", "onCreateView: "+heightInDP);
        if (heightInDP<570) {
            Toast.makeText( getContext(), "Please change a phone", Toast.LENGTH_SHORT).show();
        }
        else if (heightInDP<650) {
            CardView cv=view.findViewById(R.id.cv);
            ViewGroup.LayoutParams a=cv.getLayoutParams();
            int b=(int) (250* (getResources().getDisplayMetrics().density));
            a.width=b;
            cv.setLayoutParams(a);
            ViewGroup.MarginLayoutParams c = (ViewGroup.MarginLayoutParams)cv.getLayoutParams();
            int d=(int) -(30* (getResources().getDisplayMetrics().density));
            Log.d("hello", "onCreateView: "+d);
            c.leftMargin=d;
            cv.setLayoutParams(c);
        }
        brightness = view.findViewById(R.id.tvBrightness);

        Single = view.findViewById(R.id.Single);
        All = view.findViewById(R.id.All);
        AllButton = view.findViewById(R.id.AllButton);
        SingleButton = view.findViewById(R.id.SingleButton);

        rbuttons = new RadioButton[4];
        rbuttons[0] = view.findViewById(R.id.rButton1);
        rbuttons[1] = view.findViewById(R.id.rButton2);
        rbuttons[2] = view.findViewById(R.id.rButton3);
        rbuttons[3] = view.findViewById(R.id.rButton4);

        fl = view.findViewById(R.id.seekBarCard);
        powerCard = view.findViewById(R.id.powerCard);

        //Single layout
        tvNoLayout = view.findViewById(R.id.noLayout);
        tvGotoSetup = view.findViewById(R.id.tvGotoSetup);
        radioGroup0 = view.findViewById(R.id.radioGroup0);
        radioGroup00 = view.findViewById(R.id.radioGroup00);
        rbuttons0 = new RadioButton[9];
        rbuttons0[0] = view.findViewById(R.id.rButton01);
        rbuttons0[1] = view.findViewById(R.id.rButton02);
        rbuttons0[2] = view.findViewById(R.id.rButton03);
        rbuttons0[3] = view.findViewById(R.id.rButton04);
        rbuttons0[4] = view.findViewById(R.id.rButton05);
        rbuttons0[5] = view.findViewById(R.id.rButton06);
        rbuttons0[6] = view.findViewById(R.id.rButton07);
        rbuttons0[7] = view.findViewById(R.id.rButton08);
        rbuttons0[8] = view.findViewById(R.id.rButton09);
        add0 = view.findViewById(R.id.AddColor00);

        radioGroup = view.findViewById(R.id.radioGroup);


        colorOptions = new LinkedList<>();
        colorOptions0 = new LinkedList<>();

        final TinyDB tinyDB = new TinyDB(getContext());
        if(tinyDB.getInt("Brightness") != -1){
            seekBar.setProgress(tinyDB.getInt("Brightness"));
        }else{
            seekBar.setProgress(100);
        }



        initialize_Colors();

        checked = new GradientDrawable();
        checked0 = new GradientDrawable();
        initialize_Rbuttons();
        add = view.findViewById(R.id.AddColor);

        power.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //seekBar.getProgressDrawable().setTint(getResources().getColor(R.color.seekBar_On, null));
                    power.getBackground().setTint(getResources().getColor(R.color.colorPrimary,null));


                    if(setMode == 0){
                        checkedButton = view.findViewById(radioGroup.getCheckedRadioButtonId());
                        sun.setColorFilter(0xffffd41f);
                        checkedButton.setChecked(false);
                        checkedButton.setChecked(true);
                        webView.loadUrl("http://192.168.50.48/H");
                    }else {
                        sun.setColorFilter(0xffffd41f);
                        seekBar.getProgressDrawable().setTint(seekBarColor);
                        webView.loadUrl("http://192.168.50.48/H");
                    }

                    tinyDB.putInt("Power",1);

                } else {
                    seekBar.getProgressDrawable().setTint(getResources().getColor(R.color.seekBar_Default, null));
                    sun.setColorFilter(getResources().getColor(R.color.seekBar_Default,null));
                    power.getBackground().setTint(0xffffffff);
                    webView.loadUrl("http://192.168.50.48/L");

                    tinyDB.putInt("Power",0);
                }

            }
        });

        AllButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    AllButton.setTextColor(getResources().getColor(R.color.backGround, null));
                    //All.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary, null) );
                    All.setBackground(getResources().getDrawable(R.drawable.button1, null));
                    SingleButton.setChecked(false);
                    setAllLayout();
                }else {
                    //All.setCardBackgroundColor(getResources().getColor(R.color.backGround, null) );
                    All.setBackground(getResources().getDrawable(R.drawable.button, null));
                    AllButton.setTextColor(getResources().getColor(R.color.DeepText, null));
                    setSingleLayout();
                }
            }
        });

        SingleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    SingleButton.setTextColor(getResources().getColor(R.color.backGround, null));
                    //Single.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary, null) );
                    Single.setBackground(getResources().getDrawable(R.drawable.button1, null));
                    AllButton.setChecked(false);
                    setSingleLayout();
                }else {
                    Single.setCardBackgroundColor(getResources().getColor(R.color.backGround, null) );
                    Single.setBackground(getResources().getDrawable(R.drawable.button, null));
                    SingleButton.setTextColor(getResources().getColor(R.color.DeepText, null));
                    setSingleLayout();
                }
            }
        });

        SingleButton.setChecked(true);
        SingleButton.setChecked(false);

        AllButton.setChecked(true);


        checkedOrder = 0;


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rButton1){
                        setColor(0);
                        tinyDB.putInt("rbutton", 0);
                }

                if(checkedId == R.id.rButton2){
                    setColor(1);
                    tinyDB.putInt("rbutton", 1);
                }

                if(checkedId == R.id.rButton3){
                    setColor(2);
                    tinyDB.putInt("rbutton", 2);
                }

                if(checkedId == R.id.rButton4){
                    setColor(3);
                    tinyDB.putInt("rbutton", 3);
                }
            }
        });

        radioGroup0.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                for(int i = 5; i < 9; i++){
                    rbuttons0[i].setChecked(false);
                }
                if(checkedId == R.id.rButton01){
                    setColor0(0);
                    Log.d(TAG, "onCheckedChanged: 00");
                }

                if(checkedId == R.id.rButton02){
                    setColor0(1);
                }

                if(checkedId == R.id.rButton03){
                    setColor0(2);
                }

                if(checkedId == R.id.rButton04){
                    setColor0(3);
                }
                if(checkedId == R.id.rButton05){
                    setColor0(4);
                }


            }
        });

        radioGroup00.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for(int i = 0; i < 5; i++){
                    rbuttons0[i].setChecked(false);
                }
                if(checkedId == R.id.rButton06){
                    setColor0(5);
                }

                if(checkedId == R.id.rButton07){
                    setColor0(6);
                }

                if(checkedId == R.id.rButton08){
                    setColor0(7);
                }
                if(checkedId == R.id.rButton09) {
                    setColor0(8);
                }
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = dialog_colorpicker.newInstance(0);
                ((dialog_colorpicker) newFragment).setListener(Fragment_Control.this);
                newFragment.show(getChildFragmentManager(), "dialog");
            }
        });

        add0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = dialog_colorpicker.newInstance(1);
                ((dialog_colorpicker) newFragment).setListener(Fragment_Control.this);
                newFragment.show(getChildFragmentManager(), "dialog");
            }
        });
        
        tvGotoSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: go to Setup");
                //TODO: need to add sth -Bonny
            }
        });


        if(tinyDB.getInt("rbutton") != -1){
            rbuttons[tinyDB.getInt("rbutton")].setChecked(true);
            rbuttons[tinyDB.getInt("rbutton")].setChecked(true);
        }else{
            rbuttons[0].setChecked(false);
            rbuttons[0].setChecked(true);
        }

        if(tinyDB.getInt("Power") != -1){
            if(tinyDB.getInt("Power") == 1){
                power.setChecked(true);
            }else {
                power.setChecked(false);
            }
        }else{
            power.setChecked(false);
        }
        return view;


    }

    public void setColor(int checkedOrder){

        setMode = 0;

        if(power.isChecked()){
            seek_bar.getProgressDrawable().setTint(setCheckedColor(checkedOrder));
        }
        checked.setStroke(5, setCheckedColor(checkedOrder));
        rbuttons[checkedOrder].setBackground(checked);
    }

    public void setColor0(int checkedOrder){

        checked0.setStroke(5, setCheckedColor0(checkedOrder));
        rbuttons0[checkedOrder].setBackground(checked0);

    }


    public void seekbar(View view) {
        seek_bar = (SeekBar) view.findViewById(R.id.seekBar5);
        text_view = (TextView) view.findViewById(R.id.bright_Value);
        progressValue = seek_bar.getProgress();
        text_view.setText(progressValue + "%");

        seek_bar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progressValue = progress;
                        text_view.setText(progressValue + "%");

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        text_view.setText(progressValue + "%");
                        TinyDB tinyDB = new TinyDB(getContext());
                        tinyDB.putInt("Brightness", progressValue);

                    }
                }
        );
    }



    public void initialize_Colors(){
        TinyDB tinydb = new TinyDB(this.getContext());

        if (tinydb.getInt("color0") == -1 ){
            Log.d("kan", "initialize_Colors: 0");

            colorOptions.add(getResources().getColor(R.color.yellow,null));
            colorOptions.add(getResources().getColor(R.color.blue,null));
            colorOptions.add(getResources().getColor(R.color.orange,null));
            colorOptions.add(getResources().getColor(R.color.purple,null));

        }else{
            colorOptions.add(tinydb.getInt("color0"));
            colorOptions.add(tinydb.getInt("color1"));
            colorOptions.add(tinydb.getInt("color2"));
            colorOptions.add(tinydb.getInt("color3"));
        }

        if (tinydb.getInt("color00") == -1 ){
            Log.d("kan", "initialize_Colors: 0");

            colorOptions0.add(getResources().getColor(R.color.yellow,null));
            colorOptions0.add(getResources().getColor(R.color.blue,null));
            colorOptions0.add(getResources().getColor(R.color.orange,null));
            colorOptions0.add(getResources().getColor(R.color.purple,null));
            colorOptions0.add(getResources().getColor(R.color.purple,null));
            colorOptions0.add(getResources().getColor(R.color.yellow,null));
            colorOptions0.add(getResources().getColor(R.color.blue,null));
            colorOptions0.add(getResources().getColor(R.color.orange,null));
            colorOptions0.add(getResources().getColor(R.color.purple,null));

        }else{

            colorOptions0.add(tinydb.getInt("color00"));
            colorOptions0.add(tinydb.getInt("color01"));
            colorOptions0.add(tinydb.getInt("color02"));
            colorOptions0.add(tinydb.getInt("color03"));
            colorOptions0.add(tinydb.getInt("color04"));
            colorOptions0.add(tinydb.getInt("color05"));
            colorOptions0.add(tinydb.getInt("color06"));
            colorOptions0.add(tinydb.getInt("color07"));
            colorOptions0.add(tinydb.getInt("color08"));
        }

    }

    public void initialize_Rbuttons(){
        for (int i = 0; i < 4 ; i++){
            rbuttons[i].getButtonDrawable().setTint(colorOptions.peek());
            colorOptions.add(colorOptions.remove());
        }

        for (int i = 0; i < 9 ; i++){
            rbuttons0[i].getButtonDrawable().setTint(colorOptions0.peek());
            colorOptions0.add(colorOptions0.remove());
        }
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                32,
                getResources().getDisplayMetrics()
        );


        int ipx = (int)px;
        checked.setShape(GradientDrawable.OVAL);
        checked.setSize(ipx,ipx);
        checked.setColors(new int[]{0x00000000,0x00000000});

        checked0.setShape(GradientDrawable.OVAL);
        checked0.setSize(ipx,ipx);
        checked0.setColors(new int[]{0x00000000,0x00000000});

    }

    int setCheckedColor(int which){
        int thisColor;
        for (int i = 0; i < 4 ; i++){
            rbuttons[i].setBackground(null);
        }

        int j;
        for (j = 0; j < which; j++){
            colorOptions.add(colorOptions.remove());
        }

        thisColor = colorOptions.peek();
        for (int k = which; j < 4; j++){
            colorOptions.add(colorOptions.remove());
        }

        return thisColor;

    }

    int setCheckedColor0(int which){
        int thisColor;
        for (int i = 0; i < 9 ; i++){
            rbuttons0[i].setBackground(null);
        }

        int j;
        for (j = 0; j < which; j++){
            colorOptions0.add(colorOptions0.remove());
        }

        thisColor = colorOptions0.peek();
        for (int k = which; j < 9; j++){
            colorOptions0.add(colorOptions0.remove());
        }

        return thisColor;

    }


    @Override
    public int getRGB(int rgbValue, int which){

        if(which == 0) {

            TinyDB tinydb = new TinyDB(getContext());

            colorOptions.remove();
            colorOptions.add(rgbValue);
            tinydb.remove("color0");
            tinydb.remove("color1");
            tinydb.remove("color2");
            tinydb.remove("color3");
            tinydb.putInt("color0", colorOptions.peek());
            colorOptions.add(colorOptions.remove());
            tinydb.putInt("color1", colorOptions.peek());
            colorOptions.add(colorOptions.remove());
            tinydb.putInt("color2", colorOptions.peek());
            colorOptions.add(colorOptions.remove());
            tinydb.putInt("color3", colorOptions.peek());
            colorOptions.add(colorOptions.remove());


            initialize_Rbuttons();
            rbuttons[3].setChecked(false);
            rbuttons[3].setChecked(true);
            seek_bar.setProgress(100);
            return rgbValue;
        }else{
            TinyDB tinydb = new TinyDB(getContext());
            colorOptions0.remove();
            colorOptions0.add(rgbValue);
            int a =1;

            for(int i = 0; i < 9; i++){
                tinydb.remove("color0" + i);
                Log.d(TAG, "getRGB: rmoveLoop");
            }



            for(int i = 0; i < 9; i++){
                String key = "color0" + i;
                tinydb.putInt(key, colorOptions0.peek());
                colorOptions0.add(colorOptions0.remove());
                Log.d(TAG, "getRGB: loop Sucess");
            }


            initialize_Rbuttons();
            rbuttons0[8].setChecked(false);
            rbuttons0[8].setChecked(true);
            return rgbValue;
        }
    }


    @Override
    public void beSet(int rgbValue, int which){

        power.setChecked(true);

        //clear selection
        radioGroup.clearCheck();
        for (int i = 0; i < 4 ; i++){
            rbuttons[i].setBackground(null);
        }

        seek_bar.setProgress(100);
        seek_bar.getProgressDrawable().setTint(rgbValue);
        seekBarColor = rgbValue;
        setMode = 1;

    }

    private void setAllLayout(){
        //Set visible
        power.setVisibility(View.VISIBLE);
        power.setClickable(true);
        seek_bar.setVisibility(View.VISIBLE);
        seek_bar.setActivated(true);
        sun.setVisibility(View.VISIBLE);
        for(int i = 0; i < 4; i++){
            rbuttons[i].setVisibility(View.VISIBLE);
            rbuttons[i].setClickable(true);
        }
        add.setVisibility(View.VISIBLE);
        add.setClickable(true);
        fl.setVisibility(View.VISIBLE);
        powerCard.setVisibility(View.VISIBLE);
        text_view.setVisibility(View.VISIBLE);
        brightness.setVisibility(View.VISIBLE);

        //hide
        tvGotoSetup.setVisibility(View.GONE);
        tvNoLayout.setVisibility(View.GONE);
        tvGotoSetup.setClickable(false);
        radioGroup0.setVisibility(View.GONE);
        radioGroup0.setClickable(false);
        radioGroup00.setVisibility(View.GONE);
        radioGroup00.setClickable(false);
        add0.setVisibility(View.GONE);
        add0.setClickable(false);
    }

    //reverse
    private void setSingleLayout(){
        //Set visible
        power.setVisibility(View.GONE);
        power.setClickable(false);
        seek_bar.setVisibility(View.GONE);
        seek_bar.setActivated(false);
        sun.setVisibility(View.GONE);
        for(int i = 0; i < 4; i++){
            rbuttons[i].setVisibility(View.GONE);
            rbuttons[i].setClickable(false);
        }
        add.setVisibility(View.GONE);
        add.setClickable(false);
        fl.setVisibility(View.GONE);
        powerCard.setVisibility(View.GONE);
        text_view.setVisibility(View.GONE);
        brightness.setVisibility(View.GONE);

        //hide
        tvGotoSetup.setVisibility(View.VISIBLE);
        tvNoLayout.setVisibility(View.VISIBLE);
        tvGotoSetup.setClickable(true);
        radioGroup0.setVisibility(View.VISIBLE);
        radioGroup0.setClickable(true);
        radioGroup00.setVisibility(View.VISIBLE);
        radioGroup00.setClickable(true);
        add0.setVisibility(View.VISIBLE);
        add0.setClickable(true);
    }

}


