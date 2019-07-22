package xyz.photonlab.photonlabandroid;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import xyz.photonlab.photonlabandroid.R;


public class dialog_colorpicker extends DialogFragment {
    private ColorPicker colorDisk = null;
    private TextView tv;
    private Button closeBt;
    private Button setBt;
    private Button addFavBt;
    private TextView tv_rgb;
    static String colorStr;
    static int colorcode;
    Fragment fragment_Control;
    EditText rValue;
    EditText gValue;
    EditText bValue;

    static int whichOne;


    int rgbValue;
    dialog_colorpicker.colorPick_Listener mlistener;


    static dialog_colorpicker newInstance(int which) {
        whichOne = which;
        return new dialog_colorpicker();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_colorpicker_layout, container, false);
        rValue = view.findViewById(R.id.editTextR);
        rValue.setText("0");
        rValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    rgb_reading();
                }

                catch(NumberFormatException ex){}
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

            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    rgb_reading();
                }
                catch(NumberFormatException ex){}
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

            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    rgb_reading();
                }
                catch(NumberFormatException ex){}
            }
        });


        //tv=(TextView)view.findViewById(R.id.tv_info);
        closeBt = (Button)view.findViewById(R.id.close);

        closeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             dismiss();
                //getParentFragment().getChildFragmentManager().popBackStack();
            }
        });

        addFavBt =(Button)view.findViewById(R.id.AddFavColor);

        addFavBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.getRGB(colorcode, whichOne);
                dismiss();
            }
        });

        setBt = (Button)view.findViewById(R.id.setColorButton);
        setBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (whichOne == 0) {
                    mlistener.beSet(colorcode, whichOne);
                    dismiss();
                }

            }
        });

        colorDisk=(ColorPicker)view.findViewById(R.id.colorDisk);
        colorDisk.setOnColorBackListener(new ColorPicker.OnColorBackListener() {
            @Override
            public void onColorBack(int a, int r, int g, int b) {
//                tv.setText("R：" + r + "\nG：" + g + "\nB：" + b + "\n" + colorDisk.getColorStr());
//                tv.setTextColor(Color.argb(a, r, g, b));
                rValue.setText(String.valueOf(r));
                gValue.setText(String.valueOf(g));
                bValue.setText(String.valueOf(b));

                setButton_Color(colorDisk.getColorStr());
                colorStr=colorDisk.getColorStr();
                colorcode=colorDisk.getColorcode();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    public interface colorPick_Listener {
        // TODO: Update argument type and name
        public int getRGB(int rgbValue, int which);
        public void beSet(int rgbValue, int which);
    }

    public void setListener(dialog_colorpicker.colorPick_Listener mlistener){
        this.mlistener = mlistener;

    }
    public void setButton_Color(String colorStr){
        GradientDrawable setButton_Background = new GradientDrawable();
        setButton_Background.setShape(GradientDrawable.RECTANGLE);
        setButton_Background.setCornerRadius(24);
        setButton_Background.setColor(Color.parseColor(colorStr));
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                313,
                getResources().getDisplayMetrics()
        );

        float px1 = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                52,
                getResources().getDisplayMetrics()
        );
        int width = (int) px;
        int height = (int) px1;
        setButton_Background.setSize(width,height);
        setBt.setBackground(setButton_Background);
    }
    public void rgb_reading(){
        int r= Integer.parseInt(rValue.getText().toString());
        int g= Integer.parseInt(gValue.getText().toString());
        int b= Integer.parseInt(bValue.getText().toString());
        colorStr=  "#" + colorDisk.toBrowserHexValue(r)
                + colorDisk.toBrowserHexValue(g)
                + colorDisk.toBrowserHexValue(b);
        setButton_Color(colorStr);
        colorcode = (255 & 0xff) << 24 | (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
        Log.d("yes", "rgb_reading: "+colorcode);
    }

}