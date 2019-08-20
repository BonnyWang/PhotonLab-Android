package xyz.photonlab.photonlabandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;

import xyz.photonlab.photonlabandroid.model.Session;

public class ThemeDetailActivity extends AppCompatActivity implements fragement_theme_individual.themeIndivListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_theme_detail);
        int i = getIntent().getIntExtra("current", 0);
        theme_Class theme_class = Session.getInstance().getMtheme().get(i);
        fragement_theme_individual theme_Individual = new fragement_theme_individual(theme_class, Session.getInstance().getMfavoriteTheme().contains(theme_class));
        theme_Individual.setListener(this);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_container, theme_Individual);
        ft.commit();
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
}
