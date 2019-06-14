package com.example.myapplication;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.example.myapplication.data.Theme_Info;

import java.util.List;

public class Fragment_Useless extends Fragment {
    List<theme_content_class> items =new Theme_Info().itemlist();
    int[] colorcode;
    String subtitle;
    GradientDrawable gradientDrawable,gradientDrawable2;


    public Fragment_Useless(int[]colorcodes,String title ) {
        this.colorcode=colorcodes;
        this.subtitle=title;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragement_baozha,container,false);
        ListView lv = (ListView)view.findViewById(R.id.info_list) ;
        useless_adapter adapter = new useless_adapter(getActivity(),R.layout.theme_useless_items,items);
        lv.setAdapter(adapter);

        ImageView iv=(ImageView)view.findViewById(R.id.yuanyuan);
        gradientDrawable=new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colorcode);
        gradientDrawable.setShape(GradientDrawable.OVAL);
        iv.setImageDrawable(gradientDrawable);

        TextView tv=(TextView)view.findViewById(R.id.themename);
        tv.setText(subtitle);

        Button bt = (Button)view.findViewById(R.id.setbutton);
        //write something my friend
        gradientDrawable2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colorcode);
        gradientDrawable2.setCornerRadius(10);
        gradientDrawable2.setSize(1000,50);
        bt.setBackground(gradientDrawable2);

        Button back= (Button)view.findViewById(R.id.backButton);
        //write something my friend
        //getActivity().getFragmentManager().popBackStack();  ? 不知道
        return view;
    }

}
