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
    Button btDelete;
    Button btRotate;
    Button btBack;
    setLayoutView msetLayoutView;

    float rotation;

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

        btDelete = view.findViewById(R.id.btDelete);

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msetLayoutView.deleteHex();
            }
        });

        btRotate = view.findViewById(R.id.btRotate);
        btRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                msetLayoutView.setRotation(rotation+90);
//                rotation = rotation + 90;
            }
        });

        btBack = view.findViewById(R.id.btBackLayout);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }


}
