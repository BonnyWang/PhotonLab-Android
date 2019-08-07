package xyz.photonlab.photonlabandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.views.LightStage;


public class fragment_layout extends Fragment {

    Button btAddHex;
    Button btDelete;
    Button btRotate;
    Button btBack;
    Button btNext;
    LightStage msetLayoutView;
    Session session;
    OnSavedLayoutListener listener;

    public void setListener(OnSavedLayoutListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        session = Session.getInstance();
        ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.fragment_layout, container, false);

        msetLayoutView = session.requireLayoutStage(getContext(), false);

        LinearLayout layoutContainer = view.findViewById(R.id.layoutContainer);

        if (msetLayoutView == null)
            msetLayoutView = new LightStage(getContext());

        layoutContainer.addView(msetLayoutView);

        msetLayoutView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        msetLayoutView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

        btAddHex = view.findViewById(R.id.btAddHex);

        btAddHex.setOnClickListener(v -> msetLayoutView.addLight());

        btDelete = view.findViewById(R.id.btDelete);

        btDelete.setOnClickListener(v -> msetLayoutView.deleteLight());

        btRotate = view.findViewById(R.id.btRotate);
        btRotate.setOnClickListener(v -> msetLayoutView.rotateLight());

        btBack = view.findViewById(R.id.btBackLayout);
        btBack.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());

        btNext = view.findViewById(R.id.btNext);
        btNext.setOnClickListener(view1 -> {
            int num = msetLayoutView.getUselessLightNum();
            if (num > 0) {
                Toast.makeText(getContext(), "Layout Error!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (session.saveLayoutToLocal(getContext(), msetLayoutView)) {
                if (listener != null)
                    listener.onSavedLayout(true);
                Toast.makeText(getContext(), "Saved! ", Toast.LENGTH_SHORT).show();
            } else {
                if (listener != null)
                    listener.onSavedLayout(false);
                Toast.makeText(getContext(), "Save Failed!", Toast.LENGTH_SHORT).show();
            }
            if (getActivity() != null)
                getActivity().getSupportFragmentManager().popBackStack();
        });
        return view;
    }

    interface OnSavedLayoutListener {
        void onSavedLayout(boolean saved);
    }

}
