package com.example.myapplication;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.Toast;
;
//hello motto
public class Themes extends AppCompatActivity {
    private TextView mTextMessage;
    private static TextView text_view;
    private static SeekBar seek_bar;
    private int progressValue;
    String colorOfChoice;
    Drawable button_of_choice;
    Button button;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    to_Main();
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText("OKay");
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        seekbar();
        Button Button1= (Button)findViewById(R.id.yellow_button);
        Button1.setOnClickListener((View.OnClickListener) this);
        Button Button2= (Button)findViewById(R.id.yellow_button);
        Button2.setOnClickListener((View.OnClickListener) this);
        Button Button3= (Button)findViewById(R.id.yellow_button);
        Button3.setOnClickListener((View.OnClickListener) this);
        Button Button4= (Button)findViewById(R.id.yellow_button);
        Button4.setOnClickListener((View.OnClickListener) this);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void createButton(){

        button_of_choice = (Drawable) getResources().getDrawable(R.drawable.button_of_choice);
        button = new Button(this);
        button.setBackground(button_of_choice);
        LinearLayout ll = (LinearLayout)findViewById(R.id.coloroptions);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        ll.addView(button, lp);
    }
    //根据选的颜色变，但要一开始先搞四个
    public void to_Main(){
        finish();
    }
    public void onClick(View v){
        switch (v.getId()){
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

    public void seekbar(){
        seek_bar=(SeekBar)findViewById(R.id.seekBar5);
        text_view=(TextView)findViewById(R.id.textView2);
        progressValue=seek_bar.getProgress();
        text_view.setText(progressValue+"%");

        seek_bar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progressValue = progress;
                        text_view.setText(progressValue+"%");
                        Toast.makeText(Themes.this,"Seekbar is in progress", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        Toast.makeText(Themes.this,"Seekbar is starttracking", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        text_view.setText(progressValue+"%");
                        Toast.makeText(Themes.this,"Seekbar is stoptracking", Toast.LENGTH_LONG).show();

                    }
                }
        );
    }
}
