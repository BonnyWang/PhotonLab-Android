package xyz.photonlab.photonlabandroid;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import xyz.photonlab.photonlabandroid.model.Session;

//added for Fragment -Bonny

public class MainActivity extends AppCompatActivity {
    //implements fragment_Pair.pairing_Listener
    private final String TAG = "MainActivity";

    int whichanim = 0;

    Fragment start_anim = new fragment_start_anim();
    //Fragments
    Fragment[] fragments = new Fragment[4];

    static Handler handler = new Handler();
    static Runnable runnable;

    ConstraintLayout container;

    TinyDB tinyDB;

    private BottomNavigationView navView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = menuItem -> {
        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                createOrReplaceFragment(0);
                return true;
            case R.id.navigation_dashboard:
                createOrReplaceFragment(1);
                return true;
            case R.id.navigation_notifications:
                createOrReplaceFragment(2);
                return true;
            case R.id.Setting:
                createOrReplaceFragment(3);
                return true;
        }
        return false;
    };

    private synchronized void createOrReplaceFragment(int i) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        //if fragment is null, create and add to container
        if (fragments[i] == null) {
            switch (i) {
                case 1:
                    fragments[i] = new Fragment_Theme();
                    break;
                case 2:
                    fragments[i] = new Fragment_Explore();
                    break;
                case 3:
                    fragments[i] = new fragment_setting();
                    break;
                default:
                    Log.e(TAG, "Current Fragment index is not support!");
                    throw new IllegalArgumentException();
            }
            tx.add(R.id.fgm, fragments[i]);
        }

        //add animation
        if (i > whichanim) {
            tx.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if (i < whichanim) {
            tx.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        }

        //hide other fragment and show current fragment
        for (int j = 0; j < 4; j++) {
            if (i == j) {
                tx.show(fragments[j]);
            } else {
                if (fragments[j] != null)
                    tx.hide(fragments[j]);
            }
        }

        whichanim = i;
        tx.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        setContentView(R.layout.activity_main);

        container = findViewById(R.id.MainContainer);
        container.setVisibility(View.GONE);

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //show welcome
        FragmentTransaction ft0 = getSupportFragmentManager().beginTransaction();
        ft0.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        ft0.replace(R.id.container, start_anim).addToBackStack(null);
        ft0.commit();


        //checkPermission
        getPermissions();

        tinyDB = new TinyDB(getBaseContext());

        //init fragments
        fragments[0] = new Fragment_Control();

        runnable = () -> {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(start_anim);
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    ft.add(R.id.fgm, fragment);
                }
            }
            ft.commitAllowingStateLoss();
            Log.i(TAG, "fragment created");
            container.setVisibility(View.VISIBLE);
        };

        handler.postDelayed(runnable, 3000);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        if (tinyDB.getString("LocalIp").equals("")) {

            Toast.makeText(this, "Please Pair First", Toast.LENGTH_SHORT).show();

        }

        Session.setShake(BitmapFactory.decodeResource(getResources(), R.drawable.shake));

    }


//    private class CheckLightState extends Thread{
//
//        Context context;
//
//        CheckLightState(Context context){
//            this.context = context;
//        }
//
//        @Override
//        public void run(){
//            if(tinyDB.getString("LocalIP").equals("")){
//
//                Toast.makeText(context, "Please Pair First", Toast.LENGTH_SHORT).show();
//
//            }else {
//
//                ipAddr = tinyDB.getString("LocalIp");
//
//                try {
//                    URL url = new URL("http://"+ipAddr+"/ip");
//                    Log.d(TAG, "onCreate: URLLLL"+ url);
//                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                    Log.d(TAG, "onCreate: Open Connected");
////                urlConnection.connect();
//                    Log.d(TAG, "onCreate: Connected?");
//                    try {
//                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//                    }catch (Exception e){
//                        Log.d(TAG, "onCreate: input error");
//                        Toast.makeText(context, "Device not connected", Toast.LENGTH_SHORT).show();
//                    } finally {
//                        Log.d(TAG, "onCreate: finally");
//                        urlConnection.disconnect();
//                    }
//                }catch (Exception e){
//                    Toast.makeText(context, "Device not connected", Toast.LENGTH_SHORT).show();
//                    Log.d(TAG, "onCreate: url error");
//                }
//
//
//            }


//        }


//    }
//    @Override
//    public void mainControl(String Tag, int Value){
//        String tempCommand;
//
//    }
//
//    @Override
//    public int rounterIpAddress(int address){
//        return address;
//    }

    //    @Override
//    public void onAttachFragment(Fragment fragment) {
//        if (fragment instanceof fragment_Pair) {
//            fragment_Pair fragment_pair = (fragment_Pair) fragment;
//            fragment_pair.setListener(this);
//        }
//    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
        super.onSaveInstanceState(outState);
    }

//    https://blog.csdn.net/a10615/article/details/52427047


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                Session.getInstance().setPermissionFlag(false);
            }
        }
    }

    public void getPermissions() {
        final String[] permissions = new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
        };

        for (int i = 0; i < permissions.length; i++) {
            int hasPermission = checkSelfPermission(permissions[i]);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{permissions[i]}, i);
            }
        }
    }

    public void goMain() {
        Log.i("goMain", "try to go control fragment");
        navView.setSelectedItemId(R.id.navigation_home);
    }
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







