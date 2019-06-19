package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class fragment_Pair extends Fragment implements wifiRvAdapter.OnNoteListener{

    Context context = getActivity().getApplicationContext();
    final static WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);

    List<ScanResult> results;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pair_layout, container, false);



        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                boolean success = intent.getBooleanExtra(
                        WifiManager.EXTRA_RESULTS_UPDATED, false);
                if (success) {
                    scanSuccess(wifiManager);
                } else {
                    // scan failure handling
                    scanFailure(wifiManager);
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        context.registerReceiver(wifiScanReceiver, intentFilter);

        boolean success = wifiManager.startScan();
        if (!success) {
            // scan failure handling
            scanFailure(wifiManager);
        }

        final RecyclerView rv = (RecyclerView)view.findViewById(R.id.wifiRV);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);


        wifiRvAdapter adapter = new wifiRvAdapter(results, this);

        rv.setAdapter(adapter);


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

}
