package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class useless_adapter extends ArrayAdapter<theme_content_class> {
    private Context context;
    private List<theme_content_class> contents;

    public useless_adapter(@NonNull Context context,@LayoutRes int resource,
                           @NonNull List<theme_content_class> contents) {
        super(context, resource,contents);
        this.context = context;
        this.contents = contents;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view= inflater.inflate(R.layout.theme_useless_items,null);
        theme_content_class item=contents.get(position);
        TextView subtitle = (TextView)view.findViewById(R.id.subtitle);
        subtitle.setText(item.getSubtitle());
        TextView content = (TextView)view.findViewById(R.id.content);
        content.setText(item.getContent());
        return view;
    }
}
