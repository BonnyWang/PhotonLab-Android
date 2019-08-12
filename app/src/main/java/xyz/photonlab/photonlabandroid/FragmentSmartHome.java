package xyz.photonlab.photonlabandroid;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import xyz.photonlab.photonlabandroid.model.SmartHomeItem;

public class FragmentSmartHome extends Fragment {

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
        Button exit = view.findViewById(R.id.backButton_Coming);
        recyclerView = view.findViewById(R.id.home_rView);
        exit.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());
        initData();
        initRecyclerView();
    }

    private void initData() {
        data = new ArrayList<>();
        data.add(new SmartHomeItem("Google Home", Color.rgb(100, 100, 245)));
        data.add(new SmartHomeItem("Amazon Alexa", Color.rgb(100, 200, 245)));
        data.add(new SmartHomeItem("IFTTT", Color.rgb(30, 30, 30)));
        data.add(new SmartHomeItem("Google Home", Color.rgb(245, 100, 100)));
        data.add(new SmartHomeItem("Google Home", Color.rgb(100, 100, 245)));
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new MyAdapter(data, (v, position) -> {
            Log.i("item clicked", data.get(position).getTitle());
            fragment_explore_indiv bfgExplore = new fragment_explore_indiv("https://photonlab.xyz/",
                    data.get(position).getTitle());
            FragmentTransaction ftindiv = getActivity().getSupportFragmentManager().beginTransaction();
            ftindiv.replace(R.id.container, bfgExplore).addToBackStack(null);
            ftindiv.commit();
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
        holder.cardView.setOnClickListener(v -> {
            this.listener.onItemClick(v, position);
        });
        Log.i("holder", holder.toString());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}