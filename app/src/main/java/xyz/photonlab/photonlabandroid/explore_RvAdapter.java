package xyz.photonlab.photonlabandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import xyz.photonlab.photonlabandroid.model.explore_item_Class;

public class explore_RvAdapter extends RecyclerView.Adapter<explore_RvAdapter.MyViewHolder> {

    OnNoteListener bonNoteListener;
    Fragment_Explore fragment_explore;
    boolean loaded;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView bimageView;
        TextView title, desc;

        OnNoteListener bonNoteListener;

        public MyViewHolder(View v, explore_RvAdapter.OnNoteListener bonNoteListener) {
            super(v);
            bimageView = itemView.findViewById(R.id.ivExplore);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.tv_desc);
            this.bonNoteListener = bonNoteListener;
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
    @NonNull
    @Override
    public explore_RvAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new MyViewHolder(v, bonNoteListener);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (position != bexplores.size()) {
            ImageView imageView = holder.bimageView;
            Glide.with(fragment_explore).load(bexplores.get(position).getImageLink()).centerCrop().into(imageView);
        }

        explore_item_Class item = bexplores.get(position);

        if (item.getDescription() != null) {
            holder.title.setText(item.getTitle());
            holder.desc.setText(item.getDescription());
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (loaded) {
            return bexplores.size();
        }
        return bexplores.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (bexplores.get(position).getDescription() != null)
            return R.layout.explore_item_with_desc;
        if (position == bexplores.size())
            return R.layout.progressbar;

        return R.layout.cardview_explore;
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }

}
