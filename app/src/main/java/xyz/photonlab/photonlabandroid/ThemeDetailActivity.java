package xyz.photonlab.photonlabandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;

public class ThemeDetailActivity extends AppCompatActivity implements fragement_theme_individual.themeIndivListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);


        setContentView(R.layout.activity_theme_detail);
        int i = getIntent().getIntExtra("current", 0);
        theme_Class theme_class = Session.getInstance().getMtheme().get(i);
        fragement_theme_individual theme_Individual = new fragement_theme_individual(theme_class, Session.getInstance().getMfavoriteTheme().contains(theme_class));
        theme_Individual.setListener(this);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_container, theme_Individual);
        ft.commit();
        if (Session.getInstance().isDarkMode(this))
            getWindow().setNavigationBarColor(Theme.Dark.MAIN_BACKGROUND);
        else {
            getWindow().setNavigationBarColor(Theme.Normal.MAIN_BACKGROUND);
        }
    }

    @Override
    public theme_Class Addavorite(theme_Class current) {
        setResult(0);
        return null;
    }

    @Override
    public theme_Class RemoveFavorite(theme_Class currrent) {
        setResult(1);
        return null;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
