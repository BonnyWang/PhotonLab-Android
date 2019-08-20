package xyz.photonlab.photonlabandroid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;

public class SelectMotionThemeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView list;
    List<theme_Class> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_motion_theme);
        list = findViewById(R.id.list);
        if (Session.getInstance().isDarkMode(this)) {
            getWindow().getDecorView().setBackgroundColor(Theme.Dark.MAIN_BACKGROUND);
            ((TextView) findViewById(R.id.title_tv)).setTextColor(Theme.Dark.SELECTED_TEXT);
            findViewById(R.id.button).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EA7D38")));
            getWindow().setStatusBarColor(Theme.Dark.MAIN_BACKGROUND);
            getWindow().setNavigationBarColor(Theme.Dark.MAIN_BACKGROUND);
        } else {
            getWindow().setStatusBarColor(Theme.Normal.MAIN_BACKGROUND);
            getWindow().setNavigationBarColor(Theme.Normal.MAIN_BACKGROUND);
        }
        Session.getInstance().requestTheme(this);
        System.out.println(Session.getInstance());
        data.addAll(Session.getInstance().getMtheme());

        list.setAdapter(new ThemeAdapter(this, data));

        list.setOnItemClickListener(this);

    }

    public void close(View view) {
        this.finish();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        clearChecked();
        TextView textView = view.findViewById(R.id.theme_item);
        textView.setTextColor(ThemeAdapter.colorSelected);
        Session.getInstance().setCurrentThemeIndex(this, i);
        Intent intent = new Intent();
        intent.putExtra("themeIndex", i);
        setResult(0, intent);
        finish();
    }

    private void clearChecked() {
        for (int i = 0; i < data.size(); i++) {
            TextView view = list.getChildAt(i).findViewById(R.id.theme_item);
            view.setTextColor(getResources().getColor(R.color.light_gray, getTheme()));
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}

class ThemeAdapter extends BaseAdapter {

    private Activity c;
    private List<theme_Class> data;

    static int colorSelected = Color.BLACK;
    static int colorUnselected = Color.parseColor("#999999");

    ThemeAdapter(Activity c, List<theme_Class> data) {
        this.c = c;
        this.data = data;
        if (Session.getInstance().isDarkMode(c)) {
            colorSelected = Theme.Dark.SELECTED_TEXT;
            colorUnselected = Theme.Dark.UNSELECTED_TEXT;
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = c.getLayoutInflater().inflate(R.layout.theme_items, null);
        TextView textView = view.findViewById(R.id.theme_item);
        textView.setText(data.get(i).getName());
        if (i == Session.getInstance().getCurrentThemeIndex(c)) {
            textView.setTextColor(colorSelected);
        } else {
            textView.setTextColor(colorUnselected);
        }
        return view;
    }

}
