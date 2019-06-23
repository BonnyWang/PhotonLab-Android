package com.example.myapplication;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.solver.widgets.Rectangle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;




public class Fragment_Control extends Fragment implements dialog_colorpicker.colorPick_Listener{

    SeekBar seek_bar;
    TextView text_view;
    int progressValue;
    CardView cardView;
    ToggleButton power;
    ImageView sun;
    int checkedOrder;
    RadioGroup radioGroup;
    RadioButton[] rbuttons;
    static Queue<Integer> colorOptions;

//    static ArrayList<Integer> colormmp= new ArrayList<>(4);
    GradientDrawable checked;
    RadioButton checkedButton;

    Button add;
    Fragment fragment_ColorPicker;
    static final String TAG = "fragment_Control";
    FrameLayout fl;

    //TODO:Need to later write it in main activity instead, use interface to call the function in main
    WebView webView;

    ToggleButton SingleButton;
    ToggleButton AllButton;
    CardView Single;
    CardView All;






    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment__control_layout, container, false);
        webView = view.findViewById(R.id.webView);
//        seek_bar= view.findViewById(R.id.seekBar5);
//        seek_bar.getParent().bringChildToFront(seek_bar);
        seekbar(view);
        //TODO:webview
        webView = view.findViewById(R.id.webView);

//        power = view.findViewById(R.id.PowerBackground);
//        power.setClipToOutline(true);
//        power.setElevation(10f);
        power = view.findViewById(R.id.Power);
        sun = view.findViewById(R.id.sun);
        final SeekBar seekBar = view.findViewById(R.id.seekBar5);

        Single = view.findViewById(R.id.Single);
        All = view.findViewById(R.id.All);
        AllButton = view.findViewById(R.id.AllButton);
        SingleButton = view.findViewById(R.id.SingleButton);

        //TODO:Need to be pressed instead of click -Bonny
        power.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //seekBar.getProgressDrawable().setTint(getResources().getColor(R.color.seekBar_On, null));
                    power.getBackground().setTint(getResources().getColor(R.color.colorPrimary,null));
                    checkedButton = view.findViewById(radioGroup.getCheckedRadioButtonId());
                    sun.setColorFilter(0xffffd41f);
                    checkedButton.setChecked(false);
                    checkedButton.setChecked(true);
                    webView.loadUrl("http://192.168.50.48/H");

                } else {
                    seekBar.getProgressDrawable().setTint(getResources().getColor(R.color.seekBar_Default, null));
                    sun.setColorFilter(getResources().getColor(R.color.seekBar_Default,null));                    //hello my friend
                    power.getBackground().setTint(0xffffffff);
                    webView.loadUrl("http://192.168.50.48/L");
                }

            }
        });

        AllButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    AllButton.setTextColor(getResources().getColor(R.color.backGround, null));
                    All.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary, null) );
                    SingleButton.setChecked(false);
                }else {
                    All.setCardBackgroundColor(getResources().getColor(R.color.backGround, null) );
                    AllButton.setTextColor(getResources().getColor(R.color.DeepText, null));
                }
            }
        });

        SingleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    SingleButton.setTextColor(getResources().getColor(R.color.backGround, null));
                    Single.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary, null) );
                    AllButton.setChecked(false);
                }else {
                    Single.setCardBackgroundColor(getResources().getColor(R.color.backGround, null) );
                    SingleButton.setTextColor(getResources().getColor(R.color.DeepText, null));
                }
            }
        });

        AllButton.setChecked(true);

        rbuttons = new RadioButton[4];
        rbuttons[0] = view.findViewById(R.id.rButton1);
        rbuttons[1] = view.findViewById(R.id.rButton2);
        rbuttons[2] = view.findViewById(R.id.rButton3);
        rbuttons[3] = view.findViewById(R.id.rButton4);


        checkedOrder = 0;

        radioGroup = view.findViewById(R.id.radioGroup);

        colorOptions = new LinkedList<>();
        initialize_Colors();

        checked = new GradientDrawable();
        initialize_Rbuttons();




        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rButton1){
                        setColor(0);
                    }

                    if(checkedId == R.id.rButton2){
                        setColor(1);
                    }

                    if(checkedId == R.id.rButton3){
                        setColor(2);
                    }

                    if(checkedId == R.id.rButton4){
                        setColor(3);
                    }
            }
        });

        add = view.findViewById(R.id.AddColor);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                fragment_ColorPicker = new fragment_colorpicker();
