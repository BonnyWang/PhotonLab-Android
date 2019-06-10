package com.example.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

//Added -Bonny
import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import android.content.IntentFilter;
import android.widget.Toast;


public class Themes extends AppCompatActivity {
    private TextView mTextMessage;
    private CardView mCardView;

    //added for Recycler -Bonny
    Context context;
    List<theme_Class> mtheme;

    //added for setting different color for the gradient -Bonny
    ImageView imageView_Card;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    to_Main();
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText("KKK");
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.navigation_notifications2:
                    mTextMessage.setText(R.string.title_notifications2);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);
        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //added for recycler view -Bonny
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);

        imageView_Card = (ImageView)findViewById(R.id.imageView_Card);

        initializeData();

        RvAdapter adapter = new RvAdapter(mtheme);
        rv.setAdapter(adapter);


    }

    public void to_Main(){
        finish();
    }



    private void initializeData(){
        mtheme = new ArrayList<>();
        mtheme.add(new theme_Class("Spring", new int[] {0xff009e00, 0xfffcee21}));
        mtheme.add(new theme_Class("Fizzy Peach", new int[] {0xfff24645, 0xffebc08d}));
        mtheme.add(new theme_Class("Sky", new int[] {0xff00b7ff, 0xff00ffee}));
        mtheme.add(new theme_Class("Spring", new int[] {0xff009e00, 0xfffcee21}));
        mtheme.add(new theme_Class("Fizzy Peach", new int[] {0xfff24645, 0xffebc08d}));
        mtheme.add(new theme_Class("Sky", new int[] {0xff00b7ff, 0xff00ffee}));
    }


}

