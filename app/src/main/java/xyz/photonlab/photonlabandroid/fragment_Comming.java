package xyz.photonlab.photonlabandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;


public class fragment_Comming extends NormalStatusBarFragment {

    String pageName;
    TextView title;

    Button back;

    public fragment_Comming(String pageName){
        this.pageName = pageName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__comming, container, false);

        title = view.findViewById(R.id.ComingSoon);
        title.setText(pageName);

        back = view.findViewById(R.id.backButton_Coming);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        if (Session.getInstance().isDarkMode(getContext()))
            view.setBackgroundColor(Theme.Dark.MAIN_BACKGROUND);
        return view;
    }

}
