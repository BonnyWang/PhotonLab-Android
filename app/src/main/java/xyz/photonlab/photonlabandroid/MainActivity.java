package xyz.photonlab.photonlabandroid;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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

    private long timestamp;

    //Fragments
    Fragment[] fragments = new Fragment[4];

    Animation slideInLeft, slideOutLeft, slideInRight, slideOutRight;

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

    private void createOrReplaceFragment(int i) {
        if (i == whichanim)
            return;
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
            tx.add(R.id.fgm, fragments[i], i + "");

        }
        if (i > whichanim) {
            tx.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            tx.show(fragments[i]);
            tx.hide(fragments[whichanim]);
        }
        if (i < whichanim) {
            tx.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            tx.show(fragments[i]);
            tx.hide(fragments[whichanim]);
        }
        tx.commit();
        whichanim = i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: YES");
        overridePendingTransition(0, 0);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(R.layout.activity_main);
        container = findViewById(R.id.MainContainer);
        container.setVisibility(View.GONE);
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        slideOutLeft = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        slideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        slideOutRight = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
        FragmentManager manager = getSupportFragmentManager();

        //checkPermission
        getPermissions();
        tinyDB = new TinyDB(getBaseContext());

        if (savedInstanceState != null) {
            whichanim = savedInstanceState.getInt("currentFragment");
            for (int i = 0; i < 4; i++) {
                fragments[i] = manager.getFragment(savedInstanceState, i + "");
            }
        }
        FragmentTransaction ft = manager.beginTransaction();
        //init fragments
        if (fragments[0] == null) {
            fragments[0] = new FragmentControlV2();
        }
        if (!fragments[0].isAdded())
            ft.add(R.id.fgm, fragments[0], 0 + "");
        Log.i(TAG, "fragment created");
        container.setVisibility(View.VISIBLE);
        ft.commitAllowingStateLoss();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        if (Session.getInstance().getLocalIP(this).equals("")) {
            Toast.makeText(this, "Please Pair First", Toast.LENGTH_SHORT).show();
        }

        Session.setShake(BitmapFactory.decodeResource(getResources(), R.drawable.shake));

        Session.getInstance().addOnThemeChangeListener(this);

        initTheme(Session.getInstance().isDarkMode(this));

        this.timestamp = 0;

    }

    public void initTheme(boolean isDark) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Class<? extends Theme.ThemeColors> colors;
        if (isDark) {
            colors = Theme.Dark.class;
            navView.setItemIconTintList(getResources().getColorStateList(R.color.bottom_nav_selector_dark, null));
            int s = getWindow().getDecorView().getSystemUiVisibility();
            getWindow().getDecorView().setSystemUiVisibility(s & (~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR));
            getWindow().setNavigationBarColor(0xff1f1f1f);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            colors = Theme.Normal.class;
            navView.setItemIconTintList(getResources().getColorStateList(R.color.bottom_nav_selector, null));
            getWindow().setNavigationBarColor(0xffebebeb);
        }
        try {
            getWindow().setStatusBarColor(colors.getField("MAIN_BACKGROUND").getInt(null));
            getWindow().getDecorView().setBackgroundColor(colors.getField("MAIN_BACKGROUND").getInt(null));
            Log.i(TAG, getWindow().getNavigationBarColor() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        if (Session.getInstance().isDarkMode(this)) {
            getWindow().setStatusBarColor(Theme.Dark.MAIN_BACKGROUND);
        } else {
            getWindow().setStatusBarColor(Theme.Normal.MAIN_BACKGROUND);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_scale_in, R.anim.fade_scale_out);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.w("MainActivity", "onSaveInstanceState: ");
        for (int i = 0; i < 4; i++) {
            if (fragments[i] != null) {
                getSupportFragmentManager().putFragment(outState, i + "", fragments[i]);
                if (!fragments[i].isHidden()) {
                    outState.putInt("currentFragment", i);
                }
            }
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 0) {
            long now = System.currentTimeMillis();
            if (now - timestamp > 3000) {
                Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
                timestamp = now;
            } else
                this.finish();
        } else {
            super.onBackPressed();
        }
    }

}







