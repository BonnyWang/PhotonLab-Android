package com.example.myapplication;

import android.Manifest;
import android.animation.Animator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import java.util.List;


public class fragment_Pair extends Fragment implements wifiRvAdapter.OnNoteListener{

    
    static final String TAG = "fragment_Pair";

    List<ScanResult> results;
    //pairing_Listener mlistener;
    wifiRvAdapter.OnNoteListener onNoteListener = this;
    String[] PERMS_INITIAL= {Manifest.permission.ACCESS_FINE_LOCATION};

    ProgressBar progressBar;
    ScrollView step2_Layout;
    ConstraintLayout step1_layout;
    Button yes_Connected;
    Button back;



    public fragment_Pair(){

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pair_layout, container, false);

        final Context context = getActivity().getApplicationContext();
        //Yo, This is really shitty
        requestPermissions(PERMS_INITIAL,127);
       // setListener(mlistener);
       // mlistener.scan_Wifi();
        final RecyclerView rv = (RecyclerView)view.findViewById(R.id.wifiRV);
        progressBar = view.findViewById(R.id.progressBar);
        yes_Connected = view.findViewById(R.id.YesConnected);
        back = view.findViewById(R.id.backButton_Pairing);
        step1_layout = view.findViewById(R.id.step1);
        step2_Layout = view.findViewById(R.id.step2);


        progressBar.setProgress(25);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        yes_Connected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_Gone(step1_layout);
                layout_Show(step2_Layout);
//                step2_Layout.setVisibility(View.VISIBLE);
//                step1_layout.setVisibility(View.GONE);
                progressBar.setProgress(50);
            }
        });



//
        final WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);

        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                boolean success = intent.getBooleanExtra(
                        WifiManager.EXTRA_RESULTS_UPDATED, false);
                if (success) {
                    results = wifiManager.getScanResults();
                    Log.d("SCAN", "onReceive:scan success ");
                    LinearLayoutManager llm = new LinearLayoutManager(context);
                    rv.setLayoutManager(llm);
                    wifiRvAdapter adapter = new wifiRvAdapter(results, onNoteListener);

                    rv.setAdapter(adapter);
                } else {
                    // scan failure handling
                    Log.d("SCAN", "onReceive:scan fail ");
                    scanFailure(wifiManager);
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        context.registerReceiver(wifiScanReceiver, intentFilter);

        Log.d(TAG, "onCreateView: Does it start scan?");
        wifiManager.setWifiEnabled(true);
        boolean success = wifiManager.startScan();
        if (!success) {
            // scan failure handling
            Log.d(TAG, "onCreateView: scan not success");
            scanFailure(wifiManager);
        }


//
//
//
//        wifiRvAdapter adapter = new wifiRvAdapter(results, this);
//
//        rv.setAdapter(adapter);


        // Inflate the layout for this fragment
        return view ;
    }

    private void scanSuccess(WifiManager wifiManager) {
        results = wifiManager.getScanResults();
    }

    private void scanFailure(WifiManager wifiManager) {
        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        results = wifiManager.getScanResults();
    }

    @Override
    public void onNoteClick(int position) {
        //TODO:Add later

    }


    public void layout_Show(View view){
        TranslateAnimation animate = new TranslateAnimation(0,view.getWidth(),0,0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }

    // To animate view slide out from right to left
    public void layout_Gone(View view){
        TranslateAnimation animate = new TranslateAnimation(0,-view.getWidth(),0,0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }

//    public interface pairing_Listener {
//        // TODO: Update argument type and name
//        public void scan_Wifi();
//    }
//
//    public void setListener(pairing_Listener mlistener){
//        this.mlistener = mlistener;
//
//    }

}
