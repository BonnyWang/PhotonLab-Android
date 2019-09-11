package xyz.photonlab.photonlabandroid;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.SmartHomeItem;
import xyz.photonlab.photonlabandroid.model.Theme;

public class FragmentSmartHome extends NormalStatusBarFragment {

    private RecyclerView recyclerView;
    private ArrayList<SmartHomeItem> data;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragment_smart_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton exit = view.findViewById(R.id.backButton_Coming);
        ImageButton help = view.findViewById(R.id.button3);
        if (Session.getInstance().isDarkMode(getContext())) {
            view.setBackgroundColor(Theme.Dark.MAIN_BACKGROUND);
            ((TextView) view.findViewById(R.id.ComingSoon)).setTextColor(Theme.Dark.SELECTED_TEXT);
        }
        help.setOnClickListener(v -> {
            Activity activity = getActivity();
            if (activity != null) {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://photonlab.xyz/help.html")));
            }
        });

        recyclerView = view.findViewById(R.id.home_rView);
        exit.setOnClickListener(v -> Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack());
        initData();
        initRecyclerView();
        if (Session.getInstance().isDarkMode(getContext())) {
            exit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EA7D38")));
            help.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EA7D38")));
            ((CardView)view.findViewById(R.id.plus_Theme_Card)).setCardBackgroundColor(Color.parseColor("#505154"));
        }
    }

    private void initData() {
        data = new ArrayList<>();
        data.add(new SmartHomeItem("Google Home", Color.parseColor("#4382F0"),
                "https://support.google.com/googlenest/topic/7029677?hl=en&ref_topic=7029097"));
        data.add(new SmartHomeItem("Amazon Alexa", Color.parseColor("#14BAF8"),
                "https://www.amazon.com/gp/help/customer/display.html?nodeId=G201549510"));
        data.add(new SmartHomeItem("IFTTT", Color.parseColor("#000000"),
                "https://help.ifttt.com/hc/en-us/articles/115010158167-How-does-IFTTT-work-"));
        data.add(new SmartHomeItem("ConnectYouTube", Color.parseColor("#F70000"),
                "https://photonlab.xyz/help.html"));

    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new MyAdapter(data, (v, position) -> {
            Log.i("item clicked", data.get(position).getTitle());
            fragment_explore_indiv bfgExplore = new fragment_explore_indiv(data.get(position).getLink(),
                    data.get(position).getTitle());
            FragmentTransaction exTx = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            exTx.setCustomAnimations(R.anim.pop_enter, FragmentTransaction.TRANSIT_NONE, FragmentTransaction.TRANSIT_NONE, R.anim.pop_out);
            exTx.add(R.id.container, bfgExplore).addToBackStack(null);
            exTx.commit();
        }));
    }
}

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    //view holder start
    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.smart_home_tv);
            cardView = itemView.findViewById(R.id.smart_home_rv);
        }
    }
    //view holder end

    //listener start
    interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
    //listener end

    private final ArrayList<SmartHomeItem> data;
    private final OnItemClickListener listener;

    MyAdapter(ArrayList<SmartHomeItem> data, @NonNull OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.smart_home_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv.setText(data.get(position).getTitle());
        holder.cardView.setCardBackgroundColor(data.get(position).getColor());
        holder.cardView.setOnClickListener(v -> this.listener.onItemClick(v, position));
        Log.i("holder", holder.toString());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}