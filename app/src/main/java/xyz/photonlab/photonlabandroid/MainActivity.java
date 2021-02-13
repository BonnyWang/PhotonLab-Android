package xyz.photonlab.photonlabandroid;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//added for Fragment -Bonny

public class MainActivity extends AppCompatActivity  {
    //implements fragment_Pair.pairing_Listener
    private final String TAG = "Mainactivity";

    int whichanim = 0;
    int rounterIP;


    //Fragments
    Fragment_Theme fragment_Theme = Fragment_Theme.getInstance();
    static final Fragment fragment_Control = new Fragment_Control();
    static final Fragment fragment_Setting = new fragment_setting();
    static final Fragment fragment_Explore = new Fragment_Explore();
    static final Fragment start_anim = new fragment_start_anim();

    WebView webViewMain;

    static Handler handler = new Handler();
    static Runnable runnable;

    ConstraintLayout container;

    TinyDB tinyDB;
    String ipAddr;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                    ft1.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
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
                        ft2.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
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
                        ft3.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                    whichanim = 3;
                    ft3.replace(R.id.fgm, fragment_Explore);
                    ft3.commit();
                    return true;

                case R.id.navigation_notifications2:
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

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        setContentView(R.layout.activity_main);

        container = findViewById(R.id.MainContainer);
        container.setVisibility(View.GONE);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        webViewMain = findViewById(R.id.webViewMain);

        tinyDB = new TinyDB(getBaseContext());



        FragmentTransaction ft0 = getSupportFragmentManager().beginTransaction();
        ft0.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        ft0.replace(R.id.container, start_anim).addToBackStack(null);
        ft0.commit();



        runnable = new Runnable() {
            @Override
            public void run() {
                FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
                ft.remove(start_anim);
                ft.commitAllowingStateLoss();
                container.setVisibility(View.VISIBLE);
            }
        };

        handler.postDelayed(runnable, 3000);

        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.fgm, fragment_Control);
        ft1.commit();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        if(tinyDB.getString("LocalIp").equals("")){

            Toast.makeText(this, "Please Pair First", Toast.LENGTH_SHORT).show();

        }else {

        }


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }


}







