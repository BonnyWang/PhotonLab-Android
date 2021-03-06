package xyz.photonlab.photonlabandroid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import xyz.photonlab.photonlabandroid.model.Session;

public class SelectMotionThemeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView list;
    List<theme_Class> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_select_motion_theme);
        list = findViewById(R.id.list);

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
        textView.setTextColor(Color.BLACK);
        Session.getInstance().setCurrentThemeIndex(this, i);
        finish();
    }

    private void clearChecked() {
        for (int i = 0; i < data.size(); i++) {
            TextView view = list.getChildAt(i).findViewById(R.id.theme_item);
            view.setTextColor(getResources().getColor(R.color.light_gray, getTheme()));
        }
    }
}

class ThemeAdapter extends BaseAdapter {

    private Activity c;
    private List<theme_Class> data;

    ThemeAdapter(Activity c, List<theme_Class> data) {
        this.c = c;
        this.data = data;
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
            textView.setTextColor(Color.BLACK);
        }
        return view;
    }
}
