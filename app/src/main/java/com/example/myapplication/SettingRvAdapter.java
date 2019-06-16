package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SettingRvAdapter extends RecyclerView.Adapter<SettingRvAdapter.MyViewHolder> {
    private SettingRvAdapter.OnNoteListener mOnNoteListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        TextView textView;


        SettingRvAdapter.OnNoteListener onNoteListener;

        public MyViewHolder(View v, SettingRvAdapter.OnNoteListener onNoteListener) {
            super(v);
            textView = (TextView) itemView.findViewById(R.id.SettingSub);
            this.onNoteListener = onNoteListener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    List<setting_Content> mSettings;

    public SettingRvAdapter(List<setting_Content> mSettings, SettingRvAdapter.OnNoteListener onNoteListener){
        this.mSettings = mSettings;
        this.mOnNoteListener = onNoteListener;
    }

    @Override
    public int getItemCount() {
        return mSettings.size();
    }

    @Override
    public SettingRvAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.setting_item, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(v , mOnNoteListener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(SettingRvAdapter.MyViewHolder holder, int i) {
        holder.textView.setText(mSettings.get(i).getSubtitle());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
