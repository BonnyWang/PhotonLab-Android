package xyz.photonlab.photonlabandroid;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
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


public class fragement_theme_individual extends Fragment implements LightStage.OnLightCheckedChangeListener {
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
//            switch (mtheme.getName()) {
//                case "Spring":
//                    url = "http://" + Session.getInstance().getLocalIP(getContext())
//                            + "/mode/theme?number=2&variables={\"var1\":0,\"var2\":255,\"var3\":0,\"var4\":100,\"var5\":120,\"var6\":0,\"var7\":20}";
//                    break;
//                case "Rainbow":
//                    url = "http://" + Session.getInstance().getLocalIP(getContext())
//                            + "/mode/theme?number=11&variables={\"var1\":30,\"var2\":2}";
//                    break;
//                case "Rainbow-Rainbow":
//                    url = "http://" + Session.getInstance().getLocalIP(getContext())
//                            + "/mode/theme?number=12&variables={\"var1\":40,\"var2\":2}";
//                    break;
//                case "Fizzy Peach":
//                    url = "http://" + Session.getInstance().getLocalIP(getContext())
//                            + "/mode/theme?number=13&variables={\"var1\":100,\"var2\":20,\"var3\":20}";
//                    break;
//                case "Neon Glow":
//                    url = "http://" + Session.getInstance().getLocalIP(getContext())
//                            + "/mode/theme?number=6&variables={\"var1\":0,\"var2\":0,\"var3\":0,\"var4\":0,\"var5\":255,\"var6\":161,\"var7\":20}";
//                    break;
//                case "Tri Impulse":
//                    url = "http://" + Session.getInstance().getLocalIP(getContext())
//                            + "/mode/theme?number=10&variables={\"var1\":255,\"var2\":0,\"var3\":0,\"var4\":0,\"var5\":0,\"var6\":255,\"var7\":15}";
//                    break;
//                case "Ocean Blue":
//                    url = "http://" + Session.getInstance().getLocalIP(getContext())
//                            + "/mode/theme?number=17&variables={\"var1\":13,\"var2\":20,\"var3\":222,\"var4\":0,\"var5\":15,\"var6\":20,\"var7\":100}";
//                    break;
//                case "Purple Lake":
//                    url = "http://" + Session.getInstance().getLocalIP(getContext())
//                            + "/mode/theme?number=3&variables={\"var1\":153,\"var2\":0,\"var3\":255,\"var4\":43,\"var5\":0,\"var6\":17,\"var7\":20}";
//                    break;
//                case "Fresh Papaya":
//                    url = "http://" + Session.getInstance().getLocalIP(getContext())
//                            + "/mode/theme?number=5&variables={\"var1\":50,\"var2\":0,\"var3\":3,\"var4\":248,\"var5\":187,\"var6\":34,\"var7\":20}";
//                    break;
//                case "Ultramarine":
//                    url = "http://" + Session.getInstance().getLocalIP(getContext())
//                            + "/mode/theme?number=5&variables={\"var1\":50,\"var2\":0,\"var3\":3,\"var4\":248,\"var5\":187,\"var6\":34,\"var7\":20}";
//                    break;
//                default:
//                    return;
//            }
            vibrator.vibrate(50);
            NetworkHelper helper = new NetworkHelper();

            Request request = new Request.Builder().url(url).build();
            Log.i("Themes", url);
            helper.connect(request, client);
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
        LightStage stage = Session.getInstance().requireLayoutStage(getContext(), true);
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
            setSingle.setBackgroundTintList(ColorStateList.valueOf(Theme.Dark.CARD_BACKGROUND));
        } else {
            v1.setBackgroundColor(Theme.Normal.MAIN_BACKGROUND);
            v2.setBackgroundColor(Theme.Normal.MAIN_BACKGROUND);
        }
        if (stage == null) {
            return view;
        }
        v2.addView(stage, 0);
        v2.setPadding(0, getStatusBarHeight(getContext()), 0, 0);
        stage.requireCenter();
        stage.denyMove();
        stage.setOnLightCheckedChangeListener(this);
        for (Light light : stage.getLights()) {
            if (light != null) {
                light.setPlaneColor(Color.rgb(200, 200, 200));
            }
        }
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
            light.setPlaneColor(Color.RED);
        }
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