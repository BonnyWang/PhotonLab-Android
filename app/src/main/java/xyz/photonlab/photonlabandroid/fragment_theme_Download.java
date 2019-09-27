package xyz.photonlab.photonlabandroid;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

import xyz.photonlab.photonlabandroid.model.MyTheme;
import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;

public class fragment_theme_Download extends Fragment implements dlRvAdapter.dlListener {


    static final String TAG = "fTheme_DownLoad";
    ArrayList<MyTheme> themeDownload;
    ArrayList<MyTheme> mtheme;
    RecyclerView rv;
    Button btdlDone;

    dlRvAdapter adapter;
    dlRvAdapter.dlListener mlistener = this;

    fdlListener mfdlListener;
    private View view;
    private FragmentActivity mActivity;


    public fragment_theme_Download() {
        this.mfdlListener = theme -> {

        };
        this.mtheme = new ArrayList<>();
    }

    public fragment_theme_Download(fdlListener mfdlListener, ArrayList<MyTheme> mtheme) {
        this.mfdlListener = mfdlListener;
        this.mtheme = mtheme;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_theme__download, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view1, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = getContext();

        rv = view.findViewById(R.id.rvDownloadTheme);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);

        btdlDone = view.findViewById(R.id.btdldone);
        btdlDone.setOnClickListener(v -> mActivity.getSupportFragmentManager().popBackStack());

        themeDownload = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("mtheme");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                GenericTypeIndicator<ArrayList<HashMap<String, Object>>> t = new GenericTypeIndicator<ArrayList<HashMap<String, Object>>>() {
                };
                ArrayList<HashMap<String, Object>> srcData = dataSnapshot.getValue(t);
                if (srcData != null)
                    for (int i = 0; i < srcData.size(); i++) {
                        try {
                            MyTheme item = new MyTheme();
                            item.setCreater(((String) srcData.get(i).get("creator")));
                            item.setMood(((String) srcData.get(i).get("mood")));
                            item.setName(((String) srcData.get(i).get("name")));
                            item.setNumber(((int) ((long) srcData.get(i).get("number"))));

                            ArrayList ag = (ArrayList) srcData.get(i).get("gradientColors");
                            ArrayList av = (ArrayList) srcData.get(i).get("vars");

                            int[] ig = new int[Objects.requireNonNull(ag).size()];
                            int[] iv = new int[Objects.requireNonNull(av).size()];

                            for (int j = 0; j < ig.length; j++) {
                                Log.i("#Color", ag.get(j) + "");
                                try {
                                    ig[j] = Color.parseColor(ag.get(j) + "");
                                } catch (Exception e) {
                                    ig[j] = 0xffffffff;
                                }
                            }
                            for (int j = 0; j < iv.length; j++) {
                                iv[j] = ((int) (long) av.get(j));
                            }
                            item.setGradientColors(ig);
                            item.setVars(iv);
                            themeDownload.add(item);
                        } catch (Exception e) {
                            Log.e("Theme Download", "Server Data Error!");
                        }

                    }
                try {
                    Collections.sort(themeDownload);
                    adapter = new dlLoadedAdapter(themeDownload, mlistener, mtheme, Session.getInstance().isDarkMode(getContext()));
                    rv.setAdapter(adapter);
                    Log.d(TAG, "Value is: ");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        adapter = new dlRvAdapter(themeDownload, mlistener, mtheme, Session.getInstance().isDarkMode(getContext()));
        rv.setAdapter(adapter);


        if (Session.getInstance().isDarkMode(getContext())) {
            view.setBackgroundColor(Theme.Dark.MAIN_BACKGROUND);
            btdlDone.setTextColor(Color.parseColor("#EA7D38"));
            ((TextView) view.findViewById(R.id.tvDownloadTheme)).setTextColor(Theme.Dark.TITLE);
        }

    }

    @Override
    public int dlPosition(int position) {
        Log.d(TAG, "dlPosition: " + position);
        mfdlListener.dlTheme(themeDownload.get(position));
        return position;
    }

    interface fdlListener {
        void dlTheme(MyTheme theme);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }
}
