package xyz.photonlab.photonlabandroid;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import xyz.photonlab.photonlabandroid.views.setLayoutView;


public class fragment_layout extends Fragment {

    Button btAddHex;
    setLayoutView msetLayoutView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        msetLayoutView = view.findViewById(R.id.msetLayoutView);

        btAddHex = view.findViewById(R.id.btAddHex);

        btAddHex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msetLayoutView.addHex();
            }
        });

        return view;
    }


}
