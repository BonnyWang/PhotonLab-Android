package com.example.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

//Added -Bonny
import android.bluetooth.BluetoothDevice;
import java.util.Set;
import android.content.IntentFilter;


public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private CardView mCardView;
    //TODO: This number defined could also be shown in the dialog see the official example -Bonny
    //Currently it is set to be 0 so the bluetooth would be enabled automatically -Bonny
    private final static int REQUEST_ENABLE_BT = 1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    to_Themes();
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.navigation_notifications2:
                    mTextMessage.setText(R.string.title_notifications2);
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
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Added for bluetooth function -Bonny
        //test whether device support bluetooth
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Log.d("bluetooth", "onCreate: Device does not have bluetooth");
            // Device doesn't support Bluetooth
            //TODO: need to add notification to remind user to -Bonny
        }

        //enable bluetooth if it is not turned on
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //startActivity(enableBtIntent);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        //Query paired devices -Bonny
        //query all paired devices and get the name and MAC address of each device -Bonny
        //TODO:this is just an example of query paired device, do we need it? -Bonny
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }

        Intent discoverableIntent = new Intent(
                BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(
                BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
//
//        if (bluetoothAdapter.isDiscovering()) {
//            bluetoothAdapter.cancelDiscovery();
//        }
//        bluetoothAdapter.startDiscovery();

    }

    public void to_Themes(){
        Intent it_Themes = new Intent();
        it_Themes.setClass(this, Themes.class);
        startActivity(it_Themes);
    }

    public class AcceptThread extends Thread{



    }

}







