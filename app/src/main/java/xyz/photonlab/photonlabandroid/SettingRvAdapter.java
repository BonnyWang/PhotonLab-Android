package xyz.photonlab.photonlabandroid;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SettingRvAdapter extends RecyclerView.Adapter<SettingRvAdapter.MyViewHolder> {
    private SettingRvAdapter.OnNoteListener mOnNoteListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView textView;
        ImageView titleIcon;

        SettingRvAdapter.OnNoteListener onNoteListener;

        public MyViewHolder(View v, SettingRvAdapter.OnNoteListener onNoteListener) {
            super(v);
            textView = itemView.findViewById(R.id.SettingSub);
            titleIcon = itemView.findViewById(R.id.title_icon);
            this.onNoteListener = onNoteListener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    List<setting_Content> mSettings;

    public SettingRvAdapter(List<setting_Content> mSettings, SettingRvAdapter.OnNoteListener onNoteListener) {
        this.mSettings = mSettings;
        this.mOnNoteListener = onNoteListener;
    }

    @Override
    public int getItemCount() {
        return mSettings.size();
    }

    @NonNull
    @Override
    public SettingRvAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.setting_item, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(v, mOnNoteListener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SettingRvAdapter.MyViewHolder holder, int i) {
        holder.textView.setText(mSettings.get(i).getSubtitle());
        holder.titleIcon.setImageResource(mSettings.get(i).getIconRes());
        if (i < 3) {
            holder.titleIcon.getDrawable().setTint(Color.parseColor("#64b5f6"));
        } else if (i < 7) {
            holder.titleIcon.getDrawable().setTint(Color.parseColor("#ce93d8"));
        } else {
            holder.titleIcon.getDrawable().setTint(Color.parseColor("#b0bec5"));
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}
