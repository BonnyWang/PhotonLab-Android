package com.example.myapplication;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class wifiRvAdapter extends RecyclerView.Adapter<wifiRvAdapter.MyViewHolder> {

    private  wifiRvAdapter.OnNoteListener mOnNoteListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        TextView textView;

        wifiRvAdapter.OnNoteListener onNoteListener;

        public MyViewHolder(View v, wifiRvAdapter.OnNoteListener onNoteListener) {
            super(v);
            textView = (TextView) itemView.findViewById(R.id.wifiName);
            this.onNoteListener = onNoteListener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    List<ScanResult> mWifis;

    public wifiRvAdapter(List<ScanResult> mWifis, wifiRvAdapter.OnNoteListener onNoteListener){
        this.mWifis = mWifis;
        this.mOnNoteListener = onNoteListener;
    }


    @Override
    public wifiRvAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wifi_item, viewGroup, false);
        wifiRvAdapter.MyViewHolder myViewHolder = new wifiRvAdapter.MyViewHolder(v , mOnNoteListener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(wifiRvAdapter.MyViewHolder holder, int i) {
        holder.textView.setText(mWifis.get(i).SSID);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }

    @Override
    public int getItemCount() {
        return mWifis.size();
    }

}
