package xyz.photonlab.photonlabandroid;

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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import xyz.photonlab.photonlabandroid.model.MyTheme;
import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;


public class Fragment_Theme extends Fragment
        implements RvAdapter.OnNoteListener,
        fragement_theme_individual.themeIndivListener,
        fragment_theme_Download.fdlListener, Session.OnThemeChangeListener {

    Context context;
    ArrayList<MyTheme> mtheme;
    ArrayList<MyTheme> mfavoriteTheme;
    ArrayList<MyTheme> sweetTheme;
    ImageView imageView_Card;
    Spinner spinnerMenu;
    View btn_no_more;
    RvAdapter.OnNoteListener mOnNoteListener = this;
    TextView tv_title;
    fragment_theme_Download.fdlListener mfdlListener = this;

    RecyclerView rv;

    int dlThemeNo;
    private int position = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getContext();

        return inflater.inflate(R.layout.fragment__theme_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);
        rv.setNestedScrollingEnabled(false);
        imageView_Card = view.findViewById(R.id.imageView_Card);
        btn_no_more = view.findViewById(R.id.btn_no_more);
        tv_title = view.findViewById(R.id.message);
        spinnerMenu = view.findViewById(R.id.spinnerThemes);
        SimpleCheckableAdapter spinnerAdapter = new SimpleCheckableAdapter(getResources().getStringArray(R.array.theme_Menu));
        spinnerMenu.setAdapter(spinnerAdapter);
        Session.getInstance().requestTheme(getContext());
        mtheme = Session.getInstance().getAllThemes();

        sweetTheme = new ArrayList<>();
        mfavoriteTheme = new ArrayList<>();

        for (MyTheme item : mtheme) {
            if (item.isFavorite())
                mfavoriteTheme.add(item);
            if (item.isMusic())
                sweetTheme.add(item);
        }

        Collections.sort(mtheme);

        final RvAdapter adapter = new RvAdapter(mtheme, this);

        rv.setAdapter(adapter);
        updateButtonTypeForRv();

        spinnerMenu.setSelection(0);

        spinnerMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (view != null)
                    ((TextView) view).setText(null);
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
            }
        });

        Session.getInstance().addOnThemeChangeListener(this);
        initTheme(Session.getInstance().isDarkMode(getContext()));
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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i("Theme Fragment", "onHiddenChanged: ");
    }

    @Override
    public void Addavorite(MyTheme current) {
        if (mfavoriteTheme.contains(current))
            return;
        mfavoriteTheme.add(current);
        current.setFavorite(true);
        Session.getInstance().saveTheme(getContext());
    }

    @Override
    public void RemoveFavorite(MyTheme current) {
        if (!mfavoriteTheme.contains(current))
            return;
        mfavoriteTheme.remove(current);
        current.setFavorite(false);
        if (spinnerMenu.getSelectedItemPosition() == 1) {
            RvAdapter adapterInRemF = new NoAddRvAdapter(mfavoriteTheme, this);
            rv.setAdapter(adapterInRemF);
            updateButtonTypeForRv();
        }
        Session.getInstance().saveTheme(getContext());
    }

    private void gotoIndiv(ArrayList<MyTheme> themeList, int position) {

        if (position == themeList.size()) {
            fragment_theme_Download mfragment_theme_download = new fragment_theme_Download(mfdlListener, mtheme);
            FragmentTransaction fttd = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            fttd.setCustomAnimations(R.anim.pop_enter, R.anim.pop_out, R.anim.pop_enter, R.anim.pop_out);
            fttd.replace(R.id.container, mfragment_theme_download).addToBackStack(null);
            fttd.commit();
        } else {
            MyTheme current = themeList.get(position);
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
            Addavorite(mtheme.get(position));
        } else if (resultCode == 1) {
            RemoveFavorite(mtheme.get(position));
        }
    }

    //add the downloaded themeicon
    @Override
    public void dlTheme(MyTheme theme) {
        TinyDB tinyDB = new TinyDB(getContext());
        //add to the number each time
        dlThemeNo = tinyDB.getInt("dlThemeNo") + 1;
        mtheme.add(theme);
        Collections.sort(mtheme);

        RvAdapter rvAdapter = new RvAdapter(mtheme, mOnNoteListener);
        rv.setAdapter(rvAdapter);

        Session.getInstance().saveTheme(getContext());
    }


    @Override
    public void initTheme(boolean dark) {
        Class<? extends Theme.ThemeColors> colors;
        if (dark) {
            ((CardView) btn_no_more).setCardBackgroundColor(Color.parseColor("#505154"));
            colors = Theme.Dark.class;
        } else {
            ((CardView) btn_no_more).setCardBackgroundColor(Color.parseColor("#f5f5f5"));
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
