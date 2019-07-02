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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import xyz.photonlab.photonlabandroid.R;

import java.util.ArrayList;


public class Fragment_Music extends Fragment implements RvAdapter.OnNoteListener, fragement_theme_individual.themeIndivListener {

    Context context;
    ArrayList<theme_Class> mMusic;
    ArrayList<theme_Class> mfavoriteMusic;
    ImageView imageView_Card;
    Fragment theme_Individual;
    Spinner spinnerMenu;
    RvAdapter.OnNoteListener mOnNoteListner = this;

    ArrayList<Integer> favOrder = new ArrayList<>();

    private static Fragment_Music single_instance = null;
    RecyclerView rv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static Fragment_Music getInstance()
    {
        if (single_instance == null)
            single_instance = new Fragment_Music();

        return single_instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getContext();
        View view = inflater.inflate(R.layout.fragment__music_layout, container, false);
        rv = (RecyclerView)view.findViewById(R.id.rvMusic);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);
        imageView_Card = (ImageView)view.findViewById(R.id.imageView_Card);

        spinnerMenu = view.findViewById(R.id.spinnerMusic);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(context,
                R.array.Music_Menu, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMenu.setAdapter(spinnerAdapter);

        initializeData();

        RvAdapter adapter = new RvAdapter(mMusic, this);

        rv.setAdapter(adapter);

        spinnerMenu.setSelection(0);

        spinnerMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    RvAdapter adapter0 = new RvAdapter(mMusic, mOnNoteListner);
                    rv.setAdapter(adapter0);
                }
                if(position == 1) {
                    RvAdapter adapter1 = new RvAdapter(mfavoriteMusic, mOnNoteListner);
                    rv.setAdapter(adapter1);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;

    }

    private void initializeData() {
        mMusic = new ArrayList<>();

        mMusic.add(new theme_Class("Pixie Dust", 0xffd585ff, 0xff00ffee, "Photonlab", "Relax"));
        mMusic.add(new theme_Class("Firebrick", 0xff45145a, 0xffff5300, "Photonlab", "Passion"));

        mfavoriteMusic = new ArrayList<>();
        favOrder = new ArrayList<>();
        TinyDB tinyDB = new TinyDB(this.getContext());
        if(tinyDB.getListInt("favOrderMusic").size() != 0){
            favOrder = tinyDB.getListInt("favOrderMusic");
            for(int i = 0; i < favOrder.size(); i++){
                mfavoriteMusic.add(mMusic.get(favOrder.get(i)));
            }
        }

    }

    @Override
    public void onNoteClick(int position) {


    if (spinnerMenu.getSelectedItemPosition() == 0) {
        gotoIndiv(mMusic,position);

    }

    if (spinnerMenu.getSelectedItemPosition() == 1) {
        gotoIndiv(mfavoriteMusic,position);

    }


}


    private  void gotoIndiv(ArrayList<theme_Class> themeList, int position) {
        boolean isFavorite = false;

        if (position == themeList.size()) {
            // the add button
        } else {
            theme_Class current = themeList.get(position);

            if (mfavoriteMusic.contains(current)) {
                isFavorite = true;
            }

            String name = current.getName();
            int[] gradient = current.getColors();
            theme_Individual = new fragement_theme_individual(current, isFavorite);
            ((fragement_theme_individual) theme_Individual).setListener(this);
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            ft.replace(R.id.container, theme_Individual).addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    public theme_Class Addavorite (theme_Class current){
        if(!mfavoriteMusic.contains(current)) {
            mfavoriteMusic.add(current);
            TinyDB tinyDB = new TinyDB(getContext());
            favOrder.add(mMusic.indexOf(current));
            tinyDB.putListInt("favOrderMusic", favOrder);
        }

        if(spinnerMenu.getSelectedItemPosition() == 1){
            RvAdapter adapterInAddF = new RvAdapter(mfavoriteMusic, this);
            rv.setAdapter(adapterInAddF);
        }

        return current;
    }

    @Override
    public theme_Class RemoveFavorite(theme_Class current){
        mfavoriteMusic.remove(current);

        favOrder.remove((Object)mMusic.indexOf(current));
        TinyDB tinyDB = new TinyDB(getContext());
        tinyDB.putListInt("favOrderMusic", favOrder);

        if(spinnerMenu.getSelectedItemPosition() == 1){
            RvAdapter adapterInRemF = new RvAdapter(mfavoriteMusic, this);
            rv.setAdapter(adapterInRemF);
        }

        return current;
    }

}



