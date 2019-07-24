package xyz.photonlab.photonlabandroid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;


public class fragment_explore_indiv extends Fragment implements View.OnTouchListener {

    String link;
    String title;

    WebView wvexplore;
    Button btBack;
    TextView tvTitle;
    ConstraintLayout topBox;

    public fragment_explore_indiv(String link, String title) {

        this.link = link;
        this.title = title;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore_indiv, container, false);
        topBox = view.findViewById(R.id.fgexTopBar);
        wvexplore = view.findViewById(R.id.wvexplore);
        wvexplore.loadUrl(link);
        wvexplore.getSettings().setJavaScriptEnabled(true);
        wvexplore.getSettings().setDomStorageEnabled(true);
        wvexplore.setOnTouchListener(this);

        btBack = view.findViewById(R.id.btBackExIndiv);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        tvTitle = view.findViewById(R.id.tvExIndivTitle);
        tvTitle.setText(title);

        return view;
    }


    float origin = 0f;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        view.performClick();
        //allow website to be scrolled
        wvexplore.requestDisallowInterceptTouchEvent(true);
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            origin = motionEvent.getY();
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if (motionEvent.getY() < origin && topBox.getVisibility() == View.VISIBLE) {
                topBox.setVisibility(View.GONE);
            } else if (motionEvent.getY() > origin && topBox.getVisibility() == View.GONE) {
                topBox.setVisibility(View.VISIBLE);
            }
        }
        return false;
    }
}