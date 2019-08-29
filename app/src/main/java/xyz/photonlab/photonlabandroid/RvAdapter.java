package xyz.photonlab.photonlabandroid;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import xyz.photonlab.photonlabandroid.model.MyTheme;
import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;

import java.util.List;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.MyViewHolder> {

    private OnNoteListener mOnNoteListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        TextView textView;
        ImageView imageView_Card;
        Button add_Theme;

        OnNoteListener onNoteListener;

        public MyViewHolder(View v, OnNoteListener onNoteListener) {
            super(v);
            cv = (CardView) itemView.findViewById(R.id.cv);
            textView = (TextView) itemView.findViewById(R.id.add_Theme);
            imageView_Card = (ImageView) itemView.findViewById(R.id.imageView_Card);
//            add_Theme=(Button)itemView.findViewById(R.id.plus_Theme);


            this.onNoteListener = onNoteListener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    List<MyTheme> mthemes;

    public RvAdapter(List<MyTheme> mthemes, OnNoteListener onNoteListener) {
        this.mthemes = mthemes;
        this.mOnNoteListener = onNoteListener;
    }

    @Override
    public int getItemCount() {
        return mthemes.size() + 1;
    }

    @NonNull
    @Override
    public RvAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v;
        if (viewType == R.layout.cardview) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview, viewGroup, false);
            if (Session.getInstance().isDarkMode(v.getContext())) {
                ((CardView) v.findViewById(R.id.cv)).setCardBackgroundColor(Theme.Dark.CARD_BACKGROUND);
                ((TextView) v.findViewById(R.id.add_Theme)).setTextColor(Theme.Dark.SELECTED_TEXT);
                ((TextView) v.findViewById(R.id.textView3)).setTextColor(Theme.Dark.UNSELECTED_TEXT);
            }
        } else {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.button_theme, viewGroup, false);
            if (Session.getInstance().isDarkMode(v.getContext())) {
                ((CardView) v.findViewById(R.id.plus_Theme_Card)).setCardBackgroundColor(Color.parseColor("#505154"));
            }
        }
        return new MyViewHolder(v, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int i) {
        if (i == mthemes.size()) {
//            holder.add_Theme.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Log.d("yes", "onClick: yes");
//                }
//            });
        } else {
            holder.textView.setText(mthemes.get(i).getName());
            holder.imageView_Card.setImageDrawable(mthemes.get(i).getGradientDrawable());
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mthemes.size()) ? R.layout.button_theme : R.layout.cardview;
    }
}
