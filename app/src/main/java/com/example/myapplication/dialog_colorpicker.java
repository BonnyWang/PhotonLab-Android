package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;


public class dialog_colorpicker extends DialogFragment {
    private ColorPicker colorDisk=null;
    private TextView tv;
    private Button closeBt;
    private Button setBt;
    private Button addFavBt;
    private TextView tv_rgb;
    Fragment fragment_Control;
    EditText rValue;
    EditText gValue;
    EditText bValue;

    int rgbValue;
    dialog_colorpicker.colorPick_Listener mlistener;


    static dialog_colorpicker newInstance() {

        return new dialog_colorpicker();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_colorpicker_layout, container, false);
        rValue = view.findViewById(R.id.editTextR);
        gValue = view.findViewById(R.id.editTextG);
        bValue = view.findViewById(R.id.editTextB);


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
                //do something my friend
            }
        });

        setBt = (Button)view.findViewById(R.id.setColorButton);
        setBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //constructer of fragment moumou

//                fragment_Control = new Fragment_Control(colorDisk.getColorStr());
//                FragmentTransaction ft1 = getActivity().getSupportFragmentManager().beginTransaction();
//                ft1.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//                ft1.replace(R.id.fgm, fragment_Control).addToBackStack(null);
//                ft1.commit();
                rgbValue = colorDisk.getColorcode();
                mlistener.getRGB(rgbValue);

                dismiss();

            }
        });

        colorDisk=(ColorPicker)view.findViewById(R.id.colorDisk);
        colorDisk.setOnColorBackListener(new ColorPicker.OnColorBackListener() {
            @Override
            public void onColorBack(int a, int r, int g, int b) {
//                tv.setText("R：" + r + "\nG：" + g + "\nB：" + b + "\n" + colorDisk.getColorStr());
//                tv.setTextColor(Color.argb(a, r, g, b));
                rValue.setHint(String.valueOf(r));
                gValue.setHint(String.valueOf(g));
                bValue.setHint(String.valueOf(b));

                GradientDrawable setButton_Background = new GradientDrawable();
                setButton_Background.setShape(GradientDrawable.RECTANGLE);
                setButton_Background.setCornerRadius(24);
                setButton_Background.setColor(Color.parseColor(colorDisk.getColorStr()));
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
        public int getRGB(int rgbValue);
    }

    public void setListener(dialog_colorpicker.colorPick_Listener mlistener){
        this.mlistener = mlistener;

    }
}