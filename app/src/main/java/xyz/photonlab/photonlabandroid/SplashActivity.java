package xyz.photonlab.photonlabandroid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_scale_in, R.anim.fade_scale_out);
        FrameLayout layout = new FrameLayout(this);
        layout.setId(R.id.main_container);
        setContentView(layout);
        FragmentTransaction ft0 = getSupportFragmentManager().beginTransaction();
        ft0.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        ft0.replace(R.id.main_container, new fragment_start_anim());
        ft0.commit();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (Session.getInstance().isDarkMode(this)) {
            getWindow().setStatusBarColor(Theme.Dark.MAIN_BACKGROUND);
            int s = getWindow().getDecorView().getSystemUiVisibility();
            getWindow().getDecorView().setSystemUiVisibility(s & (~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR));
            getWindow().setNavigationBarColor(0xff1f1f1f);
        } else {
            getWindow().setStatusBarColor(Theme.Normal.MAIN_BACKGROUND);
            getWindow().setNavigationBarColor(0xffebebeb);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, MainActivity.class));
            this.finish();
        }, 2000);
    }
}
