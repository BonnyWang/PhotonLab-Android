package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.MyViewHolder> {

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView textView;
        ImageView imageView_Card;


        public MyViewHolder(View v) {
            super(v);
            cv = (CardView)itemView.findViewById(R.id.cv);
            textView = (TextView) itemView.findViewById(R.id.textView);
            imageView_Card = (ImageView)itemView.findViewById(R.id.imageView_Card);
        }
    }

    List<theme_Class> mthemes;

    public RvAdapter(List<theme_Class> mthemes){
        this.mthemes = mthemes;
    }

    @Override
    public int getItemCount() {
        return mthemes.size();
    }

    @Override
    public RvAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int i) {
        holder.textView.setText(mthemes.get(i).name);
        holder.imageView_Card.setImageDrawable(mthemes.get(i).gradientDrawable);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
