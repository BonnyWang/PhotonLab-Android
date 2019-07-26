package xyz.photonlab.photonlabandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class explore_RvAdapter extends RecyclerView.Adapter<explore_RvAdapter.MyViewHolder> {

    OnNoteListener bonNoteListener;
    Fragment_Explore fragment_explore;
    boolean loaded;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView bimageView;

        OnNoteListener bonNoteListener;

        public MyViewHolder(View v, explore_RvAdapter.OnNoteListener bonNoteListener) {
            super(v);
            bimageView = itemView.findViewById(R.id.ivExplore);
            this.bonNoteListener = bonNoteListener;

//            bimageView.setOnClickListener(this);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            bonNoteListener.onNoteClick(getAdapterPosition());
        }

    }

    ArrayList<explore_item_Class> bexplores;

    public explore_RvAdapter(ArrayList<explore_item_Class> bexplores,
                             OnNoteListener bonNoteListener, Fragment_Explore fragment_explore, boolean loaded) {

        this.bexplores = bexplores;
        this.bonNoteListener = bonNoteListener;
        this.fragment_explore = fragment_explore;

        this.loaded = loaded;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public explore_RvAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View v;

        if(viewType == R.layout.cardview_explore){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_explore, parent,false);
        }else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar, parent,false);
        }

        MyViewHolder myViewHolder = new MyViewHolder(v , bonNoteListener);
        return myViewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(position == bexplores.size()){
            ;
        }else {
            ImageView imageView =  holder.bimageView;
            Glide.with(fragment_explore).load(bexplores.get(position).getImageLink()).centerCrop().into(imageView);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (loaded){
            return bexplores.size();
        }else{
            return bexplores.size()+1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == bexplores.size()) ? R.layout.progressbar : R.layout.cardview_explore;
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }

}
