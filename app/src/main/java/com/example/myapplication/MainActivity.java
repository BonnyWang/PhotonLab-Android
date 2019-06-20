package com.example.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

//added for Fragment -Bonny
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    //implements View.OnClickListener
    private final String TAG = "Mainactivity";

    TextView text_view;
    SeekBar seek_bar;
    int progressValue;
    String colorOfChoice;
    Drawable button_of_choice;
    Button button;
    int whichanim = 0;

    BluetoothAdapter mbluetoothAdapter;
    //TODO:Delete this later -Bonny
    // BluetoothConnection mbluetoothConnection;
    BluetoothSocket mbluetoothSocket;
    BluetoothDevice mbluetoothDevice;
    BluetoothDevice target;

    private static final UUID my_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    ParcelUuid[] target_UUID;

    //Fragments
    static final Fragment fragment_Theme = new Fragment_Theme();
    static final Fragment fragment_Control = new Fragment_Control();
    static final Fragment fragment_Setting = new fragment_setting();
    static final Fragment fragment_music = new Fragment_Theme();
    Fragment start_anim;





    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                    ft1.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    ft1.replace(R.id.fgm, fragment_Control);
                    ft1.commit();
                    whichanim = 0;
                    return true;

                case R.id.navigation_dashboard:

                    //ragment_Theme = new Fragment_Theme();
                    FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                    if (whichanim < 2) {
                        ft2.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else {
                        ft2.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    }

                    whichanim = 2;
                    ft2.replace(R.id.fgm, fragment_Theme);
                    ft2.commit();
                    return true;
                case R.id.navigation_notifications:
                    FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                    if (whichanim < 3) {
                        ft3.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else {
                        ft3.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    }
                    whichanim = 3;
                    ft3.replace(R.id.fgm, fragment_music);
                    ft3.commit();
                    return true;

                case R.id.navigation_notifications2:
                    // Create the fragment and show it as a dialog.
                    //DialogFragment newFragment = dialog_colorpicker.newInstance();
                    //newFragment.show(getSupportFragmentManager(), "dialog");
                    FragmentTransaction ft4 = getSupportFragmentManager().beginTransaction();
                    ft4.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                    ft4.replace(R.id.fgm, fragment_Setting);
                    ft4.commit();
                    whichanim = 4;
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        start_anim = new fragment_start_anim();
        FragmentTransaction ft0 = getSupportFragmentManager().beginTransaction();
        ft0.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        ft0.replace(R.id.container, start_anim).addToBackStack(null);
        ft0.commit();
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //Second fragment after 5 seconds appears
                getSupportFragmentManager().popBackStack();
                //fragment_Control = new Fragment_Control();
                FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                ft1.replace(R.id.fgm, fragment_Control);
                ft1.commit();
            }
        };
        handler.postDelayed(runnable, 3000);





    }

//    @Override
//    public void scan_Wifi() {
//        Context context = getApplicationContext();
//        final WifiManager wifiManager = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        List<ScanResult> results;
//
//        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context c, Intent intent) {
//                boolean success = intent.getBooleanExtra(
//                        WifiManager.EXTRA_RESULTS_UPDATED, false);
//                if (success) {
//                     results = wifiManager.getScanResults();
//                } else {
//                    // scan failure handling
//                    results = wifiManager.getScanResults();
//                }
//            }
//        };
//
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);\
//        context.registerReceiver(wifiScanReceiver, intentFilter);
//
//        boolean success = wifiManager.startScan();
//        if (!success) {
//            // scan failure handling
//            List<ScanResult> results = wifiManager.getScanResults();
//        }
//
//
//
//    }
//
//    private void scanSuccess() {
//        List<ScanResult> results = wifiManager.getScanResults();
//        fragment_Pair fragment_pair = new fragment_Pair(results);
//        FragmentTransaction ftPair = getSupportFragmentManager().beginTransaction();
//        ftPair.replace(R.id.fgm,fragment_pair).addToBackStack(null);
//        ftPair.commit();
//
//    }
//
//    private void scanFailure() {
//        // handle failure: new scan did NOT succeed
//        // consider using old scan results: these are the OLD results!
//        List<ScanResult> results = wifiManager.getScanResults();
//
//    }
}


        //Set the fragment to be control by default -Bonny


