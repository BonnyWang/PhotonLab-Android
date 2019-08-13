package xyz.photonlab.photonlabandroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class FragmentAbout extends Fragment {

    ConstraintLayout policy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        policy = view.findViewById(R.id.clPrivacy);
        policy.setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.photonlab.xyz/privacypolicy.html"))));
        Button btBack = view.findViewById(R.id.backButton_System);
        btBack.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());
    }
}
