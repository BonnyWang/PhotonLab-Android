package xyz.photonlab.photonlabandroid;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;

/**
 * created by KIO on 2019/9/9
 */
public class ColorPickerActivity extends AppCompatActivity {

    private ColorPicker colorDisk = null;
    private TextView tv;
    private ImageButton closeBt;
    private Button setBt;
    private Button addFavBt;
    static String colorStr;
    static int colorcode;
    EditText rValue;
    EditText gValue;
    EditText bValue;

    private static final String TAG = "dialog_Colorpicker";

    static int whichOne;


    dialog_colorpicker.colorPick_Listener mlistener;
    private static boolean showAddFav = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.fragment_colorpicker_layout);

        rValue = findViewById(R.id.editTextR);
        rValue.setText("0");
        rValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                colorDisk.hideCursor();
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    rgb_reading(getWindow().getDecorView());
                } catch (NumberFormatException ex) {
                }
            }
        });


        gValue = findViewById(R.id.editTextG);
        gValue.setText("0");
        gValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                colorDisk.hideCursor();
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    rgb_reading(getWindow().getDecorView());
                } catch (NumberFormatException ex) {
                }
            }
        });

        bValue = findViewById(R.id.editTextB);
        bValue.setText("0");
        bValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                colorDisk.hideCursor();
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    rgb_reading(getWindow().getDecorView());
                } catch (NumberFormatException ex) {
                }
            }
        });


        //tv=(TextView)view.findViewById(R.id.tv_info);
        closeBt = findViewById(R.id.close);

        closeBt.setOnClickListener(v -> {
            finish();
            //getParentFragment().getChildFragmentManager().popBackStack();
        });

        addFavBt = findViewById(R.id.AddFavColor);

        if (!showAddFav) {
            this.addFavBt.setVisibility(View.GONE);
        }

        addFavBt.setOnClickListener(v -> {
            mlistener.getRGB(colorcode, whichOne);
            finish();
        });

        setBt = findViewById(R.id.setColorButton);
        setBt.setOnClickListener(v -> {

            mlistener.beSet(colorcode, whichOne);
            finish();
        });

        colorDisk = findViewById(R.id.colorDisk);
        colorDisk.setOnColorBackListener((a, r, g, b) -> {
//                tv.setText("R：" + r + "\nG：" + g + "\nB：" + b + "\n" + colorDisk.getColorStr());
//                tv.setTextColor(Color.argb(a, r, g, b));

            rValue.setText(String.valueOf(r));
            gValue.setText(String.valueOf(g));
            bValue.setText(String.valueOf(b));

            setBt.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorStr)));
            colorStr = colorDisk.getColorStr();
            colorcode = colorDisk.getColorcode();
        });
        if (Session.getInstance().isDarkMode(this)) {
            getWindow().getDecorView().setBackgroundColor(Theme.Dark.MAIN_BACKGROUND);
            rValue.setTextColor(Theme.Dark.SELECTED_TEXT);
            gValue.setTextColor(Theme.Dark.SELECTED_TEXT);
            bValue.setTextColor(Theme.Dark.SELECTED_TEXT);
        }
        if (Session.getInstance().isDarkMode(this)) {
            setBt.setBackgroundTintList(ColorStateList.valueOf(Theme.Dark.CARD_BACKGROUND));
            addFavBt.setBackgroundTintList(ColorStateList.valueOf(Theme.Dark.CARD_BACKGROUND));
        }

    }

    public void rgb_reading(View view) {
        int r = Integer.parseInt(rValue.getText().toString());
        int g = Integer.parseInt(gValue.getText().toString());
        int b = Integer.parseInt(bValue.getText().toString());
//        Log.d(TAG, "refreshing color picker ");
//        colorDisk = null;
//        colorDisk = view.findViewById(R.id.colorDisk);


        colorStr = "#" + colorDisk.toBrowserHexValue(r)
                + colorDisk.toBrowserHexValue(g)
                + colorDisk.toBrowserHexValue(b);
        setBt.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorStr)));
        colorcode = (0xff) << 24 | (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
        Log.d("yes", "rgb_reading: " + colorcode);
    }

    public interface colorPick_Listener {
        // TODO: Update argument type and name
        int getRGB(int rgbValue, int which);

        void beSet(int rgbValue, int which);
    }

    public static void dismissAddFav() {
        showAddFav = false;
    }
}
