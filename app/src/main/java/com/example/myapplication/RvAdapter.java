package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.MyViewHolder> {

    private OnNoteListener mOnNoteListener;
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        TextView textView;
        ImageView imageView_Card;

        OnNoteListener onNoteListener;
        public MyViewHolder(@NonNull  View v, OnNoteListener onNoteListener) {
            super(v);
            cv = (CardView)itemView.findViewById(R.id.cv);
            textView = (TextView) itemView.findViewById(R.id.textView);
            imageView_Card = (ImageView)itemView.findViewById(R.id.imageView_Card);
            this.onNoteListener = onNoteListener;

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    List<theme_Class> mthemes;

    public RvAdapter(List<theme_Class> mthemes,OnNoteListener onNoteListener){
        this.mthemes = mthemes;
        this.mOnNoteListener=onNoteListener;
    }

    @Override
    public int getItemCount() {
        return mthemes.size();
    }

    @Override
    public RvAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(v,mOnNoteListener);
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

public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
