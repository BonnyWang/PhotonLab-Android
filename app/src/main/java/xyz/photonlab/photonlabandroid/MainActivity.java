package xyz.photonlab.photonlabandroid;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import xyz.photonlab.photonlabandroid.model.Theme;

//added for Fragment -Bonny

public class MainActivity extends AppCompatActivity implements Session.OnThemeChangeListener {
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

        Session.getInstance().addOnThemeChangeListener(this);
        initTheme(Session.getInstance().isDarkMode(this));
    }

    public void initTheme(boolean isDark) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Class<? extends Theme.ThemeColors> colors;
        if (isDark) {
            colors = Theme.Dark.class;
            navView.setItemIconTintList(getResources().getColorStateList(R.color.bottom_nav_selector_dark, null));
        } else {
            colors = Theme.Normal.class;

            navView.setItemIconTintList(getResources().getColorStateList(R.color.bottom_nav_selector, null));
        }
        try {
            getWindow().setStatusBarColor(colors.getField("MAIN_BACKGROUND").getInt(null));
            getWindow().getDecorView().setBackgroundColor(colors.getField("MAIN_BACKGROUND").getInt(null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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







