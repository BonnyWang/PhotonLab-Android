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
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import xyz.photonlab.photonlabandroid.model.Session;


public class Fragment_Theme extends Fragment
        implements RvAdapter.OnNoteListener,
        fragement_theme_individual.themeIndivListener,
        fragment_theme_Download.fdlListener {

    private final static String TAG = "Fragment_Theme";
    Context context;
    ArrayList<theme_Class> mtheme;
    ArrayList<theme_Class> mfavoriteTheme;
    ArrayList<theme_Class> sweetTheme;
    ImageView imageView_Card;
    Fragment theme_Individual;
    Spinner spinnerMenu;
    View btn_no_more;
    RvAdapter.OnNoteListener mOnNoteListener = this;

    fragment_theme_Download.fdlListener mfdlListener = this;

    ArrayList<Integer> favOrder = new ArrayList<>();

    private static Fragment_Theme single_instance = null;
    RecyclerView rv;

    int dlThemeNo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public static Fragment_Theme getInstance() {
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
        rv = view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);
        imageView_Card = view.findViewById(R.id.imageView_Card);
        btn_no_more = view.findViewById(R.id.btn_no_more);

        spinnerMenu = view.findViewById(R.id.spinnerThemes);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(context,
                R.array.theme_Menu, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMenu.setAdapter(spinnerAdapter);

        Session.getInstance().requestTheme(getContext());
        mfavoriteTheme = Session.getInstance().getMfavoriteTheme();
        mtheme = Session.getInstance().getMtheme();
        sweetTheme = Session.getInstance().getSweetTheme();

        //initializeData();


        final RvAdapter adapter = new RvAdapter(mtheme, this);

        rv.setAdapter(adapter);
        updateButtonTypeForRv();

        spinnerMenu.setSelection(0);

        spinnerMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                    ((TextView) view).setText(null);
                } catch (Exception ignored) {
                }
                if (position == 0) {

                    RvAdapter adapter0 = new RvAdapter(mtheme, mOnNoteListener);
                    rv.setAdapter(adapter0);
                    rv.getChildAt(adapter0.getItemCount() - 1);
                }

                if (position == 1) {
                    RvAdapter adapter1 = new NoAddRvAdapter(mfavoriteTheme, mOnNoteListener);
                    rv.setAdapter(adapter1);
                }

                if (position == 2) {
                    RvAdapter adapter1 = new NoAddRvAdapter(sweetTheme, mOnNoteListener);
                    rv.setAdapter(adapter1);
                }
                updateButtonTypeForRv();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                ((TextView)view).setText(null);
            }
        });


//        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
//        User user = new User("Bonny", "Bonny.y.gardy@gmail.com");
//
//        database.child("users").child("0").setValue(user);
//
//        theme_Class thistheme = mtheme.get(1);
//        database.child("mtheme").setValue(thistheme);
        return view;

    }

