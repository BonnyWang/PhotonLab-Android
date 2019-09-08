package xyz.photonlab.photonlabandroid;

import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import xyz.photonlab.photonlabandroid.model.MyTheme;
import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;

public class dlRvAdapter extends RecyclerView.Adapter<dlRvAdapter.MyViewHolder> {

    private final boolean isDark;
    List<MyTheme> mdlthemes;
    dlListener mlistener;
    ArrayList<MyTheme> mtheme;

    boolean btPress;

    private static final String TAG = "dlRvAdapter";

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final boolean isDark;
        CardView cv;
        TextView textView;
        ImageView imageView_Card;
        ToggleButton btDownload;
        dlListener mlistener;

        public MyViewHolder(View v, dlListener listener, int bottom, boolean isDark) {
            super(v);
            this.isDark = isDark;
            if (bottom == 0) {
                cv = itemView.findViewById(R.id.dlcv);
                textView = itemView.findViewById(R.id.dlThemeName);
                imageView_Card = itemView.findViewById(R.id.dlimageView_Card);
                btDownload = itemView.findViewById(R.id.btDownload);

                this.mlistener = listener;

                final Resources res = v.getContext().getResources();

                if (isDark) {
                    cv.setCardBackgroundColor(Theme.Dark.CARD_BACKGROUND);
                    itemView.findViewById(R.id.dl_top).setBackgroundColor(Theme.Dark.CARD_BACKGROUND);
                    textView.setTextColor(Theme.Dark.SELECTED_TEXT);
                }

                btDownload.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        mlistener.dlPosition(getAdapterPosition());


                        ScaleAnimation animate = new ScaleAnimation(0.5f, 1, 0.5f, 1,
                                btDownload.getWidth() * 0.5f, btDownload.getHeight() * 0.5f);
                        animate.setDuration(300);
                        btDownload.startAnimation(animate);
                        btDownload.setBackground(res.getDrawable(R.drawable.check, null));
                        //only be pressed once
                        btDownload.setClickable(false);
                    } else {

                    }
                });

                btDownload.setOnClickListener(v1 -> {
                    if (!btDownload.isPressed()) {

                    }
                });
            }

        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public dlRvAdapter(List<MyTheme> mdlthemes, dlListener mlistener, ArrayList<MyTheme> mtheme, boolean isDark) {
        this.mdlthemes = mdlthemes;
        this.mlistener = mlistener;
        this.mtheme = new ArrayList<>();
        this.mtheme = mtheme;
        this.isDark = isDark;
        for (int i = 0; i < mtheme.size(); i++) {
            Log.d(TAG, "dlRvAdapter: " + mtheme.get(i).getName());
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public dlRvAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v;
        if (viewType == R.layout.dlcardview) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dlcardview, parent, false);
            MyViewHolder vh = new MyViewHolder(v, mlistener, 0, isDark);
            return vh;
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar, parent, false);
            MyViewHolder vh = new MyViewHolder(v, mlistener, 1, isDark);
            return vh;
        }


    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if (position == mdlthemes.size()) {

        } else {
            Resources res = holder.itemView.getContext().getResources();
            holder.textView.setText(mdlthemes.get(position).getName());
            holder.imageView_Card.setImageDrawable(mdlthemes.get(position).getGradientDrawable());
            Log.d(TAG, "onBindViewHolder: This run?");

            holder.btDownload.setChecked(false);

            MyTheme temp = mdlthemes.get(position);

            for (int i = 0; i < mtheme.size(); i++) {
                if (mtheme.get(i).getName().equals(temp.getName())) {

                    Log.d(TAG, "onBindViewHolder: YYYY");
                    //holder.btDownload.setChecked(true);

                    holder.btDownload.setBackground(res.getDrawable(R.drawable.check, null));
                    //only be pressed once
                    holder.btDownload.setClickable(false);
                }
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mdlthemes.size() + 1;
    }

//    public void setListener(dlListener mlistener){
//        this.mlistener = mlistener;
//    }

    @Override
    public int getItemViewType(int position) {
        return (position == mdlthemes.size()) ? R.layout.progressbar : R.layout.dlcardview;
    }

    interface dlListener {
        int dlPosition(int position);
    }


}