package xyz.photonlab.photonlabandroid;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import xyz.photonlab.photonlabandroid.R;

import java.util.ArrayList;
import java.util.List;


public class fragement_theme_individual extends Fragment {
    List<theme_Content_Class> items = new ArrayList<>();
    int[] gradient;
    String themeName;
    GradientDrawable topCircle_Background, setButton_Background;
    ImageView topCircle;
    TextView title;
    Button setButton;
    theme_Class mtheme;
    Button backButton;
    ToggleButton favorite;

    themeIndivListener mlistener;

    boolean isFavorite;

    public fragement_theme_individual(theme_Class mtheme, boolean isFavorite) {
        this.gradient = mtheme.getColors();
        this.themeName = mtheme.getName();
        this.mtheme = mtheme;
        this.isFavorite = isFavorite;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ;
        View view = inflater.inflate(R.layout.fragement_theme_individual_layout, container, false);
        ListView lv = (ListView) view.findViewById(R.id.info_list);

        initializeData();
        theme_Content_Adapter adapter = new theme_Content_Adapter(getActivity(), R.layout.theme_info_item, items);
        lv.setAdapter(adapter);

        topCircle = view.findViewById(R.id.topCircle);
        topCircle_Background = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradient);
        topCircle_Background.setShape(GradientDrawable.OVAL);
        topCircle.setImageDrawable(topCircle_Background);

        title = view.findViewById(R.id.themeName);
        title.setText(themeName);

        setButton = view.findViewById(R.id.setbutton);
        setButton_Background = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradient);
        setButton_Background.setShape(GradientDrawable.RECTANGLE);
        setButton_Background.setCornerRadius(50);
        setButton_Background.setSize(1000, 50);
        setButton.setBackground(setButton_Background);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Data Transfer Back-End -Bonny

            }
        });


        backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        favorite = view.findViewById(R.id.favorite);
        if (isFavorite) {
            favorite.setBackgroundDrawable(getResources().getDrawable(R.drawable.favorite, null));
        } else {
            favorite.setBackgroundDrawable(getResources().getDrawable(R.drawable.favorite_border, null));
        }

        favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isFavorite = true;
                    favorite.setBackgroundDrawable(getResources().getDrawable(R.drawable.favorite, null));
                    mlistener.Addavorite(mtheme);
                } else {
                    favorite.setBackgroundDrawable(getResources().getDrawable(R.drawable.favorite_border, null));
                    mlistener.RemoveFavorite(mtheme);
                    isFavorite = false;
                }
            }
        });
        return view;
    }

    private void initializeData() {
        items = new ArrayList<>();
        items.add(new theme_Content_Class("Creator", mtheme.getCreater()));
        items.add(new theme_Content_Class("Mood", mtheme.getMood()));
    }

    public interface themeIndivListener {
        public theme_Class Addavorite(theme_Class current);

        public theme_Class RemoveFavorite(theme_Class currrent);
    }

    public void setListener(themeIndivListener mlistener) {
        this.mlistener = mlistener;
    }


}