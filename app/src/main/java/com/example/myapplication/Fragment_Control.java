package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;


public class Fragment_Control extends Fragment {

    SeekBar seek_bar;
    TextView text_view;
    int progressValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__control_layout, container, false);
        seekbar(view);
        return view;

    }



    public void seekbar(View view) {
        seek_bar = (SeekBar)view.findViewById(R.id.seekBar5);
        text_view = (TextView)view.findViewById(R.id.bright_Value);
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

    public void onClick(View view){
        switch (view.getId()){
            case R.id.yellow_button:{
                break;
            }
            case R.id.blue_button:{
                break;
            }
            case R.id.purple_button:{
                break;
            }
            case R.id.orange_button:{
                break;
            }

        }

    }

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

}
