package xyz.photonlab.photonlabandroid;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class Fragment_Explore extends Fragment implements explore_RvAdapter.OnNoteListener {

    Context context;
    ArrayList<explore_item_Class> bexplores;

    Spinner spinnerMenu;
    RecyclerView rv;

    explore_RvAdapter.OnNoteListener mOnNoteListner = this;




    private static Fragment_Explore single_instance = null;

    Fragment_Explore thisone = this;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static Fragment_Explore getInstance()
    {
        if (single_instance == null)
            single_instance = new Fragment_Explore();

        return single_instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = getContext();
        View view = inflater.inflate(R.layout.fragment__explore_layout, container, false);


        rv = (RecyclerView)view.findViewById(R.id.rvExplore);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);

        spinnerMenu = view.findViewById(R.id.spinnerExplore);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(context,
                R.array.Explore_Menu, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMenu.setAdapter(spinnerAdapter);

        initializeData();

        explore_RvAdapter adapter = new explore_RvAdapter(bexplores, this, thisone);

        rv.setAdapter(adapter);

        spinnerMenu.setSelection(0);

        spinnerMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    explore_RvAdapter adapter0 = new explore_RvAdapter(bexplores,mOnNoteListner,thisone);
                    rv.setAdapter(adapter0);
                }
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

        bexplores.add(new explore_item_Class("https://drive.google.com/uc?export=download&id=1zvrsVpOss_5Qc9fn17pHk6yKAJ5RrJyK",
                                             "https://photonlab.xyz"));
        bexplores.add(new explore_item_Class("https://drive.google.com/uc?export=download&id=12J_qCdmnVDHiKFV_UZ4126E6Pq9vtjbM",
                                             " https://store.photonlab.xyz/"));

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

            fragment_explore_indiv bfgExplore = new fragment_explore_indiv(bexplores.get(position).getLink());
            FragmentTransaction ftindiv = getActivity().getSupportFragmentManager().beginTransaction();
            ftindiv.setCustomAnimations(R.anim.pop_enter, R.anim.pop_out, R.anim.pop_enter, R.anim.pop_out);
            ftindiv.replace(R.id.container, bfgExplore).addToBackStack(null);
            ftindiv.commit();

        } else {


        }
    }


}