//    private void initializeData() {
//
//        TinyDB tinyDB = new TinyDB(getContext());
//
//        mtheme = new ArrayList<>();
//        mtheme.add(new theme_Class("Spring", 0xff009e00, 0xfffcee21, "Photonlab", "Home Happy Sunset"));
//        mtheme.add(new theme_Class("Fizzy Peach", 0xfff24645, 0xffebc08d, "Photonlab", "Sweet sweet"));
//        mtheme.add(new theme_Class("Sky", 0xff00b7ff, 0xff00ffee, "Photonlab", "Blue Blue"));
//        mtheme.add(new theme_Class("Neon Glow", 0xff00ffa1, 0xff00ffff, "Photonlab", "High"));
//
//
//        //Use for clear the database
////        for(int i = 0; i <= dlThemeNo; i++ ) {
////           tinyDB.remove("dlTheme" + i);
////            Log.d(TAG, "initializeData: Hi");
////
////        }
////
////        tinyDB.remove("dlThemeNo");
//        //
//
//        dlThemeNo = tinyDB.getInt("dlThemeNo");
//        if (tinyDB.getInt("dlThemeNo") != -1) {
//            for (int i = 0; i <= dlThemeNo; i++) {
//                mtheme.add(tinyDB.getObject("dlTheme" + i, theme_Class.class));
//            }
//            Log.d(TAG, "initializeData: Hi");
//        }
//
//        mfavoriteTheme = new ArrayList<>();
//        favOrder = new ArrayList<>();
//
//
//        if (tinyDB.getListInt("favOrder").size() != 0) {
//            favOrder = tinyDB.getListInt("favOrder");
//            for (int i = 0; i < favOrder.size(); i++) {
//                mfavoriteTheme.add(mtheme.get(favOrder.get(i)));
//                Log.d(TAG, "initializeData: tinydb");
//            }
//        }
//
//
//        sweetTheme = new ArrayList<>();
//        sweetTheme.add(new theme_Class("Fizzy Peach", 0xfff24645, 0xffebc08d, "Photonlab", "Sweet sweet"));
//    }

    private void updateButtonTypeForRv() {
        int count = Objects.requireNonNull(rv.getAdapter()).getItemCount();
        if (count == 0 && btn_no_more.getVisibility() == View.GONE) {
            btn_no_more.setVisibility(View.VISIBLE);
        }
        if (count > 0 && btn_no_more.getVisibility() == View.VISIBLE) {
            btn_no_more.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNoteClick(int position) {


        if (spinnerMenu.getSelectedItemPosition() == 0) {
            gotoIndiv(mtheme, position);

        }

        if (spinnerMenu.getSelectedItemPosition() == 1) {
            gotoIndiv(mfavoriteTheme, position);
        }

        if (spinnerMenu.getSelectedItemPosition() == 2) {
            gotoIndiv(sweetTheme, position);
        }
    }


    @Override
    public theme_Class Addavorite(theme_Class current) {
        if (!mfavoriteTheme.contains(current)) {
            mfavoriteTheme.add(current);
            TinyDB tinyDB = new TinyDB(getContext());
            favOrder.add(mtheme.indexOf(current));
            tinyDB.putListInt("favOrder", favOrder);
        }


        if (spinnerMenu.getSelectedItemPosition() == 1) {
            RvAdapter adapterInAddF = new RvAdapter(mfavoriteTheme, this);
            rv.setAdapter(adapterInAddF);
        }

        return current;
    }

    @Override
    public theme_Class RemoveFavorite(theme_Class current) {
        mfavoriteTheme.remove(current);
        favOrder.remove((Object) mtheme.indexOf(current));
        TinyDB tinyDB = new TinyDB(getContext());
        tinyDB.putListInt("favOrder", favOrder);
        if (spinnerMenu.getSelectedItemPosition() == 1) {
            RvAdapter adapterInRemF = new RvAdapter(mfavoriteTheme, this);
            rv.setAdapter(adapterInRemF);
        }

        return current;
    }

    private void gotoIndiv(ArrayList<theme_Class> themeList, int position) {
        boolean isFavorite = false;

        if (position == themeList.size()) {
            fragment_theme_Download mfragment_theme_download = new fragment_theme_Download(mfdlListener, mtheme);
            FragmentTransaction fttd = getActivity().getSupportFragmentManager().beginTransaction();
            fttd.setCustomAnimations(R.anim.pop_enter, R.anim.pop_out, R.anim.pop_enter, R.anim.pop_out);
            fttd.replace(R.id.container, mfragment_theme_download).addToBackStack(null);
            fttd.commit();
        } else {
            theme_Class current = themeList.get(position);

            if (mfavoriteTheme.contains(current)) {
                isFavorite = true;
            }

            String name = current.getName();
            int[] gradient = current.getColors();
            theme_Individual = new fragement_theme_individual(current, isFavorite);
            ((fragement_theme_individual) theme_Individual).setListener(this);
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
            ft.replace(R.id.container, theme_Individual).addToBackStack(null);
            ft.commit();
        }
    }

    //add the downloaded theme
    @Override
    public theme_Class dlTheme(theme_Class theme) {
        TinyDB tinyDB = new TinyDB(getContext());

        //add to the number each time
        dlThemeNo = tinyDB.getInt("dlThemeNo") + 1;

        tinyDB.putInt("dlThemeNo", dlThemeNo);
        tinyDB.putObject("dlTheme" + dlThemeNo, theme);


        mtheme.add(theme);

        //write a function for update the adapter
        //TODO: need to consider the menu selction
        RvAdapter rvAdapter = new RvAdapter(mtheme, mOnNoteListener);
        rv.setAdapter(rvAdapter);

        return theme;
    }


}
