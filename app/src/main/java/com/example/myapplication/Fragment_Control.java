package com.example.myapplication;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.solver.widgets.Rectangle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;



public class Fragment_Control extends Fragment implements fragment_colorpicker.colorPick_Listener{

    SeekBar seek_bar;
    TextView text_view;
    int progressValue;
    CardView cardView;
    ToggleButton power;

    int checkedOrder;
    RadioGroup radioGroup;
    RadioButton[] rbuttons;
    Queue<Integer> colorOptions;
    GradientDrawable checked;
    RadioButton checkedButton;

    Button add;
    Fragment fragment_ColorPicker;
    static final String TAG = "fragment_Control";
    FrameLayout fl;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment__control_layout, container, false);

//        seek_bar= view.findViewById(R.id.seekBar5);
//        seek_bar.getParent().bringChildToFront(seek_bar);
        seekbar(view);
//        power = view.findViewById(R.id.PowerBackground);
//        power.setClipToOutline(true);
//        power.setElevation(10f);
        power = view.findViewById(R.id.Power);
        final SeekBar seekBar = view.findViewById(R.id.seekBar5);
        //TODO:Need to be pressed instead of click -Bonny
        power.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //seekBar.getProgressDrawable().setTint(getResources().getColor(R.color.seekBar_On, null));
                    power.getBackground().setTint(getResources().getColor(R.color.colorPrimary,null));

                    checkedButton = view.findViewById(radioGroup.getCheckedRadioButtonId());
                    checkedButton.setChecked(false);
                    checkedButton.setChecked(true);
                } else {
                    seekBar.getProgressDrawable().setTint(getResources().getColor(R.color.seekBar_Default, null));
                    power.getBackground().setTint(0xffffffff);
                }

            }
        });

        rbuttons = new RadioButton[4];
        rbuttons[0] = view.findViewById(R.id.rButton1);
        rbuttons[1] = view.findViewById(R.id.rButton2);
        rbuttons[2] = view.findViewById(R.id.rButton3);
        rbuttons[3] = view.findViewById(R.id.rButton4);

        rbuttons[0].setChecked(true);
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
                    checkedOrder = 0;
                        if(power.isChecked()){
                            seek_bar.getProgressDrawable().setTint(setCheckedColor(checkedOrder));
                        }

                        checked.setStroke(5, setCheckedColor(0));
                        rbuttons[0].setBackground(checked);

                    }

                    if(checkedId == R.id.rButton2){
                        checkedOrder = 1;
                        if(power.isChecked()){
                            seek_bar.getProgressDrawable().setTint(setCheckedColor(checkedOrder));
                        }
                        checked.setStroke(5, setCheckedColor(1));
                        rbuttons[1].setBackground(checked);

                    }
                    if(checkedId == R.id.rButton3){
                        checkedOrder = 2;
                        if(power.isChecked()){
                            seek_bar.getProgressDrawable().setTint(setCheckedColor(checkedOrder));
                        }
                        checked.setStroke(5, setCheckedColor(2));
                        rbuttons[2].setBackground(checked);

                    }

                    if(checkedId == R.id.rButton4){
                        checkedOrder = 3;
                        if(power.isChecked()){
                            seek_bar.getProgressDrawable().setTint(setCheckedColor(checkedOrder));
                        }
                        checked.setStroke(5, setCheckedColor(3));
                        rbuttons[3].setBackground(checked);

                }
            }
        });

        add = view.findViewById(R.id.AddColor);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragment_ColorPicker = new fragment_colorpicker();
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.pop_enter, R.anim.slide_down,
                        R.anim.pop_enter, R.anim.slide_down);
                fl = view.findViewById(R.id.control_Container);
                fl.bringToFront();
                fl.setVisibility(View.VISIBLE);
                ft.replace(R.id.control_Container, fragment_ColorPicker).addToBackStack(null);
                ft.commit();
            }
        });


        return view;


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
        colorOptions.add(getResources().getColor(R.color.yellow,null));
        colorOptions.add(getResources().getColor(R.color.blue,null));
        colorOptions.add(getResources().getColor(R.color.orange,null));
        colorOptions.add(getResources().getColor(R.color.purple,null));

    }
    //TODO: Need to add database to save user colors or it will return to default every time -Bonny
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

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof fragment_colorpicker) {
            fragment_colorpicker fragmentColorpicker = (fragment_colorpicker) fragment;
            fragmentColorpicker.setListener(this);
        }
    }

    @Override
    public int getRGB(int rgbValue){
        fl.setVisibility(View.INVISIBLE);
        colorOptions.remove();
        colorOptions.add(rgbValue);
        initialize_Rbuttons();
        rbuttons[0].setChecked(true);
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

