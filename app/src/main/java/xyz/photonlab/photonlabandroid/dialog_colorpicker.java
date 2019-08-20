package xyz.photonlab.photonlabandroid;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;


public class dialog_colorpicker extends DialogFragment {
    private ColorPicker colorDisk = null;
    private TextView tv;
    private Button closeBt;
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
    private boolean showAddFav = true;

    public dialog_colorpicker() {
    }


    static dialog_colorpicker newInstance(int which) {
        whichOne = which;
        return new dialog_colorpicker();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_colorpicker_layout, container, false);
        rValue = view.findViewById(R.id.editTextR);
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
                    rgb_reading(view);
                } catch (NumberFormatException ex) {
                }
            }
        });


        gValue = view.findViewById(R.id.editTextG);
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
                    rgb_reading(view);
                } catch (NumberFormatException ex) {
                }
            }
        });

        bValue = view.findViewById(R.id.editTextB);
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
                    rgb_reading(view);
                } catch (NumberFormatException ex) {
                }
            }
        });


        //tv=(TextView)view.findViewById(R.id.tv_info);
        closeBt = view.findViewById(R.id.close);

        closeBt.setOnClickListener(v -> {
            dismiss();
            //getParentFragment().getChildFragmentManager().popBackStack();
        });

        addFavBt = view.findViewById(R.id.AddFavColor);

        if (!showAddFav) {
            this.addFavBt.setVisibility(View.GONE);
        }

        addFavBt.setOnClickListener(v -> {
            mlistener.getRGB(colorcode, whichOne);
            dismiss();
        });

        setBt = view.findViewById(R.id.setColorButton);
        setBt.setOnClickListener(v -> {

            mlistener.beSet(colorcode, whichOne);
            dismiss();

        });

        colorDisk = view.findViewById(R.id.colorDisk);
        colorDisk.setOnColorBackListener((a, r, g, b) -> {
//                tv.setText("R：" + r + "\nG：" + g + "\nB：" + b + "\n" + colorDisk.getColorStr());
//                tv.setTextColor(Color.argb(a, r, g, b));

            rValue.setText(String.valueOf(r));
            gValue.setText(String.valueOf(g));
            bValue.setText(String.valueOf(b));

            setButton_Color(colorDisk.getColorStr());
            colorStr = colorDisk.getColorStr();
            colorcode = colorDisk.getColorcode();
        });
        if (Session.getInstance().isDarkMode(getContext())) {
            view.setBackgroundColor(Theme.Dark.MAIN_BACKGROUND);
            rValue.setTextColor(Theme.Dark.SELECTED_TEXT);
            gValue.setTextColor(Theme.Dark.SELECTED_TEXT);
            bValue.setTextColor(Theme.Dark.SELECTED_TEXT);
        }
        if (Session.getInstance().isDarkMode(getContext())){
            setBt.setBackgroundTintList(ColorStateList.valueOf(Theme.Dark.CARD_BACKGROUND));
            addFavBt.setBackgroundTintList(ColorStateList.valueOf(Theme.Dark.CARD_BACKGROUND));
        }
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            DisplayMetrics dm = new DisplayMetrics();
            Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(dm);
            window.setLayout((int) (dm.widthPixels * 0.95), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    public void dismissAddFav() {
        this.showAddFav = false;
    }

    public interface colorPick_Listener {
        // TODO: Update argument type and name
        int getRGB(int rgbValue, int which);

        void beSet(int rgbValue, int which);
    }

    public void setListener(dialog_colorpicker.colorPick_Listener mlistener) {
        this.mlistener = mlistener;

    }

    public void setButton_Color(String colorStr) {
        setBt.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorStr)));
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
        setButton_Color(colorStr);
        colorcode = (255 & 0xff) << 24 | (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
        Log.d("yes", "rgb_reading: " + colorcode);
    }

}