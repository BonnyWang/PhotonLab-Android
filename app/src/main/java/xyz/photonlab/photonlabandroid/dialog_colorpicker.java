package xyz.photonlab.photonlabandroid;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;


public class dialog_colorpicker extends Fragment {
    private ColorPicker colorDisk = null;
    private TextView tv;
    private ImageButton closeBt, switchBtn;
    private Button setBt;
    private ImageButton addFavBt;
    private SeekBar sbColorTemp;
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
        View view = inflater.inflate(R.layout.fragment_colorpicker_layout, container, false);
        sbColorTemp = view.findViewById(R.id.color_temp);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.color_seek_back);

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
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
            //getParentFragment().getChildFragmentManager().popBackStack();
        });

        addFavBt = view.findViewById(R.id.AddFavColor);


        addFavBt.setOnClickListener(v -> {
            mlistener.getRGB(colorcode, whichOne);
            addFavBt.setBackgroundTintList(ColorStateList.valueOf(colorcode));
        });

        setBt = view.findViewById(R.id.setColorButton);
        setBt.setOnClickListener(v -> {

            mlistener.beSet(colorcode, whichOne);
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }

        });

        colorDisk = view.findViewById(R.id.colorDisk);
        colorDisk.setOnColorBackListener((a, r, g, b) -> {
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
            setBt.setBackgroundTintList(ColorStateList.valueOf(Theme.Dark.CARD_BACKGROUND));
            addFavBt.setBackgroundTintList(ColorStateList.valueOf(Theme.Dark.CARD_BACKGROUND));
        }

        if (!showAddFav) {
            this.addFavBt.setVisibility(View.GONE);
            LinearLayout.LayoutParams pr = (LinearLayout.LayoutParams) this.setBt.getLayoutParams();
            pr.setMarginEnd(pr.getMarginStart());
        }
        Vibrator vibrator = null;
        if (getContext() != null)
            vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

        final Vibrator fVibrator = vibrator;

        sbColorTemp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if ((progress == 0 || progress == 100) && fVibrator != null)
                    fVibrator.vibrate(50);

                colorDisk.hideCursor();
                colorDisk.invalidate();

                int color = bitmap.getPixel((int) ((bitmap.getWidth() - 1) * progress / 100f),
                        bitmap.getHeight() / 2);

                rValue.setText(String.valueOf(Color.red(color)));
                gValue.setText(String.valueOf(Color.green(color)));
                bValue.setText(String.valueOf(Color.blue(color)));

                setButton_Color(getHexString(color));
                colorStr = getHexString(color);
                colorcode = color;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        addFavBt.setStateListAnimator(setBt.getStateListAnimator().clone());
        switchBtn = view.findViewById(R.id.switchPan);
        Bitmap defaultBitmap = colorDisk.getBitmapTemp();
        Bitmap switchBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.colorpickercopy);

        Bitmap defaultBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.ring0);
        Bitmap switchBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.ring1);

        switchBtn.setOnClickListener(v -> {
            Bitmap current = colorDisk.getBitmapTemp();
            if (current == defaultBitmap) {
                colorDisk.setBitmapTemp(switchBitmap);
                switchBtn.setImageBitmap(switchBitmap2);
            } else {
                colorDisk.setBitmapTemp(defaultBitmap);
                switchBtn.setImageBitmap(defaultBitmap2);
            }
            colorDisk.hideCursor();
            colorDisk.invalidate();
        });
        return view;
    }

    private String getHexString(int color) {
        String s = "#";
        int colorStr = (color & 0xff000000) | (color & 0x00ff0000) | (color & 0x0000ff00) | (color & 0x000000ff);
        s = s + Integer.toHexString(colorStr);
        return s;
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