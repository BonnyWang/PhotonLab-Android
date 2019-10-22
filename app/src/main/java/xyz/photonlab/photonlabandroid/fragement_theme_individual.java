package xyz.photonlab.photonlabandroid;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import xyz.photonlab.photonlabandroid.model.MyTheme;
import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;
import xyz.photonlab.photonlabandroid.utils.NetworkHelper;
import xyz.photonlab.photonlabandroid.views.Light;
import xyz.photonlab.photonlabandroid.views.LightStage;

import static android.content.Context.VIBRATOR_SERVICE;


public class fragement_theme_individual extends Fragment implements LightStage.OnLightCheckedChangeListener, fragment_layout.OnSavedLayoutListener {
    List<theme_Content_Class> items = new ArrayList<>();
    int[] gradient;
    String themeName;
    GradientDrawable topCircle_Background, setButton_Background;
    ImageView topCircle;
    TextView title;
    Button setButton;
    MyTheme mtheme;
    Button backButton;
    ImageButton setSingle;
    ToggleButton favorite;
    int resultCode = -1;
    LightStage stage;

    Vibrator vibrator;
    themeIndivListener mlistener;

    boolean isFavorite;
    private Context context;

    public fragement_theme_individual() {
        this.gradient = new int[]{0xfff72323, 0xfff023f7};
        this.themeName = "Unknown";
        this.mtheme = new MyTheme("PhotonLab", "Honey", "Unknown", 0, gradient, new int[]{}, false, false);
        this.isFavorite = false;
    }

