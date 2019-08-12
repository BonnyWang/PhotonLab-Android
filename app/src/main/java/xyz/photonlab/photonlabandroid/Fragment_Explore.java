package xyz.photonlab.photonlabandroid;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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


public class Fragment_Explore extends Fragment implements explore_RvAdapter.OnNoteListener {

    Context context;
    ArrayList<explore_item_Class> bexplores;

    Spinner spinnerMenu;
    RecyclerView rv;
    explore_RvAdapter adapter;

    explore_RvAdapter.OnNoteListener mOnNoteListner = this;

    private static final String TAG = "Fragment_Explore";


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = getContext();
        View view = inflater.inflate(R.layout.fragment__explore_layout, container, false);


        rv = (RecyclerView) view.findViewById(R.id.rvExplore);
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
                    explore_RvAdapter adapter0 = new explore_RvAdapter(bexplores, mOnNoteListner, thisone, false);
                    rv.setAdapter(adapter0);
                }
                //TODO add position 1-creative and 2-tutorial
//                if(position == 1) {
//                    RvAdapter adapter1 = new RvAdapter(mfavoriteMusic, mOnNoteListner);
//                    rv.setAdapter(adapter1);
//                }

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
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                GenericTypeIndicator<ArrayList<explore_item_Class>> t = new GenericTypeIndicator<ArrayList<explore_item_Class>>() {
                };
                bexplores = dataSnapshot.getValue(t);
                adapter = new explore_RvAdapter(bexplores, mOnNoteListner, thisone, true);
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
                Toast.makeText(getContext(), "Failed to access server", Toast.LENGTH_SHORT).show();
            }
        });

//        mfavoriteMusic = new ArrayList<>();
//        favOrder = new ArrayList<>();
//        TinyDB tinyDB = new TinyDB(this.getContext());
//        if(tinyDB.getListInt("favOrderMusic").size() != 0){
//            favOrder = tinyDB.getListInt("favOrderMusic");
//            for(int i = 0; i < favOrder.size(); i++){
//                mfavoriteMusic.add(mMusic.get(favOrder.get(i)));
//            }
//        }

    }

    @Override
    public void onNoteClick(int position) {


        if (spinnerMenu.getSelectedItemPosition() == 0) {
            //TODO:

            fragment_explore_indiv bfgExplore = new fragment_explore_indiv(bexplores.get(position).getLink(),
                    bexplores.get(position).getTitle());
            FragmentTransaction ftindiv = getActivity().getSupportFragmentManager().beginTransaction();
            ftindiv.setCustomAnimations(R.anim.pop_enter, R.anim.pop_out, R.anim.pop_enter, R.anim.pop_out);
            ftindiv.replace(R.id.container, bfgExplore).addToBackStack(null);
            ftindiv.commit();

        } else {


        }
    }


}






