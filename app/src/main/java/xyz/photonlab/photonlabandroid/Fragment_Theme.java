package xyz.photonlab.photonlabandroid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;


public class Fragment_Theme extends Fragment
        implements RvAdapter.OnNoteListener,
        fragement_theme_individual.themeIndivListener,
        fragment_theme_Download.fdlListener, Session.OnThemeChangeListener {

    private final static String TAG = "Fragment_Theme";
    Context context;
    ArrayList<theme_Class> mtheme;
    ArrayList<theme_Class> mfavoriteTheme;
    ArrayList<theme_Class> sweetTheme;
    ImageView imageView_Card;
    Spinner spinnerMenu;
    View btn_no_more;
    RvAdapter.OnNoteListener mOnNoteListener = this;
    TextView tv_title;
    fragment_theme_Download.fdlListener mfdlListener = this;

    ArrayList<Integer> favOrder = new ArrayList<>();

    @SuppressLint("StaticFieldLeak")
    private static Fragment_Theme single_instance = null;
    RecyclerView rv;

    int dlThemeNo;
    private int position = 0;

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
        rv.setNestedScrollingEnabled(false);
        imageView_Card = view.findViewById(R.id.imageView_Card);
        btn_no_more = view.findViewById(R.id.btn_no_more);
        tv_title = view.findViewById(R.id.message);
        spinnerMenu = view.findViewById(R.id.spinnerThemes);
//        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(context,
//                R.array.theme_Menu, android.R.layout.simple_spinner_item);

        SimpleCheckableAdapter spinnerAdapter = new SimpleCheckableAdapter(getResources().getStringArray(R.array.theme_Menu));

        // spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_item);

        spinnerMenu.setAdapter(spinnerAdapter);

        Session.getInstance().requestTheme(getContext());
        mfavoriteTheme = Session.getInstance().getMfavoriteTheme();
        mtheme = Session.getInstance().getMtheme();
        sweetTheme = Session.getInstance().getSweetTheme();

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

                spinnerAdapter.setChecked(position);

                Log.i("position", position + "");

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

        Session.getInstance().addOnThemeChangeListener(this);
        initTheme(Session.getInstance().isDarkMode(getContext()));
        return view;
    }


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
            RvAdapter adapterInRemF = new NoAddRvAdapter(mfavoriteTheme, this);
            rv.setAdapter(adapterInRemF);
            updateButtonTypeForRv();
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

            Intent i = new Intent(this.context, ThemeDetailActivity.class);
            i.putExtra("current", mtheme.indexOf(current));
            this.position = mtheme.indexOf(current);
            startActivityForResult(i, 0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("result code", resultCode + "");
        if (resultCode == 0) {
            Addavorite(Session.getInstance().getMtheme().get(position));
        } else if (resultCode == 1) {
            RemoveFavorite(Session.getInstance().getMtheme().get(position));
        }
    }

    //add the downloaded themeicon
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


    @Override
    public void initTheme(boolean dark) {
        Class<? extends Theme.ThemeColors> colors;
        if (dark) {
            ((CardView) btn_no_more.findViewById(R.id.plus_Theme_Card)).setCardBackgroundColor(Color.parseColor("#505154"));
            colors = Theme.Dark.class;
        } else {
            ((CardView) btn_no_more.findViewById(R.id.plus_Theme_Card)).setCardBackgroundColor(Color.parseColor("#f5f5f5"));
            colors = Theme.Normal.class;
        }
        try {
            spinnerMenu.setBackgroundTintList(ColorStateList.valueOf(colors.getField("TITLE").getInt(null)));
            tv_title.setTextColor(ColorStateList.valueOf(colors.getField("TITLE").getInt(null)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        rv.setAdapter(new RvAdapter(mtheme, this));
        spinnerMenu.setSelection(0);
    }
}
