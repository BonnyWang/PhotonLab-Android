package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class theme_Content_Adapter extends ArrayAdapter<theme_Content_Class> {
    private Context context = getContext();
    private List<theme_Content_Class> contents;

    public theme_Content_Adapter(@NonNull Context context, int resource, List<theme_Content_Class> contents) {
        super(context, resource,contents);
        this.context = context;
        this.contents = contents;
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull  View convertView,@NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.theme_info_item, parent, false);
        theme_Content_Class item = contents.get(position);
        TextView subtitle = (TextView)view.findViewById(R.id.subtitle);
        subtitle.setText(item.getSubtitle());
        TextView content = (TextView)view.findViewById(R.id.content);
        content.setText(item.getContent());
        return view;
    }
}

