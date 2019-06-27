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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import xyz.photonlab.photonlabandroid.R;

public class fragment_colorpicker extends Fragment {
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
    static String colorStr;

    int rgbValue;
    colorPick_Listener mlistener;



    public fragment_colorpicker() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_colorpicker_layout,container,false);
        rValue = view.findViewById(R.id.editTextR);
        Log.d("shit1", "onTextChanged:hello ");
        rValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                colorStr=  "#" + colorDisk.toBrowserHexValue(Integer.parseInt(rValue.getText().toString()))
                        + colorDisk.toBrowserHexValue(Integer.parseInt(gValue.getText().toString()))
                        + colorDisk.toBrowserHexValue(Integer.parseInt(bValue.getText().toString()));
                setButton_Color(colorStr);
                Log.d("shit1", "onTextChanged:hello ");
            }

            @Override
            public void afterTextChanged(Editable s) {
                colorStr=  "#" + colorDisk.toBrowserHexValue(Integer.parseInt(rValue.getText().toString()))
                        + colorDisk.toBrowserHexValue(Integer.parseInt(gValue.getText().toString()))
                        + colorDisk.toBrowserHexValue(Integer.parseInt(bValue.getText().toString()));
                setButton_Color(colorStr);
                Log.d("shit`", "onTextChanged:hello2 ");
            }
        });

        gValue = view.findViewById(R.id.editTextG);
        gValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                colorStr=  "#" + colorDisk.toBrowserHexValue(Integer.parseInt(rValue.getText().toString()))
                        + colorDisk.toBrowserHexValue(Integer.parseInt(gValue.getText().toString()))
                        + colorDisk.toBrowserHexValue(Integer.parseInt(bValue.getText().toString()));
                setButton_Color(colorStr);
            }

            @Override
            public void afterTextChanged(Editable s) {
                colorStr=  "#" + colorDisk.toBrowserHexValue(Integer.parseInt(rValue.getText().toString()))
                        + colorDisk.toBrowserHexValue(Integer.parseInt(gValue.getText().toString()))
                        + colorDisk.toBrowserHexValue(Integer.parseInt(bValue.getText().toString()));
                setButton_Color(colorStr);
            }
        });

        bValue = view.findViewById(R.id.editTextB);
        bValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                colorStr=  "#" + colorDisk.toBrowserHexValue(Integer.parseInt(rValue.getText().toString()))
                        + colorDisk.toBrowserHexValue(Integer.parseInt(gValue.getText().toString()))
                        + colorDisk.toBrowserHexValue(Integer.parseInt(bValue.getText().toString()));
                setButton_Color(colorStr);
            }

            @Override
            public void afterTextChanged(Editable s) {
                colorStr=  "#" + colorDisk.toBrowserHexValue(Integer.parseInt(rValue.getText().toString()))
                        + colorDisk.toBrowserHexValue(Integer.parseInt(gValue.getText().toString()))
                        + colorDisk.toBrowserHexValue(Integer.parseInt(bValue.getText().toString()));
                setButton_Color(colorStr);
            }
        });
        setListener(mlistener);
        //tv=(TextView)view.findViewById(R.id.tv_info);
        closeBt = (Button)view.findViewById(R.id.close);

        closeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgbValue = colorDisk.getColorcode();
                mlistener.getRGB(rgbValue);

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
                colorStr=  "#" + colorDisk.toBrowserHexValue(Integer.parseInt(rValue.getText().toString()))
                        + colorDisk.toBrowserHexValue(Integer.parseInt(gValue.getText().toString()))
                        + colorDisk.toBrowserHexValue(Integer.parseInt(bValue.getText().toString()));
//                fragment_Control = new Fragment_Control(colorDisk.getColorStr());
//                FragmentTransaction ft1 = getActivity().getSupportFragmentManager().beginTransaction();
//                ft1.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//                ft1.replace(R.id.fgm, fragment_Control).addToBackStack(null);
//                ft1.commit();

                mlistener.getRGB(Color.parseColor(colorStr));

            }
        });

        colorDisk=(ColorPicker)view.findViewById(R.id.colorDisk);
        colorDisk.setOnColorBackListener(new ColorPicker.OnColorBackListener() {
            @Override
            public void onColorBack(int a, int r, int g, int b) {
//                tv.setText("R：" + r + "\nG：" + g + "\nB：" + b + "\n" + colorDisk.getColorStr());
//                tv.setTextColor(Color.argb(a, r, g, b));
                Log.d("shit1", "onTextChanged:yo ");
                rValue.setText(String.valueOf(r));
                gValue.setText(String.valueOf(g));
                bValue.setText(String.valueOf(b));
                setButton_Color(colorDisk.getColorStr());
                colorStr=colorDisk.getColorStr();
            }
        });






        return view;
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

    public interface colorPick_Listener {
        // TODO: Update argument type and name
        public int getRGB(int rgbValue);
    }

    public void setListener(colorPick_Listener mlistener){
        this.mlistener = mlistener;
   }





}


