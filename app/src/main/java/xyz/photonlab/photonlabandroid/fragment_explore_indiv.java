package xyz.photonlab.photonlabandroid;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;


public class fragment_explore_indiv extends Fragment {

    String link;
    String title;

    WebView wvexplore;
    Button btBack;
    TextView tvTitle;

    public fragment_explore_indiv(String link, String title) {

        this.link = link;
        this.title = title;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore_indiv, container, false);

        wvexplore = view.findViewById(R.id.wvexplore);
        wvexplore.loadUrl(link);

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

}
