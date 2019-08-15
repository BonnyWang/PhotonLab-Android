package xyz.photonlab.photonlabandroid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import xyz.photonlab.photonlabandroid.model.explore_item_Class;


public class Fragment_Explore extends Fragment implements explore_RvAdapter.OnNoteListener {

    Context context;
    ArrayList<explore_item_Class> bexplores;

    Spinner spinnerMenu;
    RecyclerView rv;
    explore_RvAdapter adapter;

    explore_RvAdapter.OnNoteListener mOnNoteListner = this;

    private static final String TAG = "Fragment_Explore";


    @SuppressLint("StaticFieldLeak")
    private static Fragment_Explore single_instance = null;

    Fragment_Explore thisone = this;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static Fragment_Explore getInstance() {
        if (single_instance == null)
            single_instance = new Fragment_Explore();

        return single_instance;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e("explore", "creating view");
        context = getContext();
        View view = inflater.inflate(R.layout.fragment__explore_layout, container, false);


        rv = view.findViewById(R.id.rvExplore);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);
        rv.setNestedScrollingEnabled(false);
        spinnerMenu = view.findViewById(R.id.spinnerExplore);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(context,
                R.array.Explore_Menu, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMenu.setAdapter(spinnerAdapter);
        initializeData();

        explore_RvAdapter adapter = new explore_RvAdapter(bexplores, this, thisone, false);

        rv.setAdapter(adapter);

        spinnerMenu.setSelection(0);

        spinnerMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    explore_RvAdapter adapter0 = new explore_RvAdapter(bexplores, mOnNoteListner, thisone, true);
                    rv.setAdapter(adapter0);
                } else if (position == 1) {//Creative
                    ArrayList<explore_item_Class> creativeList = new ArrayList<>();
                    for (explore_item_Class item : bexplores) {
                        if (item.isCreative())
                            creativeList.add(item);
                    }
                    explore_RvAdapter adapter0 = new explore_RvAdapter(creativeList, mOnNoteListner, thisone, true);
                    rv.setAdapter(adapter0);
                } else if (position == 2) {
                    ArrayList<explore_item_Class> tutorialList = new ArrayList<>();
                    for (explore_item_Class item : bexplores) {
                        if (item.isTutorial())
                            tutorialList.add(item);
                    }
                    explore_RvAdapter adapter0 = new explore_RvAdapter(tutorialList, mOnNoteListner, thisone, true);
                    rv.setAdapter(adapter0);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;

    }

    private void initializeData() {
        bexplores = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("mexplore");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                GenericTypeIndicator<ArrayList<Map<String, String>>> t = new GenericTypeIndicator<ArrayList<Map<String, String>>>() {
                };
                bexplores = new ArrayList<>();
                ArrayList<Map<String, String>> src = dataSnapshot.getValue(t);

                if (src != null)
                    for (Map<String, String> map : src) {
                        explore_item_Class item = new explore_item_Class();
                        bexplores.add(item);
                        item.setImageLink(map.get("imageLink"));
                        item.setLink(map.get("link"));
                        item.setTitle(map.get("title"));
                        String category = map.get("category");
                        if (category != null && category.equals("Creative")) {
                            item.addCategory(explore_item_Class.CREATIVE);
                        }
                        if (category != null && category.equals("Tutorial")) {
                            item.addCategory(explore_item_Class.TUTORIAL);
                        }
                    }
                adapter = new explore_RvAdapter(bexplores, mOnNoteListner, thisone, true);
                rv.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
                Toast.makeText(getContext(), "Failed to access server", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onNoteClick(int position) {
        fragment_explore_indiv bfgExplore = new fragment_explore_indiv(bexplores.get(position).getLink(),
                bexplores.get(position).getTitle());
        FragmentTransaction ftindiv = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        ftindiv.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        ftindiv.replace(R.id.container, bfgExplore).addToBackStack(null);
        ftindiv.commit();
    }


}






