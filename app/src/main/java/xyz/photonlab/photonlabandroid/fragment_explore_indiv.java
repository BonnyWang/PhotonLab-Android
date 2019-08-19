package xyz.photonlab.photonlabandroid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;


public class fragment_explore_indiv extends Fragment implements View.OnTouchListener {

    String link;
    String title;

    WebView wvexplore;
    Button btBack;
    TextView tvTitle;
    ConstraintLayout topBox;
    Button btShare;

    public fragment_explore_indiv(String link, String title) {

        this.link = link;
        this.title = title;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (wvexplore != null)
            wvexplore.clearFocus();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore_indiv_v2, container, false);
        btShare = view.findViewById(R.id.button3);
        wvexplore = view.findViewById(R.id.wvexplore);
        wvexplore.clearCache(true);
        wvexplore.loadUrl(link);
        wvexplore.getSettings().setJavaScriptEnabled(true);
        wvexplore.getSettings().setDomStorageEnabled(true);

        btBack = view.findViewById(R.id.btBackExIndiv);
        btBack.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());

        btShare.setOnClickListener((v) -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "PhotonLab Inc");
            intent.putExtra(Intent.EXTRA_TEXT, wvexplore.getUrl());
            startActivity(Intent.createChooser(intent, "Share"));
        });
        tvTitle = view.findViewById(R.id.tvExIndivTitle);
        tvTitle.setText(title);

        wvexplore.setWebViewClient(new MyWebViewClient());
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

    private class MyWebViewClient extends WebViewClient {
        //todo add some special functions
    }
}