package xyz.photonlab.photonlabandroid;

import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class fragment_motion_detect extends Fragment {


    final Context context = getContext();
    TextView tvCallDial;
    Button backButton;
    Switch swMotion;

    Calendar initialdate = Calendar.getInstance();

    TimePickerView pvTime;
    int swCheck;
    ArrayList<Integer> timeDelay;

    static Date settedDate = null;

    private static fragment_motion_detect single_instance;


    public static fragment_motion_detect getInstance()
    {
        if ( single_instance == null)
            single_instance = new fragment_motion_detect();

        return single_instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_motion_detect, container, false);
        tvCallDial = view.findViewById(R.id.tvCallDialog);
        backButton = view.findViewById(R.id.backButton_Motion);
        swMotion = view.findViewById(R.id.swMotion);
        final TinyDB tinyDB = new TinyDB(getContext());

        initialdate.set(0,0,0,0,0,0);


        initialize();

        swMotion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    swCheck = 1;
                    tinyDB.putInt("swCheck", swCheck);

                }else {
                    swCheck = 0;
                    tinyDB.putInt("swCheck", swCheck);
                }
            }
        });

        pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvCallDial.setText(getTime(date));

                timeDelay.clear();
                timeDelay.add(date.getHours());
                timeDelay.add(date.getMinutes());
                timeDelay.add(date.getSeconds());
                TinyDB tinyDB = new TinyDB(getContext());
                tinyDB.putListInt("delayTime", timeDelay);

                settedDate = date;
                initialdate.setTime(settedDate);
                Log.i("pvTime", "onTimeSelect");

            }
        }).setType(new boolean[]{false, false, false, true, true, true})
                .setLabel("Year","Monty","Day","h","min","s")
                .setCancelText("Cancle")
                .setCancelColor(getResources().getColor(R.color.design_default_color_primary,null))
                .setSubmitText("Confirm")
                .setSubmitColor(getResources().getColor(R.color.design_default_color_primary,null))
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



        return view;
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        return format.format(date);
    }

    private void initialize(){
        TinyDB tinyDB = new TinyDB(this.getContext());
        timeDelay = new ArrayList<>();

        if(tinyDB.getInt("swCheck") == 0){
            swCheck = 0;
        }else {
            swCheck = tinyDB.getInt("swCheck");
        }

        if(tinyDB.getListInt("delayTime").size() == 0){
            initialdate.set(0,0,0,0,0,0);
        }else {
            timeDelay = tinyDB.getListInt("delayTime");
            initialdate.set(0,0,0,timeDelay.get(0),timeDelay.get(1),timeDelay.get(2));
            settedDate = new Date();
            settedDate.setHours(timeDelay.get(0));
            settedDate.setMinutes(timeDelay.get(1));
            settedDate.setSeconds(timeDelay.get(2));
            tvCallDial.setText(getTime(settedDate));
        }

        if(settedDate != null){
            tvCallDial.setText(getTime(settedDate));
            initialdate.setTime(settedDate);
        }

        if(swCheck == 1){
            swMotion.setChecked(true);
        }else {
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

