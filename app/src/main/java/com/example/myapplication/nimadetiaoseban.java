package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import yuku.ambilwarna.AmbilWarnaDialog;

public class nimadetiaoseban extends AppCompatActivity {
    ConstraintLayout fucklayout;
    int DefaultColor;
    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nimadelayout);
        fucklayout=(ConstraintLayout) findViewById(R.id.layout);
        DefaultColor = ContextCompat.getColor(nimadetiaoseban.this,R.color.colorPrimary);
        button=(Button)findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

    }
  public void openColorPicker(){
      AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, DefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
          @Override
          public void onCancel(AmbilWarnaDialog dialog) {

          }

          @Override
          public void onOk(AmbilWarnaDialog dialog, int color) {
                DefaultColor = color;
                fucklayout.setBackgroundColor(DefaultColor);
          }
      });
      colorPicker.show();
  }
}