//        seekbar();
//        Button Button1= (Button)findViewById(R.id.yellow_button);
//        Button1.setOnClickListener(this);
//        Button Button2= (Button)findViewById(R.id.blue_button);
//        Button2.setOnClickListener(this);
//        Button Button3= (Button)findViewById(R.id.orange_button);
//        Button3.setOnClickListener(this);
//        Button Button4= (Button)findViewById(R.id.purple_button);
//        Button4.setOnClickListener(this);

//        mbluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (mbluetoothAdapter == null) {
//            Log.d("bluetooth", "onCreate: Device does not have bluetooth");
//            // Device doesn't support BluetoothConnection
//            //TODO: need to add notification to remind user to -Bonny
//        }
//
//        //enable bluetooth if it is not turned on -Bonny
//        if (!mbluetoothAdapter.isEnabled()) {
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivity(enableBtIntent);
//            Log.d("BT", "onCreate: BT enabled");
//            //startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//        }

        //making the phone discoverable for 300s -Bonny
//        Intent discoverableIntent = new Intent(
//                BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//        discoverableIntent.putExtra(
//                BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
//        startActivity(discoverableIntent);
//
//        Set<BluetoothDevice> mBTdevices = mbluetoothAdapter.getBondedDevices();


        //May not need this if using bonded device-Bonny
        /*if(mbluetoothAdapter.isDiscovering()){
            Log.d("BT", "onCreate:1 ");
            mbluetoothAdapter.cancelDiscovery();

            mbluetoothAdapter.startDiscovery();
        }

        if(!mbluetoothAdapter.isDiscovering()){
            Log.d("BT", "onCreate: 2");
            mbluetoothAdapter.startDiscovery();
        }*/

//        for(BluetoothDevice bt:mBTdevices){
//            String name = "LE_WH-1000XM3";
//            Log.d("BT", "onCreate: " + bt.getName()+bt.getAddress());
//            Log.d("waht", name);
//            if(bt.getName().equals(name)){
//                target = bt;
//                Log.d("O", "This one! ");
//                target_UUID = target.getUuids();
//            }
//        }
//
//
//        //TODO:Do I need this? -Bonny
//        mbluetoothDevice = mbluetoothAdapter.getRemoteDevice(target.getAddress());
//
//        try{
//            mbluetoothSocket =mbluetoothDevice.createInsecureRfcommSocketToServiceRecord(target_UUID[0].getUuid());
//            Log.d(TAG, "Did I?");
//        }catch (IOException e){
//            Log.d(TAG, "This one I'm Testing ");
//        }
//
//        try {
//            mbluetoothSocket.connect();
//            Log.d(TAG, "onCreate: ??????");
//        } catch (IOException e) {
//            try {
//                mbluetoothSocket.close();
//            } catch (IOException e2) {
//                //insert code to deal with this
//                Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
//            }
//            //mbluetoothConnection.startClient(target, my_UUID);
//            //mbluetoothConnection.test();
//        }

//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
//
//
//    public void onClick(View v){
//        switch (v.getId()){
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
//
//    public void seekbar(){
//        seek_bar=(SeekBar)findViewById(R.id.seekBar5);
//        text_view=(TextView)findViewById(R.id.textView2);
//        progressValue=seek_bar.getProgress();
//        text_view.setText(progressValue+"%");
//
//        seek_bar.setOnSeekBarChangeListener(
//                new SeekBar.OnSeekBarChangeListener() {
//                    @Override
//                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                        progressValue = progress;
//                        text_view.setText(progressValue+"%");
//                        Toast.makeText(MainActivity.this,"Seekbar is in progress", Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onStartTrackingTouch(SeekBar seekBar) {
//                        Toast.makeText(MainActivity.this,"Seekbar is starttracking", Toast.LENGTH_LONG).show();
//
//                    }
//
//                    @Override
//                    public void onStopTrackingTouch(SeekBar seekBar) {
//                        text_view.setText(progressValue+"%");
//                        Toast.makeText(MainActivity.this,"Seekbar is stoptracking", Toast.LENGTH_LONG).show();
//
//                    }
//                }
//        );
//   }
//public void to_Themes(){
//    Intent it_Themes = new Intent();
//    it_Themes.setClass(this, nimadetiaoseban.class);
//    startActivity(it_Themes);
//}