//                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
//                ft.setCustomAnimations(R.anim.pop_enter, R.anim.slide_down,
//                        R.anim.pop_enter, R.anim.slide_down);
//                fl = view.findViewById(R.id.control_Container);
//                fl.bringToFront();
//                fl.setVisibility(View.VISIBLE);
//                ft.replace(R.id.control_Container, fragment_ColorPicker).addToBackStack(null);
//                ft.commit();
                DialogFragment newFragment = dialog_colorpicker.newInstance();
                //newFragment.setTargetFragment(Fragment_Control.this,0);
                ((dialog_colorpicker) newFragment).setListener(Fragment_Control.this);
                newFragment.show(getChildFragmentManager(), "dialog");
            }
        });


        rbuttons[0].setChecked(false);
        rbuttons[0].setChecked(true);
        return view;


    }

    public void setColor(int checkedOrder){
        if(power.isChecked()){
            seek_bar.getProgressDrawable().setTint(setCheckedColor(checkedOrder));
        }
        checked.setStroke(5, setCheckedColor(checkedOrder));
        rbuttons[checkedOrder].setBackground(checked);
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
                        //Toast.makeText(MainActivity.this, "Seekbar is in progress", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        //Toast.makeText(MainActivity.this, "Seekbar is starttracking", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        text_view.setText(progressValue + "%");
                        //Toast.makeText(MainActivity.this, "Seekbar is stoptracking", Toast.LENGTH_LONG).show();

                    }
                }
        );
    }



    public void initialize_Colors(){
        TinyDB tinydb = new TinyDB(this.getContext());
//        tinydb.remove("bonny");//need to be comment
        if (tinydb.getInt("color0") == 0 ){
            Log.d("kan", "initialize_Colors: 0");
//            colormmp.add(0,getResources().getColor(R.color.yellow,null));
//            colormmp.add(1,getResources().getColor(R.color.blue,null));
//            colormmp.add(2,getResources().getColor(R.color.orange,null));
//            colormmp.add(3,getResources().getColor(R.color.purple,null));
            colorOptions.add(getResources().getColor(R.color.yellow,null));
            colorOptions.add(getResources().getColor(R.color.blue,null));
            colorOptions.add(getResources().getColor(R.color.orange,null));
            colorOptions.add(getResources().getColor(R.color.purple,null));
//            Log.d(TAG, "initialize_Colors: "+colormmp.size());
//            Log.d("kan", "initialize_Colors: 1");
//            tinydb.putListInt("bonny2",colormmp);
//            Log.d("kan", "ohhh"+tinydb.getListInt("bonny2").size());
        }else{
            colorOptions.add(tinydb.getInt("color0"));
            colorOptions.add(tinydb.getInt("color1"));
            colorOptions.add(tinydb.getInt("color2"));
            colorOptions.add(tinydb.getInt("color3"));
        }

//        for (int p =0; p <4;p++){
//            Log.d("kan", "initialize_Colors: -1");
//            colorOptions.add(tinydb.getListInt("bonny2").get(p));
//        }



    }

    public void initialize_Rbuttons(){
        for (int i = 0; i < 4 ; i++){
            rbuttons[i].getButtonDrawable().setTint(colorOptions.peek());
            colorOptions.add(colorOptions.remove());
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

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if( resultCode != Activity.RESULT_OK ) {
//            return;
//        }
//        if( requestCode == TARGET_FRAGMENT_REQUEST_CODE ) {
//            String greeting = data.getStringExtra(EXTRA_GREETING_MESSAGE);
//            showGreetingsTextView.setText(greeting);
//        }
//    }
    @Override
    public int getRGB(int rgbValue){
        //fl.setVisibility(View.INVISIBLE);
        TinyDB tinydb = new TinyDB(getContext());
//        Log.d("yes", "read or not"+ tinydb.getListInt("bonny2").size());
//        ArrayList<Integer> colormmp2 =tinydb.getListInt("bonny2");
//        for (int j=0; j<3;j++){
//            colormmp2.set(j,colormmp2.get(j+1));
//        }
//        colormmp2.set(3,rgbValue);
//        tinydb.remove("bonny2");
//        tinydb.putListInt("bonny2",colormmp2);
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
//        Log.d("yes", "getRGB: "+tinydb.getListInt("bonny2").get(3));
//        for (int  p =0; p <4;p++){
//            colorOptions.remove();
//        }
//        for (int p =0; p <4;p++){
//            Log.d("kan", "yesnono");
//            colorOptions.add(tinydb.getListInt("bonny2").get(p));}

        initialize_Rbuttons();
        rbuttons[3].setChecked(false);
        rbuttons[3].setChecked(true);
        return  rgbValue;
    }




}
//    public void onClick(View view){
//        switch (view.getId()){
//            case R.id.yellow_button:{
//                break;
//            }
//            case R.id.blue_button:{
//                break;
//            }
//            case R.id.purple_button:{
//                break;
//            }
//            case R.id.orange_button:{
//                break;
//            }
//
//        }
//
//    }

    //  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//    public void createButton(){
//
//        button_of_choice = (Drawable) getResources().getDrawable(R.drawable.button_of_choice);
//        button = new Button(this);
//        button.setBackground(button_of_choice);
//        LinearLayout ll = (LinearLayout)findViewById(R.id.coloroptions);
//        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        ll.addView(button, lp);
//    }
//    //根据选的颜色变，但要一开始先搞四个