    public fragement_theme_individual(MyTheme mtheme, boolean isFavorite) {
        this.gradient = mtheme.getGradientColors();
        this.themeName = mtheme.getName();
        this.mtheme = mtheme;
        this.isFavorite = isFavorite;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragement_theme_individual_layout, container, false);
        ListView lv = view.findViewById(R.id.info_list);
        vibrator = (Vibrator) Objects.requireNonNull(getActivity()).getSystemService(VIBRATOR_SERVICE);
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
        setButton_Background.setCornerRadius(20);
        setButton.setBackground(setButton_Background);
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(10, TimeUnit.MILLISECONDS).build();
        setButton.setOnClickListener(v -> {
            String url = "http://" + Session.getInstance().getLocalIP(getContext()) + "/mode/theme?number=";
            url += mtheme.getNumber() + "&variables=";

            JSONObject object = new JSONObject();
            for (int i = 0; i < mtheme.getVars().length; i++) {
                try {
                    object.put("var" + (i + 1), mtheme.getVars()[i]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            url += object.toString();
            vibrator.vibrate(50);
            NetworkHelper helper = new NetworkHelper();

            Request request = new Request.Builder().url(url).build();
            Log.i("Themes", url);
            helper.connect(request, client);
            if (stage != null) {
                List<Light> lights = stage.getLights();

                try {
                    for (Light light : lights) {
                        light.setPlaneColor(gradient[gradient.length - 1]);
                    }
                    Session.getInstance().saveLayoutToLocal(getContext(), stage);
                    Session.getInstance().notifyLayoutChanged();
                    onSavedLayout(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            getActivity().setResult(resultCode);
            getActivity().finish();
        });


        favorite = view.findViewById(R.id.favorite);
        if (isFavorite) {
            favorite.setBackgroundDrawable(getResources().getDrawable(R.drawable.favorite, null));
            resultCode = 0;
        } else {
            favorite.setBackgroundDrawable(getResources().getDrawable(R.drawable.favorite_border, null));
            resultCode = 1;
        }

        favorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                isFavorite = true;
                favorite.setBackgroundDrawable(getResources().getDrawable(R.drawable.favorite, null));
                mlistener.Addavorite(mtheme);
                resultCode = 0;
            } else {
                favorite.setBackgroundDrawable(getResources().getDrawable(R.drawable.favorite_border, null));
                mlistener.RemoveFavorite(mtheme);
                isFavorite = false;
                resultCode = 1;
            }
        });
        setSingle = view.findViewById(R.id.setSingle);
        setSingle.setStateListAnimator(setButton.getStateListAnimator().clone());
        setSingle.setBackgroundTintList(ColorStateList.valueOf(gradient[gradient.length - 1]));

        Animation in = AnimationUtils.loadAnimation(getContext(), R.anim.pop_enter);
        Animation out = AnimationUtils.loadAnimation(getContext(), R.anim.pop_out);

        ConstraintLayout v1 = view.findViewById(R.id.frameLayout);
        FrameLayout v2 = view.findViewById(R.id.lights);

        setSingle.setOnClickListener(v -> {
            v2.setVisibility(View.VISIBLE);
            v2.startAnimation(in);
        });

        v2.findViewById(R.id.exit).setOnClickListener(v -> {
            v2.setVisibility(View.GONE);
            v2.startAnimation(out);
        });

        if (Session.getInstance().isDarkMode(getContext())) {
            v1.setBackgroundColor(Theme.Dark.MAIN_BACKGROUND);
            v2.setBackgroundColor(Theme.Dark.MAIN_BACKGROUND);
        } else {
            v1.setBackgroundColor(Theme.Normal.MAIN_BACKGROUND);
            v2.setBackgroundColor(Theme.Normal.MAIN_BACKGROUND);
        }
        v2.setPadding(0, getStatusBarHeight(getContext()), 0, 0);
        loadStage(v2);
        view.findViewById(R.id.tvGotoSetup).setOnClickListener(v -> {
            fragment_layout fragmentLayout = new fragment_layout();
            FragmentTransaction ftl = getActivity().getSupportFragmentManager().beginTransaction();
            ftl.setCustomAnimations(R.anim.pop_enter, R.anim.pop_out, R.anim.pop_enter, R.anim.pop_out);
            ftl.replace(R.id.lights, fragmentLayout).addToBackStack(null);
            ftl.commit();
        });
        if (stage != null) {
            view.findViewById(R.id.content_container).setVisibility(View.GONE);
        }
        Session.getInstance().registerOnLayoutSaveListener(this);
        return view;
    }

    private void initializeData() {
        items = new ArrayList<>();
        items.add(new theme_Content_Class("Creator", mtheme.getCreater()));
        items.add(new theme_Content_Class("Mood", mtheme.getMood()));
    }

    @Override
    public void onLightCheckedChanged(Light light) {
        if (light != null) {
            Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(50);
            NetworkHelper helper = new NetworkHelper();
            String url = "http://" + Session.getInstance().getLocalIP(context) + "/mode/theme?node=" + light.getNum() + "&number=" + mtheme.getNumber();
            JSONObject jsonObject = new JSONObject();
            for (int i = 0; i < mtheme.getVars().length; i++) {
                try {
                    jsonObject.put("var" + (i + 1), mtheme.getVars()[i]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            url += "&variables=" + jsonObject.toString();
            Log.i("ThemeIndividual", "onLightCheckedChanged: " + url);
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            helper.connect(request);
            try {
                light.setPlaneColor(gradient[gradient.length - 1]);
                Session.getInstance().saveLayoutToLocal(getContext(), light, stage.getLights().indexOf(light));
                Session.getInstance().notifyLayoutChanged();

                onSavedLayout(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSavedLayout(boolean saved) {
        Log.i("AddView", stage + ":" + saved);
        if (saved && stage == null) {
            try {
                Objects.requireNonNull(getView()).findViewById(R.id.content_container).setVisibility(View.GONE);
                loadStage(getView().findViewById(R.id.lights));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loadStage(ViewGroup parent) {
        stage = Session.getInstance().requireLayoutStage(getContext(), true);
        if (stage == null)
            return;
        parent.addView(stage, 0);
        stage.requireCenter();
        stage.denyMove();
        stage.setOnLightCheckedChangeListener(this);
    }

    public interface themeIndivListener {
        void Addavorite(MyTheme current);

        void RemoveFavorite(MyTheme currrent);
    }

    public void setListener(themeIndivListener mlistener) {
        this.mlistener = mlistener;
    }

    private static int getStatusBarHeight(@Nullable Context context) {
        if (context == null)
            return 0;
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}