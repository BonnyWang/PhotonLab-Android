package xyz.photonlab.photonlabandroid;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
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

import static android.content.Context.VIBRATOR_SERVICE;


public class fragement_theme_individual extends Fragment {
    List<theme_Content_Class> items = new ArrayList<>();
    int[] gradient;
    String themeName;
    GradientDrawable topCircle_Background, setButton_Background;
    ImageView topCircle;
    TextView title;
    Button setButton;
    MyTheme mtheme;
    Button backButton;
    ToggleButton favorite;
    int resultCode = -1;

    Vibrator vibrator;
    themeIndivListener mlistener;

    boolean isFavorite;

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
        setButton_Background.setShape(GradientDrawable.RECTANGLE);
        setButton_Background.setCornerRadius(30);
        setButton_Background.setSize(1000, 50);
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
        if (Session.getInstance().isDarkMode(getContext()))
            view.setBackgroundColor(Theme.Dark.MAIN_BACKGROUND);
        return view;
    }

    private void initializeData() {
        items = new ArrayList<>();
        items.add(new theme_Content_Class("Creator", mtheme.getCreater()));
        items.add(new theme_Content_Class("Mood", mtheme.getMood()));
    }

    public interface themeIndivListener {
        void Addavorite(MyTheme current);

        void RemoveFavorite(MyTheme currrent);
    }

    public void setListener(themeIndivListener mlistener) {
        this.mlistener = mlistener;
    }

}