package xyz.photonlab.photonlabandroid;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SimpleCheckableAdapter extends BaseAdapter {

    private final String[] data;
    private int checked;

    public SimpleCheckableAdapter(String[] data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView != null ? convertView :
                LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(data[position]);
        if (position == checked)
            textView.setTextColor(Color.BLACK);
        else
            textView.setTextColor(Color.parseColor("#c6c6c6"));
        return view;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }
}
