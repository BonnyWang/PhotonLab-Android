package xyz.photonlab.photonlabandroid;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;
import xyz.photonlab.photonlabandroid.model.explore_item_Class;
import xyz.photonlab.photonlabandroid.utils.OnMultiClickListener;

public class explore_RvAdapter extends RecyclerView.Adapter<explore_RvAdapter.MyViewHolder> {

    OnNoteListener bonNoteListener;
    Fragment_Explore fragment_explore;
    boolean loaded;

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView bimageView;
        TextView title, desc;

        OnNoteListener bonNoteListener;

        public MyViewHolder(View v, explore_RvAdapter.OnNoteListener bonNoteListener) {
            super(v);
            bimageView = itemView.findViewById(R.id.ivExplore);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.tv_desc);
            if (Session.getInstance().isDarkMode(v.getContext())) {
                if (title != null)
                    title.setTextColor(Theme.Dark.SELECTED_TEXT);
                if (desc != null)
                    desc.setTextColor(Color.parseColor("#888888"));
                ((CardView) itemView.findViewById(R.id.cv)).setCardBackgroundColor(Theme.Dark.CARD_BACKGROUND);
            }
            this.bonNoteListener = bonNoteListener;
            v.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    bonNoteListener.onNoteClick(getAdapterPosition());
                }
            });
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
