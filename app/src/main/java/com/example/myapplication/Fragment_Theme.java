package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Theme extends Fragment implements RvAdapter.OnNoteListener,fragement_theme_individual.themeIndivListener {

    private final static String TAG = "Fragment_Theme";
    Context context;
    ArrayList<theme_Class> mtheme;
    static ArrayList<theme_Class> mfavoriteTheme = new ArrayList<>();
    static ArrayList<theme_Class> sweetTheme = new ArrayList<>();
    ImageView imageView_Card;
    Fragment theme_Individual;
    Spinner spinnerMenu;
    RvAdapter.OnNoteListener mOnNoteListener = this;

    private static Fragment_Theme single_instance = null;
    RecyclerView rv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public static Fragment_Theme getInstance()
    {
        if (single_instance == null)
            single_instance = new Fragment_Theme();

        return single_instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getContext();
        View view = inflater.inflate(R.layout.fragment__theme_layout, container, false);
        rv = (RecyclerView)view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);
        imageView_Card = (ImageView)view.findViewById(R.id.imageView_Card);

        spinnerMenu = view.findViewById(R.id.spinnerThemes);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(context,
                R.array.theme_Menu, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMenu.setAdapter(spinnerAdapter);


        initializeData();

        final RvAdapter adapter = new RvAdapter(mtheme, this);

        rv.setAdapter(adapter);

        spinnerMenu.setSelection(0);

        spinnerMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    RvAdapter adapter0 = new RvAdapter(mtheme, mOnNoteListener);
                    rv.setAdapter(adapter0);
                }
                if(position == 1) {
                    RvAdapter adapter1 = new RvAdapter(mfavoriteTheme, mOnNoteListener);
                    rv.setAdapter(adapter1);
                }

                if(position == 2) {
                    RvAdapter adapter1 = new RvAdapter(sweetTheme, mOnNoteListener);
                    rv.setAdapter(adapter1);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;

    }

    private void initializeData(){
        mtheme = new ArrayList<>();
        mtheme.add(new theme_Class("Spring", new int[] {0xff009e00, 0xfffcee21}, "Photonlab", "Home Happy Sunset"));
        mtheme.add(new theme_Class("Fizzy Peach", new int[] {0xfff24645, 0xffebc08d},"Photonlab", "Sweet sweet"));
        mtheme.add(new theme_Class("Sky", new int[] {0xff00b7ff, 0xff00ffee},"Photonlab", "Blue Blue"));

        sweetTheme.add(new theme_Class("Fizzy Peach", new int[] {0xfff24645, 0xffebc08d},"Photonlab", "Sweet sweet"));
    }

    @Override
    public void onNoteClick(int position) {
        boolean isFavorite = false;

        if (position == mtheme.size()){
            //do something
            Log.d("yes", "onNoteClick: success");
        }else{

            if (spinnerMenu.getSelectedItemPosition() == 0){
                theme_Class current = mtheme.get(position);
                for(int i = 0; i < mfavoriteTheme.size(); i++){
                    if (current.equals(mfavoriteTheme.get(i))){
                        isFavorite = true;
                    }
                }

                String name = current.getName();
                int[] gradient = current.getColors();
                theme_Individual = new fragement_theme_individual(current, isFavorite);
                ((fragement_theme_individual) theme_Individual).setListener(this);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                ft.replace(R.id.container, theme_Individual).addToBackStack(null);
                ft.commit();
            }

            if (spinnerMenu.getSelectedItemPosition() == 1){
                theme_Class current = mfavoriteTheme.get(position);
                String name = current.getName();
                int[] gradient = current.getColors();
                theme_Individual = new fragement_theme_individual(current, true);
                ((fragement_theme_individual) theme_Individual).setListener(this);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                ft.replace(R.id.container, theme_Individual).addToBackStack(null);
                ft.commit();
            }

            if (spinnerMenu.getSelectedItemPosition() == 2) {
                theme_Class current = sweetTheme.get(position);

                if (mfavoriteTheme.contains(current)) {
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

    }

    @Override
    public theme_Class Addavorite (theme_Class current){
        if(!mfavoriteTheme.contains(current)) {
            mfavoriteTheme.add(current);
        }

        if(spinnerMenu.getSelectedItemPosition() == 1){
            RvAdapter adapterInAddF = new RvAdapter(mfavoriteTheme, this);
            rv.setAdapter(adapterInAddF);
        }

        return current;
    }

    @Override
    public theme_Class RemoveFavorite(theme_Class current){
        mfavoriteTheme.remove(current);
        if(spinnerMenu.getSelectedItemPosition() == 1){
            RvAdapter adapterInRemF = new RvAdapter(mfavoriteTheme, this);
            rv.setAdapter(adapterInRemF);
        }

        return current;
    }


}
