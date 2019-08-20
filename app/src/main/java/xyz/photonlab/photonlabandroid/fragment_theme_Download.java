package xyz.photonlab.photonlabandroid;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_theme_Download.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_theme_Download#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_theme_Download extends Fragment implements dlRvAdapter.dlListener {


    static final String TAG = "fTheme_DownLoad";
    ArrayList<theme_Class> themeDownload;
    ArrayList<theme_Class> mtheme;
    RecyclerView rv;
    Button btdlDone;

    dlRvAdapter adapter;
    dlRvAdapter.dlListener mlistener = this;

    fdlListener mfdlListener;


    public fragment_theme_Download(fdlListener mfdlListener, ArrayList<theme_Class> mtheme) {
        this.mfdlListener = mfdlListener;
        this.mtheme = mtheme;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme__download, container, false);

        rv = (RecyclerView) view.findViewById(R.id.rvDownloadTheme);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        btdlDone = view.findViewById(R.id.btdldone);
        btdlDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        themeDownload = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("mtheme");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                GenericTypeIndicator<ArrayList<theme_Class>> t = new GenericTypeIndicator<ArrayList<theme_Class>>() {
                };
                themeDownload = dataSnapshot.getValue(t);
                adapter = new dlLoadedAdapter(themeDownload, mlistener, mtheme);
                rv.setAdapter(adapter);
                Log.d(TAG, "Value is: ");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        adapter = new dlRvAdapter(themeDownload, mlistener, mtheme);
        rv.setAdapter(adapter);


        if (Session.getInstance().isDarkMode(getContext())) {
            view.setBackgroundColor(Theme.Dark.MAIN_BACKGROUND);
            btdlDone.setTextColor(Color.parseColor("#EA7D38"));
            ((TextView) view.findViewById(R.id.tvDownloadTheme)).setTextColor(Theme.Dark.TITLE);
        }

        return view;
    }

    @Override
    public int dlPosition(int position) {
        Log.d(TAG, "dlPosition: " + position);
        mfdlListener.dlTheme(themeDownload.get(position));
        return position;
    }

    interface fdlListener {
        public theme_Class dlTheme(theme_Class theme);
    }

}
